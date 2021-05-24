<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 2021.05.21 추가 : S -->
<%@page import="com.loanscrefia.front.pay.service.PayService"%>
<%@page import="com.loanscrefia.front.pay.domain.PayDomain"%>
<%@page import="com.loanscrefia.front.search.service.SearchService"%>
<%@page import="com.loanscrefia.front.search.domain.SearchDomain"%>
<%@page import="org.springframework.web.context.*" %>
<%@page import="org.springframework.web.context.support.*" %>
<!-- 2021.05.21 추가 : E -->

<!-- 올앳관련 함수 Import //-->
<%@page import="java.util.*,java.net.*,com.loanscrefia.util.pay.AllatUtil"%>

<%
  //Request Value Define
  //----------------------

  // Service Code
  String sCrossKey = "ec62e31d3dac1119c934391187e3160b";	//설정필요 [사이트 참조 - http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#shop]
  String sShopId   = "crefia";								//설정필요
  String sAmount   = "1";									//결제 금액을 다시 계산해서 만들어야 함(해킹방지)  ( session, DB 사용 )

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
  
  out.println("::::::::::::::::::::::::::::::::::: new 전 :::::::::::::::::::::::::::::::::::");
  
  AllatUtil util = new AllatUtil();
  HashMap hm     = null;
  hm = util.approvalReq(strReq, "NOSSL");					//설정필요 : SSL(443 포트) 통신 불가 시 SSL을 NOSSL로 변경(NOSSL 로 변경 시 80 포트 이용)
  //hm = util.approvalReq(strReq, "SSL");

  // 결제 결과 값 확인
  //------------------
  String sReplyCd     = (String)hm.get("reply_cd");
  String sReplyMsg    = (String)hm.get("reply_msg");

  out.println("::::::::::::::::::::::::::::::::::: new 후 :::::::::::::::::::::::::::::::::::");
  
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
    String sApprovalYmdHms = (String)hm.get("approval_ymdhms");
    String sSeqNo          = (String)hm.get("seq_no");
    String sApprovalNo     = (String)hm.get("approval_no");
    String sCardId         = (String)hm.get("card_id");
    String sCardNm         = (String)hm.get("card_nm");
    String sSellMm         = (String)hm.get("sell_mm");
    String sZerofeeYn      = (String)hm.get("zerofee_yn");
    String sCertYn         = (String)hm.get("cert_yn");
    String sContractYn     = (String)hm.get("contract_yn");
    String sSaveAmt        = (String)hm.get("save_amt");
    String sBankId         = (String)hm.get("bank_id");
    String sBankNm         = (String)hm.get("bank_nm");
    String sCashBillNo     = (String)hm.get("cash_bill_no");
    String sCashApprovalNo = (String)hm.get("cash_approval_no");
    String sEscrowYn       = (String)hm.get("escrow_yn");
    String sAccountNo      = (String)hm.get("account_no");
    String sAccountNm      = (String)hm.get("account_nm");
    String sIncomeAccNm    = (String)hm.get("income_account_nm");
    String sIncomeLimitYmd = (String)hm.get("income_limit_ymd");
    String sIncomeExpectYmd= (String)hm.get("income_expect_ymd");
    String sCashYn         = (String)hm.get("cash_yn");
    String sHpId           = (String)hm.get("hp_id");
    String sTicketId       = (String)hm.get("ticket_id");
    String sTicketPayType  = (String)hm.get("ticket_pay_type");
    String sTicketNm       = (String)hm.get("ticket_nm");
    String sPointAmt       = (String)hm.get("point_amt");

    out.println("결과코드               : " + sReplyCd          + "<br>");
    out.println("결과메세지             : " + sReplyMsg         + "<br>");
    out.println("주문번호               : " + sOrderNo          + "<br>");
    out.println("승인금액               : " + sAmt              + "<br>");
    out.println("지불수단               : " + sPayType          + "<br>");
    out.println("승인일시               : " + sApprovalYmdHms   + "<br>");
    out.println("거래일련번호           : " + sSeqNo            + "<br>");
    out.println("에스크로 적용 여부     : " + sEscrowYn         + "<br>");
	out.println("==================== 신용 카드 ===================<br>");
    out.println("승인번호               : " + sApprovalNo       + "<br>");
    out.println("카드ID                 : " + sCardId           + "<br>");
    out.println("카드명                 : " + sCardNm           + "<br>");
    out.println("할부개월               : " + sSellMm           + "<br>");
    out.println("무이자여부             : " + sZerofeeYn        + "<br>");   //무이자(Y),일시불(N)
    out.println("인증여부               : " + sCertYn           + "<br>");   //인증(Y),미인증(N)
    out.println("직가맹여부             : " + sContractYn       + "<br>");   //3자가맹점(Y),대표가맹점(N)
    out.println("세이브 결제 금액       : " + sSaveAmt          + "<br>");
    out.println("포인트 결제 금액       : " + sPointAmt         + "<br>");
	out.println("=============== 계좌 이체 / 가상계좌 =============<br>");
    out.println("은행ID                 : " + sBankId           + "<br>");
    out.println("은행명                 : " + sBankNm           + "<br>");
    out.println("현금영수증 일련 번호   : " + sCashBillNo       + "<br>");
    out.println("현금영수증 승인 번호   : " + sCashApprovalNo   + "<br>");
    out.println("===================== 가상계좌 ===================<br>");
    out.println("계좌번호               : " + sAccountNo        + "<br>");
    out.println("입금 계좌명            : " + sIncomeAccNm      + "<br>");
    out.println("입금자명               : " + sAccountNm        + "<br>");
    out.println("입금기한일             : " + sIncomeLimitYmd   + "<br>");
    out.println("입금예정일             : " + sIncomeExpectYmd  + "<br>");
    out.println("현금영수증신청 여부    : " + sCashYn           + "<br>");
    out.println("===================== 휴대폰 결제 ================<br>");
    out.println("이동통신사구분         : " + sHpId             + "<br>");
    out.println("===================== 상품권 결제 ================<br>");
    out.println("상품권ID               :" + sTicketId          + "<br>");
    out.println("상품권 이름            :" + sTicketPayType     + "<br>");
    out.println("결제구분               :" + sTicketNm          + "<br>");

	// 배포본에서는 제외 시킬것 //////////////////////////////////////////
	/* 
	String sPartcancelYn  = (String)hm.get("partcancel_yn");
	String sBCCertNo      = (String)hm.get("bc_cert_no");
	String sCardNo        = (String)hm.get("card_no");
	String sIspFullCardCd = (String)hm.get("isp_full_card_cd");
	String sCardType      = (String)hm.get("card_type");
	String sBankAccountNm = (String)hm.get("bank_account_nm");
    out.println("===================== 배포본제외 ================<br>");
	out.println("신용카드 부분취소가능여부 : " + sPartcancelYn  + "<br>"); 
	out.println("BC인증번호                : " + sBCCertNo      + "<br>");
	out.println("카드번호 Return           : " + sCardNo        + "<br>");
	out.println("ISP 전체 카드코드         : " + sIspFullCardCd + "<br>");
	out.println("카드구분                  : " + sCardType      + "<br>");
	out.println("계좌이체 예금주명         : " + sBankAccountNm + "<br>"); 
	*/
	//////////////////////////////////////////////////////////////////////
	
	
	//----------------------[2021.05.21 추가 : S]----------------------
	/*
	int masterSeq = Integer.parseInt(request.getParameter("masterSeq"));
	
	//(1)결제정보 저장
	ServletContext servletContext 	= getServletContext();
	WebApplicationContext waContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	PayService payService 			= (PayService)waContext.getBean("PayService");
	PayDomain payDomain				= new PayDomain();
	String id 						= "";
	String name 					= "";
	
	if(sPayType.equals("")){
		//카드
		id 		= sCardId;
		name 	= sCardNm;
	}else if(sPayType.equals("")){
		//계좌이체
		id 		= sBankId;
		name 	= sBankNm;
	}
	
	payDomain.setMasterSeq(masterSeq);
	payDomain.setOrderNo(sOrderNo);
	payDomain.setMasterSeq(Integer.parseInt(request.getParameter("masterSeq")));
	payDomain.setPayType(sPayType);
	payDomain.setSeqNo(sSeqNo);
	payDomain.setApprovalNo(sApprovalNo);
	payDomain.setId(id);
	payDomain.setName(name);
	payDomain.setSellMm(sSellMm);
	payDomain.setAmt(Integer.parseInt(sAmt));

	payService.insertPayResult(payDomain);
	
	//(2)모집인 테이블의 결제 컬럼 수정 -> 굳이 마스터 테이블에 결제 관련된 컬럼이 있어야 하나???
	
	//(3)모집인 상태(pl_reg_stat) 자격취득으로 수정
	SearchService userService 		= (SearchService)waContext.getBean("SearchService");
	SearchDomain userDomain 		= new SearchDomain();
	
	userDomain.setMasterSeq(masterSeq);
	userService.updatePlRegStat(userDomain);
	
	//(4)결제 완료 화면 이동 : masterSeq 들고 이동해야함
	out.println("<script>");
	out.println("location.href='';");
	out.println("</script>");
	*/
	//----------------------[2021.05.21 추가 : E]----------------------
	
  }else{
    // reply_cd 가 "0000" 아닐때는 에러 (자세한 내용은 매뉴얼참조)
    // reply_msg 가 실패에 대한 메세지
    out.println("결과코드               : " + sReplyCd  + "<br>");
    out.println("결과메세지             : " + sReplyMsg + "<br>");
  }
%>

<%--
    [신용카드 전표출력 예제]

    결제가 정상적으로 완료되면 아래의 소스를 이용하여, 고객에게 신용카드 전표를 보여줄 수 있습니다.
    전표 출력시 상점아이디와 주문번호를 설정하시기 바랍니다.

    var urls ="http://www.allatpay.com/servlet/AllatBizPop/member/pop_card_receipt.jsp?shop_id=상점아이디&order_no=주문번호";
    window.open(urls,"app","width=410,height=650,scrollbars=0");

    현금영수증 전표 또는 거래확인서 출력에 대한 문의는 올앳페이 사이트의 1:1상담을 이용하시거나
    02) 3788-9990 으로 전화 주시기 바랍니다.

    전표출력 페이지는 저희 올앳 홈페이지의 일부로써, 홈페이지 개편 등의 이유로 인하여 페이지 변경 또는 URL 변경이 있을 수
    있습니다. 홈페이지 개편에 관한 공지가 있을 경우, 전표출력 URL을 확인하시기 바랍니다.
--%>










