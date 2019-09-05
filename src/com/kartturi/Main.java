package com.kartturi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
	// write your code here

        if(args.length == 0) {
            Request r = new Request();
            try  {
                System.out.println("getting weather info.......");
                 r.fetchWeatherInfo();
            } catch (IOException e) {
                System.out.println("errorr");
                System.out.println(e);
            }
        } else if(args.length == 1) {

            String city = args[0];
            if(city.startsWith("-d")) {
                Request r = new Request();


                //get city weather
                try  {
                    System.out.println("getting weather info.......");

                    r.getDayWeather();
                } catch (IOException e) {
                    System.out.println("errorr");
                    System.out.println(e);
                }

            } else {
                Request r = new Request(args[0]);

                try  {
                    System.out.println("getting weather info.......");
                    r.fetchWeatherInfo();
                } catch (IOException e) {
                    System.out.println("errorr");
                    System.out.println(e);
                }
            }
        } else {
            if(args[0].startsWith("-d") || args.length > 2)  {
                System.out.printf("Error, write city and then " +
                        "-d not anything else or otherway around around");
            } else {
                Request r = new Request(args[0]);
                try  {
                    System.out.println("getting weather info.......");

                    r.getDayWeather();
                } catch (IOException e) {
                    System.out.println("errorr");
                    System.out.println(e);
                }
            }





        }









    }
}
