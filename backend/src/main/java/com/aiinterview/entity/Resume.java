package com.aiinterview.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "resumes")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String fileName;
    private String filePath;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resume_skills",
               joinColumns = @JoinColumn(name = "resume_id"),
               inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> detectedSkills = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    public Resume() {}

    public Resume(User user, String fileName, String filePath, String extractedText) {
        this.user = user;
        this.fileName = fileName;
        this.filePath = filePath;
        this.extractedText = extractedText;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public Set<Skill> getDetectedSkills() { return detectedSkills; }
    public void setDetectedSkills(Set<Skill> detectedSkills) { this.detectedSkills = detectedSkills; }

    public static ResumeBuilder builder() {
        return new ResumeBuilder();
    }

    public static class ResumeBuilder {
        private User user;
        private String fileName;
        private String filePath;
        private String extractedText;
        private Set<Skill> detectedSkills;

        public ResumeBuilder user(User user) { this.user = user; return this; }
        public ResumeBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public ResumeBuilder filePath(String filePath) { this.filePath = filePath; return this; }
        public ResumeBuilder extractedText(String extractedText) { this.extractedText = extractedText; return this; }
        public ResumeBuilder detectedSkills(Set<Skill> detectedSkills) { this.detectedSkills = detectedSkills; return this; }

        public Resume build() {
            Resume resume = new Resume(user, fileName, filePath, extractedText);
            if (this.detectedSkills != null) resume.setDetectedSkills(this.detectedSkills);
            return resume;
        }
    }
}
