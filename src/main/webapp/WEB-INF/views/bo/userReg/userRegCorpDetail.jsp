<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
function pageLoad(){
	//newForm.append($('<input/>', {type: 'hidden', name: 'data1', value:'value1' }));

	//파일찾기
	$(".goFileUpload").on("click", function () {
		$(this).prev().prev().click();
	});
	
	//첨부파일명 보여주기
	$(".inputFile").on("change", function () {
		var fileVal 	= $(this).val().split("\\");
		var fileName 	= fileVal[fileVal.length - 1];
		$(this).prev().val(fileName);
	});
	
	//첨부파일 삭제
	$(".goFileDel").on("click", function () {
		var fileSeq 	= $(this).attr("data-fileSeq");
		var fileType 	= $(this).attr("data-fileType");
		var essential 	= $(this).attr("data-essential");
		var targetArea 	= $(this).parent();
		
		if(confirm("삭제하시겠습니까?")){
			var p = {
				  url		: "/common/fileDelete"
				, param		: {
					fileSeq : fileSeq
				}
				, success	: function(opt,result) {
					if(result.data > 0){
						alert("삭제되었습니다.");
						
						var html = '';
						
						html += '<input type="text" class="w50 file_input" readonly disabled>';
						html += '<input type="file" name="files" class="inputFile" data-essential="'+essential+'" style="display: none;"/>';
						html += '<input type="hidden" name="fileTypeList" value="'+fileType+'"/>';
						html += '<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>';
						
						targetArea.html(html);
					}
				}
			}
			AjaxUtil.post(p);
		}
	});
	
	//첨부파일 다운로드
	$(".goFileDownload").on("click", function () {
		var fileSeq 	= $(this).attr("data-fileSeq");
		
		var p = {
			  url 			: "/common/fileDown"
			, contType		: "application/json; charset=UTF-8"
			, responseType	: "arraybuffer"
			, param 		: {
				fileSeq : fileSeq
			}
		}
		AjaxUtil.post(p);
	});
	
	//대표자 및 임원관련 사항 엑셀 업로드
	$("#").on("change", function () {
		alert("대표자 및 임원관련 사항 엑셀 업로드 시작");
	});
	
	//업무수행에 필요한 전문성을 갖춘 인력에 관한 사항 엑셀 업로드
	$("#").on("change", function () {
		alert("업무수행에 필요한 전문성을 갖춘 인력에 관한 사항 엑셀 업로드 시작");
	});
	
	//전산설비 운영, 유지 및 관리를 전문적으로 수행할 수 있는 인력에 관한 사항 엑셀 업로드
	$("#").on("change", function () {
		alert("전산설비 운영, 유지 및 관리를 전문적으로 수행할 수 있는 인력에 관한 사항 엑셀 업로드 시작");
	});
}

//수정
function goUserRegInfoUpdt() {
	//validation
	var vdChkResult = 0;
	
	$(".inputFile").each(function(){
		if($(this).attr("data-essential") == "Y" && $(this).val() == ""){
			vdChkResult++;
		}
	});
	if(vdChkResult > 0){
		alert("필수 첨부서류가 누락되었습니다.");
		return;
	}
	
	//수정
	var p = {
		  name 		: "userRegInfoUpdFrm"
		, success 	: function (opt,result) {
			if(result.data > 0){
				alert("저장되었습니다.");
				location.reload();
			}
 	    }
	}
	AjaxUtil.files(p);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록 - 법인</h2>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
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
						<td>${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5">이력보기</a></td>
						<th>결제여부</th>
						<td>${result.userRegInfo.plPayStat } (국민카드 / 2021.10.20)</td>
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
						<th>상호</th>
						<td><input type="text" name="plMerchantName" class="w100" value="${result.userRegInfo.plMerchantName }"></td>
						<th>대표이사</th>
						<td><input type="text" name="plCeoName" class="w100" value="${result.userRegInfo.plCeoName }"></td>
					</tr>
					<tr>
						<th>법인등록번호</th>
						<td colspan="3"><input type="text" name="plMerchantNo" class="w100" value="${result.userRegInfo.plMerchantNo }"></td>
					</tr>
					<tr>
						<th>설립년월일</th>
						<td colspan="3"><input type="text" name="corpFoundDate" class="w100" value="${result.userRegInfo.corpFoundDate }"></td>
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
						<th>상세주소(법인등기부본상)</th>
						<td colspan="3"><input type="text" name="addrDetail" class="w100" value="${result.userRegInfo.addrDetail }"></td>
					</tr>
					<tr>
						<th>자본금</th>
						<td colspan="3"><input type="text" name="capital" class="w100" value="${result.userRegInfo.capital }"></td>
					</tr>
					<tr>
						<th>계약일자</th>
						<td><input type="text" name="comContDate" class="w50" value="${result.userRegInfo.comContDate }"></td>
						<th>위탁예정기간</th>
						<td><input type="text" name="entrustDate" class="w50" value="${result.userRegInfo.entrustDate }"></td>
					</tr>
				</table>
			</div>
			
			<h3>대표자 및 임원관련 사항</h3>
			<div id="table02">
				<table class="view_table">
					<tr>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
							<a href="javascript:alert('대표자 및 임원관련 사항 엑셀 샘플 다운로드');" class="btn_Lgray btn_small mgl5">샘플 다운로드</a>
						</td>
					</tr>
				</table>
			</div>
			
			<h3>업무수행에 필요한 전문성을 갖춘 인력에 관한 사항</h3>
			<div id="table03">
				<table class="view_table">
					<tr>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
							<a href="javascript:alert('업무수행에 필요한 전문성을 갖춘 인력에 관한 사항 엑셀 샘플 다운로드');" class="btn_Lgray btn_small mgl5">샘플 다운로드</a>
						</td>
					</tr>
				</table>
			</div>

			<h3>전산설비 운영, 유지 및 관리를 전문적으로 수행할 수 있는 인력에 관한 사항</h3>
			<div id="table04">
				<table class="view_table">
					<tr>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" class="inputFile" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
							<a href="javascript:alert('전산설비 운영, 유지 및 관리를 전문적으로 수행할 수 있는 인력에 관한 사항 엑셀 샘플 다운로드');" class="btn_Lgray btn_small mgl5">샘플 다운로드</a>
						</td>
					</tr>
				</table>
			</div>
	
			<h3>첨부서류</h3>
			<div class="sub_tit">
				<h4>1. 신청인 관련 서류</h4>
				<p class="sub_txt">* 필수서류</p>
			</div>
			<div id="table05">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">정관 *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="1"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">법인등기부등본 *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="2"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">설립, 등록 신청의 의사결정을 증명하는 서류 *<br />(등록신청 관련 발기인총회, 창립주주총회 또는 이사회의 공증을 받은 의사록)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="3"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">본점의 위치 및 명칭을 기재한 서류<br />(법인등기부에서 확인되지 않는 경우 제출)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="4"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">주주명부 *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="5"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">영위하는 다른 업종에 대한 증빙서류 *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="6"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>2. 대표 및 임원관련 서류</h4>
			</div>
			<div id="table06">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr class="파일 추가 및 삭제 관련해서....">
						<th class="acenter">대표자 이력서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="7"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">대표자 경력증명서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="8"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="9"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">임원 이력서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="10"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">임원 경력증명서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="11"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="12"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>3. 금융상품 관련 서류</h4>
			</div>
			<div id="table07">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">금융상품 유형, 내용에 대한 설명자료</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="13"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>4. 교육이수관련 서류</h4>
			</div>
			<div id="table08">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">대표 교육과정 이수확인서 (경력) 또는 인증서(신규) *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="14"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">대표 경력증명서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="15"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">임원 교육과정 이수확인서 (경력) 또는 인증서(신규) *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="16"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">임원 경력증명서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="17"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">+</a>
							<a href="#" class="btn_Lgray btn_add mgl5 mgt7">-</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>5. 업무수행기준요건관련 서류</h4>
			</div>
			<div id="table09">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">업무수행기준요건관련 서류</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="18"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>6. 전문인력관련 서류</h4>
			</div>
			<div id="table10">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">이수확인서 (경력) 또는 인증서(신규) *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="19"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">경력증명서 (업무인력)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="20"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">경력증명서 (전산인력)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="21"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">자격확인 서류 (전산인력)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="22"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>7. 물적설비관련 서류</h4>
			</div>
			<div id="table11">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">물적 설비내역에 대한 증빙서류</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="23"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">사무공간 / 전산설비 등의 임차계약서 사본</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="24"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">부동산 등기부등본</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="25"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>8. 사회적 신용</h4>
			</div>
			<div id="table12">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">신청인의 사회적신용에 대한 결격사유없음 확인서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="26"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="sub_tit mgt30">
				<h4>9. 기타</h4>
			</div>
			<div id="table13">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">대리인 신청 위임장 (위임인 인감날인)</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="27"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">위임인 인감증명서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="28"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="btn_wrap">
				<a href="javascript:history.back();" class="btn_gray">목록</a>
				<c:if test="${result.userRegInfo.plStat ne '2' }"> 
					<!-- 승인요청상태가 아닐 때만 수정/삭제 가능 -->
					<a href="javascript:void(0);" class="btn_black btn_right02" onclick="goUserRegInfoUpdt();">저장</a>
					<a href="javascript:alert('등록신청취소건으로 은행연합회에 공유되고 해당 내용은 삭제되야 한다.');" class="btn_black btn_right" onclick="">삭제</a>
				</c:if>
			</div>
		</div>
	</form>
</div>

