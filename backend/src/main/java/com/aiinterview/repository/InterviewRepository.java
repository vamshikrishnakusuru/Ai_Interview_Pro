package com.aiinterview.repository;

import com.aiinterview.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByUserId(Long userId);
    List<Interview> findByUser(com.aiinterview.entity.User user);
    List<Interview> findByUserOrderByCreatedAtDesc(com.aiinterview.entity.User user);
    List<Interview> findByUserOrderByCreatedAtAsc(com.aiinterview.entity.User user);
}
