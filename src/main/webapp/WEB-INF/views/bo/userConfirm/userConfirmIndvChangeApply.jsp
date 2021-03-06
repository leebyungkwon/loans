<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
var originPlMName 		= "${result.userRegInfo.plMName}";
var originPlMZId 		= "${result.userRegInfo.plMZId}";
var originPlCellphone 	= "${result.userRegInfo.plCellphone}";

var chgPlMName			= "";
var chgPlMZId			= "";

function pageLoad(){
	//위반이력코드 호출
	//goCallViolationCd();
	
	//이름,주민번호,휴대폰번호 변경 시 증빙서류 필수
	/*
	$("#plMName").on("propertychange change keyup paste input",function(){
		chgPlMName = $(this).val();
		
		if(originPlMName != chgPlMName || (chgPlMZId != "" && originPlMZId != chgPlMZId)){
			$("#chgVeriDoc1").prev().empty().append("주민등록증 또는 주민등록 초본(성명, 주민등록번호 변경 시) *");
			$("#chgVeriDoc1 > .inputFile").attr("data-essential","Y");
			$("#chgVeriDoc1 > .goFileReset").attr("data-essential","Y");
			$("#chgVeriDoc1 > .goFileDel").attr("data-essential","Y");
		}else{
			$("#chgVeriDoc1").prev().empty().append("주민등록증 또는 주민등록 초본(성명, 주민등록번호 변경 시)");
			$("#chgVeriDoc1 > .inputFile").attr("data-essential","N");
			$("#chgVeriDoc1 > .goFileReset").attr("data-essential","N");
			$("#chgVeriDoc1 > .goFileDel").attr("data-essential","N");
		}
	});
	$("#plMZId").on("propertychange change keyup paste input",function(){
		chgPlMZId = $(this).val();
		
		if(originPlMZId != chgPlMZId || (chgPlMName != "" && originPlMName != chgPlMName)){
			$("#chgVeriDoc1").prev().empty().append("주민등록증 또는 주민등록 초본(성명, 주민등록번호 변경 시) *");
			$("#chgVeriDoc1 > .inputFile").attr("data-essential","Y");
			$("#chgVeriDoc1 > .goFileReset").attr("data-essential","Y");
			$("#chgVeriDoc1 > .goFileDel").attr("data-essential","Y");
		}else{
			$("#chgVeriDoc1").prev().empty().append("주민등록증 또는 주민등록 초본(성명, 주민등록번호 변경 시)");
			$("#chgVeriDoc1 > .inputFile").attr("data-essential","N");
			$("#chgVeriDoc1 > .goFileReset").attr("data-essential","N");
			$("#chgVeriDoc1 > .goFileDel").attr("data-essential","N");
		}
	});
	*/
	$("#plCellphone").on("propertychange change keyup paste input",function(){
		var inputVal = $(this).val();
		
		if(originPlCellphone != inputVal){
			$("#chgVeriDoc2").prev().empty().append("정보변경 관련 서류 *<br>(개명시 주민등록초본, 휴대폰번호 변경시 휴대폰명의확인서 첨부 필요)");
			$("#chgVeriDoc2 > .inputFile").attr("data-essential","Y");
			$("#chgVeriDoc2 > .goFileReset").attr("data-essential","Y");
			$("#chgVeriDoc2 > .goFileDel").attr("data-essential","Y");
		}else{
			$("#chgVeriDoc2").prev().empty().append("정보변경 관련 서류<br>(개명시 주민등록초본, 휴대폰번호 변경시 휴대폰명의확인서 첨부 필요)");
			$("#chgVeriDoc2 > .inputFile").attr("data-essential","N");
			$("#chgVeriDoc2 > .goFileReset").attr("data-essential","N");
			$("#chgVeriDoc2 > .goFileDel").attr("data-essential","N");
		}
	});
}

//위반이력 영역 추가
function goViolationAdd(obj){
	
	var html 		= '';
	
	html += '<tr class="violationArea">';
	html += '<th>위반이력사항</th>';
	html += '<td colspan="3">';
	html += '<select name="violationCdArr">';
	html += '<option value="">선택해 주세요.</option>';
	
	<c:forEach var="list" items="${result.violationCodeList}">
		html += '<option value="${list.codeDtlCd}">'+"${list.codeDtlNm}"+'</option>';
	</c:forEach>
	
	/*
	for(var i = 0;i < codeListLen;i++){
		console.log(codeList[i].codeDtlCd);
		html += '<option value="'+codeList[i].codeDtlCd+'">'+codeList[i].codeDtlNm+'</option>';
	}
	*/
	
	html += '</select> '; //공백 제거 금지
	html += '<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a> '; //공백 제거 금지
	html += '<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDel(this);">-</a>';
	html += '</td>';
	html += '</tr>';
	
	$("#table > table").append(html);
}

//변경요청
function goUserChangeApply(){
	//validation
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("변경사유를 입력해 주세요.");
		$("#plHistTxt").focus();
		return;
	}
	if(!goFileEssentialChk()){
		alert(messages.COM0007);
		return;
	}
	//요청
	if(confirm("모집인 변경사항을 요청하시겠습니까?")){
		goFileTypeListDisabled();
		
		var p = {
			  name 		: "userRegInfoUpdFrm"
			, success 	: function (opt,result) {
				goUserConfirmList();
	 	    }
		}
		AjaxUtil.files(p);
	}
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

	<form name="userRegInfoUpdFrm" action="/member/confirm/userChangeApply" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="fileGrpSeq" value="${result.userRegInfo.fileSeq }"/>
	
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
						<td><input type="text" name="plMName" id="plMName" class="w100" value="${result.userRegInfo.plMName }" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
						<th>주민번호</th>
						<td><input type="text" name="plMZId" id="plMZId" class="w100" value="${result.userRegInfo.plMZId }" maxlength="14" placeholder="- 포함" data-vd='{"type":"pId","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td colspan="3"><input type="text" name="plCellphone" id="plCellphone" class="w100" value="${result.userRegInfo.plCellphone }" maxlength="13" placeholder="- 포함" data-vd='{"type":"mobileNo","len":"1,13","req":true,"msg":"휴대폰번호(- 포함)를 입력해 주세요."}'></td>
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
					<c:if test="${result.userRegInfo.plStat eq '10' || result.userRegInfo.plStat eq '11' || result.userRegInfo.plStat eq '12' }">
						<tr>
							<th>사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
						</tr>
					</c:if>
					<c:if test="${result.userRegInfo.plStat eq '5' or result.userRegInfo.plStat eq '6' or result.userRegInfo.plStat eq '7' or result.userRegInfo.plStat eq '10'}">
						<tr>
							<th>보완요청사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
						</tr>					
					</c:if>
					<tr>
						<th>변경사유</th>
						<td colspan="3">
							<textarea rows="6" cols="" id="plHistTxt" name="plHistTxt" class="w100"></textarea>
							<!-- <input type="text" name="plHistTxt" id="plHistTxt" class="w100" maxlength="200"/> -->
						</td>
					</tr>
					
					
					
<%-- 					<c:choose>
						<c:when test="${fn:length(result.violationInfoList) > 0 }">
							<c:forEach var="violationInfoList" items="${result.violationInfoList }" varStatus="status">
								<tr class="violationArea">
									<th>위반이력사항</th>
									<td colspan="3" <c:if test="${violationInfoList.applyYn eq 'Y' }">class="red"</c:if>>
										${violationInfoList.violationCdNm }
										<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a>
										<c:choose>
											<c:when test="${violationInfoList.vioNum ne null && violationInfoList.vioNum ne '' }">
												<c:if test="${violationInfoList.applyYn eq 'N' }">
													<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDataDelApply('${violationInfoList.violationSeq }',this);">-</a>
												</c:if>
											</c:when>
											<c:otherwise>
												<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDataDel('${violationInfoList.violationSeq }',this);">-</a>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="violationArea">
								<th>위반이력사항</th>
								<td colspan="3">
									<select name="violationCdArr">
										<option value="">선택해 주세요.</option>
										<c:forEach var="violationCodeList" items="${result.violationCodeList }">
											<option value="${violationCodeList.codeDtlCd }">${violationCodeList.codeDtlNm }</option>
										</c:forEach>
									</select>
									<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a>
									<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDel(this);">-</a>
								</td>
							</tr>
						</c:otherwise>
					</c:choose> --%>
					
					
					
					
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
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType1.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="1"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType1 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" data-fileType="1" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="1" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType2.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="2"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType2 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" data-fileType="2" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="2" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<c:if test="${result.userRegInfo.careerTyp eq '1' }">
						<tr>
							<th class="acenter">인증서(신규) *</th>
							<td>
								<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType4.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" readonly disabled>
								<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
								<input type="hidden" name="fileTypeList" value="4"/>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType4 ne null }">
										<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">다운로드</a>
										<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" data-fileType="4" data-essential="Y">삭제</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="4" data-essential="Y">초기화</a>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
					<c:if test="${result.userRegInfo.careerTyp eq '2' }">
						<tr>
							<th class="acenter">경력교육과정 수료증 *</th>
							<td>
								<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType3.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" readonly disabled>
								<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
								<input type="hidden" name="fileTypeList" value="3"/>
								<c:choose>
									<c:when test="${result.userRegInfo.fileType3 ne null }">
										<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">다운로드</a>
										<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" data-fileType="3" data-essential="Y">삭제</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="3" data-essential="Y">초기화</a>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
					<tr>
						<th class="acenter">경력증명서</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType5.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="5"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType5 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" data-fileType="5" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="5" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">위탁계약서</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType6.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="6"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType6 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" data-fileType="6" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="6" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType12.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType12.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="12"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType12 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType12.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType12.fileSeq }" data-fileType="12" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="12" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">대리인 신청 위임장 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType8.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="8"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType8 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }" data-fileType="8" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="8" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">위임인 인감증명서</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType9.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="9"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType9 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }" data-fileType="9" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="9" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">행정정보공동이용 사전동의서 *<br>(외국인인 경우 결격요건 확인서 및 본국 감독당국의 결격요건, 범죄이력 확인서류)</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType14.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType14.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile pdfOnly" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="14"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType14 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType14.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType14.fileSeq }" data-fileType="14" data-essential="Y" data-pdfOnly="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="14" data-essential="Y" data-pdfOnly="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">개인정보필수동의서 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType15.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="15"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType15 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }" data-fileType="15" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="15" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">정보변경 관련 서류<br>(개명시 주민등록초본, 휴대폰번호 변경시 휴대폰명의확인서 첨부 필요)</th>
						<td id="chgVeriDoc2">
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType11.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType11.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="11"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType11 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType11.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType11.fileSeq }" data-fileType="11" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="11" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
			<div class="btn_wrap">
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
				<a href="javascript:void(0);" class="btn_blue btn_right w100p" onclick="goUserChangeApply();">변경요청</a>
			</div>
		</div>
	</form>
</div>

