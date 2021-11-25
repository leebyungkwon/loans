<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newRecruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.recruitInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 조회 및 해지 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('1');">등록정보</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab2('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
		
	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.imwonList) > 0 }">
				<c:forEach var="corpImwonList" items="${result.imwonList }" varStatus="status">
					<div class="data_wrap">
						<h3>기본정보</h3>
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
										<th>신규경력 구분</th>
										<td colspan="3">${corpImwonList.careerTypNm }</td>
									</tr>
									<tr>
										<th>이름</th>
										<td>${corpImwonList.excName }</td>
										<th>주민번호</th>
										<td>${corpImwonList.plMZId }</td>
									</tr>
									<tr>
										<th>직위</th>
										<td>${corpImwonList.positionNm }</td>
										<th>금융상품유형</th>
										<td>${result.recruitInfo.plProductNm }</td>
									</tr>
									<tr>
										<th>교육이수번호/인증서번호</th>
										<td colspan="3">${corpImwonList.plEduNo }</td>
									</tr>
									<tr>
										<th>경력시작일</th>
										<td>${corpImwonList.careerStartDate }</td>
										<th>경력종료일</th>
										<td>${corpImwonList.careerEndDate }</td>
									</tr>
									<tr>
										<th>상근여부</th>
										<td>${corpImwonList.fullTmStatNm }</td>
										<th>전문인력여부</th>
										<td>${corpImwonList.expertStatNm }</td>
									</tr>
									<tr>
										<th>임원자격 적합여부</th>
										<td colspan="3">${corpImwonList.properCdNm }</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<h3>첨부서류</h3>
						<div id="table06">
							<table class="view_table">
								<colgroup>
									<col width="38%">
									<col width="62%">
								</colgroup>
								<tbody>
									<tr>
										<th class="acenter">이력서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType7 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType7.fileSeq }">${corpImwonList.fileType7.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<th class="acenter">개인정보필수동의서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType34 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType34.fileSeq }">${corpImwonList.fileType34.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<c:if test="${corpImwonList.careerTyp eq '1' }">
										<tr>
											<th class="acenter">인증서</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType13 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType13.fileSeq }">${corpImwonList.fileType13.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<c:if test="${corpImwonList.careerTyp eq '2' }">
										<tr>
											<th class="acenter">교육수료증</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType12 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType12.fileSeq }">${corpImwonList.fileType12.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">경력증명서</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType8 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType8.fileSeq }">${corpImwonList.fileType8.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">위탁계약서</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType11 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType11.fileSeq }">${corpImwonList.fileType11.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">위탁 금융상품직접판매업자 확인서</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType28 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType28.fileSeq }">${corpImwonList.fileType28.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<tr>
										<th class="acenter">결격사유 없음 확인서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType27 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType27.fileSeq }">${corpImwonList.fileType27.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									
									<tr>
										<th class="acenter">본국 감독당국의 결격요건,범죄이력 확인서류(공증필요)</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType46 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType46.fileSeq }">${corpImwonList.fileType46.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									
									<tr>
										<th class="acenter">본국 감독당국의 결격요건,범죄이력 확인서류의 번역본</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType47 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType47.fileSeq }">${corpImwonList.fileType47.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									
								</tbody>
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
									<td colspan="3" style="text-align: center; font-weight: bold;">등록된 대표자 및 임원관련 사항이 존재하지 않습니다.</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goRecruitList();">목록</a>
		</div>
	</div>
</div>

