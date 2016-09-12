using System;
using System.Configuration;
using System.IdentityModel.Tokens;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using IdentityModel;
using Microsoft.IdentityModel.Protocols;
using Microsoft.Owin;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using Owin;
using UDIR.PAS2.OAuth.Lib;

[assembly: OwinStartup(typeof(UDIR.Id.Test.RP.OpenId.Startup))]

namespace UDIR.Id.Test.RP.OpenId
{
    public class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            var clientOptions = new AuthenticatedClientOptions
            {
                AuthorizationServer = new AuthorizationServer(ConfigurationManager.AppSettings["IdSrv"]),
                ClientId = ConfigurationManager.AppSettings["ClientId"],
                Scope = "openid profile"
            };

            JwtSecurityTokenHandler.InboundClaimTypeMap.Clear();
            app.UseCookieAuthentication(new CookieAuthenticationOptions
            {
                AuthenticationType = CookieAuthenticationDefaults.AuthenticationType
            });
            app.UseOpenIdConnectAuthentication(new OpenIdConnectAuthenticationOptions
            {
                AuthenticationType = OpenIdConnectAuthenticationDefaults.AuthenticationType,
                SignInAsAuthenticationType = CookieAuthenticationDefaults.AuthenticationType,
                Authority = clientOptions.AuthorizationServer.BaseAddress.ToString(),
                ClientId = clientOptions.ClientId,
                Scope = clientOptions.Scope,
                ResponseType = OidcConstants.TokenTypes.IdentityToken,
                RedirectUri = "https://localhost:44392",
                PostLogoutRedirectUri = "https://localhost:44392/Home/AfterLogout",
                Notifications = new OpenIdConnectAuthenticationNotifications
                {
                    RedirectToIdentityProvider = n =>
                   {

                       switch (n.ProtocolMessage.RequestType)
                       {
                           case OpenIdConnectRequestType.LogoutRequest:
                               var idToken = n.OwinContext.Authentication.User.FindFirst(OidcConstants.TokenTypes.IdentityToken);
                               if (idToken != null)
                               {
                                   n.ProtocolMessage.IdTokenHint = idToken.Value;
                               }
                               break;
                           case OpenIdConnectRequestType.TokenRequest:
                               break;
                           case OpenIdConnectRequestType.AuthenticationRequest:
                               break;
                           default:
                               throw new ArgumentOutOfRangeException();
                       }
                       return Task.FromResult(0);
                   },
                    SecurityTokenValidated = n =>
                    {
                        var id = n.AuthenticationTicket.Identity;
                        var newId = new ClaimsIdentity(id.AuthenticationType, "uid", "role");
                        id.TransferClaims(c => newId.AddClaim(c), "uid", "idp", "trx", "sid");

                        var idToken = n.ProtocolMessage.IdToken;
                        newId.AddClaim(new Claim(OidcConstants.TokenTypes.IdentityToken, idToken));
                        newId.AddClaim(new Claim("logout_protect", Guid.NewGuid().ToString()));

                        n.AuthenticationTicket = new AuthenticationTicket(newId, n.AuthenticationTicket.Properties);
                        return Task.FromResult(0);
                    }
                }
            });
        }
    }

    internal static class ClaimsIdentityExtensions
    {
        public static void TransferClaims(this ClaimsIdentity id, Action<Claim> then, params string[] claimTypes)
        {
            claimTypes.ToList().ForEach(t =>
            {
                var c = id.FindFirst(t);
                if (c != null) then(c);
            });
        }
    }
}
