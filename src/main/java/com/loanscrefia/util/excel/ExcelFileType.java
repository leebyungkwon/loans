package com.loanscrefia.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sinsiway.CryptoUtil;

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
    public static Workbook getWorkbook(String uPath, String filePath, String fileSaveNm, String fileExt) { //String filePath
        
        /*
         * FileInputStream?? ?��?��?�� 경로?�� ?��?�� ?��?��?��
         * ?��?��?�� Byte�? �??��?��?��.
         * 
         * ?��?��?�� 존재?���? ?��?��?��면�?
         * RuntimeException?�� 발생?��?��.
         */
        FileInputStream fis = null;
        try {
        	//암호화 해제
			String oFile 		= uPath + "/" + filePath + "/" + fileSaveNm + "." + fileExt;
			String chFile 	= uPath + "/" + filePath + "/" + fileSaveNm + "_dnc." + fileExt;
			CryptoUtil.decryptFile(oFile, chFile);
			//해제 끝
        	
			fis = new FileInputStream(chFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        Workbook wb = null;
        
        /*
         * ?��?��?�� ?��?��?���? 체크?��?�� .XLS ?���? HSSFWorkbook?��
         * .XLSX?���? XSSFWorkbook?�� 각각 초기?�� ?��?��.
         */
        if(fileExt.toUpperCase().endsWith("XLS")) {
            try {
                wb = new HSSFWorkbook(fis);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        else if(fileExt.toUpperCase().endsWith("XLSX")) {
            try {
                wb = new XSSFWorkbook(fis);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        
        return wb;
        
    }


}

