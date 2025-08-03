@echo off
echo Building Weather App...
cd src
mvn clean compile
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo Running Weather App...
mvn javafx:run
pause 