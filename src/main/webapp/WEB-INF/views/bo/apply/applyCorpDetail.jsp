<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/recruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
	$("#corpOcr").on("click", function(){
		if(confirm("[준비중]OCR 검증을 시작 하시겠습니까?")){
			var p = {
				  url		: "/admin/apply/corpOcr"
				, async		: false
				, param		: {
					 masterSeq 	: $("#masterSeq").val()
				}
				, success 	: function (opt,result) {
					alert("완료");
					console.log("RESULT == " , result);
					// 법인등기부등본(법인등록번호) - fileType2_12
					// 법인등기부등본(설립년월일) - fileType2_13
					
					if(result.data.fileType2_12 == "일치"){
						ocrSuccess("check_cd12");
					}if(result.data.fileType2_13 == "일치"){
						ocrSuccess("check_cd13");
					}
			    }
			}
			AjaxUtil.post(p);
		}
	});
}

//승인
function goRecruitApply(num){
	
	var plStat = "";
	var plRegStat = "";
	var preRegYn = $("#preRegYn").val();
	
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
			  url		: "/admin/apply/updatePlStat"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: plStat
				,plRegStat		: plRegStat
				,oldPlStat		: $("#oldPlStat").val()
				,preRegYn		: preRegYn
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/apply/applyPage";
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
function goApplyImprove(num){
	var plStat = '5';
	var messageCheck = "";
	if(num == "1"){
		plStat = '10';
		messageCheck = "부적격 사유를 입력해 주세요.";
	}else{
		messageCheck = "보완요청사유를 입력해 주세요.";
	}
	
	var oldHistTxt = "${result.applyInfo.plHistTxt}";
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert(messageCheck);
		$("#plHistTxt").focus();
		return false;
	}
	
	var confirmMessage = "";
	if(oldHistTxt == $("#plHistTxt").val()){
		confirmMessage = "보완요청 사유가 기존 사유와 동일합니다.\n동일한 사유로 보완요청을 하시겠습니까?";
	}else{
		confirmMessage = "보완요청을 하시겠습니까?";
	}
	if(num == "1"){
		confirmMessage = "부적격 처리를 하시겠습니까?";
	}
	
	if(confirm(confirmMessage)){
		var p = {
			  url		: "/admin/apply/updatePlStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: plStat
				,plHistTxt	: $("#plHistTxt").val()
				,oldPlStat	: $("#oldPlStat").val()
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/apply/applyPage";
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
	<input type="hidden" name="masterSeq" id="masterSeq" value="${result.applyInfo.masterSeq }"/>
	<input type="hidden" name="oldPlStat" id="oldPlStat" value="${result.applyInfo.plStat }"/>
	<input type="hidden" name="masterToId" id="masterToId" value="${result.applyInfo.masterToId }"/>
	<input type="hidden" name="preRegYn" id="preRegYn" value="${result.applyInfo.preRegYn }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 조회 및 변경 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab3('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<div class="contents">
		<div class="box block h20 mgt30 mgb10">
			<div class="input_check_wrap right mgb0">
				<input type="checkbox" id="adminCheck" class="check" <c:if test="${result.applyInfo.chkYn eq 'Y'}">checked</c:if>>
				<label for="adminCheck">실무자 확인</label>
			</div>
		</div>
		<div id="table">
			<table class="view_table">
				<tr>
					<th>회원사</th>
					<td>${result.applyInfo.comCodeNm }</td>
					<th>담당자</th>
					<td>${result.applyInfo.memberNm } (<a href="${result.applyInfo.email }">${result.applyInfo.email }</a>, ${result.applyInfo.mobileNo })</td>
				</tr>
				<tr>
					<th>모집인 상태</th>
					<td>${result.applyInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.applyInfo.masterSeq }');">이력보기</a></td>
					<th>결제여부</th>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.plPayStat ne null }">
								${result.applyInfo.plPayStat }
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>처리상태</th>
					<td colspan="3">${result.applyInfo.plStatNm }</td>
				</tr>
				<tr>
					<th>모집인 분류</th>
					<td colspan="3">${result.applyInfo.plClassNm }</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.applyInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>상호</th>
					<td>${result.applyInfo.plMerchantName }</td>
					<th>대표이사</th>
					<td>${result.applyInfo.plCeoName }</td>
				</tr>
				<tr>
					<th>대표이사 주민번호</th>
					<td>${result.applyInfo.plMZId }</td>
					<th>대표이사 휴대폰번호</th>
					<td>${result.applyInfo.plCellphone }</td>
				</tr>
				<tr>
					<th>법인등록번호</th>
					<td>${result.applyInfo.plMerchantNo }</td>
					<th>설립년월일</th>
					<td>${result.applyInfo.corpFoundDate }</td>
				</tr>
				<tr>
					<th>본점소재지</th>
					<td colspan="3">${result.applyInfo.addrNm }</td>
				</tr>
				<tr>
					<th>상세주소(법인등기부본상)</th>
					<td colspan="3">${result.applyInfo.addrDetail }</td>
				</tr>
				<tr>
					<th>자본금(백만원)</th>
					<td colspan="3">${result.applyInfo.capital }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td>${result.applyInfo.comContDate }</td>
					<th>위탁예정기간</th>
					<td>${result.applyInfo.entrustDate }</td>
				</tr>
				
				<c:choose>
					<c:when test="${result.applyInfo.plStat eq '3' or result.applyInfo.plStat eq '7'
					or result.applyInfo.plStat eq '2' or result.applyInfo.plStat eq '5' or result.applyInfo.plStat eq '7'}">
						<tr>
							<th>사유</th>
							<td colspan="3">
								<input type="text" id="plHistTxt" name="plHistTxt" class="w100" maxlength="200" value="${result.applyInfo.plHistTxt }">
							</td>
						</tr>
					</c:when>
					<c:when test="${result.applyInfo.plStat eq '4' or result.applyInfo.plStat eq '7'}">
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
				
				<c:choose>
					<c:when test="${fn:length(result.violationInfoList) > 0 }">
						<c:forEach var="violationInfoList" items="${result.violationInfoList }" varStatus="status">
							<tr>
								<th>위반이력${status.count }</th>
								<td colspan="3">${violationInfoList.violationCdNm }</td>
							</tr>
						</c:forEach>
					</c:when>
				</c:choose>
				
			</table>
		</div>

		<h3>신청인 관련 서류</h3>
		<div id="table05">
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
					<td class="acenter">정관 *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType1 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType1.fileSeq }">${result.applyInfo.fileType1.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd1" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd1}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType1.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType1.fileSeq }" >
							<label for="check_cd1">공증 유무</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd2" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd2}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType1.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType1.fileSeq }" >
							<label for="check_cd2">중개업무 포함여부</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">법인등기부등본 *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType2 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType2.fileSeq }">${result.applyInfo.fileType2.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd3" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd3}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType2.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType2.fileSeq }" >
							<label for="check_cd3">말소사항 포함 여부</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd4" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd4}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType2.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType2.fileSeq }" >
							<label for="check_cd4">기재내용 일치 여부</label>
						</div>
						
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd12" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd12}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType2.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType2.fileSeq }" >
							<label for="check_cd12">법인번호 일치 여부</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd13" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd13}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType2.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType2.fileSeq }" >
							<label for="check_cd13">설립년월일 일치 여부</label>
						</div>
						
					</td>
				</tr>
				<tr>
					<td class="acenter">설립, 등록 신청의 의사결정을 증명하는 서류 *<br />(등록신청 관련 발기인총회, 창립주주총회 또는 이사회의 공증을 받은 의사록)</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType3 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType3.fileSeq }">${result.applyInfo.fileType3.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd5" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd5}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType3.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType3.fileSeq }" >
							<label for="check_cd5">체크사항1</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd6" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd6}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType3.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType3.fileSeq }" >
							<label for="check_cd6">체크사항2</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">본점의 위치 및 명칭을 기재한 서류<br />(법인등기부에서 확인되지 않는 경우 제출)</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType4 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType4.fileSeq }">${result.applyInfo.fileType4.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd7" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd7}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType4.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType4.fileSeq }" >
							<label for="check_cd7">체크사항1</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd8" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd8}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType4.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType4.fileSeq }" >
							<label for="check_cd8">체크사항2</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">주주명부 *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType5 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType5.fileSeq }">${result.applyInfo.fileType5.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd9" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd9}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType5.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType5.fileSeq }" >
							<label for="check_cd9">인감도장 날인 여부</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">영위하는 다른 업종에 대한 증빙서류 *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType6 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType6.fileSeq }">${result.applyInfo.fileType6.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd10" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd10}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType6.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType6.fileSeq }" >
							<label for="check_cd10">체크사항1</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd11" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd11}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType6.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType6.fileSeq }" >
							<label for="check_cd11">체크사항2</label>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goApplyList();">목록</a>
			<c:if test="${result.applyInfo.plStat eq '4'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(4);">해지승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(2);">보완요청</a>
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="corpOcr">OCR검증</a>
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '2'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(2);">승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(3);">보완요청</a>
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="corpOcr">OCR검증</a>
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '3'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(3);">변경승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(4);">보완요청</a>
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="corpOcr">OCR검증</a>					
			</c:if>
			<a href="javascript:void(0);" class="btn_Lgray btn_right_small01 w100p" onclick="goApplyImprove(1);">부적격</a>
		</div>
	</div>
</div>

