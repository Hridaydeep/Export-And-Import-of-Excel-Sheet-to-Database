package com.example.ExcelModule.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "area", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;
}
