package com.aiinterview.service;

import com.aiinterview.entity.Skill;
import com.aiinterview.repository.SkillRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SkillExtractionService {

    @Autowired
    private SkillRepository skillRepository;

    private static final List<String> PREDEFINED_SKILLS = Arrays.asList(
            "Java", "Spring Boot", "React", "MySQL", "Python", "Machine Learning", 
            "TensorFlow", "Node.js", "MongoDB", "AWS", "Docker", "Kubernetes", 
            "JavaScript", "TypeScript", "HTML", "CSS", "SQL", "PostgreSQL", 
            "Redux", "Kafka", "Redis", "Angular", "Vue", "Spring Security", "Microservices"
    );

    @PostConstruct
    public void init() {
        for (String skillName : PREDEFINED_SKILLS) {
            if (skillRepository.findByNameIgnoreCase(skillName).isEmpty()) {
                skillRepository.save(new Skill(skillName));
            }
        }
    }

    public Set<Skill> extractSkills(String text) {
        if (text == null || text.isBlank()) return Collections.emptySet();

        Set<String> foundSkillNames = new HashSet<>();
        String lowercaseText = text.toLowerCase();

        for (String skillName : PREDEFINED_SKILLS) {
            // Simple word boundary check
            String pattern = "\\b" + Pattern.quote(skillName.toLowerCase()) + "\\b";
            if (Pattern.compile(pattern).matcher(lowercaseText).find()) {
                foundSkillNames.add(skillName);
            }
        }

        return foundSkillNames.stream()
                .map(name -> skillRepository.findByNameIgnoreCase(name).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
