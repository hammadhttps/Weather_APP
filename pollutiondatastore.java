import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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