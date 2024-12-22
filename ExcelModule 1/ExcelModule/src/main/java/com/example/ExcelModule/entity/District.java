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
@Table(name="district", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    public District(String name, State state) {
        this.name = name;
        this.state = state;
    }
}
