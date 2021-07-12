<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<div class="title_wrap">
	<h5>API조회 결과</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>
<table class="popup_table">
	<colgroup>
		<col width="170">
		<col width="*">
	</colgroup>
	<tbody>
		<c:choose>
			<c:when test="${result.plClass eq '1' }">
				<tr>
					<th>등록번호</th>
					<td>${result.plRegistNo}</td>
				</tr>
				<tr>
					<th>성명</th>
					<td>${result.plMName }</td>
				</tr>
				<tr>
					<th>생년월일</th>
					<td>${result.plMZId }</td>
				</tr>
			</c:when>
			<c:when test="${result.plClass eq '2' }">
				<tr>
					<th>등록번호</th>
					<td>${result.plRegistNo}</td>
				</tr>
				<tr>
					<th>법인명</th>
					<td>${result.plMName }</td>
				</tr>
				<tr>
					<th>대표성명</th>
					<td>${result.plCeoName }</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<th></th>
					<td>조회된 결과가 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${fn:length(result.searchResultList) > 0 }">
			<c:forEach var="searchResultList" items="${result.searchResultList }" varStatus="status">
				<tr>
					<th>취급상품</th>
					<td>
						<c:choose>
							<c:when test="${searchResultList.loanType eq '01'}">대출</c:when>
							<c:when test="${searchResultList.loanType eq '03'}">TM대출</c:when>
							<c:when test="${searchResultList.loanType eq '05'}">리스</c:when>
							<c:when test="${searchResultList.loanType eq '06'}">TM리스</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>계약번호</th>
					<td>${searchResultList.conNum }</td>
				</tr>

				<tr>
					<th>등록일</th>
					<td>
						${fn:substring(searchResultList.conDate,0,4)}-${fn:substring(searchResultList.conDate,4,6)}-${fn:substring(searchResultList.conDate,6,8)}
					</td>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>
</table>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">닫기</a>
</div>

