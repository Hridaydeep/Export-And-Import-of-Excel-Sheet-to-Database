package com.example.ExcelModule.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "state", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false,unique = true)
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

}
