package com.loanscrefia.system.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
    	responseMsg.setData(apiService.selectApiList(kfbApiDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API네트워크 및 서버상태 확인
	@PostMapping(value="/api/getHealthCheck")
	public ResponseEntity<ResponseMsg> getHealthCheck(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		responseMsg.setData(kfbApiService.getHealthCheck(KfbApiService.ApiDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	// API코드발급
	@PostMapping(value="/api/getApiCode")
	public ResponseEntity<ResponseMsg> getApiCode(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		//responseMsg.setData(kfbApiService.getAuthCode());
		String authCode = kfbApiService.getAuthCode();
		kfbApiDomain.setCode(authCode);
		responseMsg.setData(kfbApiDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API토큰발급
	@PostMapping(value="/api/getAuthToken")
	public ResponseEntity<ResponseMsg> getAuthToken(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		responseMsg.setData(kfbApiService.getAuthToken());
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 개인 등록가능 여부 조회
	@PostMapping(value="/api/loanCheckTest")
	public ResponseEntity<ResponseMsg> loanCheckTest(KfbApiDomain kfbApiDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null );
		String tempToken 		= kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject reqParam 	= new JSONObject();
		
		reqParam.put("name", "연수원2307");
		reqParam.put("ssn", "9912301111111");
		reqParam.put("ci", "401==");
		reqParam.put("loan_type", "05");
		
		responseMsg.setData(kfbApiService.checkLoan(tempToken,reqParam));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	@GetMapping("/api/apiPopup")
    public ModelAndView apiPopup(@ModelAttribute KfbApiDomain kfbApiSearchDomain) {
    	ModelAndView mv = new ModelAndView(CosntPage.Popup+"/apiPopup");
    	
    	
		// 계약번호 : con_num
		// 업권코드 : biz_code(은행/저축은행/보험사 등 업권코드)
		// 법인등록번호 : corp_num(법인사용인)
		// 계약 금융기관코드 : fin_code(회원사)
		// 계약 금융기관명		:	fin_name
		// 계약 금융기관 연락처	:	fin_phone
		// 계약일				:	con_date
		// 대출모집인유형코드		:	loan_type(취급상품)
		// 해지일				:	cancel_date
		// 해지코드			:	cancel_code
		
		// 2021-07-10 은행연합회 API 통신
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
		JSONObject indvParam = new JSONObject();
		String plClass = searchDomain.getClassCheck();
		// 은행연합회 API 개인조회 시작
		if("indvUserSearchFrm".equals(plClass)) {
			indvParam.put("lc_num", searchDomain.getPlRegistSearchNo());
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
				
				// 2021-07-10 아래 데이터 사용안함
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
			indvParam.put("corp_lc_num", searchDomain.getPlRegistSearchNo());
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
				
					
				// 아래데이터 활용X
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
    	
    	
    	
    	
    	
    	mv.addObject("lcNim", "");
    	
        return mv;
    }
	
	
	
	
	
	
	
}