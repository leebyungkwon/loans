<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
			if(WebUtil.isNull($(this).prev().val())){
				$(this).parent("td").html("-");
			}else{
				$(this).parent("td").html('<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="'+$(this).prev().attr("data-fileSeq")+'">'+$(this).prev().val()+'</a>');
			}
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
				$("#pageFrm").attr("action","/member/user/userRegIndvDetail");
				$("#pageFrm").submit();
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
			<h2>모집인 등록 - 개인</h2>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
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
						<td colspan="3">
							<select name="addr">
								<c:forEach var="addrCodeList" items="${result.addrCodeList }">
									<option value="${addrCodeList.codeDtlCd }" <c:if test="${addrCodeList.codeDtlCd eq result.userRegInfo.addr }">selected="selected"</c:if>>${addrCodeList.codeDtlNm }</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>상세주소</th>
						<td colspan="3"><input type="text" name="addrDetail" class="w100" value="${result.userRegInfo.addrDetail }" maxlength="200" data-vd='{"type":"text","len":"1,200","req":true,"msg":"상세주소를 입력해 주세요."}'></td>
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
					<c:if test="${result.userRegInfo.plStat eq '10' || result.userRegInfo.plStat eq '11' || result.userRegInfo.plStat eq '12' }">
						<tr>
							<th>사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
						</tr>
					</c:if>
					<tr>
						<th>승인요청사유</th>
						<td colspan="3">
							<textarea rows="6" cols="" id="applyHistTxt" name="applyHistTxt" class="w100">${result.userRegInfo.applyHistTxt }</textarea>
						</td>
					</tr>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" data-fileType="1" data-essential="N" data-realDel="Y">삭제</a>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" data-fileType="2" data-essential="Y" data-realDel="Y">삭제</a>
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
										<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" data-fileType="4" data-essential="Y" data-realDel="Y">삭제</a>
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
										<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" data-fileType="3" data-essential="Y" data-realDel="Y">삭제</a>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" data-fileType="5" data-essential="N" data-realDel="Y">삭제</a>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" data-fileType="6" data-essential="N" data-realDel="Y">삭제</a>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType12.fileSeq }" data-fileType="12" data-essential="N" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="12" data-essential="N">초기화</a>
								</c:otherwise>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType7.fileSeq }" data-fileType="7" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="7"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					 --%>
					<tr>
						<th class="acenter">대리인 신청 위임장 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType8.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="8"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType8 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }" data-fileType="8" data-essential="Y" data-realDel="Y">삭제</a>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }" data-fileType="9" data-essential="N" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="9" data-essential="N">초기화</a>
								</c:otherwise>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType13.fileSeq }" data-fileType="13" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="13"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					 --%>
					<tr>
						<th class="acenter">행정정보공동이용 사전동의서 *<br>(외국인인 경우 결격요건 확인서 및 본국 감독당국의 결격요건, 범죄이력 확인서류)</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType14.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType14.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile pdfOnly" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="14"/>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType14 ne null }">
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType14.fileSeq }">다운로드</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType14.fileSeq }" data-fileType="14" data-essential="Y" data-pdfOnly="Y" data-realDel="Y">삭제</a>
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
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }" data-fileType="15" data-essential="Y" data-realDel="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="15" data-essential="Y">초기화</a>
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

