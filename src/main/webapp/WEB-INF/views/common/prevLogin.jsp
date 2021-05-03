<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){

		$("#AdminUpdateBtn").on("click", function(){
			var p = {
				name 		: "saveAdminUpdateFrm"
				, success 	: function (opt,result) {
					alert("호잇");
					location.href = "/member/admin/adminPage";
				}
			}
		AjaxUtil.files(p);
	});

		$("#AdminCancelBtn").on("click", function(){
			location.href = "/member/admin/adminPage";
		});
	}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>관리자 수정</h2>
		</div>
	</div>
	
	<form name="saveAdminUpdateFrm" id="saveAdminUpdateFrm" action="/member/admin/saveAdminUpdate" method="post" enctype="multipart/form-data">
		<input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
		<div class="contents">
			<div id="table">
				<table class="view_table">
					<tr>
						<th>아이디</th>
						<td colspan="3"><input type="text" name="memberId" value="${adminInfo.memberId}" readonly="readonly"></td>
					</tr>
					<tr>
						<th>패스워드</th>
						<td colspan="3"><input type="text" name="password"></td>
					</tr>
					<tr>
						<th>패스워드 확인</th>
						<td colspan="3"><input type="text" name="password_chk"></td>
					</tr>
					<tr>
						<th>부서명</th>
						<td colspan="3"><input type="text" name="deptNm"></td>
					</tr>
					<tr>
						<th>담당자명</th>
						<td colspan="3"><input type="text" name="memberName"></td>
					</tr>
					<tr>
						<th>직위</th>
						<td colspan="3"><input type="text" name="positionNm"></td>
					</tr>
					<tr>
						<th>이메일</th>
						<td colspan="3"><input type="text" name="email"></td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td colspan="3"><input type="text" name="mobileNo"></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
	
	<div class="btn_wrap">
		<a href="javascript:void(0);" id="AdminCancelBtn" class="btn_gray">취소</a>
		<a href="javascript:void(0);" id="AdminUpdateBtn" class="btn_black btn_right">저장</a>
	</div>
</div>