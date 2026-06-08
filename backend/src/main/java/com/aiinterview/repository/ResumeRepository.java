package com.aiinterview.repository;

import com.aiinterview.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiinterview.entity.User;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findFirstByUserOrderByUploadedAtDesc(User user);
}
