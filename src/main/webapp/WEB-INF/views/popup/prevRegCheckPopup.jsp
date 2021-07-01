<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="title_wrap">
	<h5>기등록여부체크리스트</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>
<table class="popup_table">
	<thead>
		<tr>
			<th>접수번호</th>
			<th>회원사</th>
			<th>상품</th>
			<th>상태</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${fn:length(result) > 0 }">
				<c:forEach var="prevList" items="${result}" varStatus="status">
					<tr>
						<td class="acenter">${prevList.masterToId }</td>
						<td class="acenter">${prevList.comCodeNm }</td>
						<td class="acenter">${prevList.plProductNm }</td>
						<td class="acenter">${prevList.plStatNm }</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="5" class="acenter">데이터가 존재하지 않습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>