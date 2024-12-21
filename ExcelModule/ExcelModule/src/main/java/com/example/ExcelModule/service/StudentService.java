package com.example.ExcelModule.service;

import com.example.ExcelModule.entity.Students;
import com.example.ExcelModule.utility.Report;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface StudentService {
    public Report save(MultipartFile file);
    public List<Students> getAll();

    public ByteArrayInputStream getExcel();


}
