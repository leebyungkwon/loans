<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
function pageLoad(){
	
}
// 가승인 or 승인처리
function goCompanyStatUpdt(apprStat,roleName) {
	var apprStatNm = apprStat == "2" ? "가승인" : "승인";
	var msg = $("#msg").val();
	if(apprStat=='2' && WebUtil.isNull(msg)){
		$("#msg").focus();
		alert("가승인 사유를 입력해 주세요.");
		return false; 
	}
	
	// 승인처리시 이메일전송
	var email = $("#sendEmail").text();
	if(apprStat == '3' && WebUtil.isNull(email)){
		alert("승인처리시 이메일이 필요합니다.");
		return false;
	}
	
	var memberId = "${companyDetail.memberId}";
	if(WebUtil.isNull(memberId)){
		alert("회원정보가 잘못되었습니다.");
		return false;
	}
	
	if(confirm(apprStatNm + "처리 하시겠습니까?")){
		var p = {
			  url		: "/admin/mng/updateCompanyStat"	
			, param		: {
				 memberSeq : $("#memberSeq").val()
				,apprStat  : apprStat
				,roleName  : roleName
				,msg	   : msg
				,memberId  : memberId
				,email	   : email
			}
			, success 	: function (opt,result) {
				if(WebUtil.isNull(result.message)){
					alert(result.data[0].defaultMessage);
				}else{
					location.reload();
				}
		    }
		}
		AjaxUtil.post(p);
	}
} 

// 가승인 text 박스 생성
function msgUpdate(){
	$("#msgTr").show(); 
	$("#msg").focus(); 
	$('#msgId').attr('onclick','goCompanyStatUpdt("2","TEMP_MEMBER");');
}

// 리스트 페이지 이동
function companyList(){
	location.href="/admin/mng/companyPage"
}

// 회원사 담당자 관리 - 수정페이지 이동
function companyInfoUpd(){
	$("#companyInfoUpdFrm").submit();
}


// 비밀번호 초기화
function cleanPassword(){
	if(confirm("비밀번호를 초기화 하시겠습니까?")){
		var p = {
			  url		: "/admin/mng/cleanPassword"	
			, param		: {
				 memberSeq : $("#memberSeq").val()
			}
			, success 	: function (opt,result) {
				if(WebUtil.isNull(result.message)){
					alert(result.data[0].defaultMessage);
				}else{
					location.reload();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>회원사 담당자 관리</h2>
		</div>
	</div>
	
	<form name="companyInfoUpdFrm" id="companyInfoUpdFrm" action="/member/admin/adminDetailUpdPage" method="post" enctype="multipart/form-data">
		<input type="hidden" name="memberSeq" id="memberSeq" value="${companyDetail.memberSeq }"/>
	</form>
	<form name="companyStatUpdt" id="companyDetailFrm" action="/admin/mng/updateCompanyStat" method="post" enctype="multipart/form-data">
		<input type="hidden" name="memberSeq" id="memberSeq" value="${companyDetail.memberSeq }"/>
		
		<div class="contents">
			<div id="table">
				<table class="view_table" id="dtlTable">
					<tr>
						<th>회원사</th>
						<td colspan="3">${companyDetail.comCodeNm}</td>
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
						<td id="sendEmail">${companyDetail.email}</td>
					</tr>
					<tr>
						<th>직장 전화번호</th>
						<td>${companyDetail.extensionNo}</td>
						<th>가입일</th>
						<td>${companyDetail.joinDt}</td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td colspan="3">${companyDetail.mobileNo}</td>
					</tr>
					<tr>
						<th>승인상태</th>
						<td colspan="3">${companyDetail.apprStatNm}</td>
					</tr>
					<tr>
						<th class="acenter">첨부파일</th>
						<td colspan="3">
							<a href="/common/fileDown?fileSeq=${file.fileSeq}">${file.fileFullNm}</a>
						</td>
					</tr>
					<tr>
						<c:choose>
							<c:when test="${companyDetail.apprStat eq 2}">
								<th>가승인 사유</th>
								<td colspan="3">
									${companyDetail.msg}
								</td>
							</c:when>
						</c:choose>
					</tr>
					<tr style="display: none;" id="msgTr">
						<th>가승인 사유</th>
						<td colspan="3"><textarea maxLength="200" rows="4" type="text" name="msg" id="msg" class="w100"></textarea></td>
					</tr>
				</table>
			</div>
			
			<div class="btn_wrap">	
				 <c:if test="${companyDetail.apprStat eq 1}">
				  	 <a href="javascript:void(0);" class="btn_gray" onclick="companyList();">목록</a>
   		 		 	 <a href="javascript:void(0);" class="btn_black btn_right02" onclick="msgUpdate();" id="msgId">가승인</a>
   		 		 	 <a href="javascript:void(0);" class="btn_blue btn_right" onclick="goCompanyStatUpdt('3','MEMBER');">승인</a>
     			 </c:if>
				 <c:if test="${companyDetail.apprStat eq 2}">
					  <a href="javascript:void(0);" class="btn_gray" onclick="companyList();">목록</a>
      		 		  <a href="javascript:void(0);" class="btn_blue btn_right" onclick="goCompanyStatUpdt('3','MEMBER');">승인</a>
     			 </c:if>
     			  <c:if test="${companyDetail.apprStat eq 3}">
      		 		  <a href="javascript:void(0);" class="btn_gray" onclick="companyList();">목록</a>
     			 </c:if>
     			 
     			 
     			 <a href="javascript:void(0);" class="btn_black" style="position:absolute; left:180px;" onclick="companyInfoUpd();">수정</a>
     			 <a href="javascript:void(0);" class="btn_blue" style="float:left;" onclick="cleanPassword();">비밀번호초기화</a>
			</div>
		</div>
	</form>
</div>

