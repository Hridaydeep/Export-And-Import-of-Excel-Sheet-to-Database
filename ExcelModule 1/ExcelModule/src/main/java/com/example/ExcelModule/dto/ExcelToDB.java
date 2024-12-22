package com.example.ExcelModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelToDB {

    private String stdName;

    private Long rollNo;

    private Integer stdClass;

    private String address;

    private String country;

    private String state;

    private String district;

    private String block;

    private String area;
}
