package pt.iscte_iul.ista.ES.Grupo08.ES_PROJETO_CALENDAR_G08;


import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class URLInfoToDB {

	public static void main(String[] args) {
		icsToDB();
		JSONArray output = readFromDB();
		System.out.println(output);
	}

	public static void icsToDB() {
		try {
			URL url = new URL("https://fenix.iscte-iul.pt/publico/publicPersonICalendar.do?method=iCalendar&username=pmaal1@iscte.pt&password=4nW90X1wHzGP2YQc5ardt24MEz9hEACP0uss6KwnUXgO76bZcF2NLXjzdmqaF738FVbA9Uhu3ADP5pAMVBkftzHfDvzoMBMe5jPdWVRboFdCpfW02WbnAnSN6eWkeGd7");

			Scanner s = new Scanner(url.openStream());

			try {
				MongoClient mongodb = new MongoClient("localhost", 27017);
				MongoDatabase database = mongodb.getDatabase("ProjetoES_DB");
				MongoCollection<Document> coll = database.getCollection("EventosPedro");
				
			
				String line = s.nextLine();
				while (s.hasNext()) {
					if(line.contains("DTSTART")) {
						Document doc = new Document();
					
						doc.put("START", StringUtils.substringAfter(line, ":"));
						line = s.nextLine();
						doc.put("END", StringUtils.substringAfter(line, ":"));
						line = s.nextLine();
						doc.put("SUMMARY", StringUtils.substringAfter(line, ":"));

						//Creates an event object that contains de json object that contains the details
						
						coll.insertOne(doc);

						
						line = s.nextLine();  

					} else line = s.nextLine();

				}
				
			mongodb.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray readFromDB() {
		JSONArray calendar = new JSONArray();
		JSONParser parser = new JSONParser();  
		 
		try {
			MongoClient mongodb = new MongoClient("localhost", 27017);
			MongoDatabase database = mongodb.getDatabase("ProjetoES_DB");
			MongoCollection<Document> coll = database.getCollection("EventosPedro");
			
			MongoCursor<Document> cursor = coll.find().iterator();
			
			try {
				while(cursor.hasNext()) {
					String event = cursor.next().toJson();
					JSONObject json = (JSONObject) parser.parse(event);
					calendar.add(json);
					
				}
			} finally {
				cursor.close();
			}
			
			mongodb.close();
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return calendar;
		
		
	}
	
}