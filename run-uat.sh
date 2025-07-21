#!/bin/bash

echo "=================================================="
echo "  Database Connection Status Monitor"
echo "  Multi-Environment Support (SIT + UAT)"
echo "=================================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 21 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java version $JAVA_VERSION is not supported. Please install Java 17 or higher."
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"
echo ""

echo "🔄 Starting Database Connection Status Monitor..."
echo "📊 Monitoring SIT and UAT environments simultaneously"
echo "🌐 Web Interface: http://localhost:8080"
echo "🔗 API Endpoint: http://localhost:8080/api/status"
echo ""

# Start the application
mvn spring-boot:run