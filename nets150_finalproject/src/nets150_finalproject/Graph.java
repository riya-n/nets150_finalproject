package nets150_finalproject;

import java.util.*;

public class Graph {
    
    private ArrayList<Node> graph;
    
    public class Node {
        private ArrayList<Edge> outEdges;
        private String airportCode; // e.g., SFO
        private String countryCode; // e.g., US - incase we decide to do word
        
        Node(String airportCode, String countryCode) {
            this.outEdges = new ArrayList<Edge>();
            this.airportCode = airportCode;
            this.countryCode = countryCode;
        }
        
        public void addOutEdge(Edge v) {
            this.outEdges.add(v);
        }
        
        public ArrayList<Edge> getOutEdges() {
            return this.outEdges;
        }
        
        public String getAirport() {
            return this.airportCode;
        }
        
        public String getCountry() {
            return this.countryCode;
        }

    }
    
    public class Edge {
        private int v; // reference to the airport's index in the graph arraylist
        private int weight; // min price of flight
        private String airlineName; // e.g., Alaska Airlines
        // TODO: add departure date and time or flight time
        
        Edge(int v, int weight, String airlineName) {
            this.v = v;
            this.weight = weight;
            this.airlineName = airlineName;
        }
        
        public int getNode() {
            return this.v;
        }
        
        public int getWeight() {
            return this.weight;
        }
        
        public String getAirline() {
            return this.airlineName;
        }
    }
    
    public Graph(Node origin, Node destination) {
        graph = new ArrayList<Node>();
        graph.add(origin);
        graph.add(destination);
    }
    
    public int getSize() {
        return graph.size();
    }
    
    public void addEdge(int u, int v, int weight, String airlineName) {
        if (u < 0 || v < 0 || u >= graph.size() || v >= graph.size() || u == v 
                || weight < 0 || airlineName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        Node origin = graph.get(u);
        Edge flight = new Edge(v, weight, airlineName);
        origin.addOutEdge(flight);
    }
    
    public Integer getWeight(int u, int v) {
        if (u < 0 || v < 0 || u >= graph.size() || v >= graph.size() || u == v) {
            throw new IllegalArgumentException();
        }
        Node origin = graph.get(u);
        ArrayList<Edge> outEdges = origin.getOutEdges();
        for (Edge e: outEdges) {
            if (e.getNode() == v) {
                return e.getWeight();
            }
        }
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
