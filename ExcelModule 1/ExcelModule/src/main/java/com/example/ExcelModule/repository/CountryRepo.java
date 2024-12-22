package com.example.ExcelModule.repository;

import com.example.ExcelModule.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepo extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);

//    @Query("SELECT c.name FROM Country c WHERE c.id = :id")
//    String findNameById(@Param("id") Long id);

}
