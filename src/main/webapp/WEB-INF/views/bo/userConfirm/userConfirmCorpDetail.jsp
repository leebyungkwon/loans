<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
}

//변경요청 페이지 이동
function goUserChangeApplyPage(){
	$("#pageFrm").attr("action","/member/confirm/userConfirmCorpChangeApply");
	$("#pageFrm").submit();
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 조회 및 변경 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab2('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<div class="contents">
		<div id="table">
			<table class="view_table">
				<tr>
					<th>회원사</th>
					<td>${result.userRegInfo.comCodeNm }</td>
					<th>담당자</th>
					<td>${result.userRegInfo.memberNm } (<a href="${result.userRegInfo.email }">${result.userRegInfo.email }</a>, ${result.userRegInfo.mobileNo })</td>
				</tr>
				<tr>
					<th>모집인 상태</th>
					<td>${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.userRegInfo.masterSeq }');">이력보기</a></td>
					<th>결제여부</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.plPayStat ne null }">
								${result.userRegInfo.plPayStat } (국민카드 / 2021.10.20)
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>처리상태</th>
					<td colspan="3">${result.userRegInfo.plStatNm }</td>
				</tr>
				<tr>
					<th>모집인 분류</th>
					<td colspan="3">${result.userRegInfo.plClassNm }</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.userRegInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>상호</th>
					<td>${result.userRegInfo.plMerchantName }</td>
					<th>대표이사</th>
					<td>${result.userRegInfo.plCeoName }</td>
				</tr>
				<tr>
					<th>법인등록번호</th>
					<td colspan="3">${result.userRegInfo.plMerchantNo }</td>
				</tr>
				<tr>
					<th>설립년월일</th>
					<td colspan="3">${result.userRegInfo.corpFoundDate }</td>
				</tr>
				<tr>
					<th>본점소재지</th>
					<td colspan="3">${result.userRegInfo.addrNm }</td>
				</tr>
				<tr>
					<th>상세주소(법인등기부본상)</th>
					<td colspan="3">${result.userRegInfo.addrDetail }</td>
				</tr>
				<tr>
					<th>자본금(백만원)</th>
					<td colspan="3">${result.userRegInfo.capital }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td>${result.userRegInfo.comContDate }</td>
					<th>위탁예정기간</th>
					<td>${result.userRegInfo.entrustDate }</td>
				</tr>
				<c:if test="${result.userRegInfo.plStat eq '3' || result.userRegInfo.plStat eq '4' }">
					<tr>
						<th>요청사유</th>
						<td colspan="3">${result.userRegInfo.plHistTxt }</td>
					</tr>
				</c:if>
				<c:if test="${result.userRegInfo.plStat eq '5' }">
					<tr>
						<th>반려사유</th>
						<td colspan="3">${result.userRegInfo.sendMsg }</td>
					</tr>
				</c:if>
				<c:if test="${result.userRegInfo.plStat eq '4' }">
					<!-- 회원사가 해지요청한 날짜 -->
					<tr>
						<th>해지요청일자</th>
						<td colspan="3">${result.userRegInfo.comHaejiDate }</td>
					</tr>
				</c:if>
			</table>
		</div>

		<h3>신청인 관련 서류</h3>
		<div id="table05">
			<table class="view_table">
				<colgroup>
					<col width="38%"/>
					<col width="62%"/>
				</colgroup>
				<tr>
					<th class="acenter">정관 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType1 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }">${result.userRegInfo.fileType1.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">법인등기부등본 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType2 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">${result.userRegInfo.fileType2.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">설립, 등록 신청의 의사결정을 증명하는 서류 *<br />(등록신청 관련 발기인총회, 창립주주총회 또는 이사회의 공증을 받은 의사록)</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType3 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">${result.userRegInfo.fileType3.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">본점의 위치 및 명칭을 기재한 서류<br />(법인등기부에서 확인되지 않는 경우 제출)</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType4 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">${result.userRegInfo.fileType4.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">주주명부 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType5 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">${result.userRegInfo.fileType5.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">영위하는 다른 업종에 대한 증빙서류 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType6 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">${result.userRegInfo.fileType6.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
			<c:if test="${result.userRegInfo.plRegStat ne '4' }">
				<c:if test="${result.userRegInfo.plRegStat eq '2' }">
					<a href="javascript:void(0);" class="btn_Lgray btn_right w100p" id="userCancel" onclick="goUserCancelPage();">즉시취소</a>
				</c:if>
				<c:if test="${result.userRegInfo.plRegStat eq '3' and (result.userRegInfo.plStat eq '5' or result.userRegInfo.plStat eq '7') }">
					<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="userChangeApply" onclick="goUserChangeApplyPage();">변경요청</a>
					<a href="javascript:void(0);" class="btn_black btn_right w100p" id="userDropApply" onclick="goUserDropApplyPage();">해지요청</a>
				</c:if>
			</c:if>
			
		</div>
	</div>
</div>

