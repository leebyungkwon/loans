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
	$("#statsFrm").attr("action","/admin/stats1/excelDown");
	$("#statsFrm").submit();
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회사별 모집인</h2>
		</div>
		<div class="info_box">
			<form id="statsFrm" action="/admin/stats1/statsPage">
				<table class="info_box_table" style="width: 90%;">
					<colgroup>
						<col width="10%">
						<col width="23%">
						<col width="10%">
						<col width="23%">
					</colgroup>
					<tr>
						<th>상태</th>
						<td>
							<select name="plRegStat">
								<option value="" <c:if test="${plRegStat eq '' }">selected="selected"</c:if>>전체</option>
								<option value="1" <c:if test="${plRegStat eq '1' }">selected="selected"</c:if>>승인요청(보완요청 포함)</option>
								<option value="2" <c:if test="${plRegStat eq '2' }">selected="selected"</c:if>>승인완료</option>
								<option value="3" <c:if test="${plRegStat eq '3' }">selected="selected"</c:if>>자격취득(결제완료 포함)</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="excelDown();">다운로드</a>
			</div>
		</div>
		<div style="overflow-y: auto; max-height: 900px;">
			<table>
				<thead>
					<tr>
						<th rowspan="3">회사명</th>
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
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(result) > 0 }">
							<c:forEach var="list" items="${result }" varStatus="status">
								<tr>
									<td>${list.comCodeNm }</td>
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
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="13">데이터가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
</div>

