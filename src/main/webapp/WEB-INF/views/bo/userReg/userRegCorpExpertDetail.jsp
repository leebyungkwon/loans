<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//전문인력 엑셀 업로드
	$("#userRegExpertFile").on("change", function () {
		var p = {
			  name 		: "userRegCorpExpertInfoInsertFrm"
			, success 	: function (opt,result) {
				var msg = result.data;
				
				if(msg == "success"){
					alert("전산인력이 등록되었습니다.");
					location.reload();
				}else if(msg == "fail"){
					alert("실패했습니다.");
					return;
				}else{
					alert("[데이터 확인 필요]\n"+msg);
					location.reload();
				}
	 	    }
		}
		AjaxUtil.files(p);	
	});
}

//수정
function goUserRegInfoUpdt() {
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

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div id="file_table" class="mgt30">
		<form name="userRegCorpExpertInfoInsertFrm" id="userRegCorpExpertInfoInsertFrm" action="/member/user/corpExpertExcelUpload" method="post" enctype="multipart/form-data">
			<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
			
			<table class="view_table">
				<tbody>
					<tr>
						<td class="pdr0">
							<input type="text" class="top_file_input file_input" readonly disabled>
							<input type="file" name="files" id="userRegExpertFile" class="inputFile" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegExpertFile').click();">파일찾기</a>
							<a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goSampleDownload();">샘플 다운로드</a>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div class="btn_wrap02">
		<div class="right">
			<a href="javascript:void(0);" class="btn_blue btn_middle" onclick="">추가</a>
		</div>
	</div>

	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.expertList) > 0 }">
				<c:forEach var="corpExpertList" items="${result.expertList }" varStatus="status">
					<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="" method="post" enctype="multipart/form-data">
						<input type="hidden" name="expSeq" value="${corpExpertList.expSeq }"/>
						<input type="hidden" name="fileGrpSeq" value="${corpExpertList.fileSeq }"/>
				
						<div class="data_wrap">
							<div id="table03">
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
											<td colspan="3">${corpExpertList.careerTypNm }</td>
										</tr>
										<tr>
											<th>이름</th>
											<td><input type="text" name="expName" value="${corpExpertList.expName }" class="w100"></td>
											<th>주민번호</th>
											<td><input type="text" name="plMZId" value="${corpExpertList.expName }" class="w100"></td>
										</tr>
										<tr>
											<th>금융상품유형</th>
											<td colspan="3">${result.userRegInfo.plProductNm }</td>
										</tr>
										<tr>
											<th>교육이수번호</th>
											<td colspan="3"><input type="text" name="plEduNo" value="${corpExpertList.plEduNo }" class="w100"></td>
										</tr>
										<tr>
											<th>경력시작일</th>
											<td><input type="text" name="careerStartDt" value="${corpExpertList.careerStartDt }" class="w100"></td>
											<th>경력종료일</th>
											<td><input type="text" name="careerEndDt" value="${corpExpertList.careerEndDt }" class="w100"></td>
										</tr>
									</tbody>
								</table>
							</div>
					
							<h3>전문인력관련 서류</h3>
							<div id="table10">
								<table class="view_table">
									<colgroup>
										<col width="38%"/>
										<col width="62%"/>
									</colgroup>
									<tbody>
										<tr>
											<th class="acenter">이수확인서 (경력)</th>
											<td>
												<c:choose>
													<c:when test="${corpExpertList.fileType16 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType16.fileSeq }">${corpExpertList.fileType16.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpExpertList.fileType16.fileSeq }" data-fileType="16" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="16"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">인증서(신규)</th>
											<td>
												<c:choose>
													<c:when test="${corpExpertList.fileType17 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType17.fileSeq }">${corpExpertList.fileType17.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpExpertList.fileType17.fileSeq }" data-fileType="17" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="17"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">경력증명서 (업무인력)</th>
											<td>
												<c:choose>
													<c:when test="${corpExpertList.fileType18 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType18.fileSeq }">${corpExpertList.fileType18.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpExpertList.fileType18.fileSeq }" data-fileType="18" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="18"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<div class="btn_wrap02">
								<div class="right">
									<a href="javascript:void(0);" class="btn_blue btn_middle mgr5" onclick="goCorpItInfoUpdt('${corpExpertList.expSeq }');">저장</a>
									<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpItInfoDel('${corpExpertList.expSeq }');">삭제</a>
								</div>
							</div>
						</div>
					</form>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
					
					<div class="data_wrap">
						<div id="table03">
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
										<td colspan="3">
											<select name="careerTyp">
												<c:forEach var="careerTypList" items="${result.careerTypList }">
													<option value="${careerTypList.codeDtlCd }">${careerTypList.codeDtlNm }</option>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr>
										<th>이름</th>
										<td><input type="text" name="expName" class="w100"></td>
										<th>주민번호</th>
										<td><input type="text" name="plMZId" class="w100"></td>
									</tr>
									<tr>
										<th>금융상품유형</th>
										<td colspan="3">${result.userRegInfo.plProductNm }</td>
									</tr>
									<tr>
										<th>교육이수번호</th>
										<td colspan="3"><input type="text" name="plEduNo" class="w100"></td>
									</tr>
									<tr>
										<th>경력시작일</th>
										<td><input type="text" name="careerStartDt" class="w100"></td>
										<th>경력종료일</th>
										<td><input type="text" name="careerEndDt" class="w100"></td>
									</tr>
								</tbody>
							</table>
						</div>
				
						<h3>전문인력관련 서류</h3>
						<div id="table10">
							<table class="view_table">
								<colgroup>
									<col width="38%"/>
									<col width="62%"/>
								</colgroup>
								<tbody>
									<tr>
										<th class="acenter">이수확인서 (경력)</th>
										<td>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="16"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">인증서(신규)</th>
										<td>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="17"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">경력증명서 (업무인력)</th>
										<td>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="18"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<div class="btn_wrap02">
							<div class="right">
								<a href="javascript:void(0);" class="btn_blue btn_middle mgr5" onclick="goCorpItInfoUpdt('');">저장</a>
								<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpItInfoDel('');">삭제</a>
							</div>
						</div>
					</div>
				</form>
			</c:otherwise>
		</c:choose>

		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="">목록</a>
		</div>
	</div>
</div>

