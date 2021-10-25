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
			<h2>모집인 조회 및 해지 - 개인</h2>
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
						<th>회원사</th>
						<td>${result.userRegInfo.comCodeNm }</td>
						<th>담당자</th>
						<td>${result.userRegInfo.memberNm } (${result.userRegInfo.email }<c:if test="${result.userRegInfo.extensionNo ne null && result.userRegInfo.extensionNo ne '' }">, ${result.userRegInfo.extensionNo }</c:if>)</td>
					</tr>
					<tr>
						<th>가등록번호</th>
						<td colspan="3">${result.userRegInfo.preLcNum }</td>
					</tr>
					<tr>
						<th>모집인 상태</th>
						<td>${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.userRegInfo.masterSeq }');">이력보기</a></td>
						<th>처리상태</th>
						<td>${result.userRegInfo.plStatNm }</td>
					</tr>
					<tr>
						<th>모집인 분류</th>
						<td colspan="3">${result.userRegInfo.plClassNm }</td>
					</tr>
					<tr>
						<th>법인사용인여부</th>
						<td colspan="3">${result.userRegInfo.corpUserYn }</td>
					</tr>
					<c:if test="${result.userRegInfo.corpUserYn eq 'Y' }">
						<tr>
							<th>법인명</th>
							<td>${result.userRegInfo.plMerchantName }</td>
							<th>법인등록번호</th>
							<td>${result.userRegInfo.plMerchantNo }</td>
						</tr>
					</c:if>
					<tr>
						<th>신규경력구분</th>
						<td colspan="3">${result.userRegInfo.careerTypNm }</td>
					</tr>
					<tr>
						<th>금융상품유형</th>
						<td colspan="3">${result.userRegInfo.plProductNm }</td>
					</tr>
					<tr>
						<th>이름</th>
						<td>${result.userRegInfo.plMName }</td>
						<th>주민번호</th>
						<td>${result.userRegInfo.plMZId }</td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td colspan="3">${result.userRegInfo.plCellphone }</td>
					</tr>
					
					<tr>
						<th>주소</th>
						<td>${result.userRegInfo.addrNm }</td>
						<th>상세주소</th>
						<td>${result.userRegInfo.addrDetail }</td>
					</tr>
					 
<%-- 					<tr>
						<th>주소</th>
						<td colspan="3">
							<select name="addr">
								<c:forEach var="addrCodeList" items="${result.addrCodeList }">
									<option value="${addrCodeList.codeDtlCd }" <c:if test="${addrCodeList.codeDtlCd eq result.userRegInfo.addr }">selected="selected"</c:if>>${addrCodeList.codeDtlNm }</option>
								</c:forEach>
							</select>
						</td>
					</tr> --%>
					<tr>
						<th>교육이수번호/인증서번호</th>
						<td colspan="3">${result.userRegInfo.plEduNo }</td>
					</tr>
					<tr>
						<th>경력시작일</th>
						<td>${result.userRegInfo.careerStartDate }</td>
						<th>경력종료일</th>
						<td>${result.userRegInfo.careerEndDate }</td>
					</tr>
					<tr>
						<th>계약일자</th>
						<td>${result.userRegInfo.comContDate }</td>
						<th>위탁예정기간</th>
						<td>${result.userRegInfo.entrustDate }</td>
					</tr>
					<c:if test="${result.userRegInfo.plStat eq '5' || result.userRegInfo.plStat eq '6' || result.userRegInfo.plStat eq '7' }">
						<tr>
							<th>보완요청사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
						</tr>
					</c:if>
					<c:if test="${result.userRegInfo.plStat eq '10' || result.userRegInfo.plStat eq '11' || result.userRegInfo.plStat eq '12' }">
						<tr>
							<th>사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
						</tr>
					</c:if>
					<tr>
						<th>승인요청사유</th>
						<td colspan="3">${result.userRegInfo.applyHistTxt }</td>
					</tr>
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

