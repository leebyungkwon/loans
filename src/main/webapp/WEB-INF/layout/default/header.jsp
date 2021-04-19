<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
	<div class="k_header">
		<div class="gnb_banner_app">
			<a class="gnb_banner">
				<div>
					<span>테스트 페이지 입니다.	</span> 
					<span class="bt_down">테스트버튼</span>
				</div>
			</a>
		</div>
		<div class="header_wrap">
			<div class="k_logo">
				<a class="" href="/login">K_PROJECT</a>
			</div>
			<ul class="menu-box ng-star-inserted">
				<sec:authorize access="isAuthenticated()">
				<li>
					<a href="/"> 
					<span class="sp ico_my"></span> 
					<strong class="txt">MY PAGE</strong>
					</a>
				</li>
				<li>
					<a href="/logout"> 
						<span class="sp ico_login"></span>
						<strong class="txt">로그아웃</strong>
					</a>
				</li>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
				<li>
					<a href="/login"> 
						<span class="sp ico_login"></span>
						<strong class="txt">로그인</strong>
					</a>
				</li>
				<li>
					<a href="/signup"> 
						<span class="sp ico_login"></span>
						<strong class="txt">회원가입</strong>
					</a>
				</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
