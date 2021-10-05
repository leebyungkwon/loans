package com.loanscrefia.system.api.controller;

import java.io.IOException;
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
import com.loanscrefia.member.user.domain.UserDomain;
import com.loanscrefia.member.user.repository.UserRepository;
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
	
	@Autowired private UserRepository userRepo;

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
	public ResponseEntity<ResponseMsg> getHealthCheck(KfbApiDomain kfbApiDomain) throws IOException{
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
	public ResponseEntity<ResponseMsg> getApiCode(KfbApiDomain kfbApiDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		//responseMsg.setData(kfbApiService.getAuthCode());
		String authCode = kfbApiService.getAuthCode();
		kfbApiDomain.setCode(authCode);
		responseMsg.setData(kfbApiDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// API토큰발급
	@PostMapping(value="/api/getAuthToken")
	public ResponseEntity<ResponseMsg> getAuthToken(KfbApiDomain kfbApiDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		responseMsg = kfbApiService.getAuthToken();
		KfbApiDomain kfbApiResultDomain = new KfbApiDomain();
		if(responseMsg.getData() != null) {
			JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
			if(!"fail".equals(responseMsg.getCode())) {
				kfbApiResultDomain.setToken(responseJson.getString("authorization"));
				kfbApiResultDomain.setResCode(responseJson.getString("res_code"));
				kfbApiResultDomain.setResMsg(responseJson.getString("res_msg"));
				responseMsg.setCode(null);
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
		responseMsg.setCode(null);
		responseMsg.setData(kfbApiResultDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	//가등록번호 조회 팝업
	@GetMapping("/api/apiPreSearchPopup")
    public ModelAndView apiPreSearchPopup(RecruitDomain recruitSearchDomain) throws IOException {
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
				recruitDomain.setPlRegistNo(recruitSearchDomain.getPreLcNum());
				recruitDomain.setPlMName(responseJson.getString("name"));
				String ssn = responseJson.getString("ssn");
				if(ssn != null) {
					ssn = ssn.substring(0, 6);
				}
				recruitDomain.setPlMZId(ssn);
				//recruitDomain.setPlMName(responseJson.getString("mobile"));
				//recruitDomain.setPlMName(responseJson.getString("career_yn"));
				recruitDomain.setPreRegYn(responseJson.getString("fee_yn"));	// 수수료 기 납부 여부
				recruitDomain.setPreRegState("state");							// P : 가등록완료, C : 가등록취소, S : 최종등록완료
				
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				JSONObject jsonObj = new JSONObject();
				List<SearchResultDomain> searchResult = new ArrayList<SearchResultDomain>();
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					SearchResultDomain searchResultDomain = new SearchResultDomain();
					
					// 가등록시 등록번호 분기처리 확인
					if(!jsonObj.isNull("corp_num")) {
						searchResultDomain.setCorpNum(jsonObj.getString("corp_num"));
					}
					searchResultDomain.setCorpNum(jsonObj.getString("con_mobile"));
					searchResultDomain.setConDate(jsonObj.getString("con_date"));
					searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					if(!jsonObj.isNull("fin_name")) {
						searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					}
					
					if(!jsonObj.isNull("fin_phone")) {
						searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					}
					
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));
					//searchResultDomain.setCancelDate(jsonObj.getString("cancel_date"));
					//searchResultDomain.setCancelCode(jsonObj.getString("cancel_code"));
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
    		responseMsg = kfbApiService.commonKfbApi(apiKey, indvParam, KfbApiService.ApiDomain+KfbApiService.PreLoanCorpUrl, "GET", recruitSearchDomain.getPlClass(), "Y");
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				RecruitDomain recruitDomain = new RecruitDomain();
				
				// 화면 분기처리용
				recruitDomain.setPlClass("2");
				
				recruitDomain.setPlRegistNo(recruitSearchDomain.getPreLcNum());
				//recruitDomain.setPlMerchantNo(responseJson.getString("corp_num"));		// 법인등록번호
				recruitDomain.setPlMName(responseJson.getString("corp_name"));			// 법인명
				recruitDomain.setPlCeoName(responseJson.getString("corp_rep_name"));	// 법인대표성명
				String corpRepSsn = responseJson.getString("corp_rep_ssn");
				if(corpRepSsn != null) {
					corpRepSsn = corpRepSsn.substring(0, 6);
				}
				recruitDomain.setPlMZId(corpRepSsn);		// 법인대표주민번호			
				//recruitDomain.setCareerTyp(responseJson.getString("career_yn"));		// 경력여부
				recruitDomain.setCareerTyp(responseJson.getString("state"));			// P : 가등록완료, C : 가등록취소, S : 최종등록완료
				recruitDomain.setPreRegYn(responseJson.getString("fee_yn"));			// 수수료 기 납부 여부
				
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				JSONObject jsonObj = new JSONObject();
				List<SearchResultDomain> searchResult = new ArrayList<SearchResultDomain>();
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					
					SearchResultDomain searchResultDomain = new SearchResultDomain();
					if(!jsonObj.isNull("biz_code")) {
						searchResultDomain.setBizCode(jsonObj.getString("biz_code"));
					}
					if(!jsonObj.isNull("con_date")) {
						searchResultDomain.setConDate(jsonObj.getString("con_date"));
					}
					if(!jsonObj.isNull("fin_code")) {
						searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					}
					if(!jsonObj.isNull("fin_name")) {
						searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					}
					if(!jsonObj.isNull("fin_phone")) {
						searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					}
					
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));
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
    public ModelAndView apiSearchPopup(RecruitDomain recruitSearchDomain) throws IOException {
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
				
				recruitDomain.setPlRegistNo(recruitSearchDomain.getPlRegistNo());
				recruitDomain.setPlMName(responseJson.getString("name"));
				String ssn = responseJson.getString("ssn");
				if(ssn != null) {
					ssn = ssn.substring(0, 6);
				}
				recruitDomain.setPlMZId(ssn);
				//recruitDomain.setPlMName(responseJson.getString("career_yn"));
				
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				JSONObject jsonObj = new JSONObject();
				List<SearchResultDomain> searchResult = new ArrayList<SearchResultDomain>();
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					
					SearchResultDomain searchResultDomain = new SearchResultDomain();
					searchResultDomain.setConNum(jsonObj.getString("con_num"));
					searchResultDomain.setBizCode(jsonObj.getString("biz_code"));
					if(!jsonObj.isNull("corp_num")) {
						searchResultDomain.setCorpNum(jsonObj.getString("corp_num"));
					}
					searchResultDomain.setConMobile(jsonObj.getString("con_mobile"));
					searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					if(!jsonObj.isNull("fin_name")) {
						searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					}
					
					if(!jsonObj.isNull("fin_phone")) {
						searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					}			
					searchResultDomain.setConDate(jsonObj.getString("con_date"));
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));
					
					// 등록시 해지정보 분기처리 확인
					if(!jsonObj.isNull("cancel_date")) {
						searchResultDomain.setCancelDate(jsonObj.getString("cancel_date"));
					}
					if(!jsonObj.isNull("cancel_code")) {
						searchResultDomain.setCancelCode(jsonObj.getString("cancel_code"));
					}
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
				
				recruitDomain.setPlRegistNo(recruitSearchDomain.getPlRegistNo());
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
					searchResultDomain.setConDate(jsonObj.getString("con_date"));
					searchResultDomain.setFinCode(jsonObj.getString("fin_code"));
					if(!jsonObj.isNull("fin_name")) {
						searchResultDomain.setFinName(jsonObj.getString("fin_name"));
					}
					if(!jsonObj.isNull("fin_phone")) {
						searchResultDomain.setFinPhone(jsonObj.getString("fin_phone"));
					}
					searchResultDomain.setLoanType(jsonObj.getString("loan_type"));

					if(!jsonObj.isNull("cancel_date")) {
						searchResultDomain.setCancelDate(jsonObj.getString("cancel_date"));
					}
					if(!jsonObj.isNull("cancel_code")) {
						searchResultDomain.setCancelCode(jsonObj.getString("cancel_code"));
					}
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
	
	
	
	
	// 가등록번호로 본등록번호 등록 배치
	@PostMapping(value="/api/apiBatch")
	public ResponseEntity<ResponseMsg> apiBatch(KfbApiDomain kfbApiDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		UserDomain userSearchDomain = new UserDomain();
		List<UserDomain> userList = userRepo.selectApiBatchList(userSearchDomain);
		if(userList.size() > 0) {
			for(UserDomain tmp : userList) {
				// 금융상품 3, 6번 제외
				String prdCheck = tmp.getPlProduct();
				String lcNum = "";
				String conNum = "";
				// 가등록에서 본등록시 등록번호 발급
				
				UserDomain userDomain = new UserDomain();
				if("01".equals(prdCheck) || "05".equals(prdCheck)) {
					
					// 2021-06-25 은행연합회 API 통신 - 등록
					String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
					JSONObject jsonParam = new JSONObject();
					String plClass = tmp.getPlClass();
					if("1".equals(plClass)) {
						jsonParam.put("pre_lc_num", tmp.getPreLcNum());
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "POST", plClass, "Y");				
					}else {
						jsonParam.put("pre_corp_lc_num", tmp.getPreLcNum());
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "POST", plClass, "Y");
					}
					
					if("success".equals(responseMsg.getCode())) {
						JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
						if("1".equals(tmp.getPlClass())) {
							lcNum = responseJson.getString("lc_num");
						}else {
							lcNum = responseJson.getString("corp_lc_num");
						}
						JSONObject jsonObj = new JSONObject();
						JSONArray conArr = responseJson.getJSONArray("con_arr");
						// 계약금융기관코드(저장되어있는 데이터 비교)
						String comCode = Integer.toString(tmp.getComCode());
						for(int i=0; i<conArr.length(); i++){
							jsonObj = conArr.getJSONObject(i);
							String loanType = jsonObj.getString("loan_type");
							String finCode = jsonObj.getString("fin_code");
							// 등록시 계약김융기관코드 및 대출모집인 유형코드(상품코드)가 동일한 정보만 저장(계약일, 대출모집인휴대폰번호 등등 추가가능)
							if(loanType.equals(prdCheck) && finCode.equals(comCode)) {
								conNum = jsonObj.getString("con_num");
								break;
							}
						}
						
						userDomain.setMasterSeq(tmp.getMasterSeq());
						userDomain.setPlRegistNo(lcNum);
						userDomain.setConNum(conNum);
						userDomain.setConNum("3");
						kfbApiRepository.updateKfbApiByUserInfo(userDomain);
					}
				}				
			}
		}
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	// 가등록번호로 본등록번호 수동등록
	@PostMapping(value="/api/apiReg")
	public ResponseEntity<ResponseMsg> apiReg(UserDomain userSearchDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		KfbApiDomain kfbApiDomain = new KfbApiDomain();
		UserDomain userResult = userRepo.getApiReg(userSearchDomain);
		// 금융상품 3, 6번 제외
		String prdCheck = userResult.getPlProduct();
		String lcNum = "";
		String conNum = "";
		// 가등록에서 본등록시 등록번호 발급
		
		if("01".equals(prdCheck) || "05".equals(prdCheck)) {
			
			// 2021-06-25 은행연합회 API 통신 - 등록
			String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
			JSONObject jsonParam = new JSONObject();
			String plClass = userResult.getPlClass();
			if("1".equals(plClass)) {
				jsonParam.put("pre_lc_num", userResult.getPreLcNum());
				responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "POST", plClass, "Y");				
			}else {
				jsonParam.put("pre_corp_lc_num", userResult.getPreLcNum());
				responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "POST", plClass, "Y");
			}
			
			if("success".equals(responseMsg.getCode())) {
				JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
				if("1".equals(userResult.getPlClass())) {
					lcNum = responseJson.getString("lc_num");
				}else {
					lcNum = responseJson.getString("corp_lc_num");
				}
				JSONObject jsonObj = new JSONObject();
				JSONArray conArr = responseJson.getJSONArray("con_arr");
				// 계약금융기관코드(저장되어있는 데이터 비교)
				String comCode = Integer.toString(userResult.getComCode());
				for(int i=0; i<conArr.length(); i++){
					jsonObj = conArr.getJSONObject(i);
					String loanType = jsonObj.getString("loan_type");
					String finCode = jsonObj.getString("fin_code");
					// 등록시 계약김융기관코드 및 대출모집인 유형코드(상품코드)가 동일한 정보만 저장(계약일, 대출모집인휴대폰번호 등등 추가가능)
					if(loanType.equals(prdCheck) && finCode.equals(comCode)) {
						conNum = jsonObj.getString("con_num");
						break;
					}
				}
				
				UserDomain updateDomain = new UserDomain();
				updateDomain.setMasterSeq(userResult.getMasterSeq());
				updateDomain.setPlRegistNo(lcNum);
				updateDomain.setConNum(conNum);
				updateDomain.setPlRegStat("3");
				kfbApiRepository.updateKfbApiByUserInfo(updateDomain);
			}
		}
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	// 승인완료인 건 결제정보가 있을시 자격취득으로 변경
	@PostMapping(value="/api/apiApplyReg")
	public ResponseEntity<ResponseMsg> apiApplyReg(KfbApiDomain kfbApiDomain) throws IOException{
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		UserDomain userSearchDomain = new UserDomain();
		List<UserDomain> userList = userRepo.selectApiApplyList(userSearchDomain);
		if(userList.size() > 0) {
			for(UserDomain tmp : userList) {
				// 금융상품 3, 6번 제외
				String prdCheck = tmp.getPlProduct();
				String lcNum = "";
				String conNum = "";
				// 가등록에서 본등록시 등록번호 발급
				
				UserDomain userDomain = new UserDomain();
				if("01".equals(prdCheck) || "05".equals(prdCheck)) {
					
					// 2021-06-25 은행연합회 API 통신 - 등록
					String apiKey = kfbApiRepository.selectKfbApiKey(kfbApiDomain);
					JSONObject jsonParam = new JSONObject();
					String plClass = tmp.getPlClass();
					if("1".equals(plClass)) {
						jsonParam.put("pre_lc_num", tmp.getPreLcNum());
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanUrl, "POST", plClass, "Y");				
					}else {
						jsonParam.put("pre_corp_lc_num", tmp.getPreLcNum());
						responseMsg = kfbApiService.commonKfbApi(apiKey, jsonParam, KfbApiService.ApiDomain+KfbApiService.LoanCorpUrl, "POST", plClass, "Y");
					}
					
					if("success".equals(responseMsg.getCode())) {
						JSONObject responseJson = new JSONObject(responseMsg.getData().toString());
						if("1".equals(tmp.getPlClass())) {
							lcNum = responseJson.getString("lc_num");
						}else {
							lcNum = responseJson.getString("corp_lc_num");
						}
						JSONObject jsonObj = new JSONObject();
						JSONArray conArr = responseJson.getJSONArray("con_arr");
						// 계약금융기관코드(저장되어있는 데이터 비교)
						String comCode = Integer.toString(tmp.getComCode());
						for(int i=0; i<conArr.length(); i++){
							jsonObj = conArr.getJSONObject(i);
							String loanType = jsonObj.getString("loan_type");
							String finCode = jsonObj.getString("fin_code");
							// 등록시 계약김융기관코드 및 대출모집인 유형코드(상품코드)가 동일한 정보만 저장(계약일, 대출모집인휴대폰번호 등등 추가가능)
							if(loanType.equals(prdCheck) && finCode.equals(comCode)) {
								conNum = jsonObj.getString("con_num");
								break;
							}
						}
						
						userDomain.setMasterSeq(tmp.getMasterSeq());
						userDomain.setPlRegistNo(lcNum);
						userDomain.setConNum(conNum);
						userDomain.setPlRegStat("3");
						kfbApiRepository.updateKfbApiByUserInfo(userDomain);
					}
				}				
			}
		}
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
}