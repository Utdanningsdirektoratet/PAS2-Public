namespace UDIR.PAS2.OAuth.Client
{
	internal class AuthenticatedClientOptions
	{
		public string ClientId { get; set; }

		public string ClientSecret { get; set; }

		public string Scope { get; set; }

		public AuthorizationServer AuthorizationServer { get; set; }
	}
}
