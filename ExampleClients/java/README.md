### Java eksempelimplementasjon
I denne mappen finner man et eksempel på hvordan man kan bruke det eksterne APIet eksponert av PAS.

### Hvordan bruke denne eksempelapplikasjonen?
- Naviger til mappen der du har sjekket ut koden
- Kompiler programmet med kommandoen `javac.exe SignatureExample.java`
- Kjør programmet med kommandoen `java.exe SignatureExample`

## Generere klasser fra XSD
1. Hent ned XSD fra {url til testmiljø}~/api/ekstern/skjema
2. Pakk ut zip filen i en selvvalgt mappe
3. Gå inn i den utpakkede mappen
4. Opprett en ny mappe som heter src
5. Opprette pakke for pamelding ved å kjøre følgende kommando:
   xjc -d src -p udir.types.pamelding ApiError.xsd Pamelding.xsd PameldingReference.xsd PameldingReport.xsd PameldteKandidater.xsd
6. I den generere pakken åpne package-info.java og modifisèr den til å inneholde namespace prefix (f.eks):

```java
@javax.xml.bind.annotation.XmlSchema(namespace = "http://pas.udir.no/Pamelding", 
xmlns = { 
		@XmlNs(namespaceURI = "http://pas.udir.no/Pamelding", prefix = "pa"),		
		@XmlNs(namespaceURI = "http://pas.udir.no/CommonTypes", prefix = "ct"),		
	}, 
elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)

package udir.types.pamelding;
import javax.xml.bind.annotation.XmlNs;
```

7. Opprette pakke for eksamensplan ved å kjøre følgende kommando:
   xjc -d src -p udir.types.eksamensplan Eksamensplan.xsd
8. I den generere pakken åpne package-info.java og modifisèr den til å inneholde namespace prefix (f.eks):
```java

@javax.xml.bind.annotation.XmlSchema(namespace = "http://pas.udir.no/Eksamensplan", 
xmlns = {		
		@XmlNs(namespaceURI = "http://pas.udir.no/Eksamensplan", prefix = "ep"),
		@XmlNs(namespaceURI = "http://pas.udir.no/CommonTypes", prefix = "ct")		
	},
elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
package udir.types.eksamensplan;

import javax.xml.bind.annotation.XmlNs;

```

9. Opprette pakke for client identification ved å kjøre følgende kommando:
   xjc -d src -p udir.types.clientidentification Login.xsd
10. I den generere pakken åpne package-info.java og modifisèr den til å inneholde namespace prefix (f.eks):

```java
@javax.xml.bind.annotation.XmlSchema(namespace = "http://www.w3.org/2000/09/xmldsig#", 
xmlns = {		
		@XmlNs(namespaceURI = "http://pas.udir.no/ClientIdentification", prefix = "ci"),
		@XmlNs(namespaceURI = "http://www.w3.org/2000/09/xmldsig#", prefix="ds")
	},
elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)

package udir.types.clientidentification;
import javax.xml.bind.annotation.XmlNs;

```
