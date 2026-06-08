package com.aiinterview.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interviews")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String jobTitle;
    
    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    private Double averageScore;
    @Column(columnDefinition = "TEXT")
    private String overallFeedback;
    private Integer totalQuestions;

    @Column(name = "difficulty_level")
    private String difficultyLevel = "MEDIUM";

    @Column(name = "interview_context", columnDefinition = "LONGTEXT")
    private String interviewContext;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    public enum InterviewStatus {
        PENDING, ONGOING, COMPLETED, CANCELLED
    }

    public Interview() {}

    public Interview(User user, String jobTitle, InterviewStatus status, List<Question> questions) {
        this.user = user;
        this.jobTitle = jobTitle;
        this.status = status;
        this.questions = questions != null ? questions : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public Double getAverageScore() { return averageScore; }
    public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }
    public String getOverallFeedback() { return overallFeedback; }
    public void setOverallFeedback(String overallFeedback) { this.overallFeedback = overallFeedback; }
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public String getInterviewContext() { return interviewContext; }
    public void setInterviewContext(String interviewContext) { this.interviewContext = interviewContext; }

    public static InterviewBuilder builder() {
        return new InterviewBuilder();
    }

    public static class InterviewBuilder {
        private User user;
        private String jobTitle;
        private InterviewStatus status;
        private List<Question> questions;
        private String difficultyLevel = "MEDIUM";
        private String interviewContext;

        public InterviewBuilder user(User user) { this.user = user; return this; }
        public InterviewBuilder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public InterviewBuilder status(InterviewStatus status) { this.status = status; return this; }
        public InterviewBuilder questions(List<Question> questions) { this.questions = questions; return this; }
        public InterviewBuilder difficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; return this; }
        public InterviewBuilder interviewContext(String interviewContext) { this.interviewContext = interviewContext; return this; }
        
        public Interview build() {
            Interview inter = new Interview(user, jobTitle, status, questions);
            inter.setDifficultyLevel(difficultyLevel);
            inter.setInterviewContext(interviewContext);
            return inter;
        }
    }
}
