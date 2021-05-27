<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div class="wrap">
	<div class="header">
		<div class="inner">
			<h1>
				<a href="/main">
					<img src="/static/images/common/top_logo_ov.png" alt="" />
				</a>
			</h1>
			<sec:authorize access="isAuthenticated()">
				<div class="log_menu">
					<span>${member.memberName }님</span>
					<div class="logout">
						<a href="/logout">로그아웃</a>
					</div>
				</div>
			</sec:authorize>
		</div>
	</div>
	
	<div class="container">
