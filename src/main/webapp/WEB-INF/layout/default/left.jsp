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
	var prevMn		= "${prevMn}";
	if(menuUrl[2] == "mng" || prevMn == "mng"){ 
		$("#menuMng").addClass("on");
	}else if(menuUrl[2] == "user"){
		$("#menuUser").addClass("on");
	}else if(menuUrl[2] == "admin"){
		$("#menuAdmin").addClass("on");
	}else if(menuUrl[2] == "company"){
		$("#menuCompany").addClass("on");
	}else if(menuUrl[2] == "crefia"){
		$("#menuCrefia").addClass("on");
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
	}else if(menuUrl[2] == "users"){
		$("#menuIndvUsers").addClass("on");
	}else if(menuUrl[2] == "corpUsers"){
		$("#menuCorpUsers").addClass("on");
	}else if(menuUrl[2] == "inactive"){
		$("#menuInactive").addClass("on");
	}else if(menuUrl[2] == "newUser"){
		$("#menuNewUser").addClass("on");
	}else if(menuUrl[2] == "newConfirm"){
		$("#menuNewConfirm").addClass("on");
	}else if(menuUrl[2] == "newApply"){
		$("#menuNewApply").addClass("on");
	}else if(menuUrl[2] == "newRecruit"){
		$("#menuNewRecruit").addClass("on");
	}else if(menuUrl[2] == "updateIndvUsers"){
		$("#menuUpdateIndvUsers").addClass("on");
	}else if(menuUrl[2] == "updateCorpUsers"){
		$("#menuUpdateCorpUsers").addClass("on");
	}
	
	
	
	
}
</script>

<div class="gnb">
	<ul>
		<sec:authorize access="hasAnyRole('MEMBER')">
			<li id="menuConfirm"><a href="/member/confirm/userConfirmPage">모집인 조회 및 변경</a></li>
			<li id="menuUser"><a href="/member/user/userRegPage">모집인 등록</a></li>
			<li id="menuAdmin"><a href="/member/admin/adminPage">관리자 조회 및 변경</a></li>
			<li id="menuNewUser"><a href="/member/newUser/newUserRegPage">(고도화-회원사)모집인 등록신청 확인</a></li>
			<li id="menuNewConfirm"><a href="/member/newConfirm/newConfirmPage">(고도화-회원사)해지신청 및 조회</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN')">
			<li id="menuUser"><a href="/member/user/userRegPage">모집인 등록</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN')">
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
			
			<li id="menuNewApply"><a href="/admin/newApply/newApplyPage">(고도화-협회)모집인 등록신청 확인</a></li>
			<li id="menuNewRecruit"><a href="/admin/newRecruit/newRecruitPage">(고도화-협회)해지신청 및 조회</a></li>
			
			<li id="menuIndvUsers"><a href="/admin/users/indvUsersPage">(고도화)개인회원관리</a></li>
			<li id="menuCorpUsers"><a href="/admin/corpUsers/corpUsersPage">(고도화)법인회원 및 법인관리</a></li>
			
			
			<li id="menuInactive"><a href="/admin/inactive/inactivePage">(고도화)휴면회원관리</a></li>
			
			<li id="menuUpdateIndvUsers"><a href="/admin/updateIndvUsers/updateIndvUsersPage">(고도화)개인회원 정보변경관리</a></li>
			<li id="menuUpdateCorpUsers"><a href="/admin/updateCorpUsers/updateCorpUsersPage">(고도화)법인회원 정보변경관리</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM', 'MEMBER')">
			<li id="menuBoard"><a href="/common/board/noticePage">공지사항</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('SYSTEM')">
			<li id="menuConfirm"><a href="/member/confirm/userConfirmPage">(회원사)모집인 조회 및 변경</a></li>
			<li id="menuUser"><a href="/member/user/userRegPage">모집인 등록</a></li>
			<li id="menuAdmin"><a href="/member/admin/adminPage">관리자 조회 및 변경</a></li>
			
			
			<li id="menuRecruit"><a href="/admin/recruit/recruitPage">(협회)모집인 조회 및 변경</a></li>
			<li id="menuApply"><a href="/admin/apply/applyPage">모집인 승인처리</a></li>
			<li id="menuCompany"><a href="/admin/company/companyCodePage">회원사 관리</a></li> 
			<li id="menuMng"><a href="/admin/mng/companyPage">회원사 담당자 관리</a></li>
			<li id="menuCrefia"><a href="/admin/crefia/crefiaPage">협회 관리자 관리</a></li>
			<li id="menuCrefiaWork"><a href="/admin/crefiaWork/crefiaWorkPage">협회 관리자 업무분장</a></li>
			<li id="menuCorp"><a href="/admin/corp/corpPage">법인 관리</a></li>
			<li id="menuEdu"><a href="/admin/edu/eduPage">교육이수번호 조회</a></li>
			<li id="menuStats"><a href="javascript:void(0);">통계</a></li>
			
			<li id="menuCode"><a href="/system/code/codePage">코드관리</a></li>
			<li id="menuApi"><a href="/system/api/apiPage">API관리</a></li>
			<li id="menuIndvUsers"><a href="/admin/users/indvUsersPage">(고도화)개인회원관리</a></li>
			<li id="menuCorpUsers"><a href="/admin/corpUsers/corpUsersPage">(고도화)법인회원 및 법인관리</a></li>
			
			<li id="menuInactive"><a href="/admin/inactive/inactivePage">(고도화)휴면회원관리</a></li>
			
			
			<li id="menuNewUser"><a href="/member/newUser/newUserRegPage">(고도화-회원사)모집인 등록신청 확인</a></li>
			<li id="menuNewConfirm"><a href="/member/newConfirm/newConfirmPage">(고도화-회원사)해지신청 및 조회</a></li>
			
			
			<li id="menuNewApply"><a href="/admin/newApply/newApplyPage">(고도화-협회)모집인 등록 승인처리</a></li>
			<li id="menuNewRecruit"><a href="/admin/newRecruit/newRecruitPage">(고도화-협회)모집인 조회 및 해지</a></li>
			
			<li id="menuUpdateIndvUsers"><a href="/admin/updateIndvUsers/updateIndvUsersPage">(고도화)개인회원 정보변경관리</a></li>
			<li id="menuUpdateCorpUsers"><a href="/admin/updateCorpUsers/updateCorpUsersPage">(고도화)법인회원 정보변경관리</a></li>
		</sec:authorize>
	</ul>
</div>

