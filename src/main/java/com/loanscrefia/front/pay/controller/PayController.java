package com.loanscrefia.front.pay.controller;

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
import com.loanscrefia.front.pay.domain.PayDomain;
import com.loanscrefia.front.pay.service.PayService;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

@Controller
@RequestMapping(value="/front/pay")
public class PayController {
	
	@Autowired private SearchService searchService;
	@Autowired private PayService payService;
	
	//모집인 결제 > 모집인 조회 페이지
	@GetMapping(value="/payUserSearchPage")
	public String payUserSearchPage() {
		return CosntPage.FoPayPage+"/payUserSearch";
	}
	
	//모집인 결제 > 모집인 조회 : 개인
	@PostMapping(value="/payIndvUserSearch")
	public ResponseEntity<ResponseMsg> payIndvUserSearch(SearchDomain searchDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "");
		responseMsg.setData(searchService.selectIndvUserInfo(searchDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//모집인 결제 > 모집인 조회 : 법인
	@PostMapping(value="/payCorpUserSearch")
	public ResponseEntity<ResponseMsg> payCorpUserSearch(SearchDomain searchDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "");
		responseMsg.setData(searchService.selectCorpUserInfo(searchDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//모집인 결제 > 모집인 조회 결과 페이지
	@PostMapping(value="/payUserSearchResult")
	public ModelAndView payUserSearchResult(SearchDomain searchDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.FoPayPage+"/payUserSearchResult");
		mv.addObject("searchUserInfo", searchService.selectSearchUserInfo(searchDomain));
		return mv;
	}
	
	//[allAt]결제 인증정보 수신 페이지
	@PostMapping(value="/allatReceive")
	public String allatReceive() {
		return CosntPage.FoPayPage+"/allat_receive";
	}
	
	//[allAt]결제 승인요청 및 결과수신 페이지
	@PostMapping(value="/allatApproval")
	public String allatApproval() {
		return CosntPage.FoPayPage+"/allat_approval";
	}
	
	//결제완료 페이지
	@PostMapping(value="/payResult")
	public ModelAndView payResult(SearchDomain searchDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.FoPayPage+"/payResult");
		System.out.println("PayController > payResult() :::::::: "+searchDomain.getMasterSeq()); //[추후 삭제]
		mv.addObject("masterSeq",searchDomain.getMasterSeq()); //[추후 삭제]
		mv.addObject("payResultInfo",searchService.selectSearchUserInfo(searchDomain));
		return mv;
	}
	
	//등록증 다운로드 페이지
	@GetMapping(value="/certiCardDownloadPopup")
	public ModelAndView certiCardDownloadPopup(SearchDomain searchDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.Popup+"/certiCardDownloadPopup");
		return mv;
	}
	
	//결제 테스트용[추후 삭제]
	@PostMapping(value="/payTest")
	public ResponseEntity<ResponseMsg> payTest(PayDomain payDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(payService.insertPayResult(payDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}