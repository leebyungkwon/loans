<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript" src="/static/js/recruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
}

//승인
function goRecruitApply(){
	if(confirm("요청사항을 승인하시겠습니까?")){
		var p = {
			  url		: "/admin/recruit/updatePlStat"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: '7'
				,plRegStat		: '2'
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/recruit/recruitPage";
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
function goRecruitImprove(){
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("사유를 입력해 주세요");
		$("#plHistTxt").focus();
		return false;
	}
	
	if(confirm("보완요청을 하시겠습니까?")){
		var p = {
			  url		: "/admin/recruit/updatePlStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '5'
				,plHistTxt	: $("#plHistTxt").val()
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/recruit/recruitPage";
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
					<td>${result.applyInfo.plRegStatNm } <a href="javascript:alert('준비중입니다.');" class="btn_Lgray btn_small mgl5">이력보기</a></td>
					<th>결제여부</th>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.plPayStat ne null }">
								${result.applyInfo.plPayStat } (국민카드 / 2021.10.20)
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
					<th>법인등록번호</th>
					<td colspan="3">${result.applyInfo.plMerchantNo }</td>
				</tr>
				<tr>
					<th>설립년월일</th>
					<td colspan="3">${result.applyInfo.corpFoundDate }</td>
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
						<tr>
							<th>해지요청사유</th>
							<td colspan="3">${result.applyInfo.plHistCd }</td>
						</tr>
						<tr>
							<th>해지일자</th>
							<td colspan="3">${result.applyInfo.comHaejiDate }</td>
						</tr>
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
					<th class="acenter">정관 *</th>
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
					<th class="acenter">법인등기부등본 *</th>
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
					</td>
				</tr>
				<tr>
					<th class="acenter">설립, 등록 신청의 의사결정을 증명하는 서류 *<br />(등록신청 관련 발기인총회, 창립주주총회 또는 이사회의 공증을 받은 의사록)</th>
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
							<label for="check_cd5">체크1</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd6" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd6}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType3.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType3.fileSeq }" >
							<label for="check_cd6">체크2</label>
						</div>
					</td>
				</tr>
				<tr>
					<th class="acenter">본점의 위치 및 명칭을 기재한 서류<br />(법인등기부에서 확인되지 않는 경우 제출)</th>
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
							<label for="check_cd7">체크1</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd8" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd8}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType4.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType4.fileSeq }" >
							<label for="check_cd8">체크2</label>
						</div>
					</td>
				</tr>
				<tr>
					<th class="acenter">주주명부 *</th>
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
					<th class="acenter">영위하는 다른 업종에 대한 증빙서류 *</th>
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
							<label for="check_cd10">체크1</label>
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd11" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd11}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType6.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType6.fileSeq }" >
							<label for="check_cd11">체크2</label>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goApplyList();">목록</a>
			<c:if test="${result.recruitInfo.plStat eq '2' or result.recruitInfo.plStat eq '3' or result.recruitInfo.plStat eq '4'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply();">승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove();">보완</a>
			</c:if>
		</div>
	</div>
</div>

