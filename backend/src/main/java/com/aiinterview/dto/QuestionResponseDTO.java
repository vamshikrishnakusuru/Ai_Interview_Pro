package com.aiinterview.dto;

public class QuestionResponseDTO {
    private String questionText;
    private String category;
    private String difficulty;
    private Boolean isFollowUp = false;
    private String adaptiveReason;

    public QuestionResponseDTO() {}

    public QuestionResponseDTO(String questionText, String category, String difficulty, Boolean isFollowUp, String adaptiveReason) {
        this.questionText = questionText;
        this.category = category;
        this.difficulty = difficulty;
        this.isFollowUp = isFollowUp;
        this.adaptiveReason = adaptiveReason;
    }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Boolean getIsFollowUp() { return isFollowUp; }
    public void setIsFollowUp(Boolean isFollowUp) { this.isFollowUp = isFollowUp; }
    public String getAdaptiveReason() { return adaptiveReason; }
    public void setAdaptiveReason(String adaptiveReason) { this.adaptiveReason = adaptiveReason; }
}
