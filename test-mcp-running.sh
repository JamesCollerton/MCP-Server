#!/bin/bash

echo "Testing MCP server that's already running..."

# Test 1: Initialize connection
echo "=== Test 1: Initialize MCP connection ==="
curl -s -N -H "Accept: text/event-stream" \
     -H "Content-Type: application/json" \
     http://localhost:8081/mcp/messages \
     -d '{
       "jsonrpc": "2.0",
       "id": 1,
       "method": "initialize",
       "params": {
         "protocolVersion": "2024-11-05",
         "capabilities": {
           "tools": {}
         },
         "clientInfo": {
           "name": "test-client",
           "version": "1.0.0"
         }
       }
     }'

echo -e "\n\n=== Test 2: List available tools ==="
curl -s -N -H "Accept: text/event-stream" \
     -H "Content-Type: application/json" \
     http://localhost:8081/mcp/messages \
     -d '{
       "jsonrpc": "2.0",
       "id": 2,
       "method": "tools/list",
       "params": {}
     }'

echo -e "\n\n=== Test 3: Call weather forecast tool ==="
curl -s -N -H "Accept: text/event-stream" \
     -H "Content-Type: application/json" \
     http://localhost:8081/mcp/messages \
     -d '{
       "jsonrpc": "2.0",
       "id": 3,
       "method": "tools/call",
       "params": {
         "name": "getWeatherForecastByLocation",
         "arguments": {
           "latitude": 47.6062,
           "longitude": -122.3321
         }
       }
     }'

echo -e "\n\n=== Test 4: Call alerts tool ==="
curl -s -N -H "Accept: text/event-stream" \
     -H "Content-Type: application/json" \
     http://localhost:8081/mcp/messages \
     -d '{
       "jsonrpc": "2.0",
       "id": 4,
       "method": "tools/call",
       "params": {
         "name": "getAlerts",
         "arguments": {
           "state": "NY"
         }
       }
     }'

echo -e "\n\nMCP server tests completed!" 