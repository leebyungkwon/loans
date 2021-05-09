<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//전산인력 엑셀 업로드
	$("#userRegItFile").on("change", function () {
		var p = {
			  name 		: "userRegCorpItInfoInsertFrm"
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

//추가
function goDataAreaAdd() {
	alert("영역 추가해야해!");
	
	
	/*
	//초기화,이벤트..
	var html = $(".cloneTarget").clone();
	$("#target").before(html);
	*/
}

//수정
function goCorpItInfoUpdt(operSeq) {
	var formNm = "userRegInfoUpdFrm"+operSeq;
	var p = {
		  name 		: formNm
		, success 	: function (opt,result) {
			if(result.data == "success"){
				alert("저장되었습니다.");
				location.reload();
			}
 	    }
	}
	AjaxUtil.files(p);
}

//삭제
function goCorpItInfoDel(operSeq) {
	if(confirm("정말 삭제하시겠습니까?")){
		alert("삭제해야해!");
	}
}

/*
//등록
function goCorpItReg() {
	var formNm = "userRegInfoUpdFrm"+operSeq;
	var p = {
		  name 		: formNm
		, success 	: function (opt,result) {
			if(result.data > 0){
				alert("저장되었습니다.");
				location.reload();
			}
 	    }
	}
	AjaxUtil.files(p);
}

//영역 삭제
function goCorpItDel(operSeq) {
	alert("추가된 영역 삭제!");
}
*/
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
			<li><a href="javascript:void(0);" onclick="goTab('3');">전문성 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<div id="file_table" class="mgt30">
		<form name="userRegCorpItInfoInsertFrm" id="userRegCorpItInfoInsertFrm" action="/member/user/insertUserRegCorpItInfoByExcel" method="post" enctype="multipart/form-data">
			<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
			
			<table class="view_table">
				<tbody>
					<tr>
						<td class="pdr0">
							<input type="text" class="top_file_input file_input" readonly disabled>
							<input type="file" name="files" id="userRegItFile" class="inputFile" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegItFile').click();">파일찾기</a>
							<a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goSampleDownload();">샘플 다운로드</a>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div class="btn_wrap02">
		<div class="right">
			<a href="javascript:void(0);" class="btn_gray btn_middle" onclick="goDataAreaAdd();">추가</a>
		</div>
	</div>

	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.itList) > 0 }">
				<c:forEach var="corpItList" items="${result.itList }" varStatus="status">
					<form name="userRegInfoUpdFrm${corpItList.operSeq }" action="/member/user/updateUserRegCorpItInfo" method="post" enctype="multipart/form-data">
						<input type="hidden" name="operSeq" value="${corpItList.operSeq }"/>
						<input type="hidden" name="fileGrpSeq" value="${corpItList.fileSeq }"/>
						
						<div class="data_wrap">
							<div id="table">
								<table class="view_table">
									<colgroup>
										<col width="15%">
										<col width="35%">
										<col width="15%">
										<col width="35%">
									</colgroup>
									<tbody>
										<tr>
											<th>이름</th>
											<td><input type="text" name="operName" value="${corpItList.operName }" class="w100"></td>
											<th>주민번호</th>
											<td><input type="text" name="plMZId" value="${corpItList.plMZId }" class="w100"></td>
										</tr>
									</tbody>
								</table>
							</div>
					
							<h3>전산인력관련 서류</h3>
							<div id="table10">
								<table class="view_table">
									<colgroup>
										<col width="38%"/>
										<col width="62%"/>
									</colgroup>
									<tbody>
										<tr>
											<th class="acenter">경력증명서 (전산인력)</th>
											<td>
												<c:choose>
													<c:when test="${corpItList.fileType19 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpItList.fileType19.fileSeq }">${corpItList.fileType19.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpItList.fileType19.fileSeq }" data-fileType="19" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="19"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">자격확인 서류(전산인력)</th>
											<td>
												<c:choose>
													<c:when test="${corpItList.fileType20 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpItList.fileType20.fileSeq }">${corpItList.fileType20.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpItList.fileType20.fileSeq }" data-fileType="20" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="20"/>
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
									<a href="javascript:void(0);" class="btn_blue btn_middle mgr5" onclick="goCorpItInfoUpdt('${corpItList.operSeq }');">저장</a>
									<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpItInfoDel('${corpItList.operSeq }');">삭제</a>
								</div>
							</div>
						</div>
					</form>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<form name="userRegInfoInsertFrm" action="" method="post" enctype="multipart/form-data">
					<div class="data_wrap">
						<div id="table">
							<table class="view_table">
								<colgroup>
									<col width="15%">
									<col width="35%">
									<col width="15%">
									<col width="35%">
								</colgroup>
								<tbody>
									<tr>
										<th>이름</th>
										<td><input type="text" name="operName" class="w100"></td>
										<th>주민번호</th>
										<td><input type="text" name="plMZId" class="w100"></td>
									</tr>
								</tbody>
							</table>
						</div>
					
						<h3>전산인력관련 서류</h3>
						<div id="table10">
							<table class="view_table">
								<colgroup>
									<col width="38%"/>
									<col width="62%"/>
								</colgroup>
								<tbody>
									<tr>
										<th class="acenter">경력증명서 (전산인력)</th>
										<td>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="19"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">자격확인 서류(전산인력)</th>
										<td>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="20"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<div class="btn_wrap02">
							<div class="right">
								<a href="javascript:void(0);" class="btn_blue btn_middle mgr5" onclick="goCorpItReg();">저장</a>
								<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpItDel();">삭제</a>
							</div>
						</div>
					</div>
				</form>
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="">목록</a>
		</div>
	</div>
</div>

