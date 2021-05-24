package com.loanscrefia.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.admin.corp.repository.CorpRepository;
import com.loanscrefia.member.edu.domain.EduDomain;
import com.loanscrefia.member.edu.repository.EduRepository;
import com.loanscrefia.util.excel.ExcelCellRef;
import com.loanscrefia.util.excel.ExcelColumn;
import com.loanscrefia.util.excel.ExcelFileType;

@Component
public class UtilExcel<T> {
	
	public UtilExcel() {
		
	}
	
	@Autowired private CorpRepository corpRepo;
	@Autowired private EduRepository eduRepo; 
	
	//참고 : https://eugene-kim.tistory.com/46
	public List<Map<String, Object>> upload(String path, Class<?> dClass) {

		Field[] fields = dClass.getDeclaredFields();

		List<String> headerName = new ArrayList<String>();
		List<String> vCell 		= new ArrayList<String>();
		List<String> vEnum 		= new ArrayList<String>();
		List<Integer> vLenMin 	= new ArrayList<Integer>();
		List<Integer> vLenMax 	= new ArrayList<Integer>();
		List<String> chkDb 		= new ArrayList<String>();
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(ExcelColumn.class)) {
				ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);
				
				headerName.add(columnAnnotation.headerName());
				vCell.add(columnAnnotation.vCell());
				vEnum.add(columnAnnotation.vEnum());
				vLenMax.add(columnAnnotation.vLenMax());
				vLenMin.add(columnAnnotation.vLenMin());
				chkDb.add(columnAnnotation.chkDb());
			}
		}
		
		List<Map<String, Object>> result 	= new ArrayList<Map<String, Object>>(); 
		Workbook wb 						= ExcelFileType.getWorkbook(path);
		Sheet sheet 						= wb.getSheetAt(0);
		int numOfCells 						= sheet.getRow(0).getPhysicalNumberOfCells();
		
		Map<String, Object> errorMsgMap		= new HashMap<String, Object>();
		String errorMsg						= "";
		
		for(int i = 1; i < sheet.getLastRowNum() + 1; i++) {
		    Row row 				= null;
		    Cell cell 				= null;
		    String cellName 		= "";
		    Map<String, Object> map = null;
		    
		    CorpDomain corpChkParam = new CorpDomain();
		    EduDomain eduChkParam 	= new EduDomain();
		    
	        row = sheet.getRow(i);
	        
	        if(row != null) {
	            map = new HashMap<String, Object>();
	            for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
	                cell 		= row.getCell(cellIndex);
	                cellName 	= ExcelCellRef.getName(cell, cellIndex);
	                
	                //System.out.println(cellIndex + " :: " + cellName + " :: " + cell.getCellType() + " :: " + ExcelCellRef.getValue(cell));
	                
	                boolean valChkResult = false;
	                
	                for(int j = 0;j < vCell.size();j++) {
	                	if(cellName.equals(vCell.get(j))) {
	                		if(ExcelCellRef.getValue(cell).length() < vLenMin.get(j)){
	                			errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " :: 최저 길이는 " + vLenMin.get(j) + " 입니다.\n";
	                		}
	                		if(ExcelCellRef.getValue(cell).length() > vLenMax.get(j)){
	                			errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " :: 최대 길이는 " + vLenMax.get(j) + " 입니다.\n";
	                		}
	                		if(!vEnum.get(j).isEmpty()){
	                			String val[] = vEnum.get(j).split(",");
	        	                for(int k = 0;k < val.length;k++) {
	        	                	//System.out.println(val[k]+ " , " + ExcelCellRef.getValue(cell) + " = " + val[k].equals(ExcelCellRef.getValue(cell)));
	        	                	if(val[k].equals(ExcelCellRef.getValue(cell))) valChkResult = true;
	        	                }
	        	                if(!valChkResult) {
	        	                	errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " :: 필수 값은 [" + vEnum.get(j) + "] 입니다.\n";
	        	                }
	                		}
	                		if(!chkDb.get(j).isEmpty()){
	                			if(chkDb.get(j).equals("corp1")) {
	                				//법인 정보 유효 체크(법인사용인)
	                				corpChkParam.setPlMerchantName(ExcelCellRef.getValue(cell));
	                			}else if(chkDb.get(j).equals("corp2")) {
	                				//법인 정보 유효 체크(법인사용인)
	                				corpChkParam.setPlMerchantNo(ExcelCellRef.getValue(cell));
	                				
	                				if((corpChkParam.getPlMerchantName() != null && !corpChkParam.getPlMerchantName().equals("")) || (corpChkParam.getPlMerchantNo() != null && !corpChkParam.getPlMerchantNo().equals(""))) {
	                					int chkResult = selectCorpInfoCnt(corpChkParam);
		                				
		                				if(chkResult == 0) {
		                					errorMsg += row.getRowNum() + 1 + "번째 줄의 법인정보가 유효하지 않습니다.\n";
		                				}
	                				}
	                			}else if(chkDb.get(j).equals("edu1")) {
	                				//교육이수번호,인증서번호 유효 체크
	                				eduChkParam.setCareerTyp(ExcelCellRef.getValue(cell));
	                			}else if(chkDb.get(j).equals("edu2")) {
	                				//교육이수번호,인증서번호 유효 체크
	                				eduChkParam.setPlMName(ExcelCellRef.getValue(cell));
	                			}else if(chkDb.get(j).equals("edu3")) {
	                				//교육이수번호,인증서번호 유효 체크
	                				eduChkParam.setPlMZId(ExcelCellRef.getValue(cell));
	                			}else if(chkDb.get(j).equals("edu4")) {
	                				//교육이수번호,인증서번호 유효 체크
	                				eduChkParam.setPlProduct(ExcelCellRef.getValue(cell));
	                			}else if(chkDb.get(j).equals("edu5")) {
	                				//교육이수번호,인증서번호 유효 체크
	                				eduChkParam.setPlEduNo(ExcelCellRef.getValue(cell));

	                				int chkResult 	= plEduNoCheck(eduChkParam);
		                			
	                				if(chkResult == 0) {
	                					errorMsg += row.getRowNum() + 1 + "번째 줄의 교육이수번호/인증서번호가 유효하지 않습니다.\n";
	                				}
	                			}
	                		}
	                	}
	                }
	                map.put(cellName, ExcelCellRef.getValue(cell));
	            }
	            result.add(map);
		    }
		}
		//System.out.println("errorMsg :: " + errorMsg);
        
        if(errorMsg != null && !errorMsg.equals("")) {
        	errorMsgMap.put("errorMsg", errorMsg);
        	result.clear();
        	result.add(errorMsgMap);
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
							
							if(value == null || value.equals("")) {
								cell.setCellValue("");
							}else {
								cell.setCellValue(StringEscapeUtils.unescapeHtml3(value.toString()));
							}
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
	
	//법인 정보 유효 체크(법인사용인)
	@Transactional(readOnly=true)
	private int selectCorpInfoCnt(CorpDomain corpDomain) {
		return corpRepo.selectCorpInfoCnt(corpDomain);
	}
	
	//교육이수번호,인증서번호 유효 체크
	@Transactional(readOnly=true)
	private int plEduNoCheck(EduDomain eduDomain) {
		return eduRepo.plEduNoCheck(eduDomain);
	}
	
	//날짜 형식 및 값 체크
	public boolean dateCheck(String date, String format) {
		SimpleDateFormat dateFormatParser = new SimpleDateFormat(format, Locale.KOREA);
		dateFormatParser.setLenient(false);
		try {
			dateFormatParser.parse(date);
			return true;
		}catch(Exception exception) {
			return false;
		}
	}
	


}
