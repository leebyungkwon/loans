<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newRecruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	$("#corpOcr").on("click", function(){
		if(confirm("OCR 검증을 시작 하시겠습니까?")){
			var p = {
				  url		: "/admin/newApply/newCorpOcr"
				, async		: false
				, param		: {
					 masterSeq 	: $("#masterSeq").val()
				}
				, success 	: function (opt,result) {
					alert("완료");
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
	var plProduct = $("#plProduct").val();
	var applyMessage = "요청사항을 승인하시겠습니까?";
	
	if(num == "2"){
		plStat = "9";
		plRegStat = "2";
		// 2021-11-01 승인완료에 대한 승인이력
		var applyComHistTxt = $("#applyComHistTxt").val();
		if(WebUtil.isNull(applyComHistTxt)){
			applyMessage = "승인완료사유가 없습니다.\n요청사항을 승인하시겠습니까?";
		}
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
				,applyComHistTxt : applyComHistTxt
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
	if(num == "1"){
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
			<h2>모집인 등록 승인처리 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab3('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<div class="contents">
		<div class="box block h20 mgt30 mgb10">
		
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
					<td>${result.applyInfo.preLcNum }</td>
				</tr>
				<tr>
					<th>모집인 분류</th>
					<td>${result.applyInfo.plClassNm }</td>
					<th>결격사유 및 범죄이력</th>
					<td>${result.applyInfo.disVal }</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td>${result.applyInfo.plProductNm }</td>
					<th>금융상품 세부내용</th>
					<td>
						<c:choose>
							<c:when test="${fn:length(result.plProductDetailList) > 0 }">
								<c:forEach var="productDetailList" items="${result.plProductDetailList }" varStatus="loop">
									${productDetailList.plProductDtlCdNm}
									<c:if test="${!loop.last}"> / </c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								해당없음
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>업종</th>
					<td colspan="3">${result.applyInfo.plWork }</td>
				</tr>
				<tr>
					<th>법인명</th>
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
					<td>${result.applyInfo.addrBase }</td>
					<th>본점소재지 상세</th>
					<td>${result.applyInfo.addrDetail }</td>
				</tr>
				<tr>
					<th>자본금(백만원)</th>
					<td>${result.applyInfo.capital }</td>
					<th>의결권있는 발행주식 총수</th>
					<td>${result.applyInfo.votingStockCnt }</td>
				</tr>
				<tr>
					<th>영위하는 다른 업종</th>
					<td>${result.applyInfo.otherField }</td>
					<th>관할검찰청 또는 지청</th>
					<td>${result.applyInfo.withinGovrNm }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td>${result.applyInfo.comContDate }</td>
					<th>위탁예정기간</th>
					<td>${result.applyInfo.entrustDate }</td>
				</tr>
				
<%-- 				<tr>
					<th>승인요청사유</th>
					<td colspan="3">
						<textarea rows="6" cols="" id="" name="" class="w100" readonly>${result.applyInfo.applyHistTxt }</textarea>
					</td>
				</tr> --%>
				
				<tr>
					<th>승인완료사유</th>
					<td colspan="3">
						<textarea rows="6" cols="" id="applyComHistTxt" name="applyComHistTxt" class="w100" >${result.applyInfo.applyComHistTxt }</textarea>
					</td>
				</tr>
				
				<c:choose>
					<c:when test="${result.applyInfo.plStat eq '3' or result.applyInfo.plStat eq '7'
					or result.applyInfo.plStat eq '2' or result.applyInfo.plStat eq '5' or result.applyInfo.plStat eq '7' or result.applyInfo.plStat eq '15'}">
						<tr>
							<th>사유<br/>(보완요청 및 부적격)</th>
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

		<h3>신청인 관련 서류</h3>
		<div id="table05">
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
							<label for="check_cd2">금융상품판매대리중개업무 포함여부</label>
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
					<td class="acenter">설립,등록 신청의 의사록 *</td>
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
							<label for="check_cd5">등록의사 포함 여부</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd6" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd6}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType3.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType3.fileSeq }" >
							<label for="check_cd6">공증 유무</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">본점의 위치 및 명칭을 기재한 서류</td>
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
							<label for="check_cd7">본점위치 확인 여부</label>
						</div>
<%-- 						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd8" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd8}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType4.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType4.fileSeq }" >
							<label for="check_cd8">체크사항2</label>
						</div> --%>
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
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd8" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd8}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType5.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType5.fileSeq }" >
							<label for="check_cd8">주민등록번호 마스킹 여부</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">영위하는 다른 업종에 대한 증빙서류</td>
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
							<label for="check_cd10">정관상 영위하는 업무 관련 증명서 제출 여부</label>
						</div>
<%-- 						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd11" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd11}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType6.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType6.fileSeq }" >
							<label for="check_cd11">체크사항2</label>
						</div> --%>
					</td>
				</tr>
				
				<tr>
					<td class="acenter">업무수행기준 *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType15 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType15.fileSeq }">${result.applyInfo.fileType15.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd116" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd116}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType15.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType15.fileSeq }" >
							<label for="check_cd116">업무수행기준 표준(안) 반영여부</label>
						</div>
<%-- 						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd112" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd112}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType15.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType15.fileSeq }" >
							<label for="check_cd112">체크사항2</label>
						</div> --%>
					</td>
				</tr>
				
				<tr>
					<td class="acenter">위탁계약서</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType31 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType31.fileSeq }">${result.applyInfo.fileType31.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd117" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd117}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType31.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType31.fileSeq }" >
							<label for="check_cd117">금융상품유형, 계약일자, 위탁예정기간 일치 여부</label>
						</div>
<%-- 						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd118" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd118}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType31.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType31.fileSeq }" >
							<label for="check_cd118">체크사항2</label>
						</div> --%>
					</td>
				</tr>
				
				<tr>
					<td class="acenter">위탁 금융상품직접판매업자 확인서</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType32 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType32.fileSeq }">${result.applyInfo.fileType32.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd119" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd119}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType32.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType32.fileSeq }" >
							<label for="check_cd119">금융상품유형, 계약일자, 위탁예정기간 일치 여부</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd120" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd120}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType32.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType32.fileSeq }" >
							<label for="check_cd120">법인 인감 날인 여부</label>
						</div>
					</td>
				</tr>
				
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goApplyList();">목록</a>
<%-- 			<c:if test="${result.applyInfo.plStat eq '14'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(5);">승인요청취소승인</a>
			</c:if> --%>
			
			<c:if test="${result.applyInfo.plStat eq '15'}">
				<c:if test="${result.adminCreGrp eq '2'}">
					<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(2);">승인</a>
				</c:if>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(3);">보완요청</a>
				<!-- <a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="corpOcr">OCR검증</a> -->
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '3'}">
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goApplyImprove(4);">보완요청</a>
				<!-- <a href="javascript:void(0);" class="btn_Lgray btn_right_small04 w100p" id="corpOcr">OCR검증</a> -->					
			</c:if>
			<a href="javascript:void(0);" class="btn_Lgray btn_right_small01 w100p" onclick="goApplyImprove(1);">부적격</a>
			<a href="javascript:void(0);" class="btn_Lgray" style="position: absolute; left: 0;" onclick="prevRegCheckPopup()">기등록확인</a>
		</div>
	</div>
</div>

