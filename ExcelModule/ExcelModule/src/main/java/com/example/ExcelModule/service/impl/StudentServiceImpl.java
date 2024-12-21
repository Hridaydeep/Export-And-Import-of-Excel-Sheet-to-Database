package com.example.ExcelModule.service.impl;

import com.example.ExcelModule.entity.*;
import com.example.ExcelModule.dto.ExcelToDB;
import com.example.ExcelModule.helper.ExcelHelper;
import com.example.ExcelModule.helper.Export;
import com.example.ExcelModule.repository.*;
import com.example.ExcelModule.service.StudentService;
import com.example.ExcelModule.utility.DetailReport;
import com.example.ExcelModule.utility.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentsRepo student;
    @Autowired
    private StateRepo state;
    @Autowired
    private DistrictRepo district;
    @Autowired
    private CountryRepo country;
    @Autowired
    private AreaRepo area;
    @Autowired
    private BlockRepo block;


    public Report save(MultipartFile file){
        long invalidRowCount=0;
        List<Students> validStudents=new ArrayList<>();
        List<DetailReport> detailReportList=new ArrayList<>();
        try {
            List<ExcelToDB> list = ExcelHelper.ImportExceltoDB(file.getInputStream());

            for(ExcelToDB excelRow: list){
                //1. checking Country
                Optional<Country> countryOpt=country.findByName(excelRow.getCountry());
                if(countryOpt.isEmpty()){
                    invalidRowCount++;
                    detailReportList.add(new DetailReport(excelRow, "Error in Country"));
                    //System.out.println("Country");;
                    continue;
                }

                Country country1=countryOpt.get();

                //checking state
                Optional<State> stateOpt=state.findByNameAndCountry(excelRow.getState(), country1);
                if (stateOpt.isEmpty()) {
                    //System.out.println("State");;
                    detailReportList.add(new DetailReport(excelRow, "Error in State"));
                    invalidRowCount++;
                    continue;
                }
                State state1 = stateOpt.get();


                // 3. Check if the district exists in the DB
                Optional<District> districtOpt = district.findByNameAndState(excelRow.getDistrict(), state1);
                if (districtOpt.isEmpty()) {
                    //System.out.println("District");
                    detailReportList.add(new DetailReport(excelRow, "Error in District"));
                    invalidRowCount++;
                    continue;
                }
                District district1 = districtOpt.get();


                // 4. Check if the block exists in the DB
                Optional<Block> blockOpt = block.findByNameAndDistrict(excelRow.getBlock(), district1);
                if (blockOpt.isEmpty()) {
                    //System.out.println("Block");;
                    detailReportList.add(new DetailReport(excelRow, "Error in Block"));
                    invalidRowCount++;
                    continue;
                }
                Block block1 = blockOpt.get();

                // 5. Check if the area exists in the DB
                Optional<Area> areaOpt = area.findByNameAndBlock(excelRow.getArea(), block1);
                if (areaOpt.isEmpty()) {
                    //System.out.println("Area");;
                    detailReportList.add(new DetailReport(excelRow, "Error in Area"));
                    invalidRowCount++;
                    continue;
                }
                Area area1 = areaOpt.get();

                // 6. Check if the combination of rollNo and class is unique
                boolean isRollNoUnique = !student.existsByRollNoAndStdClass(excelRow.getRollNo(), excelRow.getStdClass());
                if (!isRollNoUnique) {
                    detailReportList.add(new DetailReport(excelRow, "Error in Roll and Class"));
                    //System.out.println("Roll and class");;
                    invalidRowCount++;
                    continue;
                }

                // If all checks pass, create a new student entity and add it to the list
                Students student1 = new Students();
                student1.setStdName(excelRow.getStdName());
                student1.setRollNo(excelRow.getRollNo());
                student1.setStdClass(excelRow.getStdClass());
                student1.setAddress(excelRow.getAddress());
                student1.setCountry(country1);
                student1.setState(state1);
                student1.setDistrict(district1);
                student1.setBlock(block1);
                student1.setArea(area1);

                validStudents.add(student1);


            }
            student.saveAll(validStudents);
            Report report=new Report();
            report.setInValidEntry(invalidRowCount);
            report.setValidEntry(list.size()-invalidRowCount);
            report.setDetailReportList(detailReportList);
            return report;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Students> getAll() {
        return student.findAll();
    }





    //Export
    public ByteArrayInputStream getExcel() {
        List<Students> studentInfo = student.findAll();
        List<ExcelToDB> listOf= new ArrayList<>();
        for(Students std:studentInfo){
            ExcelToDB excelToDB= new ExcelToDB();
            excelToDB.setAddress(std.getAddress());
            excelToDB.setBlock(std.getBlock().getName());
            excelToDB.setArea(std.getArea().getName());
            excelToDB.setDistrict(std.getDistrict().getName());
            excelToDB.setState(std.getState().getName());
            excelToDB.setCountry(std.getCountry().getName());
            excelToDB.setStdName(std.getStdName());
            excelToDB.setRollNo(std.getRollNo());
            excelToDB.setStdClass(std.getStdClass());
            listOf.add(excelToDB);
        }

        return Export.dataToExcel(listOf);
    }
}
