#!/bin/bash

echo "🚀 Demonstrating Beautiful Weather App Console Version..."
echo ""

# Create a demo input file
cat > demo_input.txt << EOF
3
London
4
EOF

echo "📱 Running demo with London weather data..."
echo "───────────────────────────────────────────"

# Run the console app with demo input
java -cp "target/lib/json-20230227.jar:target/classes" com.weatherapp.ConsoleWeatherApp < demo_input.txt

echo ""
echo "✅ Demo completed!"
echo ""
echo "🎯 To run the app interactively:"
echo "   java -cp \"target/lib/json-20230227.jar:target/classes\" com.weatherapp.ConsoleWeatherApp"
echo ""
echo "🌐 For the beautiful GUI version, ensure JavaFX is installed and run:"
echo "   ./run.sh"

# Clean up
rm -f demo_input.txt