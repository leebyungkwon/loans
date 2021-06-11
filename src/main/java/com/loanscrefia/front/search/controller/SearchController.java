package com.loanscrefia.front.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

@Controller
@RequestMapping(value="/front/search")
public class SearchController {

	@Autowired private SearchService searchService;
	
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
	@PostMapping(value="/userSearchResult")
	public ModelAndView userSearchResult(SearchDomain searchDoamin){
		ModelAndView mv = new ModelAndView(CosntPage.FoSearchPage+"/userSearchResult");
		mv.addObject("result", searchService.userSearchResult(searchDoamin));
        return mv;
	}
}