package nets150_finalproject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Main {

    public static void main(String[] args) {
        try {
            com.mashape.unirest.http.HttpResponse<String> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/US/USD/en-US/SFO-sky/JFK-sky/2020-09-01?inboundpartialdate=2019-12-01")
                    .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                    .header("x-rapidapi-key", "0b94b14200msh8b975854c382fa0p1db856jsn66be75f55f3c")
                    .asString();
            System.out.println(response.getBody());
        } catch (UnirestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
