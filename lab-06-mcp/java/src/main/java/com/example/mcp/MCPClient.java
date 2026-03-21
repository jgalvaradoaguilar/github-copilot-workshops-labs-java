package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

import java.time.Duration;
import java.util.Map;

public class MCPClient {
    
    public static void main(String[] args) {
        try {
            runClient();
        } catch (Exception e) {
            System.err.println("Error running MCP client: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runClient() throws Exception {
        // Create JSON mapper
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(objectMapper);
        
        // Create server parameters for the stdio transport
        ServerParameters serverParams = ServerParameters.builder("java")
            .args("-jar", "target/mcp-demo-1.0-SNAPSHOT.jar")
            .build();
        
        // Create STDIO transport to connect to server
        StdioClientTransport transport = new StdioClientTransport(serverParams, jsonMapper);
        
        // Create sync client
        McpSyncClient client = McpClient.sync(transport)
            .clientInfo(new McpSchema.Implementation("demo-client", "1.0.0"))
            .requestTimeout(Duration.ofSeconds(30))
            .build();
        
        // Initialize the client (connects to server)
        client.initialize();
        
        System.out.println("Connected to MCP Server\n");
        
        // List available tools
        System.out.println("== TOOLS ==");
        ListToolsResult toolsResult = client.listTools();
        for (Tool tool : toolsResult.tools()) {
            System.out.println(" - " + tool.name() + ": " + tool.description());
        }
        
        // Test tool calls
        System.out.println("\n== TOOL CALLS ==");
        
        testToolCall(client, "add", Map.of("a", 1, "b", 7));
        testToolCall(client, "multiply", Map.of("a", 3, "b", 9));
        testToolCall(client, "divide", Map.of("a", 42, "b", 7));
        testToolCall(client, "subtract", Map.of("a", 100, "b", 42));
        
        // Error demo
        System.out.println("\n== ERROR DEMO (divide by zero) ==");
        testToolCall(client, "divide", Map.of("a", 1, "b", 0));
        
        // Cleanup
        client.close();
        System.out.println("\nClient closed.");
    }
    
    private static void testToolCall(McpSyncClient client, String toolName, Map<String, Object> args) {
        try {
            CallToolResult result = client.callTool(new McpSchema.CallToolRequest(toolName, args));
            if (result.content() != null && !result.content().isEmpty()) {
                Object content = result.content().get(0);
                if (content instanceof TextContent textContent) {
                    System.out.println(toolName + " " + args + " => " + textContent.text());
                } else {
                    System.out.println(toolName + " " + args + " => " + content);
                }
            }
        } catch (Exception e) {
            System.out.println(toolName + " " + args + " => ERROR: " + e.getMessage());
        }
    }
}
