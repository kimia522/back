package com.co2fp.backend.backend.repository;
import com.co2fp.backend.backend.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
}