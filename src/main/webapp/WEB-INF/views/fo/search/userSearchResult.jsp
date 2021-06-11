<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
function pageLoad() {
	
}
</script>

<div class="inquiry_result_wrap">
	<div class="title">대출모집인 조회결과</div>
	<div class="inner">
		<div class="member_pic">
			<img src="/static/images/sub/member_pic.jpg" alt="">
		</div>
		<div class="table_wrap">
			<div class="table_title">현재경력</div>
			<table class="member_info">
				<colgroup>
					<col width="210"/>
					<col width="*"/>
				</colgroup>
				<tr>
					<th>등록번호</th>
					<td>${result.searchUserInfo.masterToId }</td>
				</tr>
				<tr>
					<th>성명</th>
					<td>${result.searchUserInfo.plMName }</td>
				</tr>
				<tr>
					<th>계약금융회사</th>
					<td>${result.searchUserInfo.comCodeNm }</td>
				</tr>
				<tr>
					<th>금융회사 전화번호</th>
					<td>${result.searchUserInfo.compTel }</td>
				</tr>
				<tr>
					<th>소속법인</th>
					<td>${result.searchUserInfo.plClassNm }</td>
				</tr>
				<tr>
					<th>계약일</th>
					<td>${result.searchUserInfo.comContDate }</td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(result.violationInfoList) > 0 }">
						<c:forEach var="violationInfoList" items="${result.violationInfoList }" varStatus="status">
							<tr>
								<th>위반확정일 (status.count)</th>
								<td>${violationInfoList.regTimestamp }</td>
							</tr>
							<tr>
								<th>위반행위 사유 (status.count)</th>
								<td>${violationInfoList.violationCdNm }</td>
							</tr>
						</c:forEach>
					</c:when>
				</c:choose>
			</table>
		</div>
		<div class="table_wrap mgt60">
			<div class="table_title">과거경력</div>
			<table class="member_info">
				<colgroup>
					<col width="210"/>
					<col width="*"/>
				</colgroup>
				<tr>
					<th>계약금융회사 (계약기간)</th>
					<td>우리금융캐피탈 (2017-07-05 ~ 2019-12-19)</td>
				</tr>
				<tr>
					<th>소속법인</th>
					<td>개인</td>
				</tr>
				<tr>
					<th>계약해지 (해지사유)</th>
					<td>정상해지</td>
				</tr>
				<tr>
					<th>위반확정일 (1)</th>
					<td>-</td>
				</tr>
				<tr>
					<th>위반행위 사유 (1)</th>
					<td>-</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="btn_wrap">
		<a href="/front/search/userSearchPage" class="btn_black">홈으로</a>
	</div>
</div>
      
      