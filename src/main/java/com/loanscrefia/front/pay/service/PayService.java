package com.loanscrefia.front.pay.service;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.front.pay.domain.PayDomain;
import com.loanscrefia.front.pay.repository.PayRepository;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayService {

	@Autowired private PayRepository payRepo;
	@Autowired private SearchService searchService;
	
	//은행연합회
	@Autowired private KfbApiService kfbApiService;
	
	//결제정보 저장
	@Transactional
	public boolean insertPayResult(PayDomain payDomain) {
		
		SearchDomain param 	= new SearchDomain();
		
		//(1)결제 테이블에 정보 저장
		int payResultInsertResult = payRepo.insertPayResult(payDomain);
		
		if(payResultInsertResult > 0) {
			/*
			//임시
			param.setMasterSeq(payDomain.getMasterSeq());
			param.setPlRegStat("3");
			int updateResult = searchService.updatePlRegStat(param);
			
			if(updateResult == 0) {
				result = false;
			}
			*/
			
			//(2)은행연합회 통신(본동록) : 성공 시 모집인 상태(pl_reg_stat) = 자격취득 / 실패 시 결제완료(이후 프로세스는 협회쪽에서 수동으로 진행)
			SearchDomain searchDoamin 	= new SearchDomain();
			searchDoamin.setMasterSeq(payDomain.getMasterSeq());
			searchDoamin.setPlRegStat("2");
			SearchDomain userInfo 		= searchService.selectSearchUserInfo(searchDoamin);
			
			if("01".equals(userInfo.getPlProduct()) || "05".equals(userInfo.getPlProduct())) { //금융상품유형이 TM대출,TM리스이면 은행연합회 API 통신 X(임시) -> 추후 조건문 주석
				KfbApiDomain kfbApiDomain 	= new KfbApiDomain();
				String apiToken 			= kfbApiService.selectKfbApiKey(kfbApiDomain);
				
				JSONObject loanApiReqParam 	= new JSONObject();
				ResponseMsg loanApiResult 	= new ResponseMsg(HttpStatus.OK, "fail", null, "오류가 발생하였습니다.");
				
				if(userInfo.getPlClass().equals("1")) {
					//개인
					loanApiReqParam.put("pre_lc_num", userInfo.getPreLcNum());
					loanApiResult = kfbApiService.commonKfbApi(apiToken, loanApiReqParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "POST", userInfo.getPlClass(), "Y");
				}else {
					//법인
					loanApiReqParam.put("pre_corp_lc_num", userInfo.getPreLcNum());
					loanApiResult = kfbApiService.commonKfbApi(apiToken, loanApiReqParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "POST", userInfo.getPlClass(), "Y");
				}
				
				String lcNum 	= "";
				String conNum 	= "";
				
				if(loanApiResult.getCode().equals("success")) {
					JSONObject loanApiResponse = (JSONObject)loanApiResult.getData();
					log.info("#########################################");
					log.info("insertPayResult() >> loanApiResponse >> "+loanApiResponse);
					log.info("#########################################");
					
					if(userInfo.getPlClass().equals("1")) {
						//개인
						lcNum = loanApiResponse.getString("lc_num");
					}else {
						//법인
						lcNum = loanApiResponse.getString("corp_lc_num");
					}
					
					if(StringUtils.isEmpty(lcNum)) {
						log.info("#########################################");
						log.info("payService >> insertPayResult() >> lcNum empty");
						log.info("#########################################");
						
						param.setMasterSeq(payDomain.getMasterSeq());
						param.setPlRegStat("5");
						searchService.updatePlRegStat(param);
						
						return false;
					}
					
					JSONObject jsonObj 	= new JSONObject();
					JSONArray conArr 	= loanApiResponse.getJSONArray("con_arr");
					
					for(int i = 0; i < conArr.length(); i++){
						jsonObj 		= conArr.getJSONObject(i);
						String loanType = jsonObj.getString("loan_type");
						String finCode 	= jsonObj.getString("fin_code");
						//통신결과로 리턴받은 계약금융기관코드와 상품코드 등록된 데이터와 비교
						if(loanType.equals(userInfo.getPlProduct()) && finCode.equals(Integer.toString(userInfo.getComCode()))) {
							conNum = jsonObj.getString("con_num");
						}
					}
					
					if(StringUtils.isEmpty(conNum)) {
						log.info("#########################################");
						log.info("payService >> insertPayResult() >> conNum empty");
						log.info("#########################################");
						
						param.setMasterSeq(payDomain.getMasterSeq());
						param.setPlRegStat("5");
						searchService.updatePlRegStat(param);
						
						return false;
					}
					
					//(3)모집인 상태(pl_reg_stat) 자격취득으로 수정 + 계약번호 저장
					param.setMasterSeq(payDomain.getMasterSeq());
					param.setPlRegStat("3");
					param.setPlRegistNo(lcNum);
					param.setConNum(conNum);
					int updateResult = searchService.updatePlRegStat(param);
					
					if(updateResult == 0) {
						//모집인 상태(pl_reg_stat) 변경 실패
						log.info("#########################################");
						log.info("payService >> insertPayResult() >> 모집인 상태(pl_reg_stat) 변경 실패");
						log.info("#########################################");
						
						param.setMasterSeq(payDomain.getMasterSeq());
						param.setPlRegStat("5");
						param.setPlRegistNo(lcNum);
						param.setConNum(conNum);
						searchService.updatePlRegStat(param);
						
						return false;
					}
				}else {
					//은행연합회 통신(본동록) 실패
					log.info("#########################################");
					log.info("payService >> insertPayResult() >> 은행연합회 통신(본동록) 실패");
					log.info("#########################################");
					
					param.setMasterSeq(payDomain.getMasterSeq());
					param.setPlRegStat("5");
					searchService.updatePlRegStat(param);
					
					return false;
				}
			}
			
		}else {
			//결제 테이블 저장 실패
			log.info("#########################################");
			log.info("payService >> insertPayResult() >> 결제 테이블 저장 실패");
			log.info("#########################################");
			
			param.setMasterSeq(payDomain.getMasterSeq());
			param.setPlRegStat("5");
			searchService.updatePlRegStat(param);
			
			return false;
		}
		
		return true;
	}
	
	
	
	
	
	
}
