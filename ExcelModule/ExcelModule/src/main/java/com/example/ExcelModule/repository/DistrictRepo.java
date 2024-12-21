package com.example.ExcelModule.repository;

import com.example.ExcelModule.entity.District;
import com.example.ExcelModule.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DistrictRepo extends JpaRepository<District, Long> {

    Optional<District> findByNameAndState(String name, State state);

    //Optional<District> findById(Long district);
//    @Query("SELECT d.name FROM District d WHERE d.id = :districtId")
//    String findNameById(@Param("districtId") Long districtId);

}
