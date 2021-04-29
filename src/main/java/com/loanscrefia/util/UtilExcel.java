package com.loanscrefia.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.loanscrefia.util.excel.ExcelCellRef;
import com.loanscrefia.util.excel.ExcelColumn;
import com.loanscrefia.util.excel.ExcelFileType;

public class UtilExcel<T> {
	
	//참고 : https://eugene-kim.tistory.com/46
	public List<Map<String, Object>> upload(String path) {

		List<Map<String, Object>> result 	= new ArrayList<Map<String, Object>>(); 
		Workbook wb 						= ExcelFileType.getWorkbook("C:\\Users\\sohui\\Desktop\\excelTest_corp.xlsx"); //path
		Sheet sheet 						= wb.getSheetAt(0);
		int numOfCells 						= sheet.getRow(0).getPhysicalNumberOfCells();
		
		for(int i = 1; i < sheet.getLastRowNum() + 1; i++) {
		    Row row 				= null;
		    Cell cell 				= null;
		    String cellName 		= "";
		    Map<String, Object> map = null;
		    
	        row = sheet.getRow(i);
	        
	        if(row != null) {
	            map = new HashMap<String, Object>();
	            for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
	                cell 		= row.getCell(cellIndex);
	                cellName 	= ExcelCellRef.getName(cell, cellIndex);
	                
	                System.out.println(cellIndex + " :: " + cellName + " :: " + cell.getCellType() + " :: " + ExcelCellRef.getValue(cell));
	                /*
	                if( !excelReadOption.getOutputColumns().contains(cellName) ) {
	                    continue;
	                }
	                */
	                map.put(cellName, ExcelCellRef.getValue(cell));
	            }
	            result.add(map);
		    }
		}
		
		return result;
	}
	

	public void downLoad(List<T> data, Class<T> type,OutputStream stream) throws IOException, IllegalArgumentException, IllegalAccessException {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet();
		int rowIndex = 0;
		Row row = sheet.createRow(rowIndex++);
		
		for (Field field : type.getDeclaredFields()) {
			field.setAccessible(true);
			if (null != field.getAnnotation(ExcelColumn.class)) {
				Cell cell = row.createCell(field.getAnnotation(ExcelColumn.class).order());
				cell.setCellValue(field.getAnnotation(ExcelColumn.class).headerName().toString());
			}
		}

		for(T target: data) {
			row = sheet.createRow(rowIndex++);
			for(Field field : target.getClass().getDeclaredFields()){
				for (Field f : type.getDeclaredFields()) {
					if (null != f.getAnnotation(ExcelColumn.class)) {
						if(field.getName().equals(f.getName())) {

							field.setAccessible(true);
							f.setAccessible(true);
							Object value = f.get(target); 
							Cell cell = row.createCell(f.getAnnotation(ExcelColumn.class).order());
							cell.setCellValue(StringEscapeUtils.unescapeHtml3(value.toString()));
						}
					}
				}
			}
		}
		
	    
		wb.write(stream);
		wb.close();
		wb.dispose();
		stream.close();
	}

}
