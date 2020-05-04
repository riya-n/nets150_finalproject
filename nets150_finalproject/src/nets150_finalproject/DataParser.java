package nets150_finalproject;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataParser {
    private String baseURL;
    private Document currentDoc;
    Map<String, String> cityMap;
    Map<String, String> cityAirportMap;
    Map<String, String> reverseCityAirportMap;
    Map<String, ArrayList<String>> options;

   

    // Constructor that connects to the baseURL that is homepage
    public DataParser() {

        this.baseURL = "https://travel.usnews.com/Destinations/USA/";
    
        this.cityMap = new HashMap<String, String>();
        this.cityAirportMap = new HashMap<String, String>();
        this.reverseCityAirportMap = new HashMap<String, String>();
        this.options = new HashMap<String,ArrayList<String>>();

    
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
           
            getLinks();
        } catch (IOException e) {
            System.out.println("Could not connect :(");
        }
    }
    
//Function that gets the endpoints for each city so we can check if airport exists
    private Map<String, String> getLinks() {
        // TODO Auto-generated method stub
        Elements placeValues = (currentDoc.select("ul").select("li"));
      
         
        for (Element place : placeValues) {
                //System.out.println(place.text());
                String ext =(place.getElementsByTag("a").attr("href"));
                cityMap.put(place.text(), ext);
            
        }

        return cityMap;
        
    }
    
    
    //Function gets one MAIN airport code if it exists on the page
    //Newyork MANUALLY set to JFK as that is the main airport but LGA is returned from website
    private Map<String, String> getAirportCodes(){
        String airportCode = "";
        
        Document newDoc=null;
        
        for(String city : cityMap.keySet()) {
            String extension = cityMap.get(city);
           // System.out.println(city);
            String url = "https://travel.usnews.com/"+extension;
            // System.out.println(url);
            try {
                newDoc = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Could not connect to city page :(");
            }
          
           Elements values = 
            newDoc.select("section.travel-guide-overview__Section-s1ywd7zy-4")
          .select("div.travel-guide-overview__CustomRaw-s1ywd7zy-2.ozIEP.Raw-s13q2jmv-0.cLqQpz  >p");             
          
           //THIS IS THE DATA FROM THE PAGE
           String information = values.text();
           //checks if data contains word airport and splits it from there
           if (information.contains("airport")) {
               String result =
                       information.substring(
                         information.indexOf("Airport ") + ")".length(),
                         
                         information.length());

                    
                     result =
                       result.substring(
                         0,
                         result.indexOf(")"));
                    
               //REGEX TO GET THE AIRPORT CODE FROM BETWEEN BRACKETS      
               Matcher m = Pattern.compile("\\((.*?)\\)").matcher(result+")");
               if(m.find()) {
                   airportCode = m.group(1);

               }
               //Checks if an airport code is received and adds to map
               if (airportCode.length()<4 && airportCode != "") {
                   if (airportCode.equals("LGA")) {
                       
                       this.cityAirportMap.put(city, "JFK");
                       this.reverseCityAirportMap.put("JFK", city);
                   }
                   else {
                   this.cityAirportMap.put(city, airportCode);
                   this.reverseCityAirportMap.put(airportCode, city);
                   }
               }
               
           }
          
        }
        return cityAirportMap;
        
    }
    
    
    //Now that we have airport we can go to those pages and get information about them
    //Newyork MANUALLY set to JFK as that is the main airport but LGA is returned from website
    private void makeListsOfCitiesWithKeyWords(){
        Document newDoc=null;
        ArrayList<String>listmuseums = new ArrayList<String>();
        ArrayList<String>listbeach = new ArrayList<String>();
        ArrayList<String>listhistorical = new ArrayList<String>();
        ArrayList<String>listnightlife = new ArrayList<String>();
        ArrayList<String>listsport = new ArrayList<String>();
        ArrayList<String>listoutdoor = new ArrayList<String>();
        ArrayList<String>listrestaurants = new ArrayList<String>();
        
        for(String city : cityAirportMap.keySet()) {
            String extension = cityMap.get(city);
           // System.out.println(city);
            String url = "https://travel.usnews.com/"+extension;
            // System.out.println(url);
            try {
                newDoc = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Could not connect to city page :(");
            }
            Elements values = 
                    newDoc.select("section.travel-guide-overview__Section-s1ywd7zy-4")
                  .select("div.Raw-s13q2jmv-0.eWuROm  >p");             
            
            String info = values.text();
            //System.out.println(values.text());
            if(info.contains("museum")){
                listmuseums.add(city);  
            }
            if(info.contains("beach")){
                listbeach.add(city);  
            }
            if(info.contains("histor")){
               // System.out.println(city);
                listhistorical.add(city);  
            }
            if(info.contains("nightlife")){
                listnightlife.add(city);  
            }
            if(info.contains("sports")){
                listsport.add(city);  
            }
            if(info.contains("restaurant")){
                listrestaurants.add(city);  
            }
            if(info.contains("outdoor")){
                listoutdoor.add(city);  
            }
            
            this.options.put("museums", listmuseums);
            this.options.put("beach", listbeach);
            this.options.put("historical", listhistorical);
            this.options.put("nightlife", listmuseums);
            this.options.put("sports", listsport);
            this.options.put("restaurants", listrestaurants);
            this.options.put("outdoor", listoutdoor);
            
        }
    }
    
    //Returns a list of the cities that can be visited
    public ArrayList<String> cityOptions(String keyword){
        getAirportCodes();
        makeListsOfCitiesWithKeyWords();
        ArrayList<String> returnCities;
        returnCities =  options.get(keyword);
        return returnCities;
        
            }
    

                
  //Returns a list of cities that can be visitied and their airport
    public static void main(String[] args) {
        DataParser db = new DataParser();
        Map<String, String> cityMap ;
        ArrayList<String> optcities;
        
        
        optcities = db.cityOptions("outdoor");
        for (String element: optcities) {
            System.out.println(element+". The Airport is "+ db.cityAirportMap.get(element));
        }
    }
    
    }
