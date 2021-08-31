<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
}

//변경요청 페이지 이동
function goUserChangeApplyPage(){
	$("#pageFrm").attr("action","/member/confirm/userConfirmIndvChangeApply");
	$("#pageFrm").submit();
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 조회 및 변경 - 개인</h2>
		</div>
	</div>

	<div class="contents">
		<h3>등록정보</h3>
		<div id="table">
			<table class="view_table">
				<tr>
					<th>회원사</th>
					<td>${result.userRegInfo.comCodeNm }</td>
					<th>담당자</th>
					<td>${result.userRegInfo.memberNm } (${result.userRegInfo.email }<c:if test="${result.userRegInfo.extensionNo ne null && result.userRegInfo.extensionNo ne '' }">, ${result.userRegInfo.extensionNo }</c:if>)</td>
				</tr>
				<tr>
					<th>등록번호</th>
					<td colspan="3">${result.userRegInfo.plRegistNo }</td>
				</tr>
				<tr>
					<th>모집인 상태</th>
					<td>${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.userRegInfo.masterSeq }');">이력보기</a></td>
					<th>결제여부</th>
					<td>
						<c:choose>
							<c:when test="${!empty result.payResult}">
								결제완료 (${result.payResult.name} / ${result.payResult.regTimestamp})
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
					<th>법인사용인여부</th>
					<td colspan="3">${result.userRegInfo.corpUserYn }</td>
				</tr>
				<c:if test="${result.userRegInfo.corpUserYn eq 'Y' }">
					<tr>
						<th>법인명</th>
						<td>${result.userRegInfo.plMerchantName }</td>
						<th>법인등록번호</th>
						<td>${result.userRegInfo.plMerchantNo }</td>
					</tr>
				</c:if>
				<tr>
					<th>신규경력구분</th>
					<td colspan="3">${result.userRegInfo.careerTypNm }</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.userRegInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>${result.userRegInfo.plMName }</td>
					<th>주민번호</th>
					<td>${result.userRegInfo.plMZId }</td>
				</tr>
				<tr>
					<th>휴대폰번호</th>
					<td colspan="3">${result.userRegInfo.plCellphone }</td>
				</tr>
				<tr>
					<th>주소</th>
					<td colspan="3">${result.userRegInfo.addrNm }</td>
				</tr>
				<tr>
					<th>상세주소</th>
					<td colspan="3">${result.userRegInfo.addrDetail }</td>
				</tr>
				<tr>
					<th>교육이수번호/인증서번호</th>
					<td colspan="3">${result.userRegInfo.plEduNo }</td>
				</tr>
				<tr>
					<th>경력시작일</th>
					<td>${result.userRegInfo.careerStartDate }</td>
					<th>경력종료일</th>
					<td>${result.userRegInfo.careerEndDate }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td colspan="3">${result.userRegInfo.comContDate }</td>
				</tr>
				<tr>
					<th>위탁예정기간</th>
					<td colspan="3">${result.userRegInfo.entrustDate }</td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(result.violationInfoList) > 0 }">
						<c:forEach var="violationInfoList" items="${result.violationInfoList }" varStatus="status">
							<tr>
								<th>위반이력${status.count }</th>
								<td colspan="3" <c:if test="${violationInfoList.applyYn eq 'Y' }">class="red"</c:if>>${violationInfoList.violationCdNm }</td>
							</tr>
						</c:forEach>
					</c:when>
				</c:choose>
				<c:if test="${result.userRegInfo.plStat eq '3' }">
					<tr>
						<th>요청사유</th>
						<td colspan="3">${result.userRegInfo.plHistTxt }</td>
					</tr>
				</c:if>
				<c:if test="${result.userRegInfo.plStat eq '4' or result.userRegInfo.plRegStat eq '4' }">
					<tr>
						<th>해지사유</th>
						<td colspan="3">${result.userRegInfo.plHistCdNm }</td>
					</tr>
					<tr>
						<th>해지요청일자</th> <!-- 회원사가 해지요청한 날짜 -->
						<td colspan="3">${result.userRegInfo.comHaejiDate }</td>
					</tr>
				</c:if>
				<c:if test="${result.userRegInfo.plStat eq '5' or result.userRegInfo.plStat eq '6' or result.userRegInfo.plStat eq '7' }">
					<tr>
						<th>보완요청사유</th>
						<td colspan="3">${result.userRegInfo.plHistTxt }</td>
					</tr>
				</c:if>
				<c:if test="${result.userRegInfo.plStat eq '10' || result.userRegInfo.plStat eq '11' || result.userRegInfo.plStat eq '12' }">
					<tr>
						<th>사유</th>
						<td colspan="3">${result.userRegInfo.plHistTxt }</td>
					</tr>
				</c:if>
			</table>
		</div>

		<h3>첨부서류</h3>
		<div id="table02">
			<table class="view_table">
				<colgroup>
					<col width="50%"/>
					<col width="50%"/>
				</colgroup>
				<tr>
					<th class="acenter">사진(등록증 게시용)</th>
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
					<th class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType2 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">${result.userRegInfo.fileType2.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<c:if test="${result.userRegInfo.careerTyp eq '1' }">
					<tr>
						<th class="acenter">인증서(신규) *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType4 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">${result.userRegInfo.fileType4.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<c:if test="${result.userRegInfo.careerTyp eq '2' }">
					<tr>
						<th class="acenter">경력교육과정 수료증 *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType3 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">${result.userRegInfo.fileType3.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<tr>
					<th class="acenter">경력증명서</th>
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
					<th class="acenter">위탁계약서</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType6 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">${result.userRegInfo.fileType6.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType12 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType12.fileSeq }">${result.userRegInfo.fileType12.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<%-- 
				<tr>
					<th class="acenter">결격사유없음 확인서(파산, 피한정후견인등) *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType7 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType7.fileSeq }">${result.userRegInfo.fileType7.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				 --%>
				<tr>
					<th class="acenter">대리인 신청 위임장 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType8 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }">${result.userRegInfo.fileType8.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">위임인 인감증명서</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType9 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }">${result.userRegInfo.fileType9.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<%-- 
				<tr>
					<th class="acenter">후견부존재증명서 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType13 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType13.fileSeq }">${result.userRegInfo.fileType13.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">주민등록증 또는 주민등록 초본(성명, 주민등록번호 변경 시)</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType10 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType10.fileSeq }">${result.userRegInfo.fileType10.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				 --%>
				<tr>
					<th class="acenter">행정정보공동이용 사전동의서 *<br>(외국인인 경우 결격요건 확인서 및 본국 감독당국의 결격요건, 범죄이력 확인서류)</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType14 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType14.fileSeq }">${result.userRegInfo.fileType14.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">개인정보필수동의서 *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType15 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }">${result.userRegInfo.fileType15.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">휴대폰 명의 확인서(휴대폰번호 변경 시)</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType11 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType11.fileSeq }">${result.userRegInfo.fileType11.fileFullNm }</a>
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
					<a href="javascript:void(0);" class="btn_black btn_right w100p" id="userCancel" onclick="goUserCancelPage();">즉시취소</a>
				</c:if>
				<c:if test="${result.userRegInfo.plRegStat eq '3' and (result.userRegInfo.plStat eq '5' or result.userRegInfo.plStat eq '6' or result.userRegInfo.plStat eq '7' or result.userRegInfo.plStat eq '9') }">
					<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="userChangeApply" onclick="goUserChangeApplyPage();">변경요청</a>
					<a href="javascript:void(0);" class="btn_black btn_right w100p" id="userDropApply" onclick="goUserDropApplyPage();">해지요청</a>
				</c:if>
				<%-- 
				<c:if test="${result.userRegInfo.plStat eq '11' or result.userRegInfo.plStat eq '12' }">
					<a href="javascript:void(0);" class="btn_black btn_right w100p">삭제</a>
				</c:if>
				 --%>
			</c:if>
		</div>
	</div>
</div>

