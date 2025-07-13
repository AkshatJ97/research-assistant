package com.research.assistant;

public class ResearchRequest {
    private String content;
    private ResearchOperation operation;

    // Getters and setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public ResearchOperation getOperation() { return operation; }
    public void setOperation(ResearchOperation operation) { this.operation = operation; }
}
