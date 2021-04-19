package com.loanscrefia.util.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileType {
	/**
     * 
     * ?��???��?��?�� ?��?��?�� Workbook 객체?�� 리턴?��?��.
     * XLS?? XLSX ?��?��?���? 비교?��?��.
     * 
     * @param filePath
     * @return
     * 
     */
    public static Workbook getWorkbook(String filePath) {
        
        /*
         * FileInputStream?? ?��?��?�� 경로?�� ?��?�� ?��?��?��
         * ?��?��?�� Byte�? �??��?��?��.
         * 
         * ?��?��?�� 존재?���? ?��?��?��면�?
         * RuntimeException?�� 발생?��?��.
         */
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        Workbook wb = null;
        
        /*
         * ?��?��?�� ?��?��?���? 체크?��?�� .XLS ?���? HSSFWorkbook?��
         * .XLSX?���? XSSFWorkbook?�� 각각 초기?�� ?��?��.
         */
        if(filePath.toUpperCase().endsWith(".XLS")) {
            try {
                wb = new HSSFWorkbook(fis);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        else if(filePath.toUpperCase().endsWith(".XLSX")) {
            try {
                wb = new XSSFWorkbook(fis);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        
        return wb;
        
    }


}

