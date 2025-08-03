package com.mcp.mcpserver;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpserverApplication {

    private static final Logger logger = LoggerFactory.getLogger(McpserverApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(McpserverApplication.class, args);
        
        // Debug: Print all bean names to see what's registered
        logger.info("=== Registered Beans ===");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("mcp") || 
                beanName.toLowerCase().contains("tool") ||
                beanName.toLowerCase().contains("controller")) {
                logger.info("Found bean: {}", beanName);
            }
        }
        logger.info("=== End Beans ===");
    }

	@Bean
	public ToolCallbackProvider weatherTools(WeatherService weatherService) {
		return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
	}
}