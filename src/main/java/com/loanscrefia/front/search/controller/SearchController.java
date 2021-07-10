package com.loanscrefia.front.search.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.recruit.domain.RecruitDomain;
import com.loanscrefia.common.common.domain.KfbApiDomain;
import com.loanscrefia.common.common.repository.KfbApiRepository;
import com.loanscrefia.common.common.service.KfbApiService;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.domain.SearchResultDomain;
import com.loanscrefia.front.search.service.SearchService;


@Controller
@RequestMapping(value="/front/search")
public class SearchController {

	@Autowired private SearchService searchService;
	
	@Autowired 
	private KfbApiService kfbApiService;
	
	@Autowired
	private KfbApiRepository kfbApiRepository;
	
	//대출모집인 조회 페이지
	@GetMapping(value="/userSearchPage")
	public String userSearchPage() {
		return CosntPage.FoSearchPage+"/userSearch";
	}
	
	//대출모집인 조회 : 개인
	@PostMapping(value="/indvUserSearch")
	public ResponseEntity<ResponseMsg> indvUserSearch(SearchDomain searchDomain){
		ResponseMsg responseMsg = searchService.selectIndvUserInfo(searchDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//대출모집인 조회 : 법인
	@PostMapping(value="corpUserSearch")
	public ResponseEntity<ResponseMsg> corpUserSearch(SearchDomain searchDomain){
		ResponseMsg responseMsg = searchService.selectCorpUserInfo(searchDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//대출모집인 조회 결과 페이지
	@GetMapping(value="/userSearchResult")
	public ModelAndView userSearchResult(SearchDomain searchDomain){
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, null, null,  "fail");
		ModelAndView mv = new ModelAndView(CosntPage.FoSearchPage+"/userSearchResult");
		
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
	}
}