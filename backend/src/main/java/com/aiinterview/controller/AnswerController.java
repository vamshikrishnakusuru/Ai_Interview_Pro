package com.aiinterview.controller;

import com.aiinterview.dto.AnswerRequest;
import com.aiinterview.entity.Answer;
import com.aiinterview.entity.Interview;
import com.aiinterview.entity.Question;
import com.aiinterview.repository.AnswerRepository;
import com.aiinterview.repository.InterviewRepository;
import com.aiinterview.repository.QuestionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AnswerController.class);

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private com.aiinterview.service.GeminiService geminiService;

    @PostMapping
    public ResponseEntity<?> saveAnswer(@Valid @RequestBody AnswerRequest request) {
        System.out.println(">>> RECEIVED ANSWER FOR QUESTION: " + request.getQuestionId());
        Interview interview = interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Answer answer = Answer.builder()
                .interview(interview)
                .question(question)
                .answerText(request.getAnswerText())
                .durationSeconds(request.getDurationSeconds())
                .build();

        Answer saved = answerRepository.save(answer);
        
        // Asynchronously evaluate using Gemini
        geminiService.evaluateAnswer(question.getQuestionText(), request.getAnswerText())
            .subscribe(feedback -> {
                saved.setAiScore(feedback.getScore());
                saved.setConfidenceLevel(feedback.getConfidence());
                saved.setTechnicalAccuracy(feedback.getTechnicalAccuracy());
                saved.setCommunicationFeedback(feedback.getCommunication());
                saved.setMissingConcepts(String.join(", ", feedback.getMissingConcepts()));
                saved.setSuggestions(String.join(", ", feedback.getSuggestions()));
                answerRepository.save(saved);

                // Update Interview level stats
                updateInterviewStats(interview.getId());
            });

        logger.info("Answer saved and evaluation triggered for interview {} and question {}", request.getInterviewId(), request.getQuestionId());
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/evaluate")
    public Mono<com.aiinterview.dto.AIResponseDTO> evaluateAnswer(@RequestBody java.util.Map<String, String> request) {
        String question = request.get("question");
        String answer = request.get("answer");
        return geminiService.evaluateAnswer(question, answer);
    }

    private void updateInterviewStats(Long interviewId) {
        interviewRepository.findById(interviewId).ifPresent(interview -> {
            java.util.List<Answer> answers = answerRepository.findByInterview(interview);
            if (!answers.isEmpty()) {
                double avg = answers.stream()
                        .filter(a -> a.getAiScore() != null)
                        .mapToDouble(Answer::getAiScore)
                        .average()
                        .orElse(0.0);
                interview.setAverageScore(Math.round(avg * 10.0) / 10.0);
                
                // Aggregated feedback (simplified)
                if (avg >= 8.0) interview.setOverallFeedback("Strong Performance");
                else if (avg >= 5.0) interview.setOverallFeedback("Moderate Performance");
                else interview.setOverallFeedback("Needs Improvement");

                interviewRepository.save(interview);
            }
        });
    }
}
