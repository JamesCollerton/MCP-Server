package org.springframework.ai.mcp.sample.server;

import io.modelcontextprotocol.server.McpServerFeatures;
import org.springaicommunity.mcp.spring.SyncMcpAnnotationProvider;
import org.springframework.ai.mcp.sample.server.prompts.McpServerPrompts;
import org.springframework.ai.mcp.sample.server.resources.McpServerResources;
import org.springframework.ai.mcp.sample.server.tools.McpServerTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class McpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpServerApplication.class, args);
	}

	@Bean
	public List<McpServerFeatures.SyncToolSpecification> toolSpecs(McpServerTools mcpServerTools) {
		return SyncMcpAnnotationProvider.createSyncToolSpecifications(List.of(mcpServerTools));
	}

	@Bean
	public List<McpServerFeatures.SyncResourceSpecification> resourceSpecs(McpServerResources mcpServerResources) {
		return SyncMcpAnnotationProvider.createSyncResourceSpecifications(List.of(mcpServerResources));
	}

	@Bean
	public List<McpServerFeatures.SyncPromptSpecification> promptSpecs(McpServerPrompts mcpServerPrompts) {
		return SyncMcpAnnotationProvider.createSyncPromptSpecifications(List.of(mcpServerPrompts));
	}

}
