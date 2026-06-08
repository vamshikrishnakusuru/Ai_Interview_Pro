package com.aiinterview.controller;

import com.aiinterview.dto.AnalyticsDTOs.*;
import com.aiinterview.entity.User;
import com.aiinterview.repository.UserRepository;
import com.aiinterview.security.UserDetailsImpl;
import com.aiinterview.service.AnalyticsService;
import com.aiinterview.service.CoachingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final CoachingService coachingService;
    private final UserRepository userRepository;

    public AnalyticsController(AnalyticsService analyticsService, CoachingService coachingService, UserRepository userRepository) {
        this.analyticsService = analyticsService;
        this.coachingService = coachingService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId()).orElseThrow();
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary() {
        SummaryResponse summary = analyticsService.getSummary(getCurrentUser());
        summary.setPersonalCoachSummary(coachingService.generateCoachSummary(getCurrentUser()));
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/trends")
    public ResponseEntity<List<TrendData>> getTrends(@RequestParam(defaultValue = "all") String period) {
        return ResponseEntity.ok(analyticsService.getTrends(getCurrentUser(), period));
    }

    @GetMapping("/skills")
    public ResponseEntity<List<SkillPerformance>> getSkills() {
        return ResponseEntity.ok(analyticsService.getSkillPerformance(getCurrentUser()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<InterviewHistoryItem>> getHistory() {
        return ResponseEntity.ok(analyticsService.getHistory(getCurrentUser()));
    }
}
