import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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