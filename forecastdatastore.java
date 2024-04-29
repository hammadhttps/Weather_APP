import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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