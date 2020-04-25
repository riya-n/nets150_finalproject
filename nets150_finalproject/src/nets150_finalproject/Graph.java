package nets150_finalproject;

import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Graph {
    
    private ArrayList<Node> graph;
    private String origin; // airport code
    private String destination; // airport code
    private String date; // on which the person first leaves in yyyy-mm-dd format
    
    public class Node {
        private ArrayList<Edge> outEdges;
        private String airportCode; // e.g., SFO
//        private String countryCode; // e.g., US - incase we decide to do word
        
        Node(String airportCode) {//, String countryCode) {
            this.outEdges = new ArrayList<Edge>();
            this.airportCode = airportCode;
//            this.countryCode = countryCode;
        }
        
        public void addOutEdge(Edge v) {
            // don't add the edge if it already exists
            int i = v.getNodeIndex();
            boolean contains = false;
            for (Edge e : outEdges) {
                if (e.getNodeIndex() == i) {
                    contains = true;
                    break;
                }
            }
            
            if (!contains) {
                this.outEdges.add(v);
            }

        }
        
        public ArrayList<Edge> getOutEdges() {
            return this.outEdges;
        }
        
        public String getAirport() {
            return this.airportCode;
        }
        
//        public String getCountry() {
//            return this.countryCode;
//        }

    }
    
    public class Edge {
        private int v; // reference to the airport's index in the graph arraylist
        private double price; // min price of flight
        private String airlineName; // e.g., Alaska Airlines
        // TODO: add departure date and time or flight time
        
        Edge(int v, double weight, String airlineName) {
            this.v = v;
            this.price = weight;
            this.airlineName = airlineName;
        }
        
        public int getNodeIndex() {
            return this.v;
        }
        
        public double getPrice() {
            return this.price;
        }
        
        public String getAirline() {
            return this.airlineName;
        }
    }
    
    public Graph(String origin, String destination, String date, ArrayList<String> airports) {
        graph = new ArrayList<Node>();
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        graph.add(new Node(origin)); // this way origin is at index 0
        for (String airport : airports) {
            if (!airport.equals(origin) && !airport.equals(destination)) {
                graph.add(new Node(airport));
            }
        }
        graph.add(new Node(destination)); // this way destination is at index graph.size - 1
    }
    
    public int getSize() {
        return graph.size();
    }
    
    public void constructGraph() {
        // find the direct flights to all other airports from origin airport
        Node originAirport = graph.get(0);
        for (int i = 1; i < graph.size(); i++) {
            Node stop1Ariport = graph.get(i);
            String stop1 = stop1Ariport.getAirport();
            JSONObject flight1 = getAPIData(this.origin, stop1, this.date);
            if (flight1 != null) {
                double price1 = (double) flight1.get("price");
                String airline1 = (String) flight1.get("airline");
                Edge e1 = new Edge(i, price1, airline1);
                originAirport.addOutEdge(e1);
                // find the direct flights to all other airports (except to origin airport) from this airport
                for (int j = 1; j < graph.size(); j++) {
                    Node stop2Airport = graph.get(j);
                    String stop2 = stop2Airport.getAirport();
                    if (!stop1.equals(this.destination) && !stop2.equals(stop1)) {
                        JSONObject flight2 = getAPIData(stop1, stop2, this.date);
                        if (flight2 != null) {
                            double price2 = (double) flight2.get("price");
                            String airline2 = (String) flight2.get("airline");
                            Edge e2 = new Edge(j, price2, airline2);
                            stop1Ariport.addOutEdge(e2);
                            // find the direct flights to the destination airport from this airport
                            if (!stop2.equals(this.destination)) {
                                JSONObject flight3 = getAPIData(stop2, this.destination, this.date);
                                if (flight3 != null) {
                                    double price3 = (double) flight3.get("price");
                                    String airline3 = (String) flight3.get("airline");
                                    // destination node will be at end of graph list
                                    Edge e3 = new Edge(graph.size() - 1, price3, airline3);
                                    stop2Airport.addOutEdge(e3);
                                    
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public JSONObject getAPIData(String origin, String destination, String date) {
        com.mashape.unirest.http.HttpResponse<String> response;
        String country = "US";
        String currency = "USD";
        String lang = "en-US";
        try {
            response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/"
                        + country + "/" + currency + "/" + lang + "/" + origin + "-sky/" + destination + "-sky/" + date)
                    .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                    .header("x-rapidapi-key", "0b94b14200msh8b975854c382fa0p1db856jsn66be75f55f3c")
                    .asString();
            JSONParser parser = new JSONParser();
            JSONObject res = (JSONObject) parser.parse(response.getBody());
            JSONArray quotes = (JSONArray) res.get("Quotes");
            JSONArray carriers = (JSONArray) res.get("Carriers");
            for (int i = 0; i < quotes.size(); i++) {
                JSONObject quote = (JSONObject) quotes.get(i);
                
                double minPrice = (double) quote.get("MinPrice"); //need this
                boolean direct = (boolean) quote.get("Direct"); //check this
                if (direct) {
                    JSONObject outbounds = (JSONObject) quote.get("OutboundLeg");
                    JSONArray carrierIds = (JSONArray) outbounds.get("CarrierIds");
                    Long carrierId = (Long) carrierIds.get(0);
                    
                    for (Object o : carriers) {
                        JSONObject obj = (JSONObject) o;
                        Long carrier = (Long) obj.get("CarrierId");
                        String name = (String) obj.get("Name");
                        if (carrier.equals(carrierId)) {
                            JSONObject flightDetails = new JSONObject();
                            flightDetails.put("price", minPrice);
                            flightDetails.put("airline", name);
                            return flightDetails;
                        }
                    }
                } 
            }
            
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printGraph() {
        for (Node u : graph) {
            System.out.println("-----");
            String start = u.getAirport();
            System.out.println(start + ": ");
            ArrayList<Edge> edges = u.getOutEdges();
            for (Edge e : edges) {
                Node v = graph.get(e.getNodeIndex());
                String end = v.getAirport();
                double weight = e.getPrice();
                String airline = e.getAirline();
                System.out.println("    to " + end + " for $" + weight + " on " + airline);
                
            }
        }
        
    }

}
