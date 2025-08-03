#!/bin/bash

echo "Testing MCP server in STDIO mode..."

# Build the JAR
cd example-mcp-server/starter-webflux-server
echo "Building JAR..."
mvn clean package -DskipTests > /dev/null 2>&1

# Test with web application type disabled (STDIO mode)
echo "Running MCP server in STDIO mode..."

# Create a test script that sends multiple messages
cat > /tmp/mcp-test.sh << 'EOF'
#!/bin/bash

# Initialize message
echo '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{"tools":{}},"clientInfo":{"name":"test-client","version":"1.0.0"}}}'

# List tools message
echo '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}'

# Call weather tool message
echo '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"getWeatherForecastByLocation","arguments":{"latitude":47.6062,"longitude":-122.3321}}}'

# Wait a bit
sleep 1
EOF

chmod +x /tmp/mcp-test.sh

# Run the server in STDIO mode and send messages
echo "Starting MCP server and sending test messages..."
java -Dspring.main.web-application-type=none -jar target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar < /tmp/mcp-test.sh

# Clean up
rm -f /tmp/mcp-test.sh

echo "STDIO test completed!" 