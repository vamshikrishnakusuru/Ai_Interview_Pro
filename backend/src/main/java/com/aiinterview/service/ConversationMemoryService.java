package com.aiinterview.service;

import com.aiinterview.entity.Answer;
import com.aiinterview.entity.Interview;
import com.aiinterview.entity.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConversationMemoryService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String buildMemoryState(Interview interview) {
        try {
            List<Question> questions = interview.getQuestions();
            List<String> discussedSkills = new ArrayList<>();
            List<String> strengths = new ArrayList<>();
            List<String> weaknesses = new ArrayList<>();
            List<String> keywords = new ArrayList<>();

            for (Question q : questions) {
                if (q.getCategory() != null && !discussedSkills.contains(q.getCategory())) {
                    discussedSkills.add(q.getCategory());
                }
                Answer ans = q.getAnswer();
                if (ans != null) {
                    // Strengths analysis
                    if (ans.getAiScore() != null && ans.getAiScore() >= 8) {
                        strengths.add("Strong performance in " + q.getCategory() + " (" + q.getQuestionText() + ")");
                    }
                    // Weakness analysis
                    if (ans.getAiScore() != null && ans.getAiScore() < 5) {
                        weaknesses.add("Struggled in " + q.getCategory() + " (" + q.getQuestionText() + ")");
                        if (ans.getMissingConcepts() != null && !ans.getMissingConcepts().isEmpty()) {
                            weaknesses.add("Missing concepts: " + ans.getMissingConcepts());
                        }
                    }
                    // Extract technical keywords from candidate answer
                    extractKeywords(ans.getAnswerText(), keywords);
                }
            }

            Map<String, Object> memoryMap = new HashMap<>();
            memoryMap.put("discussedSkills", discussedSkills);
            memoryMap.put("provenStrengths", strengths);
            memoryMap.put("weaknesses", weaknesses);
            memoryMap.put("mentionedKeywords", keywords);

            return objectMapper.writeValueAsString(memoryMap);
        } catch (Exception e) {
            System.err.println("!!! FAILED TO COMPILE MEMORY STATE: " + e.getMessage());
            return "{}";
        }
    }

    private void extractKeywords(String answerText, List<String> keywords) {
        if (answerText == null || answerText.trim().isEmpty()) return;
        String[] terms = {"spring boot", "react", "mysql", "docker", "aws", "kubernetes", "jpa", "redux", "garbage collection", "decorators", "decorators", "acid", "lambda", "jwt"};
        String textLower = answerText.toLowerCase();
        for (String term : terms) {
            if (textLower.contains(term) && !keywords.contains(term)) {
                keywords.add(term);
            }
        }
    }
}
