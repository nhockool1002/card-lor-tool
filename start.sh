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

# Kill existing Java processes for this app
echo "🔄 Stopping any existing instances..."
pkill -f "db-connection-status" 2>/dev/null
sleep 2

echo "🚀 Starting Database Connection Status Monitor..."
echo ""
echo "📊 Features:"
echo "   • Monitors SIT and UAT environments simultaneously"
echo "   • Environment dropdown selector"
echo "   • Real-time status bar"
echo "   • Auto-refresh every 30 seconds"
echo ""
echo "🌐 Access URLs:"
echo "   • Web Dashboard: http://localhost:8080"
echo "   • API Endpoint: http://localhost:8080/api/status"
echo "   • Health Check: http://localhost:8080/actuator/health"
echo ""

# Start the application
mvn spring-boot:run &

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 15

# Test if application is running
if curl -s http://localhost:8080/api/status > /dev/null; then
    echo ""
    echo "✅ Application started successfully!"
    echo ""
    echo "📱 Quick Test Commands:"
    echo "   • curl http://localhost:8080/api/status"
    echo "   • curl http://localhost:8080/api/status?env=SIT"
    echo "   • curl http://localhost:8080/api/status?env=UAT"
    echo "   • curl http://localhost:8080/api/summary"
    echo ""
    echo "🎯 Open your browser and navigate to: http://localhost:8080"
else
    echo ""
    echo "❌ Application failed to start or is not responding"
    echo "📋 Check the logs above for any errors"
fi