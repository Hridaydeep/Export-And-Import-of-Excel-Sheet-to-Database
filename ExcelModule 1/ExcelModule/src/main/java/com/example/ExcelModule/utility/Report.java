package com.example.ExcelModule.utility;

import lombok.Data;

import java.util.List;

@Data
public class Report {
    Long validEntry;
    Long inValidEntry;

    List<DetailReport> detailReportList;
}
