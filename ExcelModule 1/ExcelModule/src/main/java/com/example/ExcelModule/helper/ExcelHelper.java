package com.example.ExcelModule.helper;

import com.example.ExcelModule.dto.ExcelToDB;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    /**
     * Check if the given multipart file is an Excel file (.xlsx or .xls).
     * @param file the multipart file to check
     * @return true if the file is an Excel file, false otherwise
     */
    public static boolean checkExcelFormat(MultipartFile file){

        String contentType = file.getContentType();
        if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || contentType.equals("application/vnd.ms-excel")){
            return true;
        }
        return false;
    }


    /**
     * Imports an Excel file into a list of {@link ExcelToDB} objects.
     * This method reads an Excel file (in .xlsx format) and converts it into a list of {@link ExcelToDB} objects.
     * The Excel file should contain a sheet named "Sheet1" with the following columns:
     * <pre>
     * | Column index | Column name      | Data type |
     * |--------------|------------------|-----------|
     * | 0            | Student name     | String    |
     * | 1            | Roll no          | Long      |
     * | 2            | Class            | Integer   |
     * | 3            | Address          | String    |
     * | 4            | Area             | String    |
     * | 5            | Block            | String    |
     * | 6            | District         | String    |
     * | 7            | State            | String    |
     * | 8            | Country          | String    |
     * </pre>
     * @param is the input stream of the Excel file
     * @return the list of {@link ExcelToDB} objects
     */
    public static List<ExcelToDB> ImportExceltoDB(InputStream is){

        List<ExcelToDB> list=new ArrayList<>();
        try{
            XSSFWorkbook workbook=new XSSFWorkbook(is);
            XSSFSheet sheet=workbook.getSheet("Sheet1");
            int rowNumber=0;
            Iterator<Row> iterator=sheet.iterator();
            while(iterator.hasNext()){
                Row row=iterator.next();
                if(rowNumber==0){
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cells=row.iterator();
                int cid=0;
                ExcelToDB excelToDB=new ExcelToDB();
                while(cells.hasNext()){
                    Cell cell=cells.next();
                    switch(cid){
                        case 0:
                            excelToDB.setStdName(cell.getStringCellValue());
                            break;
                        case 1:
                            excelToDB.setRollNo((long)cell.getNumericCellValue());
                            break;
                        case 2:
                            excelToDB.setStdClass((int)cell.getNumericCellValue());
                            break;
                        case 3:
                            excelToDB.setAddress(cell.getStringCellValue());
                            break;
                        case 4:
                            excelToDB.setArea(cell.getStringCellValue());
                            break;
                        case 5:
                            excelToDB.setBlock(cell.getStringCellValue());
                            break;
                        case 6:
                            excelToDB.setDistrict(cell.getStringCellValue());
                            break;
                        case 7:
                            excelToDB.setState(cell.getStringCellValue());
                            break;
                        case 8:
                            excelToDB.setCountry(cell.getStringCellValue());
                            break;
                        default:
                            break;

                    }
                    cid++;
                }
                list.add(excelToDB);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
