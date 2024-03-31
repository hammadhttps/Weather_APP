import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class WeatherAppUI extends JFrame {
    private JTextField cityTextField;
    private JButton searchButton;
    private JButton showForecastButton;
    private JButton showDialogueButton;
    private JTextArea resultTextArea;

    public WeatherAppUI() {
        setTitle("Weather App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        cityTextField = new JTextField(20);
        searchButton = new JButton("Search");
        showForecastButton = new JButton("Show Forecast");
        showDialogueButton = new JButton("Show Dialogue");
        resultTextArea = new JTextArea(15, 50);
        resultTextArea.setEditable(false);

        inputPanel.add(new JLabel("Enter City Name:"));
        inputPanel.add(cityTextField);
        inputPanel.add(searchButton);
        inputPanel.add(showForecastButton);
        inputPanel.add(showDialogueButton);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cityName = cityTextField.getText().trim();
                if (!cityName.isEmpty()) {
                    Call backend = new Call();
                    backend.check_weather_name(cityName);
                    resultTextArea.setText(""); 
                } else {
                    JOptionPane.showMessageDialog(WeatherAppUI.this, "Please enter a city name");
                }
            }
        });

        showForecastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cityName = cityTextField.getText().trim();
                if (!cityName.isEmpty()) {
                    Call backend = new Call();
                    backend.show_forecast(cityName);
                    resultTextArea.setText(""); 
                } else {
                    JOptionPane.showMessageDialog(WeatherAppUI.this, "Please enter a city name");
                }
            }
        });

        showDialogueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cityName = cityTextField.getText().trim();
                if (!cityName.isEmpty()) {
                    Call backend = new Call();
                    backend.show_dialogue(cityName);
                    resultTextArea.setText(""); 
                } else {
                    JOptionPane.showMessageDialog(WeatherAppUI.this, "Please enter a city name");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherAppUI();
            }
        });
    }
}
