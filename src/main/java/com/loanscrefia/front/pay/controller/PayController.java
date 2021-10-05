package com.loanscrefia.front.pay.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.loanscrefia.util.pay.AllatUtil;

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
	public ResponseEntity<ResponseMsg> payIndvUserSearch(SearchDomain searchDomain) throws IOException {
		ResponseMsg responseMsg = searchService.selectPayIndvUserInfo(searchDomain);
		return new ResponseEntity<ResponseMsg>(responseMsg ,HttpStatus.OK);
	}
	
	//모집인 결제 > 모집인 조회 : 법인
	@PostMapping(value="/pay/payCorpUserSearch")
	public ResponseEntity<ResponseMsg> payCorpUserSearch(SearchDomain searchDomain) throws IOException {
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
	public ModelAndView allatApproval(HttpServletRequest request) throws IOException {
		
		System.out.println("PayController > allatApproval() >> result :::::::: 결과값 작업진행 :::::::::::::");
		
		//결제완료 페이지 이동
		ModelAndView mv 			= new ModelAndView(CosntPage.FoPayPage+"/payResult");
		SearchDomain payResultInfo 	= new SearchDomain();
		PayDomain payDomain 		= new PayDomain();
		
		String sCrossKey = "ec62e31d3dac1119c934391187e3160b";		//설정필요 [사이트 참조 - http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#shop]
		String sShopId   = "crefia";								//설정필요
		String sAmount   = request.getParameter("allat_amt");		//결제 금액을 다시 계산해서 만들어야 함(해킹방지)  ( session, DB 사용 )
		
		String sEncData  = request.getParameter("allat_enc_data");
		String strReq = "";
		  
		// 요청 데이터 설정
		//----------------------
		strReq  ="allat_shop_id="   +sShopId;
		strReq +="&allat_amt="      +sAmount;
		strReq +="&allat_enc_data=" +sEncData;
		strReq +="&allat_cross_key="+sCrossKey;
		
		// 올앳 결제 서버와 통신  : AllatUtil.approvalReq->통신함수, HashMap->결과값
		//-----------------------------------------------------------------------------
		
		AllatUtil util = new AllatUtil();
		HashMap hm     = null;
		//hm = util.approvalReq(strReq, "NOSSL");					//설정필요 : SSL(443 포트) 통신 불가 시 SSL을 NOSSL로 변경(NOSSL 로 변경 시 80 포트 이용)
		hm = util.approvalReq(strReq, "SSL");
		    
		// 결제 결과 값 확인
		//------------------
		String sReplyCd     = (String)hm.get("reply_cd");
		String sReplyMsg    = (String)hm.get("reply_msg");

		/* 결과값 처리
		--------------------------------------------------------------------------
		   결과 값이 '0000'이면 정상임. 단, allat_test_yn=Y 일경우 '0001'이 정상임.
		   실제 결제   : allat_test_yn=N 일 경우 reply_cd=0000 이면 정상
		   테스트 결제 : allat_test_yn=Y 일 경우 reply_cd=0001 이면 정상
		--------------------------------------------------------------------------*/
		if( sReplyCd.equals("0000") ){
			// reply_cd "0000" 일때만 성공
			String sOrderNo        = (String)hm.get("order_no");
			String sAmt            = (String)hm.get("amt");
			String sPayType        = (String)hm.get("pay_type");
			//String sApprovalYmdHms = (String)hm.get("approval_ymdhms");
			String sSeqNo          = (String)hm.get("seq_no");
			String sApprovalNo     = (String)hm.get("approval_no");
			String sCardId         = (String)hm.get("card_id");
			String sCardNm         = (String)hm.get("card_nm");
			String sSellMm         = (String)hm.get("sell_mm");
		  	//String sZerofeeYn      = (String)hm.get("zerofee_yn");
		  	//String sCertYn         = (String)hm.get("cert_yn");
		    //String sContractYn     = (String)hm.get("contract_yn");
		    //String sSaveAmt        = (String)hm.get("save_amt");
		    String sBankId         = (String)hm.get("bank_id");
		    String sBankNm         = (String)hm.get("bank_nm");
		    //String sCashBillNo     = (String)hm.get("cash_bill_no");
		    //String sCashApprovalNo = (String)hm.get("cash_approval_no");
		    //String sEscrowYn       = (String)hm.get("escrow_yn");
		    //String sAccountNo      = (String)hm.get("account_no");
		    //String sAccountNm      = (String)hm.get("account_nm");
		    //String sIncomeAccNm    = (String)hm.get("income_account_nm");
		    //String sIncomeLimitYmd = (String)hm.get("income_limit_ymd");
		    //String sIncomeExpectYmd= (String)hm.get("income_expect_ymd");
		    //String sCashYn         = (String)hm.get("cash_yn");
		    //String sHpId           = (String)hm.get("hp_id");
		    //String sTicketId       = (String)hm.get("ticket_id");
		    //String sTicketPayType  = (String)hm.get("ticket_pay_type");
		    //String sTicketNm       = (String)hm.get("ticket_nm");
		    //String sPointAmt       = (String)hm.get("point_amt");
			
			//----------------------[2021.05.21 추가 : S]----------------------
			//(1)결제정보 저장
			int masterSeq 	= Integer.parseInt(request.getParameter("masterSeq"));
			String id 		= "";
			String name 	= "";
			
			if(sCardId != null && !sCardId.equals("")){
				//카드
				id 		= sCardId;
				name 	= sCardNm;
			}else if(sBankId != null && !sBankId.equals("")){
				//계좌이체
				id 		= sBankId;
				name 	= sBankNm;
			}
			
			payDomain.setOrderNo(sOrderNo);
			payDomain.setMasterSeq(masterSeq);
			payDomain.setPayType(sPayType);
			payDomain.setSeqNo(sSeqNo);
			payDomain.setApprovalNo(sApprovalNo);
			payDomain.setId(id);
			payDomain.setName(name);
			payDomain.setSellMm(sSellMm);
			payDomain.setAmt(Integer.parseInt(sAmt));
			
			boolean result = payService.insertPayResult(payDomain);
			SearchDomain searchDomain = new SearchDomain();
			searchDomain.setMasterSeq(masterSeq);
			if(result) {
				searchDomain.setPlRegStat("3"); //모집인 상태가 자격취득인 것
				payResultInfo 	= searchService.selectSearchUserInfo(searchDomain);
			}else {
				searchDomain.setPlRegStat("5"); //모집인 상태가 결제완료인 것
				payResultInfo 	= searchService.selectSearchUserInfo(searchDomain);
			}
			payService.updatePayResultApi(payResultInfo);
			
		}
		
		mv.addObject("resultCode", sReplyCd);
		mv.addObject("resultMessage", sReplyMsg);
		
		payResultInfo.setAmt(payDomain.getAmt());
		mv.addObject("payResultInfo",payResultInfo);
		return mv;
	}
	
	//결제완료 페이지
	/*
	 * @PostMapping(value="/pay/payResult") public ModelAndView payResult(PayDomain
	 * payDomain, SearchDomain searchDomain) {
	 * 
	 * System.out.println("PayController > payResult() >> result :::::::: "+result);
	 * //[결제 테스트 후 추후 삭제]
	 * 
	 * //결제완료 페이지 이동 ModelAndView mv = new
	 * ModelAndView(CosntPage.FoPayPage+"/payResult"); SearchDomain payResultInfo =
	 * new SearchDomain();
	 * 
	 * if(result) { searchDomain.setPlRegStat("3"); //모집인 상태가 자격취득인 것 payResultInfo
	 * = searchService.selectSearchUserInfo(searchDomain); }else {
	 * searchDomain.setPlRegStat("5"); //모집인 상태가 결제완료인 것 payResultInfo =
	 * searchService.selectSearchUserInfo(searchDomain); }
	 * payService.updatePayResultApi(payResultInfo);
	 * payResultInfo.setAmt(payDomain.getAmt());
	 * mv.addObject("payResultInfo",payResultInfo); return mv; }
	 */
	
	//등록증 다운로드 페이지
	@GetMapping(value="/pay/certiCardDownloadPopup")
	public ModelAndView certiCardDownloadPopup(SearchDomain searchDomain) {
		ModelAndView mv = new ModelAndView(CosntPage.Popup+"/certiCardDownloadPopup");
		return mv;
	}
	
	//결제 테스트용[추후 삭제]
	@PostMapping(value="/pay/payTest")
	public ResponseEntity<ResponseMsg> payTest(PayDomain payDomain) throws IOException {
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