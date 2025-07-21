#!/bin/bash

echo "=== Testing Database Connection Status Application ==="
echo "Waiting for application to start..."
sleep 15

echo -e "\n=== Testing API Endpoint ==="
echo "GET /api/status"
curl -s http://localhost:8080/api/status | jq '.' || echo "API not ready yet"

echo -e "\n\n=== Testing Web Dashboard ==="
echo "GET /"
curl -s http://localhost:8080/ | head -10

echo -e "\n\n=== Application Status ==="
if curl -s http://localhost:8080/actuator/health &>/dev/null; then
    echo "✅ Application is running"
    echo "📊 Dashboard: http://localhost:8080/"
    echo "🔗 API: http://localhost:8080/api/status"
else
    echo "❌ Application is not responding"
fi