package com.loanscrefia.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.admin.corp.repository.CorpRepository;
import com.loanscrefia.admin.edu.domain.EduDomain;
import com.loanscrefia.admin.edu.service.EduService;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.UserRepository;
import com.loanscrefia.util.excel.ExcelCellRef;
import com.loanscrefia.util.excel.ExcelColumn;
import com.loanscrefia.util.excel.ExcelFileType;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Component
public class UtilExcel<T> {
	
	public UtilExcel() {
		
	}
	
	@Autowired private CorpRepository corpRepo;
	@Autowired private EduService eduService;
	@Autowired private UserRepository userRepo;
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	private String param1; //금융상품유형
	private String param2; //분류(개인,법인)
	private String param3; //임원탭 값
	private String param4;
	private String param5;
	
	public UtilExcel<T> setParam1(String param) {
		this.param1 = param;
		return this;
	}
	public UtilExcel<T> setParam2(String param) {
		this.param2 = param;
		return this;
	}
	public UtilExcel<T> setParam3(String param) {
		this.param3 = param;
		return this;
	}
	public UtilExcel<T> setParam4(String param) {
		this.param4 = param;
		return this;
	}
	public UtilExcel<T> setParam5(String param) {
		this.param5 = param;
		return this;
	}
	
	//참고 : https://eugene-kim.tistory.com/46
	public List<Map<String, Object>> upload(String uPath, String filePath, String fileSaveNm, String fileExt, Class<?> dClass) {

		Field[] fields = dClass.getDeclaredFields();

		List<String> headerName = new ArrayList<String>();
		List<String> vCell 		= new ArrayList<String>();
		List<String> vEnum 		= new ArrayList<String>();
		List<Integer> vLenMin 	= new ArrayList<Integer>();
		List<Integer> vLenMax 	= new ArrayList<Integer>();
		List<String> chkDb 		= new ArrayList<String>();
		List<String> vEncrypt 	= new ArrayList<String>();
		List<String> chkPrd 	= new ArrayList<String>();
		List<String> chkFormat 	= new ArrayList<String>();
		List<String> chkDate 	= new ArrayList<String>();
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(ExcelColumn.class)) {
				ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);
				
				headerName.add(columnAnnotation.headerName());
				vCell.add(columnAnnotation.vCell());
				vEnum.add(columnAnnotation.vEnum());
				vLenMax.add(columnAnnotation.vLenMax());
				vLenMin.add(columnAnnotation.vLenMin());
				chkDb.add(columnAnnotation.chkDb());
				vEncrypt.add(columnAnnotation.vEncrypt());
				chkPrd.add(columnAnnotation.chkPrd());
				chkFormat.add(columnAnnotation.chkFormat());
				chkDate.add(columnAnnotation.chkDate());
			}
		}
		
		List<Map<String, Object>> result 	= new ArrayList<Map<String, Object>>();
		Map<String, Object> errorMsgMap		= new HashMap<String, Object>();
		String errorMsg						= "";
		
		try {
			Workbook wb 						= ExcelFileType.getWorkbook(uPath, filePath, fileSaveNm, fileExt);
			Sheet sheet 						= wb.getSheetAt(0);
			int numOfCells 						= sheet.getRow(0).getPhysicalNumberOfCells();
			int physicalNumberOfRows			= sheet.getPhysicalNumberOfRows();
			
			if(physicalNumberOfRows <= 1) {
				errorMsg = "엑셀 양식에 입력된 데이터가 없습니다.";
				errorMsgMap.put("errorMsg", errorMsg);
	        	result.clear();
	        	result.add(errorMsgMap);
	        	return result;
			}else if(physicalNumberOfRows > 31) {
				errorMsg = "최대 30명까지 등록할 수 있습니다.";
				errorMsgMap.put("errorMsg", errorMsg);
	        	result.clear();
	        	result.add(errorMsgMap);
	        	return result;
			}
			
			//최초 등록 시 대출 상품 중복 체크용 리스트
			List<Map<String, Object>> plProductArr = new ArrayList<Map<String, Object>>(); //String plProductArr[] 	= new String[physicalNumberOfRows-1];
			int arrPosition 		= 0;
			
			for(int i = 1; i < sheet.getLastRowNum() + 1; i++) {
			    Row row 				= null;
			    Cell cell 				= null;
			    String cellName 		= "";
			    String cellVal			= "";
			    Map<String, Object> map = null;
			    
			    CorpDomain corpChkParam = new CorpDomain();
			    EduDomain eduChkParam 	= new EduDomain();
			    UserDomain userChkParam = new UserDomain();
			    String[] careerDtArr	= new String[2];
			    
			    //row check
				int cellChkCnt 			= 0;
			    
		        row = sheet.getRow(i);
		        
		        if(row != null) {
		        	for(int t = 0;t < vCell.size();t++) {
	            		if(StringUtils.isEmpty(ExcelCellRef.getValue(row.getCell(t)).trim())) {
	            			cellChkCnt++;
	            		}
	            	}
		        	if(vCell.size() == cellChkCnt) {
		        		errorMsg = row.getRowNum() + 1 + "번째 줄의 데이터가 잘못되었습니다. 해당 row 우클릭 삭제 후 업로드해 주세요.<br>";
		        		break;
		        	}
		        	
		            map = new HashMap<String, Object>();
		            for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
		                cell 		= row.getCell(cellIndex);
		                cellName 	= ExcelCellRef.getName(cell, cellIndex);
		                cellVal 	= ExcelCellRef.getValue(cell).trim();
		                
		                //log.info(cellIndex + " :: " + cellName + " :: " + cell.getCellType() + " :: " + cellVal);
		                
		                boolean valChkResult = false;
		                
		                for(int j = 0;j < vCell.size();j++) {
		                	if(cellName.equals(vCell.get(j))) {
		                		if(cellVal.length() < vLenMin.get(j)){
		                			errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " :: 최저 길이는 " + vLenMin.get(j) + " 입니다.<br>";
		                		}
		                		if(cellVal.length() > vLenMax.get(j)){
		                			errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " :: 최대 길이는 " + vLenMax.get(j) + " 입니다.<br>";
		                		}
		                		if(!vEnum.get(j).isEmpty()){
		                			String val[] = vEnum.get(j).split(",");
		        	                for(int k = 0;k < val.length;k++) {
		        	                	//log.info(val[k]+ " , " + cellVal + " = " + val[k].equals(cellVal));
		        	                	if(val[k].equals(cellVal)) valChkResult = true;
		        	                }
		        	                if(!valChkResult) {
		        	                	errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " :: 필수 값은 [" + vEnum.get(j) + "] 입니다.<br>";
		        	                }
		                		}
		                		if(!chkDb.get(j).isEmpty()){
		                			if(chkDb.get(j).equals("corp")) {
		                				//법인 정보 유효 체크(법인사용인)
		                				if(StringUtils.isNotEmpty(cellVal)) { // && !cellVal.equals("도이치 법인번호") 추가해야해************************
		                					corpChkParam.setPlMerchantNo(cellVal);
		                					if(selectCorpInfoChk(corpChkParam) == 0) {
		                						errorMsg += row.getRowNum() + 1 + "번째 줄의 법인정보가 유효하지 않습니다.<br>";
		                					}
		                				}
		                			}else if(chkDb.get(j).equals("edu1")) {
		                				//교육이수번호,인증서번호 유효 체크
		                				eduChkParam.setCareerTyp(cellVal);
		                			}else if(chkDb.get(j).equals("edu2")) {
		                				//교육이수번호,인증서번호 유효 체크
		                				eduChkParam.setUserName(cellVal);
		                			}else if(chkDb.get(j).equals("edu3")) {
		                				//교육이수번호,인증서번호 유효 체크
		                				String birth 	= "";
		                				String gender 	= "";
		                				if(StringUtils.isNotEmpty(cellVal)) {
		                					String[] plMZId = cellVal.split("-");
		                					birth 			= plMZId[0];
				                			gender 			= plMZId[1].substring(0, 1);
		                				}
		                				eduChkParam.setUserBirth(birth);
		                				eduChkParam.setUserSex(gender);
		                			}else if(chkDb.get(j).equals("edu4")) {
		                				//교육이수번호,인증서번호 유효 체크
		                				String prdCd = "prdCd";
		                				if(StringUtils.isNotEmpty(cellVal)) {
			                				if(cellVal.equals("1") || cellVal.equals("3")) {
			                					prdCd = "LP0" + eduChkParam.getCareerTyp();
			                				}else if(cellVal.equals("5") || cellVal.equals("6")) {
			                					prdCd = "LS0" + eduChkParam.getCareerTyp();
			                				}
		                				}
		                				eduChkParam.setProcessCd(prdCd);
		                			}else if(chkDb.get(j).equals("edu5")) {
		                				//교육이수번호,인증서번호 유효 체크
		                				if(eduChkParam.getProcessCd() == null || eduChkParam.getProcessCd().equals("")) {
		                					//법인의 임원 또는 전문인력 등록하는 엑셀에는 금융상품유형 없으므로 화면에서 값 가져옴
		                					String prdCd = "";
		                					if(this.param1.equals("01") || this.param1.equals("03")) {
		                						prdCd = "LP0" + eduChkParam.getCareerTyp();
			                				}else if(this.param1.equals("05") || this.param1.equals("06")) {
			                					prdCd = "LS0" + eduChkParam.getCareerTyp();
			                				}
		                					eduChkParam.setProcessCd(prdCd);
		                				}
		                				boolean goContinue = true;
		                				if(StringUtils.isNotEmpty(this.param3)) {
		                					//법인의 임원 엑셀은 교육이수번호 셀에 값이 있을 때만 유효성 체크
		                					if(StringUtils.isEmpty(cellVal)) {
		                						goContinue = false;
		                					}
		                				}
		                				if(goContinue) {
		                					eduChkParam.setSrchInput(cellVal);
			                				int chkResult = eduService.plEduNoChk(eduChkParam);
				                			
			                				if(chkResult == 0) {
			                					errorMsg += row.getRowNum() + 1 + "번째 줄의 신규/경력, 금융상품유형, 교육이수번호/인증서번호가 유효하지 않습니다.<br>";
			                				}
		                				}
		                			}
		                		}
		                		if(!chkFormat.get(j).isEmpty()){
		                			if(chkFormat.get(j).equals("pId")) {
		                				//주민등록번호 형식 체크
		                				if(StringUtils.isNotEmpty(cellVal) && !plMZIdFormatChk(cellVal)) {
			                				errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " 형식을 확인해 주세요.<br>";
			                			}
		                			}else if(chkFormat.get(j).equals("cal")) {
		                				//날짜 형식 체크
		                				if(StringUtils.isNotEmpty(cellVal) && !dateFormatChk(cellVal,"yyyy-MM-dd")) {
			                				errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + "의 날짜는 yyyy-mm-dd 형식으로 입력해 주세요.<br>";
			                			}
		                			}else if(chkFormat.get(j).equals("ci")) {
		                				//CI 형식 체크
		                				if(StringUtils.isNotEmpty(cellVal) && !cellVal.endsWith("==")) {
		                					errorMsg += row.getRowNum() + 1 + "번째 줄의 CI 형식이 유효하지 않습니다.<br>";
		                				}
		                			}else if(chkFormat.get(j).equals("mNo")) {
		                				//법인번호 형식 체크
	                					if(StringUtils.isNotEmpty(cellVal) && !plMerchantNoFormatChk(cellVal)) {
			                				errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + " 형식을 확인해 주세요.<br>";
			                			}
		                			}
		                		}
		                		if(!vEncrypt.get(j).isEmpty()){
		                			//암호화(주민번호,법인번호)
		                			if(vEncrypt.get(j).equals("Y")) {
		                				if(StringUtils.isNotEmpty(cellVal)) {
		                					if(cryptoApply) {
		                						cellVal = CryptoUtil.encrypt(cellVal.replaceAll("-", ""));
		                					}else {
		                						cellVal = cellVal.replaceAll("-", "");
		                					}
		                				}
		                			}
		                		}
		                		if(!chkPrd.get(j).isEmpty()) {
		                			//상품별 등록여부 체크 : 대출 상품일 경우 회원사 통틀어서 하나 / 나머지는 중복 가능
		                			Map<String, Object> prdMap = new HashMap<String, Object>();
		                			
		                			if(chkPrd.get(j).equals("prd1")) {
		                				userChkParam.setPlProduct("0"+cellVal);
		                			}else if(chkPrd.get(j).equals("prd2")) {
		                				//S : 엑셀 파일 내 중복 확인용
	                					prdMap.put("plProduct", userChkParam.getPlProduct());
		                				prdMap.put("ci", cellVal);
		                				plProductArr.add(arrPosition, prdMap);
		                				//E : 엑셀 파일 내 중복 확인용
		                				
		                				//모집인 테이블(mas01) 중복 체크
		                				userChkParam.setCi(cellVal);
		                				userChkParam.setPlClass(this.param2);
		                				int dupChkResult = userRegDupChk(userChkParam);
			                			
		                				if(dupChkResult > 0) {
		                					errorMsg += row.getRowNum() + 1 + "번째 줄의 모집인은 이미 등록된 상태입니다.<br>";
	                					}
		                			}
		                		}
		                		if(!chkDate.get(j).isEmpty()) {
		                			/*
		                			if(chkDate.get(j).equals("contDt")) {
		                				//계약일자 체크(엑셀 업로드 시점보다 이후면 X) -> 2021-08-03 계약일자는 예정일자가 들어갈 수 있으므로 업로드일자보다 미래의 날자 기재 가능
		                				if(StringUtils.isNotEmpty(cellVal)) {
		                					if(!comContDateChk(cellVal)) {
				                				errorMsg += row.getRowNum() + 1 + "번째 줄의 " + headerName.get(j) + "가 업로드 일자보다 이후입니다.<br>";
		                					}
		                				}
		                			}
		                			*/
		                			/*
		                			else if(chkDate.get(j).equals("careerStDt")) {
		                				//경력시작일 > 경력종료일 체크
		                				if(StringUtils.isNotEmpty(cellVal)) {
		                					careerDtArr[0] = cellVal;
			                			}
		                			}else if(chkDate.get(j).equals("careerEdDt")) {
		                				//경력시작일 > 경력종료일 체크
		                				if(StringUtils.isNotEmpty(cellVal)) {
		                					careerDtArr[1] = cellVal;
			                			}
		                				if(careerDtArr != null && (careerDtArr[0] != null && !careerDtArr[0].equals("")) && (careerDtArr[1] != null && !careerDtArr[1].equals(""))) {
		                					if(!careerDateChk(careerDtArr[0],careerDtArr[1])) {
		                						errorMsg += row.getRowNum() + 1 + "번째 줄의 경력시작일이 경력종료일보다 이후입니다.<br>";
		                					}
		                				}
		                			}
		                			*/
		                		}
		                	}
		                }
		                map.put(cellName, cellVal);
		            }
		            arrPosition++;
		            result.add(map);
			    }
			}
			//log.info("errorMsg :: " + errorMsg);
			
			log.info("#########################################");
			log.info("UtilExcel > plProductArr :: "+ plProductArr);
			log.info("#########################################");
			
			//최초 등록 시 대출 상품 중복 체크
	    	if(plProductArr.size() > 0) {
	    		//중복제거
	    		Set<Map<String, Object>> set = new HashSet<Map<String, Object>>(plProductArr);
	    		
	    		log.info("#########################################");
	    		log.info("UtilExcel > set :: "+ set);
	    		log.info("#########################################");
	    		
	    		if(plProductArr.size() != set.size()) {
	    			errorMsg = "엑셀 데이터에서 동일한 금융상품유형에 중복된 CI가 존재합니다.";
	    		}
	    	}
			
	    	//에러메세지 있을 때 
	        if(StringUtils.isNotEmpty(errorMsg)) {
	        	errorMsgMap.put("errorMsg", errorMsg);
	        	result.clear();
	        	result.add(errorMsgMap);
	        }
	        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			errorMsgMap.put("errorMsg", "오류가 발생했습니다.<br>관리자에게 문의해 주세요.");
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
	private int selectCorpInfoChk(CorpDomain corpDomain) {
		if(StringUtils.isNotEmpty(corpDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				corpDomain.setPlMerchantNo(CryptoUtil.encrypt(corpDomain.getPlMerchantNo().replaceAll("-", "")));
			}else {
				corpDomain.setPlMerchantNo(corpDomain.getPlMerchantNo().replaceAll("-", ""));
			}
		}
		int result = corpRepo.selectCorpInfoCnt(corpDomain);
		return result;
	}
	
	//모집인 중복체크
	@Transactional(readOnly=true)
	private int userRegDupChk(UserDomain userDomain) {
		return userRepo.userRegDupChk(userDomain);
	}
	
	//날짜 형식 및 값 체크
	private boolean dateFormatChk(String date, String format) {
		try {
			SimpleDateFormat dateFormatParser = new SimpleDateFormat(format, Locale.KOREA);
			dateFormatParser.setLenient(false);
			String result = dateFormatParser.format(dateFormatParser.parse(date));
			return date.equals(result);
		}catch(ParseException e) {
			return false;
		}
	}
	
	//주민등록번호 형식 체크
	private boolean plMZIdFormatChk(String plMZId) {
		return Pattern.matches("^(?:[0-9]{2}(?:0[0-9]|1[0-6])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-8][0-9]{6}$", plMZId);
	}
	
	//법인번호 형식 체크
	private boolean plMerchantNoFormatChk(String plMerchantNo) {
		return Pattern.matches("^[0-9]{6}-[0-9]{7}$", plMerchantNo);
	}

	//계약일자 체크(엑셀 업로드 시점보다 이후면 X)
	private boolean comContDateChk(String comContDate) {
		try {
			SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
			
			Date currentDt 	= dateFormatParser.parse(dateFormatParser.format(new Date()));
			Date comContDt 	= dateFormatParser.parse(comContDate);
			int compare 	= currentDt.compareTo(comContDt);

			if(compare < 0) { //엑셀 업로드 시점 < 계약일자
				return false;
			}
		}catch(ParseException e) {
			return false;
		}
		return true;
	}
	
	//경력시작일 > 경력종료일 체크
	private boolean careerDateChk(String careerStartDate, String careerEndDate) {
		try {
			SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
			
			Date careerStDt = dateFormatParser.parse(careerStartDate);
			Date careerEdDt = dateFormatParser.parse(careerEndDate);
			int compare 	= careerStDt.compareTo(careerEdDt);

			if(compare > 0) { //경력시작일 > 경력종료일
				return false;
			}
		}catch(ParseException e) {
			return false;
		}
		return true;
	}

}
