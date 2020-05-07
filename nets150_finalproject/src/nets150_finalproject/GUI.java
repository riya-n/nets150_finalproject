package nets150_finalproject;

//imports necessary libraries for Java swing
	import java.awt.*;
	import java.util.ArrayList;
	import java.util.Arrays;
	import javax.swing.*;
	import org.json.simple.JSONArray;
	import org.json.simple.JSONObject;
	
	
	

	
	/**
	 * Main class that specifies the frame and widgets of the GUI
	 */

	public class GUI implements Runnable {
		
		@Override
	    public void run() { 
			
			
			// Top-level frame in which gui components live
	        final JFrame frame = new JFrame("Vacay Away!");
	        frame.setLocation(300, 300);
	        
	        // Status panel
	        final JPanel status_panel = new JPanel();
	        frame.add(status_panel, BorderLayout.SOUTH);
	        final JLabel status = new JLabel("");
	        status_panel.add(status);
	        
	        
	        //gets title for  keyword prompt message
			String title = "Please enter the type of place you wish to go using the following keywords: " 
			
							+ " museums, beach, historical, nightlife, sports, restaurants, outdoor";
			
			// assuming the user enters the data correctly
			
			//retrieves the user input for the keyword
			String keyword = JOptionPane.showInputDialog(frame, title);

			DataParser db = new DataParser();
			ArrayList<String> optcities;
			optcities = db.cityOptions(keyword);
			//String[] optcitiesArr = (String[]) optcities.toArray();
			String[] optcitiesArr = Arrays.copyOf(optcities.toArray(), optcities.size(), String[].class);
			

			
			//String locationTitle = "Here are the places you can visit in the US of that type:";
			
			
			for (String element : optcities) {
				System.out.println(element);
			}
			System.out.println("Please choose one of the places to go to.");

			String destCity = (String) JOptionPane.showInputDialog(frame, "Choose a city",
					"Here are the placees you can visit in the US of that type, choose one", 
					JOptionPane.QUESTION_MESSAGE, null, optcitiesArr, optcitiesArr[1]);
			
			String destination = db.cityAirportMap.get(destCity);

			
			/*
			 * requests the airport abbreviation
			 */
			
			String airportAbbrev = "Please enter the airport you wish to fly out from. (3 letter initials)";
			//retrieves the user input for the keyword
			String origin = JOptionPane.showInputDialog(frame, airportAbbrev);
			
			/*
			 * requests the departure date
			 */
			String  datePrompt =  "Please enter the date you would like to fly on (yyyy-mm-dd).";
			String date = JOptionPane.showInputDialog(frame, datePrompt);

			//changes the status label to show that  we're finding routes
			status.setText("Finding routes...");
			
			ArrayList<String> airports = db.getAllAirports();

			//output to be displayed
			ArrayList<String> output = new ArrayList<String>();
			
			Graph g = new Graph(origin, destination, date, airports);
			
			JSONArray route = g.getCheapestRoute();
			if (route == null) {
				String str = "Error: no flight routes from " + origin + " to " + destination + " on " + date;
				output.add(str);
				status.setText("Unfortunately, no can do :(");
			} else {
				System.out.println("-----");
				System.out.println("Best Route: ");
				for (int i = 0; i < route.size(); i++) {
					JSONObject flight = (JSONObject) route.get(i);
					String start = (String) flight.get("Origin");
					String end = (String) flight.get("Destination");
					double price = (double) flight.get("Price");
					String airline = (String) flight.get("Airline");
					//set it as a string
					//add it to the frame
					//box layout
					String str = "    " + start + " to " + end + " for " + price + " on " + airline;
					output.add(str);
				}
				status.setText("Displaying best route");
			}
			
			String[] outputArr = (String[]) output.toArray();
			JList<String> finalResults = new JList<String>(outputArr);
			frame.add(finalResults, BorderLayout.CENTER);
      
	        
		}

		/**
	     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
	     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
	     */
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new GUI());
	    }
	}


