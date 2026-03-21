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

public class MCPServer {
    
    public static void main(String[] args) {
        System.err.println("MCP Server starting...");
        
        // Create JSON mapper for transport - configure to ignore unknown fields
        // This is required for compatibility with VS Code Copilot
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(objectMapper);
        
        // Create STDIO transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(jsonMapper);
        
        // Create and configure sync server with basic info
        McpServer.sync(transportProvider)
            .serverInfo("demo-mcp-server", "1.0.0")
            .build();
        
        System.err.println("Server is running...");
    }
}
