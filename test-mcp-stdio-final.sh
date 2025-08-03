#!/bin/bash

echo "Testing MCP server through STDIO (final version)..."

# Build the JAR
cd example-mcp-server/starter-webflux-server
# echo "Building JAR..."
# mvn clean package -DskipTests > /dev/null 2>&1

echo "Starting MCP server in STDIO mode..."

# Create a test file with all messages
cat > /tmp/mcp-messages.txt << 'EOF'
{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{"tools":{}},"clientInfo":{"name":"test-client","version":"1.0.0"}}}
{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}
{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"getWeatherForecastByLocation","arguments":{"latitude":47.6062,"longitude":-122.3321}}}
{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"getAlerts","arguments":{"state":"NY"}}}
EOF

echo "Sending MCP messages and capturing responses..."
echo "================================================"

# Run the server and send messages, capturing output
java -Dspring.main.web-application-type=none -jar target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar < /tmp/mcp-messages.txt 2>&1

echo "================================================"
echo "MCP STDIO test completed!"

# Clean up
# rm -f /tmp/mcp-messages.txt 