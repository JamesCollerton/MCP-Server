package org.springframework.ai.mcp.sample.server.prompts;

import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class McpServerPrompts {

    @McpPrompt(name = "greeting", description = "A simple greeting prompt")
    public McpSchema.GetPromptResult greetingPrompt(
            @McpArg(name = "name", description = "The name to greet", required = true) String name) {
        return new McpSchema.GetPromptResult("Greeting", List.of(new McpSchema.PromptMessage(McpSchema.Role.ASSISTANT,
                new McpSchema.TextContent("Hello, " + name + "! Welcome to the MCP system."))));
    }

}
