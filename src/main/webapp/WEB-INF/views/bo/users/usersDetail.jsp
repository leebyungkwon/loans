<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
function pageLoad(){
	
}



// 리스트 페이지 이동
function usersList(){
	history.back();
}

//로그인 차단 해제
function loginStopUpdate() {
	
	var userSeqArr = [];
	var userSeq = $("#userSeq").val();
	if(WebUtil.isNull(userSeq)){
		alert("회원정보가 잘못되었습니다.\w새로고침 후 다시 시도해 주세요.");
		return false;
	}else{
		userSeqArr.push(userSeq);	
	}
	
	if(confirm("로그인 차단을 해제 하시겠습니까?")){
		var p = {
			  url		: "/admin/users/loginStopUpdate"	
			, param		: {
				userSeqArr 	: userSeqArr  
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
			<h2>회원관리 상세</h2>
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
					<th>생년월일</th>
					<td>${usersInfo.birthDt}</td>
					<th>성별</th>
					<td>${usersInfo.gender}</td>
				</tr>
				
				<tr>
					<th>주소</th>
					<td>${usersInfo.addr}</td>
					<th>상세주소</th>
					<td colspan="3">${usersInfo.addrDetail}</td>
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
						<td>${usersInfo.corpApprStat}</td>
						<th>법인승인일</th>
						<td>${usersInfo.corpApprDt}</td>
						<th></th>
						<td></td>
					</tr>
				</c:if>
				
				<tr>
					<th>이메일</th>
					<td>${usersInfo.email}</td>
					<th>탈퇴여부</th>
					<td>${usersInfo.dropYn}</td>
					<th>탈퇴일</th>
					<td>${usersInfo.dropDt}</td>
				</tr>
				
				<tr>
					<th>휴면여부</th>
					<td>${usersInfo.inactiveYn}</td>
					<th>휴면변환일</th>
					<td>${usersInfo.inactiveDt}</td>
					<th></th>
					<td></td>
				</tr>
				
				<tr>
					<th>마지막로그인일시</th>
					<td>${usersInfo.lastLoginDt}</td>
					<th>로그인실패횟수</th>
					<td>${usersInfo.failCnt}</td>
					<th>로그인차단일시</th>
					<td>${usersInfo.failStopDt}</td>
				</tr>
				
				<tr>
					<th class="acenter">첨부파일</th>
					<td colspan="5">
						<a href="/common/fileDown?fileSeq=${file.fileSeq}">${file.fileFullNm}</a>
					</td>
				</tr>
			
			</tbody>
			</table>
		</div>
		
		<div class="btn_wrap">
			<c:if test="${not empty usersInfo.failStopDt}">
				<a href="javascript:void(0);" class="btn_blue" style="float:left;" onclick="loginStopUpdate();">로그인차단해제</a>
			</c:if>
			<a href="javascript:void(0);" class="btn_gray" onclick="usersList();">목록</a>
			<a href="javascript:void(0);" class="btn_blue btn_right" onclick="goCompanyStatUpdt('3','MEMBER');">승인</a>
		</div>
	</div>
</div>
