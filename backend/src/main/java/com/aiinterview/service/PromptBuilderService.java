package com.aiinterview.service;

import com.aiinterview.entity.Interview;
import com.aiinterview.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptBuilderService {

    public String buildInitialQuestionPrompt(String jobTitle, String detectedSkills) {
        return "You are an elite technical interviewer. Generate the first question of the interview.\n" +
                "Job Title: " + jobTitle + "\n" +
                "Candidate Skills: " + detectedSkills + "\n" +
                "Target Difficulty: EASY (Warm-up phase)\n\n" +
                "Generate a relevant, open-ended question to start the interview.\n" +
                "Response MUST be a valid JSON object matching this exact schema:\n" +
                "{\n" +
                "  \"questionText\": \"The question itself\",\n" +
                "  \"category\": \"The specific skill/topic category (e.g. Java, React, SQL, General)\",\n" +
                "  \"difficulty\": \"EASY\",\n" +
                "  \"isFollowUp\": false,\n" +
                "  \"adaptiveReason\": \"First warm-up question based on job role.\"\n" +
                "}\n" +
                "Do NOT include markdown, backticks, or wrapping text outside of the JSON block.";
    }

    public String buildAdaptiveQuestionPrompt(Interview interview, String detectedSkills, String targetDifficulty, String memoryState) {
        List<Question> questions = interview.getQuestions();
        Question lastQuestion = questions.isEmpty() ? null : questions.get(questions.size() - 1);
        String lastAnswerText = (lastQuestion != null && lastQuestion.getAnswer() != null) ? lastQuestion.getAnswer().getAnswerText() : "N/A";

        return "You are an elite, adaptive technical interviewer. Assess the current state and generate the next question.\n\n" +
                "--- ROLE & SESSION CONTEXT ---\n" +
                "Job Title: " + interview.getJobTitle() + "\n" +
                "Candidate Resume Skills: " + detectedSkills + "\n" +
                "Target Difficulty: " + targetDifficulty + "\n\n" +
                "--- INTERVIEW MEMORY STATE (JSON) ---\n" +
                memoryState + "\n\n" +
                "--- LAST INTERACTION ---\n" +
                "AI Question: \"" + (lastQuestion != null ? lastQuestion.getQuestionText() : "") + "\"\n" +
                "Candidate Answer: \"" + lastAnswerText + "\"\n\n" +
                "--- INSTRUCTIONS FOR ADAPTATION & CONVERSATION ---\n" +
                "1. If the candidate struggled with the previous topic (score < 5), ask a foundational or supportive follow-up question in the same category to clarify or guide them.\n" +
                "2. If the candidate did extremely well (score >= 8), pivot to an advanced scenario in the same category, or introduce a new skill from the resume.\n" +
                "3. If the candidate mentioned specific technologies or architectural choices in their last answer (e.g., 'Spring Boot', 'REST APIs', 'Redux'), prioritize a conversational follow-up question exploring that mention (e.g., 'How did you structure that?'). Set \"isFollowUp\": true.\n" +
                "4. Check the discussedSkills list in the memory state. DO NOT ask the same question or cover the exact same concept repeatedly.\n\n" +
                "Response MUST be a valid JSON object matching this exact schema:\n" +
                "{\n" +
                "  \"questionText\": \"The generated question\",\n" +
                "  \"category\": \"The target skill category (e.g. Java, React, MySQL, Python, AWS, General)\",\n" +
                "  \"difficulty\": \"" + targetDifficulty + "\",\n" +
                "  \"isFollowUp\": true/false,\n" +
                "  \"adaptiveReason\": \"Detailed rationale explaining why this question/difficulty was chosen based on the candidate's performance or keywords.\"\n" +
                "}\n" +
                "Do NOT include any markdown, backticks, or wrapping text outside of the JSON block.";
    }
}
