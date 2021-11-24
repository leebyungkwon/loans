<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
function pageLoad(){
	
}



// 리스트 페이지 이동
function usersList(){
	history.back();
}

// 승인 및 거절
function updateUserStat(num){
	
	var userSeq = "${usersInfo.userSeq}";
	var userIndvReqSeq = "${usersInfo.userIndvReqSeq}";
	var stat = "";
	var txt = $("#txt").val();
	var confirmMessage = "";
	
	if(WebUtil.isNull(userSeq) || WebUtil.isNull(userIndvReqSeq)){
		alert("새로고침 후 다시 시도해 주세요.");
		return false;
	}
	
	if(num == "2"){
		stat = "2";
		confirmMessage = "정보변경에 대한 승인을 하시겠습니까?";
	}else if(num == "3"){
		stat = "3";
		confirmMessage = "정보변경에 대한 거절을 하시겠습니까?";
	}else{
		alert("새로고침 후 다시 시도해 주세요.");
		return false;
	}
	
	if(confirm(confirmMessage)){
		var p = {
			  url		: "/admin/updateIndvUsers/updateIndvUsersStat"	
			, param		: {
				userSeq				:	userSeq
				, userIndvReqSeq	:	userIndvReqSeq
				, stat				:	stat
				, txt				:	txt
			}
			, success 	: function (opt,result) {
				//location.href="/admin/updateIndvUsers/updateIndvUsersPage";
		    }
		}
		AjaxUtil.post(p);
	}
	
}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>개인회원 정보변경 변경 전 상세</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table1">
			<table class="view_table" id="beforeTable">
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
						<th>주민등록번호</th>
						<td>${usersInfo.plMZId}</td>
						<th>신청일</th>
						<td>${usersInfo.reqDate}</td>
						<th>상태</th>
						<td>${usersInfo.statNm}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="top_box">
		<div class="title type2">
			<h2>개인회원 정보변경 변경 후 상세</h2>
		</div>
	</div>
	
	<div class="contents">
		<div id="table2">
			<table class="view_table" id="afterTable">
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
						<c:if test="${empty usersInfo.reqUserName}">
							<td>${usersInfo.userName}</td>
						</c:if>
						<c:if test="${!empty usersInfo.reqUserName}">
							<td style="color:blue;">${usersInfo.reqUserName}</td>
						</c:if>
						<th>연락처</th>
						<c:if test="${empty usersInfo.reqMobileNo}">
							<td>${usersInfo.mobileNo}</td>
						</c:if>
						<c:if test="${!empty usersInfo.reqMobileNo}">
							<td style="color:blue;">${usersInfo.reqMobileNo}</td>
						</c:if>
					</tr>
					<tr>
						<th>주민등록번호</th>
						<c:if test="${empty usersInfo.reqPlMZId}">
							<td>${usersInfo.plMZId}</td>
						</c:if>
						<c:if test="${!empty usersInfo.reqPlMZId}">
							<td style="color:blue;">${usersInfo.reqPlMZId}</td>
						</c:if>
						<th>신청일</th>
						<td>${usersInfo.reqDate}</td>
						<th>상태</th>
						<td>${usersInfo.statNm}</td>
					</tr>
					<tr>
						<th>사유</th>
						<td colspan="5">
							<textarea rows="6" cols="" id="txt" name="txt" class="w100">${usersInfo.txt }</textarea>
						</td>
					</tr>
					
					<c:choose>
						<c:when test="${fn:length(fileList) > 0 }">
							<c:forEach var="updateFileList" items="${fileList}" varStatus="status">
							<tr>
								<c:if test="${updateFileList.fileType eq '13'}">
									<th>이름변경 첨부파일</th>
								</c:if>
								<c:if test="${updateFileList.fileType eq '14'}">
									<th>주민등록번호 첨부파일</th>
								</c:if>
								<td colspan="5">
									<a href="/common/fileDown?fileSeq=${updateFileList.fileSeq}">${updateFileList.fileFullNm}</a>
								</td>
							</tr>
							</c:forEach>
						</c:when>
					</c:choose>
				</tbody>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="usersList();">목록</a>
			<c:if test="${usersInfo.stat eq '1'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03" onclick="updateUserStat('2');">승인</a>
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small01" onclick="updateUserStat('3');">거절</a>
			</c:if>
		</div>
	</div>
</div>

