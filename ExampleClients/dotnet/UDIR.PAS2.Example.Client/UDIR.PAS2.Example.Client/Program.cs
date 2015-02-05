using System;
using System.Net;
using System.Net.Http;
using System.Security.Cryptography;
using System.Threading.Tasks;
using System.Xml;
using UDIR.PAS2.Example.Client.Extensions;

namespace UDIR.PAS2.Example.Client
{
    class Program
    {
        static void Main(string[] args)
        {
            const string baseAddress = "https://eksamen-tst2.udir.no/";

            //obtain cookie by logging in
            var cookie = Login(baseAddress);

            //Use the cookie to issue a request and display result
            IssueRequestWithCookie(cookie, baseAddress, "/api/ekstern/skoler/1234/prøveperioder/432/kandidater");

            Console.ReadLine();
        }

        private static void IssueRequestWithCookie(Cookie cookie, string baseAddress, string relativeAddress)
        {
            Console.WriteLine("Press enter to try to issue request with the newly obtained cookie");
            Console.ReadLine();

            var cookieContainer = new CookieContainer();

            if(cookie != null)
                cookieContainer.Add(cookie);

            var handler = new WebRequestHandler
            {
                CookieContainer = cookieContainer,
                UseCookies = true,
                UseDefaultCredentials = true
            };

            using (var client = new HttpClient(handler))
            {
                client.BaseAddress = new Uri(baseAddress);
                var response = client.GetAsync(relativeAddress).Result;

                System.Console.WriteLine(response);
                System.Console.WriteLine(response.Content.ReadAsStringAsync().Result);
            }
        }

        private static Cookie Login(string baseAddress)
        {
            var xmlSignature = new XmlDocument(){PreserveWhitespace = true};
            using (var rng = new RNGCryptoServiceProvider())
            {
                var nonceBytes = new byte[8];
                rng.GetBytes(nonceBytes);

                var nonce = Convert.ToBase64String(nonceBytes);
                var timeStamp = DateTime.Now.ToString("s");

                xmlSignature.LoadXml(
                    string.Format(@"<ci:ClientIdentification 
                        xmlns:xs='http://www.w3.org/2001/XMLSchema' 
                        xmlns:ci='http://pas.udir.no/ClientIdentification'>
                    <OrgNr>875561162</OrgNr>    
                    <User>skoleadmin</User>
                    <Nonce>{0}</Nonce>
                    <TimeStamp>{1}</TimeStamp>                                  
                  </ci:ClientIdentification>", nonce, timeStamp));
            }
            
            xmlSignature.Sign();

            var signature = xmlSignature.ConvertToString();

            var handler = new WebRequestHandler
            {
                CookieContainer = new CookieContainer(),
                UseCookies = true,
                UseDefaultCredentials = true
            };

            using (var client = new HttpClient(handler))
            {
                client.BaseAddress = new Uri(baseAddress);
                Task.WaitAll(client.PostAsync("/api/ekstern/innlogging", new StringContent(signature)));

                var allcookies = handler.CookieContainer.GetCookies(new Uri(baseAddress));

                return allcookies["FedAuth"];
            }
        }
    }
}
