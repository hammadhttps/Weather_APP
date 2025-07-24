#!/bin/bash

# Beautiful Weather App Runner
echo "🌤️ Building Beautiful Weather App..."

# Create target directories
mkdir -p target/classes
mkdir -p target/lib

# Download dependencies if not present
DEPS_DIR="target/lib"

# JSON library
JSON_JAR="$DEPS_DIR/json-20230227.jar"
if [ ! -f "$JSON_JAR" ]; then
    echo "📦 Downloading JSON library..."
    curl -L "https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar" -o "$JSON_JAR"
fi

# JavaFX (basic approach - may need manual installation)
echo "ℹ️  Note: This script assumes JavaFX is available in your Java installation."
echo "ℹ️  For full functionality, please install JavaFX 21 or use Maven with proper setup."

# Copy resources
echo "📁 Copying resources..."
cp -r src/main/resources/* target/classes/ 2>/dev/null || true

# Compile Java files
echo "🔨 Compiling Java files..."
CLASSPATH="$JSON_JAR"

# Find and compile all Java files
find src/main/java -name "*.java" > target/sources.txt

if [ -s target/sources.txt ]; then
    javac -cp "$CLASSPATH" -d target/classes --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml @target/sources.txt
    
    if [ $? -eq 0 ]; then
        echo "✅ Compilation successful!"
        echo "🚀 Running Beautiful Weather App..."
        echo ""
        
        # Run the application
        java -cp "$CLASSPATH:target/classes" --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml com.weatherapp.WeatherApp
    else
        echo "❌ Compilation failed!"
        echo ""
        echo "🔧 Troubleshooting:"
        echo "1. Ensure JavaFX is installed (apt install openjfx)"
        echo "2. Or use Maven: mvn clean javafx:run"
        echo "3. Check that all dependencies are available"
        exit 1
    fi
else
    echo "❌ No Java source files found!"
    exit 1
fi