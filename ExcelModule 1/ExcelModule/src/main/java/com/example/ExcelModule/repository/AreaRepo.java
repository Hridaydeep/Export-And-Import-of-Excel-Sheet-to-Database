package com.example.ExcelModule.repository;

import com.example.ExcelModule.entity.Area;
import com.example.ExcelModule.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepo extends JpaRepository<Area, Long> {
   // @Query("SELECT a FROM Area a WHERE a.name = :name AND a.block.id = :blockId")
   Optional<Area> findByNameAndBlock(String name, Block block);

    //Optional<Area> findById(Long areaID);

//    @Query("SELECT a.name FROM Area a WHERE a.id = :areaId")
//    String findNameById(@Param("areaId") Long areaId);

}
