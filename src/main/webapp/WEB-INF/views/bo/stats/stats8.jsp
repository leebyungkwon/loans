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
	location.href = "/admin/stats8/excelDown";
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>해지처리현황(모집인별)</h2>
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
			<table style="width: auto;">
				<thead>
					<tr>
						<th rowspan="4">회사명</th>
						<th colspan="12">해지 신청접수</th>
						<th colspan="12">해지처리 예정</th>
						<th colspan="12">해지 처리완료</th>
					</tr>
					<tr>
						<th colspan="4">대출</th>
						<th colspan="4">리스할부</th>
						<th colspan="4">계</th>
						<th colspan="4">대출</th>
						<th colspan="4">리스할부</th>
						<th colspan="4">계</th>
						<th colspan="4">대출</th>
						<th colspan="4">리스할부</th>
						<th colspan="4">계</th>
					</tr>
					<tr>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
						<th colspan="2">개인</th>
						<th colspan="2">법인</th>
					</tr>
					<tr>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
						<th>직속</th>
						<th>모집법인<br>소속</th>
						<th>오프라인</th>
						<th>TM법인</th>
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
									<td>${list.total1 }</td>
									<td>${list.total2 }</td>
									<td>${list.total3 }</td>
									<td>${list.total4 }</td>
									<td>${list.case9 }</td>
									<td>${list.case10 }</td>
									<td>${list.case11 }</td>
									<td>${list.case12 }</td>
									<td>${list.case13 }</td>
									<td>${list.case14 }</td>
									<td>${list.case15 }</td>
									<td>${list.case16 }</td>
									<td>${list.total5 }</td>
									<td>${list.total6 }</td>
									<td>${list.total7 }</td>
									<td>${list.total8 }</td>
									<td>${list.case17 }</td>
									<td>${list.case18 }</td>
									<td>${list.case19 }</td>
									<td>${list.case20 }</td>
									<td>${list.case21 }</td>
									<td>${list.case22 }</td>
									<td>${list.case23 }</td>
									<td>${list.case24 }</td>
									<td>${list.total9 }</td>
									<td>${list.total10 }</td>
									<td>${list.total11 }</td>
									<td>${list.total12 }</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="37">데이터가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
</div>

