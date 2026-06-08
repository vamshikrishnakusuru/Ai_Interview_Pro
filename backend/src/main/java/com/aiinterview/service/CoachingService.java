package com.aiinterview.service;

import com.aiinterview.dto.AnalyticsDTOs.*;
import com.aiinterview.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachingService {

    private final AnalyticsService analyticsService;

    public CoachingService(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public String generateCoachSummary(User user) {
        SummaryResponse summary = analyticsService.getSummary(user);

        if (summary.getTotalInterviews() == 0) {
            return "Start your first interview to receive personalized AI coaching!";
        }

        StringBuilder coach = new StringBuilder();

        // 1. Overall assessment
        if (summary.getAverageScore() >= 8.0) {
            coach.append("Excellent work! You are performing at a high level across most categories. ");
        } else if (summary.getAverageScore() >= 5.0) {
            coach.append("You have a solid foundation, but there's room for technical refinement in key areas. ");
        } else {
            coach.append("Focus on strengthening your core fundamentals before your next mock session. ");
        }

        // 2. Skill specific
        if (summary.getStrongestSkill() != null) {
            coach.append("Your expertise in ").append(summary.getStrongestSkill()).append(" is a major strength. ");
        }

        if (summary.getWeakestSkill() != null && !summary.getWeakestSkill().equals(summary.getStrongestSkill())) {
            coach.append("However, I recommend focusing more on ").append(summary.getWeakestSkill()).append(" to balance your profile. ");
        }

        // 3. Concept advice
        if (!summary.getMissingConcepts().isEmpty()) {
            coach.append("Reviewing concepts like ").append(summary.getMissingConcepts().get(0))
                 .append(" could significantly boost your score.");
        }

        return coach.toString();
    }
}
