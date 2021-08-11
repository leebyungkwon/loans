<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//승인요청상태이면 수정 불가
	var plStat = "${result.userRegInfo.plStat}";
	if(plStat == "2"){
		$("input").prop("readonly",true);
		$("option").attr("disabled",true);
		$(".goFileDel").remove();
		$(".inputFile").each(function(){
			$(this).parent("td").html("-");
		});
	}
	
	//datepicker
	goDatepickerDraw();
}

//수정
function goUserRegInfoUpdt() {
	//수정
	if(confirm("저장하시겠습니까?")){
		goFileTypeListDisabled();
		
		var p = {
			  name 		: "userRegInfoUpdFrm"
			, success 	: function (opt,result) {
				//location.href = "/member/user/userRegPage";
				goTab("1");
	 	    }
		}
		AjaxUtil.files(p);
	}
}

//모집인 등록 삭제
function goUserRegInfoCancel() {
	if(confirm("삭제하시겠습니까?")){
		var p = {
			  url		: "/member/user/deleteUserRegInfo"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
			}
			, success 	: function (opt,result) {
				goUserRegInfoList();
		    }
		}
		AjaxUtil.post(p);
	}
}

//승인요청
function goUserAcceptApply(){
	//validation
	if(!goFileEssentialChk()){
		alert(messages.COM0007);
		return;
	}
	if("${result.userRegInfo.imwonCnt}" == 0){
		if(confirm("최소 1명 이상의 대표자 및 임원을 등록해야 합니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab("2");
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
		if(confirm("최소 1명 이상의 전문인력을 등록해야 합니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab("3");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.itCnt}" == 0){
		if(confirm("최소 1명 이상의 전산인력을 등록해야 합니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab("4");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.imwonFileCompYn}" == "N"){
		if(confirm("대표자 및 임원의 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab("2");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.expertFileCompYn}" == "N"){
		if(confirm("전문인력의 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab("3");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.itFileCompYn}" == "N"){
		if(confirm("전산인력의 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab("4");
			return;
		}else{
			return;
		}
	}
	if("${result.userRegInfo.etcFileCompYn}" == "N"){
		if(confirm("기타 필수 첨부서류가 누락되었습니다.\n해당 탭으로 이동하시겠습니까?")){
			goTab("5");
			return;
		}else{
			return;
		}
	}
	//요청
	if(confirm("승인요청하시겠습니까?")){
		$("#userRegInfoUpdFrm").attr("action","/member/user/userAcceptApply2");
		
		goFileTypeListDisabled();
		
		var p = {
			  name 		: "userRegInfoUpdFrm"
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("승인요청되었습니다.");
					location.href = "/member/user/userRegPage";
				}else if(result.data == -2){
					alert("이미 승인완료된 모집인입니다.");
					location.reload();
				}else if(result.data == -1){
					alert("법인이 승인되지 않은 법인사용자가 존재합니다.");
					return;
				}else if(result.data == -3){
					alert("잘못된 확장자의 첨부파일을 등록 하였습니다.[0003]");
					return;
				}
	 	    }
		}
		AjaxUtil.files(p);
	}
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
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
						<th>가등록번호</th>
						<td colspan="3">${result.userRegInfo.preLcNum }</td>
					</tr>
					<tr>
						<th>모집인 상태</th>
						<td colspan="3">${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.userRegInfo.masterSeq }');">이력보기</a></td>
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
						<td colspan="3"><input type="text" name="plWork" class="w100" value="${result.userRegInfo.plWork }" maxlength="50"></td>
					</tr>
					<tr>
						<th>상호</th>
						<td>${result.userRegInfo.plMerchantName }</td>
						<th>대표이사</th>
						<td>${result.userRegInfo.plCeoName }</td>
					</tr>
					<tr>
						<th>대표이사 주민번호</th>
						<td>${result.userRegInfo.plMZId }</td>
						<th>대표이사 휴대폰번호</th>
						<td>${result.userRegInfo.plCellphone }</td>
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
							<input type="text" name="comContDate" onclick="goDatepickerShow(this);" readonly="readonly" value="${result.userRegInfo.comContDate }" class="w100" maxlength="10" placeholder="- 포함">
							<div class="calendar01"></div>
						</td>
						<th>위탁예정기간</th>
						<td>
							<input type="text" name="entrustDate" onclick="goDatepickerShow(this);" readonly="readonly" value="${result.userRegInfo.entrustDate }" class="w100" maxlength="10" placeholder="- 포함">
							<div class="calendar01"></div>
						</td>
					</tr>
					<c:if test="${result.userRegInfo.plStat eq '5' || result.userRegInfo.plStat eq '6' || result.userRegInfo.plStat eq '7' }">
						<tr>
							<th>보완요청사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" data-fileType="1" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="1"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="1" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">법인등기부등본 *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType2 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">${result.userRegInfo.fileType2.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" data-fileType="2" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="2"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="2" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">설립, 등록 신청의 의사결정을 증명하는 서류 *<br />(등록신청 관련 발기인총회, 창립주주총회 또는 이사회의 공증을 받은 의사록)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType3 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">${result.userRegInfo.fileType3.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" data-fileType="3" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="3"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="3" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">본점의 위치 및 명칭을 기재한 서류<br />(법인등기부에서 확인되지 않는 경우 제출)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType4 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">${result.userRegInfo.fileType4.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" data-fileType="4" data-essential="N" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="4"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="4" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">주주명부 *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType5 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">${result.userRegInfo.fileType5.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" data-fileType="5" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="5"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="5" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">영위하는 다른 업종에 대한 증빙서류</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType6 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">${result.userRegInfo.fileType6.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" data-fileType="6" data-essential="N" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="6"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="6" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">업무수행기준요건관련 서류 *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType15 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }">${result.userRegInfo.fileType15.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }" data-fileType="15" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="15"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="15" data-essential="Y">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">위탁계약서</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType31 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType31.fileSeq }">${result.userRegInfo.fileType31.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType31.fileSeq }" data-fileType="31" data-essential="N" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="31"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="31" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType32 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType32.fileSeq }">${result.userRegInfo.fileType32.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType32.fileSeq }" data-fileType="32" data-essential="N" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="32"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="32" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
	
			<div class="btn_wrap">
				<c:if test="${result.userRegInfo.plStat ne '2' && result.userRegInfo.fileCompYn eq 'Y' }"> 
					<c:if test="${result.userRegInfo.plRegStat eq '1' && result.userRegInfo.plStat ne '10' && result.userRegInfo.plStat ne '11' && result.userRegInfo.plStat ne '12' }">
						<a href="javascript:void(0);" class="btn_blue" onclick="goUserAcceptApply();" style="position: absolute; left: 0;">승인요청</a>
					</c:if>
				</c:if>
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
				<c:if test="${result.userRegInfo.plStat ne '2' }"> 
					<c:if test="${result.userRegInfo.plStat ne '10' }">
						<a href="javascript:void(0);" class="btn_blue btn_right02" onclick="goUserRegInfoUpdt();">저장</a>
					</c:if>
				</c:if>
				<c:if test="${result.userRegInfo.plStat ne '2' || result.userRegInfo.plStat eq '10' }"> 
					<a href="javascript:void(0);" class="btn_Lgray btn_right" onclick="goUserRegInfoCancel();">삭제</a>
				</c:if>
			</div>
		</div>
	</form>
</div>

