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
  
  // 2021-07-28 로딩바 작업 진행 예정
  out.println("결제 진행중입니다.");
  
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
	
	//(2)결제 완료 화면 이동 : masterSeq 들고 이동해야함
	pageContext.setAttribute("sOrderNo", sOrderNo);
	pageContext.setAttribute("masterSeq", masterSeq);
	pageContext.setAttribute("sPayType", sPayType);
	pageContext.setAttribute("sSeqNo", sSeqNo);
	pageContext.setAttribute("sApprovalNo", sApprovalNo);
	pageContext.setAttribute("id", id);
	pageContext.setAttribute("name", name);
	pageContext.setAttribute("sSellMm", sSellMm);
	pageContext.setAttribute("sAmt", sAmt);
	
	out.println("<script>");
	out.println("function pageLoad() {$('#goPayResultPage').submit();}");
	out.println("</script>");
  }else{
    // reply_cd 가 "0000" 아닐때는 에러 (자세한 내용은 매뉴얼참조)
    // reply_msg 가 실패에 대한 메세지
    out.println("결과코드			: " + sReplyCd  + "<br>");
    out.println("결과메세지			: " + sReplyMsg + "<br>");
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

<form id="goPayResultPage" action="/front/pay/payResult" method="post">
	<input type="hidden" name="orderNo" value="${pageScope.sOrderNo }"/>
	<input type="hidden" name="masterSeq" value="${pageScope.masterSeq }"/>
	<input type="hidden" name="payType" value="${pageScope.sPayType }"/>
	<input type="hidden" name="seqNo" value="${pageScope.sSeqNo }"/>
	<input type="hidden" name="approvalNo" value="${pageScope.sApprovalNo }"/>
	<input type="hidden" name="id" value="${pageScope.id }"/>
	<input type="hidden" name="name" value="${pageScope.name }"/>
	<input type="hidden" name="sellMm" value="${pageScope.sSellMm }"/>
	<input type="hidden" name="amt" value="${pageScope.sAmt }"/>
</form>




