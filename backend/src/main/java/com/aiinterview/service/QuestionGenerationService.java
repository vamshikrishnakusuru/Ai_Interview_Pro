package com.aiinterview.service;

import com.aiinterview.entity.Skill;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionGenerationService {

    private static final Map<String, List<String>> SKILL_QUESTIONS = new HashMap<>();

    static {
        SKILL_QUESTIONS.put("Java", Arrays.asList(
            "Explain OOPS concepts in Java.",
            "Difference between JVM, JRE and JDK.",
            "Explain the Java Memory Model and Garbage Collection.",
            "What are the new features in Java 17 and Java 21?"
        ));
        SKILL_QUESTIONS.put("Spring Boot", Arrays.asList(
            "Explain dependency injection and Inversion of Control in Spring Boot.",
            "Difference between @Component, @Service, and @Repository.",
            "How do you handle exceptions globally in Spring Boot?",
            "Explain the Spring Boot application lifecycle."
        ));
        SKILL_QUESTIONS.put("React", Arrays.asList(
            "What are React hooks and why are they used?",
            "Explain the Virtual DOM and how React updates the UI.",
            "Difference between functional and class components.",
            "How do you manage state in a complex React application?"
        ));
        SKILL_QUESTIONS.put("MySQL", Arrays.asList(
            "Explain the difference between JOIN types in SQL.",
            "What are indexes and how do they improve performance?",
            "Explain ACID properties in database transactions.",
            "How do you optimize a slow database query?"
        ));
        SKILL_QUESTIONS.put("Python", Arrays.asList(
            "Difference between list and tuple in Python.",
            "Explain decorators in Python.",
            "How is memory managed in Python?",
            "What are generators and how do they work?"
        ));
        SKILL_QUESTIONS.put("AWS", Arrays.asList(
            "Explain the difference between EC2, S3, and Lambda.",
            "What is an IAM role and how is it used?",
            "How do you ensure high availability in AWS?",
            "Difference between horizontal and vertical scaling."
        ));
    }

    private static final List<String> GENERIC_QUESTIONS = Arrays.asList(
        "Tell me about your greatest professional achievement.",
        "How do you handle conflict within a team?",
        "Where do you see yourself in five years?",
        "Why are you interested in this role?"
    );

    public List<String> generateQuestions(Set<Skill> skills, int count) {
        List<String> pool = new ArrayList<>();
        
        for (Skill skill : skills) {
            List<String> questions = SKILL_QUESTIONS.get(skill.getName());
            if (questions != null) {
                pool.addAll(questions);
            }
        }

        if (pool.isEmpty()) {
            pool.addAll(GENERIC_QUESTIONS);
        }

        Collections.shuffle(pool);
        
        // Return up to 'count' questions
        return pool.subList(0, Math.min(count, pool.size()));
    }
}
