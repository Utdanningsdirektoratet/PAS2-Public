using System;
using System.Configuration;
using System.Net;
using System.Windows.Forms;
using UDIR.PAS2.OAuth.Lib;

namespace UDIR.PAS2.OAuth.Client
{
	internal class Program
	{
		[STAThread]
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
				var bearerToken = client.DefaultRequestHeaders.Authorization;

				var response = client.GetAsync("/api/ping").GetAwaiter().GetResult();
				if (response.StatusCode == HttpStatusCode.OK)
				{
					Clipboard.SetText(bearerToken.ToString());
					Console.WriteLine("Authorization header er kopiert til utklipstavlen. Trykk en tast for å lukke dette vinduet");
				}
				else
				{
					Console.WriteLine("Klarte ikke å hente Bearertoken");
				}
				Console.ReadLine();
			}
			catch (Exception e)
			{
				Console.WriteLine(e.Message);
			}

		}
	}
}
