import java.net.*;
import udir.types.eksamensplan.*;
import javax.xml.bind.*;
import java.io.*;

public class EksamensplanExample {
	
	private static String baseAddress = "https://eksamen-sas.udir.no";

	public static void main(String[] args) throws Exception{		
			
			String uri = baseAddress + "/api/ekstern/eksamensplan";
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");

			JAXBContext jc = JAXBContext.newInstance(EksamensplanResponse.class);
			InputStream xml = connection.getInputStream();			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(xml));
			String line = null;
			while((line = in.readLine()) != null) {
  				System.out.println(line);
			}
			
			connection.disconnect();		
	}
}