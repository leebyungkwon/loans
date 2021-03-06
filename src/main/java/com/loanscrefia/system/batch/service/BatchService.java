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
	
	//SMS ????????????
	@Value("${sms.apply}")
	public boolean smsApply;
	
	//?????? ?????? ?????? - ?????? ????????????
	@Value("${expired.regCancel}")
	public int regCancel;
	
	//?????? ?????? ?????? - ????????? ????????????
	@Value("${expired.notApply}")
	public int notApply;
	
	//?????? ?????? ?????? - ?????? ????????? ????????????
	@Value("${expired.notApplyAgain}")
	public int notApplyAgain;
	
	//?????? ?????? ?????? - ?????? ????????????
	@Value("${expired.cancel}")
	public int cancel;
		
	//?????? ?????? ?????? - ???????????? ???????????? ????????????
	@Value("${expired.reject}")
	public int reject;
		
	//?????? ?????? ?????? - ???????????? ?????????(?????????) ????????????
	@Value("${expired.inaq}")
	public int inaq;
		
	
	@Transactional
	public int recruitReg(BatchDomain req) {
		ApiDomain param = new ApiDomain();
		int cnt = 0;
		//schedule ?????? ????????????
		try {
			//???????????? ???????????? param ?????????
			
			//api ??????
			ResponseMsg result = apiService.excuteApi(param);
			
			
			//????????? ??????
			NewRecruitDomain recruitDomain = new NewRecruitDomain();
			recruitRepository.insertNewMasterStep(recruitDomain);
			if("success".equals(result.getCode())) cnt = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			//schedule ?????? ????????????
			return cnt;
		}
	}

	public List<BatchDomain> selectReqBatchList(BatchDomain param) {
		return batchRepository.selectReqBatchList(param);
	}
	
	// 2021-11-24 ?????? ?????? ?????? ??????
	public int getTokenCheck(BatchDomain param) {
		return batchRepository.getTokenCheck(param);
	}
	
	
	
	
	
	// ???????????? insert
	@Transactional
	public int insertBatchPlanInfo(BatchDomain batchDomain) {
		return batchRepository.insertBatchPlanInfo(batchDomain);
	}
	
	
	// 2021-11-09 ?????????????????? ??????
	public List<BatchDomain> selectBatchList(BatchDomain param) {
		return batchRepository.selectBatchList(param);
	}
	
	// 2021-11-11 ??????????????? ??????
	public List<NewApplyDomain> selectDropApplyList(BatchDomain param) {
		return batchRepository.selectDropApplyList(param);
	}
	
	
	
	// 2021-11-10 ?????? ?????? ?????? ??????
	public int insertScheduleHist(BatchDomain param) {
		return batchRepository.insertScheduleHist(param);
	}
	
	// 2021-11-10 ?????? ?????? ?????? ??????
	public void updateScheduleHist(BatchDomain param) {
		batchRepository.updateScheduleHist(param);
	}
	
	
	
	// 2021-11-15 ????????? ??????
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
			req.setError("??????(??????/??????) ???????????? ??????");
			errorMessage = "??????(??????/??????) ???????????? ??????";
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
				// ????????? ?????? ??????
				req.setStatus("3");
				errorMessage = "????????? ????????? ?????? ?????? :: "+delResult.getCode();
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
	
	

	// 2021-11-115 ?????????
	@Transactional
	public int preloanReg(BatchDomain req) throws Exception {
		// ???????????? ????????? ????????????
		String errorMessage = "";
		ApiDomain preLoanParam = new ApiDomain();
		preLoanParam.setMethod("POST");
		JSONObject jsonParam = new JSONObject(req.getParam());
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("????????? ?????? seq ???????????? ??????");
			errorMessage = "????????? ?????? seq ???????????? ??????";
			throw new Exception();
		}

		String plClass = req.getProperty01();
		if("1".equals(plClass)) {
			preLoanParam.setUrl("/loan/v1/pre-loan-consultants");
			preLoanParam.setApiName("preIndvLoanReg");

			// ???????????? ????????? decrypt
			String ssn = jsonParam.getString("ssn");
			jsonParam.remove("ssn");
			jsonParam.put("ssn", CryptoUtil.decrypt(ssn));
			
		}else if("2".equals(plClass)) {
			preLoanParam.setUrl("/loan/v1/pre-loan-corp-consultants");
			preLoanParam.setApiName("preCorpLoanReg");
			
			// ???????????? ????????? decrypt
			String corpRepSsn = jsonParam.getString("corp_rep_ssn");
			jsonParam.remove("corp_rep_ssn");
			jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(corpRepSsn));
			
			// ???????????? ????????? decrypt
			String corpNum = jsonParam.getString("corp_num");
			jsonParam.remove("corp_num");
			jsonParam.put("corp_num", CryptoUtil.decrypt(corpNum));
			
		}else {
			req.setStatus("3");
			req.setError("??????(??????/??????) ???????????? ??????");
			errorMessage = "??????(??????/??????) ???????????? ??????";
			throw new Exception();
		}
		
		// master_seq ?????? ??? ?????? 
		jsonParam.remove("master_seq");
		preLoanParam.setParamJson(jsonParam);
		
		int cnt = 0;
		try {
			ResponseMsg preRegResult = apiService.excuteApi(preLoanParam);
			if("success".equals(preRegResult.getCode())) {
				String preLcNum ="";
				String feeYn = "";
				JSONObject preLoanResponseJson = new JSONObject(preRegResult.getData().toString());
				
				// ???????????????
				if(!preLoanResponseJson.isNull("fee_yn")) {
					feeYn = preLoanResponseJson.getString("fee_yn");
				}
				
				// ??????/??????(???????????????)
				if("1".equals(plClass)) {
					if(!preLoanResponseJson.isNull("pre_lc_num")) {
						preLcNum = preLoanResponseJson.getString("pre_lc_num");
					}
				}else {
					if(!preLoanResponseJson.isNull("pre_corp_lc_num")) {
						preLcNum = preLoanResponseJson.getString("pre_corp_lc_num");
					}
				}
				
				// ????????? ?????? ??? ??????????????? ????????? ???????????? ??????
				if(StringUtils.isEmpty(preLcNum)) {
					req.setStatus("3");
					req.setError("??????????????? ?????? ??????");
					errorMessage = "??????????????? ?????? ??????";
					throw new Exception();
				}
				
				
				// ??????????????? update
				NewApplyDomain newApplyDomain = new NewApplyDomain();
				newApplyDomain.setPreLcNum(preLcNum);
				newApplyDomain.setMasterSeq(masterSeq);
				newApplyDomain.setPreRegYn(feeYn);
				batchRepository.updatePreLcNum(newApplyDomain);
				req.setStatus("2");
				cnt = 1;
				
				
			}else {
				// ????????? ??????
				req.setStatus("3");
				errorMessage = "??????????????? ????????? ?????? ?????? :: "+preRegResult.getCode();
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
	
	

	
	
	// 2021-11-115 ?????????
	@Transactional
	public int loanReg(BatchDomain req) throws Exception {
		// ???????????? ????????? ????????????
		String errorMessage = "";
		ApiDomain loanParam = new ApiDomain();
		loanParam.setMethod("POST");
		JSONObject jsonParam = new JSONObject(req.getParam());
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("????????? ?????? seq ???????????? ??????");
			errorMessage = "????????? ?????? seq ???????????? ??????";
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
		
		// master_seq ?????? ??? ?????? 
		jsonParam.remove("master_seq");
		loanParam.setParamJson(jsonParam);
		
		// ???????????? update
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		String lcNum ="";
		String conNum = "";
		int cnt = 0;
		try {
			ResponseMsg regResult = apiService.excuteApi(loanParam);
			if("success".equals(regResult.getCode())) {
				
				// ????????? master_seq??? ??????, ?????????????????? ??????
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
						req.setError("?????????????????? ?????? ??????");
						errorMessage = "?????????????????? ?????? ??????";
						throw new Exception();
					}
					
				}else {
					if(!loanResponseJson.isNull("corp_lc_num")) {
						lcNum = loanResponseJson.getString("corp_lc_num");
					}else {
						req.setStatus("3");
						req.setError("?????????????????? ?????? ??????");
						errorMessage = "?????????????????? ?????? ??????";
						throw new Exception();
					}
				}
				
				JSONObject jsonObj = new JSONObject();
				JSONArray conArr = loanResponseJson.getJSONArray("con_arr");
				// ????????????????????????(?????????????????? ????????? ??????)
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					String loanType = jsonObj.getString("loan_type");
					String finCode = jsonObj.getString("fin_code");
					// ????????? ???????????????????????? ??? ??????????????? ????????????(????????????)??? ????????? ????????? ??????(?????????, ?????????????????????????????? ?????? ????????????)
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
				
				
				// ???????????? ?????? ????????? ??????
				int smsResult = 0;
				SmsDomain smsDomain = new SmsDomain();
				smsDomain.setTranCallback("0220110770");
				smsDomain.setTranStatus("1");
				smsDomain.setTranEtc1("10070");
				smsDomain.setTranPhone(applyResult.getPlCellphone().replaceAll("-", ""));
				String tranMsg = "";
				
				if("1".equals(plClass)) {
					tranMsg = applyResult.getUserName()+"?????? ??????????????? ????????? ????????? ?????????????????????. ??????????????? "+lcNum+" ?????????.";
				}else {
					tranMsg = applyResult.getPlMerchantName()+" ????????? ??????????????? ????????? ????????? ?????????????????????. ??????????????? "+lcNum+" ?????????.";
				}
				
				smsDomain.setTranMsg(tranMsg);
				smsResult = smsRepository.sendSms(smsDomain);
				
			}else {
				// ????????? ??????
				req.setStatus("3");
				errorMessage = "??????????????? ????????? ?????? ?????? :: "+regResult.getCode();
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

	// 2021-11-11 ????????????
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
			req.setError("????????? seq ???????????? ??????");
			errorMessage = "????????? seq ???????????? ??????";
			throw new Exception();
		}
		
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("????????? ?????? seq ???????????? ??????");
			errorMessage = "????????? ?????? seq ???????????? ??????";
			throw new Exception();
		}

		// master_seq ?????? ??? ?????? 
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
			// ????????? ?????? ????????? ??????
			jsonParam.remove("mobile_no");
			
		}else {
			req.setStatus("3");
			req.setError("??????(??????/??????) ???????????? ??????");
			errorMessage = "??????(??????/??????) ???????????? ??????";
			throw new Exception();
		}
		
		loanUpdParam.setParamJson(jsonParam);
		
		int cnt = 0;
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		
		try {
			ResponseMsg updResult = apiService.excuteApi(loanUpdParam);
			if("success".equals(updResult.getCode())) {
				// ???????????? ????????? ????????? ??????
				if("1".equals(plClass)) {
					newApplyDomain.setPlMName(jsonParam.getString("name"));
					// ?????????, ????????? ??? JSON??????
					JSONObject jsonObj = new JSONObject();
					JSONArray conArr = jsonParam.getJSONArray("con_arr");
					for(int i=0; i<conArr.length(); i++){
						jsonObj = conArr.getJSONObject(i);
						newApplyDomain.setPlCellphone(jsonObj.getString("con_mobile").replaceAll("-", ""));
					}
					
					// ???????????? ???????????? ??????
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setUserName(jsonParam.getString("name"));
					usersDomain.setMobileNo(jsonObj.getString("con_mobile").replaceAll("-", ""));
					batchRepository.updateIndvUsersInfo(usersDomain);
					
					// ???????????? ?????? ??? ???????????????MAS01 ?????? ??????
					batchRepository.updateIndvMasInfo(newApplyDomain);
					
				}else {
					newApplyDomain.setPlMerchantName(jsonParam.getString("corp_name"));
					newApplyDomain.setPlCeoName(jsonParam.getString("corp_rep_name"));
					
					// ???????????? ???????????? ??????
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setMobileNo(mobileNo);
					usersDomain.setUserName(jsonParam.getString("corp_rep_name"));
					
					// ???????????? ????????? ?????? ??????
					//usersDomain.setMobileNo();
					batchRepository.updateCorpUsersInfo(usersDomain);
					
					// ????????? ??????
					usersDomain.setPlMerchantName(jsonParam.getString("corp_name"));
					batchRepository.updateCorpInfo(usersDomain);
					
				}
				
				newApplyDomain.setApiResMsg(updResult.getMessage());
				newApplyDomain.setApiSuccessCode(updResult.getCode());
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// ????????????
				req.setStatus("3");
				errorMessage = "???????????? ?????? ?????? :: "+updResult.getCode();
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
	
	
	// 2021-11-11 ?????????????????? ??????
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
			req.setError("????????? seq ???????????? ??????");
			errorMessage = "????????? seq ???????????? ??????";
			throw new Exception();
		}

		// master_seq ?????? ??? ?????? 
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
				
				// ???????????? + ci ?????? ??????
				UsersDomain usersDomain = new UsersDomain();
				usersDomain.setUserSeq(userSeq);
				usersDomain.setPlMZId(aftSsn);
				usersDomain.setUserCi(aftCi);
				batchRepository.updateUsersSsnInfo(usersDomain);
				
				// ???????????? + ci ?????? ?????? ??? ????????? ?????? ??????
				
				newApplyDomain.setCi(aftCi);
				newApplyDomain.setPlMZId(aftSsn);
				batchRepository.updateMasSsnInfo(newApplyDomain);
				newApplyDomain.setApiResMsg(updResult.getMessage());
				newApplyDomain.setApiSuccessCode(updResult.getCode());
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// ????????????
				req.setStatus("3");
				errorMessage = "???????????? ?????? ?????? :: "+updResult.getCode();
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
	
	
	// ???????????? ??? ?????????????????? ?????? ????????? ????????? ??????
	public void userInfoUpdResult(String plClass, NewApplyDomain newApplyDomain, BatchDomain req) {
		// ???????????????????????? ?????? seq??? ???????????? ?????? req????????? ?????? ??????
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
	
	// 2021-11-11 ??????????????????
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
			req.setError("????????? seq ???????????? ??????");
			errorMessage = "????????? seq ???????????? ??????";
			throw new Exception();
		}
		
		int masterSeq = 0;
		if(!jsonParam.isNull("master_seq")) {
			masterSeq = jsonParam.getInt("master_seq");
		}else {
			req.setStatus("3");
			req.setError("????????? ?????? seq ???????????? ??????");
			errorMessage = "????????? ?????? seq ???????????? ??????";
			throw new Exception();
		}
		
		// master_seq ?????? ??? ?????? 
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
			
			// ???????????? ????????? decrypt
			String ssn = jsonParam.getString("corp_rep_ssn");
			jsonParam.remove("corp_rep_ssn");
			jsonParam.put("corp_rep_ssn", CryptoUtil.decrypt(ssn));
			
		}else {
			req.setStatus("3");
			req.setError("??????(??????/??????) ???????????? ??????");
			errorMessage = "??????(??????/??????) ???????????? ??????";
			throw new Exception();
		}
		
		int cnt = 0;
		
		NewApplyDomain newApplyDomain = new NewApplyDomain();
		newApplyDomain.setMasterSeq(masterSeq);
		
		try {
			ResponseMsg updResult = apiService.excuteApi(loanUpdParam);
			if("success".equals(updResult.getCode())) {
				// ???????????? ????????? ????????? ??????
				if("1".equals(plClass)) {
					newApplyDomain.setPlMName(jsonParam.getString("name"));
					// ?????????, ????????? ??? JSON??????
					JSONObject jsonObj = new JSONObject();
					JSONArray conArr = jsonParam.getJSONArray("con_arr");
					for(int i=0; i<conArr.length(); i++){
						jsonObj = conArr.getJSONObject(i);
						newApplyDomain.setPlCellphone(jsonObj.getString("con_mobile").replaceAll("-", ""));
					}
					
					// ???????????? ???????????? ??????
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
					
					// ???????????? ???????????? ??????
					UsersDomain usersDomain = new UsersDomain();
					usersDomain.setUserSeq(userSeq);
					usersDomain.setUserName(jsonParam.getString("corp_rep_name"));
					usersDomain.setPlMZId(CryptoUtil.encrypt(jsonParam.getString("corp_rep_ssn")));
					usersDomain.setUserCi(jsonParam.getString("corp_rep_ci"));
					
					// ???????????? ????????? ?????? ??????
					//usersDomain.setMobileNo();
					batchRepository.updateCorpUsersInfo(usersDomain);
					
					// ????????? ??????
					//usersDomain.setPlMerchantName(jsonParam.getString("corp_rep_name").toString());
					//batchRepository.updateCorpInfo(usersDomain);
					
				}
				
				newApplyDomain.setApiResMsg(updResult.getMessage());
				newApplyDomain.setApiSuccessCode(updResult.getCode());
				req.setStatus("2");
				cnt = 1;
				
			}else {
				
				// ????????????
				req.setStatus("3");
				errorMessage = "????????????API ?????? ?????? :: "+updResult.getCode();
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
	
	
	
	
	
	
	
	
	// 2021-11-11 ??????
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
			req.setError("????????? ?????? seq ???????????? ??????");
			errorMessage = "????????? ?????? seq ???????????? ??????";
			throw new Exception();
		}

		// master_seq ?????? ??? ?????? 
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
			req.setError("??????(??????/??????) ???????????? ??????");
			errorMessage = "??????(??????/??????) ???????????? ??????";
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
				// ????????????
				req.setStatus("3");
				errorMessage = "???????????? ?????? ?????? :: "+updResult.getCode();
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
	
	
	
	// 2021-11-11 ???????????? ??????
	@Transactional
	public int violationReg(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain vioRegParam = new ApiDomain();
		vioRegParam.setMethod("POST");
		JSONObject jsonParam = new JSONObject(req.getParam());
		vioRegParam.setUrl("/loan/v1/violation-consultants");
		vioRegParam.setApiName("violationReg");

		// ???????????? ????????? decrypt
		String ssn = jsonParam.getString("ssn");
		jsonParam.remove("ssn");
		jsonParam.put("ssn", CryptoUtil.decrypt(ssn));
		
		// ???????????? ?????????
		String vioSeq = jsonParam.getString("vio_seq");
		// vio_seq ?????? ??? ?????? 
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
				
				//????????????
				NewUserDomain vioRegDomain = new NewUserDomain();
				vioRegDomain.setVioNum(apiVioNum);
				vioRegDomain.setViolationSeq(Integer.parseInt(vioSeq));
				batchRepository.updateUserViolationInfo(vioRegDomain);
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// ???????????? ?????? ??????
				req.setStatus("3");
				errorMessage = "???????????? ????????? ?????? ?????? :: "+vioResult.getCode();
				
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
	
	
	
	

	// 2021-11-12 ???????????? ??????
	@Transactional
	public int violationDel(BatchDomain req) throws Exception {
		String errorMessage = "";
		ApiDomain vioDelParam = new ApiDomain();
		vioDelParam.setMethod("DELETE");
		JSONObject jsonParam = new JSONObject(req.getParam());
		vioDelParam.setUrl("/loan/v1/violation-consultants");
		vioDelParam.setApiName("violationDel");

		// ???????????? ?????????
		String vioSeq = jsonParam.getString("vio_seq");
		// vio_seq ?????? ??? ?????? 
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
				
				//????????????
				NewUserDomain vioRegDomain = new NewUserDomain();
				vioRegDomain.setVioNum(apiVioNum);
				vioRegDomain.setViolationSeq(Integer.parseInt(vioSeq));
				batchRepository.deleteUserViolationInfo(vioRegDomain);
				req.setStatus("2");
				cnt = 1;
				
			}else {
				// ???????????? ?????? ??????
				req.setStatus("3");
				errorMessage = "???????????? ????????? ?????? ?????? :: "+vioResult.getCode();
				
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
			return new ResponseMsg(HttpStatus.OK, "success", "?????????????????????.");
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", "????????? ?????????????????????.");
		}
	}
	
	
	
	//2021-12-23 ??????????????? ?????? ??? ????????? ????????? ??????
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
			//????????? ??????
			ResponseMsg responseMsg = apiService.excuteApi(apiDomain);
			
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				
				if(!responseJson.isNull("fee_yn")) {
					//??????????????? ??????
					if("Y".equals(responseJson.getString("fee_yn"))) {
						//(1)????????? ?????? ??????
						SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
						Date currentDt = new Date();
						String today = dateFormatParser.format(currentDt);
						
						BatchDomain batchDomain = new BatchDomain();
						
						batchDomain.setScheduleName("loanReg");
						batchDomain.setParam(jsonParam.toString());
						batchDomain.setProperty01(plClass);
						batchDomain.setProperty05("direct_"+today);
						
						this.insertBatchPlanInfo(batchDomain);
						
						//(2)?????? ????????? ????????? pre_reg_yn ??????
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
				//?????? ??????
				req.setStatus("3");
				req.setError("code ::"+responseMsg.getCode()+" Message :: "+responseMsg.getMessage());
			}
		}catch (Exception e) {
			e.printStackTrace();
			req.setStatus("3");
			req.setError("preRegContSearch() ?????? ?????? :: catch???");
		}finally {
			batchRepository.updateSchedule(req);
		}
		return cnt;
	}
	
	/* -------------------------------------------------------------------------------------------------------------------
	 * 2021-12-07 ?????? ?????? ??????
	 * tb_lc_file01 / tb_lc_file_check
	 * tb_lc_mas01 / tb_lc_mas01_hist / tb_lc_mas01_step / tb_lc_product_detail
	 * tb_lc_mas01_imwon / tb_lc_mas01_expert / tb_lc_mas01_it : ?????? ???????????? ??????
	 * interval??? application.yml??? ???????????? ??????
	 * -------------------------------------------------------------------------------------------------------------------
	 */
	
	//2021-12-07 ???????????? ??????(?????????????????? 5???)
	@Transactional
	public void regCancelInfoDel() throws Exception {
		//???????????? ?????????
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(regCancel);
		List<NewApplyDomain> masList = batchRepository.selectRegCancelInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-07 ????????? ?????? ??????(?????? ?????????????????? 60???)
	@Transactional
	public void notApplyInfoDel() throws Exception {
		//????????? ?????? ?????????
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
					//TO-BE??? ?????? ??????
					this.masterAndRelatedInfoDel(masList.get(i));
				}
			}
		}
	}
	
	//2021-12-07 ?????? ????????? ?????? ??????(?????? ???????????????????????? 60???)
	@Transactional
	public void notApplyAgainInfoDel() throws Exception {
		//????????? ?????? ?????????
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
					//TO-BE??? ?????? ??????
					this.masterAndRelatedInfoDel(masList.get(i));
				}
			}
		}
	}
	
	//2021-12-07 ?????? ?????? ??????(?????????????????? 60???)
	@Transactional
	public void cancelInfoDel() throws Exception {
		//?????? ?????? ?????????
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(cancel);
		List<NewApplyDomain> masList = batchRepository.selectCancelInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-07 ???????????? ???????????? ?????? ??????(???????????????????????? 60???)
	@Transactional
	public void rejectInfoDel() throws Exception {
		//???????????? ???????????? ?????? ?????????
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(reject);
		List<NewApplyDomain> masList = batchRepository.selectRejectInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-23 ???????????? ?????????(?????????) ?????? ??????(??????????????????????????? 60???)
	@Transactional
	public void inaqInfoDel() throws Exception {
		//???????????? ?????????(?????????) ?????? ?????????
		NewApplyDomain masParam = new NewApplyDomain();
		masParam.setInterval(inaq);
		List<NewApplyDomain> masList = batchRepository.selectInaqInfoList(masParam);
		
		if(masList.size() > 0) {
			for(int i = 0;i < masList.size();i++) {
				this.masterAndRelatedInfoDel(masList.get(i));
			}
		}
	}
	
	//2021-12-07 ?????? ?????? ??????
	@Transactional
	public void masterAndRelatedInfoDel(NewApplyDomain newApplyDomain) {
		
		int masterDelResult = batchRepository.realDeleteMasInfo(newApplyDomain);
		
		//??? ???
		if(masterDelResult > 0) {
			batchRepository.realDeleteMasHistInfo(newApplyDomain);
			batchRepository.realDeleteMasStepInfo(newApplyDomain);
			batchRepository.realDeletePrdDtlInfo(newApplyDomain);
			
			FileDomain fileDomain = new FileDomain();
			fileDomain.setFileGrpSeq(newApplyDomain.getFileSeq());
			commonService.realDeleteFileByGrpSeq(fileDomain);
			
			//????????? ??? ?????????????????? ??????
			if(newApplyDomain.getPlClass().equals("2")) {
				//?????? ?????????
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
				
				//?????????????????? ?????????
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
				
				//???????????? ?????????
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
	
	//2021-12-07 ????????? ?????? ?????? ????????? ?????? ?????? ??? ????????? ??????
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
			req.setError("??????(??????/??????) ???????????? ??????");
			errorMessage = "??????(??????/??????) ???????????? ??????";
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
				//????????? ?????? ?????? : ?????? ??????
				NewApplyDomain masInfo = batchRepository.getMasInfo(newApplyDomain);
				
				if(masInfo != null) {
					this.masterAndRelatedInfoDel(masInfo);
				}
				//newApplyDomain.setApiResMsg(delResult.getMessage()); 		//?????? ???????????? ?????? ??????
				//newApplyDomain.setApiSuccessCode(delResult.getCode()); 	//?????? ???????????? ?????? ??????
				req.setStatus("2");
				cnt = 1;
			}else {
				//????????? ?????? ??????
				errorMessage = "????????? ????????? ?????? ?????? :: "+delResult.getCode();
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
			batchRepository.deletePreLcNum(newApplyDomain); //????????? ?????? ?????? ??? ??????????????? ??????
			batchRepository.updateSchedule(req);
		}
		return cnt;
	}
	
	/* -------------------------------------------------------------------------------------------------------------------
	 * 2021-12-22 ?????? ??????
	 * -------------------------------------------------------------------------------------------------------------------
	 */
	
	//??????????????????(????????????)
	@Transactional
	public void saveRegStatsInfo() throws Exception {
		//?????? ??? ??????
		StatsDomain param = new StatsDomain();
		int deleteResult = batchRepository.realDeleteRegStatsInfo(param);
		
		if(deleteResult > 0) {
			batchRepository.insertRegStatsInfo(param);
		}
	}
	
	//??????????????????(????????????)
	@Transactional
	public void saveCancelStatsInfo() throws Exception {
		//?????? ??? ??????
		StatsDomain param = new StatsDomain();
		int deleteResult = batchRepository.realDeleteCancelStatsInfo(param);
		
		if(deleteResult > 0) {
			batchRepository.insertCancelStatsInfo(param);
		}
	}
	
	
}
