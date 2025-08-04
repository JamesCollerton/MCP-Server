package org.springframework.ai.mcp.sample.server.resources;

import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class McpServerResources {

    @McpResource(uri = "mcp-server://{name}", name = "MCP Server Metadata", description = "Provides metadata on the server")
    public McpSchema.ReadResourceResult getUserProfile(McpSchema.ReadResourceRequest request, String name) {
        return new McpSchema.ReadResourceResult(List.of(new McpSchema.TextResourceContents(request.uri(), "text/plain", formatMetadata(name))));
    }

    private String formatMetadata(String name) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("name", "MCP Server");
        metadata.put("caller", name);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString().trim();
    }

}
