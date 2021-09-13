package com.loanscrefia.front.pay.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loanscrefia.admin.apply.domain.ApplyDomain;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.config.string.CosntPage;
import com.loanscrefia.front.pay.domain.PayDomain;
import com.loanscrefia.front.pay.service.PayService;
import com.loanscrefia.front.search.domain.SearchDomain;
import com.loanscrefia.front.search.service.SearchService;

@Controller
@RequestMapping(value="/front")
public class PayController {
	
	@Autowired private SearchService searchService;
	@Autowired private PayService payService;
	
	//모집인 결제 > 모집인 조회 페이지
	@GetMapping(value="/pay/payUserSearchPage")
	public String payUserSearchPage() {
		return CosntPage.FoPayPage+"/payUserSearch";
	}
	
	//모집인 결제 > 모집인 조회 : 개인
	@PostMapping(value="/pay/payIndvUserSearch")
	public ResponseEntity<ResponseMsg> payIndvUserSearch(SearchDomain searchDomain) {
		ResponseMsg responseMsg = searchService.selectPayIndvUserInfo(searchDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//모집인 결제 > 모집인 조회 : 법인
	@PostMapping(value="/pay/payCorpUserSearch")
	public ResponseEntity<ResponseMsg> payCorpUserSearch(SearchDomain searchDomain) {
		ResponseMsg responseMsg = searchService.selectPayCorpUserInfo(searchDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//모집인 결제 > 모집인 조회 결과 페이지
	@PostMapping(value="/pay/payUserSearchResult")
	public ModelAndView payUserSearchResult(SearchDomain searchDomain) {
		ModelAndView mv 			= new ModelAndView(CosntPage.FoPayPage+"/payUserSearchResult");
		searchDomain.setPlRegStat("2"); //모집인 상태가 승인완료인 것
		SearchDomain searchUserInfo = searchService.selectSearchUserInfo(searchDomain);
		mv.addObject("searchUserInfo",searchUserInfo);
		return mv;
	}
	
	//[allAt]결제 인증정보 수신 페이지
	@RequestMapping(value="/pay/allatReceive")
	public String allatReceive() {
		System.out.println(":::::::::::::::: payController >> allatReceive() ::::::::::::::::");
		return CosntPage.FoPayPage+"/allat_receive";
	}
	
	//[allAt]결제 승인요청 및 결과수신 페이지
	@PostMapping(value="/pay/allatApproval")
	public String allatApproval() {
		return CosntPage.FoPayPage+"/allat_approval";
	}
	
	//결제완료 페이지
	@PostMapping(value="/pay/payResult")
	public ModelAndView payResult(PayDomain payDomain, SearchDomain searchDomain) {
		boolean result = payService.insertPayResult(payDomain);
		System.out.println("PayController > payResult() >> result :::::::: "+result); //[결제 테스트 후 추후 삭제]
		
		//결제완료 페이지 이동
		ModelAndView mv 			= new ModelAndView(CosntPage.FoPayPage+"/payResult");
		SearchDomain payResultInfo 	= new SearchDomain();
		
		if(result) {
			searchDomain.setPlRegStat("3"); //모집인 상태가 자격취득인 것
			payResultInfo 	= searchService.selectSearchUserInfo(searchDomain);
		}else {
			searchDomain.setPlRegStat("5"); //모집인 상태가 결제완료인 것
			payResultInfo 	= searchService.selectSearchUserInfo(searchDomain);
		}
		payService.updatePayResultApi(payResultInfo);
		payResultInfo.setAmt(payDomain.getAmt());
		mv.addObject("payResultInfo",payResultInfo);
		return mv;
	}
	
	//등록증 다운로드 페이지
	@GetMapping(value="/pay/certiCardDownloadPopup")
	public ModelAndView certiCardDownloadPopup(SearchDomain searchDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.Popup+"/certiCardDownloadPopup");
		return mv;
	}
	
	//결제 테스트용[추후 삭제]
	@PostMapping(value="/pay/payTest")
	public ResponseEntity<ResponseMsg> payTest(PayDomain payDomain) {
		ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK ,null);
		responseMsg.setData(payService.insertPayResult(payDomain));
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	
	
	
	
	//2021-09-03 모집인결제조회 페이지
	@GetMapping(value="/paySearch/payResultSearch")
	public String resultPayUserSearchPage() {
		return CosntPage.FoPayPage+"/payResultSearch";
	}
	
	//2021-09-03 모집인결제조회 : 개인
	@PostMapping(value="/paySearch/payResultIndvSearch")
    public ResponseEntity<ResponseMsg> payResultIndvSearch(SearchDomain searchDomain) {
		ResponseMsg responseMsg = searchService.getPayResultIndvSearch(searchDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
    }
	
	
	//모집인 결제 > 모집인 조회 : 법인
	@PostMapping(value="/paySearch/payResultCorpSearch")
	public ResponseEntity<ResponseMsg> payResultCorpSearch(SearchDomain searchDomain) {
		ResponseMsg responseMsg = searchService.getPayResultCorpSearch(searchDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	
	//모집인 결제 > 조회 결과 페이지
	@PostMapping(value="/paySearch/payResultSearchResult")
	public ModelAndView payResultSearchResult(SearchDomain searchDomain) {
		ModelAndView mv 			= new ModelAndView(CosntPage.FoPayPage+"/payResultSearchResult");
		SearchDomain payResultInfo = searchService.getPayResultSearchResult(searchDomain);
		mv.addObject("payResultInfo",payResultInfo);
		return mv;
	}
	
}