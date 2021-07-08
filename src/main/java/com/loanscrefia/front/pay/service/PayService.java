package com.loanscrefia.front.pay.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.front.pay.domain.PayDomain;
import com.loanscrefia.front.pay.repository.PayRepository;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

@Service
public class PayService {

	@Autowired private PayRepository payRepo;
	@Autowired private SearchService searchService;
	
	//은행연합회
	@Autowired private KfbApiService kfbApiService;
	
	//결제정보 저장
	@Transactional
	public boolean insertPayResult(PayDomain payDomain) {
		
		boolean result 		= true;
		SearchDomain param 	= new SearchDomain();
		
		//(1)결제 테이블에 정보 저장
		int payResultInsertResult = payRepo.insertPayResult(payDomain);
		
		if(payResultInsertResult > 0) {
			//임시
			param.setMasterSeq(payDomain.getMasterSeq());
			param.setPlRegStat("3");
			int updateResult = searchService.updatePlRegStat(param);
			
			if(updateResult == 0) {
				result = false;
			}
			
			/*
			//(2)은행연합회 통신(본동록) : 성공 시 모집인 상태(pl_reg_stat) = 자격취득 / 실패 시 결제완료(이후 프로세스는 협회쪽에서 수동으로 진행)
			KfbApiDomain kfbApiDomain 	= new KfbApiDomain();
			String apiToken 			= kfbApiService.selectKfbApiKey(kfbApiDomain);
			
			SearchDomain searchDoamin 	= new SearchDomain();
			searchDoamin.setMasterSeq(payDomain.getMasterSeq());
			searchDoamin.setPlRegStat("2");
			SearchDomain userInfo 		= searchService.selectSearchUserInfo(searchDoamin);
			
			JSONObject loanApiReqParam 	= new JSONObject();
			ResponseMsg loanApiResult 	= new ResponseMsg(HttpStatus.OK, "fail", null, "오류가 발생하였습니다.");
			
			if(userInfo.getPlClass().equals("1")) {
				//개인
				loanApiReqParam.put("pre_lc_num", userInfo.getPreLcNum());
				loanApiResult = kfbApiService.commonKfbApi(apiToken, loanApiReqParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "POST", userInfo.getPlClass());
			}else {
				//법인
				loanApiReqParam.put("pre_corp_lc_num", userInfo.getPreLcNum());
				loanApiResult = kfbApiService.commonKfbApi(apiToken, loanApiReqParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "POST", userInfo.getPlClass());
			}
			
			if(loanApiResult.getCode().equals("success")) {
				JsonObject loanApiResponse = (JsonObject)loanApiResult.getData();
				System.out.println("#########################################");
				System.out.println("insertPayResult() >> loanApiResponse >> "+loanApiResponse);
				System.out.println("#########################################");
				
				//(3)모집인 상태(pl_reg_stat) 자격취득으로 수정 + 계약번호 저장
				param.setMasterSeq(payDomain.getMasterSeq());
				param.setPlRegStat("3");
				param.setConNum(loanApiResponse.get("con_num").toString());
				int updateResult = searchService.updatePlRegStat(param);
				
				if(updateResult == 0) {
					result = false;
				}
			}else {
				//은행연합회 통신(본동록) 실패
				param.setMasterSeq(payDomain.getMasterSeq());
				param.setPlRegStat("");
				searchService.updatePlRegStat(param);
				
				return false;
			}
			*/
			
		}else {
			//결제 테이블 저장 실패
			result = false;
		}
		return result;
	}
	
	
	
	
	
	
}
