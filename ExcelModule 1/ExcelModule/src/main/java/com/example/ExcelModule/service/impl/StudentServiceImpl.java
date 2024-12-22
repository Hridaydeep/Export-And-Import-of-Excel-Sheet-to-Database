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

    public Report save(MultipartFile file) {
        long invalidRowCount = 0;
        List<Students> validStudents = new ArrayList<>();
        List<DetailReport> detailReportList = new ArrayList<>();

        try {
            List<ExcelToDB> list = ExcelHelper.ImportExceltoDB(file.getInputStream());

            for (ExcelToDB excelRow : list) {
                try {
                    // 1. Fetch or save Country
                    Country country1 = null;
                    try {
                        String countryName = excelRow.getCountry();
                        if (countryName == null || countryName.trim().isEmpty()) {
                            detailReportList.add(new DetailReport(excelRow, "Invalid country name"));
                            invalidRowCount++;
                            continue;
                        }
                        country1 = country.findByName(excelRow.getCountry())
                                .orElseGet(() -> country.save(new Country(excelRow.getCountry())));
                    } catch (Exception ex) {
                        detailReportList.add(new DetailReport(excelRow, "Error processing country: " + ex.getMessage()));
                        invalidRowCount++;
                        continue;
                    }

                    // 2. Fetch or save State
                    State state1 = null;
                    try {
                        Country finalCountry = country1;
                        state1 = state.findByNameAndCountry(excelRow.getState(), country1)
                                .orElseGet(() -> state.save(new State(excelRow.getState(), finalCountry)));
                    } catch (Exception ex) {
                        detailReportList.add(new DetailReport(excelRow, "Error processing state: " + ex.getMessage()));
                        invalidRowCount++;
                        continue;
                    }

                    // 3. Fetch or save District
                    District district1 = null;
                    try {
                        State finalState = state1;
                        district1 = district.findByNameAndState(excelRow.getDistrict(), state1)
                                .orElseGet(() -> district.save(new District(excelRow.getDistrict(), finalState)));
                    } catch (Exception ex) {
                        detailReportList.add(new DetailReport(excelRow, "Error processing district: " + ex.getMessage()));
                        invalidRowCount++;
                        continue;
                    }

                    // 4. Fetch or save Block
                    Block block1 = null;

                    try {
                        District finalDistrict = district1;
                        block1 = block.findByNameAndDistrict(excelRow.getBlock(), district1)
                                .orElseGet(() -> block.save(new Block(excelRow.getBlock(), finalDistrict)));
                    } catch (Exception ex) {
                        detailReportList.add(new DetailReport(excelRow, "Error processing block: " + ex.getMessage()));
                        invalidRowCount++;
                        continue;
                    }

                    // 5. Fetch or save Area
                    Area area1 = null;
                    try {
                        Block finalBlock = block1;
                        area1 = area.findByNameAndBlock(excelRow.getArea(), block1)
                                .orElseGet(() -> area.save(new Area(excelRow.getArea(), finalBlock)));
                    } catch (Exception ex) {
                        detailReportList.add(new DetailReport(excelRow, "Error processing area: " + ex.getMessage()));
                        invalidRowCount++;
                        continue;
                    }

                    // 6. Check for duplicate Roll No and Class
                    try {
                        if (student.existsByRollNoAndStdClass(excelRow.getRollNo(), excelRow.getStdClass())) {
                            detailReportList.add(new DetailReport(excelRow, "Duplicate Roll No and Class combination"));
                            invalidRowCount++;
                            continue;
                        }
                    } catch (Exception ex) {
                        detailReportList.add(new DetailReport(excelRow, "Error checking duplicate Roll No and Class: " + ex.getMessage()));
                        invalidRowCount++;
                        continue;
                    }

                    // 7. Create and add a valid student
                    try {
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
                    } catch (Exception ex) {
                        detailReportList.add(new DetailReport(excelRow, "Error processing student details: " + ex.getMessage()));
                        invalidRowCount++;
                    }

                } catch (Exception ex) {
                    // Catch any other unexpected exceptions for the row
                    detailReportList.add(new DetailReport(excelRow, "Unexpected error: " + ex.getMessage()));
                    invalidRowCount++;
                }
            }

            // Save all valid students in batch
            try {
                student.saveAll(validStudents);
            } catch (Exception ex) {
                detailReportList.add(new DetailReport(null, "Error saving valid students: " + ex.getMessage()));
            }

            // Create and return the report
            Report report = new Report();
            report.setInValidEntry(invalidRowCount);
            report.setValidEntry(list.size() - invalidRowCount);
            report.setDetailReportList(detailReportList);
            return report;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

  /*  public Report save(MultipartFile file){
        long invalidRowCount=0;
        List<Students> validStudents=new ArrayList<>();
        List<DetailReport> detailReportList=new ArrayList<>();
        try {
            List<ExcelToDB> list = ExcelHelper.ImportExceltoDB(file.getInputStream());

            for(ExcelToDB excelRow: list){
                /*
                //1. checking Country
                Optional<Country> countryOpt=country.findByName(excelRow.getCountry());
                if(countryOpt.isEmpty()){
                    country.save(excelRow.getCountry());
                    //invalidRowCount++;
                    //detailReportList.add(new DetailReport(excelRow, "Error in Country"));
                    //System.out.println("Country");;
                    //continue;
                }
                countryOpt=country.findByName(excelRow.getCountry());
                Country country1=countryOpt.get();

                //checking state
                Optional<State> stateOpt=state.findByNameAndCountry(excelRow.getState(), country1);
                if (stateOpt.isEmpty()) {
                    state.save(excelRow.getState(),country1);
                    //System.out.println("State");;
                    //detailReportList.add(new DetailReport(excelRow, "Error in State"));
                    //invalidRowCount++;
                    //continue;
                }
                stateOpt=state.findByNameAndCountry(excelRow.getState(), country1);
                State state1 = stateOpt.get();


                // 3. Check if the district exists in the DB
                Optional<District> districtOpt = district.findByNameAndState(excelRow.getDistrict(), state1);
                if (districtOpt.isEmpty()) {
                    district.save(excelRow.getDistrict(), state1);
                    //System.out.println("District");
                    //detailReportList.add(new DetailReport(excelRow, "Error in District"));
                    //invalidRowCount++;
                    //continue;
                }
                districtOpt = district.findByNameAndState(excelRow.getDistrict(), state1);
                District district1 = districtOpt.get();


                // 4. Check if the block exists in the DB
                Optional<Block> blockOpt = block.findByNameAndDistrict(excelRow.getBlock(), district1);
                if (blockOpt.isEmpty()) {
                    block.save(excelRow.getBlock(), district1);
                    //System.out.println("Block");;
                    //detailReportList.add(new DetailReport(excelRow, "Error in Block"));
                    //invalidRowCount++;
                    //continue;
                }
                blockOpt = block.findByNameAndDistrict(excelRow.getBlock(), district1);
                Block block1 = blockOpt.get();

                // 5. Check if the area exists in the DB
                Optional<Area> areaOpt = area.findByNameAndBlock(excelRow.getArea(), block1);
                if (areaOpt.isEmpty()) {
                    area.save(excelRow.getArea(), block1);
                    //System.out.println("Area");;
                    //detailReportList.add(new DetailReport(excelRow, "Error in Area"));
                    //invalidRowCount++;
                    //continue;
                }
                areaOpt = area.findByNameAndBlock(excelRow.getArea(), block1);
                Area area1 = areaOpt.get();

                // 6. Check if the combination of rollNo and class is unique
                boolean isRollNoUnique = !student.existsByRollNoAndStdClass(excelRow.getRollNo(), excelRow.getStdClass());
                if (!isRollNoUnique) {
                    detailReportList.add(new DetailReport(excelRow, "Error in Roll and Class"));
                    //System.out.println("Roll and class");;
                    //invalidRowCount++;
                    //continue;
                }
                */

              /*  // 1. Fetch or save Country
                Country country1 = country.findByName(excelRow.getCountry())
                        .orElseGet(() -> country.save(new Country(excelRow.getCountry())));

                // 2. Fetch or save State
                State state1 = state.findByNameAndCountry(excelRow.getState(), country1)
                        .orElseGet(() -> state.save(new State(excelRow.getState(), country1)));

                // 3. Fetch or save District
                District district1 = district.findByNameAndState(excelRow.getDistrict(), state1)
                        .orElseGet(() -> district.save(new District(excelRow.getDistrict(), state1)));

                // 4. Fetch or save Block
                Block block1 = block.findByNameAndDistrict(excelRow.getBlock(), district1)
                        .orElseGet(() -> block.save(new Block(excelRow.getBlock(), district1)));

                // 5. Fetch or save Area
                Area area1 = area.findByNameAndBlock(excelRow.getArea(), block1)
                        .orElseGet(() -> area.save(new Area(excelRow.getArea(), block1)));

                // 6. Check if the roll number and class combination is unique
                if (student.existsByRollNoAndStdClass(excelRow.getRollNo(), excelRow.getStdClass())) {
                    detailReportList.add(new DetailReport(excelRow, "Duplicate Roll No and Class combination"));
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
    }*/


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
