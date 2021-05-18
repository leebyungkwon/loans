<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
var originPlMName 		= "${result.userRegInfo.plMName}";
var originPlMZId 		= "${result.userRegInfo.plMZId}";
var originPlCellphone 	= "${result.userRegInfo.plCellphone}";

function pageLoad(){
	//위반이력사항(코드)
	var violationCode = {
		 useCode 	: true
		,code 		: 'VIT001'
		,target 	: '.violationCd'
		,updData 	: ''
	};
	DataUtil.selectBox(violationCode);
}

//위반이력사항 영역 추가
function goViolationAdd(obj) {
	var html = $(obj).parent().parent().clone();
	$("#table > table").append(html);
}

//위반이력사항 영역 삭제
function goViolationDel(obj) {
	var violationAreaLen = $(".violationArea").length;
	if(violationAreaLen == 1){
		alert("더 이상 삭제할 수 없습니다.");
		return;
	}
	$(obj).closest("tr").remove();
}

//변경요청
function goUserChangeApply(){
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("변경사유를 입력해 주세요.");
		$("#plHistTxt").focus();
		return;
	}
	//이름,주민번호,휴대폰번호 변경 시 증빙서류 필수
	if(originPlMName != $("#plMName").val() || originPlMZId != $("#plMZId").val()){
		//$("#chgVeriDoc1").parent().prev().append(" *");
		$("#chgVeriDoc1").attr("data-essential","Y");
	}else{
		$("#chgVeriDoc1").attr("data-essential","N");
	}
	if(originPlCellphone != $("#plCellphone").val()){
		//$("#chgVeriDoc1").parent().prev().append(" *");
		$("#chgVeriDoc2").attr("data-essential","Y");
	}else{
		$("#chgVeriDoc2").attr("data-essential","N");
	}
	//validation
	if(!goFileEssentialChk()){
		alert(messages.COM0006);
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
						<td><input type="text" name="plMName" id="plMName" class="w100" value="${result.userRegInfo.plMName }" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
						<th>주민번호</th>
						<td><input type="text" name="plMZId" id="plMZId" class="w100" value="${result.userRegInfo.plMZId }" maxlength="14" placeholder="- 포함" data-vd='{"type":"text","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
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
					<tr>
						<th>변경사유</th>
						<td colspan="3"><input type="text" name="plHistTxt" id="plHistTxt" class="w100" maxlength="200"/></td>
					</tr>
					<tr class="violationArea">
						<th>위반이력사항</th>
						<td colspan="3">
							<select name="violationCdArr" class="violationCd"></select>
							<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a>
							<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationDel(this);">-</a>
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
						<th class="acenter">사진 (등록증 게시용) *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType1 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }">${result.userRegInfo.fileType1.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" data-fileType="1" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="1"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일 *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType2 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">${result.userRegInfo.fileType2.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" data-fileType="2" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="2"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">교육과정 이수확인서 (경력)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType3 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">${result.userRegInfo.fileType3.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" data-fileType="3" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="3"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="3" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">인증서(신규)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType4 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">${result.userRegInfo.fileType4.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" data-fileType="4" data-essential="N">삭제</a>
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
						<th class="acenter">경력증명서 *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType5 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">${result.userRegInfo.fileType5.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" data-fileType="5" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="5"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">금융상품 유형, 내용에 대한 설명자료(계약서) *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType6 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">${result.userRegInfo.fileType6.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" data-fileType="6" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="6"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">결격사유없음 확인서 (파산, 피한정후견인등) *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType7 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType7.fileSeq }">${result.userRegInfo.fileType7.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType7.fileSeq }" data-fileType="7" data-essential="Y">삭제</a>
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
					<tr>
						<th class="acenter">대리인 신청 위임장(위임인 인간날인)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType8 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }">${result.userRegInfo.fileType8.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }" data-fileType="8" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="8"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="8" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">위임인 인감증명서</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType9 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }">${result.userRegInfo.fileType9.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }" data-fileType="9" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="9"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="9" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">주민등록증 또는 주민등록 초본(성명, 주민등록번호 변경 시)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="chgVeriDoc1" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="10"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="10" data-essential="N">초기화</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">휴대폰 명의 확인서(휴대폰번호 변경 시)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="chgVeriDoc2" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="11"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="11" data-essential="N">초기화</a>
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
