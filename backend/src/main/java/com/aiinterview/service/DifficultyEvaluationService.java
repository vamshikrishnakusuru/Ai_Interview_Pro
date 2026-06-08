package com.aiinterview.service;

import com.aiinterview.entity.Answer;
import com.aiinterview.entity.Interview;
import com.aiinterview.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DifficultyEvaluationService {

    /**
     * Determines the next question's difficulty level based on recent candidate responses.
     * Rules:
     * - Average score >= 8.0: HARD (or scale up: EASY -> MEDIUM -> HARD)
     * - Average score < 5.0: EASY (or scale down: HARD -> MEDIUM -> EASY)
     * - Otherwise: MEDIUM
     */
    public String determineNextDifficulty(Interview interview) {
        List<Question> questions = interview.getQuestions();
        if (questions == null || questions.isEmpty()) {
            return "EASY"; // Start with EASY/warmup questions
        }

        // Get the latest evaluated answers
        double totalScore = 0;
        int evaluatedCount = 0;
        String currentDifficulty = interview.getDifficultyLevel() != null ? interview.getDifficultyLevel() : "MEDIUM";

        for (Question q : questions) {
            Answer ans = q.getAnswer();
            if (ans != null && ans.getAiScore() != null) {
                totalScore += ans.getAiScore();
                evaluatedCount++;
            }
        }

        if (evaluatedCount == 0) {
            return currentDifficulty; // Keep current if no answers evaluated yet
        }

        double average = totalScore / evaluatedCount;

        if (average >= 8.0) {
            if ("EASY".equals(currentDifficulty)) return "MEDIUM";
            return "HARD";
        } else if (average < 5.0) {
            if ("HARD".equals(currentDifficulty)) return "MEDIUM";
            return "EASY";
        } else {
            return "MEDIUM";
        }
    }

    /**
     * Calculates a granular confidence score percentage (0-100) based on answer length,
     * time taken, and the textual confidence metadata returned by Gemini.
     */
    public Integer calculateConfidenceScore(String answerText, Integer durationSeconds, String aiConfidenceLevel) {
        if (answerText == null || answerText.trim().isEmpty()) {
            return 0;
        }

        int score = 50; // Start at baseline 50

        // Length factors
        int wordCount = answerText.trim().split("\\s+").length;
        if (wordCount > 40) {
            score += 15;
        } else if (wordCount < 10) {
            score -= 15;
        }

        // Duration factors (avoiding extremely fast or extremely slow/hesitant answers)
        if (durationSeconds != null && durationSeconds > 0) {
            double wordsPerSecond = (double) wordCount / durationSeconds;
            if (wordsPerSecond > 0.8 && wordsPerSecond < 2.5) {
                score += 15; // Natural speech speed
            } else if (wordsPerSecond < 0.4) {
                score -= 15; // Slow, indicating hesitation or pausing
            }
        }

        // Gemini textual confidence factors
        if (aiConfidenceLevel != null) {
            String cleanConf = aiConfidenceLevel.toUpperCase();
            if (cleanConf.contains("HIGH")) {
                score += 20;
            } else if (cleanConf.contains("LOW")) {
                score -= 20;
            }
        }

        // Clamp between 0 and 100
        return Math.max(0, Math.min(100, score));
    }
}
