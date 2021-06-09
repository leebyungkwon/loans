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
			<li class="on"><a href="javascript:void(0);" onclick="goTab3('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.expertList) > 0 }">
				<c:forEach var="corpExpertList" items="${result.expertList }" varStatus="status">
					<div class="data_wrap">
						<h3>기본정보</h3>
						<div id="table03">
							<table class="view_table">
								<colgroup>
									<col width="15%">
									<col width="35%">
									<col width="15%">
									<col width="35%">
								</colgroup>
								<tbody>
									<tr>
										<th>신규경력 구분</th>
										<td colspan="3">${corpExpertList.careerTypNm }</td>
									</tr>
									<tr>
										<th>이름</th>
										<td>${corpExpertList.expName }</td>
										<th>주민번호</th>
										<td>${corpExpertList.plMZId }</td>
									</tr>
									<tr>
										<th>금융상품유형</th>
										<td colspan="3">${result.applyInfo.plProductNm }</td>
									</tr>
									<tr>
										<th>교육이수번호</th>
										<td colspan="3">${corpExpertList.plEduNo }</td>
									</tr>
									<tr>
										<th>경력시작일</th>
										<td>${corpExpertList.careerStartDate }</td>
										<th>경력종료일</th>
										<td>${corpExpertList.careerEndDate }</td>
									</tr>
								</tbody>
							</table>
						</div>
				
						<h3>전문인력관련 서류</h3>
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
								<c:if test="${corpExpertList.careerTyp eq '2' }">
									<tr>
										<td class="acenter">경력교육과정 수료증 *</td>
										<td>
											<c:choose>
												<c:when test="${corpExpertList.fileType16 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType16.fileSeq }">${corpExpertList.fileType16.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
										<td>
											<div class="input_check_wrap mgr10">
												<input type="checkbox" id="check_cd200" class="check check_cd" <c:if test="${!empty corpExpertList.checkCd200}">checked</c:if>
												<c:if test="${empty corpExpertList.fileType16.fileSeq}">disabled</c:if>
												 data-fileSeq="${corpExpertList.fileType16.fileSeq }" >
												<label for="check_cd200">교육이수 및 인증내역 검증</label>
											</div>
											<div class="input_check_wrap mgr10">
												<input type="checkbox" id="check_cd201" class="check check_cd" <c:if test="${!empty corpExpertList.checkCd201}">checked</c:if>
												<c:if test="${empty corpExpertList.fileType16.fileSeq}">disabled</c:if>
												 data-fileSeq="${corpExpertList.fileType16.fileSeq }" >
												<label for="check_cd201">교육기관 직인 날인</label>
											</div>
										</td>
									</tr>
									
								</c:if>
								
								<c:if test="${corpExpertList.careerTyp eq '1' }">
									<tr>
										<td class="acenter">인증서(신규) *</td>
										<td>
											<c:choose>
												<c:when test="${corpExpertList.fileType17 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType17.fileSeq }">${corpExpertList.fileType17.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
										<td>
											<div class="input_check_wrap mgr10">
												<input type="checkbox" id="check_cd202" class="check check_cd" <c:if test="${!empty corpExpertList.checkCd202}">checked</c:if>
												<c:if test="${empty corpExpertList.fileType17.fileSeq}">disabled</c:if>
												 data-fileSeq="${corpExpertList.fileType17.fileSeq }" >
												<label for="check_cd202">교육이수 및 인증내역 검증</label>
											</div>
											<div class="input_check_wrap mgr10">
												<input type="checkbox" id="check_cd203" class="check check_cd" <c:if test="${!empty corpExpertList.checkCd203}">checked</c:if>
												<c:if test="${empty corpExpertList.fileType17.fileSeq}">disabled</c:if>
												 data-fileSeq="${corpExpertList.fileType17.fileSeq }" >
												<label for="check_cd203">교육기관 직인 날인</label>
											</div>
										</td>
									</tr>
								</c:if>
								<tr>
									<td class="acenter">경력증명서 (업무인력) *</td>
									<td>
										<c:choose>
											<c:when test="${corpExpertList.fileType18 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType18.fileSeq }">${corpExpertList.fileType18.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd204" class="check check_cd" <c:if test="${!empty corpExpertList.checkCd204}">checked</c:if>
											<c:if test="${empty corpExpertList.fileType18.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpExpertList.fileType18.fileSeq }" >
											<label for="check_cd204">경력 인정여부</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd205" class="check check_cd" <c:if test="${!empty corpExpertList.checkCd205}">checked</c:if>
											<c:if test="${empty corpExpertList.fileType18.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpExpertList.fileType18.fileSeq }" >
											<label for="check_cd205">최근5년간 업무 기재 여부</label>
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
									<td colspan="3" style="text-align: center; font-weight: bold;">등록된 전문인력이 존재하지 않습니다.</td>
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

