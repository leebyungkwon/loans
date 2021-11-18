package com.loanscrefia.system.batch.service;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.apply.repository.NewApplyRepository;
import com.loanscrefia.admin.recruit.domain.NewRecruitDomain;
import com.loanscrefia.admin.recruit.repository.NewRecruitRepository;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.common.common.domain.ApiDomain;
import com.loanscrefia.common.common.service.ApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.repository.BatchRepository;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Service
public class BatchService{
	
	@Autowired
	private BatchRepository batchRepository;
	@Autowired
	private ApiService apiService;
	@Autowired
	private NewRecruitRepository recruitRepository;
	
	@Autowired
	private NewApplyRepository newApplyRepository;
	

	@Transactional
	public int recruitReg(BatchDomain req) {
		ApiDomain param = new ApiDomain();
		int cnt = 0;
		//schedule 시작 이력저장
		try {
			//필요정보 호출하여 param 만들기
			
			
			//api 호출
			ResponseMsg result = apiService.excuteApi(param);
			
			
			//데이터 등록
			NewRecruitDomain recruitDomain = new NewRecruitDomain();
			recruitRepository.insertNewMasterStep(recruitDomain);
			if("success".equals(result.getCode())) cnt = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			//schedule 완료 이력저장
			return cnt;
		}
	}

	public List<BatchDomain> selectReqBatchList(BatchDomain param) {
		return batchRepository.selectReqBatchList(param);
	}
	
	
	
	
	
	// 기등록자 insert
	@Transactional
	public int insertBatchPlanInfo(BatchDomain batchDomain) {
		return batchRepository.insertBatchPlanInfo(batchDomain);
	}
	
	
	// 2021-11-09 스케줄명으로 조회
	public List<BatchDomain> selectBatchList(BatchDomain param) {
		return batchRepository.selectBatchList(param);
	}
	
	// 2021-11-11 해지리스트 조회
	public List<NewApplyDomain> selectDropApplyList(BatchDomain param) {
		return batchRepository.selectDropApplyList(param);
	}
	
	
	
	// 2021-11-10 배치 시작 이력 저장
	public int insertScheduleHist(BatchDomain param) {
		return batchRepository.insertScheduleHist(param);
	}
	
	// 2021-11-10 배치 종료 이력 저장
	public void updateScheduleHist(BatchDomain param) {
		batchRepository.updateScheduleHist(param);
	}
	
	
	
	// 2021-11-15 가등록 삭제
	@Transactional
	public int preLoanDel(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain preLoanDelParam = new ApiDomain();
		preLoanDelParam.setMethod("DELETE");
		JSONObject jsonParam = new JSONObject(req.getParam());
		String plClass = req.getProperty01();
		String preLcNum = jsonParam.getString("pre_lc_num");
		String resultParam = "";
		if("1".equals(plClass)) {
			preLoanDelParam.setUrl("/loan/v1/pre-loan-consultants");
			preLoanDelParam.setApiName("preIndvLoanDel");
			resultParam = "pre_lc_num="+preLcNum;
		}else if("2".equals(plClass)) {
			preLoanDelParam.setUrl("/loan/v1/pre-loan-corp-consultants");
			preLoanDelParam.setApiName("preIndvLoanDel");
			resultParam = "pre_corp_lc_num="+preLcNum;
		}else {
			req.setStatus("3");
			req.setError("구분(개인/법인) 파라미터 오류");
			errorMessage = "구분(개인/법인) 파라미터 오류";
			throw new Exception();
		}

		int masterSeq = jsonParam.getInt("master_seq");
		jsonParam.remove("master_seq");
		preLoanDelParam.setParam(resultParam);
		
		NewApplyDomain preDelDomain = new NewApplyDomain();
		preDelDomain.setMasterSeq(masterSeq);
		
		int cnt = 0;
		try {
			ResponseMsg delResult = apiService.excuteApi(preLoanDelParam);
			if("success".equals(delResult.getCode())) {
				preDelDomain.setApiResMsg(delResult.getMessage());
				preDelDomain.setApiSuccessCode(delResult.getCode());
				req.setStatus("2");
				cnt = 1;
			}else {
				// 가등록 삭제 실패
				req.setStatus("3");
				errorMessage = "가등록 삭제시 오류 발생 :: "+delResult.getCode();
				preDelDomain.setApiResMsg(delResult.getMessage());
				preDelDomain.setApiSuccessCode(delResult.getCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			preDelDomain.setApiResMsg(e.getMessage());
			preDelDomain.setApiSuccessCode("fail");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			batchRepository.deletePreLcNum(preDelDomain);
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
	

	// 2021-11-115 가등록
	@Transactional
	public int preloanReg(BatchDomain req) throws Exception {
		// 가등록에 필요한 파라미터
		String errorMessage = "";
		ApiDomain preLoanParam = new ApiDomain();
		preLoanParam.setMethod("POST");
		JSONObject jsonParam = new JSONObject(req.getParam());
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("모집인 계약 seq 파라미터 오류");
			errorMessage = "모집인 계약 seq 파라미터 오류";
			throw new Exception();
		}

		String plClass = req.getProperty01();
		if("1".equals(plClass)) {
			preLoanParam.setUrl("/loan/v1/pre-loan-consultants");
			preLoanParam.setApiName("preIndvLoanReg");

			// 암호화된 데이터 decrypt
			String ssn = jsonParam.getString("ssn");
			jsonParam.remove("ssn");
			jsonParam.put("ssn", CryptoUtil.decrypt(ssn));
			
		}else if("2".equals(plClass)) {
			preLoanParam.setUrl("/loan/v1/pre-loan-corp-consultants");
			preLoanParam.setApiName("preCorpLoanReg");
			
			// 암호화된 데이터 decrypt
			String corpRepSsn = jsonParam.getString("corp_rep_ssn");
			jsonParam.remove("corp_rep_ssn");
			jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(corpRepSsn));
			
			// 암호화된 데이터 decrypt
			String corpNum = jsonParam.getString("corp_num");
			jsonParam.remove("corp_num");
			jsonParam.put("corp_num", CryptoUtil.decrypt(corpNum));
			
		}else {
			req.setStatus("3");
			req.setError("구분(개인/법인) 파라미터 오류");
			errorMessage = "구분(개인/법인) 파라미터 오류";
			throw new Exception();
		}
		
		// master_seq 추출 후 제거 
		jsonParam.remove("master_seq");
		preLoanParam.setParamJson(jsonParam);
		
		int cnt = 0;
		try {
			ResponseMsg preRegResult = apiService.excuteApi(preLoanParam);
			if("success".equals(preRegResult.getCode())) {
				String preLcNum ="";
				String feeYn = "";
				JSONObject preLoanResponseJson = new JSONObject(preRegResult.getData().toString());
				
				// 기등록여부
				if(!preLoanResponseJson.isNull("fee_yn")) {
					feeYn = preLoanResponseJson.getString("fee_yn");
				}
				
				// 개인/법인(가등록번호)
				if("1".equals(plClass)) {
					if(!preLoanResponseJson.isNull("pre_lc_num")) {
						preLcNum = preLoanResponseJson.getString("pre_lc_num");
					}
				}else {
					if(!preLoanResponseJson.isNull("pre_corp_lc_num")) {
						preLcNum = preLoanResponseJson.getString("pre_corp_lc_num");
					}
				}
				
				// 가등록 성공 후 가등록번호 추출에 실패했을 경우
				if(StringUtils.isEmpty(preLcNum)) {
					req.setStatus("3");
					req.setError("가등록번호 생성 실패");
					errorMessage = "가등록번호 생성 실패";
					throw new Exception();
				}
				
				
				// 가등록번호 update
				NewApplyDomain newApplyDomain = new NewApplyDomain();
				newApplyDomain.setPreLcNum(preLcNum);
				newApplyDomain.setMasterSeq(masterSeq);
				newApplyDomain.setPreRegYn(feeYn);
				batchRepository.updatePreLcNum(newApplyDomain);
				req.setStatus("2");
				cnt = 1;
				
				
			}else {
				// 가등록 실패
				req.setStatus("3");
				errorMessage = "가등록번호 생성시 오류 발생 :: "+preRegResult.getCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
	

	
	
	// 2021-11-115 본등록
	@Transactional
	public int loanReg(BatchDomain req) throws Exception {
		// 가등록에 필요한 파라미터
		String errorMessage = "";
		ApiDomain loanParam = new ApiDomain();
		loanParam.setMethod("POST");
		JSONObject jsonParam = new JSONObject(req.getParam());
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("모집인 계약 seq 파라미터 오류");
			errorMessage = "모집인 계약 seq 파라미터 오류";
			throw new Exception();
		}

		String plClass = req.getProperty01();
		if("1".equals(plClass)) {
			loanParam.setUrl("/loan/v1/loan-consultants");
			loanParam.setApiName("indvLoanReg");
		}else {
			loanParam.setUrl("/loan/v1/loan-corp-consultants");
			loanParam.setApiName("corpLoanReg");
		}
		
		// master_seq 추출 후 제거 
		jsonParam.remove("master_seq");
		loanParam.setParamJson(jsonParam);
		
		// 등록번호 update
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		String lcNum ="";
		String conNum = "";
		int cnt = 0;
		try {
			ResponseMsg regResult = apiService.excuteApi(loanParam);
			if("success".equals(regResult.getCode())) {
				
				// 성공시 master_seq로 상품, 회원사코드등 조회
				NewApplyDomain newApplySearchDomain = new NewApplyDomain();
				newApplySearchDomain.setMasterSeq(masterSeq);
				NewApplyDomain applyResult = newApplyRepository.getNewApplyDetail(newApplySearchDomain);
				String comCode = applyResult.getComCode();
				String userLoanType = applyResult.getPlProduct();
				

				JSONObject loanResponseJson = new JSONObject(regResult.getData().toString());
				if("1".equals(plClass)) {
					if(!loanResponseJson.isNull("lc_num")) {
						lcNum = loanResponseJson.getString("lc_num");
					}else {
						req.setStatus("3");
						req.setError("개인등록번호 생성 실패");
						errorMessage = "개인등록번호 생성 실패";
						throw new Exception();
					}
					
				}else {
					if(!loanResponseJson.isNull("corp_lc_num")) {
						lcNum = loanResponseJson.getString("corp_lc_num");
					}else {
						req.setStatus("3");
						req.setError("법인등록번호 생성 실패");
						errorMessage = "법인등록번호 생성 실패";
						throw new Exception();
					}
				}
				
				JSONObject jsonObj = new JSONObject();
				JSONArray conArr = loanResponseJson.getJSONArray("con_arr");
				// 계약금융기관코드(저장되어있는 데이터 비교)
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					String loanType = jsonObj.getString("loan_type");
					String finCode = jsonObj.getString("fin_code");
					// 등록시 계약금융기관코드 및 대출모집인 유형코드(상품코드)가 동일한 정보만 저장(계약일, 대출모집인휴대폰번호 등등 추가가능)
					if(loanType.equals(userLoanType) && finCode.equals(comCode)) {
						conNum = jsonObj.getString("con_num");
						break;
					}
				}
				newApplyDomain.setPlRegistNo(lcNum);
				newApplyDomain.setConNum(conNum);
				newApplyDomain.setApiResMsg(regResult.getMessage());
				newApplyDomain.setApiSuccessCode(regResult.getCode());
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// 본등록 실패
				req.setStatus("3");
				errorMessage = "본등록번호 생성시 오류 발생 :: "+regResult.getCode();
				newApplyDomain.setApiResMsg(regResult.getMessage());
				newApplyDomain.setApiSuccessCode("fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			newApplyDomain.setApiResMsg(e.getMessage());
			newApplyDomain.setApiSuccessCode("fail");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			batchRepository.updateLcNum(newApplyDomain);
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}

	// 2021-11-11 정보수정
	@Transactional
	public int loanUpd(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain loanUpdParam = new ApiDomain();
		loanUpdParam.setMethod("PUT");
		JSONObject jsonParam = new JSONObject(req.getParam());
		
		int userSeq = 0;
		if(!jsonParam.isNull("user_seq")) {
			userSeq = jsonParam.getInt("user_seq");
		}else {
			req.setStatus("3");
			req.setError("모집인 seq 파라미터 오류");
			errorMessage = "모집인 seq 파라미터 오류";
			throw new Exception();
		}
		
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("모집인 계약 seq 파라미터 오류");
			errorMessage = "모집인 계약 seq 파라미터 오류";
			throw new Exception();
		}

		
		// master_seq 추출 후 제거 
		jsonParam.remove("user_seq");
		jsonParam.remove("master_seq");
		loanUpdParam.setParamJson(jsonParam);
		String plClass = req.getProperty01();
		String reqUserSeq = req.getProperty02();
		String reqSeq = req.getProperty03();
		
		
		if("1".equals(plClass)) {
			loanUpdParam.setUrl("/loan/v1/loan-consultants");
			loanUpdParam.setApiName("indvLoanUpd");
		}else if("2".equals(plClass)) {
			loanUpdParam.setUrl("/loan/v1/loan-corp-consultants");
			loanUpdParam.setApiName("corpLoanUpd");
			
			// 암호화된 데이터 decrypt
			String ssn = jsonParam.getString("corp_rep_ssn");
			jsonParam.remove("corp_rep_ssn");
			jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(ssn));
			
		}else {
			req.setStatus("3");
			req.setError("구분(개인/법인) 파라미터 오류");
			errorMessage = "구분(개인/법인) 파라미터 오류";
			throw new Exception();
		}
		
		int cnt = 0;
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		
		try {
			ResponseMsg updResult = apiService.excuteApi(loanUpdParam);
			if("success".equals(updResult.getCode())) {
				// 계약건별 수정된 데이터 추가
				if("1".equals(plClass)) {
					newApplyDomain.setPlMName(jsonParam.getString("name"));
					// 연락처, 계약일 등 JSON배열
					JSONObject jsonObj = new JSONObject();
					JSONArray conArr = jsonParam.getJSONArray("con_arr");
					for(int i=0; i<conArr.length(); i++){
						jsonObj = conArr.getJSONObject(i);
						newApplyDomain.setPlCellphone(jsonObj.getString("con_mobile").replaceAll("-", ""));
					}
					
					// 개인회원 회원정보 수정
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setUserName(jsonParam.getString("name"));
					usersDomain.setMobileNo(jsonObj.getString("con_mobile").replaceAll("-", ""));
					batchRepository.updateIndvUsersInfo(usersDomain);
					
					// 개인정보 수정 후 계약테이블MAS01 정보 수정
					batchRepository.updateIndvMasInfo(newApplyDomain);
					
				}else {
					newApplyDomain.setPlMerchantName(jsonParam.getString("corp_name"));
					newApplyDomain.setPlCeoName(jsonParam.getString("corp_rep_name"));
					newApplyDomain.setPlMZId(CryptoUtil.encrypt(jsonParam.getString("corp_rep_ssn")));
					newApplyDomain.setCi(jsonParam.getString("corp_rep_ci"));
					
					// 법인회원 회원정보 수정
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setUserName(jsonParam.getString("corp_rep_name"));
					usersDomain.setPlMZId(CryptoUtil.encrypt(jsonParam.getString("corp_rep_ssn")));
					usersDomain.setUserCi(jsonParam.getString("corp_rep_ci"));
					
					// 법인회원 연락처 수정 확인
					//usersDomain.setMobileNo();
					batchRepository.updateCorpUsersInfo(usersDomain);
					
					// 법인명 수정
					usersDomain.setPlMerchantName(jsonParam.getString("corp_rep_name"));
					batchRepository.updateCorpInfo(usersDomain);
					
				}
				
				newApplyDomain.setApiResMsg(updResult.getMessage());
				newApplyDomain.setApiSuccessCode(updResult.getCode());
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// 통신오류
				req.setStatus("3");
				errorMessage = "정보수정 오류 발생 :: "+updResult.getCode();
				newApplyDomain.setApiResMsg(updResult.getMessage());
				newApplyDomain.setApiSuccessCode("fail");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			
			newApplyDomain.setApiResMsg(e.getMessage());
			newApplyDomain.setApiSuccessCode("fail");
			
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			if("1".equals(plClass)) {
				batchRepository.updateIndvMasInfo(newApplyDomain);
			}else {
				batchRepository.updateCorpMasInfo(newApplyDomain);
			}
			
			BatchDomain reqBatch = new BatchDomain();
			reqBatch.setScheduleName("loanUpd");
			reqBatch.setProperty02(reqUserSeq);
			
			// 스케쥴테이블에서 해당 seq가 마지막인 경우 req테이블 상태 변경
			int reqCnt = batchRepository.selectReqCnt(reqBatch);
			if(reqCnt == 0) {
				UsersDomain reqResult = new UsersDomain();
				if("1".equals(plClass)) {
					reqResult.setUserIndvReqSeq(Integer.parseInt(reqSeq));
					batchRepository.updateIndvReq(reqResult);
				}else {
					reqResult.setUserCorpReqSeq(Integer.parseInt(reqSeq));
					batchRepository.updateCorpReq(reqResult);
				}
			}
			
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
	
	
	
	
	// 2021-11-11 건별정보수정
	@Transactional
	public int caseLoanUpd(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain loanUpdParam = new ApiDomain();
		loanUpdParam.setMethod("PUT");
		JSONObject jsonParam = new JSONObject(req.getParam());
		
		int userSeq = 0;
		if(!jsonParam.isNull("user_seq")) {
			userSeq = jsonParam.getInt("user_seq");
		}else {
			req.setStatus("3");
			req.setError("모집인 seq 파라미터 오류");
			errorMessage = "모집인 seq 파라미터 오류";
			throw new Exception();
		}
		
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("모집인 계약 seq 파라미터 오류");
			errorMessage = "모집인 계약 seq 파라미터 오류";
			throw new Exception();
		}
		
		// master_seq 추출 후 제거 
		jsonParam.remove("user_seq");
		jsonParam.remove("master_seq");
		loanUpdParam.setParamJson(jsonParam);
		String plClass = req.getProperty01();
		if("1".equals(plClass)) {
			loanUpdParam.setUrl("/loan/v1/loan-consultants");
			loanUpdParam.setApiName("indvCaseLoanUpd");
		}else if("2".equals(plClass)) {
			loanUpdParam.setUrl("/loan/v1/loan-corp-consultants");
			loanUpdParam.setApiName("corpCaseLoanUpd");
			
			// 암호화된 데이터 decrypt
			String ssn = jsonParam.getString("corp_rep_ssn");
			jsonParam.remove("corp_rep_ssn");
			jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(ssn));
			
		}else {
			req.setStatus("3");
			req.setError("구분(개인/법인) 파라미터 오류");
			errorMessage = "구분(개인/법인) 파라미터 오류";
			throw new Exception();
		}
		
		int cnt = 0;
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		
		try {
			ResponseMsg updResult = apiService.excuteApi(loanUpdParam);
			if("success".equals(updResult.getCode())) {
				// 계약건별 수정된 데이터 추가
				if("1".equals(plClass)) {
					newApplyDomain.setPlMName(jsonParam.getString("name"));
					// 연락처, 계약일 등 JSON배열
					JSONObject jsonObj = new JSONObject();
					JSONArray conArr = jsonParam.getJSONArray("con_arr");
					for(int i=0; i<conArr.length(); i++){
						jsonObj = conArr.getJSONObject(i);
						newApplyDomain.setPlCellphone(jsonObj.getString("con_mobile").replaceAll("-", ""));
					}
					
					// 개인회원 회원정보 수정
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setUserName(jsonParam.getString("name"));
					usersDomain.setMobileNo(jsonObj.getString("con_mobile").replaceAll("-", ""));
					batchRepository.updateIndvUsersInfo(usersDomain);
					
				}else {
					newApplyDomain.setPlMerchantName(jsonParam.getString("corp_name"));
					newApplyDomain.setPlCeoName(jsonParam.getString("corp_rep_name"));
					newApplyDomain.setPlMZId(CryptoUtil.encrypt(jsonParam.getString("corp_rep_ssn")));
					newApplyDomain.setCi(jsonParam.getString("corp_rep_ci"));
					
					// 법인회원 회원정보 수정
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setUserName(jsonParam.getString("corp_rep_name"));
					usersDomain.setPlMZId(CryptoUtil.encrypt(jsonParam.getString("corp_rep_ssn")));
					usersDomain.setUserCi(jsonParam.getString("corp_rep_ci"));
					
					// 법인회원 연락처 수정 확인
					//usersDomain.setMobileNo();
					batchRepository.updateCorpUsersInfo(usersDomain);
					
					// 법인명 수정
					//usersDomain.setPlMerchantName(jsonParam.getString("corp_rep_name").toString());
					//batchRepository.updateCorpInfo(usersDomain);
					
				}
				
				newApplyDomain.setApiResMsg(updResult.getMessage());
				newApplyDomain.setApiSuccessCode(updResult.getCode());
				req.setStatus("2");
				cnt = 1;
				
			}else {
				
				// 통신오류
				req.setStatus("3");
				errorMessage = "정보수정API 오류 발생 :: "+updResult.getCode();
				newApplyDomain.setApiResMsg(updResult.getMessage());
				newApplyDomain.setApiSuccessCode("fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			newApplyDomain.setApiResMsg(e.getMessage());
			newApplyDomain.setApiSuccessCode("fail");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			if("1".equals(plClass)) {
				batchRepository.updateCaseIndvMasInfo(newApplyDomain);
			}else {
				batchRepository.updateCaseCorpMasInfo(newApplyDomain);
			}
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
	
	
	
	
	
	
	
	// 2021-11-11 해지
	@Transactional
	public int dropApply(NewApplyDomain drop, BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain dropParam = new ApiDomain();
		dropParam.setMethod("PUT");
		
		JSONObject jsonParam = new JSONObject();
		JSONObject jsonArrayParam = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		String plClass = drop.getPlClass();
		
		if("1".equals(plClass)) {
			// 개인
			jsonParam.put("lc_num", drop.getPlRegistNo());
			jsonParam.put("name", drop.getPlMName());
			
			// 배열
			jsonArrayParam.put("con_num", drop.getConNum());
			jsonArrayParam.put("con_date", drop.getComContDate().replaceAll("-", ""));
			jsonArrayParam.put("con_mobile", drop.getPlCellphone().replaceAll("-", ""));
			jsonArrayParam.put("fin_phone", "");
			jsonArrayParam.put("loan_type", drop.getPlProduct());
			jsonArrayParam.put("cancel_date", drop.getCreHaejiDate().replaceAll("-", ""));
			jsonArrayParam.put("cancel_code", drop.getPlHistCd());
			jsonArray.put(jsonArrayParam);
			jsonParam.put("con_arr", jsonArray);
			
		}else {
			// 법인
			jsonParam.put("corp_lc_num", drop.getPlRegistNo());						// 등록번호
			jsonParam.put("corp_name", drop.getPlMerchantName());					// 법인명
			jsonParam.put("corp_rep_name", drop.getPlCeoName());					// 법인대표명
			jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(drop.getPlMZId()));	// 법인대표주민번호
			jsonParam.put("corp_rep_ci", drop.getCi());								// 법인대표CI
			
			// 배열
			jsonArrayParam.put("con_num", drop.getConNum());
			jsonArrayParam.put("con_date", drop.getComContDate().replaceAll("-", ""));
			jsonArrayParam.put("fin_phone", "");
			jsonArrayParam.put("loan_type", drop.getPlProduct());
			jsonArrayParam.put("cancel_date", drop.getCreHaejiDate().replaceAll("-", ""));
			jsonArrayParam.put("cancel_code", drop.getPlHistCd());
			jsonArray.put(jsonArrayParam);
			jsonParam.put("con_arr", jsonArray);
			
		}
		dropParam.setParamJson(jsonParam);
		if("1".equals(plClass)) {
			dropParam.setUrl("/loan/v1/loan-consultants");
			dropParam.setApiName("indvDropApply");
		}else if("2".equals(plClass)) {
			dropParam.setUrl("/loan/v1/loan-corp-consultants");
			dropParam.setApiName("corpDropApply");
		}else {
			req.setStatus("3");
			req.setError("구분(개인/법인) 파라미터 오류");
			errorMessage = "구분(개인/법인) 파라미터 오류";
			throw new Exception();
		}
		int cnt = 0;
		try {
			ResponseMsg dropResult = apiService.excuteApi(dropParam);
			if("success".equals(dropResult.getCode())) {
				// 해지완료 후 상태변경
				drop.setApiResMsg(dropResult.getMessage());
				drop.setApiSuccessCode(dropResult.getCode());
				req.setStatus("2");
				cnt = 1;
			}else {
				// 해지API발송 실패
				req.setStatus("3");
				errorMessage = "해지완료 오류 발생 :: "+dropResult.getCode();
				drop.setApiResMsg(dropResult.getMessage());
				drop.setApiSuccessCode("fail");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			drop.setApiResMsg(e.getMessage());
			drop.setApiSuccessCode("fail");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			batchRepository.updateDropApply(drop);
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
	
	
	// 2021-11-11 위반이력 등록
	@Transactional
	public int violationReg(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain vioRegParam = new ApiDomain();
		vioRegParam.setMethod("POST");
		JSONObject jsonParam = new JSONObject(req.getParam());
		vioRegParam.setUrl("/loan/v1/violation-consultants");
		vioRegParam.setApiName("violationReg");

		// 암호화된 데이터 decrypt
		String ssn = jsonParam.getString("ssn").toString();
		jsonParam.remove("ssn");
		jsonParam.put("ssn", CryptoUtil.decrypt(ssn));
		
		// 위반이력 시퀀스
		String vioSeq = jsonParam.getString("vio_seq").toString();
		// vio_seq 추출 후 제거 
		jsonParam.remove("vio_seq");
		vioRegParam.setParamJson(jsonParam);
		
		int cnt = 0;
		try {
			ResponseMsg vioResult = apiService.excuteApi(vioRegParam);
			if("success".equals(vioResult.getCode())) {
				JSONObject vioResponseJson = new JSONObject(vioResult.getData().toString());
				String apiVioNum = "";
				if(!vioResponseJson.isNull("vio_num")) {
					apiVioNum = vioResponseJson.getString("vio_num");
				}
				
				//위반이력
				NewUserDomain vioRegDomain = new NewUserDomain();
				vioRegDomain.setVioNum(apiVioNum);
				vioRegDomain.setViolationSeq(Integer.parseInt(vioSeq));
				batchRepository.updateUserViolationInfo(vioRegDomain);
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// 위반이력 등록 실패
				req.setStatus("3");
				errorMessage = "위반이력 등록시 오류 발생 :: "+vioResult.getCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}
	
	
	
	

	// 2021-11-12 위반이력 삭제
	@Transactional
	public int violationDel(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain vioDelParam = new ApiDomain();
		vioDelParam.setMethod("DELETE");
		JSONObject jsonParam = new JSONObject(req.getParam());
		vioDelParam.setUrl("/loan/v1/violation-consultants");
		vioDelParam.setApiName("violationDel");

		// 위반이력 시퀀스
		String vioSeq = jsonParam.getString("vio_seq");
		// vio_seq 추출 후 제거 
		jsonParam.remove("vio_seq");
		
		// DELETE param
		String vioNum = jsonParam.getString("vio_num");
		String vioNumUrl = "vio_num="+vioNum;
		vioDelParam.setParam(vioNumUrl);
		//vioDelParam.setParamJson(jsonParam);
		
		int cnt = 0;
		try {
			ResponseMsg vioResult = apiService.excuteApi(vioDelParam);
			if("success".equals(vioResult.getCode())) {
				JSONObject vioResponseJson = new JSONObject(vioResult.getData().toString());
				String apiVioNum = "";
				if(!vioResponseJson.isNull("vio_num")) {
					apiVioNum = vioResponseJson.getString("vio_num");
				}
				
				//위반이력
				NewUserDomain vioRegDomain = new NewUserDomain();
				vioRegDomain.setVioNum(apiVioNum);
				vioRegDomain.setViolationSeq(Integer.parseInt(vioSeq));
				batchRepository.deleteUserViolationInfo(vioRegDomain);
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// 위반이력 삭제 실패
				req.setStatus("3");
				errorMessage = "위반이력 삭제시 오류 발생 :: "+vioResult.getCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
			
		} finally {
			batchRepository.updateSchedule(req);
			return cnt;
		}
	}

	public List<BatchDomain> selectAllBatchList() {
		return batchRepository.selectAllBatchList();
	}

	public List<BatchDomain> selectBatchErrList(BatchDomain batch) {
		return batchRepository.selectBatchErrList(batch);
	}

	public List<BatchDomain> selectBatchErrHistList(BatchDomain batch) {
		return batchRepository.selectBatchErrHistList(batch);
	}
	
	
	@Transactional
	public ResponseMsg refreshBatch(BatchDomain batch){
		int result = batchRepository.refreshBatch(batch);
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", "완료되었습니다.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "오류가 발생하였습니다.");
		}
	}
	
}
