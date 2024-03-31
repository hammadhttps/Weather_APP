import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Call {
    private HttpResponse<String> response;
    private HttpResponse<String> response1;
    private HttpResponse<String> response2;

    public void get_data(double latitude, double longitude) {
        String apiKey = "a95ac20b44385c921f020bdcf01d1094";
//        double latitude = 32.166351;
//        double longitude = 74.195900;

        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + latitude
                + "&lon=" + longitude
                + "&appid=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            //   System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        store_data();
    }

    private void store_data() {
        File f1 = new File("data.txt");
        try {
            FileWriter fw1 = new FileWriter("data.txt", true);
            fw1.write(response.body());
            fw1.write("\n");
            fw1.write("\n");
            fw1.flush();
            fw1.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void get_forcast_data(double latitude, double longitude) {
        String apiKey = "a95ac20b44385c921f020bdcf01d1094";
//        double latitude = 32.166351;
//        double longitude = 74.195900;

        String url = "https://api.openweathermap.org/data/2.5/forecast"
                + "?lat=" + latitude
                + "&lon=" + longitude
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
        store_forecast_data();
    }

    private void store_forecast_data() {
        File f2 = new File("forecast.txt");
        try {
            FileWriter fw2 = new FileWriter("forecast.txt", true);
            fw2.write(response1.body());
            fw2.write("\n");
            fw2.write("\n");
            fw2.flush();
            fw2.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void get_airpollution_data(double latitude, double longitude) {
        String apiKey = "a95ac20b44385c921f020bdcf01d1094";
//        double latitude = 32.166351;
//        double longitude = 74.195900;

        String url = "https://api.openweathermap.org/data/2.5/air_pollution"
                + "?lat=" + latitude
                + "&lon=" + longitude
                + "&appid=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            //   System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        store_pollution_data();
    }

    void store_pollution_data() {
        File f3 = new File("pollution.txt");
        try {
            FileWriter fw3 = new FileWriter("pollution.txt", true);
            fw3.write(response2.body());
            fw3.write("\n");
            fw3.write("\n");
            fw3.flush();
            fw3.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    void check_weather(double lat, double lon) {
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
                    System.out.println(line);
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //    void check_weather_name(String name) {
//        int num = 0;
//        try {
//            BufferedReader br1 = new BufferedReader(new FileReader("data.txt"));
//            String line;
//            while ((line = br1.readLine()) != null) {
//                //num=line.indexOf(name);
//                if (line.contains(name)) {
//                    System.out.println(line);
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.getMessage();
//        }
//    }
    void check_weather_name(String name) {
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

    void get_sunrise_sunset(String name) {
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


    void show_dialogue(String name) {
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
    boolean search_lat_lon(double lat,double lon){
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
    void show_weather_conditions(String name){
        check_weather_name(name);
        show_dialogue(name);
    }
    void show_pollution(double lat, double lon) {
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
    private Call callObject;

    public WeatherApp() {
        initComponents();
        callObject = new Call();
    }

    private void initComponents() {
        setLayout(new GridLayout(7, 2, 5, 5));

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
                callObject.get_data(lat, lon);
                callObject.get_forcast_data(lat, lon);
                callObject.get_airpollution_data(lat, lon);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for latitude and longitude.");
            }
        } else if (e.getSource() == checkWeatherButton) {
            callObject.check_weather(
                    Double.parseDouble(latitudeField.getText()),
                    Double.parseDouble(longitudeField.getText())
            );
        } else if (e.getSource() == sunriseSunsetButton) {
            callObject.get_sunrise_sunset(cityField.getText());
        } else if (e.getSource() == showPollutionButton) {
            callObject.show_pollution(
                    Double.parseDouble(latitudeField.getText()),
                    Double.parseDouble(longitudeField.getText())
            );
        } else if (e.getSource() == showWeatherConditionsButton) {
            callObject.show_weather_conditions(cityField.getText());
        } else if (e.getSource() == showDialogueButton) {
            callObject.show_dialogue(cityField.getText());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherApp());
    }
}