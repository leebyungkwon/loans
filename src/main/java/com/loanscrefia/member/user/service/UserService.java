package com.loanscrefia.member.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.admin.corp.service.CorpService;
import com.loanscrefia.admin.edu.domain.EduDomain;
import com.loanscrefia.admin.edu.service.EduService;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.domain.PayResultDomain;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.common.member.domain.MemberDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.domain.UserExpertDomain;
import com.loanscrefia.member.user.domain.UserImwonDomain;
import com.loanscrefia.member.user.domain.UserItDomain;
import com.loanscrefia.member.user.domain.excel.UserCorpExcelDomain;
import com.loanscrefia.member.user.domain.excel.UserIndvExcelDomain;
import com.loanscrefia.member.user.repository.UserRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;
import com.loanscrefia.util.UtilExcel;
import com.loanscrefia.util.UtilFile;
import com.loanscrefia.util.UtilMask;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Service
public class UserService {

	@Autowired private UserRepository userRepo;
	@Autowired private CorpService corpService;
	@Autowired private CommonService commonService;
	@Autowired private CodeService codeService;
	@Autowired private EduService eduService;
	@Autowired private UtilFile utilFile;
	@Autowired private UtilExcel<T> utilExcel;
	@Autowired private KfbApiService kfbApiService; //은행연합회
	
	//첨부파일 경로
	@Value("${upload.filePath}")
	public String uPath;
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//은행연합회 API 적용여부
	@Value("${kfbApi.apply}")
	public boolean kfbApiApply;
	
	/* -------------------------------------------------------------------------------------------------------
	 * 금융상품유형이 TM대출,TM리스이면 은행연합회 API 통신 X(임시) -> 추후 조건문 주석
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	public boolean kfbApiContinue(String plProduct) {
		
		boolean kfbApiContinue = true;
		
		//3 : TM대출, 6 : TM리스
		if(plProduct.equals("3") || plProduct.equals("6")) {
			kfbApiContinue = false;
		}
		
		return kfbApiContinue;
	}
	
	public boolean kfbApiContinue2(String plProduct) {
		
		boolean kfbApiContinue = true;
		
		//3 : TM대출, 6 : TM리스
		if(plProduct.equals("03") || plProduct.equals("06")) {
			kfbApiContinue = false;
		}
		
		return kfbApiContinue;
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 조회 및 변경
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//모집인 조회 및 변경 > 리스트
	@Transactional(readOnly=true)
	public List<UserDomain> selectUserConfirmList(UserDomain userDomain){
		
		UtilMask mask = new UtilMask();		
		
		//세션 정보
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		userDomain.setCreGrp(loginInfo.getCreGrp());
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(userDomain.getPlMZId())) {
			if(cryptoApply) {
				userDomain.setPlMZId(CryptoUtil.encrypt(userDomain.getPlMZId().replaceAll("-", "")));
			}else {
				userDomain.setPlMZId(userDomain.getPlMZId().replaceAll("-", ""));
			}
		}
		if(StringUtils.isNotEmpty(userDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				userDomain.setPlMerchantNo(CryptoUtil.encrypt(userDomain.getPlMerchantNo().replaceAll("-", "")));
			}else {
				userDomain.setPlMerchantNo(userDomain.getPlMerchantNo().replaceAll("-", ""));
			}
		}
		
		//리스트
		List<UserDomain> userConfirmList = userRepo.selectUserConfirmList(userDomain);
		
		if(userConfirmList.size() > 0) {
			String plMZId 		= "";
			String plMerchantNo = "";
			for(int i = 0;i < userConfirmList.size();i++) {
				if(StringUtils.isNotEmpty(userConfirmList.get(i).getPlMZId())) {
					if(cryptoApply) {
						plMZId 	= CryptoUtil.decrypt(userConfirmList.get(i).getPlMZId());
					}else {
						plMZId 	= userConfirmList.get(i).getPlMZId();
						userDomain.setPlMZId(plMZId);
					}
					if(StringUtils.isNotEmpty(plMZId)) {
						// 2021-09-30 엑셀다운로드시 주민번호 마스킹 해제
						if(!"false".equals(userDomain.getIsPaging())) {
							plMZId = mask.maskSSN(plMZId);
						}
					}
					plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
					userConfirmList.get(i).setPlMZId(plMZId);
				}
				if(StringUtils.isNotEmpty(userConfirmList.get(i).getPlMerchantNo())) {
					if(cryptoApply) {
						plMerchantNo 	= CryptoUtil.decrypt(userConfirmList.get(i).getPlMerchantNo());
					}else {
						plMerchantNo 	= userConfirmList.get(i).getPlMerchantNo();
					}
					plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
					userConfirmList.get(i).setPlMerchantNo(plMerchantNo);
				}
			}
		}
		return userConfirmList;
	}
	
	//모집인 조회 및 변경 > 
	
	//모집인 조회 및 변경 > 
	
	
	/* -------------------------------------------------------------------------------------------------------
	 * 회원사 시스템 > 모집인 등록
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//모집인 등록 > 리스트
	@Transactional(readOnly=true)
	public List<UserDomain> selectUserRegList(UserDomain userDomain){

		UtilMask mask = new UtilMask();
		
		//세션 정보
		MemberDomain memberDomain 	= new MemberDomain();
		MemberDomain loginInfo 		= commonService.getMemberDetail(memberDomain);
		userDomain.setCreGrp(loginInfo.getCreGrp());
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(userDomain.getPlMZId())) {
			if(cryptoApply) {
				userDomain.setPlMZId(CryptoUtil.encrypt(userDomain.getPlMZId().replaceAll("-", "")));
			}else {
				userDomain.setPlMZId(userDomain.getPlMZId().replaceAll("-", ""));
			}
		}
		if(StringUtils.isNotEmpty(userDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				userDomain.setPlMerchantNo(CryptoUtil.encrypt(userDomain.getPlMerchantNo().replaceAll("-", "")));
			}else {
				userDomain.setPlMerchantNo(userDomain.getPlMerchantNo().replaceAll("-", ""));
			}
		}
		
		//리스트
		List<UserDomain> userRegList = userRepo.selectUserRegList(userDomain);
		
		if(userRegList.size() > 0) {
			String plMZId 		= "";
			String plMerchantNo = "";
			for(int i = 0;i < userRegList.size();i++) {
				if(StringUtils.isNotEmpty(userRegList.get(i).getPlMZId())) {
					if(cryptoApply) {
						plMZId 	= CryptoUtil.decrypt(userRegList.get(i).getPlMZId());
					}else {
						plMZId 	= userRegList.get(i).getPlMZId();
						userDomain.setPlMZId(plMZId);
					}
					if(StringUtils.isNotEmpty(plMZId)) {
						plMZId = mask.maskSSN(plMZId);
					}
					plMZId 		= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
					userRegList.get(i).setPlMZId(plMZId);
				}
				if(StringUtils.isNotEmpty(userRegList.get(i).getPlMerchantNo())) {
					if(cryptoApply) {
						plMerchantNo 	= CryptoUtil.decrypt(userRegList.get(i).getPlMerchantNo());
					}else {
						plMerchantNo 	= userRegList.get(i).getPlMerchantNo();
					}
					plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
					userRegList.get(i).setPlMerchantNo(plMerchantNo);
				}
			}
		}
		return userRegList;
	}
	
	/*
	//모집인 등록(엑셀) > 업로드한 엑셀 파일 삭제(+서버)
	public void userRegExcelFileDelete(int fileGrpSeq) {
		FileDomain fileParam = new FileDomain();
		fileParam.setFileGrpSeq(fileGrpSeq);
		fileParam.setFilePath("excel");
		commonService.realDeleteFileByGrpSeq(fileParam);
	}
	*/
	
	//모집인 등록(엑셀) > 개인
	@Transactional
	public ResponseMsg insertUserRegIndvInfoByExcel(MultipartFile[] files, UserDomain userDomain) throws IOException{
		
		//세션 정보
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam2(userDomain.getPlClass()).upload(uPath, filePath, fileSaveNm, fileExt, UserIndvExcelDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				/*
				//업로드한 엑셀 파일 삭제
				if(file.get(0).getFileGrpSeq() != null) {
					this.userRegExcelFileDelete(file.get(0).getFileGrpSeq());
				}
				*/
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					int insertResult = 0;
					
					if(kfbApiApply) {
						//================================================[S : 은행연합회 통신]================================================
						KfbApiDomain kfbApiDomain 	= new KfbApiDomain();
						String apiToken 			= kfbApiService.selectKfbApiKey(kfbApiDomain);
						String apiMsg 				= "";
						
						/*
						for(int i = 0;i < excelResult.size();i++) {
							if(kfbApiContinue(excelResult.get(i).get("G").toString())) { //******
								//(1)등록가능 여부 조회
								JSONObject checkLoanApiReqParam = new JSONObject();
							
								checkLoanApiReqParam.put("name", excelResult.get(i).get("B").toString());
								checkLoanApiReqParam.put("ssn", CryptoUtil.decrypt(excelResult.get(i).get("C").toString()));
								checkLoanApiReqParam.put("ci", excelResult.get(i).get("O").toString());
								checkLoanApiReqParam.put("loan_type", "0"+excelResult.get(i).get("G").toString());
								
								ResponseMsg checkLoanApiResult = kfbApiService.checkLoan(apiToken, checkLoanApiReqParam);
								
								if(checkLoanApiResult.getCode().equals("success")) {
									//(2)등록가능 여부 조회 결과 : 한건이라도 등록이 불가능한 모집인(reg_yn = "N")이 있으면 데이터 등록 X
									JSONObject checkLoanApiResponse = (JSONObject)checkLoanApiResult.getData();
									log.info("#########################################");
									log.info("insertUserRegIndvInfoByExcel() >> checkLoanApiResponse >> "+checkLoanApiResponse);
									log.info("#########################################");
									
									if(checkLoanApiResponse.getString("reg_yn").equals("N")) {
										//checkLoanApiResponse의 reg_yn = "N"일 때
										apiMsg += "은행연합회 등록가능 여부 조회 결과 :: " + excelResult.get(i).get("B").toString() + "("+excelResult.get(i).get("D").toString()+")" + "님은 가등록이 불가능합니다.<br>";
										
									}
								}else {
									apiMsg += "은행연합회 등록가능 여부 조회 실패 :: " + excelResult.get(i).get("B").toString() + "("+excelResult.get(i).get("D").toString()+")" + "님 :: " + checkLoanApiResult.getMessage() + "<br>";
								}
							}
						}
						
						if(StringUtils.isNotEmpty(apiMsg)) {
							return new ResponseMsg(HttpStatus.OK, "", apiMsg, "");
						}
						*/
						
						//(3)가등록
						for(int j = 0;j < excelResult.size();j++) {
							if(kfbApiContinue(excelResult.get(j).get("G").toString())) { //******
								JSONObject preLoanApiReqParam 	= new JSONObject();
								JSONObject conArrParam 			= new JSONObject();
								JSONArray conArr				= new JSONArray();
								
								preLoanApiReqParam.put("name", excelResult.get(j).get("B").toString());
								preLoanApiReqParam.put("ssn", CryptoUtil.decrypt(excelResult.get(j).get("C").toString()));
								preLoanApiReqParam.put("ci", excelResult.get(j).get("O").toString());
								
								if(excelResult.get(j).get("I") == null || excelResult.get(j).get("I").equals("")) {
									conArrParam.put("corp_num", "");
								}else {
									conArrParam.put("corp_num", CryptoUtil.decrypt(excelResult.get(j).get("I").toString()));
								}
								
								conArrParam.put("con_mobile", excelResult.get(j).get("D").toString());
								conArrParam.put("con_date", excelResult.get(j).get("M").toString().replace("-", ""));
								conArrParam.put("fin_code", Integer.toString(loginInfo.getComCode()));
								conArrParam.put("fin_phone", "");
								conArrParam.put("loan_type", "0"+excelResult.get(j).get("G").toString());
								conArr.put(conArrParam);
								
								preLoanApiReqParam.put("con_arr", conArr);
								
								log.info("#########################################");
								log.info("insertUserRegIndvInfoByExcel() >> preLoanApiReqParam >> "+preLoanApiReqParam);
								log.info("#########################################");
								
								ResponseMsg preLoanApiResult = kfbApiService.preLoanIndv(apiToken, preLoanApiReqParam, "POST");
								
								if(preLoanApiResult.getCode().equals("success")) {
									JSONObject preLoanApiResponse = (JSONObject)preLoanApiResult.getData();
									log.info("#########################################");
									log.info("insertUserRegIndvInfoByExcel() >> preLoanApiResponse >> "+preLoanApiResponse);
									log.info("#########################################");
									
									excelResult.get(j).put("preLcNum", preLoanApiResponse.getString("pre_lc_num")); 					//가등록 번호
									
									if(!preLoanApiResponse.isNull("fee_yn")) {
										excelResult.get(j).put("preRegYn", preLoanApiResponse.getString("fee_yn").toString()); 			//수수료 기 납부 여부
									}else {
										excelResult.get(j).put("preRegYn", "N");
									}
									
									/*
									if(!checkLoanApiResponse.isNull("career_yn")) {
										excelResult.get(j).put("apiCareerYn", checkLoanApiResponse.getString("career_yn").toString());	//경력여부
									}else {
										excelResult.get(j).put("apiCareerYn", "N");
									}
									*/
									
									//(4)모집인 테이블 저장
									UserDomain insertDomain 				= new UserDomain();
									List<Map<String, Object>> insertParam 	= new ArrayList<Map<String, Object>>();
									
									insertParam.add(0, excelResult.get(j));
									
									insertDomain.setExcelParam(insertParam);
									insertResult += userRepo.insertUserRegIndvInfoByExcel(insertDomain);
									
								}else {
									apiMsg += "은행연합회 가등록 실패 :: " + excelResult.get(j).get("B").toString() + "("+excelResult.get(j).get("D").toString()+")" + "님 :: " + preLoanApiResult.getMessage() +"<br>";
								}
							}else {
								//(4)금융상품유형이 TM대출,TM리스이면 은행연합회 API 통신은 하지않지만 모집인 테이블에 저장은 해야함
								UserDomain insertDomain 				= new UserDomain();
								List<Map<String, Object>> insertParam 	= new ArrayList<Map<String, Object>>();
								
								insertParam.add(0, excelResult.get(j));
								
								insertDomain.setExcelParam(insertParam);
								insertResult += userRepo.insertUserRegIndvInfoByExcel(insertDomain);
							}
						}
						
						if(StringUtils.isNotEmpty(apiMsg)) {
							return new ResponseMsg(HttpStatus.OK, "", apiMsg, "");
						}
						//================================================[E : 은행연합회 통신]================================================
					}else {
						userDomain.setExcelParam(excelResult);
						insertResult = userRepo.insertUserRegIndvInfoByExcel(userDomain);
					}
					
					//(5)모집인 테이블 저장 결과
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "모집인이 등록되었습니다.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//모집인 등록(엑셀) > 법인
	@Transactional
	public ResponseMsg insertUserRegCorpInfoByExcel(MultipartFile[] files, UserDomain userDomain) throws IOException{
		
		//세션 정보
		HttpServletRequest request 	= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session 		= request.getSession();
		MemberDomain loginInfo 		= (MemberDomain)session.getAttribute("member");
		
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam2(userDomain.getPlClass()).upload(uPath, filePath, fileSaveNm, fileExt, UserCorpExcelDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				/*
				//업로드한 엑셀 파일 삭제
				if(file.get(0).getFileGrpSeq() != null) {
					this.userRegExcelFileDelete(file.get(0).getFileGrpSeq());
				}
				*/
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					int insertResult = 0;
					
					if(kfbApiApply) {
						//================================================[S : 은행연합회 통신]================================================
						KfbApiDomain kfbApiDomain 	= new KfbApiDomain();
						String apiToken 			= kfbApiService.selectKfbApiKey(kfbApiDomain);
						String apiMsg 				= "";
						
						/*
						for(int i = 0;i < excelResult.size();i++) {
							if(kfbApiContinue(excelResult.get(i).get("K").toString())) { //******
								//(1)등록가능 여부 조회
								JSONObject checkLoanApiReqParam = new JSONObject();
							
								checkLoanApiReqParam.put("corp_num", CryptoUtil.decrypt(excelResult.get(i).get("E").toString()));
								checkLoanApiReqParam.put("corp_rep_ssn", CryptoUtil.decrypt(excelResult.get(i).get("C").toString()));
								checkLoanApiReqParam.put("corp_rep_ci", excelResult.get(i).get("N").toString());
								checkLoanApiReqParam.put("loan_type", "0"+excelResult.get(i).get("K").toString());
								
								ResponseMsg checkLoanApiResult = kfbApiService.checkLoanCorp(apiToken, checkLoanApiReqParam);
								
								if(checkLoanApiResult.getCode().equals("success")) {
									//(2)등록가능 여부 조회 결과 : 한건이라도 등록이 불가능한 모집인(reg_yn = "N")이 있으면 데이터 등록 X
									JSONObject checkLoanApiResponse = (JSONObject)checkLoanApiResult.getData();
									log.info("#########################################");
									log.info("insertUserRegCorpInfoByExcel() >> checkLoanApiResponse >> "+checkLoanApiResponse);
									log.info("#########################################");
									
									if(checkLoanApiResponse.getString("reg_yn").equals("N")) {
										//checkLoanApiResponse의 reg_yn = "N"일 때
										apiMsg += "은행연합회 등록가능 여부 조회 결과 :: 법인등록번호 " + CryptoUtil.decrypt(excelResult.get(i).get("E").toString()) + " :: 가등록이 불가능합니다.<br>";
										
									}
								}else {
									apiMsg += "은행연합회 등록가능 여부 조회 실패 :: 법인등록번호 " + CryptoUtil.decrypt(excelResult.get(i).get("E").toString()) + " :: " + checkLoanApiResult.getMessage() + "<br>";
								}
							}
						}
						
						if(StringUtils.isNotEmpty(apiMsg)) {
							return new ResponseMsg(HttpStatus.OK, "", apiMsg, "");
						}
						*/
						
						//(3)가등록
						for(int j = 0;j < excelResult.size();j++) {
							if(kfbApiContinue(excelResult.get(j).get("K").toString())) { //******
								JSONObject preLoanApiReqParam 	= new JSONObject();
								JSONObject conArrParam 			= new JSONObject();
								JSONArray conArr				= new JSONArray();
								
								preLoanApiReqParam.put("corp_num", CryptoUtil.decrypt(excelResult.get(j).get("E").toString()));
								preLoanApiReqParam.put("corp_name", excelResult.get(j).get("A").toString());
								preLoanApiReqParam.put("corp_rep_name", excelResult.get(j).get("B").toString());
								preLoanApiReqParam.put("corp_rep_ssn", CryptoUtil.decrypt(excelResult.get(j).get("C").toString()));
								preLoanApiReqParam.put("corp_rep_ci", excelResult.get(j).get("N").toString());
								
								conArrParam.put("con_date", excelResult.get(j).get("L").toString().replace("-", ""));
								conArrParam.put("fin_code", Integer.toString(loginInfo.getComCode()));
								conArrParam.put("fin_phone", "");
								conArrParam.put("loan_type", "0"+excelResult.get(j).get("K").toString());
								conArr.put(conArrParam);
								
								preLoanApiReqParam.put("con_arr", conArr);
								
								log.info("#########################################");
								log.info("insertUserRegCorpInfoByExcel() >> preLoanApiReqParam >> "+preLoanApiReqParam);
								log.info("#########################################");
								
								ResponseMsg preLoanApiResult = kfbApiService.preLoanCorp(apiToken, preLoanApiReqParam, "POST");
								
								if(preLoanApiResult.getCode().equals("success")) {
									JSONObject preLoanApiResponse = (JSONObject)preLoanApiResult.getData();
									log.info("#########################################");
									log.info("insertUserRegCorpInfoByExcel() >> preLoanApiResponse >> "+preLoanApiResponse);
									log.info("#########################################");
									
									excelResult.get(j).put("preLcNum", preLoanApiResponse.getString("pre_corp_lc_num")); 		//가등록 번호
									
									if(!preLoanApiResponse.isNull("fee_yn")) {
										excelResult.get(j).put("preRegYn", preLoanApiResponse.getString("fee_yn").toString()); 	//수수료 기 납부 여부
									}else {
										excelResult.get(j).put("preRegYn", "N");
									}
									
									//(4)모집인 테이블 저장
									UserDomain insertDomain 				= new UserDomain();
									List<Map<String, Object>> insertParam 	= new ArrayList<Map<String, Object>>();
									
									insertParam.add(0, excelResult.get(j));
									
									insertDomain.setExcelParam(insertParam);
									insertResult += userRepo.insertUserRegCorpInfoByExcel(insertDomain);
									
									//(5)법인 정보 저장
									CorpDomain corpDomain = new CorpDomain();
									corpDomain.setExcelParam(insertParam);
									corpService.insertCorpInfoByExcel(corpDomain);
									
								}else {
									apiMsg += "은행연합회 가등록 실패 :: 법인등록번호 " + CryptoUtil.decrypt(excelResult.get(j).get("E").toString()) + " :: "+ preLoanApiResult.getMessage() +"<br>";
								}
							}else {
								//(4)금융상품유형이 TM대출,TM리스이면 은행연합회 API 통신은 하지않지만 모집인 테이블에 저장은 해야함
								UserDomain insertDomain 				= new UserDomain();
								List<Map<String, Object>> insertParam 	= new ArrayList<Map<String, Object>>();
								
								insertParam.add(0, excelResult.get(j));
								
								insertDomain.setExcelParam(insertParam);
								insertResult += userRepo.insertUserRegCorpInfoByExcel(insertDomain);
								
								//(5)법인 정보 저장
								CorpDomain corpDomain = new CorpDomain();
								corpDomain.setExcelParam(insertParam);
								corpService.insertCorpInfoByExcel(corpDomain);
							}
						}
						
						if(StringUtils.isNotEmpty(apiMsg)) {
							return new ResponseMsg(HttpStatus.OK, "", apiMsg, "");
						}
						//================================================[E : 은행연합회 통신]================================================
					}else {
						userDomain.setExcelParam(excelResult);
						insertResult = userRepo.insertUserRegCorpInfoByExcel(userDomain);
						
						CorpDomain corpDomain = new CorpDomain();
						corpDomain.setExcelParam(excelResult);
						corpService.insertCorpInfoByExcel(corpDomain);
					}
					
					//(6)모집인 테이블 저장 결과
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "모집인이 등록되었습니다.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//모집인 등록(엑셀) > 법인 : 대표자 및 임원 정보 등록
	@Transactional
	public ResponseMsg insertUserRegCorpImwonInfoByExcel(MultipartFile[] files, UserImwonDomain userImwonDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userImwonDomain.getMasterSeq(),"save");
				
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam1(userImwonDomain.getPlProduct())
											   .setParam3("imwon")
											   .upload(uPath, filePath, fileSaveNm, fileExt, UserImwonDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				/*
				//업로드한 엑셀 파일 삭제
				if(file.get(0).getFileGrpSeq() != null) {
					this.userRegExcelFileDelete(file.get(0).getFileGrpSeq());
				}
				*/
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userImwonDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegCorpImwonInfoByExcel(userImwonDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "대표자 및 임원이 등록되었습니다.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//모집인 등록(수동) > 법인 : 대표자 및 임원 정보 등록
	public ResponseMsg insertUserRegCorpImwonInfo(MultipartFile[] files, UserImwonDomain userImwonDomain, FileDomain fileDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userImwonDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//상세
		UserDomain param 		= new UserDomain();
		param.setMasterSeq(userImwonDomain.getMasterSeq());
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(param);
		
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userImwonDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userImwonDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		//교육이수번호 체크
		if(StringUtils.isNotEmpty(userImwonDomain.getPlEduNo())) {
			EduDomain eduChkParam 	= new EduDomain();
			String[] plMZId 		= userImwonDomain.getPlMZId().split("-");
			String birth 			= plMZId[0];
			String gender 			= plMZId[1].substring(0, 1);
			String prdCd			= "";
			
			if(userRegInfo.getPlProduct().equals("01") || userRegInfo.getPlProduct().equals("03")) {
				prdCd = "LP0" + userImwonDomain.getCareerTyp();
			}else if(userRegInfo.getPlProduct().equals("05") || userRegInfo.getPlProduct().equals("06")) {
				prdCd = "LS0" + userImwonDomain.getCareerTyp();
			}
			
			eduChkParam.setCareerTyp(userImwonDomain.getCareerTyp());
			eduChkParam.setUserName(userImwonDomain.getExcName());
			eduChkParam.setUserBirth(birth);
			eduChkParam.setUserSex(gender);
			eduChkParam.setProcessCd(prdCd);
			eduChkParam.setSrchInput(userImwonDomain.getPlEduNo());
			
			int chkResult = eduService.plEduNoChk(eduChkParam);
			
			if(chkResult == 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "신규/경력, 금융상품유형, 교육이수번호/인증서번호가 일치하지 않습니다.");
			}
		}
		
		//등록
		if(StringUtils.isNotEmpty(userImwonDomain.getPlMZId())) {
			if(cryptoApply) {
				userImwonDomain.setPlMZId(CryptoUtil.encrypt(userImwonDomain.getPlMZId().replace("-", "")));
			}else {
				userImwonDomain.setPlMZId(userImwonDomain.getPlMZId().replace("-", ""));
			}
		}
		int result = userRepo.insertUserRegCorpImwonInfo(userImwonDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록(엑셀) > 법인 : 전문인력 정보 등록
	@Transactional
	public ResponseMsg insertUserRegCorpExpertInfoByExcel(MultipartFile[] files, UserExpertDomain userExpertDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userExpertDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.setParam1(userExpertDomain.getPlProduct()).upload(uPath, filePath, fileSaveNm, fileExt, UserExpertDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");
				
				/*
				//업로드한 엑셀 파일 삭제
				if(file.get(0).getFileGrpSeq() != null) {
					this.userRegExcelFileDelete(file.get(0).getFileGrpSeq());
				}
				*/
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userExpertDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegCorpExpertInfoByExcel(userExpertDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "업무수행인력이 등록되었습니다.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//모집인 등록(수동) > 법인 : 전문인력 정보 등록
	public ResponseMsg insertUserRegCorpExpertInfo(MultipartFile[] files, UserExpertDomain userExpertDomain, FileDomain fileDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userExpertDomain.getMasterSeq(),"save");
				
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//상세
		UserDomain param 		= new UserDomain();
		param.setMasterSeq(userExpertDomain.getMasterSeq());
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(param);
		
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userExpertDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userExpertDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		//교육이수번호 체크
		EduDomain eduChkParam 	= new EduDomain();
		String[] plMZId 		= userExpertDomain.getPlMZId().split("-");
		String birth 			= plMZId[0];
		String gender 			= plMZId[1].substring(0, 1);
		String prdCd			= "";
		
		if(userRegInfo.getPlProduct().equals("01") || userRegInfo.getPlProduct().equals("03")) {
			prdCd = "LP0" + userExpertDomain.getCareerTyp();
		}else if(userRegInfo.getPlProduct().equals("05") || userRegInfo.getPlProduct().equals("06")) {
			prdCd = "LS0" + userExpertDomain.getCareerTyp();
		}
		
		eduChkParam.setCareerTyp(userExpertDomain.getCareerTyp());
		eduChkParam.setUserName(userExpertDomain.getExpName());
		eduChkParam.setUserBirth(birth);
		eduChkParam.setUserSex(gender);
		eduChkParam.setProcessCd(prdCd);
		eduChkParam.setSrchInput(userExpertDomain.getPlEduNo());
		
		int chkResult = eduService.plEduNoChk(eduChkParam);
		
		if(chkResult == 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", "신규/경력, 금융상품유형, 교육이수번호/인증서번호가 일치하지 않습니다.");
		}
		
		//등록
		if(StringUtils.isNotEmpty(userExpertDomain.getPlMZId())) {
			if(cryptoApply) {
				userExpertDomain.setPlMZId(CryptoUtil.encrypt(userExpertDomain.getPlMZId().replace("-", "")));
			}else {
				userExpertDomain.setPlMZId(userExpertDomain.getPlMZId().replace("-", ""));
			}
		}
		int result = userRepo.insertUserRegCorpExpertInfo(userExpertDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록(엑셀) > 법인 : 전산인력 정보 등록
	@Transactional
	public ResponseMsg insertUserRegCorpItInfoByExcel(MultipartFile[] files, UserItDomain userItDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userItDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//첨부파일 저장(엑셀업로드용 path에 저장 후 배치로 삭제 예정)
		Map<String, Object> ret = utilFile.setPath("excel")
				.setFiles(files)
				.setExt("excel")
				//.setEntity(fileDomain)
				.upload();
		
		List<Map<String, Object>> excelResult = new ArrayList<Map<String, Object>>();
		
		//첨부파일 저장에 성공하면
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				//엑셀 업로드
				String filePath		= file.get(0).getFilePath();
				String fileSaveNm	= file.get(0).getFileSaveNm();
				String fileExt		= file.get(0).getFileExt();
				excelResult			= utilExcel.upload(uPath, filePath, fileSaveNm, fileExt, UserItDomain.class);
				
				//엑셀 업로드 후 에러메세지
				String errorMsg = (String)excelResult.get(0).get("errorMsg");

				/*
				//업로드한 엑셀 파일 삭제
				if(file.get(0).getFileGrpSeq() != null) {
					this.userRegExcelFileDelete(file.get(0).getFileGrpSeq());
				}
				*/
				
				if(errorMsg != null && !errorMsg.equals("")) {
					//에러메세지 있음
					return new ResponseMsg(HttpStatus.OK, "", errorMsg, "");
				}else {
					//에러메세지 없음 -> 저장
					userItDomain.setExcelParam(excelResult);
					int insertResult = userRepo.insertUserRegCorpItInfoByExcel(userItDomain);
					
					if(insertResult > 0) {
						return new ResponseMsg(HttpStatus.OK, "success", "전산인력이 등록되었습니다.");
					}
				}
			}
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//모집인 등록(수동) > 법인 : 전산인력 정보 등록
	public ResponseMsg insertUserRegCorpItInfo(MultipartFile[] files, UserItDomain userItDomain, FileDomain fileDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userItDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userItDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userItDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		//등록
		if(StringUtils.isNotEmpty(userItDomain.getPlMZId())) {
			if(cryptoApply) {
				userItDomain.setPlMZId(CryptoUtil.encrypt(userItDomain.getPlMZId().replace("-", "")));
			}else {
				userItDomain.setPlMZId(userItDomain.getPlMZId().replace("-", ""));
			}
		}
		int result = userRepo.insertUserRegCorpItInfo(userItDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 승인요청
	@Transactional
	public int userAcceptApply(UserDomain userDomain){
		
		int result 			= 0;
		int[] masterSeqArr 	= userDomain.getMasterSeqArr();
		
		for(int i = 0;i < masterSeqArr.length;i++) {
			userDomain.setMasterSeq(masterSeqArr[i]);
			
			UserDomain userRegInfo = userRepo.getUserRegDetail(userDomain);
			
			if(userRegInfo.getPlRegStat().equals("2")) {
				//모집인상태가 승인완료이면 승인요청 불가
				return -2;
			}
			
			/*
			//2021.08.11 엑셀 업로드할 때 법인 등록여부는 체크하니까 협회쪽에서 승인처리할 때 로직 추가
			if(userRegInfo.getPlClass().equals("1") && userRegInfo.getCorpUserYn().equals("Y")) {
				//법인사용인일 때 -> 해당 법인이 승인된 후에 승인요청할 수 있음 + 금융감독원 승인여부가 Y이면 패스
				int corpCheck 		= userRepo.corpStatCheck(userRegInfo);
				int corpPassCheck 	= corpService.corpPassCheck(userRegInfo);
				
				if(corpCheck == 0 && corpPassCheck == 0) {
					return -1;
				}
			}
			*/
			
			if(!userRegInfo.getPlStat().equals("2") && !userRegInfo.getPlStat().equals("10")) {
				//처리상태가 승인요청중이거나 등록요건 불충족이면 이력 저장 X, 상태값 수정 X
				//기본 이력 저장*****
				this.insertUserHistory(userDomain);
				
				//최초승인요청일 저장
				if(StringUtils.isEmpty(userRegInfo.getFirstAppDate())) {
					userDomain.setFirstAppDate("reg");
				}
				
				//상태값 수정
				result += this.updateUserStat(userDomain);
			}
		}
		return result;
	}
	
	//모집인 등록 > 상세 > 승인요청
	@Transactional
	public int userAcceptApply2(MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
		
		int result 				= 0;
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(userDomain);
		
		if(userRegInfo.getPlRegStat().equals("2")) {
			//모집인상태가 승인완료이면 승인요청 불가
			return -2;
		}
		
		/*
		//2021.08.11 엑셀 업로드할 때 법인 등록여부는 체크하니까 협회쪽에서 승인처리할 때 로직 추가
		if(userRegInfo.getPlClass().equals("1") && userRegInfo.getCorpUserYn().equals("Y")) {
			//법인사용인일 때 -> 해당 법인이 승인된 후에 승인요청할 수 있음 + 금융감독원 승인여부가 Y이면 패스
			int corpCheck 		= userRepo.corpStatCheck(userRegInfo);
			int corpPassCheck 	= corpService.corpPassCheck(userRegInfo);
			
			if(corpCheck == 0 && corpPassCheck == 0) {
				return -1;
			}
		}
		*/
		
		//기본 이력 저장*****
		this.insertUserHistory(userDomain);
		
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return -3;
		}
		
		//수정
		if(StringUtils.isNotEmpty(userDomain.getPlMZId())) {
			if(cryptoApply) {
				userDomain.setPlMZId(CryptoUtil.encrypt(userDomain.getPlMZId().replace("-", "")));
			}else {
				userDomain.setPlMZId(userDomain.getPlMZId().replace("-", ""));
			}
		}
		if(StringUtils.isNotEmpty(userDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				userDomain.setPlMerchantNo(CryptoUtil.encrypt(userDomain.getPlMerchantNo().replace("-", "")));
			}else {
				userDomain.setPlMerchantNo(userDomain.getPlMerchantNo().replace("-", ""));
			}
		}
		int updateResult1 = userRepo.updateUserRegInfo(userDomain);
		
		//최초승인요청일 저장
		if(StringUtils.isEmpty(userRegInfo.getFirstAppDate())) {
			userDomain.setFirstAppDate("reg");
		}
		
		//상태값 수정
		userDomain.setPlStat("2");
		int updateResult2 = this.updateUserStat(userDomain);
		
		//결과
		if(updateResult1 > 0 && updateResult2 > 0) {
			result = updateResult1 + updateResult2;
		}
		
		return result;
	}
	
	//모집인 등록 > 상세 : 개인
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegIndvDetail(UserDomain userDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//위반이력 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(userDomain);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//첨부파일
    	if(userRegInfo.getFileSeq() != null) {
    		FileDomain fileParam = new FileDomain();
    		
    		fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
        	List<FileDomain> fileList = commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				userRegInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				userRegInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				userRegInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				userRegInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				userRegInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				userRegInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("8")) {
        				userRegInfo.setFileType8(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("9")) {
        				userRegInfo.setFileType9(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("11")) {
        				userRegInfo.setFileType11(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("12")) {
        				userRegInfo.setFileType12(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("14")) {
        				userRegInfo.setFileType14(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				userRegInfo.setFileType15(fileList.get(i));
        			}
        		}
        	}
    	}
    	
    	//위반이력
    	List<UserDomain> violationInfoList 	= userRepo.selectUserViolationInfoList(userDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(userDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
    	
    	//전달
    	result.put("addrCodeList", addrCodeList);
    	result.put("violationCodeList", violationCodeList);
    	result.put("userRegInfo", userRegInfo);
    	result.put("violationInfoList", violationInfoList);
    	result.put("payResult", payResult);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(등록정보 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegCorpDetail(UserDomain userDomain){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("ADD001");
		List<CodeDtlDomain> addrCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//위반이력 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("VIT001");
		List<CodeDtlDomain> violationCodeList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(userDomain);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//첨부파일
		if(userRegInfo.getFileSeq() != null) {
			FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("1")) {
        				userRegInfo.setFileType1(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("2")) {
        				userRegInfo.setFileType2(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("3")) {
        				userRegInfo.setFileType3(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("4")) {
        				userRegInfo.setFileType4(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("5")) {
        				userRegInfo.setFileType5(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("6")) {
        				userRegInfo.setFileType6(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("15")) {
        				userRegInfo.setFileType15(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("31")) {
        				userRegInfo.setFileType31(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("32")) {
        				userRegInfo.setFileType32(fileList.get(i));
        			}
        		}
        	}
		}
		
		//위반이력
    	List<UserDomain> violationInfoList = userRepo.selectUserViolationInfoList(userDomain);
    	
    	//결제정보
    	PayResultDomain payResultDomain 	= new PayResultDomain();
    	payResultDomain.setMasterSeq(userDomain.getMasterSeq());
    	PayResultDomain payResult 			= commonService.getPayResultDetail(payResultDomain);
		
		//전달
		result.put("addrCodeList", addrCodeList);
		result.put("violationCodeList", violationCodeList);
		result.put("userRegInfo", userRegInfo);
		result.put("violationInfoList", violationInfoList);
		result.put("payResult", payResult);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(대표자 및 임원관련사항 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegCorpImwonDetail(UserImwonDomain userImwonDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();

		//구분(신규,경력) 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상근여부 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("FTM001");
		List<CodeDtlDomain> fullTmStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//전문인력여부 코드 리스트
		codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("EXP001");
		List<CodeDtlDomain> expertStatList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		UserDomain dtlParam			= new UserDomain();
		dtlParam.setMasterSeq(userImwonDomain.getMasterSeq());
		UserDomain userRegInfo 		= userRepo.getUserRegDetail(dtlParam);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//대표자 및 임원 리스트
		List<UserImwonDomain> imwonList = userRepo.selectUserRegCorpImwonList(userImwonDomain);
		
		//첨부파일
		if(imwonList.size() > 0) {
			for(int i = 0;i < imwonList.size();i++) {
				String imwonPlMZId 	= "";
				if(cryptoApply) {
					imwonPlMZId 	= CryptoUtil.decrypt(imwonList.get(i).getPlMZId());
				}else {
					imwonPlMZId 	= imwonList.get(i).getPlMZId();
				}
				imwonPlMZId 		= imwonPlMZId.substring(0, 6) + "-" + imwonPlMZId.substring(6);
				imwonList.get(i).setPlMZId(imwonPlMZId);
				
				if(imwonList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(imwonList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("7")) {
							imwonList.get(i).setFileType7(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("8")) {
							imwonList.get(i).setFileType8(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("10")) {
							imwonList.get(i).setFileType10(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("11")) {
							imwonList.get(i).setFileType11(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("12")) {
							imwonList.get(i).setFileType12(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("13")) {
							imwonList.get(i).setFileType13(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("27")) {
							imwonList.get(i).setFileType27(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("28")) {
							imwonList.get(i).setFileType28(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("33")) {
							imwonList.get(i).setFileType33(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("34")) {
							imwonList.get(i).setFileType34(fileList.get(j));
						}
					}
				}
			}
		}
		
		//전달
		result.put("careerTypList", careerTypList);
		result.put("fullTmStatList", fullTmStatList);
		result.put("expertStatList", expertStatList);
		result.put("userRegInfo", userRegInfo);
		result.put("imwonList", imwonList);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(전문인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegCorpExpertDetail(UserExpertDomain userExpertDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//구분(신규,경력) 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("CAR001");
		List<CodeDtlDomain> careerTypList = codeService.selectCodeDtlList(codeDtlParam);
		
		//상세
		UserDomain dtlParam		= new UserDomain();
		dtlParam.setMasterSeq(userExpertDomain.getMasterSeq());
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(dtlParam);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//전문인력 리스트
		List<UserExpertDomain> expertList = userRepo.selectUserRegCorpExpertList(userExpertDomain);
		
		//첨부파일
		if(expertList.size() > 0) {
			for(int i = 0;i < expertList.size();i++) {
				String expertPlMZId = "";
				if(cryptoApply) {
					expertPlMZId 	= CryptoUtil.decrypt(expertList.get(i).getPlMZId());
				}else {
					expertPlMZId 	= expertList.get(i).getPlMZId();
				}
				expertPlMZId 		= expertPlMZId.substring(0, 6) + "-" + expertPlMZId.substring(6);
				expertList.get(i).setPlMZId(expertPlMZId);
				
				if(expertList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(expertList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("16")) {
							expertList.get(i).setFileType16(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("17")) {
							expertList.get(i).setFileType17(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("18")) {
							expertList.get(i).setFileType18(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("35")) {
							expertList.get(i).setFileType35(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("36")) {
							expertList.get(i).setFileType36(fileList.get(j));
						}
					}
				}
			}
		}
		
		//전달
		result.put("careerTypList", careerTypList);
		result.put("userRegInfo", userRegInfo);
		result.put("expertList", expertList);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(전산인력 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegCorpItDetail(UserItDomain userItDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		UserDomain dtlParam		= new UserDomain();
		dtlParam.setMasterSeq(userItDomain.getMasterSeq());
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(dtlParam);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//전산인력 리스트
		List<UserItDomain> itList 	= userRepo.selectUserRegCorpItList(userItDomain);
		
		//첨부파일
		if(itList.size() > 0) {
			for(int i = 0;i < itList.size();i++) {
				if(StringUtils.isNotEmpty(itList.get(i).getPlMZId())) {
					String itPlMZId 	= "";
					if(cryptoApply) {
						itPlMZId 		= CryptoUtil.decrypt(itList.get(i).getPlMZId());
					}else {
						itPlMZId 		= itList.get(i).getPlMZId();
					}
					itPlMZId 			= itPlMZId.substring(0, 6) + "-" + itPlMZId.substring(6);
					itList.get(i).setPlMZId(itPlMZId);
				}
				if(itList.get(i).getFileSeq() != null) {
					FileDomain fileParam 		= new FileDomain();
					fileParam.setFileGrpSeq(itList.get(i).getFileSeq());
					List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
					
					for(int j = 0;j < fileList.size();j++) {
						if(fileList.get(j).getFileType().equals("19")) {
							itList.get(i).setFileType19(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("20")) {
							itList.get(i).setFileType20(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("37")) {
							itList.get(i).setFileType37(fileList.get(j));
						}else if(fileList.get(j).getFileType().equals("38")) {
							itList.get(i).setFileType38(fileList.get(j));
						}
					}
				}
			}
		}
		
		//전달
		result.put("userRegInfo", userRegInfo);
		result.put("itList", itList);
		
		return result;
	}
	
	//모집인 등록 > 상세 : 법인(기타 탭)
	@Transactional(readOnly=true)
	public Map<String,Object> getUserRegCorpEtcDetail(UserDomain userDomain){
		
		Map<String, Object> result 	= new HashMap<String, Object>();
		
		//상세
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(userDomain);
		
		if(StringUtils.isNotEmpty(userRegInfo.getPlMZId())) {
			String plMZId 	= "";
			if(cryptoApply) {
				plMZId 		= CryptoUtil.decrypt(userRegInfo.getPlMZId());
			}else {
				plMZId 		= userRegInfo.getPlMZId();
			}
			plMZId 			= plMZId.substring(0, 6) + "-" + plMZId.substring(6);
			userRegInfo.setPlMZId(plMZId);
			
		}
		if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
			String plMerchantNo = "";
			if(cryptoApply) {
				plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
			}else {
				plMerchantNo 	= userRegInfo.getPlMerchantNo();
			}
			plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
			userRegInfo.setPlMerchantNo(plMerchantNo);
		}
		
		//첨부파일
    	if(userRegInfo.getFileSeq() != null) {
    		FileDomain fileParam 		= new FileDomain();
    		fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
        	List<FileDomain> fileList 	= commonService.selectFileList(fileParam);
        	
        	if(fileList.size() > 0) {
        		for(int i = 0;i < fileList.size();i++) {
        			if(fileList.get(i).getFileType().equals("21")) {
        				userRegInfo.setFileType21(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("22")) {
        				userRegInfo.setFileType22(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("23")) {
        				userRegInfo.setFileType23(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("25")) {
        				userRegInfo.setFileType25(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("26")) {
        				userRegInfo.setFileType26(fileList.get(i));
        			}else if(fileList.get(i).getFileType().equals("29")) {
        				userRegInfo.setFileType29(fileList.get(i));
        			}
        		}
        	}
    	}
    	
		//전달
		result.put("userRegInfo", userRegInfo);
		
		return result;
	}
	
	//모집인 등록 > 수정
	@Transactional
	public ResponseMsg updateUserRegInfo(MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//기본 이력 저장*****
		this.insertUserHistory(userDomain);
				
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		//수정
		if(StringUtils.isNotEmpty(userDomain.getPlMZId())) {
			if(cryptoApply) {
				userDomain.setPlMZId(CryptoUtil.encrypt(userDomain.getPlMZId().replace("-", "")));
			}else {
				userDomain.setPlMZId(userDomain.getPlMZId().replace("-", ""));
			}
		}
		if(StringUtils.isNotEmpty(userDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				userDomain.setPlMerchantNo(CryptoUtil.encrypt(userDomain.getPlMerchantNo().replace("-", "")));
			}else {
				userDomain.setPlMerchantNo(userDomain.getPlMerchantNo().replace("-", ""));
			}
		}
		int result = userRepo.updateUserRegInfo(userDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 수정 : 법인(대표자 및 임원관련사항 탭)
	@Transactional
	public ResponseMsg updateUserRegCorpImwonInfo(MultipartFile[] files, UserImwonDomain userImwonDomain, FileDomain fileDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userImwonDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userImwonDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userImwonDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		//수정
		if(StringUtils.isNotEmpty(userImwonDomain.getPlMZId())) {
			if(cryptoApply) {
				userImwonDomain.setPlMZId(CryptoUtil.encrypt(userImwonDomain.getPlMZId().replace("-", "")));
			}else {
				userImwonDomain.setPlMZId(userImwonDomain.getPlMZId().replace("-", ""));
			}
		}
		int result = userRepo.updateUserRegCorpImwonInfo(userImwonDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 수정 : 법인(전문인력)
	@Transactional
	public ResponseMsg updateUserRegCorpExpertInfo(MultipartFile[] files, UserExpertDomain userExpertDomain, FileDomain fileDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userExpertDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userExpertDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userExpertDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		//수정
		if(StringUtils.isNotEmpty(userExpertDomain.getPlMZId())) {
			if(cryptoApply) {
				userExpertDomain.setPlMZId(CryptoUtil.encrypt(userExpertDomain.getPlMZId().replace("-", "")));
			}else {
				userExpertDomain.setPlMZId(userExpertDomain.getPlMZId().replace("-", ""));
			}
		}
		int result = userRepo.updateUserRegCorpExpertInfo(userExpertDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 수정 : 법인(전산인력)
	@Transactional
	public ResponseMsg updateUserRegCorpItInfo(MultipartFile[] files, UserItDomain userItDomain, FileDomain fileDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userItDomain.getMasterSeq(),"save");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//첨부파일 저장
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userItDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userItDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		//수정
		if(StringUtils.isNotEmpty(userItDomain.getPlMZId())) {
			if(cryptoApply) {
				userItDomain.setPlMZId(CryptoUtil.encrypt(userItDomain.getPlMZId().replace("-", "")));
			}else {
				userItDomain.setPlMZId(userItDomain.getPlMZId().replace("-", ""));
			}
		}
		int result = userRepo.updateUserRegCorpItInfo(userItDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0001", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 삭제 
	@Transactional
	public ResponseMsg deleteUserRegInfo(UserDomain userDomain) throws IOException{
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userDomain.getMasterSeq(),"del");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		//상세
		UserDomain userRegInfo = userRepo.getUserRegDetail(userDomain);
		
		if(userRegInfo.getPlClass().equals("2")) { //법인은 하위에 등록된 데이터(법인사용인,임원 등 정보)가 있으면 삭제 불가
			UserImwonDomain chkParam1 	= new UserImwonDomain();
			UserExpertDomain chkParam2 	= new UserExpertDomain();
			UserItDomain chkParam3 		= new UserItDomain();
			
			//임원
			chkParam1.setMasterSeq(userDomain.getMasterSeq());
			List<UserImwonDomain> imwonList = userRepo.selectUserRegCorpImwonList(chkParam1);
			
			//전문인력
			chkParam2.setMasterSeq(userDomain.getMasterSeq());
			List<UserExpertDomain> expertList = userRepo.selectUserRegCorpExpertList(chkParam2);
			
			//전산인력
			chkParam3.setMasterSeq(userDomain.getMasterSeq());
			List<UserItDomain> itList = userRepo.selectUserRegCorpItList(chkParam3);
			
			if(imwonList.size() > 0 || expertList.size() > 0 || itList.size() > 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "하위 데이터가 존재하여 삭제가 불가능 합니다.");
			}
		}
		
		boolean deleteContinue 	= false;
		int deleteResult 		= 0;
		
		if(userRegInfo.getPlStat().equals("10")) {
			//부적격인 상태는 이미 협회쪽에서 가등록 취소 API 완료된 상태
			deleteContinue = true;
		}else {
			if(kfbApiApply) {
				//가등록 취소 API(TM 제외) + 우리쪽 테이블에서 삭제
				if(kfbApiContinue2(userRegInfo.getPlProduct())) {
					KfbApiDomain kfbApiDomain 		= new KfbApiDomain();
					String apiToken 				= kfbApiService.selectKfbApiKey(kfbApiDomain);
					JSONObject preLoanApiReqParam	= new JSONObject();
					ResponseMsg responseMsg 		= new ResponseMsg(HttpStatus.OK, "fail", null, "오류가 발생하였습니다.");
					
					if("1".equals(userRegInfo.getPlClass())) {
						preLoanApiReqParam.put("pre_lc_num", userRegInfo.getPreLcNum());
						responseMsg = kfbApiService.commonKfbApi(apiToken, preLoanApiReqParam, KfbApiService.ApiDomain+KfbApiService.PreLoanUrl, "DELETE", userRegInfo.getPlClass(), "Y");
					}else {
						preLoanApiReqParam.put("pre_corp_lc_num", userRegInfo.getPreLcNum());
						responseMsg = kfbApiService.commonKfbApi(apiToken, preLoanApiReqParam, KfbApiService.ApiDomain+KfbApiService.PreLoanCorpUrl, "DELETE", userRegInfo.getPlClass(), "Y");
					}
					
					if("success".equals(responseMsg.getCode())) {
						deleteContinue = true;
					}else {
						return responseMsg;
					}
					
				}else {
					deleteContinue = true;
				}
			}else {
				deleteContinue = true;
			}
		}
		
		if(deleteContinue) {
			//기본 이력 저장*****
			this.insertUserHistory(userDomain);
			
			//관련 첨부파일 삭제
			if(userRegInfo.getFileSeq() != null) {
				FileDomain fileParam = new FileDomain();
				fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
				commonService.deleteFileByGrpSeq(fileParam);
				//commonService.realDeleteFileByGrpSeq(fileParam);
			}
			
			//삭제
			deleteResult = userRepo.deleteUserRegInfo(userRegInfo);
		}
		
		if(deleteResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0006", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 선택 삭제
	@Transactional
	public ResponseMsg deleteSelectedUserRegInfo(UserDomain userDomain) throws IOException{
		
		int[] masterSeqArr 	= userDomain.getMasterSeqArr();
		int deleteResult 	= 0;
		
		KfbApiDomain kfbApiDomain 	= new KfbApiDomain();
		String apiToken 			= kfbApiService.selectKfbApiKey(kfbApiDomain);
		
		for(int i = 0;i < masterSeqArr.length;i++) {
			//상세
			userDomain.setMasterSeq(masterSeqArr[i]);
			UserDomain userRegInfo = userRepo.getUserRegDetail(userDomain);
			
			if(userRegInfo != null) {
				//상태값 체크*****
				ResponseMsg userValidation = this.userValidation(masterSeqArr[i],"del");
				
				if(userValidation.getCode().equals("E1")) {
					return userValidation;
				}
				
				if(userRegInfo.getPlClass().equals("2")) { //법인은 하위에 등록된 데이터(법인사용인,임원 등 정보)가 있으면 삭제 불가
					UserImwonDomain chkParam1 	= new UserImwonDomain();
					UserExpertDomain chkParam2 	= new UserExpertDomain();
					UserItDomain chkParam3 		= new UserItDomain();
					
					//임원
					chkParam1.setMasterSeq(userDomain.getMasterSeq());
					List<UserImwonDomain> imwonList = userRepo.selectUserRegCorpImwonList(chkParam1);
					
					//전문인력
					chkParam2.setMasterSeq(userDomain.getMasterSeq());
					List<UserExpertDomain> expertList = userRepo.selectUserRegCorpExpertList(chkParam2);
					
					//전산인력
					chkParam3.setMasterSeq(userDomain.getMasterSeq());
					List<UserItDomain> itList = userRepo.selectUserRegCorpItList(chkParam3);
					
					if(imwonList.size() > 0 || expertList.size() > 0 || itList.size() > 0) {
						if(StringUtils.isNotEmpty(userRegInfo.getPlMerchantNo())) {
							String plMerchantNo = "";
							if(cryptoApply) {
								plMerchantNo 	= CryptoUtil.decrypt(userRegInfo.getPlMerchantNo());
							}else {
								plMerchantNo 	= userRegInfo.getPlMerchantNo();
							}
							plMerchantNo 		= plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6); 
							userRegInfo.setPlMerchantNo(plMerchantNo);
						}
						return new ResponseMsg(HttpStatus.OK, "fail", userRegInfo.getPlMerchantName()+"("+userRegInfo.getPlMerchantNo()+")는 하위 데이터가 존재하여 삭제가 불가능 합니다.");
					}
				}
				
				boolean deleteContinue 	= false;
				
				if(userRegInfo.getPlStat().equals("10")) {
					//부적격인 상태는 이미 협회쪽에서 가등록 취소 API 완료된 상태
					deleteContinue = true;
				}else {
					if(kfbApiApply) {
						//가등록 취소 API(TM 제외) + 우리쪽 테이블에서 삭제
						if(kfbApiContinue2(userRegInfo.getPlProduct())) {
							JSONObject preLoanApiReqParam	= new JSONObject();
							ResponseMsg responseMsg 		= new ResponseMsg(HttpStatus.OK, "fail", null, "오류가 발생하였습니다.");
							
							if("1".equals(userRegInfo.getPlClass())) {
								preLoanApiReqParam.put("pre_lc_num", userRegInfo.getPreLcNum());
								responseMsg = kfbApiService.commonKfbApi(apiToken, preLoanApiReqParam, KfbApiService.ApiDomain+KfbApiService.PreLoanUrl, "DELETE", userRegInfo.getPlClass(), "Y");
							}else {
								preLoanApiReqParam.put("pre_corp_lc_num", userRegInfo.getPreLcNum());
								responseMsg = kfbApiService.commonKfbApi(apiToken, preLoanApiReqParam, KfbApiService.ApiDomain+KfbApiService.PreLoanCorpUrl, "DELETE", userRegInfo.getPlClass(), "Y");
							}
							
							if("success".equals(responseMsg.getCode())) {
								deleteContinue = true;
							}else {
								return responseMsg;
							}
							
						}else {
							deleteContinue = true;
						}
					}else {
						deleteContinue = true;
					}
				}
				
				if(deleteContinue) {
					//기본 이력 저장*****
					this.insertUserHistory(userRegInfo);
					
					//관련 첨부파일 삭제
					if(userRegInfo.getFileSeq() != null) {
						FileDomain fileParam = new FileDomain();
						fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
						commonService.deleteFileByGrpSeq(fileParam);
						//commonService.realDeleteFileByGrpSeq(fileParam);
					}
					
					//삭제
					deleteResult += userRepo.deleteUserRegInfo(userRegInfo);
				}
			}
		}
		if(deleteResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0006", "");
		}
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 삭제 : 법인(대표자 및 임원관련사항 탭)
	@Transactional
	public ResponseMsg deleteUserRegCorpImwonInfo(UserImwonDomain userImwonDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userImwonDomain.getMasterSeq(),"del");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		UserImwonDomain imwonInfo = userRepo.getUserRegCorpImwonInfo(userImwonDomain);
		
		//첨부파일 삭제
		if(imwonInfo.getFileSeq() != null) {
			FileDomain fileParam = new FileDomain();
			fileParam.setFileGrpSeq(imwonInfo.getFileSeq());
			commonService.deleteFileByGrpSeq(fileParam);
			//commonService.realDeleteFileByGrpSeq(fileParam);
		}
		
		//삭제
		int result = userRepo.deleteUserRegCorpImwonInfo(userImwonDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0006", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 삭제 : 법인(전문인력 탭)
	@Transactional
	public ResponseMsg deleteUserRegCorpExpertInfo(UserExpertDomain userExpertDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userExpertDomain.getMasterSeq(),"del");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		UserExpertDomain expertInfo = userRepo.getUserRegCorpExpertInfo(userExpertDomain);
		
		//첨부파일 삭제
		if(expertInfo.getFileSeq() != null) {
			FileDomain fileParam = new FileDomain();
			fileParam.setFileGrpSeq(expertInfo.getFileSeq());
			commonService.deleteFileByGrpSeq(fileParam);
			//commonService.realDeleteFileByGrpSeq(fileParam);
		}
		
		//삭제
		int result = userRepo.deleteUserRegCorpExpertInfo(userExpertDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0006", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	//모집인 등록 > 삭제 : 법인(전산인력 탭)
	@Transactional
	public ResponseMsg deleteUserRegCorpItInfo(UserItDomain userItDomain){
		//상태값 체크*****
		ResponseMsg userValidation = this.userValidation(userItDomain.getMasterSeq(),"del");
		
		if(userValidation.getCode().equals("E1")) {
			return userValidation;
		}
		
		UserItDomain itInfo = userRepo.getUserRegCorpItInfo(userItDomain);
		
		//첨부파일 삭제
		if(itInfo.getFileSeq() != null) {
			FileDomain fileParam = new FileDomain();
			fileParam.setFileGrpSeq(itInfo.getFileSeq());
			commonService.deleteFileByGrpSeq(fileParam);
			//commonService.realDeleteFileByGrpSeq(fileParam);
		}
		
		//삭제
		int result = userRepo.deleteUserRegCorpItInfo(userItDomain);
		
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "COM0006", "");
		}
		
		return new ResponseMsg(HttpStatus.OK, "COM0002", "");
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 모집인 상태 / 처리상태 변경 관련
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//기본
	@Transactional
	public int updateUserStat(UserDomain userDomain) {
		//상태수정
		int result = userRepo.updateUserStat(userDomain);
		
		//단계별 이력 저장*****
		this.insertUserStepHistory(userDomain);
		
		return result; 
	}
	
	//즉시취소
	@Transactional
	public ResponseMsg userCancel(UserDomain userDomain) throws IOException{
		
		//상세
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(userDomain);
		
		if(userRegInfo.getPlClass().equals("2")) { //법인은 하위에 등록된 데이터(법인사용인,임원 등 정보)가 있으면 즉시취소 불가
			UserImwonDomain chkParam1 	= new UserImwonDomain();
			UserExpertDomain chkParam2 	= new UserExpertDomain();
			UserItDomain chkParam3 		= new UserItDomain();
			
			//임원
			chkParam1.setMasterSeq(userDomain.getMasterSeq());
			List<UserImwonDomain> imwonList = userRepo.selectUserRegCorpImwonList(chkParam1);
			
			//전문인력
			chkParam2.setMasterSeq(userDomain.getMasterSeq());
			List<UserExpertDomain> expertList = userRepo.selectUserRegCorpExpertList(chkParam2);
			
			//전산인력
			chkParam3.setMasterSeq(userDomain.getMasterSeq());
			List<UserItDomain> itList = userRepo.selectUserRegCorpItList(chkParam3);
			
			if(imwonList.size() > 0 || expertList.size() > 0 || itList.size() > 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "하위 데이터가 존재하여 즉시취소가 불가능 합니다.");
			}
		}
		
		boolean updateContinue 	= false;
		int updateResult 		= 0;
		
		//가등록 취소 API(TM 제외) + 우리쪽 테이블 UPDATE
		if(kfbApiApply) {
			if(kfbApiContinue2(userRegInfo.getPlProduct())) {
				KfbApiDomain kfbApiDomain 		= new KfbApiDomain();
				String apiToken 				= kfbApiService.selectKfbApiKey(kfbApiDomain);
				JSONObject preLoanApiReqParam	= new JSONObject();
				ResponseMsg responseMsg 		= new ResponseMsg(HttpStatus.OK, "fail", null, "오류가 발생하였습니다.");
				
				if("1".equals(userRegInfo.getPlClass())) {
					preLoanApiReqParam.put("pre_lc_num", userRegInfo.getPreLcNum());
					responseMsg = kfbApiService.commonKfbApi(apiToken, preLoanApiReqParam, KfbApiService.ApiDomain+KfbApiService.PreLoanUrl, "DELETE", userRegInfo.getPlClass(), "Y");
				}else {
					preLoanApiReqParam.put("pre_corp_lc_num", userRegInfo.getPreLcNum());
					responseMsg = kfbApiService.commonKfbApi(apiToken, preLoanApiReqParam, KfbApiService.ApiDomain+KfbApiService.PreLoanCorpUrl, "DELETE", userRegInfo.getPlClass(), "Y");
				}
				
				if("success".equals(responseMsg.getCode())) {
					updateContinue = true;
				}else {
					return responseMsg;
				}
			}else {
				updateContinue = true;
			}
		}else {
			updateContinue = true;
		}
		
		if(updateContinue) {
			//기본 이력 저장*****
			this.insertUserHistory(userDomain);
			
			//관련 첨부파일 삭제
			if(userRegInfo.getFileSeq() != null) {
				FileDomain fileParam = new FileDomain();
				fileParam.setFileGrpSeq(userRegInfo.getFileSeq());
				commonService.deleteFileByGrpSeq(fileParam);
				//commonService.realDeleteFileByGrpSeq(fileParam);
			}
			
			//상태 수정*****
			updateResult = this.updateUserStat(userDomain);
		}
		
		if(updateResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "취소되었습니다.");
		}
		
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//변경요청
	@Transactional
	public ResponseMsg userChangeApply(MultipartFile[] files, UserDomain userDomain, FileDomain fileDomain){
		
		//기본 이력 저장*****
		this.insertUserHistory(userDomain);
		
		//상태 수정*****
		userDomain.setPlStat("3");
		this.updateUserStat(userDomain);
		
		//정보 수정
		Map<String, Object> ret = utilFile.setPath("userReg")
				.setFiles(files)
				.setExt("all")
				.setEntity(fileDomain)
				.multiUpload();
		
		if((boolean) ret.get("success")) {
			List<FileDomain> file = (List<FileDomain>) ret.get("data");
			if(file.size() > 0) {
				userDomain.setFileSeq(file.get(0).getFileGrpSeq());
			}else {
				userDomain.setFileSeq(fileDomain.getFileGrpSeq());
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", ret.get("message"));
		}
		
		if(StringUtils.isNotEmpty(userDomain.getPlMZId())) {
			if(cryptoApply) {
				userDomain.setPlMZId(CryptoUtil.encrypt(userDomain.getPlMZId().replace("-", "")));
			}else {
				userDomain.setPlMZId(userDomain.getPlMZId().replace("-", ""));
			}
		}
		if(StringUtils.isNotEmpty(userDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				userDomain.setPlMerchantNo(CryptoUtil.encrypt(userDomain.getPlMerchantNo().replace("-", "")));
			}else {
				userDomain.setPlMerchantNo(userDomain.getPlMerchantNo().replace("-", ""));
			}
		}
		int result = userRepo.updateUserRegInfo(userDomain);
		
		//위반이력 등록
		String[] violationCdArr = userDomain.getViolationCdArr();
		
		if(violationCdArr != null && violationCdArr.length > 0) {
			for(int i = 0;i < violationCdArr.length;i++) {
				if(violationCdArr[i] != null && !violationCdArr[i].equals("")) {
					userDomain.setViolationCd(violationCdArr[i]);
					userRepo.insertUserViolationInfo(userDomain);
				}
			}
		}
		
		//결과
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "변경요청이 완료되었습니다.");
		}
		
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//해지요청 
	@Transactional
	public ResponseMsg userDropApply(UserDomain userDomain){
		
		//상세
		UserDomain userRegInfo = userRepo.getUserRegDetail(userDomain);
		
		/*
		if(userRegInfo.getPlClass().equals("2")) { //법인은 하위에 등록된 데이터(법인사용인,임원 등 정보)가 있으면 해지요청 불가
			UserImwonDomain chkParam1 	= new UserImwonDomain();
			UserExpertDomain chkParam2 	= new UserExpertDomain();
			UserItDomain chkParam3 		= new UserItDomain();
			
			//임원
			chkParam1.setMasterSeq(userDomain.getMasterSeq());
			List<UserImwonDomain> imwonList = userRepo.selectUserRegCorpImwonList(chkParam1);
			
			//전문인력
			chkParam2.setMasterSeq(userDomain.getMasterSeq());
			List<UserExpertDomain> expertList = userRepo.selectUserRegCorpExpertList(chkParam2);
			
			//전산인력
			chkParam3.setMasterSeq(userDomain.getMasterSeq());
			List<UserItDomain> itList = userRepo.selectUserRegCorpItList(chkParam3);
			
			if(imwonList.size() > 0 || expertList.size() > 0 || itList.size() > 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", "하위 데이터가 존재하여 해지요청이 불가능 합니다.");
			}
		}
		*/
		
		//기본 이력 저장*****
		this.insertUserHistory(userDomain);
		
		//수정
		int updateResult = this.updateUserStat(userDomain);
		
		if(updateResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "해지요청이 완료되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 모집인 기본 이력 / 단계별 이력 관련
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//모집인 기본 이력 저장
	@Transactional
	public int insertUserHistory(UserDomain userDomain) {
		return userRepo.insertUserHistory(userDomain);
	}
	
	//모집인 단계별 이력 저장
	@Transactional
	public int insertUserStepHistory(UserDomain userDomain) {
		return userRepo.insertUserStepHistory(userDomain);
	}
	
	//모집인 단계별 이력 리스트
	@Transactional(readOnly=true)
	public List<UserDomain> selectUserStepHistoryList(UserDomain userDomain) {
		return userRepo.selectUserStepHistoryList(userDomain);
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * 위반이력
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	//위반이력 삭제 
	@Transactional
	public ResponseMsg deleteViolationInfo(UserDomain userDomain){
		
		int deleteResult = userRepo.deleteUserViolationInfo(userDomain);
		
		if(deleteResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "위반이력이 삭제되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	//위반이력 삭제요청
	@Transactional
	public ResponseMsg applyDeleteViolationInfo(UserDomain userDomain){
		
		int updateResult = userRepo.applyDeleteUserViolationInfo(userDomain);
		
		if(updateResult > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "삭제요청되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", "실패했습니다.");
	}
	
	/* -------------------------------------------------------------------------------------------------------
	 * (공통)모집인 등록 > 상태값 체크
	 * -------------------------------------------------------------------------------------------------------
	 */
	
	public String userRegValidation(int masterSeq) {
		
		String msg 	= "";
		
		//모집인 상세
		UserDomain param 		= new UserDomain();
		param.setMasterSeq(masterSeq);
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(param);
		
		String plRegStat 		= userRegInfo.getPlRegStat(); 	//모집인 상태 	-> [REG001]승인전,승인완료,자격취득,해지완료
		String plStat 			= userRegInfo.getPlStat();		//처리상태 	-> [MAS001]미요청,승인요청,변경요청,해지요청,보완요청(=반려),변경요청(보완),해지요청(보완),취소,완료,등록요건 불충족(=부적격),보완 미이행,등록수수료 미결제
		
		if(plRegStat.equals("1") && plStat.equals("2")) {
			msg = "승인요청중인 모집인입니다.\n새로고침 후 모집인 상태와 처리상태를 확인해 주세요.";
		}else if(plRegStat.equals("2") && plStat.equals("9")) {
			msg = "승인완료된 모집인입니다.\n새로고침 후 모집인 상태와 처리상태를 확인해 주세요.";
		}
		
		return msg;
	}
	
	public String userConfirmValidation(int masterSeq) {
		
		String msg = "";
		
		//모집인 상세
		UserDomain param 		= new UserDomain();
		param.setMasterSeq(masterSeq);
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(param);
		
		String plRegStat 		= userRegInfo.getPlRegStat(); 	//모집인 상태 	-> [REG001]승인전,승인완료,자격취득,해지완료
		String plStat 			= userRegInfo.getPlStat();		//처리상태 	-> [MAS001]미요청,승인요청,변경요청,해지요청,보완요청(=반려),변경요청(보완),해지요청(보완),취소,완료,등록요건 불충족(=부적격),보완 미이행,등록수수료 미결제
		
		if(plRegStat.equals("4")) {
			msg = "해지완료된 모집인입니다.\n새로고침 후 모집인 상태와 처리상태를 확인해 주세요.";
		}else if(plStat.equals("3")) {
			msg = "변경요청중인 모집인입니다.\n새로고침 후 모집인 상태와 처리상태를 확인해 주세요.";
		}else if(plStat.equals("4")) {
			msg = "해지요청중인 모집인입니다.\n새로고침 후 모집인 상태와 처리상태를 확인해 주세요.";
		}
		
		return msg;
	}
	
	public ResponseMsg userValidation(int masterSeq, String type) {
		
		String code = "";
		String msg 	= "";
		
		//모집인 상세
		UserDomain param 	= new UserDomain();
		param.setMasterSeq(masterSeq);
		UserDomain userRegInfo 	= userRepo.getUserRegDetail(param);
		String plRegStat 	= userRegInfo.getPlRegStat();
		String plStat 		= userRegInfo.getPlStat();
		
		if(type.equals("save")) {
			if(!plStat.equals("1") && !plStat.equals("5") && !plStat.equals("6") && !(plRegStat.equals("3") && plStat.equals("9"))) {
				code 	= "E1";
				msg 	= "등록,수정이 불가능한 상태입니다.\n새로고침 후 처리상태를 확인해 주세요.";
			}
		}else if(type.equals("del")) {
			if(!plStat.equals("1") && !plStat.equals("5") && !plStat.equals("16")) {
				code 	= "E1";
				msg 	= "삭제가 불가능한 상태입니다.\n새로고침 후 처리상태를 확인해 주세요.";
			}
		}
		
		return new ResponseMsg(HttpStatus.OK, code, msg);
	}
	
	
	
	
	
	
	
	
	
}
