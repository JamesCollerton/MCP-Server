#!/usr/bin/env python3
import json
import subprocess
import sys

def test_mcp_server():
    """Test the MCP server through STDIO"""
    
    # MCP server JAR path
    jar_path = "example-mcp-server/starter-webflux-server/target/mcp-weather-starter-webflux-server-0.0.1-SNAPSHOT.jar"
    
    # Initialize message
    init_message = {
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
    }
    
    print("Testing MCP server through STDIO...")
    print(f"Sending initialize message: {json.dumps(init_message, indent=2)}")
    
    try:
        # Start the MCP server process
        process = subprocess.Popen(
            ["java", "-jar", jar_path],
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        
        # Send the initialize message
        input_data = json.dumps(init_message) + "\n"
        stdout, stderr = process.communicate(input=input_data, timeout=10)
        
        print("Response from MCP server:")
        print("STDOUT:", stdout)
        if stderr:
            print("STDERR:", stderr)
            
    except subprocess.TimeoutExpired:
        print("Process timed out")
        process.kill()
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    test_mcp_server() 