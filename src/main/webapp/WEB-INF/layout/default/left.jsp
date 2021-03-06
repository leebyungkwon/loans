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
	}else if(menuUrl[2] == "totList"){
		$("#menuTotList").addClass("on");
	}else if(menuUrl[2] == "stats1"){
		$("#menuStats1").addClass("on");
	}else if(menuUrl[2] == "stats2"){
		$("#menuStats2").addClass("on");
	}else if(menuUrl[2] == "stats3"){
		$("#menuStats3").addClass("on");
	}else if(menuUrl[2] == "stats4"){
		$("#menuStats4").addClass("on");
	}else if(menuUrl[2] == "stats5"){
		$("#menuStats5").addClass("on");
	}else if(menuUrl[2] == "stats6"){
		$("#menuStats6").addClass("on");
	}else if(menuUrl[2] == "stats7"){
		$("#menuStats7").addClass("on");
	}else if(menuUrl[2] == "stats8"){
		$("#menuStats8").addClass("on");
	}else if(menuUrl[2] == "stats9"){
		$("#menuStats9").addClass("on");
	}
}
</script>

<div class="gnb">
	<ul>
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM')">
			<li class="cate1 ">??????</li>
			<li class="cate2">????????? ??????</li>
			<li id="menuUser"><a href="/member/user/userRegPage">????????? ??????</a></li>
			<li id="menuRecruit"><a href="/admin/recruit/recruitPage">????????? ?????? ??? ??????</a></li>
			<li id="menuApply"><a href="/admin/apply/applyPage">????????? ????????????</a></li>
			<!-- <li id=""><a href="javascript:alert('??????????????????.');">????????? ?????? ????????????</a></li> --> 
			
			<li class="cate2">????????? ??????</li>
			<li id="menuCompany"><a href="/admin/company/companyCodePage">????????? ??????</a></li> 
			<li id="menuMng"><a href="/admin/mng/companyPage">????????? ????????? ??????</a></li>
			
			<li class="cate2">????????? ??????</li>
			<li id="menuCrefia"><a href="/admin/crefia/crefiaPage">?????? ????????? ??????</a></li>
			<li id="menuCrefiaWork"><a href="/admin/crefiaWork/crefiaWorkPage">?????? ????????? ????????????</a></li>
			<li id="menuCorp"><a href="/admin/corp/corpPage">?????? ??????</a></li>
			<li id="menuEdu"><a href="/admin/edu/eduPage">?????????????????? ??????</a></li>
		
			<li class="cate2">[?????????]????????? ??????</li>
			<li id="menuNewApply"><a href="/admin/newApply/newApplyPage">????????? ?????? ????????????</a></li>
			<li id="menuNewRecruit"><a href="/admin/newRecruit/newRecruitPage">????????? ?????? ??? ??????</a></li>
			
			<li class="cate2">[?????????]?????? ??????</li>
			<li id="menuIndvUsers"><a href="/admin/users/indvUsersPage">??????????????????</a></li>
			<li id="menuCorpUsers"><a href="/admin/corpUsers/corpUsersPage">??????????????????</a></li>
			<li id="menuInactive"><a href="/admin/inactive/inactivePage">??????????????????</a></li>
			
			<li class="cate2">[?????????]?????? ?????? ????????????</li>		
			<li id="menuUpdateIndvUsers"><a href="/admin/updateIndvUsers/updateIndvUsersPage">???????????? ??????????????????</a></li>
			<li id="menuUpdateCorpUsers"><a href="/admin/updateCorpUsers/updateCorpUsersPage">???????????? ??????????????????</a></li>
			
			<li class="cate2">[?????????]??????</li>
			<li id="menuStats1"><a href="/admin/stats1/statsPage">????????? ?????? ????????? ??????</a></li> 
			<li id="menuStats2"><a href="/admin/stats2/statsPage">????????????</a></li> 
			<li id="menuStats3"><a href="/admin/stats3/statsPage">?????? ??????</a></li> 
			<li id="menuStats4"><a href="/admin/stats4/statsPage">?????????????????????</a></li> 
			<li id="menuStats5"><a href="/admin/stats5/statsPage">??????????????????(?????????)</a></li> 
			<li id="menuStats6"><a href="/admin/stats6/statsPage">??????????????????(????????????)</a></li> 
			<li id="menuStats7"><a href="/admin/stats7/statsPage">??????????????????(?????????)</a></li> 
			<li id="menuStats8"><a href="/admin/stats8/statsPage">??????????????????(????????????)</a></li>
			<li id="menuStats9"><a href="/admin/stats9/statsPage">???????????? ????????????</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('MEMBER', 'SYSTEM')">
			<li class="cate1 ">${member.comCodeNm} <sec:authorize access="hasAnyRole('SYSTEM')">????????????</sec:authorize></li>
			<li class="cate2">????????? ??????</li>
			<li id="menuConfirm"><a href="/member/confirm/userConfirmPage">????????? ?????? ??? ??????</a></li>
			<li id="menuUser"><a href="/member/user/userRegPage">????????? ??????</a></li>
			<li class="cate2">????????? ??????</li>
			<li id="menuAdmin"><a href="/member/admin/adminPage">????????? ?????? ??? ??????</a></li>
			<li class="cate2">[?????????]????????? ??????</li>
			<li id="menuTotList"><a href="/member/totList/totListPage">?????? ????????? ?????? ??????</a></li>
			<li id="menuNewUser"><a href="/member/newUser/newUserRegPage">????????? ???????????? ??????</a></li>
			<li id="menuNewConfirm"><a href="/member/newConfirm/newConfirmPage">????????? ?????? ??? ??????/??????</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ADMIN', 'SYSTEM', 'MEMBER')">
			<li class="cate2">[?????????]??????</li>	
			<li id="menuBoard"><a href="/common/board/noticePage">????????????</a></li>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('SYSTEM')">
			<li class="cate1 ">?????????</li>
			<li class="cate2">[??????]</li>	
			<li id="menuCode"><a href="/system/code/codePage">????????????</a></li>
			<li id="menuApi"><a href="/system/api/apiPage">API??????</a></li>
		</sec:authorize>
	</ul>
</div>

