<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- <script language=JavaScript charset="euc-kr" src="https://tx.allatpay.com/common/AllatPayLayer.js"></script> -->
<script language=JavaScript charset="euc-kr" src="https://tx.allatpay.com/common/NonAllatPayRE.js"></script>

<!-- 운영 -->
<c:set var="prdReceiveUrl" value="https://loanagent.crefia.or.kr/front/pay/allatReceive"></c:set>
<!-- 개발 -->
<c:set var="devReceiveUrl" value="http://loanagentd.crefia.or.kr/front/pay/allatReceive"></c:set>

<script language=Javascript>
//결제페이지 호출
function ftn_approval(dfm) {
	if(confirm("결제 후 환불은 불가능 합니다.\n계속 진행하시겠습니까?")){
		AllatPay_Approval(dfm);
		//결제창 자동종료 체크 시작
		AllatPay_Closechk_Start();
	}
}

//결과값 반환(receive 페이지에서 호출)
function result_submit(result_cd,result_msg,enc_data) {
	//결제창 자동종료 체크 종료
	AllatPay_Closechk_End();
	
	alert("result_cd >> "+result_cd);
	alert("result_msg >> "+result_msg);
	
	if(result_cd != '0000'){
		window.setTimeout(function(){alert(result_cd + " : " + result_msg);},1000);
	}else{
		//결제성공하면 인증결과값을 allat_enc_data 필드에 설정하고 After Page(allat_approval.jsp)로 submit
		fm.allat_enc_data.value = enc_data;

		fm.action = "/front/pay/allatApproval"; //"allat_approval.jsp";
		fm.method = "post";
		fm.target = "_self";
		fm.submit();
	}
}
</script>

<form name="fm" id="payFrm" method="post" action="/front/pay/allatApproval"> <!--승인요청 및 결과수신페이지 지정 //-->
	<!-- 필수정보 -->
	<input type="hidden" name="allat_encode_type" value="U"> <!-- 인코딩 -->
	<input type="hidden" name="allat_shop_id" value="crefia" maxlength="20"> <!-- Allat에서 발급한 고유 상점 ID -->
	<input type="hidden" name="allat_order_no" value="${searchUserInfo.masterSeq }" maxlength="70"> <!-- 쇼핑몰에서 사용하는 고유 주문번호 : 공백,작은따옴표('),큰따옴표(") 사용 불가 -->
	<input type="hidden" name="allat_amt" id="allat_amt" value="20000" maxlength="10"> <!-- 총 결제금액 : 숫자(0~9)만 사용가능 -->
	<input type="hidden" name="allat_pmember_id" value="1" maxlength="20"> <!-- 쇼핑몰의 회원ID : 공백,작은따옴표('),큰따옴표(") 사용 불가 -->
	<input type="hidden" name="allat_product_cd" value="결제상품코드" maxlength="1000"> <!-- 여러 상품의 경우 구분자 이용, 구분자('||':파이프 2개) : 공백,작은따옴표('),큰따옴표(") 사용 불가 -->
	<input type="hidden" name="allat_product_nm" value="결제상품명" maxlength="1000"> <!-- 여러 상품의 경우 구분자 이용, 구분자('||':파이프 2개) -->
	<input type="hidden" name="allat_buyer_nm" value="테스트" maxlength="20"> <!-- 결제자성명 -->
	<input type="hidden" name="allat_recp_nm" value="여신금융협회" maxlength="20"> <!-- 수취인성명 -->
	<input type="hidden" name="allat_recp_addr" value="서울" maxlength="120"> <!-- 수취인주소 -->
	<input type="hidden" name="shop_receive_url" value="https://loanagent.crefia.or.kr/front/pay/allatReceive"> <!-- 인증정보수신URL : Full URL 입력 -->
	<input type="hidden" name="allat_enc_data" value=""> <!-- 주문정보암호화필드 : 값은 자동으로 설정됨 -->
	<!-- 옵션정보 -->
	<input type="hidden" name="allat_card_yn" value="Y" maxlength="1"> <!-- 신용카드 결제 사용 여부 : 사용(Y),사용하지 않음(N) - Default : 올앳과 계약된 사용여부 -->
	<input type="hidden" name="allat_bank_yn" value="Y" maxlength="1"> <!-- 계좌이체 결제 사용 여부 : 사용(Y),사용하지 않음(N) - Default : 올앳과 계약된 사용여부 -->
	<input type="hidden" name="allat_vbank_yn" value="N" maxlength="1"> <!-- 무통장(가상계좌) 결제 사용 여부 : 사용(Y),사용하지 않음(N) - Default : 올앳과 계약된 사용여부 -->
	<input type="hidden" name="allat_hp_yn" value="N" maxlength="1"> <!-- 휴대폰 결제 사용 여부 : 사용(Y),사용하지 않음(N) - Default : 올앳과 계약된 사용여부 -->
	<input type="hidden" name="allat_ticket_yn" value="N" maxlength="1"> <!-- 상품권 결제 사용 여부 : 사용(Y),사용하지 않음(N) - Default : 올앳과 계약된 사용여부 -->
	<input type="hidden" name="allat_account_key" value="" maxlength="20"> <!-- 무통장(가상계좌) 인증 Key : 계좌 채번방식이 Key별 방식일 때만 사용함(건별 채번방식일때 무시, 신청한 상점만 이용 가능하며 회원별 고유 값 필요) -->
	<input type="hidden" name="allat_tax_yn" value="Y" maxlength="1"> <!-- 과세여부 : Y(과세), N(비과세) - Default : Y(공급가액과 부가세가 표기되며 현금영수증 사용시 Y로 설정해야 한다.) -->
	<input type="hidden" name="allat_sell_yn" value="Y" maxlength="1"> <!-- 할부 사용 여부 : 할부사용(Y), 할부 사용않함(N) - Default : Y -->
	<input type="hidden" name="allat_zerofee_yn" value="Y" maxlength="1"> <!-- 일반/무이자 할부 사용 여부 : 일반(N), 무이자 할부(Y) - Default :N -->
	<input type="hidden" name="allat_bonus_yn" value="N" maxlength="1"> <!-- 포인트 사용 여부 : 사용(Y), 사용 않음(N) - Default : N -->
	<input type="hidden" name="allat_cash_yn" value="N" maxlength="1"> <!-- 현금 영수증 발급 여부 : 사용(Y), 사용 않음(N) - Default : 올앳과 계약된 사용여부(계좌이체/무통장입금(가상계좌)를 이용하실 때, 상점이 현금영수증 사용업체로 지정 되어 있으면 사용가능) -->
	<input type="hidden" name="allat_email_addr" value="" maxlength="50"> <!-- 결제 정보 수신 E-mail : 에스크로 서비스 사용시에 필수 필드.(결제창에서 E-Mail주소를 넣을 수도 있음) -->
	<input type="hidden" name="allat_test_yn" value="N" maxlength="1"> <!-- 테스트 여부 : 테스트(Y),서비스(N) - Default : N(테스트 결제는 실결제가 나지 않으며 테스트 성공시 결과값은 "0001" 리턴) -->
	<!-- 필요정보 -->
	<input type="hidden" name="masterSeq" value="${searchUserInfo.masterSeq }"/>
</form>

