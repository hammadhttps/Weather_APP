import javax.swing.*;
import java.awt.*;

public class main extends JFrame {
    private JButton checkWeatherBtn, airPollutionBtn, humidityBtn, sunriseSunsetBtn;

    public main() {
        setTitle("Weather Forecast Application");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel headingLabel = new JLabel("Weather Forecast Application");
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(headingLabel);

        checkWeatherBtn = new JButton("Check Weather");
        airPollutionBtn = new JButton("Air Pollution Info");
        humidityBtn = new JButton("Humidity Info");
        sunriseSunsetBtn = new JButton("Sunrise/Sunset Time");

        panel.add(checkWeatherBtn);
        panel.add(airPollutionBtn);
        panel.add(humidityBtn);
        panel.add(sunriseSunsetBtn);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new main();
    }
}
