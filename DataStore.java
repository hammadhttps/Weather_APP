import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class DataStore {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/weather_data";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "(H@mm@d)123"; // replace with your MySQL password

    public void storeWeatherData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
        int pressure = main.getInt("pressure");
        int humidity = main.getInt("humidity");

        String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        String query = "INSERT INTO weather (temp, pressure, humidity, description) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, temp);
            statement.setInt(2, pressure);
            statement.setInt(3, humidity);
            statement.setString(4, description);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeForecastData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray list = jsonObject.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            long dt = forecast.getLong("dt");
            Date date = new Date(dt * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedDate = sdf.format(date);

            JSONObject main = forecast.getJSONObject("main");
            double temp = main.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
            int humidity = main.getInt("humidity");

            JSONArray weather = forecast.getJSONArray("weather");
            String description = weather.getJSONObject(0).getString("description");

            storeData("forecast", temp, null, null, null, formattedDate, description);
        }
    }

    public void storePollutionData(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject main = jsonObject.getJSONObject("main");
        double co = main.getDouble("co");
        double no = main.getDouble("no");
        double no2 = main.getDouble("no2");
        double o3 = main.getDouble("o3");
        double so2 = main.getDouble("so2");

        storeData("pollution", null, null, null, null, null, null, co, no, no2, o3, so2);
    }

    private void storeData(String tableName, Double temp, Integer pressure, Integer humidity, String description,
                           String date, String forecastDescription, Double... pollutants) {
        String query = null;

        if (tableName.equals("weather")) {
            query = "INSERT INTO weather (temp, pressure, humidity, description) VALUES (?, ?, ?, ?)";
        } else if (tableName.equals("forecast")) {
            query = "INSERT INTO forecast (temp, humidity, description, date) VALUES (?, ?, ?, ?)";
        } else if (tableName.equals("pollution")) {
            query = "INSERT INTO pollution (co, no, no2, o3, so2) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            int index = 1;

            if (tableName.equals("weather")) {
                statement.setDouble(index++, temp);
                statement.setInt(index++, pressure);
                statement.setInt(index++, humidity);
                statement.setString(index++, description);
            } else if (tableName.equals("forecast")) {
                statement.setDouble(index++, temp);
                statement.setInt(index++, humidity);
                statement.setString(index++, forecastDescription);
                statement.setString(index++, date);
            } else if (tableName.equals("pollution")) {
                for (Double pollutant : pollutants) {
                    statement.setDouble(index++, pollutant);
                }
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
