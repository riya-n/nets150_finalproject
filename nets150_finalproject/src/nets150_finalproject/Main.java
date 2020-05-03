package nets150_finalproject;

import java.util.*;
import org.json.simple.*;

public class Main {

    public static void main(String[] args) {
        String origin = "PHL";
        String destination = "MCO";
        String date = "2020-06-01";
        ArrayList<String> airports = new ArrayList<String>();
        airports.add("SFO");
        airports.add("JFK");
        airports.add("DFW");
        airports.add("CLT");
        airports.add("PHL");
        airports.add("MIA");
        airports.add("VPS");
        airports.add("BOS");
        airports.add("ATL");
        airports.add("MCO");
        airports.add("IAD");
        
        
        Graph g = new Graph(origin, destination, date, airports);
        JSONArray route = g.getCheapestRoute();
        if (route == null) {
            System.out.println("Error: no flight routes from " + origin + " to " + destination + " on " + date);
        } else {
            System.out.println("-----");
            System.out.println("Route: ");
            for (int i = 0; i < route.size(); i++) {
                JSONObject flight = (JSONObject) route.get(i);
                String start = (String) flight.get("Origin");
                String end = (String) flight.get("Destination");
                double price = (double) flight.get("Price");
                String airline = (String) flight.get("Airline");
                System.out.println("    " + start + " to " + end + " for " + price + " on " + airline);
            }
            
        }

    }

}
