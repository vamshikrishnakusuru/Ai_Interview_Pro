package com.aiinterview.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    private String audioUrl;
    private Integer durationSeconds;
    // AI Feedback Fields
    private Integer aiScore;
    private String confidenceLevel;
    private String technicalAccuracy;
    private String communicationFeedback;
    @Column(columnDefinition = "TEXT")
    private String missingConcepts;
    @Column(columnDefinition = "TEXT")
    private String suggestions;

    @Column(name = "confidence_score")
    private Integer confidenceScore;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Answer() {}

    public Answer(Question question, Interview interview, String answerText, String audioUrl, Integer durationSeconds) {
        this.question = question;
        this.interview = interview;
        this.answerText = answerText;
        this.audioUrl = audioUrl;
        this.durationSeconds = durationSeconds;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public Interview getInterview() { return interview; }
    public void setInterview(Interview interview) { this.interview = interview; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getAiScore() { return aiScore; }
    public void setAiScore(Integer aiScore) { this.aiScore = aiScore; }
    public String getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(String confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    public String getTechnicalAccuracy() { return technicalAccuracy; }
    public void setTechnicalAccuracy(String technicalAccuracy) { this.technicalAccuracy = technicalAccuracy; }
    public String getCommunicationFeedback() { return communicationFeedback; }
    public void setCommunicationFeedback(String communicationFeedback) { this.communicationFeedback = communicationFeedback; }
    public String getMissingConcepts() { return missingConcepts; }
    public void setMissingConcepts(String missingConcepts) { this.missingConcepts = missingConcepts; }
    public String getSuggestions() { return suggestions; }
    public void setSuggestions(String suggestions) { this.suggestions = suggestions; }

    public Integer getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Integer confidenceScore) { this.confidenceScore = confidenceScore; }

    public static AnswerBuilder builder() {
        return new AnswerBuilder();
    }

    public static class AnswerBuilder {
        private Question question;
        private Interview interview;
        private String answerText;
        private String audioUrl;
        private Integer durationSeconds;
        private Integer aiScore;
        private String confidenceLevel;
        private String technicalAccuracy;
        private String communicationFeedback;
        private String missingConcepts;
        private String suggestions;
        private Integer confidenceScore;

        public AnswerBuilder question(Question question) { this.question = question; return this; }
        public AnswerBuilder interview(Interview interview) { this.interview = interview; return this; }
        public AnswerBuilder answerText(String answerText) { this.answerText = answerText; return this; }
        public AnswerBuilder audioUrl(String audioUrl) { this.audioUrl = audioUrl; return this; }
        public AnswerBuilder durationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public AnswerBuilder aiScore(Integer aiScore) { this.aiScore = aiScore; return this; }
        public AnswerBuilder confidenceLevel(String confidenceLevel) { this.confidenceLevel = confidenceLevel; return this; }
        public AnswerBuilder technicalAccuracy(String technicalAccuracy) { this.technicalAccuracy = technicalAccuracy; return this; }
        public AnswerBuilder communicationFeedback(String communicationFeedback) { this.communicationFeedback = communicationFeedback; return this; }
        public AnswerBuilder missingConcepts(String missingConcepts) { this.missingConcepts = missingConcepts; return this; }
        public AnswerBuilder suggestions(String suggestions) { this.suggestions = suggestions; return this; }
        public AnswerBuilder confidenceScore(Integer confidenceScore) { this.confidenceScore = confidenceScore; return this; }

        public Answer build() {
            Answer ans = new Answer(question, interview, answerText, audioUrl, durationSeconds);
            ans.setAiScore(aiScore);
            ans.setConfidenceLevel(confidenceLevel);
            ans.setTechnicalAccuracy(technicalAccuracy);
            ans.setCommunicationFeedback(communicationFeedback);
            ans.setMissingConcepts(missingConcepts);
            ans.setSuggestions(suggestions);
            ans.setConfidenceScore(confidenceScore);
            return ans;
        }
    }
}
