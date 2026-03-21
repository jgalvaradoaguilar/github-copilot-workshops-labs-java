package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpSchema.JsonSchema;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;

import java.util.List;
import java.util.Map;

/**
 * MCP Server implementation using the official SDK 0.16.0
 */
public class MCPServer {
    
    public static void main(String[] args) {
        System.err.println("MCP Server starting with tools...");
        
        // Create JSON mapper for transport - configure to ignore unknown fields
        // This is required for compatibility with VS Code Copilot which may send
        // newer protocol fields that the SDK doesn't recognize yet
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(objectMapper);
        
        // Create STDIO transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(jsonMapper);
        
        // Create and configure sync server with tools using the toolCall API
        McpServer.sync(transportProvider)
            .serverInfo("demo-mcp-server", "1.0.0")
            .toolCall(createAddTool(), (exchange, request) -> {
                Map<String, Object> args2 = request.arguments();
                int a = ((Number) args2.get("a")).intValue();
                int b = ((Number) args2.get("b")).intValue();
                int result = a + b;
                return CallToolResult.builder()
                    .content(List.of(new TextContent(String.valueOf(result))))
                    .build();
            })
            .toolCall(createSubtractTool(), (exchange, request) -> {
                Map<String, Object> args2 = request.arguments();
                int a = ((Number) args2.get("a")).intValue();
                int b = ((Number) args2.get("b")).intValue();
                int result = a - b;
                return CallToolResult.builder()
                    .content(List.of(new TextContent(String.valueOf(result))))
                    .build();
            })
            .toolCall(createMultiplyTool(), (exchange, request) -> {
                Map<String, Object> args2 = request.arguments();
                int a = ((Number) args2.get("a")).intValue();
                int b = ((Number) args2.get("b")).intValue();
                int result = a * b;
                return CallToolResult.builder()
                    .content(List.of(new TextContent(String.valueOf(result))))
                    .build();
            })
            .toolCall(createDivideTool(), (exchange, request) -> {
                Map<String, Object> args2 = request.arguments();
                int a = ((Number) args2.get("a")).intValue();
                int b = ((Number) args2.get("b")).intValue();
                if (b == 0) {
                    return CallToolResult.builder()
                        .content(List.of(new TextContent("Error: Division by zero")))
                        .isError(true)
                        .build();
                }
                double result = (double) a / b;
                return CallToolResult.builder()
                    .content(List.of(new TextContent(String.valueOf(result))))
                    .build();
            })
            .build();
        
        System.err.println("Available tools: add, subtract, multiply, divide");
        System.err.println("Server is running...");
    }

    private static Tool createAddTool() {
        return Tool.builder()
            .name("add")
            .description("Add two numbers")
            .inputSchema(new JsonSchema(
                "object",
                Map.of(
                    "a", Map.of("type", "integer", "description", "First number"),
                    "b", Map.of("type", "integer", "description", "Second number")
                ),
                List.of("a", "b"),
                false,
                null,
                null
            ))
            .build();
    }
    
    private static Tool createSubtractTool() {
        return Tool.builder()
            .name("subtract")
            .description("Subtract two numbers")
            .inputSchema(new JsonSchema(
                "object",
                Map.of(
                    "a", Map.of("type", "integer", "description", "First number"),
                    "b", Map.of("type", "integer", "description", "Second number")
                ),
                List.of("a", "b"),
                false,
                null,
                null
            ))
            .build();
    }
    
    private static Tool createMultiplyTool() {
        return Tool.builder()
            .name("multiply")
            .description("Multiply two numbers")
            .inputSchema(new JsonSchema(
                "object",
                Map.of(
                    "a", Map.of("type", "integer", "description", "First number"),
                    "b", Map.of("type", "integer", "description", "Second number")
                ),
                List.of("a", "b"),
                false,
                null,
                null
            ))
            .build();
    }
    
    private static Tool createDivideTool() {
        return Tool.builder()
            .name("divide")
            .description("Divide two numbers")
            .inputSchema(new JsonSchema(
                "object",
                Map.of(
                    "a", Map.of("type", "integer", "description", "Numerator"),
                    "b", Map.of("type", "integer", "description", "Denominator")
                ),
                List.of("a", "b"),
                false,
                null,
                null
            ))
            .build();
    }
}
