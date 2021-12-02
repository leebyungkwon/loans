<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
			<h2>모집인 조회 및 변경 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
		
	<div class="contents">
		<div class="data_wrap">
			<h3>첨부서류</h3>
			<div id="table1">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tbody>
						<tr>
							<th class="acenter">물적설비내역 확인서 *</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType42 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType42.fileSeq }">${result.recruitInfo.fileType42.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType42}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType42.fileGrpSeq }', '42');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="acenter">물적설비내역 증빙서류 *</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType21 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType21.fileSeq }">${result.recruitInfo.fileType21.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType21}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType21.fileGrpSeq }', '21');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="acenter">사무공간 배치현황 *</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType22 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType22.fileSeq }">${result.recruitInfo.fileType22.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType22}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType22.fileGrpSeq }', '22');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="acenter">고정사업장 증빙서류 *</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType23 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType23.fileSeq }">${result.recruitInfo.fileType23.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType23}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType23.fileGrpSeq }', '23');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="acenter">결격사유 없음 확인서(법인) *</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType41 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType41.fileSeq }">${result.recruitInfo.fileType41.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType41}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType41.fileGrpSeq }', '41');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="acenter">기업신용정보 조회서(최근 3년간 이력) *</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType29 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType29.fileSeq }">${result.recruitInfo.fileType29.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType29}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType29.fileGrpSeq }', '29');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="acenter">법인인감증명서 *</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType43 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType43.fileSeq }">${result.recruitInfo.fileType43.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType43}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType43.fileGrpSeq }', '43');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
						<tr>
							<th class="acenter">개인신용정보 보관 전산설비 관련 확인서</th>
							<td>
								<c:choose>
									<c:when test="${result.recruitInfo.fileType44 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType44.fileSeq }">${result.recruitInfo.fileType44.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
								<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
									<c:if test="${!empty result.recruitInfo.histFileType44}">
										<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType44.fileGrpSeq }', '44');">변경사항</a>
									</c:if>
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goRecruitList();">목록</a>
		</div>
	</div>
</div>

