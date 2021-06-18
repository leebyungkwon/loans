package com.loanscrefia.util.excel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;

public class ExcelCellRef {
	/**
     * Cell?�� ?��?��?��?�� Column Name?�� �??��?��?��(A,B,C..)
     * 만약 Cell?�� Null?��?���? int cellIndex?�� 값으�?
     * Column Name?�� �??��?��?��.
     * @param cell
     * @param cellIndex
     * @return
     */
    public static String getName(Cell cell, int cellIndex) {
        int cellNum = 0;
        if(cell != null) {
            cellNum = cell.getColumnIndex();
        }
        else {
            cellNum = cellIndex;
        }
        
        return CellReference.convertNumToColString(cellNum);
    }
    
    public static String getValue(Cell cell) {
        String value = "";
        
        if(cell == null) {
            value = "";
        }
        else {
            if( cell.getCellType() == cell.CELL_TYPE_FORMULA ) {
                value = cell.getCellFormula();
            }
            else if( cell.getCellType() == cell.CELL_TYPE_NUMERIC ) {
            	if(DateUtil.isCellDateFormatted(cell)) {
    				Date date 	= cell.getDateCellValue();
    				value 		= new SimpleDateFormat("yyyy-MM-dd").format(date);
    			}else {
    				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                	value = cell.getStringCellValue();
    			}
            }
            else if( cell.getCellType() == Cell.CELL_TYPE_STRING ) {
                value = cell.getStringCellValue();
            }
            else if( cell.getCellType() == Cell.CELL_TYPE_BOOLEAN ) {
                value = cell.getBooleanCellValue() + "";
            }
            else if( cell.getCellType() == Cell.CELL_TYPE_ERROR ) {
                value = cell.getErrorCellValue() + "";
            }
            else if( cell.getCellType() == Cell.CELL_TYPE_BLANK ) {
                value = "";
            }
            else {
                value = cell.getStringCellValue();
            }
        }
        
        return value.trim();
    }
}
