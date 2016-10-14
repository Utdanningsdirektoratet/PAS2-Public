using System;
using System.Configuration;
using System.Net;
using UDIR.PAS2.OAuth.Lib;

namespace UDIR.PAS2.OAuth.Client
{
	internal class Program
	{
		static void Main(string[] args)
		{
			var clientOptions = new AuthenticatedClientOptions
			{
				AuthorizationServer = new AuthorizationServer(ConfigurationManager.AppSettings["IdSrv"]),
				ClientId = ConfigurationManager.AppSettings["ClientId"],
				ClientSecret = ConfigurationManager.AppSettings["ClientSecret"],
				Scope = ConfigurationManager.AppSettings["ApiScope"]
			};

			var factory = new AuthenticatedHttpClientFactory(clientOptions, new Uri(ConfigurationManager.AppSettings["ApiUri"]));

			try
			{
				var client = factory.GetHttpClient();
				var response = client.GetAsync("/api/ping").GetAwaiter().GetResult();
				if (response.StatusCode == HttpStatusCode.OK)
				{
					Console.WriteLine("Successfully autenticated with IdSrv and sucessfully used bearer token to call API");
				}
			}
			catch (Exception e)
			{
				Console.WriteLine(e.Message);
			}

		}
	}
}
