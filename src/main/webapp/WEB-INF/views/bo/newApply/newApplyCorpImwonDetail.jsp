<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newRecruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
	$(".ocr_imwon_click").on("click", function(){
		var fileSeq = $(this).attr("data-imwon-file-seq");
		var excSeq = $(this).attr("data-imwon-seq");
		var dataIndex = $(this).attr("data-index");
		if(confirm("OCR 검증을 시작 하시겠습니까?")){
			var p = {
				  url		: "/admin/newApply/newCorpImwonOcr"
				, async		: false
				, param		: {
					excSeq 	: excSeq
					 , fileSeq	: fileSeq
				}
				, success 	: function (opt,result) {
					alert("완료");
					// fileType27 - 후견부존재증명
					// fileType12 - 경력
					// fileType13 - 신규일경우 인증서 추출
					
/* 					if(result.data.fileType27 == "충족"){
						ocrImwonSuccess("check_cd113", dataIndex);
						
					} */
					
					if(result.data.fileType12 == "일치"){
						ocrImwonSuccess("check_cd106", dataIndex);
						
					}if(result.data.fileType13 == "일치"){
						ocrImwonSuccess("check_cd108", dataIndex);
					}
			    }
			}
			AjaxUtil.post(p);
		}
	});
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" id="masterSeq" value="${result.applyInfo.masterSeq }"/>
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
			<li class="on"><a href="javascript:void(0);" onclick="goTab3('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
		
	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.imwonList) > 0 }">
				<c:forEach var="corpImwonList" items="${result.imwonList }" varStatus="status">
					<div class="data_wrap" id="index${status.index}">
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
										<th>이름</th>
										<td>${corpImwonList.excName }</td>
										<th>주민번호</th>
										<td>${corpImwonList.plMZId }</td>
									</tr>
									<tr>
										<th>신규경력 구분</th>
										<td>${corpImwonList.careerTypNm }</td>
										<th>직위</th>
										<td>${corpImwonList.positionCdNm }</td>
									</tr>
									<tr>
										<th>교육이수번호/인증서번호</th>
										<td colspan="3">${corpImwonList.plEduNo }</td>
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
							<table class="view_table border_table">
								<colgroup>
									<col width="40%"/>
									<col width="30%"/>
									<col width="30%"/>
								</colgroup>
								<tr>
									<th>구분</th>
									<th>첨부이미지</th>
									<th>체크사항</th>
								</tr>
								<tr>
									<td class="acenter">이력서 *</td>
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
											<label for="check_cd100">최근 5년간 이력 기재여부</label>
										</div>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd117" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd117}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType7.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType7.fileSeq }" >
											<label for="check_cd117">인감날임 또는 서명 여부</label>
										</div>
									</td>
								</tr>
								
								<tr>
									<td class="acenter">개인정보필수동의서(대표이사) *</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType34 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType34.fileSeq }">${corpImwonList.fileType34.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd118" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd118}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType34.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType34.fileSeq }" >
											<label for="check_cd118">대표자 및 임원용 동의서 작성 여부</label>
										</div>
									</td>
								</tr>
								
								<c:if test="${corpImwonList.careerTyp eq '1' }">
									<tr>
										<td class="acenter">인증서 *</td>
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
<%-- 											<div class="input_check_wrap mgr10">
												<input type="checkbox" id="check_cd109" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd109}">checked</c:if>
												<c:if test="${empty corpImwonList.fileType13.fileSeq}">disabled</c:if>
												 data-fileSeq="${corpImwonList.fileType13.fileSeq }" >
												<label for="check_cd109">교육기관 직인 날인 여부</label>
											</div> --%>
										</td>
									</tr>
								</c:if>
								
								<c:if test="${corpImwonList.careerTyp eq '2' }">
									<tr>
										<td class="acenter">교육수료증 *</td>
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
<%-- 											<div class="input_check_wrap mgr10">
												<input type="checkbox" id="check_cd107" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd107}">checked</c:if>
												<c:if test="${empty corpImwonList.fileType12.fileSeq}">disabled</c:if>
												 data-fileSeq="${corpImwonList.fileType12.fileSeq }" >
												<label for="check_cd107">교육기관 직인 날인 여부</label>
											</div> --%>
										</td>
									</tr>
									<tr>
										<td class="acenter">경력증명서</td>
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
												<label for="check_cd101">최근5년이내 3년이상 금융회사 경력여부</label>
											</div>
										</td>
									</tr>
										
									<tr>
										<td class="acenter">위탁계약서</td>
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
												<label for="check_cd105">경력인정 가능 여부</label>
											</div>
										</td>
									</tr>
									<tr>
										<td class="acenter">위탁 금융상품직접판매업자 확인서</td>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType28 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType28.fileSeq }">${corpImwonList.fileType28.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
										<td>
											<div class="input_check_wrap mgr10">
												<input type="checkbox" id="check_cd114" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd114}">checked</c:if>
												<c:if test="${empty corpImwonList.fileType28.fileSeq}">disabled</c:if>
												 data-fileSeq="${corpImwonList.fileType28.fileSeq }" >
												<label for="check_cd114">경력인정 가능 여부</label>
											</div>
										</td>
									</tr>
										
								</c:if>
								
								<tr>
									<td class="acenter">결격사유 없음 확인서 *</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType27 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType27.fileSeq }">${corpImwonList.fileType27.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd113" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd113}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType27.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType27.fileSeq }" >
											<label for="check_cd113">해외 감독당국의 범죄경력확인 서류 등 첨부여부</label>
										</div>
									</td>
								</tr>
								
								
								<tr>
									<td class="acenter">본국 감독당국의 결격요건,범죄이력 확인서류(공증필요)</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType46 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType46.fileSeq }">${corpImwonList.fileType46.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd119" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd119}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType46.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType46.fileSeq }" >
											<label for="check_cd119">확인</label>
										</div>
									</td>
								</tr>
								
								<tr>
									<td class="acenter">본국 감독당국의 결격요건,범죄이력 확인서류의 번역본</td>
									<td>
										<c:choose>
											<c:when test="${corpImwonList.fileType47 ne null }">
												<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType47.fileSeq }">${corpImwonList.fileType47.fileFullNm }</a>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="input_check_wrap mgr10">
											<input type="checkbox" id="check_cd120" class="check check_cd" <c:if test="${!empty corpImwonList.checkCd120}">checked</c:if>
											<c:if test="${empty corpImwonList.fileType47.fileSeq}">disabled</c:if>
											 data-fileSeq="${corpImwonList.fileType47.fileSeq }" >
											<label for="check_cd120">확인</label>
										</div>
									</td>
								</tr>
								
								
							</table>
						</div>
<%-- 						<div class="btn_wrap02">
							<div class="right">
								<a href="javascript:void(0);" class="btn_blue btn_middle mgr5 ocr_imwon_click" 
								data-index="${status.index}" data-imwon-file-seq="${corpImwonList.fileSeq}"
								data-imwon-seq="${corpImwonList.excSeq}" >OCR검증</a>
							</div>
						</div> --%>
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

