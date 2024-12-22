package com.example.ExcelModule.repository;

import com.example.ExcelModule.entity.Country;
import com.example.ExcelModule.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateRepo extends JpaRepository<State, Long> {
    Optional<State> findByNameAndCountry(String name, Country country);

    //Optional<State> findById(Long id);
//    @Query("SELECT s.name FROM State s WHERE s.id = :stateId")
//    String findNameById(@Param("stateId") Long stateId);
}
