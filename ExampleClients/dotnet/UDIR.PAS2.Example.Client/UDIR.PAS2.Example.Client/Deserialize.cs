using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace UDIR.PAS2.Example.Client
{
	public static class Deserialize
	{

		public static void CanValidatePameldteKandidater()
		{
			// Arrange

			// Set up the namsespaces
			XmlReaderSettings settings = new XmlReaderSettings();
			settings.Schemas.Add("http://pas.udir.no/CommonTypes", @"Xsd\CommonTypes.xsd");
			settings.Schemas.Add("http://pas.udir.no/Karakteroversikt", @"Xsd\Karakteroversikt.xsd");
			//settings.Schemas.Add("http://pas.udir.no/Pamelding", @"Xsd\Pamelding.xsd");
			settings.Schemas.Add("http://pas.udir.no/Pamelding", @"Xsd\PameldteKandidater.xsd");
			settings.Schemas.Add("http://pas.udir.no/Eksamensplan", @"Xsd\Eksamensplan.xsd");
			settings.ValidationType = ValidationType.Schema;


			// Create an XmlReader using the defined namespaces
			XmlReader reader = XmlReader.Create(new XmlTextReader(@"TestData\PameldteKandidater.xml"), settings);
			XmlDocument document = new XmlDocument();

			// Act & assert
			document.Load(reader);

		}
	}
}
