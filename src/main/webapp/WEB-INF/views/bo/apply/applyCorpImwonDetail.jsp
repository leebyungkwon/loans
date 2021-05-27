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
			<li class="on"><a href="javascript:void(0);" onclick="goTab3('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
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
										<td>${result.applyInfo.plProductNm }</td>
									</tr>
									<tr>
										<th>교육이수번호</th>
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
								</tbody>
							</table>
						</div>
						
						<h3>1. 대표 및 임원관련 서류</h3>
						<div id="table06">
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
									<td class="acenter">대표자 이력서</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType7 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType7.fileSeq }">${corpImwonList.fileType7.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd100" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd100}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType7.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType7.fileSeq }" >
											<label for="check_cd100">인감도장 날인여부</label>
										</div>
									</td>
								</tr>
								<tr>
									<td class="acenter">대표자 경력증명서</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType8 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType8.fileSeq }">${corpImwonList.fileType8.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd101" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd101}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType8.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType8.fileSeq }" >
											<label for="check_cd101">최근5년간 업무 기재</label>
										</div>
									</td>
								</tr>
								<tr>
									<td class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType9 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType9.fileSeq }">${corpImwonList.fileType9.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd102" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd102}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType9.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType9.fileSeq }" >
											<label for="check_cd102">결격사유 유무 검증</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd103" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd103}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType9.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType9.fileSeq }" >
											<label for="check_cd103">서명 누락 여부</label>
										</div>
									</td>
								</tr>
								<tr>
									<td class="acenter">인감증명서</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType10 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType10.fileSeq }">${corpImwonList.fileType10.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd104" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd104}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType10.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType10.fileSeq }" >
											<label for="check_cd104">결격사유 유무 검증</label>
										</div>
									</td>
								</tr>
							</table>
						</div>
						
						<h3>2. 금융상품 관련 서류</h3>
						<div id="table07">
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
									<td class="acenter">금융상품 유형, 내용에 대한 설명자료</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType11 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType11.fileSeq }">${corpImwonList.fileType11.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd105" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd105}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType11.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType11.fileSeq }" >
											<label for="check_cd105">기재사항 일치여부</label>
										</div>
									</td>
								</tr>
							</table>
						</div>
						
						<h3>3. 교육이수관련 서류</h3>
						<div id="table08">
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
									<td class="acenter">대표 교육과정 이수확인서 (경력)</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType12 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType12.fileSeq }">${corpImwonList.fileType12.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd106" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd106}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType12.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType12.fileSeq }" >
											<label for="check_cd106">교육이수 및 인증내역 검증</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd107" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd107}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType12.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType12.fileSeq }" >
											<label for="check_cd107">교육기관 직인 날인 여부</label>
										</div>
									</td>
								</tr>
								<tr>
									<td class="acenter">대표 인증서(신규)</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType13 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType13.fileSeq }">${corpImwonList.fileType13.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd108" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd108}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType13.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType13.fileSeq }" >
											<label for="check_cd108">교육이수 및 인증내역 검증</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd109" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd109}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType13.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType13.fileSeq }" >
											<label for="check_cd109">교육기관 직인 날인 여부</label>
										</div>
									</td>
								</tr>
								<tr>
									<td class="acenter">대표 경력증명서</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType14 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType14.fileSeq }">${corpImwonList.fileType14.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd110" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd110}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType14.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType14.fileSeq }" >
											<label for="check_cd108">경력 인정여부</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd111" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd111}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType14.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType14.fileSeq }" >
											<label for="check_cd111">최근5년간 업무 기재 여부</label>
										</div>
									</td>
								</tr>
							</table>
						</div>
						
						<h3>4. 업무수행기준요건관련 서류</h3>
						<div id="table09">
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
									<td class="acenter">업무수행기준요건관련 서류</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType15 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType15.fileSeq }">${corpImwonList.fileType15.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd112" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd112}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType15.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType15.fileSeq }" >
											<label for="check_cd112">금소법상 필요사항 포함여부</label>
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
									<td colspan="3" style="text-align: center; font-weight: bold;">등록된 대표자 및 임원관련 사항이 존재하지 않습니다.</td>
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

