package com.research.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Map;

@Service
public class ResearchService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest request) {
        // Handle citation in Java, others via Gemini
        if (request.getOperation() == ResearchOperation.CITE) {
            return generateCitation(request.getContent());
        }

        String prompt = buildPrompt(request);
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractTextFromResponse(response);
    }

    private String buildPrompt(ResearchRequest request) {
        switch (request.getOperation()) {
            case SUMMARIZE:
                return "Clear and concise summary of the following text in a few sentences:\n\n" + request.getContent();
            case SUGGEST:
                return "Based on the following content, suggest related topics and further reading. Provide clear headings and bullet points:\n\n" + request.getContent();
            case PARAPHRASE:
                return "Paraphrase the following text in simpler language:\n\n" + request.getContent();
            case DEFINE:
                return "Provide the definition and meaning of the following word or phrase:\n\n" + request.getContent();
            default:
                throw new IllegalArgumentException("Invalid operation: " + request.getOperation());
        }
    }

    private String extractTextFromResponse(String response) {
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                return geminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
            }
            return "No Content Found";
        } catch (Exception e) {
            return "Error Parsing Response: " + e.getMessage();
        }
    }

    // Simple APA citation generator for demonstration
    private String generateCitation(String content) {
        // Expecting content = "title|url" or just url
        String[] parts = content.split("\\|");
        String title = parts.length > 1 ? parts[0] : "Untitled";
        String url = parts.length > 1 ? parts[1] : parts[0];
        String year = String.valueOf(LocalDate.now().getYear());
        return String.format("APA: %s. (%s). Retrieved from %s", title, year, url);
    }
}
