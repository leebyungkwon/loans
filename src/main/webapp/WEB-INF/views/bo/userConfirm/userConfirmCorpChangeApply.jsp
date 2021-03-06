<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//위반이력 코드
	//goCallViolationCd();
	
	//datepicker
	goDatepickerDraw();
}

//위반이력 영역 추가
function goViolationAdd(obj){
	
	var html 		= '';
	
	html += '<tr class="violationArea">';
	html += '<th>위반이력사항</th>';
	html += '<td colspan="3">';
	html += '<select name="violationCdArr" class="violationCd">';
	html += '<option value="">선택해 주세요.</option>';
	
	<c:forEach var="list" items="${result.violationCodeList}">
		html += '<option value="${list.codeDtlCd}">'+"${list.codeDtlNm}"+'</option>';
	</c:forEach>
	
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
	if("${result.userRegInfo.imwonCnt}" == 0){
		if(confirm("최소 1명 이상의 대표자 및 임원을 등록해야 합니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab3("2");
			return;
		}else{
			return;
		}
	}else{
		if("${result.userRegInfo.imwonEduCnt}" == 0){
			if(confirm("최소 1명 이상의 유효한 교육이수번호/인증서번호가 등록된 대표자 및 임원을 등록해야 합니다.\n해당 탭으로 이동하시겠습니까?")){
				goTab("2");
				return;
			}else{
				return;
			}
		}
	}
	if("${result.userRegInfo.expertCnt}" == 0){
		if(confirm("최소 1명 이상의 업무수행인력을 등록해야 합니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab3("3");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.itCnt}" == 0){
		if(confirm("최소 1명 이상의 전산인력을 등록해야 합니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab3("4");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.imwonFileCompYn}" == "N"){
		if(confirm("대표자 및 임원의 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab3("2");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.expertFileCompYn}" == "N"){
		if(confirm("업무수행인력의 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab3("3");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.itFileCompYn}" == "N"){
		if(confirm("전산인력의 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab3("4");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.etcFileCompYn}" == "N"){
		if(confirm("기타 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab3("5");
			return;
		}else{
			return;
		}
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
			<h2>모집인 조회 및 변경 - 법인</h2>
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
	
	<form name="userRegInfoUpdFrm" action="/member/confirm/userChangeApply" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="fileGrpSeq" value="${result.userRegInfo.fileSeq }"/>
	
		<div class="contents">
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
						<th>금융상품유형</th>
						<td colspan="3">${result.userRegInfo.plProductNm }</td>
					</tr>
					<tr>
						<th>업종</th>
						<td colspan="3"><input type="text" name="plWork" class="w100" value="${result.userRegInfo.plWork }" maxlength="20"></td>
					</tr>
					<tr>
						<th>상호</th>
						<td><input type="text" name="plMerchantName" class="w100" value="${result.userRegInfo.plMerchantName }" maxlength="30" data-vd='{"type":"text","len":"1,30","req":true,"msg":"상호를 입력해 주세요."}'></td>
						<th>대표이사</th>
						<td><input type="text" name="plCeoName" class="w100" value="${result.userRegInfo.plCeoName }" maxlength="30" data-vd='{"type":"text","len":"1,10","req":true,"msg":"대표이사명을 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>대표이사 주민번호</th>
						<td><input type="text" name="plMZId" class="w100" value="${result.userRegInfo.plMZId }" maxlength="14" placeholder="- 포함" data-vd='{"type":"pId","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
						<th>대표이사 휴대폰번호</th>
						<td><input type="text" name="plCellphone" class="w100" value="${result.userRegInfo.plCellphone }" maxlength="13" placeholder="- 포함" data-vd='{"type":"mobileNo","len":"1,13","req":true,"msg":"휴대폰번호(- 포함)를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>법인등록번호</th>
						<td>${result.userRegInfo.plMerchantNo }</td>
						<th>설립년월일</th>
						<td>
							<input type="text" name="corpFoundDate" id="corpFoundDate" onclick="goDatepickerShow(this);" class="w100" value="${result.userRegInfo.corpFoundDate }" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"설립년월일(- 포함)을 입력해 주세요."}' readonly="readonly">
						 	<div class="calendar01"></div>
						</td>
					</tr>
					<tr>
						<th>본점소재지</th>
						<td colspan="3">
							<select name="addr">
								<c:forEach var="addrCodeList" items="${result.addrCodeList }">
									<option value="${addrCodeList.codeDtlCd }" <c:if test="${addrCodeList.codeDtlCd eq result.userRegInfo.addr }">selected="selected"</c:if>>${addrCodeList.codeDtlNm }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>상세주소(법인등기부등본상)</th>
						<td colspan="3"><input type="text" name="addrDetail" class="w100" value="${result.userRegInfo.addrDetail }" maxlength="200" data-vd='{"type":"text","len":"1,200","req":true,"msg":"상세주소(법인등기부등본상)를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>자본금(백만원)</th>
						<td colspan="3"><input type="text" name="capital" class="w100" value="${result.userRegInfo.capital }" maxlength="20" data-vd='{"type":"num","len":"1,20","req":true,"msg":"자본금(백만원)을 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>의결권있는 발행주식 총수</th>
						<td colspan="3"><input type="text" name="votingStockCnt" class="w100" value="${result.userRegInfo.votingStockCnt }" maxlength="20" data-vd='{"type":"num","len":"1,20","req":true,"msg":"의결권있는 발행주식 총수를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>계약일자</th>
						<td>
							<input type="text" name="comContDate" id="comContDate" onclick="goDatepickerShow(this);" class="w100" value="${result.userRegInfo.comContDate }" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"계약일자(- 포함)를 입력해 주세요."}' readonly="readonly">
						 	<div class="calendar01"></div>
						</td>
						<th>위탁예정기간</th>
						<td>
							<input type="text" name="entrustDate" onclick="goDatepickerShow(this);" class="w100" value="${result.userRegInfo.entrustDate }" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"위탁예정기간(- 포함)을 입력해 주세요."}' readonly="readonly">
						 	<div class="calendar01"></div>
						</td>
					</tr>
					<c:if test="${result.userRegInfo.plStat eq '5' || result.userRegInfo.plStat eq '6' || result.userRegInfo.plStat eq '7' || result.userRegInfo.plStat eq '10'}">
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
					<tr>
						<th>변경사유</th>
						<td colspan="3">
							<textarea rows="6" cols="" id="plHistTxt" name="plHistTxt" class="w100"></textarea>
							<!-- <input type="text" name="plHistTxt" id="plHistTxt" class="w100" maxlength="200"/> -->
						</td>
					</tr>
					
					
					<%-- 
					<c:choose>
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
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType1.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="1"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType1 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" data-fileType="1" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="1" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">법인등기부등본 *</th>
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
					<tr>
						<th class="acenter">설립, 등록 신청의 의사결정을 증명하는 서류 *<br />(등록신청 관련 발기인총회, 창립주주총회 또는 이사회의 공증을 받은 의사록)</th>
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
					<tr>
						<th class="acenter">본점의 위치 및 명칭을 기재한 서류<br />(법인등기부에서 확인되지 않는 경우 제출)</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType4.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="4"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType4 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" data-fileType="4" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="4" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">주주명부 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType5.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="5"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType5 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" data-fileType="5" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="5" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">영위하는 다른 업종에 대한 증빙서류</th>
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
						<th class="acenter">업무수행기준요건관련 서류 *</th>
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
						<th class="acenter">위탁계약서</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType31.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType31.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="31"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType31 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType31.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType31.fileSeq }" data-fileType="31" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="31" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType32.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType32.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="32"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType32 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType32.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType32.fileSeq }" data-fileType="32" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="32" data-essential="N">초기화</a>
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

