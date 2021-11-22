<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<div class="title_wrap">
	<h5>위반이력 조회 결과</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>
<table class="popup_table">
	<colgroup>
		<col width="170">
		<col width="*">
	</colgroup>
	<tbody>
		<c:choose>
			<c:when test="${fn:length(result.searchResultList) > 0 }">
				<c:forEach var="searchResultList" items="${result.searchResultList }" varStatus="status">
					<tr>
						<th>금융기관코드</th>
						<td>
							${searchResultList.vioFinCode }
						</td>
					</tr>
					<tr>
						<th>위반일</th>
						<td>${searchResultList.vioDate }</td>
					</tr>
					<tr>
						<th>위반코드</th>
						<td>${searchResultList.vioCode}</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<th></th>
					<td>
						조회된 데이터가 없습니다.
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">닫기</a>
</div>

