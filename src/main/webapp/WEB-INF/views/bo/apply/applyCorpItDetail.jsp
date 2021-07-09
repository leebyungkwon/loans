<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/recruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.applyInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 조회 및 변경 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('3');">전문성 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.itList) > 0 }">
				<c:forEach var="corpItList" items="${result.itList }" varStatus="status">
					<div class="data_wrap">
						<h3>기본정보</h3>
						<div id="table">
							<table class="view_table">
								<colgroup>
									<col width="15%">
									<col width="35%">
									<col width="15%">
									<col width="35%">
								</colgroup>
								<tbody>
									<tr>
										<th>이름</th>
										<td>${corpItList.operName }</td>
									</tr>
								</tbody>
							</table>
						</div>
				
						<h3>전산인력관련 서류</h3>
						<div id="table10">
							<table class="view_table border_table">
								<colgroup>
									<col width="50%"/>
									<col width="20%"/>
									<col width="30%"/>
								</colgroup>
								<tr>
									<th>구분</th>
									<th>첨부이미지</th>
									<th>체크사항</th>
								</tr>
								<tr>
									<td class="acenter">경력증명서(전산인력)</td>
									<td>
										<c:choose>
											<c:when test="${corpItList.fileType19 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpItList.fileType19.fileSeq }">${corpItList.fileType19.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd300" class="check check_cd" <c:if test="${!empty corpItList.checkCd300}">checked</c:if>
											<c:if test="${empty corpItList.fileType19.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpItList.fileType19.fileSeq }" >
											<label for="check_cd300">최근5년간 업무 기재 여부</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd301" class="check check_cd" <c:if test="${!empty corpItList.checkCd301}">checked</c:if>
											<c:if test="${empty corpItList.fileType19.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpItList.fileType19.fileSeq }" >
											<label for="check_cd301">근로계약서, 4대보험 가입증명서류</label>
										</div>
									</td>
								</tr>
								<tr>
									<td class="acenter">자격확인 서류(전산인력)</td>
									<td>
										<c:choose>
											<c:when test="${corpItList.fileType20 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpItList.fileType20.fileSeq }">${corpItList.fileType20.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd302" class="check check_cd" <c:if test="${!empty corpItList.checkCd302}">checked</c:if>
											<c:if test="${empty corpItList.fileType20.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpItList.fileType20.fileSeq }" >
											<label for="check_cd302">체크사항1</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd303" class="check check_cd" <c:if test="${!empty corpItList.checkCd303}">checked</c:if>
											<c:if test="${empty corpItList.fileType20.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpItList.fileType20.fileSeq }" >
											<label for="check_cd303">체크사항2</label>
										</div>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="data_wrap">
					<div id="table02">
						<table class="view_table">
							<colgroup>
								<col width="15%">
								<col width="35%">
								<col width="15%">
								<col width="35%">
							</colgroup>
							<tbody>
								<tr>
									<td colspan="3" style="text-align: center; font-weight: bold;">등록된 전산인력이 존재하지 않습니다.</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goApplyList();">목록</a>
		</div>
	</div>
</div>

