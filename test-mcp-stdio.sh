#!/bin/bash

echo "Testing MCP server through STDIO..."

# Build the JAR first
cd example-mcp-server/starter-webflux-server
echo "Building JAR..."
mvn clean package -DskipTests > /dev/null 2>&1

# Test the MCP server through STDIO
echo "Testing MCP protocol through STDIO..."

# Create a temporary file with the initialize message
cat > /tmp/mcp-init.json << 'EOF'
{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{"tools":{}},"clientInfo":{"name":"test-client","version":"1.0.0"}}}
EOF

# Send the initialize message and capture response
echo "Sending initialize message..."
java -jar target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar < /tmp/mcp-init.json > /tmp/mcp-response.txt 2>&1 &

# Wait a moment for the server to process
sleep 2

# Check if we got a response
if [ -s /tmp/mcp-response.txt ]; then
    echo "Response received:"
    cat /tmp/mcp-response.txt
else
    echo "No response received"
fi

# Clean up
rm -f /tmp/mcp-init.json /tmp/mcp-response.txt

echo "STDIO test completed!" 