package com.aiinterview.service;

import com.aiinterview.dto.AnalyticsDTOs.*;
import com.aiinterview.entity.Answer;
import com.aiinterview.entity.Interview;
import com.aiinterview.entity.User;
import com.aiinterview.repository.InterviewRepository;
import com.aiinterview.util.SkillCategory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final InterviewRepository interviewRepository;

    public AnalyticsService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public SummaryResponse getSummary(User user) {
        List<Interview> interviews = interviewRepository.findByUserOrderByCreatedAtDesc(user);
        if (interviews.isEmpty()) return new SummaryResponse();

        SummaryResponse summary = new SummaryResponse();
        summary.setTotalInterviews(interviews.size());

        // Calculate Average Score across all interviews that have a score
        double avg = interviews.stream()
                .filter(i -> i.getAverageScore() != null)
                .mapToDouble(Interview::getAverageScore)
                .average()
                .orElse(0.0);
        summary.setAverageScore(Math.round(avg * 10.0) / 10.0);

        // Score Distribution
        Map<String, Long> distribution = new HashMap<>();
        long weak = 0, moderate = 0, strong = 0;
        for (Interview i : interviews) {
            if (i.getAverageScore() == null) continue;
            if (i.getAverageScore() < 5.0) weak++;
            else if (i.getAverageScore() < 8.0) moderate++;
            else strong++;
        }
        distribution.put("Weak (0-4)", weak);
        distribution.put("Moderate (5-7)", moderate);
        distribution.put("Strong (8-10)", strong);
        summary.setScoreDistribution(distribution);

        // Missing Concepts aggregation
        List<String> allMissing = interviews.stream()
                .flatMap(i -> i.getQuestions().stream())
                .map(q -> q.getAnswer())
                .filter(a -> a != null && a.getMissingConcepts() != null)
                .map(Answer::getMissingConcepts)
                .flatMap(s -> Arrays.stream(s.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.equalsIgnoreCase("None"))
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
        summary.setMissingConcepts(allMissing);

        // Find Strongest/Weakest Skills
        List<SkillPerformance> skills = getSkillPerformance(user);
        if (!skills.isEmpty()) {
            skills.sort(Comparator.comparing(SkillPerformance::getAverageScore).reversed());
            summary.setStrongestSkill(skills.get(0).getSkill());
            summary.setWeakestSkill(skills.get(skills.size() - 1).getSkill());
        }

        return summary;
    }

    public List<TrendData> getTrends(User user, String period) {
        List<Interview> interviews = interviewRepository.findByUserOrderByCreatedAtAsc(user);
        
        // Filter based on period if necessary (simplified for now to last 10 sessions)
        int limit = period.equalsIgnoreCase("all") ? interviews.size() : 10;
        int skip = Math.max(0, interviews.size() - limit);

        return interviews.stream()
                .skip(skip)
                .filter(i -> i.getAverageScore() != null)
                .map(i -> new TrendData(i.getCreatedAt(), i.getAverageScore(), i.getJobTitle()))
                .collect(Collectors.toList());
    }

    public List<SkillPerformance> getSkillPerformance(User user) {
        List<Interview> interviews = interviewRepository.findByUser(user);
        
        Map<SkillCategory, List<Integer>> skillScores = new HashMap<>();

        for (Interview interview : interviews) {
            for (com.aiinterview.entity.Question q : interview.getQuestions()) {
                if (q.getAnswer() != null && q.getAnswer().getAiScore() != null) {
                    SkillCategory cat = SkillCategory.normalize(q.getCategory());
                    skillScores.computeIfAbsent(cat, k -> new ArrayList<>()).add(q.getAnswer().getAiScore());
                }
            }
        }

        return skillScores.entrySet().stream()
                .map(entry -> {
                    double avg = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    return new SkillPerformance(entry.getKey().name(), Math.round(avg * 10.0) / 10.0, entry.getValue().size());
                })
                .collect(Collectors.toList());
    }

    public List<InterviewHistoryItem> getHistory(User user) {
        List<Interview> interviews = interviewRepository.findByUserOrderByCreatedAtDesc(user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        return interviews.stream().map(i -> {
            InterviewHistoryItem item = new InterviewHistoryItem();
            item.setId(i.getId());
            item.setDate(i.getCreatedAt().format(formatter));
            item.setJobTitle(i.getJobTitle());
            item.setScore(i.getAverageScore());
            item.setStatus(i.getStatus().name());
            
            // Best skill in THIS specific interview
            String best = i.getQuestions().stream()
                    .filter(q -> q.getAnswer() != null && q.getAnswer().getAiScore() != null)
                    .max(Comparator.comparing(q -> q.getAnswer().getAiScore()))
                    .map(q -> q.getCategory())
                    .orElse("N/A");
            item.setStrongestSkill(best);
            
            return item;
        }).collect(Collectors.toList());
    }
}
