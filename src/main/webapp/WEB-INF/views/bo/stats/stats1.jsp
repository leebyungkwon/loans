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
	
	//datepicker
	$("#date_cal01").datepicker({
		 dateFormat	: "yy-mm-dd"
	 	,changeMonth: true
		,changeYear	: true
		,onSelect	: function(dateText1,inst) {
			$("#srchDate1").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal02").datepicker({
		 dateFormat	: "yy-mm-dd"
	 	,changeMonth: true
		,changeYear	: true
		,onSelect	: function(dateText1,inst) {
			$("#srchDate2").val(dateText1);
			$(this).hide();
		}
	});
}

$(document).mouseup(function(e){
	var calendar01 = $(".calendar01");
	if(calendar01.has(e.target).length === 0){
		calendar01.hide();
	}
});

function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}

function excelDown() {
	$("#statsFrm").attr("action","/admin/stats1/excelDown");
	$("#statsFrm").submit();
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회사별 등록 모집인 현황</h2>
		</div>
		<div class="info_box">
			<table class="info_box_table" style="width: 90%;">
				<tr>
					<td>
						1. 기준일자의 각 회사별 자격취득(등록번호 부여) 모집인 수<br>
						2. 등록건수 기준
					</td>
				</tr>
			</table>
		</div>
		<div class="info_box">
			<form id="statsFrm" action="/admin/stats1/statsPage">
				<table class="info_box_table" style="width: 90%;">
					<colgroup>
						<col width="10%">
						<col width="23%">
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
								<option value="3" <c:if test="${plRegStat eq '3' }">selected="selected"</c:if>>자격취득(결제완료 포함)</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>자격취득일</th>
						<td colspan="5" class="long_input">
							<div class="input_wrap">
		               			<input type="text" name="srchDate1" id="srchDate1" value="${srchDate1 }" class="input_calendar" readonly="readonly" onclick="$('#date_cal01').show();">
		               			<a class="calendar_ico" onclick="$('#date_cal01').show();"></a>
							 	<div id="date_cal01" class="calendar01"></div>
		             			</div>
						  	~
						 	<div class="input_wrap mgr5">
								<input type="text" name="srchDate2" id="srchDate2" value="${srchDate2 }" class="input_calendar" readonly="readonly" onclick="$('#date_cal02').show();">
								<a class="calendar_ico" onclick="$('#date_cal02').show();"></a>
								<div id="date_cal02" class="calendar01"></div>
							</div>
							<div class="date_btn">
								<a href="javascript:void(0);" onclick="goGetDate('today');">오늘</a>
								<a href="javascript:void(0);" onclick="goGetDate('1');">어제</a>
								<a href="javascript:void(0);" onclick="goGetDate('7');">1주일</a>
								<a href="javascript:void(0);" onclick="goGetDate('15');">15일</a>
								<a href="javascript:void(0);" onclick="goGetDate('oneMonthAgo');">1개월</a>
							</div>
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
						<th rowspan="3"></th>
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
									<td>${list.allCnt }</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="14">데이터가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
</div>

