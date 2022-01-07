<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style>
table td{padding: 5px 10px;}
</style>

<script type="text/javascript">
function pageLoad() {

}

function excelDown() {
	location.href = "/admin/stats9/excelDown";
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>불법모집 이력현황</h2>
		</div>
		<div class="info_box">
			<table class="info_box_table" style="width: 90%;">
				<tr>
					<td>
						1. 각 회사별 불법모집 이력 등록건수
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div class="contents" style="overflow-x: auto;">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="excelDown();">다운로드</a>
			</div>
		</div>
		<div style="overflow-x: auto; overflow-y: auto; max-height: 900px;">
			<table style="width: max-content;">
				<thead>
					<tr>
						<th rowspan="4">회사명</th>
						<c:forEach var="cdList" items="${violationCdList }" varStatus="status">
							<th colspan="8">${cdList.codeDtlNm }(${cdList.codeDtlCd })</th>
						</c:forEach>
					</tr>
					<tr>
						<c:forEach var="cdList" items="${violationCdList }" varStatus="status">
							<th colspan="4">대출</th>
							<th colspan="4">리스할부</th>
						</c:forEach>
					</tr>
					<tr>
						<c:forEach var="cdList" items="${violationCdList }" varStatus="status">
							<th colspan="2">개인</th>
							<th colspan="2">법인</th>
							<th colspan="2">개인</th>
							<th colspan="2">법인</th>
						</c:forEach>
					</tr>
					<tr>
						<c:forEach var="cdList" items="${violationCdList }" varStatus="status">
							<th>직속</th>
							<th>모집법인<br>소속</th>
							<th>오프라인</th>
							<th>TM법인</th>
							<th>직속</th>
							<th>모집법인<br>소속</th>
							<th>오프라인</th>
							<th>TM법인</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(result) > 0 }">
							<c:forEach var="list" items="${result }" varStatus="status">
								<tr>
									<td>${list.name }</td>
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
									<td>${list.case13 }</td>
									<td>${list.case14 }</td>
									<td>${list.case15 }</td>
									<td>${list.case16 }</td>
									<td>${list.case17 }</td>
									<td>${list.case18 }</td>
									<td>${list.case19 }</td>
									<td>${list.case20 }</td>
									<td>${list.case21 }</td>
									<td>${list.case22 }</td>
									<td>${list.case23 }</td>
									<td>${list.case24 }</td>
									<td>${list.case25 }</td>
									<td>${list.case26 }</td>
									<td>${list.case27 }</td>
									<td>${list.case28 }</td>
									<td>${list.case29 }</td>
									<td>${list.case30 }</td>
									<td>${list.case31 }</td>
									<td>${list.case32 }</td>
									<td>${list.case33 }</td>
									<td>${list.case34 }</td>
									<td>${list.case35 }</td>
									<td>${list.case36 }</td>
									<td>${list.case37 }</td>
									<td>${list.case38 }</td>
									<td>${list.case39 }</td>
									<td>${list.case40 }</td>
									<td>${list.case41 }</td>
									<td>${list.case42 }</td>
									<td>${list.case43 }</td>
									<td>${list.case44 }</td>
									<td>${list.case45 }</td>
									<td>${list.case46 }</td>
									<td>${list.case47 }</td>
									<td>${list.case48 }</td>
									<td>${list.case49 }</td>
									<td>${list.case50 }</td>
									<td>${list.case51 }</td>
									<td>${list.case52 }</td>
									<td>${list.case53 }</td>
									<td>${list.case54 }</td>
									<td>${list.case55 }</td>
									<td>${list.case56 }</td>
									<td>${list.case57 }</td>
									<td>${list.case58 }</td>
									<td>${list.case59 }</td>
									<td>${list.case60 }</td>
									<td>${list.case61 }</td>
									<td>${list.case62 }</td>
									<td>${list.case63 }</td>
									<td>${list.case64 }</td>
									<td>${list.case65 }</td>
									<td>${list.case66 }</td>
									<td>${list.case67 }</td>
									<td>${list.case68 }</td>
									<td>${list.case69 }</td>
									<td>${list.case70 }</td>
									<td>${list.case71 }</td>
									<td>${list.case72 }</td>
									<td>${list.case73 }</td>
									<td>${list.case74 }</td>
									<td>${list.case75 }</td>
									<td>${list.case76 }</td>
									<td>${list.case77 }</td>
									<td>${list.case78 }</td>
									<td>${list.case79 }</td>
									<td>${list.case80 }</td>
									<td>${list.case81 }</td>
									<td>${list.case82 }</td>
									<td>${list.case83 }</td>
									<td>${list.case84 }</td>
									<td>${list.case85 }</td>
									<td>${list.case86 }</td>
									<td>${list.case87 }</td>
									<td>${list.case88 }</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="89">데이터가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
</div>

