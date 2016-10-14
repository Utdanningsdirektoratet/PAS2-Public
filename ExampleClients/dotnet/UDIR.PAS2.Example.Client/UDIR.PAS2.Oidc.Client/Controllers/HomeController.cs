using System.Web;
using System.Web.Mvc;
using Microsoft.Ajax.Utilities;
using Microsoft.Owin.Security.Cookies;
using System.Security.Claims;

namespace UDIR.Id.Test.RP.OpenId.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        [Authorize]
        public ActionResult Beskyttet()
        {
            ViewBag.Message = "Beskyttet";

            return View();
        }

        [AuthorizeKandidat]
        public ActionResult ClaimsKandidat()
        {
            ViewBag.Message = "Kandidat";

            return View();
        }

        public ActionResult Logout(string spoofProtection)
        {
            if (Request.IsAuthenticated)
            {
                AntiSpoof(spoofProtection);
                var ctx = Request.GetOwinContext();
                ctx.Authentication.SignOut();
                return RedirectToAction("Logout");
            }

            return View();
        }

        public ActionResult LogoutLocally(string spoofProtection) {
            if (Request.IsAuthenticated)
            {
                AntiSpoof(spoofProtection);
                var ctx = Request.GetOwinContext();
                ctx.Authentication.SignOut(CookieAuthenticationDefaults.AuthenticationType);
            }
            return RedirectToAction("AfterLogout");
        }

        public ActionResult LogoutWithIFrame(string spoofProtection)
        {
            AntiSpoof(spoofProtection);
            ViewBag.Spoof = spoofProtection;
            return View();
        }

        private static void AntiSpoof(string spoofProtection) {
            var claimsIdentity = ClaimsPrincipal.Current.Identity as ClaimsIdentity;
            var logoutClaim = claimsIdentity?.FindFirst("logout_protect").Value;
            if (spoofProtection != logoutClaim)
            {
                throw new System.Exception("Spoof alert");
            }
        }

        public void SignoutCleanup(string sid)
        {
            if (!Request.IsAuthenticated) return;
    
            Request.GetOwinContext().Authentication.User.FindFirst("sid").IfNotNull(c =>
            {
                if (c.Value == sid)
                {
                    Request.GetOwinContext().Authentication.SignOut("Cookies");
                }
            });
        }

        public ActionResult AfterLogout()
        {
            return View();
        }
    }

    public class AuthorizeKandidatAttribute : AuthorizeAttribute
    {
        protected override bool AuthorizeCore(HttpContextBase httpContext)
        {
            var isAuthorized = base.AuthorizeCore(httpContext);
            if (!isAuthorized)
            {
                httpContext.Request.GetOwinContext().Environment.Add("IsKandidat", true);
            }
            return isAuthorized;
        }
    }
}