import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.net.ssl.HttpsURLConnection;	

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SignatureExample {
	private static String baseAddress = "https://eksamen-sas.udir.no";

	public static void main(String[] args) {
		try {
			String cookie = login(baseAddress);
			if (cookie != null && !cookie.isEmpty()) {
				issueRequestWithCookie(
						baseAddress,"/api/ekstern/kandidat?orgno=874546852&periodekode=H-2015",
						cookie);		
				logout(cookie );
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static void logout(String cookie)
			throws MalformedURLException, ProtocolException, IOException{
		
		System.out.println("Now logging out...");
		
		URL obj = new URL(baseAddress + "/api/ekstern/utlogging");
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		// add the cookie
		con.setRequestProperty("Cookie",cookie);
		
		// set content-length to 0
		con.setFixedLengthStreamingMode(0);
		
		// set HTTP verb
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();	
	}
	
	private static String login(String baseAddress)
			throws ParserConfigurationException, SAXException, IOException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			KeyStoreException, CertificateException,
			UnrecoverableEntryException, MarshalException,
			XMLSignatureException, TransformerException {
		
		System.out.println("Requesting autentication cookie");

		String nonce = getNonce();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String timeStamp = formatter.format(new Date());

		String xmlString = String.format("<?xml version='1.0' encoding='UTF-8'?>"
				+ "<ci:ClientIdentification xmlns:xs='http://www.w3.org/2001/XMLSchema' "
				+ "xmlns:ci='http://pas.udir.no/ClientIdentification'>"
				+ "<Skoleorgno>875561162</Skoleorgno> " 
				+ "<Skolenavn>Eksempel skole</Skolenavn> "
				+ "<Brukernavn>skoleadmin</Brukernavn> "
				+ "<Nonce>%s</Nonce> " 
				+ "<TimeStamp>%s</TimeStamp> "
				+ "</ci:ClientIdentification>", nonce, timeStamp);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document xmlDoc = builder.parse(new InputSource(new StringReader(
				xmlString)));

		Document xmlSignature = sign(xmlDoc);

		// Posting to login API
		URL obj = new URL(baseAddress + "/api/ekstern/innlogging");
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(toString(xmlSignature));
		wr.flush();
		wr.close();

		return getAuthCookie(con);
	}

	private static Document sign(Document doc) throws NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, KeyStoreException,
			CertificateException, FileNotFoundException, IOException,
			UnrecoverableEntryException, javax.xml.crypto.MarshalException,
			XMLSignatureException, TransformerException {

		// Create a DOM XMLSignatureFactory that will be used to
		// generate the enveloped signature.
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

		// Create a Reference to the enveloped document (in this case,
		// you are signing the whole document, so a URI of "" signifies
		// that, and also specify the SHA1 digest algorithm and
		// the ENVELOPED Transform.
		Transform transform = fac.newTransform(Transform.ENVELOPED,
				(TransformParameterSpec) null);
		DigestMethod digestMethod = fac
				.newDigestMethod(DigestMethod.SHA1, null);
		Reference ref = fac.newReference("", digestMethod,
				Collections.singletonList(transform), null, null);

		// Create the SignedInfo.
		CanonicalizationMethod canonicalizationMethod = fac
				.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
						(C14NMethodParameterSpec) null);
		SignatureMethod signatureMethod = fac.newSignatureMethod(
				SignatureMethod.RSA_SHA1, null);
		SignedInfo si = fac.newSignedInfo(canonicalizationMethod,
				signatureMethod, Collections.singletonList(ref));

		// Load the KeyStore and get the signing key and certificate.
		String password = "123456";
		String keyAlias = "1";

		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream("UDIR.PAS2.keystore"),password.toCharArray());

		KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks
				.getEntry(keyAlias,
						new KeyStore.PasswordProtection(password.toCharArray()));
		X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

		// Create the KeyInfo containing the X509Data.
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

		// Create a DOMSignContext and specify the RSA PrivateKey and
		// location of the resulting XMLSignature's parent element.
		DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(),
				doc.getDocumentElement());

		// Create the XMLSignature, but don't sign it yet.
		XMLSignature signature = fac.newXMLSignature(si, ki);

		// Marshal, generate, and sign the enveloped signature.
		signature.sign(dsc);

		// Output the resulting document.
		OutputStream os = new FileOutputStream("xmlOut.xml");
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));

		return doc;
	}


	private static String getNonce() throws NoSuchAlgorithmException {
		byte[] nonceBytes = new byte[8];
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(new Date().toString().getBytes());
		random.nextBytes(nonceBytes);

		return DatatypeConverter.printBase64Binary(nonceBytes);
	}

	private static void issueRequestWithCookie(String baseAddress,
			String relativeAddress, String cookie) throws IOException {

		System.out.println("Press enter to try to issue request with the newly obtained cookie");
		System.in.read();

		URL obj = new URL(baseAddress + relativeAddress);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		// add the cookie
		con.setRequestProperty("Cookie",cookie);
		
		// optional default is GET
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}

	private static String getAuthCookie(HttpsURLConnection con) throws IOException {
		int responseCode = con.getResponseCode();
		if (responseCode != 200) {
			System.err.println("Response code: " + responseCode);
			System.err.println("So quitting...");
			System.exit(1);
		}
		String headerField = con.getHeaderField("Set-Cookie");
		String[] cookies = headerField.split(";");
		for (String cookie : cookies) {
			if (cookie.startsWith("FedAuth")) {
				return cookie;
			}
		}

		return null;
	}

	private static String toString(Document doc) {
		try {
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			String returnValue = sw.toString();

			return returnValue;
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}
}