package com.example.ExcelModule.utility;

import com.example.ExcelModule.dto.ExcelToDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReport {
    ExcelToDB excelToDB;
    String reason;
}
