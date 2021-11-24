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

// 위반이력 등록 및 배치 insert
function newUpdateVio(){
	if(confirm("저장과 동시에 은행연합회 API가 발송됩니다.\n위반이력을 저장하시겠습니까?")){
		var p = {
			  name 		: "userRegInfoUpdFrm"
			, success 	: function (opt,result) {
				goUserConfirmList();
	 	    }
		}
		AjaxUtil.files(p);
	}
}


//위반이력 영역 추가
function goViolationAdd(obj){
	
	var html 		= '';
	
	html += '<tr class="violationArea">';
	html += '<th>위반이력사항</th>';
	html += '<td colspan="3">';
	html += '<select name="violationCdArr">';
	html += '<option value="">선택해 주세요.</option>';
	
	<c:forEach var="list" items="${result.violationCodeList}">
		html += '<option value="${list.codeDtlCd}">'+"${list.codeDtlNm}"+'</option>';
	</c:forEach>
	
	/*
	for(var i = 0;i < codeListLen;i++){
		console.log(codeList[i].codeDtlCd);
		html += '<option value="'+codeList[i].codeDtlCd+'">'+codeList[i].codeDtlNm+'</option>';
	}
	*/
	
	html += '</select> '; //공백 제거 금지
	html += '<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a> '; //공백 제거 금지
	html += '<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDel(this);">-</a>';
	html += '</td>';
	html += '</tr>';
	
	$("#table > table").append(html);
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

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/newConfirm/newUpdateVio" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="userSeq" id="userSeq" value="${result.userRegInfo.userSeq}"/>
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
						<th>등록번호</th>
						<td>${result.userRegInfo.plRegistNo }</td>
						<th>API상태메세지</th>
						<td>${result.userRegInfo.apiResMsg }</td>
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
					<c:choose>
						<c:when test="${fn:length(result.violationInfoList) > 0 }">
							<c:forEach var="violationInfoList" items="${result.violationInfoList }" varStatus="status">
								<tr class="violationArea">
									<th>위반이력사항</th>
									<td colspan="3">
										${violationInfoList.violationCdNm }
										<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a>
										<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDataDel('${violationInfoList.vioNum }','${violationInfoList.violationSeq }',this);">-</a>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="violationArea">
								<th>위반이력사항</th>
								<td colspan="3">
									<select name="violationCdArr">
										<option value="">선택해 주세요.</option>
										<c:forEach var="violationCodeList" items="${result.violationCodeList }">
											<option value="${violationCodeList.codeDtlCd }">${violationCodeList.codeDtlNm }</option>
										</c:forEach>
									</select>
									<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a>
									<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDel(this);">-</a>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
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
				<a href="javascript:void(0);" class="btn_Lgray" style="position: absolute; left: 0;" onclick="newUpdateVio()">위반이력저장</a>
			</div>
		</div>
	</form>
</div>

