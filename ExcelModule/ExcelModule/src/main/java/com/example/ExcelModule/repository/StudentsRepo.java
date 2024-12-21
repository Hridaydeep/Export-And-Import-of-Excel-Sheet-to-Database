package com.example.ExcelModule.repository;

import com.example.ExcelModule.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepo extends JpaRepository<Students, Long> {
    boolean existsByRollNoAndStdClass(Long rollNo, Integer stdClass);
}
