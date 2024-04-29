import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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