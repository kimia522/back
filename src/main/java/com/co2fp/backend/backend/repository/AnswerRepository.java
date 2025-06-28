package com.co2fp.backend.backend.repository;
import com.co2fp.backend.backend.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
    List<AnswerEntity> findByUser_UserId(Long userId);
    List<AnswerEntity> findByUser_UserIdAndDate(Long userId, LocalDate date);
    List<AnswerEntity> findByUser_UserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}