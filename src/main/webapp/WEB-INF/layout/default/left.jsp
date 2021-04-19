<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
		<div class="article_left">
			<!-- 
			<section class="my_my">
			    <div class="my_myin">
			        <h3>이병권</h3>
			        <ul>
			            <li>
			                <a class="btn_heart" href="">
			                    <span class="txt">나의 하트 </span> <em>6</em>
			                </a>
			            </li>
			            <li>
			                <a href="">
			                    <span class="txt">팔로잉 <span>0</span></span>
			                </a>
			            </li>
			        </ul>
			    </div>
			</section>
			 -->
			<section class="k_menu">
	    		<ul class="k_menu_1">
	            	<li>
	                	<h2>
	                        <button type="button" class="btn_cate">템플릿</button>
	                    </h2> 
	                </li>
	            </ul>
	            <h4>게시판</h4>
	            <sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM')">
			    <ul class="k_menu_2">
			        <li><a href="/system/templete/templete">템플릿1</a></li>
			        <li><a href="/system/templete/templete2">템플릿2</a></li>
			        <li><a href="/system/manage/excel">엑셀 템플릿 관리</a></li>
			    </ul>
			    </sec:authorize>
			    <sec:authorize access="hasAnyRole('MEMBER', 'SYSTEM')">
			    <h4>회원사</h4>
			    <ul class="k_menu_2">
			        <li><a href="/bo/recruit/recruitPage">모집인 조회</a></li>
			        <li><a href="/bo/recruit/regList">모집인 등록</a></li>
			    </ul>
			    </sec:authorize>
			    <h4>시스템관리</h4>
			    <ul class="k_menu_2">
			        <li><a href="/bo/board/notice">공지사항</a></li>
			        <li><a href="/bo/code/codeList">공통코드관리</a></li>
			        <li><a href="/bo/admin/adminList">관리자관리</a></li>
			    </ul>
			</section>
 		</div>
 		
		
		