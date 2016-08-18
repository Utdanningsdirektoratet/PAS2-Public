using System;
using System.Net.Http;
using System.Security.Authentication;
using IdentityModel.Client;

namespace UDIR.PAS2.OAuth.Client
{
	internal class AuthenticatedHttpClientFactory
	{
		private readonly object _semaphore = new object();
		private readonly Uri _resourceServerBase;
		private readonly Action<string> _debugMessageHandler;
		private BearerToken _bearerToken;
		private readonly AuthenticatedClientOptions _options;

		public AuthenticatedHttpClientFactory(AuthenticatedClientOptions options, Uri resourceServerBase, Action<string> debugMessageHandler = null)
		{
			_options = options;
			_resourceServerBase = resourceServerBase;
			_debugMessageHandler = debugMessageHandler ?? (s => { });
		}

		private void FetchBearerTokenIfNecessary()
		{
			lock (_semaphore)
			{
				if (_bearerToken != null && _bearerToken.ExpiryTime >= DateTimeOffset.UtcNow) return;
				if (_bearerToken != null)
				{
					Debug("Bearer token expired at {0:O}. Fetching new one", _bearerToken.ExpiryTime);
				}
				var tokenResponse =
					new TokenClient(_options.AuthorizationServer.TokenEndpoint, _options.ClientId, _options.ClientSecret)
						.RequestClientCredentialsAsync(_options.Scope)
						.Result;

				if (tokenResponse.IsError)
				{
					throw new AuthenticationException(string.Format("Could not aquire access token. Reason:{0}, HttpStatusCode:{1}, HttpReason:{2}",
						tokenResponse.Error, tokenResponse.HttpErrorStatusCode, tokenResponse.HttpErrorReason));
				}
				_bearerToken = new BearerToken(DateTimeOffset.UtcNow.AddSeconds(tokenResponse.ExpiresIn), tokenResponse.AccessToken);
				Debug("Done fetching bearer token. Next expiry at {0:O}", _bearerToken.ExpiryTime);
			}
		}

		public HttpClient GetHttpClient()
		{
			return CreateClient((baseAddress, bearerToken) =>
			{
				var httpClient = new HttpClient();
				httpClient.SetBearerToken(bearerToken.Value);
				httpClient.BaseAddress = baseAddress;
				return httpClient;
			});
		}

		public T CreateClient<T>(Func<Uri, BearerToken, T> ctor)
		{
			FetchBearerTokenIfNecessary();
			return ctor(_resourceServerBase, _bearerToken);
		}

		private void Debug(string message, params object[] args)
		{
			_debugMessageHandler(string.Format(message, args));
		}
	}
}
