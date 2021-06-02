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
			<th>변경 전</th>
			<th>변경 후</th>
			<th>일시</th>
		</tr>
		
	</thead>
	<tbody>
		<tr>  
			<td class="acenter">
				<c:choose>
					<c:when test="${recruitHistDetail.histType eq 'keyName' }">
						${recruitHistDetail.plMName }	
					</c:when>
					<c:when test="${recruitHistDetail.histType eq 'keyId' }">
						${recruitHistDetail.plMZId }
					</c:when>
					<c:when test="${recruitHistDetail.histType eq 'keyPhone' }">
						${recruitHistDetail.plCellphone }
					</c:when>
				</c:choose>
			</td>
			<td class="acenter">
				${afterData}	
			</td>
			<td class="acenter">${recruitHistDetail.regTimestamp }</td>
		</tr>
	</tbody>
</table>