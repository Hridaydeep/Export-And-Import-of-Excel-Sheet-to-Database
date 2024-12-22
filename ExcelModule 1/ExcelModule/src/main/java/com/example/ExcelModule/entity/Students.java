package com.example.ExcelModule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="students", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"roll_no","class"})
})
public class Students {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stdId;

    @NotNull
    @Column(nullable = false)
    private String stdName;

    @Column(nullable = false)
    private Long rollNo;

    @Column(nullable = false)
    private Integer stdClass;

    @NotNull
    @Column(nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

}
