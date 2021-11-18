<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newUserReg/common.js"></script>

<script type="text/javascript">

function pageLoad(){
	
	//datepicker
	goDatepickerDraw();
	
}


</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>해지신청 및 조회 - 개인</h2>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/newUser/newUserApply" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="plStat" id="plStat" value=""/>
		
		<div class="contents">
			<h3>등록정보</h3>
			<div id="table">
				<table class="view_table">
					<tr>
						<th>신청구분</th>
						<td>${result.userRegInfo.plRegStatNm }</td>
						<th>처리상태</th>
						<td>${result.userRegInfo.plStatNm }</td>
					</tr>
				
				
					<tr>
						<th>모집인유형</th>
						<td>${result.userRegInfo.plClassNm }</td>
						<th>성명</th>
						<td>${result.userRegInfo.plMName }</td>
					</tr>
					
					<tr>
						<th>주민등록번호</th>
						<td>${result.userRegInfo.plMZId }</td>
						<th>신청일</th>
						<td>${result.userRegInfo.comHaejiDate }</td>
					</tr>
					<c:if test="${result.userRegInfo.plStat eq '4' or !empty result.userRegInfo.creHaejiDate}">
						<tr>
							<th>해지요청사유</th>
							<td colspan="3">${result.userRegInfo.plHistCdNm }</td>
						</tr>
						<tr>
							<th>해지승인일자</th>
							<td colspan="3">${result.userRegInfo.creHaejiDate }</td>
						</tr>
					</c:if>
				
				</table>
			</div>
			
			<div class="btn_wrap">
				<c:if test="${result.userRegInfo.plRegStat eq '3' and result.userRegInfo.plStat eq '9'}">
					<a href="javascript:void(0);" class="btn_black btn_right w100p" id="userDropApply" onclick="goUserDropApplyPage();">해지요청</a>
				</c:if>
				<c:if test="${result.userRegInfo.plRegStat eq '3' and result.userRegInfo.plStat eq '4' && result.userRegInfo.plStatReqPath eq '2'}">
					<a href="javascript:void(0);" class="btn_black btn_right w100p" onclick="goUserDropApplyCancel();">해지요청취소</a>
				</c:if>
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
			</div>
		</div>
	</form>
</div>

