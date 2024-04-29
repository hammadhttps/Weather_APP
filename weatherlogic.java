import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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