<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){

		$("#AdminInsertBtn").on("click", function(){
			$("#adminDetailFrm").submit();
		});

		$("#AdminCancelBtn").on("click", function(){
			location.href = "/member/admin/adminPage";
		});
	}
	
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>관리자 조회 및 변경</h2>
		</div>
	</div>

	<form id="adminDetailFrm" method="post" action="/member/admin/adminDetailUpdate">
		<input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
	</form>

	<div class="contents">
		<div id="table">
			<table class="view_table">
				<tr>
					<th>아이디</th>
					<td colspan="3">${adminInfo.memberId}</td>
				</tr>
				<tr>
					<th>부서명</th>
					<td colspan="3">${adminInfo.deptNm}</td>
				</tr>
				<tr>
					<th>담당자명</th>
					<td colspan="3">${adminInfo.memberName}</td>
				</tr>
				<tr>
					<th>직위</th>
					<td colspan="3">${adminInfo.positionNm}</td>
				</tr>
				<tr>
					<th>이메일</th>
					<td colspan="3">${adminInfo.email}</td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td colspan="3">${adminInfo.mobileNo}</td>
				</tr>
				<tr>
					<th>가입일</th>
					<td colspan="3">${adminInfo.joinDt}</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="btn_wrap">
		<a href="javascript:void(0);" id="AdminCancelBtn" class="btn_gray">취소</a>
		<a href="javascript:void(0);" id="AdminInsertBtn" class="btn_black btn_right">수정</a>
	</div>
</div>	
