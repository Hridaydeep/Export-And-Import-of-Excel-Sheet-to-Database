package com.example.ExcelModule.controller;

import com.example.ExcelModule.entity.Students;
import com.example.ExcelModule.helper.ExcelHelper;
import com.example.ExcelModule.service.StudentService;
import com.example.ExcelModule.utility.Report;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {
    /**
     * using Interface class for loose coupling
     *
     */
    @Autowired
    StudentService studentService;
    /**
     * This endpoint is used to upload the Excel file containing the student data.
     * The Excel file is validated by checking if the file is an Excel file or not.
     * If the file is valid, the student data is saved to the database and a report
     * is generated containing the number of valid and invalid entries and a whether .
     * the file upload done correctly or not
     *
     * @param file The Excel file to be uploaded.
     * @return A ResponseEntity containing the report of the uploaded file.
     * in the report conatin number of valid and invalid entries
     * also it contain the list of invalid entries with the error listed in it
     */

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file){
        if(ExcelHelper.checkExcelFormat(file)){
            Report report=this.studentService.save(file);
            return ResponseEntity.ok(
                    Map.of(
                            "message", "File uploaded successfully",
                            "validEntry", "Number of valid Entry: " + report.getValidEntry(),
                            "invalidEntry", "Number of invalid Entry: " + report.getInValidEntry(),
                            "failedEntries", report.getDetailReportList() // List of invalid entries
                    )
            );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Upload Excel File only");
    }

    /**
     * In this API we Will be returning
     * all student Information present
     * in the Students Table
     */
    @GetMapping("/get")
    public ResponseEntity<List<Students>> get(){
        return ResponseEntity.ok(this.studentService.getAll());
    }


    /**
     * This REST API is used to download the Student data present in the
     * database in the form of Excel file.
     *
     * @return The Excel file containing the Student data.
     */
    @GetMapping("/export")
    public ResponseEntity<Resource> getExcel(){
        String fileName="StudentInfo.xlsx";
        ByteArrayInputStream in = studentService.getExcel();
        InputStreamResource file = new InputStreamResource(in);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body((Resource) file);
    }
}
