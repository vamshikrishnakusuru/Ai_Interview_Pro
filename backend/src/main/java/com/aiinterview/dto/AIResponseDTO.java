package com.aiinterview.dto;

import java.util.List;

public class AIResponseDTO {
    private int score;
    private String confidence;
    private String technicalAccuracy;
    private String communication;
    private List<String> missingConcepts;
    private List<String> suggestions;

    public AIResponseDTO() {}

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }
    public String getTechnicalAccuracy() { return technicalAccuracy; }
    public void setTechnicalAccuracy(String technicalAccuracy) { this.technicalAccuracy = technicalAccuracy; }
    public String getCommunication() { return communication; }
    public void setCommunication(String communication) { this.communication = communication; }
    public List<String> getMissingConcepts() { return missingConcepts; }
    public void setMissingConcepts(List<String> missingConcepts) { this.missingConcepts = missingConcepts; }
    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
}
