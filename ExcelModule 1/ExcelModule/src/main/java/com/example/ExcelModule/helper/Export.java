package com.example.ExcelModule.helper;

import com.example.ExcelModule.dto.ExcelToDB;
import com.example.ExcelModule.utility.ExcelFile;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.example.ExcelModule.utility.ExcelFile.HEADERS;

public class Export {

    /**
     * This method takes a list of {@link ExcelToDB} objects and converts it into
     * an Excel file as a byte array.
     *
     * @param studentInfos the list of {@link ExcelToDB} objects to be converted
     * @return the byte array of the Excel file
     */
    public static ByteArrayInputStream dataToExcel(List<ExcelToDB> studentInfos){
        // create workbook
        Workbook workBook= new XSSFWorkbook();
        ByteArrayOutputStream  out= new ByteArrayOutputStream();

        try{
            // create sheet
            Sheet sheet = workBook.createSheet(ExcelFile.SHEET_NAME);

            //create rows
            Row row = sheet.createRow(0);

            //setting the header
            for(int i = 0; i< ExcelFile.HEADERS.length; i++){
                Cell cell= row.createCell(i);
                cell.setCellValue(ExcelFile.HEADERS[i]);
            }

            //values adding in the row
            int rowIndex=1;
            for(ExcelToDB c: studentInfos){
                Row dataRow=sheet.createRow(rowIndex++);

                dataRow.createCell(0).setCellValue(c.getStdName());
                dataRow.createCell(1).setCellValue(c.getRollNo());
                dataRow.createCell(2).setCellValue(c.getStdClass());
                dataRow.createCell(3).setCellValue(c.getAddress());
                dataRow.createCell(4).setCellValue(c.getArea());
                dataRow.createCell(5).setCellValue(c.getBlock());
                dataRow.createCell(6).setCellValue(c.getDistrict());
                dataRow.createCell(7).setCellValue(c.getState());
                dataRow.createCell(8).setCellValue(c.getCountry());
                //rowIndex++;
            }
            workBook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
            workBook.close();
            out.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }
}
