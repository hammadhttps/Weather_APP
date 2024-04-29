import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

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