using System;

namespace UDIR.PAS2.OAuth.Client
{
	internal class AuthorizationServer
	{
		public AuthorizationServer(Uri baseAddress)
		{
			BaseAddress = baseAddress;
			TokenEndpoint = new Uri(baseAddress, "/connect/token").ToString();
		}

		public AuthorizationServer(string baseAddress) : this(new Uri(baseAddress))
		{
		}

		public string TokenEndpoint { get; }
		public Uri BaseAddress { get; }
	}
}
