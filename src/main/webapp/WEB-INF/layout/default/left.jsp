<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
	function menuActiveFunction(){
		var pathName = location.pathname;
		$(".gnb > ul > li > a").each(function() {
			var path = $(this).attr("href");
			$(this).parent().removeClass("on");
			if(path == pathName){
				$(this).parent().addClass("on");
			}
		});
	}
</script>



<div class="gnb">
	<ul>
		<!-- class="on" -->
		<sec:authorize access="hasAnyRole('MEMBER', 'SYSTEM')">
		<li>
			<a href="#">모집인 조회 및 변경</a>
		</li>
		<li>
			<a href="/member/user/userRegPage">모집인 등록</a>
		</li>
		<li>
			<a href="/member/admin/adminPage">관리자 조회 및 변경</a>
		</li>
		</sec:authorize>
		
		
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM')">
		<li>
			<a href="#">(협회)모집인 조회 및 변경</a>
		</li>
		<li>
			<a href="#">(협회)모집인 승인처리</a>
		</li>
		<li>
			<a href="#">(협회)결제내역</a>
		</li>
		<li>
			<a href="#">(협회)타협회 내역 다운로드</a>
		</li>
		<li>
			<a href="/admin/company/companyCodePage">(협회)회원사 관리</a>
		</li>
		<li>
			<a href="/admin/crefia/crefiaPage">(협회)관리자 관리</a>
		</li>
		<li>
			<a href="/admin/company/companyPage">(협회)회원사 담당자 조회</a>
		</li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM', 'MEMBER')">
		<li>
			<a href="/common/board/noticePage">공지사항</a>
		</li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('SYSTEM')">
		<li>
			<a href="/system/code/codePage">코드관리</a>
		</li>
		</sec:authorize>
		
	</ul>
</div>
		