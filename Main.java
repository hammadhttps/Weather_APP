import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 class DataStore {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/weather_data";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "(H@mm@d)123"; // replace with your MySQL password

     public void storeWeatherData(String data) {
         JSONObject jsonObject = new JSONObject(data);
         JSONObject main = jsonObject.getJSONObject("main");
         double temp = main.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
         int pressure = main.getInt("pressure");
         int humidity = main.getInt("humidity");

         String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

         String query = "INSERT INTO weather (temp, pressure, humidity, description) VALUES (?, ?, ?, ?)";

         try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
              PreparedStatement statement = connection.prepareStatement(query)) {

             statement.setDouble(1, temp);
             statement.setInt(2, pressure);
             statement.setInt(3, humidity);
             statement.setString(4, description);

             statement.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

    public void storeForecastData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray list = jsonObject.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            long dt = forecast.getLong("dt");
            Date date = new Date(dt * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedDate = sdf.format(date);

            JSONObject main = forecast.getJSONObject("main");
            double temp = main.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
            int humidity = main.getInt("humidity");

            JSONArray weather = forecast.getJSONArray("weather");
            String description = weather.getJSONObject(0).getString("description");

            storeData("forecast", temp, null, null, null, formattedDate, description);
        }
    }

    public void storePollutionData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject main = jsonObject.getJSONObject("main");
        double co = main.getDouble("co");
        double no = main.getDouble("no");
        double no2 = main.getDouble("no2");
        double o3 = main.getDouble("o3");
        double so2 = main.getDouble("so2");

        storeData("pollution", null, null, null, null, null, null, co, no, no2, o3, so2);
    }

    private void storeData(String tableName, Double temp, Integer pressure, Integer humidity, String description,
                           String date, String forecastDescription, Double... pollutants) {
        String query = null;

        if (tableName.equals("weather")) {
            query = "INSERT INTO weather (temp, pressure, humidity, description) VALUES (?, ?, ?, ?)";
        } else if (tableName.equals("forecast")) {
            query = "INSERT INTO forecast (temp, humidity, description, date) VALUES (?, ?, ?, ?)";
        } else if (tableName.equals("pollution")) {
            query = "INSERT INTO pollution (co, no, no2, o3, so2) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            int index = 1;

            if (tableName.equals("weather")) {
                statement.setDouble(index++, temp);
                statement.setInt(index++, pressure);
                statement.setInt(index++, humidity);
                statement.setString(index++, description);
            } else if (tableName.equals("forecast")) {
                statement.setDouble(index++, temp);
                statement.setInt(index++, humidity);
                statement.setString(index++, forecastDescription);
                statement.setString(index++, date);
            } else if (tableName.equals("pollution")) {
                for (Double pollutant : pollutants) {
                    statement.setDouble(index++, pollutant);
                }
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


interface weather_fetcher{
    void get_weather_data(double lat,double lon);
}
interface forecast_fetcher{
    void get_forecast_data(double lat,double lon);
}
interface pollution_fetcher{
    void get_pollution_data(double lat,double lon);
}
interface store_weatherdata{
    void store_weather_data();
}
interface store_forecastdata{
    void store_forecast_data();
}
interface store_pollutiondata{
    void store_pollution_data();
}
interface weather_logic{
    void check_weather_with_lat_lon(double lat,double lon);
    void check_weather_with_name(String name);
}
interface get_sunrise_sunset{
    void get_sunrisesunset(String name);
}
interface  get_dialogue{
    void get_basic_dialogues(String name);
}
interface get_pollution_data{
    boolean search_lat_lon(double lat,double lon);
    void get_pollutiondata(double lat,double lon);
}
interface display{
    void display_data(double lat,double lon,String name,int num);
    void show_weather_conditions(String name);
}
//api call for weather data=========================================
class weather_api_call implements weather_fetcher{
    private static HttpResponse<String> response;
    //static String data;
    @Override
    public void get_weather_data(double lat, double lon){
        String apiKey = "a95ac20b44385c921f020bdcf01d1094";
//        double latitude = 32.166351;
//        double longitude = 74.195900;

        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            // System.out.println(response.body());
            //data=response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //  System.out.println(response.body());
    }
    String get_response(){
        //   System.out.println(data);
        return response.body();
    }
}
//api call for 5 day forecast===================================================
class forecast_api_call implements forecast_fetcher{
    private static HttpResponse<String> response1;
    @Override
    public void get_forecast_data(double lat,double lon){
        String apiKey = "a95ac20b44385c921f020bdcf01d1094";
//        double latitude = 32.166351;
//        double longitude = 74.195900;

        String url = "https://api.openweathermap.org/data/2.5/forecast"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            response1 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            //   System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    String get_response(){
        //   System.out.println(data);
        return response1.body();
    }
}
//api call for pollution========================================================================
class pollution_api_call implements pollution_fetcher{
    private static HttpResponse<String> response3;
    @Override
    public void get_pollution_data(double lat,double lon){
        String apiKey = "a95ac20b44385c921f020bdcf01d1094";
//        double latitude = 32.166351;
//        double longitude = 74.195900;

        String url = "https://api.openweathermap.org/data/2.5/air_pollution"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            response3 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            //   System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    String get_response(){
        //   System.out.println(data);
        return response3.body();
    }
}
class weatherdatastore implements store_weatherdata {
    private weather_api_call w1 = new weather_api_call();

    @Override
    public void store_weather_data() {
        File f1 = new File("weather.txt");
        String response= w1.get_response();
        //  System.out.println(response.body());
        try (FileWriter fw1 = new FileWriter("data.txt", true)) {
            fw1.write(response);
            fw1.write("\n");
            fw1.write("\n");
            fw1.flush();
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }

    }
}
class forecastdatastore implements store_forecastdata{
    private forecast_api_call fa1=new forecast_api_call();
    @Override
    public void store_forecast_data(){
        File f2 = new File("forecast.txt");
        try {
            FileWriter fw2 = new FileWriter("forecast.txt", true);
            fw2.write(fa1.get_response());
            fw2.write("\n");
            fw2.write("\n");
            fw2.flush();
            fw2.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
class pollutiondatastore implements  store_pollutiondata{
    pollution_api_call pa1=new pollution_api_call();

    @Override
    public void store_pollution_data() {
        File f3 = new File("pollution.txt");
        try {
            FileWriter fw3 = new FileWriter("pollution.txt", true);
            fw3.write(pa1.get_response());
            fw3.write("\n");
            fw3.write("\n");
            fw3.flush();
            fw3.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
class weatherlogic implements weather_logic{
    @Override
    public void check_weather_with_lat_lon(double lat,double lon){
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            String line;
            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
            Matcher matcher;
            while ((line = br.readLine()) != null) {
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    double number = Double.parseDouble(matcher.group());
                    if (lon == number) {
                        flag = true;

                    }
                    if (lat == number) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }

                }
                if (flag == true) {
                    //System.out.println(line);
                    break;
                }
            }
            if(flag==true){
                try (BufferedReader br1 = new BufferedReader(new FileReader("data.txt"))) {
                    String line1;
                    StringBuilder jsonData = new StringBuilder();
                    while ((line1 = br1.readLine()) != null) {
//                    if (line1.contains(name)) {
//                        jsonData.append(line1);
//                    }
                        jsonData.append(line1);
                    }

                    // Find the index of "sunrise" key
                    int startIndex = jsonData.indexOf("\"temp\"");

                    // If "sunrise" key is found
                    if (startIndex != -1) {
                        // Find the index of the value associated with "sunrise" key
                        int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                        int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                        if (valueEndIndex == -1) {
                            valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                        }

                        // Extract the value associated with "sunrise" key
                        String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                        // Print the key and its value
                        System.out.println("Temp: " + sunriseValue);
                    } else {
                        System.out.println("Key 'temp' not found in the JSON data.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (BufferedReader br2 = new BufferedReader(new FileReader("data.txt"))) {
                    String line2;
                    StringBuilder jsonData1 = new StringBuilder();
                    while ((line2 = br.readLine()) != null) {
//                    if (line2.contains(name)) {
//                        jsonData.append(line2);
//                    }
                        jsonData1.append(line2);
                    }

                    // Find the index of "sunrise" key
                    int startIndex = jsonData1.indexOf("\"pressure\"");

                    // If "sunrise" key is found
                    if (startIndex != -1) {
                        // Find the index of the value associated with "sunrise" key
                        int valueStartIndex = jsonData1.indexOf(":", startIndex) + 1;
                        int valueEndIndex = jsonData1.indexOf(",", valueStartIndex);
                        if (valueEndIndex == -1) {
                            valueEndIndex = jsonData1.indexOf("}", valueStartIndex);
                        }

                        // Extract the value associated with "sunrise" key
                        String sunriseValue = jsonData1.substring(valueStartIndex, valueEndIndex).trim();

                        // Print the key and its value
                        System.out.println("Pressure: " + sunriseValue);
                    } else {
                        System.out.println("Key 'temp' not found in the JSON data.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (BufferedReader br3 = new BufferedReader(new FileReader("data.txt"))) {
                    String line3;
                    StringBuilder jsonData3 = new StringBuilder();
                    while ((line3 = br3.readLine()) != null) {
//                    if (line.contains(name)) {
//                        jsonData.append(line);
//                    }
                        jsonData3.append(line3);
                    }

                    // Find the index of "sunrise" key
                    int startIndex = jsonData3.indexOf("\"humidity\"");

                    // If "sunrise" key is found
                    if (startIndex != -1) {
                        // Find the index of the value associated with "sunrise" key
                        int valueStartIndex = jsonData3.indexOf(":", startIndex) + 1;
                        int valueEndIndex = jsonData3.indexOf(",", valueStartIndex);
                        if (valueEndIndex == -1) {
                            valueEndIndex = jsonData3.indexOf("}", valueStartIndex);
                        }

                        // Extract the value associated with "sunrise" key
                        String sunriseValue = jsonData3.substring(valueStartIndex, valueEndIndex).trim();

                        // Print the key and its value
                        System.out.println("Humidity: " + sunriseValue);
                    } else {
                        System.out.println("Key 'temp' not found in the JSON data.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void check_weather_with_name(String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }

            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"temp\"");

            // If "sunrise" key is found
            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Temp: " + sunriseValue);
            } else {
                System.out.println("Key 'temp' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }

            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"pressure\"");

            // If "sunrise" key is found
            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Pressure: " + sunriseValue);
            } else {
                System.out.println("Key 'temp' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }

            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"humidity\"");

            // If "sunrise" key is found
            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Humidity: " + sunriseValue);
            } else {
                System.out.println("Key 'temp' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class sunrise_sunset implements  get_sunrise_sunset{
    @Override
    public void get_sunrisesunset(String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }
            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"sunrise\"");

            // If "sunrise" key is found
            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Sunrise: " + sunriseValue);
            } else {
                System.out.println("Key 'sunrise' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }

            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"sunset\"");


            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Sunset: " + sunriseValue);
            } else {
                System.out.println("Key 'sunrise' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class dialogue implements get_dialogue{
    @Override
    public void get_basic_dialogues(String name){
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }

            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"feels_like\"");

            // If "sunrise" key is found
            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Feelslike: " + sunriseValue);
            } else {
                System.out.println("Key 'Feels like' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }

            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"temp_min\"");

            // If "sunrise" key is found
            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Min Temp: " + sunriseValue);
            } else {
                System.out.println("Key 'Feels like' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            StringBuilder jsonData = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains(name)) {
                    jsonData.append(line);
                }
            }

            // Find the index of "sunrise" key
            int startIndex = jsonData.indexOf("\"temp_max\"");

            // If "sunrise" key is found
            if (startIndex != -1) {
                // Find the index of the value associated with "sunrise" key
                int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                if (valueEndIndex == -1) {
                    valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                }

                // Extract the value associated with "sunrise" key
                String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                // Print the key and its value
                System.out.println("Max Temp: " + sunriseValue);
            } else {
                System.out.println("Key 'Feels like' not found in the JSON data.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class pollution_data implements  get_pollution_data{
    @Override
    public boolean search_lat_lon(double lat, double lon){
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            String line;
            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
            Matcher matcher;
            while ((line = br.readLine()) != null) {
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    double number = Double.parseDouble(matcher.group());
                    if (lon == number) {
                        flag = true;

                    }
                    if (lat == number) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }

            }
        }
        catch(IOException e){
            e.getMessage();
        }
        return flag;
    }

    @Override
    public void get_pollutiondata(double lat, double lon) {
        boolean flag=search_lat_lon(lat,lon);
        if(flag==true){
            try (BufferedReader br = new BufferedReader(new FileReader("pollution.txt"))) {
                String line;
                StringBuilder jsonData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    jsonData.append(line);

                }

                // Find the index of "sunrise" key
                int startIndex = jsonData.indexOf("\"co\"");

                // If "sunrise" key is found
                if (startIndex != -1) {
                    // Find the index of the value associated with "sunrise" key
                    int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                    int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                    if (valueEndIndex == -1) {
                        valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                    }

                    // Extract the value associated with "sunrise" key
                    String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                    // Print the key and its value
                    System.out.println("co: " + sunriseValue);
                } else {
                    System.out.println("Key  not found in the JSON data.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader br = new BufferedReader(new FileReader("pollution.txt"))) {
                String line;
                StringBuilder jsonData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    jsonData.append(line);

                }

                // Find the index of "sunrise" key
                int startIndex = jsonData.indexOf("\"no\"");

                // If "sunrise" key is found
                if (startIndex != -1) {
                    // Find the index of the value associated with "sunrise" key
                    int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                    int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                    if (valueEndIndex == -1) {
                        valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                    }

                    // Extract the value associated with "sunrise" key
                    String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                    // Print the key and its value
                    System.out.println("no: " + sunriseValue);
                } else {
                    System.out.println("Key  not found in the JSON data.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader br = new BufferedReader(new FileReader("pollution.txt"))) {
                String line;
                StringBuilder jsonData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    jsonData.append(line);

                }

                // Find the index of "sunrise" key
                int startIndex = jsonData.indexOf("\"no2\"");

                // If "sunrise" key is found
                if (startIndex != -1) {
                    // Find the index of the value associated with "sunrise" key
                    int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                    int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                    if (valueEndIndex == -1) {
                        valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                    }

                    // Extract the value associated with "sunrise" key
                    String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                    // Print the key and its value
                    System.out.println("no2: " + sunriseValue);
                } else {
                    System.out.println("Key  not found in the JSON data.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader br = new BufferedReader(new FileReader("pollution.txt"))) {
                String line;
                StringBuilder jsonData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    jsonData.append(line);

                }

                // Find the index of "sunrise" key
                int startIndex = jsonData.indexOf("\"o3\"");

                // If "sunrise" key is found
                if (startIndex != -1) {
                    // Find the index of the value associated with "sunrise" key
                    int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                    int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                    if (valueEndIndex == -1) {
                        valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                    }

                    // Extract the value associated with "sunrise" key
                    String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                    // Print the key and its value
                    System.out.println("o3: " + sunriseValue);
                } else {
                    System.out.println("Key  not found in the JSON data.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader br = new BufferedReader(new FileReader("pollution.txt"))) {
                String line;
                StringBuilder jsonData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    jsonData.append(line);

                }

                // Find the index of "sunrise" key
                int startIndex = jsonData.indexOf("\"so2\"");

                // If "sunrise" key is found
                if (startIndex != -1) {
                    // Find the index of the value associated with "sunrise" key
                    int valueStartIndex = jsonData.indexOf(":", startIndex) + 1;
                    int valueEndIndex = jsonData.indexOf(",", valueStartIndex);
                    if (valueEndIndex == -1) {
                        valueEndIndex = jsonData.indexOf("}", valueStartIndex);
                    }

                    // Extract the value associated with "sunrise" key
                    String sunriseValue = jsonData.substring(valueStartIndex, valueEndIndex).trim();

                    // Print the key and its value
                    System.out.println("so2: " + sunriseValue);
                } else {
                    System.out.println("Key  not found in the JSON data.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class displayer implements  display{
    private pollution_data pd1=new pollution_data();
    private dialogue d1=new dialogue();
    private sunrise_sunset ss1=new sunrise_sunset();
    private weatherlogic wl1=new weatherlogic();

    @Override
    public void display_data(double lat, double lon, String name,int num) {
        if(num==1){
            pd1.get_pollutiondata(lat,lon);
        } else if(num==2){
            d1.get_basic_dialogues(name);
        } else if(num==3){
            wl1.check_weather_with_name(name);
        } else if(num==4){
            wl1.check_weather_with_lat_lon(lat,lon);
        }
    }

    @Override
    public void show_weather_conditions(String name) {
        d1.get_basic_dialogues(name);
        ss1.get_sunrisesunset(name);
        wl1.check_weather_with_name(name);
    }
}
 class WeatherApp extends JFrame implements ActionListener {
    private JTextField latitudeField;
    private JTextField longitudeField;
    private JTextField cityField;
    private JButton getDataButton;
    private JButton checkWeatherButton;
    private JButton sunriseSunsetButton;
    private JButton showPollutionButton;
    private JButton showWeatherConditionsButton;
    private JButton showDialogueButton;
    private JButton showForecastButton;
    private weather_api_call wa;
    private forecast_api_call fa;
    private weatherdatastore wd1;
    private forecastdatastore fd1;
    private pollutiondatastore pds1;
    private pollution_api_call pa;
    private pollution_data pd;
    private dialogue d;
    private displayer d2;
    private sunrise_sunset ss;
    private weatherlogic wl;

     private DataStore dataStore;

    public WeatherApp() {
        initComponents();
        wa = new weather_api_call();
        wd1=new weatherdatastore();
        fa = new forecast_api_call();
        fd1=new forecastdatastore();
        pa = new pollution_api_call();
        pds1=new pollutiondatastore();
        pd = new pollution_data();
        d = new dialogue();
        d2 = new displayer();
        ss = new sunrise_sunset();
        wl = new weatherlogic();
        dataStore = new DataStore();

    }

    private void initComponents() {
        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("Latitude:"));
        latitudeField = new JTextField(10);
        add(latitudeField);

        add(new JLabel("Longitude:"));
        longitudeField = new JTextField(10);
        add(longitudeField);

        add(new JLabel("City Name:"));
        cityField = new JTextField(10);
        add(cityField);

        getDataButton = new JButton("Get Data");
        getDataButton.addActionListener(this);
        add(getDataButton);

        checkWeatherButton = new JButton("Check Weather");
        checkWeatherButton.addActionListener(this);
        add(checkWeatherButton);

        sunriseSunsetButton = new JButton("Sunrise Sunset");
        sunriseSunsetButton.addActionListener(this);
        add(sunriseSunsetButton);

        showPollutionButton = new JButton("Show Pollution");
        showPollutionButton.addActionListener(this);
        add(showPollutionButton);

        showWeatherConditionsButton = new JButton("Show Weather Conditions");
        showWeatherConditionsButton.addActionListener(this);
        add(showWeatherConditionsButton);

        showDialogueButton = new JButton("Show Dialogue");
        showDialogueButton.addActionListener(this);
        add(showDialogueButton);

        showForecastButton = new JButton("Show Forecast");
        showForecastButton.addActionListener(this);
        add(showForecastButton);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getDataButton) {
            try {
                double lat = Double.parseDouble(latitudeField.getText());
                double lon = Double.parseDouble(longitudeField.getText());
                wa.get_weather_data(lat, lon);
                pa.get_pollution_data(lat,lon);
                dataStore.storeWeatherData(wa.get_response());
                fa.get_forecast_data(lat, lon);
                wd1.store_weather_data();
                pds1.store_pollution_data();
                fd1.store_forecast_data();
                dataStore.storeForecastData(fa.get_response());
                pa.get_pollution_data(lat, lon);
                dataStore.storePollutionData(pa.get_response());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for latitude and longitude.");
            }
        } else if (e.getSource() == checkWeatherButton) {
            wl.check_weather_with_lat_lon(
                    Double.parseDouble(latitudeField.getText()),
                    Double.parseDouble(longitudeField.getText())
            );
        } else if (e.getSource() == sunriseSunsetButton) {
            ss.get_sunrisesunset(cityField.getText());
        } else if (e.getSource() == showPollutionButton) {
            pd.get_pollutiondata(
                    Double.parseDouble(latitudeField.getText()),
                    Double.parseDouble(longitudeField.getText())
            );
        } else if (e.getSource() == showWeatherConditionsButton) {
            d2.show_weather_conditions(cityField.getText());
        } else if (e.getSource() == showDialogueButton) {
            d.get_basic_dialogues(cityField.getText());
        } else if (e.getSource() == showForecastButton) {
            try {
                double lat = Double.parseDouble(latitudeField.getText());
                double lon = Double.parseDouble(longitudeField.getText());
                fa.get_forecast_data(lat, lon);
                StringBuilder forecastData = new StringBuilder();
                JSONObject jsonObject = new JSONObject(fa.get_response());
                JSONArray list = jsonObject.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject forecast = list.getJSONObject(i);
                    long dt = forecast.getLong("dt");
                    Date date = new Date(dt * 1000);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = sdf.format(date);
                    JSONObject main = forecast.getJSONObject("main");
                    double temp = main.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
                    double humidity = main.getDouble("humidity");
                    JSONArray weather = forecast.getJSONArray("weather");
                    String description = weather.getJSONObject(0).getString("description");
                    forecastData.append(formattedDate).append(": ").append(temp).append("°C, ").append(humidity).append("% humidity, ").append(description).append("\n");
                }
                JOptionPane.showMessageDialog(this, forecastData.toString(), "5-Day Forecast", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException | JSONException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching forecast data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherApp());
    }
}