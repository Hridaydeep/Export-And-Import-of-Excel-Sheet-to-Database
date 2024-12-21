package com.example.ExcelModule.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="district", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;
}
