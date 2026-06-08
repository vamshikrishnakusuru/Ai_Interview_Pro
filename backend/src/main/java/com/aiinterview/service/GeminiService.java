package com.aiinterview.service;

import com.aiinterview.dto.AIResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class GeminiService {

    private final java.net.http.HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    private boolean isAiEnabled = true;

    public GeminiService() {
        this.httpClient = java.net.http.HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.contains("YOUR_GEMINI_API_KEY") || apiKey.equals("AIzaSyDliVAO41hLZaiv3qeZCtV1hfBrkUrRSvs")) {
            System.err.println("!!! GEMINI SERVICE: API Key is missing or invalid. AI features will run in OFFLINE/FALLBACK mode.");
            this.isAiEnabled = false;
        } else {
            System.out.println(">>> GEMINI SERVICE: AI features initialized successfully.");
            this.isAiEnabled = true;
        }
    }

    public Mono<AIResponseDTO> evaluateAnswer(String question, String answer) {
        if (!isAiEnabled) {
            System.out.println(">>> GEMINI SERVICE: AI is disabled. Returning offline fallback evaluation.");
            return Mono.just(createFallbackResponse());
        }
        return Mono.fromCallable(() -> {
            System.out.println(">>> CALLING GOOGLE AI (HTTP CLIENT)...");
            String prompt = String.format(
                "You are an AI Interviewer. Evaluate this answer.\n\n" +
                "Question: %s\n" +
                "Answer: %s\n\n" +
                "Return ONLY a JSON object with: score(number), confidence(string), technicalAccuracy(string), communication(string), missingConcepts(array of strings), suggestions(array of strings).",
                question, answer
            );

            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(
                        Map.of("text", prompt)
                    ))
                )
            );

            String jsonPayload = objectMapper.writeValueAsString(requestBody);
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey.trim();

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("!!! GOOGLE API ERROR [" + response.statusCode() + "]: " + response.body());
                return createFallbackResponse();
            }

            System.out.println(">>> RAW RESPONSE FROM GOOGLE: " + response.body());
            return parseGeminiResponse(response.body());
        }).onErrorResume(e -> {
            System.err.println("!!! CRITICAL HTTP FAILURE: " + e.getMessage());
            return Mono.just(createFallbackResponse());
        });
    }

    public Mono<com.aiinterview.dto.QuestionResponseDTO> generateDynamicQuestion(String prompt) {
        if (!isAiEnabled) {
            System.out.println(">>> GEMINI SERVICE: AI is disabled. Returning offline fallback question.");
            return Mono.just(createFallbackQuestion());
        }
        return Mono.fromCallable(() -> {
            System.out.println(">>> CALLING GOOGLE AI FOR DYNAMIC QUESTION GENERATION...");
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(
                        Map.of("text", prompt)
                    ))
                )
            );

            String jsonPayload = objectMapper.writeValueAsString(requestBody);
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey.trim();

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("!!! GOOGLE API ERROR [" + response.statusCode() + "]: " + response.body());
                return createFallbackQuestion();
            }

            System.out.println(">>> RAW QUESTION RESPONSE FROM GOOGLE: " + response.body());
            return parseQuestionResponse(response.body());
        }).onErrorResume(e -> {
            System.err.println("!!! CRITICAL HTTP QUESTION GEN FAILURE: " + e.getMessage());
            return Mono.just(createFallbackQuestion());
        });
    }

    private com.aiinterview.dto.QuestionResponseDTO parseQuestionResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");
            
            String textResponse = textNode.asText();
            int start = textResponse.indexOf("{");
            int end = textResponse.lastIndexOf("}");
            if (start != -1 && end != -1) {
                textResponse = textResponse.substring(start, end + 1);
            }
            
            return objectMapper.readValue(textResponse, com.aiinterview.dto.QuestionResponseDTO.class);
        } catch (Exception e) {
            System.err.println("!!! QUESTION PARSING ERROR: " + e.getMessage());
            return createFallbackQuestion();
        }
    }

    private com.aiinterview.dto.QuestionResponseDTO createFallbackQuestion() {
        return new com.aiinterview.dto.QuestionResponseDTO(
            "Could you explain your general experience working with modern web application frameworks and APIs?",
            "General",
            "MEDIUM",
            false,
            "Fallback triggered due to structural formatting issue."
        );
    }

    private AIResponseDTO parseGeminiResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");
            
            String textResponse = textNode.asText();
            int start = textResponse.indexOf("{");
            int end = textResponse.lastIndexOf("}");
            if (start != -1 && end != -1) {
                textResponse = textResponse.substring(start, end + 1);
            }
            
            return objectMapper.readValue(textResponse, AIResponseDTO.class);
        } catch (Exception e) {
            System.err.println("!!! PARSING ERROR: " + e.getMessage());
            return createFallbackResponse();
        }
    }

    private AIResponseDTO createFallbackResponse() {
        AIResponseDTO fallback = new AIResponseDTO();
        fallback.setScore(0);
        fallback.setConfidence("Error");
        fallback.setTechnicalAccuracy("Evaluation failed due to system error.");
        fallback.setCommunication("N/A");
        fallback.setMissingConcepts(List.of("ERROR EVALUATING CONCEPTS"));
        fallback.setSuggestions(List.of("Please try again later"));
        return fallback;
    }
}
