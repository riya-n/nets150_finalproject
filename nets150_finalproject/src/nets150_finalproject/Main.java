package nets150_finalproject;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> airports = new ArrayList<String>();
        airports.add("SFO");
        airports.add("JFK");
        airports.add("DFW");
        airports.add("CLT");
        
        Graph g = new Graph("SFO", "JFK", "2020-05-01", airports);
        g.constructGraph();
        g.printGraph();

    }

}
