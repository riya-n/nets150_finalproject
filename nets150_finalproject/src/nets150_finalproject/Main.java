package nets150_finalproject;

import java.util.*;
import org.json.simple.*;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        
        System.out.println("Please enter the type of place you wish to go using the following keywords: ");
        System.out.println("museums, beach, historical, nightlife, sports, restaurants, outdoor");
        // assuming the user enters the data correctly
        String keyword = in.nextLine();

        DataParser db = new DataParser();
        ArrayList<String> optcities;
        optcities = db.cityOptions(keyword);
        
        System.out.println("Here are the places you can visit in the US of that type:");
        for (String element: optcities) {
            System.out.println(element);
        }
        System.out.println("Please choose one of the places to go to.");
        
        String destCity = in.nextLine();
        String destination = db.cityAirportMap.get(destCity);
        
        System.out.println("Please enter the airport you wish to fly out from.");
        String origin = in.nextLine();
        
        System.out.println("Please enter the date you would like to fly on (yyyy-mm-dd).");
        String date = in.nextLine();
        
        ArrayList<String> airports = db.getAllAirports();
        
        Graph g = new Graph(origin, destination, date, airports);
        JSONArray route = g.getCheapestRoute();
        if (route == null) {
            System.out.println("Error: no flight routes from " + origin + " to " + destination + " on " + date);
        } else {
            System.out.println("-----");
            System.out.println("Best Route: ");
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
