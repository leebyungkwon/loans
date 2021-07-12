<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
		
	<div class="contents">
		<div class="data_wrap">
			<h3>1. 물적설비관련 서류</h3>
			<div id="table1">
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
						<td class="acenter">물적 설비내역에 대한 증빙서류 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType21 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType21.fileSeq }">${result.applyInfo.fileType21.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd400" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd400}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType21.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType21.fileSeq }" >
								<label for="check_cd400">체크사항1</label>
							</div>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd401" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd401}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType21.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType21.fileSeq }" >
								<label for="check_cd401">체크사항2</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="acenter">사무공간 / 전산설비 등의 임차계약서 사본 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType22 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType22.fileSeq }">${result.applyInfo.fileType22.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd402" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd402}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType22.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType22.fileSeq }" >
								<label for="check_cd402">사무공간 자료 유무</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="acenter">부동산 등기부등본 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType23 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType23.fileSeq }">${result.applyInfo.fileType23.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd403" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd403}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType23.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType23.fileSeq }" >
								<label for="check_cd403">기재 내용 일치 여부</label>
							</div>
						</td>
					</tr>
				</table>
			</div>
			
			<h3>2. 사회적 신용</h3>
			<div id="table2">
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
					<%-- 
					
					<tr>
						<td class="acenter">신청인의 사회적신용에 대한 결격사유없음 확인서 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType24 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType24.fileSeq }">${result.applyInfo.fileType24.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd404" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd404}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType24.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType24.fileSeq }" >
								<label for="check_cd404">결격사유 유무 검증</label>
							</div>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd405" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd405}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType24.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType24.fileSeq }" >
								<label for="check_cd405">서명 누락 여부</label>
							</div>
						</td>
					</tr>
					 --%>
					
					
					<tr>
						<td class="acenter">기업 신용정보조회서 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType29 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType29.fileSeq }">${result.applyInfo.fileType29.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd408" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd408}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType29.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType29.fileSeq }" >
								<label for="check_cd408">체크사항1</label>
							</div>
						</td>
					</tr>
				</table>
			</div>
			
			<h3>3. 기타</h3>
			<div id="table3">
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
						<td class="acenter">대리인 신청 위임장 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType25 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType25.fileSeq }">${result.applyInfo.fileType25.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd406" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd406}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType25.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType25.fileSeq }" >
								<label for="check_cd406">인감 날인 여부</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="acenter">위임인 인감증명서 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType26 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType26.fileSeq }">${result.applyInfo.fileType26.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd407" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd407}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType26.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType26.fileSeq }" >
								<label for="check_cd407">유효 증명서 여부</label>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>

		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goApplyList();">목록</a>
		</div>
	</div>
</div>

