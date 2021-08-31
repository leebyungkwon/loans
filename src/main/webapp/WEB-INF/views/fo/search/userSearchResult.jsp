<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<script type="text/javascript">
function pageLoad() {
	
}
</script>

<div class="inquiry_result_wrap">
	<div class="title">대출모집인 조회결과</div>
	<div class="inner">
		<div class="table_wrap">
			<div class="table_title">정보</div>
			<table class="member_info">
				<colgroup>
					<col width="210"/>
					<col width="*"/>
				</colgroup>
				
				<c:if test="${result.plClass eq '1' }">
					<tr>
						<th>등록번호</th>
						<td>${result.plRegistNo}</td>
					</tr>
					<tr>
						<th>성명</th>
						<td>${result.plMName }</td>
					</tr>
				</c:if>
				<c:if test="${result.plClass eq '2' }">
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
				</c:if>
			</table>
		</div>
		
		<c:choose>
			<c:when test="${fn:length(result.searchResultList) > 0 }">
				<c:forEach var="searchResultList" items="${result.searchResultList }" varStatus="status">
					<div class="table_wrap mgt60">
						<div class="table_title">상세정보(계약 금융기관 - ${searchResultList.finName })</div>
						<table class="member_info">
							<colgroup>
								<col width="210"/>
								<col width="*"/>
							</colgroup>
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
								<th>계약일자</th>
								<td>
									${fn:substring(searchResultList.conDate,0,4)}-${fn:substring(searchResultList.conDate,4,6)}-${fn:substring(searchResultList.conDate,6,8)}
								</td>
							</tr>
						</table>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>
	</div>
	<div class="tap_cont">
		<div class="bottom_box">
			<ul>
				<li>
					<span class="dot"></span>
					<p>취급상품이 리스로 표시되는 경우에는 할부와 오토론을 취급할 수도 있습니다.</p>
				</li>
			</ul>
		</div>
	</div>
	<div class="btn_wrap">
		<a href="/front/search/userSearchPage" class="btn_black">홈으로</a>
	</div>
</div>
      
      