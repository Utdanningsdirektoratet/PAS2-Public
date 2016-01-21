using System;
using System.Configuration;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Security.Cryptography;
using System.Text;
using System.Windows.Forms;
using System.Xml;
using System.Xml.Serialization;
using UDIR.PAS2.Example.Client.Contracts;
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
                var timeStamp = DateTime.Now;
                var clientIdentification = new ClientIdentification
                {
                    Skoleorgno = ConfigurationManager.AppSettings["skoleorgno"],
                    Skolenavn = ConfigurationManager.AppSettings["skolenavn"],
                    Brukernavn = ConfigurationManager.AppSettings["brukernavn"],
                    Nonce = nonce,
                    TimeStamp = timeStamp
                };
                
                string theSerializedString;
                using (var ms = new MemoryStream())
                {
                    var xmlWriterSettings = new XmlWriterSettings()
                    {
                        CloseOutput = false,
                        Encoding = Encoding.UTF8,
                        OmitXmlDeclaration = false,
                        Indent = true
                    };
                    using (var xw = XmlWriter.Create(ms, xmlWriterSettings))
                    {
                        var xmlSerializer = new XmlSerializer(typeof(ClientIdentification));
                        xmlSerializer.Serialize(xw, clientIdentification);
                    }

                    theSerializedString = Encoding.UTF8.GetString(ms.ToArray());

                    //remove BOM
                    var byteOrderMarkUtf8 = Encoding.UTF8.GetString(Encoding.UTF8.GetPreamble());
                    if (theSerializedString.StartsWith(byteOrderMarkUtf8))
                    {
                        theSerializedString = theSerializedString.Remove(0, byteOrderMarkUtf8.Length);
                    }
                }

                
                xmlSignature.LoadXml(theSerializedString);
            }
            
            xmlSignature.Sign();

            var signature = xmlSignature.ConvertToString();
            //var validpayload = Convert.ToBase64String(Encoding.UTF8.GetBytes(signature));
            var handler = new WebRequestHandler
            {
                CookieContainer = new CookieContainer(),
                UseCookies = true,
                UseDefaultCredentials = true
            };

            using (var client = new HttpClient(handler))
            {
                client.BaseAddress = new Uri(baseAddress);
                var response = client.PostAsync("/api/ekstern/innlogging", new StringContent(signature)).Result;
                if (!response.IsSuccessStatusCode)
                {
                    Console.Error.WriteLine("Response: " + response.StatusCode);
                    Console.Error.WriteLine("So quitting...");
                    Environment.Exit(1);
                }

                var allcookies = handler.CookieContainer.GetCookies(new Uri(baseAddress));

                return allcookies["FedAuth"];
            }
        }
    }
}
