<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/static/js/newRecruit/common.js"></script>

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
			<h2>모집인 등록 승인처리 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
		
	<div class="contents">
		<div class="data_wrap">
			<h3>첨부서류</h3>
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
						<td class="acenter">물적설비 증빙서류 *</td>
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
								<label for="check_cd400">법인 인감 날인 여부</label>
							</div>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd401" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd401}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType21.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType21.fileSeq }" >
								<label for="check_cd401">물적요건 설비내역 및 증빙자료 제출 여부</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="acenter">사무공간 배치현황 *</td>
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
								<label for="check_cd402">사무공간 배치현황 제출 여부</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="acenter">고정사업장 증빙서류 *</td>
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
								<label for="check_cd403">임차기간 6개월 이상 확보 여부</label>
							</div>
						</td>
					</tr>
					
					<tr>
						<td class="acenter">결격사유 없음 확인서 *</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType41 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType41.fileSeq }">${result.applyInfo.fileType41.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="input_check_wrap mgr10">
								<input type="checkbox" id="check_cd409" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd409}">checked</c:if>
								<c:if test="${empty result.applyInfo.fileType41.fileSeq}">disabled</c:if>
								 data-fileSeq="${result.applyInfo.fileType41.fileSeq }" >
								<label for="check_cd409">확인</label>
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

