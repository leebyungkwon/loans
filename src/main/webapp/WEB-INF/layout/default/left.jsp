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
	if(menuUrl[2] == "user"){
		$("#menuUser").addClass("on");
	}else if("${sessionScope.member.creYn}" == "N" && menuUrl[2] == "admin"){
		$("#menuAdmin").addClass("on");
	}else if(menuUrl[2] == "company"){
		$("#menuCompany").addClass("on");
	}else if(menuUrl[2] == "crefia"){
		$("#menuCrefia").addClass("on");
	}else if(menuUrl[2] == "mng" || ("${sessionScope.member.creYn}" == "Y" && menuUrl[2] == "admin")){
		$("#menuMng").addClass("on");
	}else if(menuUrl[2] == "board"){
		$("#menuBoard").addClass("on");
	}else if(menuUrl[2] == "code"){
		$("#menuCode").addClass("on");
	}else if(menuUrl[2] == "confirm"){
		$("#menuConfirm").addClass("on");
	}else if(menuUrl[2] == "corp"){
		$("#menuCorp").addClass("on");
	}else if(menuUrl[2] == "crefiaWork"){
		$("#menuCrefiaWork").addClass("on");
	}else if(menuUrl[2] == "recruit"){
		$("#menuRecruit").addClass("on");
	}else if(menuUrl[2] == "apply"){
		$("#menuApply").addClass("on");
	}else if(menuUrl[2] == "edu"){
		$("#menuEdu").addClass("on");
	}else if(menuUrl[2] == "apiList"){
		$("#menuApi").addClass("on");
	}
}
</script>



<div class="gnb">
	<ul>
		<sec:authorize access="hasAnyRole('MEMBER', 'SYSTEM')">
			<li id="menuConfirm"><a href="/member/confirm/userConfirmPage">모집인 조회 및 변경</a></li>
			<li id="menuUser"><a href="/member/user/userRegPage">모집인 등록</a></li>
			<li id="menuAdmin"><a href="/member/admin/adminPage">관리자 조회 및 변경</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN')">
			<li id="menuUser"><a href="/member/user/userRegPage">모집인 등록</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM')">
			<li id="menuRecruit"><a href="/admin/recruit/recruitPage">모집인 조회 및 변경</a></li>
			<li id="menuApply"><a href="/admin/apply/applyPage">모집인 승인처리</a></li>
			<!-- <li id=""><a href="javascript:alert('준비중입니다.');">타협회 내역 다운로드</a></li> --> 
			<li id="menuCompany"><a href="/admin/company/companyCodePage">회원사 관리</a></li> 
			<li id="menuMng"><a href="/admin/mng/companyPage">회원사 담당자 관리</a></li>
			<li id="menuCrefia"><a href="/admin/crefia/crefiaPage">협회 관리자 관리</a></li>
			<li id="menuCrefiaWork"><a href="/admin/crefiaWork/crefiaWorkPage">협회 관리자 업무분장</a></li>
			<li id="menuCorp"><a href="/admin/corp/corpPage">법인 관리</a></li>
			<li id="menuEdu"><a href="/admin/edu/eduPage">교육이수번호 조회</a></li>
			<li id="menuStats"><a href="javascript:void(0);">통계</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM', 'MEMBER')">
			<li id="menuBoard"><a href="/common/board/noticePage">공지사항</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('SYSTEM')">
			<li id="menuCode"><a href="/system/code/codePage">코드관리</a></li>
			<li id="menuApi"><a href="/system/api/apiPage">API관리</a></li>
		</sec:authorize>
	</ul>
</div>

