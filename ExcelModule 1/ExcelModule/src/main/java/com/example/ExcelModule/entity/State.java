package com.example.ExcelModule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "state", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "name", nullable = false,unique = true)
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public State (String name, Country country) {
        this.name = name;
        this.country = country;
    }

}
