using System.Web;
using System.Web.Mvc;

namespace UDIR.Id.Test.RP.OpenId
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
        }
    }
}
