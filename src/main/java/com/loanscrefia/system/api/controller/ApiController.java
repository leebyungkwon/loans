package com.loanscrefia.system.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.crefia.domain.CrefiaDomain;
import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.front.search.domain.SearchResultDomain;
import com.loanscrefia.system.api.service.ApiService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/system")
public class ApiController {
	
	@Autowired
	private KfbApiService kfbApiService;
	
	@Autowired
	private ApiService apiService;
	
	@Autowired
	private KfbApiRepository kfbApiRepository;

	// API관리 페이지
	@GetMapping("/api/apiPage")
	public ModelAndView apiPage() {
		ModelAndView mv = new ModelAndView(CosntPage.BoMainPage + "api/apiList");
		return mv;
	}

	// API리스트
	@PostMapping(value="/api/selectApiList")
	public ResponseEntity<ResponseMsg> selectApiList(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
    	responseMsg.setData(apiService.selectApiList(kfbApiDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API네트워크 및 서버상태 확인
	@PostMapping(value="/api/getHealthCheck")
	public ResponseEntity<ResponseMsg> getHealthCheck(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		responseMsg = kfbApiService.getHealthCheck(KfbApiService.ApiDomain);
		KfbApiDomain kfbApiResultDomain = new KfbApiDomain();
		if(responseMsg.getData() != null) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			kfbApiResultDomain.setResCode(responseJson.getString("res_code"));
			kfbApiResultDomain.setResMsg(responseJson.getString("res_msg"));
			
		}else {
			kfbApiResultDomain.setResCode("fail");
			kfbApiResultDomain.setResMsg("연결실패");			
		}
		responseMsg.setData(kfbApiResultDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// API코드발급
	@PostMapping(value="/api/getApiCode")
	public ResponseEntity<ResponseMsg> getApiCode(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		//responseMsg.setData(kfbApiService.getAuthCode());
		String authCode = kfbApiService.getAuthCode();
		kfbApiDomain.setCode(authCode);
		responseMsg.setData(kfbApiDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API토큰발급
	@PostMapping(value="/api/getAuthToken")
	public ResponseEntity<ResponseMsg> getAuthToken(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		responseMsg = kfbApiService.getAuthToken();
		KfbApiDomain kfbApiResultDomain = new KfbApiDomain();
		if(responseMsg.getData() != null) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			if(!"fail".equals(responseMsg.getCode())) {
				kfbApiResultDomain.setToken(responseJson.getString("authorization"));
				kfbApiResultDomain.setResCode(responseJson.getString("res_code"));
				kfbApiResultDomain.setResMsg(responseJson.getString("res_msg"));				
			}else {
				responseMsg.setCode(null);
				kfbApiResultDomain.setResCode("fail");
				kfbApiResultDomain.setResMsg("API오류발생 - DB확인");	
			}
		}else {
			responseMsg.setCode(null);
			kfbApiResultDomain.setResCode("fail");
			kfbApiResultDomain.setResMsg("연결실패");			
		}
		responseMsg.setData(kfbApiResultDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	//가등록번호 조회 팝업
	@GetMapping("/api/apiPreSearchPopup")
    public ModelAndView apiPreSearchPopup(RecruitDomain recruitSearchDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
    	ModelAndView mv = new ModelAndView(CosntPage.Popup+"/apiSearchPopup");
    	
    	
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
    	
    	// 개인
    	if("1".equals(recruitSearchDomain.getPlClass())) {
    		indvParam.put("pre_lc_num", recruitSearchDomain.getPreLcNum());
    		responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.PreLoanUrl, "GET", recruitSearchDomain.getPlClass(), "Y");
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				RecruitDomain recruitDomain = new RecruitDomain();
			
				// 화면 분기처리용
				recruitDomain.setPlClass("1");
				recruitDomain.setPlRegistNo(responseJson.getString("pre_lc_num"));
				recruitDomain.setPlMName(responseJson.getString("name"));
				String ssn = responseJson.getString("ssn");
				if(ssn != null) {
					ssn = ssn.substring(0, 6);
				}
				recruitDomain.setPlMZId(ssn);
				//recruitDomain.setPlMName(responseJson.getString("mobile"));
				//recruitDomain.setPlMName(responseJson.getString("career_yn"));
				
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				JSONObject jsonObj = new JSONObject();
				List<SearchResultDomain> searchResult = new ArrayList<SearchResultDomain>();
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					SearchResultDomain searchResultDomain = new SearchResultDomain();
					searchResultDomain.setConNum(jsonObj.getString("con_num"));
					searchResultDomain.setBizCode(jsonObj.getString("biz_code"));
					searchResultDomain.setCorpNum(jsonObj.getString("corp_num"));
					searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					searchResultDomain.setConDate(jsonObj.getString("con_date"));
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));
					searchResultDomain.setCancelDate(jsonObj.getString("cancel_date"));
					searchResultDomain.setCancelCode(jsonObj.getString("cancel_code"));
					searchResult.add(searchResultDomain);
				}
				
				// 배열 결과 넣고 화면으로 전달
				recruitDomain.setSearchResultList(searchResult);
				mv.addObject("result", recruitDomain);
			}else {
				// 이전 검증에서 통과 후				
				// 서버통신오류
			}
    		
    	}else { // 법인
    		
    		indvParam.put("pre_corp_lc_num", recruitSearchDomain.getPreLcNum());
    		responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.PreLoanUrl, "GET", recruitSearchDomain.getPlClass(), "Y");
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				RecruitDomain recruitDomain = new RecruitDomain();
				
				// 화면 분기처리용
				recruitDomain.setPlClass("2");
				
				recruitDomain.setPlRegistNo(responseJson.getString("lc_num"));
				recruitDomain.setPlMerchantNo(responseJson.getString("corp_num"));		// 법인등록번호
				recruitDomain.setPlMName(responseJson.getString("corp_name"));			// 법인명
				recruitDomain.setPlCeoName(responseJson.getString("corp_rep_name"));	// 법인대표성명
				String corpRepSsn = responseJson.getString("corp_rep_ssn");
				if(corpRepSsn != null) {
					corpRepSsn = corpRepSsn.substring(0, 6);
				}
				recruitDomain.setPlMZId(corpRepSsn);		// 법인대표주민번호			
				//recruitDomain.setCareerTyp(responseJson.getString("career_yn"));		// 경력여부
				
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				JSONObject jsonObj = new JSONObject();
				List<SearchResultDomain> searchResult = new ArrayList<SearchResultDomain>();
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					
					SearchResultDomain searchResultDomain = new SearchResultDomain();
					searchResultDomain.setConNum(jsonObj.getString("con_num"));
					searchResultDomain.setBizCode(jsonObj.getString("biz_code"));
					searchResultDomain.setCorpNum(jsonObj.getString("corp_num"));
					searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					searchResultDomain.setConDate(jsonObj.getString("con_date"));
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));
					searchResultDomain.setCancelDate(jsonObj.getString("cancel_date"));
					searchResultDomain.setCancelCode(jsonObj.getString("cancel_code"));
					searchResult.add(searchResultDomain);
				}
				
				// 배열 결과 넣고 화면으로 전달
				recruitDomain.setSearchResultList(searchResult);
				mv.addObject("result", recruitDomain);
			}else {
				// 이전 검증에서 통과 후				
				// 서버통신오류
			}
    	}
        return mv;
    }
	
	
	//등록번호 조회 팝업
	@GetMapping("/api/apiSearchPopup")
    public ModelAndView apiSearchPopup(RecruitDomain recruitSearchDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
    	ModelAndView mv = new ModelAndView(CosntPage.Popup+"/apiSearchPopup");
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
		String plClass = recruitSearchDomain.getPlClass();
		
		// 은행연합회 API 개인조회 시작
		if("1".equals(plClass)) {
			indvParam.put("lc_num", recruitSearchDomain.getPlRegistNo());
			responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "GET", "1", "N");
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				RecruitDomain recruitDomain = new RecruitDomain();
			
				// 화면 분기처리용
				recruitDomain.setPlClass("1");
				
				recruitDomain.setPlRegistNo(responseJson.getString("lc_num"));
				recruitDomain.setPlMName(responseJson.getString("name"));
				String ssn = responseJson.getString("ssn");
				if(ssn != null) {
					ssn = ssn.substring(0, 6);
				}
				recruitDomain.setPlMZId(ssn);
				//recruitDomain.setPlMName(responseJson.getString("mobile"));
				//recruitDomain.setPlMName(responseJson.getString("career_yn"));
				
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				JSONObject jsonObj = new JSONObject();
				List<SearchResultDomain> searchResult = new ArrayList<SearchResultDomain>();
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					
					SearchResultDomain searchResultDomain = new SearchResultDomain();
					searchResultDomain.setConNum(jsonObj.getString("con_num"));
					searchResultDomain.setBizCode(jsonObj.getString("biz_code"));
					searchResultDomain.setCorpNum(jsonObj.getString("corp_num"));
					searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					searchResultDomain.setConDate(jsonObj.getString("con_date"));
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));
					searchResultDomain.setCancelDate(jsonObj.getString("cancel_date"));
					searchResultDomain.setCancelCode(jsonObj.getString("cancel_code"));
					searchResult.add(searchResultDomain);
				}
				
				// 배열 결과 넣고 화면으로 전달
				recruitDomain.setSearchResultList(searchResult);
				mv.addObject("result", recruitDomain);
			}else {
				// 이전 검증에서 통과 후				
				// 서버통신오류
			}
			
			
		}else {
			// 은행연합회 API 법인조회 시작
			indvParam.put("corp_lc_num", recruitSearchDomain.getPlRegistNo());
			responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "GET", "2", "N");
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				RecruitDomain recruitDomain = new RecruitDomain();
				
				// 화면 분기처리용
				recruitDomain.setPlClass("2");
				
				recruitDomain.setPlRegistNo(responseJson.getString("lc_num"));
				recruitDomain.setPlMerchantNo(responseJson.getString("corp_num"));		// 법인등록번호
				recruitDomain.setPlMName(responseJson.getString("corp_name"));			// 법인명
				recruitDomain.setPlCeoName(responseJson.getString("corp_rep_name"));	// 법인대표성명
				String corpRepSsn = responseJson.getString("corp_rep_ssn");
				if(corpRepSsn != null) {
					corpRepSsn = corpRepSsn.substring(0, 6);
				}
				recruitDomain.setPlMZId(corpRepSsn);		// 법인대표주민번호			
				//recruitDomain.setCareerTyp(responseJson.getString("career_yn"));		// 경력여부
				
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				JSONObject jsonObj = new JSONObject();
				List<SearchResultDomain> searchResult = new ArrayList<SearchResultDomain>();
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					
					SearchResultDomain searchResultDomain = new SearchResultDomain();
					searchResultDomain.setConNum(jsonObj.getString("con_num"));
					searchResultDomain.setBizCode(jsonObj.getString("biz_code"));
					searchResultDomain.setCorpNum(jsonObj.getString("corp_num"));
					searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					searchResultDomain.setConDate(jsonObj.getString("con_date"));
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));
					searchResultDomain.setCancelDate(jsonObj.getString("cancel_date"));
					searchResultDomain.setCancelCode(jsonObj.getString("cancel_code"));
					searchResult.add(searchResultDomain);
				}
				
				// 배열 결과 넣고 화면으로 전달
				recruitDomain.setSearchResultList(searchResult);
				mv.addObject("result", recruitDomain);
			}else {
				// 이전 검증에서 통과 후				
				// 서버통신오류
			}
		}
        return mv;
    }
	
}