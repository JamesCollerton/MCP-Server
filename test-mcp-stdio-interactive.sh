#!/bin/bash

echo "Testing MCP server through STDIO (interactive mode)..."

# Build the JAR
cd example-mcp-server/starter-webflux-server
echo "Building JAR..."
mvn clean package -DskipTests > /dev/null 2>&1

echo "Starting MCP server in STDIO mode..."
echo "Sending test messages..."

# Create a named pipe for communication
PIPE_NAME="/tmp/mcp-pipe"
rm -f "$PIPE_NAME"
mkfifo "$PIPE_NAME"

# Start the server in background, reading from the pipe
java -Dspring.main.web-application-type=none -jar target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar < "$PIPE_NAME" &
SERVER_PID=$!

# Wait a moment for server to start
sleep 3

echo "=== Sending Initialize Message ==="
echo '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{"tools":{}},"clientInfo":{"name":"test-client","version":"1.0.0"}}}' > "$PIPE_NAME"

sleep 2

echo "=== Sending List Tools Message ==="
echo '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}' > "$PIPE_NAME"

sleep 2

echo "=== Sending Weather Forecast Tool Call ==="
echo '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"getWeatherForecastByLocation","arguments":{"latitude":47.6062,"longitude":-122.3321}}}' > "$PIPE_NAME"

sleep 2

echo "=== Sending Alerts Tool Call ==="
echo '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"getAlerts","arguments":{"state":"NY"}}}' > "$PIPE_NAME"

sleep 2

# Clean up
kill $SERVER_PID 2>/dev/null
rm -f "$PIPE_NAME"

echo "STDIO test completed!" 