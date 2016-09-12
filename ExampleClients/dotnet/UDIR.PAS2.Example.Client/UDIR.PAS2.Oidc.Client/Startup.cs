using System;
using System.Globalization;
using System.IdentityModel.Tokens;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using IdentityModel.Client;
using Microsoft.IdentityModel.Protocols;
using Microsoft.Owin;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using Owin;
using UDIR.Id.Core;
using UDIR.Id.Test.RP.OpenId.FeatureToggles;

[assembly: OwinStartup(typeof(UDIR.Id.Test.RP.OpenId.Startup))]

namespace UDIR.Id.Test.RP.OpenId
{
    public class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            const string clientSecret = "gibberish";
            const string idSrvBaseAddress = "https://localhost:44301";
            const string idSrvTokenEndpoint = idSrvBaseAddress + "/connect/token";
            const string idSrvUserInfoEndpoint = idSrvBaseAddress + "/connect/userinfo";
            var useBackchannel = new OidcBackchannelFeatureToggle().FeatureEnabled;
            var clientId = useBackchannel ? "udir_oidc_hybrid_test_localhost" : KnownClientIds.Login.PgscClientIds.LocalDotNetClient;
            
            JwtSecurityTokenHandler.InboundClaimTypeMap.Clear();
            app.UseCookieAuthentication(new CookieAuthenticationOptions
            {
                AuthenticationType = CookieAuthenticationDefaults.AuthenticationType, Provider = new CookieAuthenticationProvider
                {
                    OnResponseSignIn = ctx =>
                    {
                        
                    },
                    OnResponseSignOut = ctx =>
                    {
                        
                    },
                    OnResponseSignedIn = ctx =>
                    {
                        
                    },
                    OnException = ctx =>
                    {
                        
                    }
                }
            });
            app.UseOpenIdConnectAuthentication(new OpenIdConnectAuthenticationOptions
            {
                AuthenticationType = "oicd",
                SignInAsAuthenticationType = CookieAuthenticationDefaults.AuthenticationType,
                Authority = idSrvBaseAddress,
                ClientId = clientId,
                Scope = "openid profile udir_pgsc",
                ResponseType = (useBackchannel ? "code " : string.Empty) + "id_token",
                RedirectUri = "https://localhost:44302",
                PostLogoutRedirectUri = "https://localhost:44302/Home/AfterLogout",
                Notifications = new OpenIdConnectAuthenticationNotifications
                {
                    RedirectToIdentityProvider =  n =>
                    {
                        return Task.Run(() =>
                        {
                            switch (n.ProtocolMessage.RequestType)
                            {
                                case OpenIdConnectRequestType.AuthenticationRequest:
                                    if (n.OwinContext.Environment.ContainsKey("IsKandidat"))
                                    {
                                        n.ProtocolMessage.AcrValues = "udir:kandidat";
                                    }
                                    break;
                                case OpenIdConnectRequestType.LogoutRequest:
                                    n.OwinContext.Authentication.User.FindFirst("id_token").IfNotNull(idToken =>
                                    {
                                        n.ProtocolMessage.IdTokenHint = idToken.Value;
                                    });
                                    break;
                                case OpenIdConnectRequestType.TokenRequest:
                                    break;
                                default:
                                    throw new ArgumentOutOfRangeException();
                            }
                        });
                    },
                    MessageReceived = n => { return Task.Run(() => { }); }, SecurityTokenReceived = n => { return Task.Run(() => { }); }, SecurityTokenValidated = n =>
                    {
                        return Task.Run(() =>
                        {
                            var id = n.AuthenticationTicket.Identity;
                            var newId = new ClaimsIdentity(id.AuthenticationType, "uid", "role");
                            id.TransferClaims(c => newId.AddClaim(c), "uid", "idp", "trx", "sid");

                            var idToken = n.ProtocolMessage.IdToken;
                            newId.AddClaim(new Claim("id_token", idToken));
                            newId.AddClaim(new Claim("logout_protect", Guid.NewGuid().ToString()));

                            n.AuthenticationTicket = new AuthenticationTicket(newId, n.AuthenticationTicket.Properties);
                        });
                    },
                    AuthorizationCodeReceived = async n =>
                    {
                        var tokenClient = new TokenClient(idSrvTokenEndpoint, clientId, clientSecret);
                        var tokenResponse = await tokenClient.RequestAuthorizationCodeAsync(n.Code, n.RedirectUri);
                        var userInfoClient = new UserInfoClient(new Uri(idSrvUserInfoEndpoint), tokenResponse.AccessToken);
                        var userInfoResponse = await userInfoClient.GetAsync();
                        var id = new ClaimsIdentity(n.AuthenticationTicket.Identity.AuthenticationType);
                        id.AddClaims(userInfoResponse.GetClaimsIdentity().Claims);
                        id.AddClaim(new Claim("expires_at", DateTime.Now.AddSeconds(tokenResponse.ExpiresIn).ToLocalTime().ToString(CultureInfo.InvariantCulture)));
                        id.AddClaim(new Claim("id_token", n.ProtocolMessage.IdToken));
                        id.AddClaim(new Claim("sid", n.AuthenticationTicket.Identity.FindFirst("sid").Value));
                        n.AuthenticationTicket = new AuthenticationTicket(new ClaimsIdentity(id.Claims, n.AuthenticationTicket.Identity.AuthenticationType), n.AuthenticationTicket.Properties);
                    }
                }
            });
        }
    }

    internal static class ClaimsIdentityExtensions
    {
        public static void TransferClaims(this ClaimsIdentity id, Action<Claim> then, params string[] claimTypes)
        {
            claimTypes.ToList().ForEach(t => id.FindFirst(t).IfNotNull(then));
        }
    }
}
