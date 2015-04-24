using System;
using System.Configuration;
using System.Net;
using System.Net.Http;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml;
using UDIR.PAS2.Example.Client.Extensions;

namespace UDIR.PAS2.Example.Client
{
    class Program
    {
        [STAThread]
        static void Main(string[] args)
        {
             var baseAddress = ConfigurationManager.AppSettings["environmenturl"];

            //obtain cookie by logging in
            var cookie = Login(baseAddress);
            Clipboard.SetText(cookie.ToString());

            Console.WriteLine("Cookie er kopiert til utklipstavlen. Trykk en tast for å lukke dette vinduet");

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

                Console.WriteLine(response);
                Console.WriteLine(response.Content.ReadAsStringAsync().Result);
            }
        }

        private static Cookie Login(string baseAddress)
        {
            var xmlSignature = new XmlDocument{PreserveWhitespace = true};
            using (var rng = new RNGCryptoServiceProvider())
            {
                var nonceBytes = new byte[8];
                rng.GetBytes(nonceBytes);

                var nonce = Convert.ToBase64String(nonceBytes);
                var timeStamp = DateTime.Now.ToString("s");

                xmlSignature.LoadXml(
                    string.Format(@"<?xml version='1.0' encoding='UTF-8'?>
                    <ci:ClientIdentification 
                        xmlns:xs='http://www.w3.org/2001/XMLSchema' 
                        xmlns:ci='http://pas.udir.no/ClientIdentification'>
                    <Skoleorgno>875561162</Skoleorgno>    
                    <Skolenavn>En skole</Skolenavn>
                    <Brukernavn>skoleadmin</Brukernavn>
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
