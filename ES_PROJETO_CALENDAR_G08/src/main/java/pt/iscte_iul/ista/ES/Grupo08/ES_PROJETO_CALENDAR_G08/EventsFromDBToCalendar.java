package pt.iscte_iul.ista.ES.Grupo08.ES_PROJETO_CALENDAR_G08;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.davidmoodie.SwingCalendar.CalendarEvent;

/**
 *Class to receive events from MongoDB and return them in the correct format to display.  
 * @author Pedro Luis
 * @version 11/12/2022
 *
 */

public class EventsFromDBToCalendar {
	
	
	int dia, mes, ano, hora_inicio, minutos_inicio, hora_fim, minutos_fim;
	/**
	 * Constructor. 
	 * Initiates all time variables for each Calendar Event with the value 0;
	 */
	public EventsFromDBToCalendar() {
		dia = 0; mes = 0; ano = 0; hora_inicio = 0; minutos_inicio = 0; hora_fim = 0; minutos_fim = 0;
	}
	
	/**
	 * Put all received events into an Arraylist to build the calendar.
	 * @param events Array of JSONObjects given by the readFromDB() method in DB_Opreations class.
	 * @param color Color to display Calendar Events in the UI.
	 * @return Returns an Arraylist of CalendarEvent that will be used in the UI class to display every event.
	 */
	public ArrayList<CalendarEvent> JSonObjectToCalendarEvent(JSONArray events, Color color){
		JSONObject obj = new JSONObject();
		
		String summary = null;
		ArrayList<CalendarEvent> calendarEventList = new ArrayList<>();
	
		for(int i = 0; i < events.size(); i++) {
			summary = summary(events, obj, summary, i);
			obj = (JSONObject) events.get(i);
			getDayAndTime((String) obj.get("START"), true);
			getDayAndTime((String) obj.get("END"), false);
			
			calendarEventList.add(new CalendarEvent(LocalDate.of(ano, mes, dia), LocalTime.of(hora_inicio, minutos_inicio), LocalTime.of(hora_fim, minutos_fim), summary, color));
		}
		
		return calendarEventList;
	}

	private String summary(JSONArray events, JSONObject obj, String summary, int i) {
		obj = (JSONObject) events.get(i);
		summary = (String) obj.get("SUMMARY");
		return summary;
	}
	
	private void getDayAndTime(String string, boolean startDate) {
		String[] array = string.split("T");
		List<String> hour = usingSubstringMethod(array[1], 2);
		
		if(startDate) {
			List<String> dateList = usingSubstringMethod(array[0], 4);
			ano = Integer.parseInt(dateList.get(0));
			List<String> monthAndDay = usingSubstringMethod(dateList.get(1), 2);
			mes = Integer.parseInt(monthAndDay.get(0));
			dia = Integer.parseInt(monthAndDay.get(1));
			hora_inicio = Integer.parseInt(hour.get(0));
			minutos_inicio = Integer.parseInt(hour.get(1));
		
		} else {
			hora_fim = Integer.parseInt(hour.get(0));
			minutos_fim = Integer.parseInt(hour.get(1));
		}		
	}
	
	private List<String> usingSubstringMethod(String text, int n) {
	    List<String> results = new ArrayList<>();
	    int length = text.length();

	    for (int i = 0; i < length; i += n) {
	        results.add(text.substring(i, Math.min(length, i + n)));
	    }

	    return results;
	}

}
