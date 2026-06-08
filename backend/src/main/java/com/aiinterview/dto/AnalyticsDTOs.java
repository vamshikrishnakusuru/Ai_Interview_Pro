package com.aiinterview.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AnalyticsDTOs {

    public static class SummaryResponse {
        private Double averageScore;
        private String strongestSkill;
        private String weakestSkill;
        private Integer totalInterviews;
        private String personalCoachSummary;
        private List<String> missingConcepts;
        private Map<String, Long> scoreDistribution; // Weak, Moderate, Strong

        // Getters and Setters
        public Double getAverageScore() { return averageScore; }
        public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }
        public String getStrongestSkill() { return strongestSkill; }
        public void setStrongestSkill(String strongestSkill) { this.strongestSkill = strongestSkill; }
        public String getWeakestSkill() { return weakestSkill; }
        public void setWeakestSkill(String weakestSkill) { this.weakestSkill = weakestSkill; }
        public Integer getTotalInterviews() { return totalInterviews; }
        public void setTotalInterviews(Integer totalInterviews) { this.totalInterviews = totalInterviews; }
        public String getPersonalCoachSummary() { return personalCoachSummary; }
        public void setPersonalCoachSummary(String personalCoachSummary) { this.personalCoachSummary = personalCoachSummary; }
        public List<String> getMissingConcepts() { return missingConcepts; }
        public void setMissingConcepts(List<String> missingConcepts) { this.missingConcepts = missingConcepts; }
        public Map<String, Long> getScoreDistribution() { return scoreDistribution; }
        public void setScoreDistribution(Map<String, Long> scoreDistribution) { this.scoreDistribution = scoreDistribution; }
    }

    public static class TrendData {
        private LocalDateTime date;
        private Double score;
        private String title;

        public TrendData(LocalDateTime date, Double score, String title) {
            this.date = date;
            this.score = score;
            this.title = title;
        }

        public LocalDateTime getDate() { return date; }
        public Double getScore() { return score; }
        public String getTitle() { return title; }
    }

    public static class SkillPerformance {
        private String skill;
        private Double averageScore;
        private Integer count;

        public SkillPerformance(String skill, Double averageScore, Integer count) {
            this.skill = skill;
            this.averageScore = averageScore;
            this.count = count;
        }

        public String getSkill() { return skill; }
        public Double getAverageScore() { return averageScore; }
        public Integer getCount() { return count; }
    }

    public static class InterviewHistoryItem {
        private Long id;
        private String date;
        private String jobTitle;
        private Double score;
        private String strongestSkill;
        private String status;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getJobTitle() { return jobTitle; }
        public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
        public String getStrongestSkill() { return strongestSkill; }
        public void setStrongestSkill(String strongestSkill) { this.strongestSkill = strongestSkill; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
