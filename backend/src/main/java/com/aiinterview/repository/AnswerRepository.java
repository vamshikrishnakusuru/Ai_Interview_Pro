package com.aiinterview.repository;

import com.aiinterview.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    java.util.List<Answer> findByInterview(com.aiinterview.entity.Interview interview);
}
