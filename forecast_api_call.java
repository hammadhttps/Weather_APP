import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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