<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
function pageLoad(){
	
}

// 리스트 페이지 이동
function inactiveList(){
	history.back();
}

//휴면회원 활성화
function boInactiveUser() {
	var userSeqArr = [];
	var userSeq = $("#userSeq").val();
	if(WebUtil.isNull(userSeq)){
		alert("회원정보가 잘못되었습니다.\w새로고침 후 다시 시도해 주세요.");
		return false;
	}else{
		userSeqArr.push(userSeq);	
	}
	
	if(confirm("휴면회원을 활성화 하시겠습니까?")){
		var p = {
			  url		: "/admin/inactive/boInactiveUser"	
			, param		: {
				userSeqArr 	: userSeqArr  
			}
			, success 	: function (opt,result) {
				history.back();
		    }
		}
		AjaxUtil.post(p);
	}
}

// 회원관리 법인 승인처리
function usersCorpApply(){
	var userSeq = $("#userSeq").val();
	if(WebUtil.isNull(userSeq)){
		alert("법인회원정보가 잘못되었습니다.\w새로고침 후 다시 시도해 주세요.");
		return false;
	}
	
	if(confirm("법인회원을 승인하시겠습니까?")){
		var p = {
			  url		: "/admin/users/usersCorpApply"	
			, param		: {
				userSeq 	: userSeq  
			}
			, success 	: function (opt,result) {
				console.log("#####" , result);
				usersGrid.refresh();
		    }
		}
		AjaxUtil.post(p);
	}
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>휴먼회원관리 상세</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<input type="hidden" id="userSeq" value="${usersInfo.userSeq}" />
			<table class="view_table" id="dtlTable">
			<colgroup>
				<col width="12%">
				<col width="21%">
				<col width="12%">
				<col width="21%">
				<col width="12%">
				<col width="21%">
			</colgroup>
			<tbody>
				<tr>
					<th>아이디</th>
					<td>${usersInfo.userId}</td>
					<th>이름</th>
					<td>${usersInfo.userName}</td>
					<th>연락처</th>
					<td>${usersInfo.mobileNo}</td>
				</tr>
				
				<tr>
					<th>구분</th>
					<td>${usersInfo.plClassNm}</td>
					<%-- 
					<th>생년월일</th>
					<td>${usersInfo.birthDt}</td>
					 --%>
					<th>이메일</th>
					<td>${usersInfo.email}</td>				
				</tr>
				
				<tr>
					<th>가입일</th>
					<td>${usersInfo.joinDt}</td>
					<th>약관동의여부</th>
					<td>${usersInfo.termsYn}</td>
					<th>약관동의일</th>
					<td>${usersInfo.termsDt}</td>
				</tr>
				
				<c:if test="${usersInfo.plClass eq '2'}">
					<tr>
						<th>담당자이름</th>
						<td>${usersInfo.managerName}</td>
						<th>담당자연락처</th>
						<td>${usersInfo.managerMobileNo}</td>
						<th>담당자이메일</th>
						<td>${usersInfo.managerEmail}</td>
					</tr>
					<tr>
						<th>법인승인여부</th>
						<td>${usersInfo.corpApprYn}</td>
						<th>법인승인일</th>
						<td>${usersInfo.corpApprDt}</td>
						<th></th>
						<td></td>
					</tr>
				</c:if>
				<tr>
					<th>마지막로그인일시</th>
					<td>${usersInfo.lastLoginDt}</td>
					<th>로그인실패횟수</th>
					<td>${usersInfo.failCnt}</td>
					<th>로그인차단일시</th>
					<td>${usersInfo.failStopDt}</td>
				</tr>
				
				<tr>
					<th>첨부파일</th>
					<td colspan="5">
						<a href="/common/fileDown?fileSeq=${file.fileSeq}">${file.fileFullNm}</a>
					</td>
				</tr>
			
			</tbody>
			</table>
		</div>
		
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_blue btn_right" onclick="boInactiveUser();">휴면회원활성화</a>
			<a href="javascript:void(0);" class="btn_gray" onclick="inactiveList();">목록</a>
		</div>
	</div>
</div>

