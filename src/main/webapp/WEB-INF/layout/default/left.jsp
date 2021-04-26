<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<div class="gnb">
	<ul>
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM')">
			<li>
				<a href="/system/templete/templete">모집인 조회 및 변경</a>
			</li>
		</sec:authorize>
		<li>
			<a href="/admin/recruit/regList">모집인 승인처리</a>
		</li>
		<li>
			<a href="#">결제 내역</a>
		</li>
		<li>
			<a href="#">타협회 내역 다운로드</a>
		</li>
		<li class="on">
			<a href="/member/company/companyPage">회원사 관리</a>
		</li>
		<li>
			<a href="/system/code/codeList">공통코드관리</a>
		</li>
		
		<li>
			<a href="/system/templete/templetePage">temp</a>
		</li>
		
		
		
		
	</ul>
</div>
		