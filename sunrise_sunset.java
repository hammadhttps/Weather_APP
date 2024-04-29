import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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