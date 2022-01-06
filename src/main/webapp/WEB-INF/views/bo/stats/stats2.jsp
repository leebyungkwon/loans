<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style>
table td{padding: 5px 10px;}
</style>

<script type="text/javascript">
function pageLoad() {
	$("#searchBtn").on("click",function(){
		$("#statsFrm").submit();
	});
}

function excelDown() {
	location.href = "/admin/stats2/excelDown";
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>경력신규</h2>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="excelDown();">다운로드</a>
			</div>
		</div>
		<table>
			<thead>
				<tr>
					<th rowspan="4">구분</th>
					<th colspan="6">승인요청(보완요청 포함)</th>
					<th colspan="6">승인완료</th>
					<th colspan="6">자격취득(결제완료 포함)</th>
				</tr>
				<tr>
					<th colspan="2">대출</th>
					<th colspan="2">리스할부</th>
					<th colspan="2">계</th>
					<th colspan="2">대출</th>
					<th colspan="2">리스할부</th>
					<th colspan="2">계</th>
					<th colspan="2">대출</th>
					<th colspan="2">리스할부</th>
					<th colspan="2">계</th>
				</tr>
				<tr>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
					<th colspan="2">개인</th>
				</tr>
				<tr>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
					<th>직속</th>
					<th>모집법인<br>소속</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${fn:length(result) > 0 }">
						<c:forEach var="list" items="${result }" varStatus="status">
							<tr>
								<td>${list.careerTypNm }</td>
								<td>${list.case1 }</td>
								<td>${list.case2 }</td>
								<td>${list.case3 }</td>
								<td>${list.case4 }</td>
								<td>${list.case5 }</td>
								<td>${list.case6 }</td>
								<td>${list.case7 }</td>
								<td>${list.case8 }</td>
								<td>${list.case9 }</td>
								<td>${list.case10 }</td>
								<td>${list.case11 }</td>
								<td>${list.case12 }</td>
								<td>${list.total1 }</td>
								<td>${list.total2 }</td>
								<td>${list.total3 }</td>
								<td>${list.total4 }</td>
								<td>${list.total5 }</td>
								<td>${list.total6 }</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="19">데이터가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>

