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

//로그인 잠금 해제
function loginStopUpdate() {
	var userSeqArr = [];
	var userSeq = $("#userSeq").val();
	if(WebUtil.isNull(userSeq)){
		alert("회원정보가 잘못되었습니다.\w새로고침 후 다시 시도해 주세요.");
		return false;
	}else{
		userSeqArr.push(userSeq);	
	}
	
	if(confirm("로그인 잠금을 해제 하시겠습니까?")){
		var p = {
			  url		: "/admin/users/loginStopUpdate"	
			, param		: {
				userSeqArr 	: userSeqArr  
			}
			, success 	: function (opt,result) {
		    }
		}
		AjaxUtil.post(p);
	}
}

// 결격사유 및 범죄경력 수정
function updateIndvUserDis(){
	
	var userSeq = $("#userSeq").val();
	var dis1 = $("input[name='dis1']:checked").val();
	var dis2 = $("input[name='dis2']:checked").val();
	
	
	if(confirm("결격사유 및 범죄경력을 수정 하시겠습니까?")){
		var p = {
			  url		: "/admin/users/updateIndvUserDis"	
			, param		: {
				userSeq 	:	userSeq
				, dis1		:	dis1
				, dis2		:	dis2
			}
			, success 	: function (opt,result) {
		    }
		}
		AjaxUtil.post(p);
	}
	
}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>개인회원 상세</h2>
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
						<th>가입일</th>
						<td>${usersInfo.joinDt}</td>
						<th>약관동의여부</th>
						<td>${usersInfo.termsYn}</td>
						<th>약관동의일</th>
						<td>${usersInfo.termsDt}</td>
					</tr>
					
					<tr>
						<th>이메일</th>
						<td>${usersInfo.email}</td>
						<th>로그인실패횟수</th>
						<td>${usersInfo.failCnt}</td>
						<th>로그인잠금일시</th>
						<td>${usersInfo.failStopDt}</td>
					</tr>
					
					<tr>
						<th>마지막로그인일시</th>
						<td>${usersInfo.lastLoginDt}</td>
						<th>결격사유 조회결과</th>
						<td class="half_input">
							<input type="radio" name="dis1" value="Y" id="dis1" <c:if test="${usersInfo.dis1 eq 'Y'}">checked="checked"</c:if> />
							<label for="dis1">Y</label>
							<input type="radio" name="dis1" value="N" id="dis2" <c:if test="${usersInfo.dis1 eq 'N'}">checked="checked"</c:if> />
							<label for="dis2">N</label>						
						</td>
						
						<th>범죄경력 조회결과</th>
						<td class="half_input">
							<input type="radio" name="dis2" value="Y" id="dis3" <c:if test="${usersInfo.dis2 eq 'Y'}">checked="checked"</c:if> />
							<label for="dis3">Y</label>
							<input type="radio" name="dis2" value="N" id="dis4" <c:if test="${usersInfo.dis2 eq 'N'}">checked="checked"</c:if> />
							<label for="dis4">N</label>	
						</td>
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
			<c:if test="${not empty usersInfo.failStopDt}">
				<a href="javascript:void(0);" class="btn_blue" style="float:left;" onclick="loginStopUpdate();">로그인잠금해제</a>
			</c:if>
			<a href="javascript:void(0);" class="btn_gray" onclick="usersList();">목록</a>
			<a href="javascript:void(0);" class="btn_Lgray btn_right_small01" onclick="updateIndvUserDis();">결격요건수정</a>
		</div>
	</div>
</div>

