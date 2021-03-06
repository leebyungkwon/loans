<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/recruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){

}



//2021-11-02 등록번호 수정 
function updatePlRegistNo(){
	var plRegistNo = $("#updatePlRegistNo").val();
	if(confirm("등록번호를 수정하시겠습니까?")){
		var p = {
			  url		: "/admin/recruit/updatePlRegistNo"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				 , plRegistNo	: plRegistNo
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.reload();
				}else{
					alert(result.data.message);
					location.reload();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//2021-11-02 계약번호 수정 
function updateConNum(){
	var conNum = $("#updateConNum").val();
	if(confirm("계약번호를 수정하시겠습니까?")){
		var p = {
			  url		: "/admin/recruit/updateConNum"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				 , conNum	: conNum
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.reload();
				}else{
					alert(result.data.message);
					location.reload();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}



//승인
function goRecruitApply(num){
	
	var plStat = "";
	var plRegStat = "";
	var preRegYn = $("#preRegYn").val();
	var plRegistNo = $("#plRegistNo").val();
	var plProduct = $("#plProduct").val();
	if(plProduct == "01" || plProduct == "05"){
		if(WebUtil.isNull(plRegistNo)){
			alert("은행연합회 등록번호 오류발생 \n시스템관리자에 문의해 주세요.");
			return false;
		}
	}
	
	if(num == "2"){
		plStat = "9";
		plRegStat = "2";
		// 기등록자인경우 자격취득 - 완료
		if(preRegYn == "Y"){
			plStat = "9";
			plRegStat = "3";
		}
	}else if(num == "4"){
		plStat = "9";
		plRegStat = "4";
	}else if(num == "3"){
		plStat = "9";
		plRegStat = "3";
	}else{
		alert("오류가 발생하였습니다.");
		return false;
	}
	
	if(confirm("요청사항을 승인하시겠습니까?")){
		var p = {
			  url		: "/admin/recruit/updatePlStat"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: plStat
				,plRegStat		: plRegStat
				,oldPlStat		: $("#oldPlStat").val()
				,preRegYn		: preRegYn
				,plRegistNo		: plRegistNo 
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/recruit/recruitPage?historyback=Y";
				}else{
					alert(result.data.message);
					location.reload();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//보완
function goRecruitImprove(rePlStat){
	
	var oldHistTxt = $("#oldHistTxt").val();
	if($(document).find("#plHistArea").length == 0){
		var tag = '<tr id="plHistArea"><th>사유</th><td colspan="3">';
		tag += '<textarea rows="6" cols="" id="plHistTxt" name="plHistTxt" class="w100"></textarea></tr>';
		$("#infoTable").append(tag);
		return false;
	}
	
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("사유를 입력해 주세요");
		$("#plHistTxt").focus();
		return false;
	}
	
	var confirmMessage = "";
	if(oldHistTxt == $("#plHistTxt").val()){
		confirmMessage = "보완요청 사유가 기존 사유와 동일합니다.\n동일한 사유로 보완요청을 하시겠습니까?";
	}else{
		confirmMessage = "보완요청을 하시겠습니까?";
	}
	
	var plStat = rePlStat;
	
	// 2021-07-04 은행연합회 API 추가
	var plRegistNo = $("#plRegistNo").val();
	
	if(confirm(confirmMessage)){
		var p = {
			  url		: "/admin/recruit/updatePlStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: plStat
				,plHistTxt	: $("#plHistTxt").val()
				,oldPlStat	: $("#oldPlStat").val()
				,plRegistNo	: plRegistNo 
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/recruit/recruitPage?historyback=Y";
				}else{
					alert(result.data.message);
					location.reload();
				}
				
		    }
		}
		AjaxUtil.post(p);
	}
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" id="masterSeq" value="${result.recruitInfo.masterSeq }"/>
	<input type="hidden" name="oldPlStat" id="oldPlStat" value="${result.recruitInfo.plStat }"/>
	<input type="hidden" name="masterToId" id="masterToId" value="${result.recruitInfo.masterToId }"/>
	<input type="hidden" name="preRegYn" id="preRegYn" value="${result.recruitInfo.preRegYn }"/>
	<input type="hidden" name="plRegistNo" id="plRegistNo" value="${result.recruitInfo.plRegistNo }"/>
	<input type="hidden" name="plProduct" id="plProduct" value="${result.recruitInfo.plProduct }"/>
	<input type="hidden" id="oldHistTxt" value="${result.recruitInfo.plHistTxt}"/>
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
			<li><a href="javascript:void(0);" onclick="goTab2('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<div class="contents">
		<div id="table">
			<table class="view_table" id="infoTable">
				<tr>
					<th>회원사</th>
					<td>${result.recruitInfo.comCodeNm }</td>
					<th>담당자</th>
					<td>${result.recruitInfo.memberNm } (${result.recruitInfo.email }<c:if test="${result.recruitInfo.extensionNo ne null && result.recruitInfo.extensionNo ne '' }">, ${result.recruitInfo.extensionNo }</c:if>)</td>
				</tr>
				<tr>
					<th>모집인 상태</th>
					<td>${result.recruitInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.recruitInfo.masterSeq }');">이력보기</a></td>
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
					<td>${result.recruitInfo.plStatNm }</td>
					<th>등록번호</th>
					<td>
						<input type="text" id="updatePlRegistNo" value="${result.recruitInfo.plRegistNo}" /> 
						<a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="updatePlRegistNo();">등록번호 수정</a>
					</td>
				</tr>
				<tr>
					<th>모집인 분류</th>
					<td>${result.recruitInfo.plClassNm }</td>
					<th>계약번호</th>
					<td>
						<input type="text" id="updateConNum" value="${result.recruitInfo.conNum}" /> 
						<a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="updateConNum();">계약번호 수정</a>
					</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td>${result.recruitInfo.plProductNm }</td>
					<th>업종</th>
					<td>${result.recruitInfo.plWork }</td>
				</tr>
				<tr>
					<th>상호</th>
					<td>${result.recruitInfo.plMerchantName }</td>
					<th>대표이사</th>
					<td>${result.recruitInfo.plCeoName }</td>
				</tr>
				<tr>
					<th>대표이사 주민번호</th>
					<td>${result.recruitInfo.plMZId }</td>
					<th>대표이사 휴대폰번호</th>
					<td>${result.recruitInfo.plCellphone }</td>
				</tr>
				<tr>
					<th>법인등록번호</th>
					<td>${result.recruitInfo.plMerchantNo }</td>
					<th>설립년월일</th>
					<td>${result.recruitInfo.corpFoundDate }</td>
				</tr>
				<tr>
					<th>본점소재지</th>
					<td colspan="3">${result.recruitInfo.addrNm }</td>
				</tr>
				<tr>
					<th>상세주소(법인등기부등본상)</th>
					<td colspan="3">${result.recruitInfo.addrDetail }</td>
				</tr>
				<tr>
					<th>자본금(백만원)</th>
					<td colspan="3">${result.recruitInfo.capital }</td>
				</tr>
				<tr>
					<th>의결권있는 발행주식 총수</th>
					<td colspan="3">${result.recruitInfo.votingStockCnt }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td>${result.recruitInfo.comContDate }</td>
					<th>위탁예정기간</th>
					<td>${result.recruitInfo.entrustDate }</td>
				</tr>
				
				<c:choose>
					<c:when test="${fn:length(result.violationInfoList) > 0 }">
						<c:forEach var="violationInfoList" items="${result.violationInfoList }" varStatus="status">
							<tr>
								<th>위반이력사항</th>
								<td colspan="3" <c:if test="${violationInfoList.applyYn eq 'Y' }">class="red"</c:if>>${violationInfoList.violationCdNm }</td>
							</tr>
						</c:forEach>
					</c:when>
				</c:choose>
				
				<c:choose>
					<c:when test="${result.recruitInfo.plStat eq '4' or result.recruitInfo.plStat eq '7' or result.recruitInfo.plStat eq '5' or result.recruitInfo.plStat eq '9'}">
						<c:if test="${!empty result.recruitInfo.plHistCdNm }">
							<tr>
								<th>해지요청사유</th>
								<td colspan="3">${result.recruitInfo.plHistCdNm }</td>
							</tr>
							<tr>
								<th>해지일자</th>
								<td colspan="3">${result.recruitInfo.comHaejiDate }</td>
							</tr>
						</c:if>
					</c:when>
				</c:choose>
				<c:if test="${result.recruitInfo.plStat ne '4'}">
					<c:if test="${result.recruitInfo.plRegStat ne '4' }">
						<tr id="plHistArea">
							<th>사유</th>
							<td colspan="3">
								<textarea rows="6" cols="" id="plHistTxt" name="plHistTxt" class="w100">${result.recruitInfo.plHistTxt }</textarea>
								<%-- <input type="text" id="plHistTxt" name="plHistTxt" class="w100" maxlength="200" value="${result.recruitInfo.plHistTxt }"> --%>
							</td>
						</tr>
					</c:if>
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
							<c:when test="${result.recruitInfo.fileType1 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType1.fileSeq }">${result.recruitInfo.fileType1.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType1}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType1.fileGrpSeq }', '1');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">법인등기부등본 *</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType2 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType2.fileSeq }">${result.recruitInfo.fileType2.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType2}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType2.fileGrpSeq }', '2');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">설립, 등록 신청의 의사결정을 증명하는 서류 *<br />(등록신청 관련 발기인총회, 창립주주총회 또는 이사회의 공증을 받은 의사록)</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType3 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType3.fileSeq }">${result.recruitInfo.fileType3.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType3}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType3.fileGrpSeq }', '3');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">본점의 위치 및 명칭을 기재한 서류<br />(법인등기부에서 확인되지 않는 경우 제출)</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType4 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType4.fileSeq }">${result.recruitInfo.fileType4.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType4}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType4.fileGrpSeq }', '4');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">주주명부 *</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType5 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType5.fileSeq }">${result.recruitInfo.fileType5.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType5}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType5.fileGrpSeq }', '5');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">영위하는 다른 업종에 대한 증빙서류</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType6 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType6.fileSeq }">${result.recruitInfo.fileType6.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType6}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType6.fileGrpSeq }', '6');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">업무수행기준요건관련 서류 *</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType15 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType15.fileSeq }">${result.recruitInfo.fileType15.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType15}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType15.fileGrpSeq }', '15');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">위탁계약서</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType31 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType31.fileSeq }">${result.recruitInfo.fileType31.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType31}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType31.fileGrpSeq }', '31');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
				<tr>
					<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType32 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType32.fileSeq }">${result.recruitInfo.fileType32.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
						<c:if test="${result.recruitInfo.plRegStat == '3' or  result.recruitInfo.plRegStat == '4'}">
							<c:if test="${!empty result.recruitInfo.histFileType32}">
								<a href="javascript:void(0);" class="btn_blue btn_small mgl30" onclick="goRecruitFileHistShow('${result.recruitInfo.histFileType32.fileGrpSeq }', '32');">변경사항</a>
							</c:if>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goRecruitList();">목록</a>
			<c:if test="${result.recruitInfo.plStat eq '4'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(4);">해지승인</a>
				<!-- <a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove(7);">보완요청</a> -->
			</c:if>
			<c:if test="${result.recruitInfo.plStat eq '2'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(2);">승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove(5);">보완요청</a>
			</c:if>
			<c:if test="${result.recruitInfo.plStat eq '3'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(3);">변경승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove(6);">보완요청</a>					
			</c:if>
		</div>
	</div>
</div>

