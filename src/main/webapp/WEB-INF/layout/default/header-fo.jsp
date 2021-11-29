<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>


<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %>


<%
String btnShow = "O";

try {
	SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
	Date currentDt = new Date();
	
	Date d1 = dateFormatParser.parse(dateFormatParser.format(currentDt));
	Date d2 = dateFormatParser.parse("2021-11-29 10:00:00");
	
	if(d1.compareTo(d2) >= 0) {
		%>
		<script type="text/javascript">
		location.href = 'https://creditloanagent.crefia.or.kr/';
		</script>
		<%
	}
}catch(ParseException e) {
	btnShow = "X";
}
%>

<script type="text/javascript">
function menuActiveFunction(){
	$(".gnb > ul > li").removeClass("on");
	
	var pathName 	= location.pathname;
	var menuUrl 	= pathName.split("/");
	
	if(menuUrl[2] == "info"){
		$("#menuInfo").addClass("on");
	}else if(menuUrl[2] == "search"){
		$("#menuSearch").addClass("on");
	}else if(menuUrl[2] == "pay"){
		$("#menuPay").addClass("on");
	}else if(menuUrl[2] == "paySearch"){
		$("#menyPaySearch").addClass("on");
	}
}
/* 
function serviceStop(){
	alert("결제서비스 점검중 입니다.");
	return false;	
}
 */
</script>

<div id="wrap"> <!-- class="main" -->
	<div id="header">
		<div class="header_top clfix">
			<ul>
				<li><a href="/front/rules">관련규정</a></li>
				<li><a href="/front/faq">FAQ</a></li>
				<li><a href="/front/privacy">개인정보처리방침</a></li>
			</ul>
		</div>
		<div class="header_bottom">
			<div class="inner clfix">
				<h1 class="logo"><a href="/front/index"><img src="/static/images/common/fo/top_logo.png" alt="여신금융협회"></a></h1>
				<div class="gnb">
					<ul>
						<li id="menuInfo"><a href="/front/info">대출모집인 제도</a></li>
						<li id="menuSearch"><a href="/front/search/userSearchPage">대출모집인 조회</a></li>
						<li id="menuPay"><a href="/front/pay/payUserSearchPage">모집인 결제</a></li>
						<!-- <li id="menuPay"><a href="javascript:void(0)" onclick="serviceStop()">모집인 결제</a></li> -->
						<li id="menyPaySearch"><a href="/front/paySearch/payResultSearch">등록수수료 결제내역 조회</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<div id="contents">