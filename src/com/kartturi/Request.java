package com.kartturi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

public class Request {
    private String apiKey;
    private String city;
    private final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    private final String dailyWeatherUrl = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&appid=%s";
    private final String defaultCity = "turku";
    private final String folderLocation = "resources/defaults.txt";
    private String modifier;

    public Request() {

        String[] temp = getDefault();



        this.city = defaultCity;
        this.apiKey = temp[1];

    }


    public Request(String city) {
        String[] temp = getDefault();


        this.city = city;
        this.apiKey = temp[1];
    }



    public String[] getDefault() {

        try (FileInputStream fin = new FileInputStream(this.folderLocation)) {
            int i;
            String city = "";

            do {
                i = fin.read();
                if (i != -1) city += Character.toString((char) i);
            } while (i != -1);
            String[] result = city.split(" ");

            return result;
        } catch (IOException e) {
            System.out.println("Error " + e);
            return new String[]{"error"};
        }

    }

    public void fetchWeatherInfo() throws IOException {
        URL url = new URL(String.format(this.weatherUrl, this.city, this.apiKey));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            System.out.println("no results with city named " + this.city);

        } else {
            String inline = "";
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();


            JSONParser parse = new JSONParser();

            try {
                JSONObject jobj = (JSONObject) parse.parse(inline);
                JSONObject temp = (JSONObject) jobj.get("main");

                System.out.println(this.city + ": " + this.roundStringNumbers(temp.get("temp").toString()));
                System.out.println("Ilmankosteus: " + temp.get("humidity"));
                System.out.println("Päivän ylin: " + this.roundStringNumbers(temp.get("temp_max").toString()));
                System.out.println("Päivän alin " + this.roundStringNumbers(temp.get("temp_min").toString()));



            } catch (ParseException e) {
                System.out.println(e);

            }


        }
    }

    public String roundStringNumbers(String str) {

         double temp =  Double.parseDouble(str);
         int result = (int) Math.round(temp);
         String resultString = Integer.toString(result);
        return resultString;
    }

    public void getDayWeather() throws IOException {
        URL url = new URL(String.format(this.dailyWeatherUrl, this.city, this.apiKey));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            System.out.println("no results with city named " + this.city);
        } else {
            String inline = "";
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();


            JSONParser parse = new JSONParser();

            try {
                JSONObject jobj = (JSONObject) parse.parse(inline);

                JSONArray arr = (JSONArray) jobj.get("list");
                Iterator itr = arr.iterator();
                System.out.println("sää: " + this.city);
                while (itr.hasNext()) {
                    JSONObject master = (JSONObject) parse.parse(itr.next().toString());
                    JSONObject temp = (JSONObject) master.get("main");

                    System.out.println(master.get("dt_txt"));
                    System.out.println("Lämpötila " + this.roundStringNumbers(temp.get("temp").toString()));
                    System.out.println("Ilmankosteus: " + temp.get("humidity"));

                    System.out.println("--------------------");
                }


            } catch (ParseException e) {
                System.out.println(e);

            }


        }

    }
}
