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
	$("#userRegImwonFile").on("change", function () {
		alert("대표자 및 임원관련 사항 엑셀 업로드 시작");
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

	<div class="tap_wrap">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div id="file_table" class="mgt30">
		<table class="view_table">
			<tbody><tr>
				<td class="pdr0">
					<input type="text" class="top_file_input file_input" readonly disabled>
					<input type="file" name="files" id="userRegImwonFile" class="inputFile" style="display: none;"/>
					<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegImwonFile').click();">파일찾기</a>
					<a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goSampleDownload();">샘플 다운로드</a>
				</td>
			</tr>
		</tbody></table>
	</div>

	<div class="btn_wrap02">
		<div class="right">
			<a href="#" class="btn_gray btn_middle mgr5">추가</a>
			<a href="javascript:void(0);" class="btn_blue btn_middle" onclick="">저장</a>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegCorpImwon" method="post" enctype="multipart/form-data">
		<div class="contents">
			<div class="data_wrap">
				<div id="table02">
					<table class="view_table">
						<colgroup>
							<col width="15%">
							<col width="35%">
							<col width="15%">
							<col width="35%">
						</colgroup>
						<tbody>
							<tr>
								<th>신규경력 구분</th>
								<td colspan="3">신규</td>
							</tr>
							<tr>
								<th>이름</th>
								<td><input type="text" class="w100" value="홍길동"></td>
								<th>주민번호</th>
								<td><input type="text" class="w100" value="911111-1234567"></td>
							</tr>
							<tr>
								<th>직위</th>
								<td><input type="text" class="w100" value="대표자"></td>
								<th>금융상품유형</th>
								<td><input type="text" class="w100" value="대출"></td>
							</tr>
							<tr>
								<th>교육이수번호</th>
								<td colspan="3"><input type="text" class="w100" value="2021021315"></td>
							</tr>
							<tr>
								<th>경력시작일</th>
								<td><input type="text" class="w100" value="2021-01-01"></td>
								<th>경력종료일</th>
								<td><input type="text" class="w100" value="2021-05-03"></td>
							</tr>
							<tr>
								<th>상근여부</th>
								<td><input type="text" class="w100" value="상근"></td>
								<th>전문인력여부</th>
								<td><input type="text" class="w100" value="비상근"></td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<h3>1. 대표 및 임원관련 서류</h3>
				<div id="table06">
					<table class="view_table">
						<colgroup>
							<col width="38%">
							<col width="62%">
						</colgroup>
						<tbody>
							<tr>
								<th class="acenter">대표자 이력서</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
							<tr>
								<th class="acenter">대표자 경력증명서</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
							<tr>
								<th class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
							<tr>
								<th class="acenter">인감증명서</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<h3>2. 금융상품 관련 서류</h3>
				<div id="table07">
					<table class="view_table">
						<colgroup>
							<col width="38%">
							<col width="62%">
						</colgroup>
						<tbody>
							<tr>
								<th class="acenter">금융상품 유형, 내용에 대한 설명자료</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<h3>3. 교육이수관련 서류</h3>
				<div id="table08">
					<table class="view_table">
						<colgroup>
							<col width="38%">
							<col width="62%">
						</colgroup>
						<tbody>
							<tr>
								<th class="acenter">대표 교육과정 이수확인서 (경력)</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
							<tr>
								<th class="acenter">인증서(신규)</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
							<tr>
								<th class="acenter">대표 경력증명서</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<h3>4. 업무수행기준요건관련 서류</h3>
				<div id="table09">
					<table class="view_table">
						<colgroup>
							<col width="38%">
							<col width="62%">
						</colgroup>
						<tbody>
							<tr>
								<th class="acenter">업무수행기준요건관련 서류</th>
								<td>
									<input type="text" class="w50 file_input" value="" readonly="" disabled="">
									<a href="#" class="btn_gray btn_del mgl10">삭제</a>
									<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>

				<div class="btn_wrap02">
					<div class="right">
						<a href="#" class="btn_Lgray btn_middle">삭제</a>
					</div>
				</div>
			</div>
			
			<div class="btn_wrap">
				<a href="javascript:void(0);" class="btn_gray" onclick="">목록</a>
			</div>
		</div>
	</form>
</div>

