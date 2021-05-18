<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
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
			<li><a href="javascript:void(0);" onclick="goTab2('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
		
	<div class="contents">
		<div class="data_wrap">
			<h3>1. 물적설비관련 서류</h3>
			<div id="table1">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tbody>
						<tr>
							<th class="acenter">물적 설비내역에 대한 증빙서류</th>
							<td>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType21 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType21.fileSeq }">${result.userRegInfo.fileType21.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th class="acenter">사무공간 / 전산설비 등의 임차계약서 사본</th>
							<td>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType22 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType22.fileSeq }">${result.userRegInfo.fileType22.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th class="acenter">부동산 등기부등본</th>
							<td>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType23 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType23.fileSeq }">${result.userRegInfo.fileType23.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<h3>2. 사회적 신용</h3>
			<div id="table2">
				<table class="view_table">
					<colgroup>
						<col width="38%">
						<col width="62%">
					</colgroup>
					<tbody>
						<tr>
							<th class="acenter">신청인의 사회적신용에 대한 결격사유없음 확인서</th>
							<td>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType24 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType24.fileSeq }">${result.userRegInfo.fileType24.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<h3>3. 기타</h3>
			<div id="table3">
				<table class="view_table">
					<colgroup>
						<col width="38%">
						<col width="62%">
					</colgroup>
					<tbody>
						<tr>
							<th class="acenter">대리인 신청 위임장 (위임인 인감날인)</th>
							<td>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType25 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType25.fileSeq }">${result.userRegInfo.fileType25.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th class="acenter">위임인 인감증명서</th>
							<td>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType26 ne null }">
										<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType26.fileSeq }">${result.userRegInfo.fileType26.fileFullNm }</a>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
		</div>
	</div>
</div>

