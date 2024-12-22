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
@Table(name="country",  uniqueConstraints =@UniqueConstraint(columnNames = "name"))
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;


    public Country(String country) {
        this.name = country;
    }
}
