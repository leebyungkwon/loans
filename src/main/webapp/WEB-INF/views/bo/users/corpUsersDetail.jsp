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

// 결격사유 수정
function updateCorpUserDis(){
	
	var userSeq = $("#userSeq").val();
	var dis9 = $("input[name='dis9']:checked").val();
	var dis10 = $("input[name='dis10']:checked").val();
	var dis11 = $("input[name='dis11']:checked").val();
	var dis12 = $("input[name='dis12']:checked").val();
	var dis13 = $("input[name='dis13']:checked").val();
	
	if(confirm("선택한 결과로 결격요건을 수정 하시겠습니까?")){
		var p = {
			  url		: "/admin/corpUsers/updateCorpUserDis"	
			, param		: {
				userSeq 	:	userSeq
				, dis9		:	dis9
				, dis12		:	dis12
				, dis13		:	dis13
			}
			, success 	: function (opt,result) {
		    }
		}
		AjaxUtil.post(p);
	}
}

// 금융감독원 승인여부 수정
function updatePassYn(){
	var corpSeq = $("#corpSeq").val();
	var passYn = $("input[name='passYn']:checked").val();
	
	console.log("####" + corpSeq);
	
	if(confirm("금융감독원 승인여부를 수정 하시겠습니까?")){
		var p = {
			  url		: "/admin/corpUsers/updatePassYn"	
			, param		: {
				corpSeq 	:	corpSeq
				, passYn	:	passYn
			}
			, success 	: function (opt,result) {
		    }
		}
		AjaxUtil.post(p);
	}
}

//회원관리 법인 승인처리
function usersCorpApply(){
	var userSeq = $("#userSeq").val();
	var corpSeq = $("#corpSeq").val();
	if(WebUtil.isNull(userSeq)){
		alert("법인회원정보가 잘못되었습니다.\w새로고침 후 다시 시도해 주세요.");
		return false;
	}
	
	if(confirm("법인회원을 승인하시겠습니까?")){
		var p = {
			  url		: "/admin/corpUsers/usersCorpApply"	
			, param		: {
				 userSeq 	: userSeq  
				,corpSeq 	: corpSeq
			}
			, success 	: function (opt,result) {
				location.href="/admin/corpUsers/corpUsersPage?historyback=Y";
		    }
		}
		AjaxUtil.post(p);
	}
}


//회원관리 법인 가승인처리
function usersCorpTempApply(){
	var userSeq = $("#userSeq").val();
	if(WebUtil.isNull(userSeq)){
		alert("법인회원정보가 잘못되었습니다.\w새로고침 후 다시 시도해 주세요.");
		return false;
	}
	
	var apprTxt = $("#apprTxt").val();
	if(WebUtil.isNull(apprTxt)){
		alert("가승인 사유를 입력해 주세요.");
		$("#apprTxt").focus();
		return false;
	}
	
	if(confirm("법인회원을 가승인 하시겠습니까?")){
		var p = {
			  url		: "/admin/corpUsers/usersCorpTempApply"	
			, param		: {
				userSeq 	: userSeq
				, apprTxt	: apprTxt
			}
			, success 	: function (opt,result) {
				location.href="/admin/corpUsers/corpUsersPage?historyback=Y";
		    }
		}
		AjaxUtil.post(p);
	}
}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>법인회원 및 법인 상세</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<input type="hidden" id="userSeq" value="${usersInfo.userSeq}" />
			<input type="hidden" id="corpSeq" value="${usersInfo.corpSeq}" />
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
					<th>법인명</th>
					<td>${usersInfo.plMerchantName}</td>
					<th>법인번호</th>
					<td>${usersInfo.plMerchantNo}</td>
					<th>사업자번호</th>
					<td>${usersInfo.plBusinessNo}</td>
				</tr>
				<tr>
					<th>금융감독원 승인여부</th>
					<td class="half_input">
						<input type="radio" name="passYn" value="Y" id="passYn1" <c:if test="${usersInfo.passYn eq 'Y'}">checked="checked"</c:if> />
						<label for="passYn1">Y</label>
						<input type="radio" name="passYn" value="N" id="passYn2" <c:if test="${usersInfo.passYn eq 'N'}">checked="checked"</c:if> />
						<label for="passYn2">N</label>
						<a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="updatePassYn();">수정</a>				
					</td>
					<th></th>
					<td></td>
					<th></th>
					<td></td>
				</tr>
				<c:if test="${usersInfo.userSeq > 0}">
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
						<th>CI</th>
						<td colspan="3">${usersInfo.userCi}</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td>${usersInfo.email}</td>
						<th>법인승인여부</th>
						<td>${usersInfo.corpApprYn}</td>
						<th>법인승인일</th>
						<td>${usersInfo.corpApprDt}</td>
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
						<th>마지막로그인일시</th>
						<td>${usersInfo.lastLoginDt}</td>
						<th>로그인실패횟수</th>
						<td>${usersInfo.failCnt}</td>
						<th>로그인잠금일시</th>
						<td>${usersInfo.failStopDt}</td>
					</tr>
					
					<tr>
						<th>사회적신용 조회결과</th>
						<td class="half_input">
							<input type="radio" name="dis9" value="Y" id="dis9" <c:if test="${usersInfo.dis9 eq 'Y'}">checked="checked"</c:if> />
							<label for="dis9">Y</label>
							<input type="radio" name="dis9" value="N" id="dis10" <c:if test="${usersInfo.dis9 eq 'N'}">checked="checked"</c:if> />
							<label for="dis10">N</label>						
						</td>
						<th>대부업자 조회결과</th>
						<td class="half_input">
							<input type="radio" name="dis12" value="Y" id="dis15" <c:if test="${usersInfo.dis12 eq 'Y'}">checked="checked"</c:if> />
							<label for="dis15">Y</label>
							<input type="radio" name="dis12" value="N" id="dis16" <c:if test="${usersInfo.dis12 eq 'N'}">checked="checked"</c:if> />
							<label for="dis16">N</label>						
						</td>
						<th>다단계판매업자 조회결과</th>
						<td class="half_input">
							<input type="radio" name="dis13" value="Y" id="dis17" <c:if test="${usersInfo.dis13 eq 'Y'}">checked="checked"</c:if> />
							<label for="dis17">Y</label>
							<input type="radio" name="dis13" value="N" id="dis18" <c:if test="${usersInfo.dis13 eq 'N'}">checked="checked"</c:if> />
							<label for="dis18">N</label>						
						</td>
					</tr>
					<tr>
						<th>대표및임원의결격사유</th>
						<td>${usersInfo.dis1}</td>
						<th>대표및임원의범죄경력</th>
						<td>${usersInfo.dis2}</td>
						<th>결격요건수정일시</th>
						<td>${usersInfo.updDis1}</td>
					</tr>
					
					<tr>
						<th>첨부파일</th>
						<td colspan="5">
							<a href="/common/fileDown?fileSeq=${file.fileSeq}">${file.fileFullNm}</a>
						</td>
					</tr>
					
					<tr>
						<th>승인/가승인 사유</th>
						<td colspan="5">
							<textarea rows="6" cols="" id="apprTxt" name="apprTxt" class="w100">${usersInfo.apprTxt }</textarea>
						</td>
					</tr>
					
				</c:if>
			</tbody>
			</table>
		</div>
		
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="usersList();">목록</a>
			<a href="javascript:void(0);" class="btn_Lgray btn_right_small01 w100p" onclick="updateCorpUserDis();">결격요건수정</a>
			
			<c:if test="${not empty usersInfo.failStopDt}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" onclick="loginStopUpdate();">로그인잠금해제</a>
			</c:if>
			<c:if test="${usersInfo.plClass eq '2' and usersInfo.corpApprYn eq 'N'}">
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" onclick="usersCorpApply();">법인승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small03 w100p" onclick="usersCorpTempApply();">법인가승인</a>
			</c:if>
			
			<c:if test="${usersInfo.plClass eq '2' and usersInfo.corpApprYn eq 'Y' and usersInfo.roleName eq 'TEMP'}">
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" onclick="usersCorpApply();">법인승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small03 w100p" onclick="usersCorpTempApply();">법인가승인</a>
			</c:if>
		</div>
	</div>
</div>

