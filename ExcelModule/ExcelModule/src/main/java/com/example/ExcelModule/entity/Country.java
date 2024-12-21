package com.example.ExcelModule.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="country",  uniqueConstraints =@UniqueConstraint(columnNames = "name"))
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
}
