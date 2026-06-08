package com.aiinterview.service;

import com.aiinterview.dto.AIResponseDTO;
import com.aiinterview.dto.QuestionResponseDTO;
import com.aiinterview.entity.Answer;
import com.aiinterview.entity.Interview;
import com.aiinterview.entity.Question;
import com.aiinterview.entity.Resume;
import com.aiinterview.repository.AnswerRepository;
import com.aiinterview.repository.InterviewRepository;
import com.aiinterview.repository.QuestionRepository;
import com.aiinterview.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdaptiveInterviewService {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ConversationMemoryService memoryService;

    @Autowired
    private DifficultyEvaluationService difficultyService;

    @Autowired
    private PromptBuilderService promptBuilderService;

    @Autowired
    private GeminiService geminiService;

    /**
     * Triggers dynamic generation of the next question based on interview context and memory.
     */
    public Mono<Question> generateNextQuestion(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview session not found"));

        // 1. Get Resume skills context
        String detectedSkills = resumeRepository.findFirstByUserOrderByUploadedAtDesc(interview.getUser())
                .map(Resume::getDetectedSkills)
                .orElse(Collections.emptySet())
                .stream()
                .map(com.aiinterview.entity.Skill::getName)
                .collect(Collectors.joining(", "));

        if (detectedSkills.isEmpty()) {
            detectedSkills = "General Software Engineering, Web Development";
        }

        // 2. If it's the very first question, generate warm-up
        if (interview.getQuestions().isEmpty()) {
            String initialPrompt = promptBuilderService.buildInitialQuestionPrompt(interview.getJobTitle(), detectedSkills);
            return geminiService.generateDynamicQuestion(initialPrompt)
                    .map(dto -> saveQuestion(interview, dto, 1));
        }

        // 3. Evolve conversation memory state
        String memoryState = memoryService.buildMemoryState(interview);
        interview.setInterviewContext(memoryState);

        // 4. Calculate next target difficulty
        String targetDifficulty = difficultyService.determineNextDifficulty(interview);
        interview.setDifficultyLevel(targetDifficulty);
        interviewRepository.save(interview);

        // 5. Construct custom prompt and generate next question
        String nextPrompt = promptBuilderService.buildAdaptiveQuestionPrompt(
                interview, detectedSkills, targetDifficulty, memoryState
        );

        int nextOrderIndex = interview.getQuestions().size() + 1;

        return geminiService.generateDynamicQuestion(nextPrompt)
                .map(dto -> saveQuestion(interview, dto, nextOrderIndex));
    }

    /**
     * Evaluates candidate's answer using Gemini, updates DB, computes confidence metrics, and recalculates aggregate stats.
     */
    public Mono<Answer> evaluateCandidateAnswer(Long questionId, String answerText, Integer durationSeconds) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Interview interview = question.getInterview();

        Answer answer = question.getAnswer();
        if (answer == null) {
            answer = Answer.builder()
                    .interview(interview)
                    .question(question)
                    .answerText(answerText)
                    .durationSeconds(durationSeconds)
                    .build();
        } else {
            answer.setAnswerText(answerText);
            answer.setDurationSeconds(durationSeconds);
        }

        final Answer finalAnswer = answerRepository.save(answer);

        return geminiService.evaluateAnswer(question.getQuestionText(), answerText)
                .map(feedback -> {
                    finalAnswer.setAiScore(feedback.getScore());
                    finalAnswer.setConfidenceLevel(feedback.getConfidence());
                    finalAnswer.setTechnicalAccuracy(feedback.getTechnicalAccuracy());
                    finalAnswer.setCommunicationFeedback(feedback.getCommunication());
                    finalAnswer.setMissingConcepts(feedback.getMissingConcepts() != null ? String.join(", ", feedback.getMissingConcepts()) : "");
                    finalAnswer.setSuggestions(feedback.getSuggestions() != null ? String.join(", ", feedback.getSuggestions()) : "");

                    // Estimate numeric confidence score (0-100)
                    Integer numericConfidence = difficultyService.calculateConfidenceScore(
                            answerText, durationSeconds, feedback.getConfidence()
                    );
                    finalAnswer.setConfidenceScore(numericConfidence);

                    Answer saved = answerRepository.save(finalAnswer);

                    // Re-calculate session average
                    updateInterviewAggregateStats(interview.getId());

                    return saved;
                });
    }

    private Question saveQuestion(Interview interview, QuestionResponseDTO dto, int orderIndex) {
        Question lastQ = interview.getQuestions().isEmpty() ? null : interview.getQuestions().get(interview.getQuestions().size() - 1);
        
        Question question = Question.builder()
                .interview(interview)
                .questionText(dto.getQuestionText())
                .category(dto.getCategory())
                .difficulty(dto.getDifficulty())
                .difficultyLevel(dto.getDifficulty())
                .isFollowUp(dto.getIsFollowUp() != null && dto.getIsFollowUp())
                .parentQuestionId(lastQ != null ? lastQ.getId() : null)
                .adaptiveReason(dto.getAdaptiveReason())
                .orderIndex(orderIndex)
                .build();

        return questionRepository.save(question);
    }

    private void updateInterviewAggregateStats(Long interviewId) {
        interviewRepository.findById(interviewId).ifPresent(interview -> {
            List<Answer> answers = answerRepository.findByInterview(interview);
            if (!answers.isEmpty()) {
                double avg = answers.stream()
                        .filter(a -> a.getAiScore() != null)
                        .mapToDouble(Answer::getAiScore)
                        .average()
                        .orElse(0.0);
                interview.setAverageScore(Math.round(avg * 10.0) / 10.0);
                interview.setTotalQuestions(answers.size());

                // Set overall session feedback
                if (avg >= 8.0) {
                    interview.setOverallFeedback("Exceptional performance. Strong understanding of concepts.");
                } else if (avg >= 5.0) {
                    interview.setOverallFeedback("Solid fundamental knowledge with opportunities to deepen understanding.");
                } else {
                    interview.setOverallFeedback("Struggling with core concepts. Targeted revision is recommended.");
                }

                interviewRepository.save(interview);
            }
        });
    }
}
