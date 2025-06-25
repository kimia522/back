package com.co2fp.backend.backend.repository;
import com.co2fp.backend.backend.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Integer> {
}