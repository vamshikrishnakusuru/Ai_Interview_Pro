package com.aiinterview.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @Column(columnDefinition = "TEXT")
    private String questionText;

    private String category;
    private String difficulty;
    private String audioUrl;
    private Integer orderIndex;

    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @Column(name = "is_follow_up")
    private Boolean isFollowUp = false;

    @Column(name = "parent_question_id")
    private Long parentQuestionId;

    @Column(name = "adaptive_reason", columnDefinition = "TEXT")
    private String adaptiveReason;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer;

    public Question() {}

    public Question(Interview interview, String questionText, String category, String difficulty, String audioUrl, Integer orderIndex) {
        this.interview = interview;
        this.questionText = questionText;
        this.category = category;
        this.difficulty = difficulty;
        this.audioUrl = audioUrl;
        this.orderIndex = orderIndex;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Interview getInterview() { return interview; }
    public void setInterview(Interview interview) { this.interview = interview; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Answer getAnswer() { return answer; }
    public void setAnswer(Answer answer) { this.answer = answer; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public Boolean getIsFollowUp() { return isFollowUp; }
    public void setIsFollowUp(Boolean isFollowUp) { this.isFollowUp = isFollowUp; }
    public Long getParentQuestionId() { return parentQuestionId; }
    public void setParentQuestionId(Long parentQuestionId) { this.parentQuestionId = parentQuestionId; }
    public String getAdaptiveReason() { return adaptiveReason; }
    public void setAdaptiveReason(String adaptiveReason) { this.adaptiveReason = adaptiveReason; }

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

    public static class QuestionBuilder {
        private Interview interview;
        private String questionText;
        private String category;
        private String difficulty;
        private String audioUrl;
        private Integer orderIndex;
        private String difficultyLevel;
        private Boolean isFollowUp = false;
        private Long parentQuestionId;
        private String adaptiveReason;

        public QuestionBuilder interview(Interview interview) { this.interview = interview; return this; }
        public QuestionBuilder questionText(String questionText) { this.questionText = questionText; return this; }
        public QuestionBuilder category(String category) { this.category = category; return this; }
        public QuestionBuilder difficulty(String difficulty) { this.difficulty = difficulty; return this; }
        public QuestionBuilder audioUrl(String audioUrl) { this.audioUrl = audioUrl; return this; }
        public QuestionBuilder orderIndex(Integer orderIndex) { this.orderIndex = orderIndex; return this; }
        public QuestionBuilder difficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; return this; }
        public QuestionBuilder isFollowUp(Boolean isFollowUp) { this.isFollowUp = isFollowUp; return this; }
        public QuestionBuilder parentQuestionId(Long parentQuestionId) { this.parentQuestionId = parentQuestionId; return this; }
        public QuestionBuilder adaptiveReason(String adaptiveReason) { this.adaptiveReason = adaptiveReason; return this; }

        public Question build() {
            Question q = new Question(interview, questionText, category, difficulty, audioUrl, orderIndex);
            q.setDifficultyLevel(difficultyLevel);
            q.setIsFollowUp(isFollowUp);
            q.setParentQuestionId(parentQuestionId);
            q.setAdaptiveReason(adaptiveReason);
            return q;
        }
    }
}
