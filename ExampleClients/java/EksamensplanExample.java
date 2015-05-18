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
			
			EksamensplanResponse eksamensplaner = (EksamensplanResponse) jc.createUnmarshaller().unmarshal(xml);
			
			System.out.println("Fagkoder i eksamensplanen:");
			for(EksamensplanType eksamensplan : eksamensplaner.getEksamensplaner().getEksamensplan())
			{
				for(EksamenType eksamen : eksamensplan.getEksamener().getEksamen())
				{
					System.out.println(eksamen.getFagkode());
				}
			}
			
			connection.disconnect();		
	}
}