package org.springframework.ai.mcp.sample.server;

import io.modelcontextprotocol.server.McpServerFeatures;
import org.springaicommunity.mcp.spring.SyncMcpAnnotationProvider;
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

//	@Bean
//	public ToolCallbackProvider mcpServerTools(McpServerTools mcpServerTools) {
//		return MethodToolCallbackProvider.builder().toolObjects(mcpServerTools).build();
//	}

	@Bean
	public List<McpServerFeatures.SyncToolSpecification> toolSpecs(McpServerTools toolProvider) {
		return SyncMcpAnnotationProvider.createSyncToolSpecifications(List.of(toolProvider));
	}
}
