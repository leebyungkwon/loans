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

function goCompanyStatUpdt(apprStat) {
	var p = {
		  url		: "/admin/company/updateCompanyStat"	
		, param		: {
			 memberSeq : $("#memberSeq").val()
			,apprStat  : apprStat 
		}
		, success 	: function (opt,result) {
			if(result.data > 0){
				alert("수정 되었습니다.");
				location.reload();
			}
	    }
	}
	AjaxUtil.post(p);
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

function companylist(){
	location.href="/admin/company/companyPage"
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>회원사 담당자 관리</h2>
		</div>
	</div>
	
	<form name="companyStatUpdt" id="companyDetailFrm" action="/admin/company/updateCompanyStat" method="post" enctype="multipart/form-data">
		<input type="hidden" name="memberSeq" id="memberSeq" value="${companyDetail.memberSeq }"/>
		
	<div class="contents">
		<h3> </h3>
			<div id="table">
				<table class="view_table">
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
						<td>${companyDetail.email}</td>
					</tr>
					<tr>
						<th>직장 번화번호</th>
						<td>${companyDetail.extensionNo}</td>
						<th>회원가입일</th>
						<td>${companyDetail.joinDt}</td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td>${companyDetail.mobileNo}</td>
					</tr>
					<tr>
						<th>승인상태</th>
						<td colspan="3">${companyDetail.apprStat}</td>
					</tr>
					<tr>
						<th class="acenter">첨부서류</th>
						<td colspan="3">
							<a href="javascript:filedown('${file.fileSeq}')">${file.fileFullNm}</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="btn_wrap">	
				 <c:if test="${companyDetail.apprStat eq 1}">
				  	 <a href="javascript:companylist();" class="btn_gray">목록</a>
   		 		 	 <a href="javascript:goCompanyStatUpdt('2')" class="btn_black btn_right">가승인</a>
   		 		 	 <a href="javascript:goCompanyStatUpdt('3')" class="btn_black">승인</a>
     			 </c:if>
				 <c:if test="${companyDetail.apprStat eq 2}">
					  <a href="javascript:companylist();" class="btn_gray">목록</a>
      		 		  <a href="javascript:goCompanyStatUpdt('3')" class="btn_black">승인</a>
     			 </c:if>
     			  <c:if test="${companyDetail.apprStat eq 3}">
      		 		  <a href="javascript:companylist();" class="btn_gray">목록</a>
     			 </c:if>
			</div>
		</div>
	</form>
</div>

