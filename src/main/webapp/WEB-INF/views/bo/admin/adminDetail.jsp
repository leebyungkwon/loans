<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
	
		$("#adminBtn").on("click", function(){
			location.href = "/member/admin/adminPage";
		});
	
		$("#adminInsBtn").on("click", function(){
			$("#adminDetailFrm").submit();
		});
	
		//첨부파일명 보여주기
		$(".inputFile").on("change", function () {
			var fileVal 	= $(this).val().split("\\");
			var fileName 	= fileVal[fileVal.length - 1];
			$(this).prev().val(fileName);
		});
	
	}

	function filedown(fileSeq){
		var p = {
			  url : '/common/fileDown'
			, contType: 'application/json; charset=UTF-8'
			, responseType: 'arraybuffer'
			, param : {
				fileSeq : fileSeq
			}
		}
		AjaxUtil.post(p);
	}
	
   
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
				<h2>관리자 조회 및 변경</h2>
		</div>
	</div>
		
	<form id="adminDetailFrm" method="post" action="/member/admin/adminDetailUpdPage">
		<input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
	</form>
		
	<div class="contents">
		<h3> </h3>
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
					<th>회사 전화번호</th>
					<td colspan="3">${adminInfo.extensionNo}</td>
				</tr>
				<tr>
					<th>휴대폰번호</th>
					<td colspan="3">${adminInfo.mobileNo}</td>
				</tr>
				<tr>
					<th>가입일</th>
					<td colspan="3">${adminInfo.joinDt}</td>
				</tr>
				<tr>
					<th class="acenter">첨부 파일</th>
					<td colspan="3">
						<a href="javascript:filedown('${file.fileSeq}')">${file.fileFullNm}</a>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="btn_wrap">
		<a href="javascript:void(0);" id="adminBtn" class="btn_gray">목록</a>
		<sec:authentication property="principal.username" var="userSeq" />
			<c:if test="${adminInfo.memberSeq eq userSeq}">
				<a href="javascript:void(0);" id="adminInsBtn" class="btn_black btn_right">수정</a>
			</c:if>
	</div>

</div>