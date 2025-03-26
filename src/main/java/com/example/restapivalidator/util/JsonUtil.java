package com.example.restapivalidator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final int JSON_MAX_DEPTH = 8;
    public static void validateJsonSchemaDepth(String jsonStr) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonStr);
            int depth = getJsonDepth(rootNode, 1);
            if (depth > JSON_MAX_DEPTH)
                throw new IllegalArgumentException("JSON is too deeply nested (max depth: " + JSON_MAX_DEPTH + ")");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage());
        }
    }
    private static int getJsonDepth(JsonNode node, int depth) {
        if (!node.isContainerNode())
            return depth;
        int maxDepth = depth;
        for (JsonNode child : node) {
            maxDepth = Math.max(getJsonDepth(child, depth + 1), maxDepth);
        }
        return maxDepth;
    }
}
