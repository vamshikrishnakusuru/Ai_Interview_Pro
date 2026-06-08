package com.aiinterview.controller;

import com.aiinterview.entity.Answer;
import com.aiinterview.entity.Interview;
import com.aiinterview.entity.Question;
import com.aiinterview.repository.InterviewRepository;
import com.aiinterview.service.AdaptiveInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/interview")
public class AdaptiveInterviewController {

    @Autowired
    private AdaptiveInterviewService adaptiveInterviewService;

    @Autowired
    private InterviewRepository interviewRepository;

    @PostMapping("/next-question")
    public Mono<ResponseEntity<Question>> getNextQuestion(@RequestParam Long sessionId) {
        return adaptiveInterviewService.generateNextQuestion(sessionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @PostMapping("/evaluate-answer")
    public Mono<ResponseEntity<Answer>> evaluateAnswer(
            @RequestParam Long questionId,
            @RequestBody Map<String, Object> body) {
        
        String answerText = (String) body.getOrDefault("answerText", "");
        Integer durationSeconds = (Integer) body.getOrDefault("durationSeconds", 0);

        return adaptiveInterviewService.evaluateCandidateAnswer(questionId, answerText, durationSeconds)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/context/{sessionId}")
    public ResponseEntity<?> getSessionContext(@PathVariable Long sessionId) {
        return interviewRepository.findById(sessionId)
                .map(interview -> {
                    Map<String, Object> context = new HashMap<>();
                    context.put("sessionId", interview.getId());
                    context.put("difficultyLevel", interview.getDifficultyLevel());
                    context.put("overallFeedback", interview.getOverallFeedback());
                    context.put("contextJson", interview.getInterviewContext());
                    return ResponseEntity.ok(context);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/adaptive-summary/{sessionId}")
    public ResponseEntity<?> getAdaptiveSummary(@PathVariable Long sessionId) {
        return interviewRepository.findById(sessionId)
                .map(interview -> {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("sessionId", interview.getId());
                    summary.put("jobTitle", interview.getJobTitle());
                    summary.put("averageScore", interview.getAverageScore());
                    summary.put("difficultyLevel", interview.getDifficultyLevel());
                    summary.put("overallFeedback", interview.getOverallFeedback());
                    
                    List<Map<String, Object>> timeline = interview.getQuestions().stream()
                            .map(q -> {
                                Map<String, Object> item = new HashMap<>();
                                item.put("id", q.getId());
                                item.put("questionId", q.getId());
                                item.put("questionText", q.getQuestionText());
                                item.put("category", q.getCategory());
                                item.put("difficulty", q.getDifficultyLevel());
                                item.put("isFollowUp", q.getIsFollowUp());
                                item.put("adaptiveReason", q.getAdaptiveReason());
                                
                                Answer ans = q.getAnswer();
                                if (ans != null) {
                                    item.put("score", ans.getAiScore());
                                    item.put("confidenceScore", ans.getConfidenceScore());
                                    item.put("confidenceLevel", ans.getConfidenceLevel());
                                    item.put("technicalAccuracy", ans.getTechnicalAccuracy());
                                }
                                return item;
                            }).collect(Collectors.toList());

                    summary.put("timeline", timeline);
                    return ResponseEntity.ok(summary);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
