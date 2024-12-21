package com.example.ExcelModule.repository;

import com.example.ExcelModule.entity.Block;
import com.example.ExcelModule.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepo extends JpaRepository<Block, Long> {
   // @Query("SELECT b FROM Block b WHERE b.name = :name AND b.district.id = :districtId")
   Optional<Block> findByNameAndDistrict(String name, District district);

    //Optional<Block> findById(Long block);
//    @Query("SELECT b.name FROM Block b WHERE b.id = :blockId")
//    String findNameById(@Param("blockId") Long blockId);

}
