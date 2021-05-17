<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
}

//취소화면변경
function goUserCancelPage(){
	$("#histTxt").show();
	$("#plHistTxt").focus();
	$("#userChangeApply").remove();
	$("#userDropApply").remove();
	$("#userCancel").attr("onclick", "goUserCancel()");
}

//취소
function goUserCancel(){
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("취소사유를 입력해 주세요");
		return false;
	}
	
	if(confirm("취소하시겠습니까?")){
		var p = {
			  url		: "/member/confirm/updatePlRegConfirmStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '6'
				,plHistTxt	: $("#plHistTxt").val()
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("취소되었습니다.");
					goUserConfirmList();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//변경요청
function goUserChangeApply(){
	if(confirm("모집인 변경사항을 요청하시겠습니까?")){
		var p = {
			  url		: "/member/confirm/updatePlRegConfirmStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '3'
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("변경요청이 완료되었습니다.");
					goUserConfirmList();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//해지요청
function goUserDropApplyPage(){
	$("#histTxt").show();
	$("#haejiDate").show();
	$("#plHistTxt").focus();
	$("#userChangeApply").remove();
	$("#userCancel").remove();
	$("#userDropApply").attr("onclick", "goUserDropApply()");
}

function goUserDropApply(){
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("해지사유를 입력해 주세요");
		return false;
	}
	if(confirm("모집인 해지를 요청하시겠습니까?")){
		var p = {
			  url		: "/member/confirm/updatePlRegConfirmStat"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: '4'
				,plHistTxt		: $("#plHistTxt").val()
				,plRegStat		: '1'
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("해지요청이 완료되었습니다.");
					goUserConfirmList();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

/*
function fnCancel(){
	if(confirm("취소하시겠습니까?")){
		var p = {
			  url		: "/member/confirm/updatePlRegConfirmStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '6'
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("취소되었습니다.");
					goUserConfirmList();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

function fnChange(){
	if(confirm("모집인 변경사항을 요청하시겠습니까?")){
		var p = {
			  url		: "/member/confirm/updatePlRegConfirmStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '3'
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("변경요청이 완료되었습니다.");
					goUserConfirmList();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

function fnDrop(){
	if(confirm("모집인 해지를 요청하시겠습니까?")){
		var p = {
			  url		: "/member/confirm/userDropApply"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '4'
			}
			, success 	: function (opt,result) {
				goUserConfirmList();
		    }
		}
		AjaxUtil.post(p);
	}
}
*/
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
					<td>${result.userRegInfo.memberNm } (<a href="${result.userRegInfo.email }">${result.userRegInfo.email }</a>, ${result.userRegInfo.mobileNo })</td>
				</tr>
				<tr>
					<th>모집인 상태</th>
					<td>${result.userRegInfo.plRegStatNm } <a href="javascript:alert('준비중입니다.');" class="btn_Lgray btn_small mgl5">이력보기</a></td>
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
					<th>교육이수번호</th>
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
				<c:if test="${result.userRegInfo.plStat eq '4' }">
					<tr>
						<th>반려사유</th>
						<td colspan="3">${result.userRegInfo.sendMsg }</td>
					</tr>
				</c:if>
				<c:if test="${result.userRegInfo.plStat ne '7' }">
					<tr id="histTxt" style="display:none;">
						<th>사유</th>
						<td colspan="3"><input type="text" id="plHistTxt" name="plHistTxt" class="w100" maxlength="200"></td>
					</tr>
				</c:if>
				<c:if test="${!empty result.userRegInfo.comHaejiDate or !empty result.userRegInfo.creHaejiDate}">
					<tr id="haejiDate" style="display:none;">
						<th>해지일자</th>
						<td colspan="3"></td>
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
					<th class="acenter">사진 (등록증 게시용) *</th>
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
				<tr>
					<th class="acenter">교육과정 이수확인서 (경력)</th>
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
					<th class="acenter">인증서(신규)</th>
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
					<th class="acenter">경력증명서 *</th>
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
					<th class="acenter">금융상품 유형, 내용에 대한 설명자료(계약서) *</th>
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
					<th class="acenter">결격사유없음 확인서 (파산, 피한정후견인등) *</th>
					<td>
						<c:choose>
							<c:when test="${result.userRegInfo.fileType7 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType7.fileSeq }">${result.userRegInfo.fileType7.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">대리인 신청 위임장(위임인 인간날인)</th>
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
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
			<c:if test="${result.userRegInfo.plRegStat eq '2' }">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="userCancel" onclick="goUserCancelPage();">즉시취소</a>
			</c:if>
			<c:if test="${result.userRegInfo.plRegStat eq '3' and result.userRegInfo.plStat eq '7' }">
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="userChangeApply" onclick="goUserChangeApply();">변경요청</a>
				<a href="javascript:void(0);" class="btn_black btn_right w100p" id="userDropApply" onclick="goUserDropApplyPage();">해지요청</a>			
			</c:if>
		</div>
	</div>
</div>
