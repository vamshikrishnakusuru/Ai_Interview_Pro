package com.aiinterview.controller;

import com.aiinterview.entity.Interview;
import com.aiinterview.entity.Question;
import com.aiinterview.entity.User;
import com.aiinterview.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    @Autowired
    private com.aiinterview.repository.InterviewRepository interviewRepository;

    @Autowired
    private com.aiinterview.repository.UserRepository userRepository;

    @Autowired
    private com.aiinterview.repository.ResumeRepository resumeRepository;

    @Autowired
    private com.aiinterview.service.QuestionGenerationService questionGenerationService;

    @PostMapping("/start")
    public ResponseEntity<?> startInterview(@RequestParam String jobTitle) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow();

        Interview interview = Interview.builder()
                .user(user)
                .jobTitle(jobTitle)
                .status(Interview.InterviewStatus.ONGOING)
                .questions(new java.util.ArrayList<>())
                .difficultyLevel("MEDIUM")
                .build();

        Interview saved = interviewRepository.save(interview);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getInterview(@PathVariable Long id) {
        return interviewRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeInterview(@PathVariable Long id) {
        return interviewRepository.findById(id)
                .map(interview -> {
                    interview.setStatus(Interview.InterviewStatus.COMPLETED);
                    interviewRepository.save(interview);
                    return ResponseEntity.ok("Interview completed successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
