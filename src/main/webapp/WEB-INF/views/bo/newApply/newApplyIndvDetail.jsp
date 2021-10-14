<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newRecruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	// 개인 OCR 진행
	$("#indvOcr").on("click", function(){
		if(confirm("OCR 검증을 시작 하시겠습니까?")){
			var p = {
				  url		: "/admin/newApply/newIndvOcr"
				, async		: false
				, param		: {
					 masterSeq 	: $("#masterSeq").val()
				}
				, success 	: function (opt,result) {
					alert("완료");
					// 등록하고자 하는 회원의 주민번호를 비교한다 - fileType2
					// 경력인경우 - fileType3
					// 신규인경우 인증서 추출 - fileType4
					// 결격사유 - fileType7 - 제외
					// 후견부존재증명서 - fileType13 - 제외
					
					if(result.data.fileType2 == "일치"){
						ocrSuccess("check_cd2");
					}if(result.data.fileType3 == "일치"){
						ocrSuccess("check_cd3");
					}if(result.data.fileType4 == "일치"){
						ocrSuccess("check_cd4");
					}
	/* 				if(result.data.fileType7 == "충족"){
						ocrSuccess("check_cd7");
					}if(result.data.fileType13 == "충족"){
						ocrSuccess("check_cd13");
					} */
			    }
			}
			AjaxUtil.post(p);
		}
	});
}


//기등록여부체크리스트
function prevRegCheckPopup() {
	let p = {
		  id 		: "prevRegCheckPopup"
		, url 		: "/admin/newApply/prevNewRegCheckPopup"
		, params 	: {
			"masterSeq" : $("#masterSeq").val()
		}
		, success	: function(opt, result) { 
			
        }
	}
	PopUtil.openPopup(p);
}



//승인
function goRecruitApply(num){
	var plStat = "";
	var plRegStat = "";
	var preRegYn = $("#preRegYn").val();
	
	var preLcNum = $("#preLcNum").val();
	var plProduct = $("#plProduct").val();
	
	/*
	if(plProduct == "01" || plProduct == "05"){
		if(WebUtil.isNull(preLcNum)){
			alert("은행연합회 가등록번호 오류발생 \n시스템관리자에 문의해 주세요.");
			return false;
		}		
	}
	*/
	
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
	}else if(num == "5"){
		plStat = "1";
		plRegStat = "1";
	}else{
		alert("오류가 발생하였습니다.");
		return false;
	}
	
	if(confirm("요청사항을 승인하시겠습니까?")){
		var p = {
			  url		: "/admin/newApply/updateNewPlStat"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: plStat
				,plRegStat		: plRegStat
				,oldPlStat		: $("#oldPlStat").val()
				,preRegYn		: preRegYn
				,preLcNum		: preLcNum
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/newApply/newApplyPage";
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
	
	var preLcNum = $("#preLcNum").val();
	if(num == "1"){
		var plProduct = $("#plProduct").val();
		
		/*
		if(plProduct == "01" || plProduct == "05"){
			if(WebUtil.isNull(preLcNum)){
				alert("은행연합회 가등록번호 오류발생 \n시스템관리자에 문의해 주세요.");
				return false;
			}
		}
		*/
		plStat = '10';
		messageCheck = "부적격 사유를 입력해 주세요.";
	}else{
		messageCheck = "보완요청사유를 입력해 주세요.";
	}
	
	var oldHistTxt = $("#oldHistTxt").val();
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
			  url		: "/admin/newApply/updateNewPlStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: plStat
				,plHistTxt	: $("#plHistTxt").val()
				,oldPlStat	: $("#oldPlStat").val()
				,preLcNum	: preLcNum 
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/newApply/newApplyPage";
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
	<input type="hidden" name="preLcNum" id="preLcNum" value="${result.applyInfo.preLcNum }"/>
	<input type="hidden" name="plProduct" id="plProduct" value="${result.applyInfo.plProduct }"/>
	<input type="hidden" id="oldHistTxt" value="${result.applyInfo.plHistTxt}"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록 승인처리 - 개인</h2>
		</div>
	</div>

	<div class="contents">
		<div class="box">
			<h3>등록정보</h3>
			
			<c:if test="${result.applyInfo.plStat eq '2' or result.applyInfo.plStat eq '13'}">
				<div class="input_check_wrap right mgb0" style="margin-right:240px;">
					<input type="checkbox" id="appDateHold" class="check" <c:if test="${!empty result.applyInfo.appDateHold}">checked</c:if>>
					<label for="appDateHold">승인일 홀딩</label>
				</div>
			</c:if>
			
			<div class="input_check_wrap right mgb0" style="margin-right:120px;">
				<input type="checkbox" id="adminCheck" class="check" <c:if test="${result.applyInfo.adminChkYn eq 'Y'}">checked</c:if>>
				<label for="adminCheck">관리자 확인</label>
			</div>
			<div class="input_check_wrap right mgb0">
				<input type="checkbox" id="masterCheck" class="check" <c:if test="${result.applyInfo.chkYn eq 'Y'}">checked</c:if>>
				<label for="masterCheck">실무자 확인</label>
			</div>
		</div>
		<div id="table">
			<table class="view_table">
				<tr>
					<th>회원사</th>
					<td>${result.applyInfo.comCodeNm }</td>
					<th>담당자</th>
					<td>${result.applyInfo.memberNm } (${result.applyInfo.email }<c:if test="${result.applyInfo.extensionNo ne null && result.applyInfo.extensionNo ne '' }">, ${result.applyInfo.extensionNo }</c:if>)</td>
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
					<td>${result.applyInfo.plStatNm }</td>
					<th>가등록번호</th>
					<td>${result.applyInfo.preLcNum}</td>
				</tr>
				<tr>
					<th>모집인 분류</th>
					<td colspan="3">${result.applyInfo.plClassNm }</td>
				</tr>
				<tr>
					<th>법인사용인여부</th>
					<td colspan="3">${result.applyInfo.corpUserYn }</td>
				</tr>
				<c:if test="${result.applyInfo.corpUserYn eq 'Y' }">
					<tr>
						<th>법인명</th>
						<td>${result.applyInfo.plMerchantName }</td>
						<th>법인등록번호</th>
						<td>${result.applyInfo.plMerchantNo }</td>
					</tr>
				</c:if>
				<tr>
					<th>신규경력구분</th>
					<td>${result.applyInfo.careerTypNm }</td>
					<th>API신규경력구분결과</th>
					<c:choose>
						<c:when test="${result.applyInfo.apiCareerYn eq 'Y'}">
							<td>경력</td>
						</c:when>
						<c:otherwise>
							<td>신규</td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.applyInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>${result.applyInfo.plMName }</td>
					<th>주민번호</th>
					<td>${result.applyInfo.plMZId } <span id="zidCheck"></span></td>
				</tr>
				<tr>
					<th>휴대폰번호</th>
					<td colspan="3">${result.applyInfo.plCellphone }</td>
				</tr>
				<tr>
					<th>주소</th>
					<td colspan="3">${result.applyInfo.addrNm }</td>
				</tr>
				<tr>
					<th>상세주소</th>
					<td colspan="3">${result.applyInfo.addrDetail }</td>
				</tr>
				<tr>
					<th>교육이수번호/인증서번호</th>
					<td colspan="3">${result.applyInfo.plEduNo }</td>
				</tr>
				<tr>
					<th>경력시작일</th>
					<td>${result.applyInfo.careerStartDate }</td>
					<th>경력종료일</th>
					<td>${result.applyInfo.careerEndDate }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td colspan="3">${result.applyInfo.comContDate }</td>
				</tr>
				<tr>
					<th>위탁예정기간</th>
					<td colspan="3">${result.applyInfo.entrustDate }</td>
				</tr>
				
				<tr>
					<th>승인요청사유</th>
					<td colspan="3">
						<textarea rows="6" cols="" id="applyHistTxt" name="applyHistTxt" class="w100" readonly>${result.applyInfo.applyHistTxt }</textarea>
					</td>
				</tr>
				
				<c:choose>
					<c:when test="${result.applyInfo.plStat eq '3' or result.applyInfo.plStat eq '7'
					or result.applyInfo.plStat eq '2' or result.applyInfo.plStat eq '5' or result.applyInfo.plStat eq '7' or result.applyInfo.plStat eq '15'}">
						<tr>
							<th>사유</th>
							<td colspan="3">
								<textarea rows="6" cols="" id="plHistTxt" name="plHistTxt" class="w100">${result.applyInfo.plHistTxt }</textarea>
								<%-- <input type="text" id="plHistTxt" name="plHistTxt" class="w100" maxlength="200" value="${result.applyInfo.plHistTxt }"> --%>
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

		<h3>첨부서류</h3>
		<div id="table02">
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
					<td class="acenter">사진</td>
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
							<input type="checkbox" id="check_cd1" class="check check_cd"
							<c:if test="${empty result.applyInfo.fileType1.fileSeq}">disabled</c:if> 
							<c:if test="${!empty result.applyInfo.checkCd1}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType1.fileSeq }" >
							<label for="check_cd1">주민등록증 사진과 일치여부</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">신분증 사본 *</td>
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
							<input type="checkbox" id="check_cd2" class="check check_cd"
							<c:if test="${empty result.applyInfo.fileType2.fileSeq}">disabled</c:if> 
							<c:if test="${!empty result.applyInfo.checkCd2}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType2.fileSeq }" >
							<label for="check_cd2">주민등록증 사진과 일치여부</label>
						</div>
					</td>
				</tr>
				
				
				<c:if test="${result.applyInfo.careerTyp eq '1' }">
				<tr>
					<td class="acenter">인증서 *</td>
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
							<input type="checkbox" id="check_cd4" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType4.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd4}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType4.fileSeq }" >
							<label for="check_cd4">교육 이수 및 인증내역 검증</label>
						</div>
					</td>
				</tr>
				</c:if>
				
				
				<c:if test="${result.applyInfo.careerTyp eq '2' }">
				<tr>
					<td class="acenter">교육수료증 *</td>
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
							<input type="checkbox" id="check_cd3" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType3.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd3}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType3.fileSeq }" >
							<label for="check_cd3">교육 이수 및 인증내역 검증</label> 
						</div>
					</td>
				</tr>
				
				<tr>
					<td class="acenter">경력증명서</td>
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
							<input type="checkbox" id="check_cd5" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType5.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd5}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType5.fileSeq }" >
							<label for="check_cd5">최근 5년이내 3년이상 금융회사 경력여부</label>
						</div>
					</td>
				</tr>
				
				<tr>
					<td class="acenter">위탁계약서</td>
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
							<input type="checkbox" id="check_cd6" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType6.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd6}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType6.fileSeq }" >
							<label for="check_cd6">금융상품유형, 계약일자, 위탁예정기간 일치 여부</label>
						</div>
					</td>
				</tr>
				
				
				<tr>
					<td class="acenter">위탁 금융상품직접판매업자 확인서</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType12 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType12.fileSeq }">${result.applyInfo.fileType12.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd12" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType12.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd12}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType12.fileSeq }" >
							<label for="check_cd12">법인 인감 날인 여부</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd18" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType12.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd18}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType12.fileSeq }" >
							<label for="check_cd18">금융상품유형, 계약일자, 위탁예정기간 일치 여부</label>
						</div>
					</td>
				</tr>
				</c:if>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goApplyList();">목록</a>
			<c:if test="${result.applyInfo.plStat eq '4'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(4);">해지승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(2);">보완요청</a>
				<!-- <a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="indvOcr">OCR검증</a> -->
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '14'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(5);">승인요청취소승인</a>
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '15'}">
				<c:if test="${result.adminCreGrp eq '2'}">
					<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(2);">승인</a>
				</c:if>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(3);">보완요청</a>
				<!-- <a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="indvOcr">OCR검증</a> -->
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small01 w100p" onclick="goApplyImprove(1);">부적격</a>
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '3'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(3);">변경승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(4);">보완요청</a>
				<!-- <a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="indvOcr">OCR검증</a> -->					
			</c:if>
			
			<a href="javascript:void(0);" class="btn_Lgray" style="position: absolute; left: 0;" onclick="prevRegCheckPopup()">기등록확인</a>
		</div>
	</div>
</div>

