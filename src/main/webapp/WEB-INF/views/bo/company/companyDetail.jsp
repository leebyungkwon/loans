<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script type="text/javascript">

function pageLoad(){
	//첨부파일명 보여주기
	$(".inputFile").on("change", function () {
		var fileVal 	= $(this).val().split("\\");
		var fileName 	= fileVal[fileVal.length - 1];
		$(this).prev().val(fileName);
	});
}

function companyDetail() {
	var p = {
			name 		: "companyDetailFrm"
		  , success 	: function (opt,result) {
			alert("test");
 	    }
	}
	AjaxUtil.files(p);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>회원사 담당자 관리</h2>
		</div>

	</div>
	<form name="companyDetailFrm" id="companyDetailFrm" action="/admin/company/companyDetail" method="post" enctype="multipart/form-data">
		<input type="hidden" name="memberSeq" value="${companyDetail.memberSeq }"/>
	<div class="contents">
		<h3> </h3>
			<div id="table">
				<table class="view_table">
					<tr>
						<th>회원사</th>
						<td colspan="3">${companyDetail.comCode}</td>
					</tr>
					<tr>
						<th>아이디</th>
						<td colspan="3">${companyDetail.memberId}</td>
					</tr>
					<tr>
						<th>부서명</th>
						<td colspan="3">${companyDetail.deptNm}</td>
					</tr>
					<tr>
						<th>담당자명</th>
						<td colspan="3">${companyDetail.memberName}</td>
					</tr>
					<tr>
						<th>직위</th>
						<td>${companyDetail.positionNm}</td>
						<th>이메일</th>
						<td>${companyDetail.email}</td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td>${companyDetail.mobileNo}</td>
						<th>회원가입일</th>
						<td>${companyDetail.joinDt}</td>
					</tr>
					<tr>
						<th>승인상태</th>
						<td>${companyDetail.apprYn}</td>
					</tr>
					<tr>
						<th>첨부서류</th>
						<td colspan="3">
							<a href="#" class="btn_black btn_small mgl5">다운로드</a>
						</td>
					</tr>
				</table>
			</div>

			<div class="btn_wrap">
				<a href="#" class="btn_gray">목록</a>
				<a href="#" class="btn_black btn_left">반려</a>
				<a href="#" class="btn_black btn_right">승인</a>
			</div>
		</div>
	</div>

