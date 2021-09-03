<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

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
						<li id="menyPaySearch"><a href="/front/paySearch/payResultSearch">등록수수료 결제내역 조회</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<div id="contents">