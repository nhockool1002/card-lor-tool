#!/bin/bash

echo "🚀 Starting Database Connection Status Application - SIT Environment"
echo "==============================================================="

# Kill existing Java processes
pkill -f java 2>/dev/null
sleep 2

# Start application with SIT profile
echo "📊 Starting application with SIT profile..."
mvn spring-boot:run -Dspring-boot.run.profiles=sit &

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 15

# Test endpoints
echo -e "\n🔍 Testing application endpoints:"
echo "API Endpoint: http://localhost:8080/api/status"
echo "Web Dashboard: http://localhost:8080/"

# Check if application is running
if curl -s http://localhost:8080/api/status > /dev/null; then
    echo -e "\n✅ Application is running successfully!"
    echo "📊 Dashboard: http://localhost:8080/"
    echo "🔗 API: http://localhost:8080/api/status"
    echo -e "\n📋 Database Status Summary:"
    curl -s http://localhost:8080/api/status | jq '.databases[] | {name: .name, status: .status, host: .host}' 2>/dev/null || echo "Install jq for better JSON output"
else
    echo "❌ Application failed to start or is not responding"
fi