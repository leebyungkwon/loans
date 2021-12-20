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
			<c:when test="${code eq 'success' }">
				<tr>
					<th>수수료기납부여부</th>
					<td>${feeYn }</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<th></th>
					<td>${msg }</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">닫기</a>
</div>

