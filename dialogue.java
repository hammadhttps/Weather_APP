import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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