package com.aiinterview.controller;

import com.aiinterview.entity.Resume;
import com.aiinterview.entity.User;
import com.aiinterview.repository.ResumeRepository;
import com.aiinterview.repository.UserRepository;
import com.aiinterview.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import com.aiinterview.service.ResumeParserService;
import com.aiinterview.service.SkillExtractionService;
import com.aiinterview.entity.Skill;
import java.util.Set;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeParserService resumeParserService;

    @Autowired
    private SkillExtractionService skillExtractionService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow();

        try {
            // 1. Extract Text
            String extractedText = resumeParserService.extractText(file);
            
            // 2. Detect Skills
            Set<Skill> skills = skillExtractionService.extractSkills(extractedText);

            // 3. Save Resume with Skills
            Resume resume = Resume.builder()
                    .user(user)
                    .fileName(file.getOriginalFilename())
                    .filePath("/uploads/" + file.getOriginalFilename())
                    .extractedText(extractedText)
                    .detectedSkills(skills)
                    .build();

            resumeRepository.save(resume);

            return ResponseEntity.ok("Resume analyzed. Found " + skills.size() + " skills!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error parsing resume: " + e.getMessage());
        }
    }
}
