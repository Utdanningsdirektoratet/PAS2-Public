using System;
using System.Configuration;
using System.IO;
using System.Reflection;
using System.Security.Cryptography.X509Certificates;
using System.Security.Cryptography.Xml;
using System.Xml;

namespace UDIR.PAS2.Example.Client.Extensions
{
	internal static class XmlDocumentExtension
	{
		public static void Sign(this XmlDocument xmlDocument)
		{
			var certificate = ConfigurationManager.AppSettings["certificateToUse"];

			var password = ConfigurationManager.AppSettings["certificatePassword"] ?? "";

			using (var stream = Assembly.GetExecutingAssembly()
				.GetManifestResourceStream(certificate))
			{
				if (stream == null) throw new ArgumentException("Invalid certificate. Make sure you reference an embeded certificate");

				var bytes = new byte[stream.Length];
				stream.Read(bytes, 0, bytes.Length);
				var signingCert = new X509Certificate2(bytes,password);
				var signedXml = new SignedXml(xmlDocument);
				var keyInfo = new KeyInfo();
				var reference = new Reference { Uri = string.Empty };

				keyInfo.AddClause(new KeyInfoX509Data(signingCert));
				signedXml.SigningKey = signingCert.PrivateKey;
				signedXml.KeyInfo = keyInfo;
				signedXml.SignedInfo.CanonicalizationMethod = SignedXml.XmlDsigExcC14NTransformUrl;
				
				reference.AddTransform(new XmlDsigEnvelopedSignatureTransform());
				signedXml.AddReference(reference);
				signedXml.ComputeSignature();
				xmlDocument.DocumentElement.IfNotNull(
					elem => elem.AppendChild(xmlDocument.ImportNode(signedXml.GetXml(), true)));
			}
		}

		public static string ConvertToString(this XmlDocument payload)
		{
			using (var stringWriter = new StringWriter())
			using (var xmlTextWriter = XmlWriter.Create(stringWriter))
			{
				payload.WriteTo(xmlTextWriter);
				xmlTextWriter.Flush();
				return stringWriter.GetStringBuilder().ToString();
			}
		}
	}
}
