<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %>

<%@ page language="java" contentType="application/vnd.ms-excel;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
	Date currentDt = new Date();
	String today = dateFormatParser.format(currentDt);
	String fileName = "부적격처리건수_"+today;
	fileName = new String((fileName).getBytes("KSC5601"),"8859_1");
	
	response.setContentType("application/vnd.ms-excel"); //바로저장
	response.setHeader("Content-Disposition","attachment;filename="+fileName+".xls"); //디폴트 파일명 지정
	response.setHeader("Content-Description","JSP Generated Data"); 
%>

<style>
table th{border: 1px solid #ccc;}
table td{border: 1px solid #ccc;}
</style>

<body>
	<table>
		<thead>
			<tr>
				<th colspan="13">부적격처리건수</th>
			</tr>
			<tr>
				<th rowspan="3">구분</th>
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
				<th>모집법인소속</th>
				<th>오프라인</th>
				<th>TM법인</th>
				<th>직속</th>
				<th>모집법인소속</th>
				<th>오프라인</th>
				<th>TM법인</th>
				<th>직속</th>
				<th>모집법인소속</th>
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
</body>
