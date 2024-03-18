Weather Application
This project is a weather application where users can view the weather forecast of any location. It utilizes various APIs provided by OpenWeatherMap to obtain weather data in JSON format.

APIs Used
Current Weather Data:

Provides weather data for any location on Earth based on longitude and latitude.
API Endpoint: https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
5-day Forecast:

Provides weather forecast for a location for 5 days with a 3hr interval.
API Endpoint: https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
Air Pollution API:

Provides Air Quality Index and information about polluting gases.
API Endpoint: http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={API key}
Reference: OpenWeatherMap API Documentation

Usage
To use these APIs, you need to create an account on openweathermap.org to obtain an API key, which you will need to enter every time while accessing the above links.

Project Structure
The project implements two UI and two storage methods, ensuring each UI and storage is independent of each other.

UI
Terminal-based UI: A console application.
Java Desktop Application: A GUI-based application.
Both UI options should be fully functional for all use cases.

Storage
SQL Database: Any SQL database can be used for data storage.
Text-based Storage: Implementation of a ".txt" based storage mechanism.
Use Cases
The following use-cases have been implemented:

Add multiple locations to check weather with longitude and latitude.
Add multiple locations to check weather with city/country name.
Show current weather conditions.
Display basic information such as "Feels like, minimum and maximum temperature", etc.
Show sunrise and sunset time.
Display weather forecast for 5 days.
Add timestamp for weather records.
Implement Cache Management:
Utilize a database as a cache to store frequently accessed weather data, minimizing API calls and improving application performance.
Generate notifications for poor weather conditions.
Show Air Pollution data.
Generate notifications for poor air quality.
Display data about polluting gases.
Note
Ensure that you replace {lat}, {lon}, and {API key} placeholders with actual latitude, longitude, and API key obtained from OpenWeatherMap respectively in the API endpoints.

Feel free to contribute to this project and improve its functionality!