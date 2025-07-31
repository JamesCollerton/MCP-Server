package com.mcp.mcpclient;

import java.util.List;
import java.util.Scanner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.modelcontextprotocol.client.McpSyncClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpclientApplication {


	private static final String userInput = "What is the weather in Tokyo?";
	
	public static void main(String[] args) {
		SpringApplication.run(McpclientApplication.class, args);
	}

	@Bean
	public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools,
			ConfigurableApplicationContext context) {

		return args -> {

			var chatClient = chatClientBuilder
					.defaultToolCallbacks(tools)
					.build();

			System.out.println("\n>>> QUESTION: " + userInput);
			System.out.println("\n>>> ASSISTANT: " + chatClient.prompt(userInput).call().content());

			context.close();
		};
	}

}
