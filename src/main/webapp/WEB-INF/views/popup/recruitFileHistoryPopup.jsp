<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="title_wrap">
	<h5>변경사항</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>
<table class="popup_table">
	<thead>
		<tr>
			<th>파일명</th>
			<th>일시</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${fn:length(result.fileList) > 0 }">
				<c:forEach var="fileHistList" items="${result.fileList }" varStatus="status">
					<tr>
						<td class="acenter">
							<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${fileHistList.fileSeq }">${fileHistList.fileFullNm }</a>
						</td>
						<td class="acenter">${fileHistList.regTimestamp }</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="2" class="acenter">데이터가 존재하지 않습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>