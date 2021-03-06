package com.loanscrefia.system.batch.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.apply.domain.ApplyExpertDomain;
import com.loanscrefia.admin.apply.domain.ApplyImwonDomain;
import com.loanscrefia.admin.apply.domain.ApplyItDomain;
import com.loanscrefia.admin.apply.domain.NewApplyDomain;
import com.loanscrefia.admin.apply.repository.NewApplyRepository;
import com.loanscrefia.admin.recruit.domain.NewRecruitDomain;
import com.loanscrefia.admin.recruit.repository.NewRecruitRepository;
import com.loanscrefia.admin.stats.domain.StatsDomain;
import com.loanscrefia.admin.users.domain.UsersDomain;
import com.loanscrefia.common.common.domain.ApiDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.common.common.service.ApiService;
import com.loanscrefia.common.common.service.CommonService;
import com.loanscrefia.common.common.sms.domain.SmsDomain;
import com.loanscrefia.common.common.sms.repository.SmsRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.NewUserDomain;
import com.loanscrefia.system.batch.domain.BatchDomain;
import com.loanscrefia.system.batch.repository.BatchRepository;

import lombok.extern.slf4j.Slf4j;
import sinsiway.CryptoUtil;

@Slf4j
@Service
public class BatchService{
	
	@Autowired private BatchRepository batchRepository;
	@Autowired private ApiService apiService;
	@Autowired private NewRecruitRepository recruitRepository;
	@Autowired private NewApplyRepository newApplyRepository;
	@Autowired private SmsRepository smsRepository;
	@Autowired private CommonService commonService;
	
	//SMS 적용여부
	@Value("${sms.apply}")
	public boolean smsApply;
	
	//정보 삭제 관련 - 해지 처리기준
	@Value("${expired.regCancel}")
	public int regCancel;
	
	//정보 삭제 관련 - 미요청 처리기준
	@Value("${expired.notApply}")
	public int notApply;
	
	//정보 삭제 관련 - 보완 미이행 처리기준
	@Value("${expired.notApplyAgain}")
	public int notApplyAgain;
	
	//정보 삭제 관련 - 취소 처리기준
	@Value("${expired.cancel}")
	public int cancel;
		
	//정보 삭제 관련 - 금융회사 승인거절 처리기준
	@Value("${expired.reject}")
	public int reject;
		
	//정보 삭제 관련 - 등록요건 불충족(부적격) 처리기준
	@Value("${expired.inaq}")
	public int inaq;
		
	
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
	
	// 2021-11-24 금일 토큰 생성 확인
	public int getTokenCheck(BatchDomain param) {
		return batchRepository.getTokenCheck(param);
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
				
				req.setError("code ::"+delResult.getCode()+" Message :: "+delResult.getMessage());
				
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
				req.setError("code ::"+preRegResult.getCode()+" Message :: "+preRegResult.getMessage());
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
				
				
				// 본등록에 대한 메세지 발송
				int smsResult = 0;
				SmsDomain smsDomain = new SmsDomain();
				smsDomain.setTranCallback("0220110770");
				smsDomain.setTranStatus("1");
				smsDomain.setTranEtc1("10070");
				smsDomain.setTranPhone(applyResult.getPlCellphone().replaceAll("-", ""));
				String tranMsg = "";
				
				if("1".equals(plClass)) {
					tranMsg = applyResult.getUserName()+"님의 대출성상품 모집인 등록이 완료되었습니다. 등록번호는 "+lcNum+" 입니다.";
				}else {
					tranMsg = applyResult.getPlMerchantName()+" 법인의 대출성상품 모집인 등록이 완료되었습니다. 등록번호는 "+lcNum+" 입니다.";
				}
				
				smsDomain.setTranMsg(tranMsg);
				smsResult = smsRepository.sendSms(smsDomain);
				
			}else {
				// 본등록 실패
				req.setStatus("3");
				errorMessage = "본등록번호 생성시 오류 발생 :: "+regResult.getCode();
				newApplyDomain.setApiResMsg(regResult.getMessage());
				newApplyDomain.setApiSuccessCode("fail");
				
				req.setError("code ::"+regResult.getCode()+" Message :: "+regResult.getMessage());
				
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
		
		String plClass = req.getProperty01();
		String reqUserSeq = req.getProperty02();
		String reqSeq = req.getProperty03();
		String mobileNo = "";
		
		if("1".equals(plClass)) {
			loanUpdParam.setUrl("/loan/v1/loan-consultants");
			loanUpdParam.setApiName("indvLoanUpd");
		}else if("2".equals(plClass)) {
			loanUpdParam.setUrl("/loan/v1/loan-corp-consultants");
			loanUpdParam.setApiName("corpLoanUpd");
			mobileNo = jsonParam.getString("mobile_no");
			// 법인인 경우 연락처 삭제
			jsonParam.remove("mobile_no");
			
		}else {
			req.setStatus("3");
			req.setError("구분(개인/법인) 파라미터 오류");
			errorMessage = "구분(개인/법인) 파라미터 오류";
			throw new Exception();
		}
		
		loanUpdParam.setParamJson(jsonParam);
		
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
					
					// 법인회원 회원정보 수정
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setMobileNo(mobileNo);
					usersDomain.setUserName(jsonParam.getString("corp_rep_name"));
					
					// 법인회원 연락처 수정 확인
					//usersDomain.setMobileNo();
					batchRepository.updateCorpUsersInfo(usersDomain);
					
					// 법인명 수정
					usersDomain.setPlMerchantName(jsonParam.getString("corp_name"));
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
				
				req.setError("code ::"+updResult.getCode()+" Message :: "+updResult.getMessage());
				
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
			batchRepository.updateSchedule(req);
			userInfoUpdResult(plClass, newApplyDomain, req);
			return cnt;
		}
	}
	
	
	// 2021-11-11 주민등록번호 변경
	@Transactional
	public int loanSsnUpd(BatchDomain req) throws Exception {
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

		// master_seq 추출 후 제거 
		jsonParam.remove("user_seq");
		
		String plClass = req.getProperty01();
		String reqUserSeq = req.getProperty02();
		String reqSeq = req.getProperty03();
		
		loanUpdParam.setUrl("/loan/v1/mod-ssn");
		loanUpdParam.setApiName("loanSsnUpd");
		
		String befSsn = jsonParam.getString("bef_ssn");
		String aftSsn = jsonParam.getString("aft_ssn");
		String aftCi = jsonParam.getString("aft_ci");
		jsonParam.remove("bef_ssn");
		jsonParam.remove("aft_ssn");
		jsonParam.put("bef_ssn", CryptoUtil.decrypt(befSsn));
		jsonParam.put("aft_ssn", CryptoUtil.decrypt(aftSsn));
		
		loanUpdParam.setParamJson(jsonParam);
		
		int cnt = 0;
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setUserSeq(userSeq);
		newApplyDomain.setPlClass(plClass);
		
		try {
			ResponseMsg updResult = apiService.excuteApi(loanUpdParam);
			if("success".equals(updResult.getCode())) {
				
				// 주민번호 + ci 정보 수정
				UsersDomain usersDomain = new UsersDomain();
				usersDomain.setUserSeq(userSeq);
				usersDomain.setPlMZId(aftSsn);
				usersDomain.setUserCi(aftCi);
				batchRepository.updateUsersSsnInfo(usersDomain);
				
				// 주민번호 + ci 정보 수정 후 계약건 전체 수정
				
				newApplyDomain.setCi(aftCi);
				newApplyDomain.setPlMZId(aftSsn);
				batchRepository.updateMasSsnInfo(newApplyDomain);
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
				
				req.setError("code ::"+updResult.getCode()+" Message :: "+updResult.getMessage());
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
			batchRepository.updateSchedule(req);
			userInfoUpdResult(plClass, newApplyDomain, req);
			return cnt;
		}
	}
	
	
	// 정보변경 및 주민등록번호 수정 완료시 상태값 변경
	public void userInfoUpdResult(String plClass, NewApplyDomain newApplyDomain, BatchDomain req) {
		// 스케쥴테이블에서 해당 seq가 마지막인 경우 req테이블 상태 변경
		int reqCnt = batchRepository.selectReqSsnInfoCnt(req);
		String reqSeq = req.getProperty03();
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
				
				req.setError("code ::"+updResult.getCode()+" Message :: "+updResult.getMessage());
				
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
	public int dropApply(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain dropParam = new ApiDomain();
		dropParam.setMethod("PUT");
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

		// master_seq 추출 후 제거 
		jsonParam.remove("master_seq");
		
		String plClass = req.getProperty01();
		
		if("1".equals(plClass)) {
			dropParam.setUrl("/loan/v1/loan-consultants");
			dropParam.setApiName("indvDrop");
		}else if("2".equals(plClass)) {
			dropParam.setUrl("/loan/v1/loan-corp-consultants");
			dropParam.setApiName("corpDrop");
		}else {
			req.setStatus("3");
			req.setError("구분(개인/법인) 파라미터 오류");
			errorMessage = "구분(개인/법인) 파라미터 오류";
			throw new Exception();
		}
		
		dropParam.setParamJson(jsonParam);
		
		int cnt = 0;
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		
		try {
			ResponseMsg updResult = apiService.excuteApi(dropParam);
			if("success".equals(updResult.getCode())) {
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
				
				req.setError("code ::"+updResult.getCode()+" Message :: "+updResult.getMessage());
				
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
			batchRepository.updateDropMasInfo(newApplyDomain);
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
		String ssn = jsonParam.getString("ssn");
		jsonParam.remove("ssn");
		jsonParam.put("ssn", CryptoUtil.decrypt(ssn));
		
		// 위반이력 시퀀스
		String vioSeq = jsonParam.getString("vio_seq");
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
				
				req.setError("code ::"+vioResult.getCode()+" Message :: "+vioResult.getMessage());
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
				
				req.setError("code ::"+vioResult.getCode()+" Message :: "+vioResult.getMessage());
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
	
	public List<BatchDomain> selectBatchListLimited(BatchDomain batch) {
		return batchRepository.selectBatchListLimited(batch);
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
	
	
	
	//2021-12-23 승인완료된 건들 중 기등록 계약건 조회
	@Transactional
	public int preRegContSearch(BatchDomain req) throws Exception {
		
		JSONObject jsonParam 	= new JSONObject(req.getParam());
		String plClass 			= req.getProperty01();
		
		String param 	= "";
		int masterSeq 	= jsonParam.getInt("master_seq");
		
		ApiDomain apiDomain = new ApiDomain();
		apiDomain.setMethod("GET");
		
		if("1".equals(plClass)) {
			param = "pre_lc_num="+jsonParam.getString("pre_lc_num");
			apiDomain.setApiName("searchPreLoan");
			apiDomain.setUrl("/loan/v1/pre-loan-consultants");
			apiDomain.setParam(param);
		}else {
			param = "pre_corp_lc_num="+jsonParam.getString("pre_corp_lc_num");
			apiDomain.setApiName("searchPreLoanCorp");
			apiDomain.setUrl("/loan/v1/pre-loan-corp-consultants");
			apiDomain.setParam(param);
		}
		
		int cnt = 0;
		
		try {
			//가등록 조회
			ResponseMsg responseMsg = apiService.excuteApi(apiDomain);
			
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				
				if(!responseJson.isNull("fee_yn")) {
					//기등록자인 경우
					if("Y".equals(responseJson.getString("fee_yn"))) {
						//(1)본등록 배치 저장
						SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
						Date currentDt = new Date();
						String today = dateFormatParser.format(currentDt);
						
						BatchDomain batchDomain = new BatchDomain();
						
						batchDomain.setScheduleName("loanReg");
						batchDomain.setParam(jsonParam.toString());
						batchDomain.setProperty01(plClass);
						batchDomain.setProperty05("direct_"+today);
						
						this.insertBatchPlanInfo(batchDomain);
						
						//(2)관련 승인전 계약건 pre_reg_yn 수정
						NewApplyDomain newApplyDomain = new NewApplyDomain();
						newApplyDomain.setMasterSeq(masterSeq);
						
						if("1".equals(plClass)){
							batchRepository.updatePreRegYnIndvCont(newApplyDomain);
						}else {
							batchRepository.updatePreRegYnCorpCont(newApplyDomain);
						}
					}
				}
				
				req.setStatus("2");
				cnt = 1;
				
			}else {
				//조회 실패
				req.setStatus("3");
				req.setError("code ::"+responseMsg.getCode()+" Message :: "+responseMsg.getMessage());
			}
		}catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			req.setError("preRegContSearch() 배치 오류 :: catch문");
		}finally {
			batchRepository.updateSchedule(req);
		}
		return cnt;
	}
	
	/* -------------------------------------------------------------------------------------------------------------------
	 * 2021-12-07 정보 삭제 관련
	 * tb_lc_file01 / tb_lc_file_check
	 * tb_lc_mas01 / tb_lc_mas01_hist / tb_lc_mas01_step / tb_lc_product_detail
	 * tb_lc_mas01_imwon / tb_lc_mas01_expert / tb_lc_mas01_it : 법인 모집인일 때만
	 * interval은 application.yml에 선언되어 있음
	 * -------------------------------------------------------------------------------------------------------------------
	 */
	
	//2021-12-07 해지정보 삭제(해지일로부터 5년)
	@Transactional
	public void regCancelInfoDel() throws Exception {
		//해지정보 리스트
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(regCancel);
		List<NewApplyDomain> masList = batchRepository.selectRegCancelInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-07 미요청 정보 삭제(최종 저장일로부터 60일)
	@Transactional
	public void notApplyInfoDel() throws Exception {
		//미요청 정보 리스트
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(notApply);
		List<NewApplyDomain> masList = batchRepository.selectNotApplyInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				if(masList.get(i).getRegPath().equals("B")) {
					//AS-IS
					if(StringUtils.isEmpty(masList.get(i).getPreLcNum())) {
						this.masterAndRelatedInfoDel(masList.get(i));
					}else {
						BatchDomain batchDomain = new BatchDomain();
						JSONObject jsonParam 	= new JSONObject();
						
						jsonParam.put("master_seq", masList.get(i).getMasterSeq());
						jsonParam.put("pre_lc_num", masList.get(i).getPreLcNum());
						
						batchDomain.setScheduleName("notApplyPreLoanDel");
						batchDomain.setParam(jsonParam.toString());
						
						if("1".equals(masList.get(i).getPlClass())) {
							batchDomain.setProperty01("1");
						}else {
							batchDomain.setProperty01("2");
						}
						
						this.insertBatchPlanInfo(batchDomain);
					}
				}else {
					//TO-BE는 바로 삭제
					this.masterAndRelatedInfoDel(masList.get(i));
				}
			}
		}
	}
	
	//2021-12-07 보완 미이행 정보 삭제(최종 보완요청일로부터 60일)
	@Transactional
	public void notApplyAgainInfoDel() throws Exception {
		//미요청 정보 리스트
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(notApplyAgain);
		List<NewApplyDomain> masList = batchRepository.selectNotApplyAgainInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				if(masList.get(i).getRegPath().equals("B")) {
					//AS-IS
					if(StringUtils.isEmpty(masList.get(i).getPreLcNum())) {
						this.masterAndRelatedInfoDel(masList.get(i));
					}else {
						BatchDomain batchDomain = new BatchDomain();
						JSONObject jsonParam 	= new JSONObject();
						
						jsonParam.put("master_seq", masList.get(i).getMasterSeq());
						jsonParam.put("pre_lc_num", masList.get(i).getPreLcNum());
						
						batchDomain.setScheduleName("notApplyPreLoanDel");
						batchDomain.setParam(jsonParam.toString());
						
						if("1".equals(masList.get(i).getPlClass())) {
							batchDomain.setProperty01("1");
						}else {
							batchDomain.setProperty01("2");
						}
						
						this.insertBatchPlanInfo(batchDomain);
					}
				}else {
					//TO-BE는 바로 삭제
					this.masterAndRelatedInfoDel(masList.get(i));
				}
			}
		}
	}
	
	//2021-12-07 취소 정보 삭제(취소일로부터 60일)
	@Transactional
	public void cancelInfoDel() throws Exception {
		//취소 정보 리스트
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(cancel);
		List<NewApplyDomain> masList = batchRepository.selectCancelInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-07 금융회사 승인거절 정보 삭제(승인거절일로부터 60일)
	@Transactional
	public void rejectInfoDel() throws Exception {
		//금융회사 승인거절 정보 리스트
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(reject);
		List<NewApplyDomain> masList = batchRepository.selectRejectInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-23 등록요건 불충족(부적격) 정보 삭제(부적격처리일로부터 60일)
	@Transactional
	public void inaqInfoDel() throws Exception {
		//등록요건 불충족(부적격) 정보 리스트
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(inaq);
		List<NewApplyDomain> masList = batchRepository.selectInaqInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-07 정보 삭제 공통
	@Transactional
	public void masterAndRelatedInfoDel(NewApplyDomain newApplyDomain) {
		
		int masterDelResult = batchRepository.realDeleteMasInfo(newApplyDomain);
		
		//그 외
		if(masterDelResult > 0) {
			batchRepository.realDeleteMasHistInfo(newApplyDomain);
			batchRepository.realDeleteMasStepInfo(newApplyDomain);
			batchRepository.realDeletePrdDtlInfo(newApplyDomain);
			
			FileDomain fileDomain = new FileDomain();
			fileDomain.setFileGrpSeq(newApplyDomain.getFileSeq());
			commonService.realDeleteFileByGrpSeq(fileDomain);
			
			//법인일 때 하위데이터도 삭제
			if(newApplyDomain.getPlClass().equals("2")) {
				//임원 리스트
				ApplyImwonDomain imwonDoamin = new ApplyImwonDomain();
				imwonDoamin.setMasterSeq(newApplyDomain.getMasterSeq());
				List<ApplyImwonDomain> imwonList = batchRepository.selectCorpImwonDelInfoList(imwonDoamin);
				
				if(imwonList.size() > 0) {
					int imwonDelResult = batchRepository.realDeleteCorpImwonInfo(imwonDoamin);
					
					if(imwonDelResult > 0) {
						for(int i = 0;i < imwonList.size();i++) {
							if(imwonList.get(i).getFileSeq() != null) {
								FileDomain imwonFileDomain = new FileDomain();
								imwonFileDomain.setFileGrpSeq(imwonList.get(i).getFileSeq());
								commonService.realDeleteFileByGrpSeq(imwonFileDomain);
							}
						}
					}
				}
				
				//업무수행인력 리스트
				ApplyExpertDomain expertDomain = new ApplyExpertDomain();
				expertDomain.setMasterSeq(newApplyDomain.getMasterSeq());
				List<ApplyExpertDomain> expertList = batchRepository.selectCorpExpertDelInfoList(expertDomain);
				
				if(expertList.size() > 0) {
					int expertDelResult = batchRepository.realDeleteCorpExpertInfo(expertDomain);
					
					if(expertDelResult > 0) {
						for(int i = 0;i < expertList.size();i++) {
							if(expertList.get(i).getFileSeq() != null) {
								FileDomain expertFileDomain = new FileDomain();
								expertFileDomain.setFileGrpSeq(expertList.get(i).getFileSeq());
								commonService.realDeleteFileByGrpSeq(expertFileDomain);
							}
						}
					}
				}
				
				//전산인력 리스트
				ApplyItDomain itDomain = new ApplyItDomain();
				itDomain.setMasterSeq(newApplyDomain.getMasterSeq());
				List<ApplyItDomain> itList = batchRepository.selectCorpItDelInfoList(itDomain);
				
				if(itList.size() > 0) {
					int itDelResult = batchRepository.realDeleteCorpItInfo(itDomain);
					
					if(itDelResult > 0) {
						for(int i = 0;i < itList.size();i++) {
							if(itList.get(i).getFileSeq() != null) {
								FileDomain itFileDomain = new FileDomain();
								itFileDomain.setFileGrpSeq(itList.get(i).getFileSeq());
								commonService.realDeleteFileByGrpSeq(itFileDomain);
							}
						}
					}
				}
			}
		}
	}
	
	//2021-12-07 미요청 또는 보완 미이행 건들 삭제 시 가등록 삭제
	@Transactional
	public int notApplyPreLoanDel(BatchDomain req) throws Exception {
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
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		
		int cnt = 0;
		
		try {
			ResponseMsg delResult = apiService.excuteApi(preLoanDelParam);
			
			if("success".equals(delResult.getCode())) {
				//가등록 삭제 성공 : 정보 삭제
				NewApplyDomain masInfo = batchRepository.getMasInfo(newApplyDomain);
				
				if(masInfo != null) {
					this.masterAndRelatedInfoDel(masInfo);
				}
				//newApplyDomain.setApiResMsg(delResult.getMessage()); 		//완전 삭제여서 필요 없음
				//newApplyDomain.setApiSuccessCode(delResult.getCode()); 	//완전 삭제여서 필요 없음
				req.setStatus("2");
				cnt = 1;
			}else {
				//가등록 삭제 실패
				errorMessage = "가등록 삭제시 오류 발생 :: "+delResult.getCode();
				newApplyDomain.setApiResMsg(delResult.getMessage());
				newApplyDomain.setApiSuccessCode(delResult.getCode());
				req.setStatus("3");
				req.setError("code ::"+delResult.getCode()+" Message :: "+delResult.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			newApplyDomain.setApiResMsg(e.getMessage());
			newApplyDomain.setApiSuccessCode("fail");
			req.setStatus("3");
			if(StringUtils.isEmpty(errorMessage)) {
				req.setError(e.getMessage());
			}else {
				req.setError(errorMessage);
			}
		} finally {
			batchRepository.deletePreLcNum(newApplyDomain); //가등록 삭제 실패 시 리턴메세지 저장
			batchRepository.updateSchedule(req);
		}
		return cnt;
	}
	
	/* -------------------------------------------------------------------------------------------------------------------
	 * 2021-12-22 통계 관련
	 * -------------------------------------------------------------------------------------------------------------------
	 */
	
	//등록신청현황(모집인별)
	@Transactional
	public void saveRegStatsInfo() throws Exception {
		//삭제 후 등록
		StatsDomain param = new StatsDomain();
		int deleteResult = batchRepository.realDeleteRegStatsInfo(param);
		
		if(deleteResult > 0) {
			batchRepository.insertRegStatsInfo(param);
		}
	}
	
	//해지신청현황(모집인별)
	@Transactional
	public void saveCancelStatsInfo() throws Exception {
		//삭제 후 등록
		StatsDomain param = new StatsDomain();
		int deleteResult = batchRepository.realDeleteCancelStatsInfo(param);
		
		if(deleteResult > 0) {
			batchRepository.insertCancelStatsInfo(param);
		}
	}
	
	
}
