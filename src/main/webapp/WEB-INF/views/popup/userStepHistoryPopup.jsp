<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="title_wrap">
	<h5>모집인 이력보기</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>
<table class="popup_table">
	<thead>
		<tr>
			<th>모집인상태</th>
			<th>처리상태</th>
			<th>사유</th>
			<th>회원명</th>
			<th>일시</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${fn:length(stepHisList) > 0 }">
				<c:forEach var="stepHisList" items="${stepHisList }" varStatus="status">
					<tr>
						<td class="acenter">${stepHisList.plRegStatNm }</td>
						<td class="acenter">${stepHisList.plStatNm }</td>
						<td class="acenter">${stepHisList.plHistTxt }</td>
						<td class="acenter">${stepHisList.memberNm }</td>
						<td class="acenter">${stepHisList.regTimestamp }</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="4" class="acenter">데이터가 존재하지 않습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>