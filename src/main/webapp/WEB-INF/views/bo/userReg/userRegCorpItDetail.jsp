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
		$(".goFileDel").remove();
		$(".inputFile").each(function(){
			$(this).parent("td").html("-");
		});
		
		//임시
		$("#file_table").remove();
		$(".btn_wrap02").remove();
		$(".data_wrap").addClass("mgt30");
	}
	
	//전산인력 엑셀 업로드
	$("#userRegItFile").on("change", function () {
		var fileVal 	= $(this).val().split("\\");
		var fileName 	= fileVal[fileVal.length - 1];
		$(this).prev().val(fileName);
		
		//첨부파일 확장자 체크
		var size 	= $(this)[0].files[0].size;
		var ext 	= fileName.split(".").pop().toLowerCase();
		if(!Valid.fileCheck(size,ext,"Y")){
			$(this).val("");
			$(this).prev().val("");
			return;
		}
		var p = {
			  name 		: "userRegCorpItInfoInsertFrm"
			, success 	: function (opt,result) {
				if(WebUtil.isNotNull(result.data)){
					
					var html = '';
					
					html += '<div class="title_wrap">';
					html += '<h5>전산인력 등록</h5>';
					html += '<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopupByReload();"></a>';
					html += '</div>';
					html += '<p class="popup_desc" style="line-height: 30px;">'+result.data+'</p>';
					html += '<div class="popup_btn_wrap">';
					html += '<a href="javascript:void(0);" class="pop_btn_black" onclick="PopUtil.closePopupByReload();">확인</a>';
					html += '</div>';
					
					$(".popup_inner").empty();
					$(".popup_inner").append(html);
					$(".popup_wrap").show();
					
				}else{
					goTab("4");
				}
	 	    }
		}
		AjaxUtil.files(p);	
	});
}

//수정
function goCorpItInfoUpdt(operSeq) {
	if(confirm("저장하시겠습니까?")){
		goFileTypeListDisabled();
		
		var formNm = "userRegInfoUpdFrm"+operSeq;
		var p = {
			  name 		: formNm
			, success 	: function (opt,result) {
				goTab("4");
	 	    }
		}
		AjaxUtil.files(p);
	}
}

//삭제 -> 일단 delete로(2021.05.12)
function goCorpItInfoDel(operSeq) {
	if(confirm("정말 삭제하시겠습니까?")){
		var p = {
			  url		: "/member/user/deleteUserRegCorpItInfo"	
			, param		: {
				 masterSeq 	: "${result.userRegInfo.masterSeq }"
				,operSeq 	: operSeq  
			}
			, success 	: function (opt,result) {
				goTab("4");
		    }
		}
		AjaxUtil.post(p);
	}
}

//추가
function goDataAreaAdd() {
	var callUrl = "/member/user/callUserRegCorpItForm?masterSeq="+"${result.userRegInfo.masterSeq }";
	var formUrl	= "/member/user/insertUserRegCorpItInfo";
	
	goHtmlAdd(callUrl,formUrl,$(".data_wrap").length);
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
							<input type="file" name="files" id="userRegItFile" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegItFile').click();">파일찾기</a>
							<a href="/static/sample/모집인등록_전산설비_샘플.xlsx" download class="btn_Lgray btn_small mgl5" id="">샘플 다운로드</a>								
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
						<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
						<input type="hidden" name="operSeq" value="${corpItList.operSeq }"/>
						<input type="hidden" name="fileGrpSeq" value="${corpItList.fileSeq }"/>
						
						<div class="data_wrap">
							<h3>기본정보</h3>
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
											<td><input type="text" name="operName" value="${corpItList.operName }" class="w100" maxlength="10" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
											<th>주민번호</th>
											<td><input type="text" name="plMZId" value="${corpItList.plMZId }" class="w100" maxlength="14" placeholder="- 포함" data-vd='{"type":"pId","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
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
											<th class="acenter">경력증명서(전산인력)</th>
											<td>
												<c:choose>
													<c:when test="${corpItList.fileType19 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpItList.fileType19.fileSeq }">${corpItList.fileType19.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpItList.fileType19.fileSeq }" data-fileType="19" data-essential="N" data-realDel="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="19"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="19" data-essential="N">초기화</a>
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
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpItList.fileType20.fileSeq }" data-fileType="20" data-essential="N" data-realDel="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="20"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="20" data-essential="N">초기화</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">아웃소싱 업체 상주직원 관련 증빙서류</th>
											<td>
												<c:choose>
													<c:when test="${corpItList.fileType37 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpItList.fileType37.fileSeq }">${corpItList.fileType37.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpItList.fileType37.fileSeq }" data-fileType="37" data-essential="N" data-realDel="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="37"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="37" data-essential="N">초기화</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">개인정보필수동의서 *</th>
											<td>
												<c:choose>
													<c:when test="${corpItList.fileType38 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpItList.fileType38.fileSeq }">${corpItList.fileType38.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpItList.fileType38.fileSeq }" data-fileType="38" data-essential="Y" data-realDel="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="38"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="38" data-essential="Y">초기화</a>
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
				<form name="userRegInfoInsertFrm1" action="/member/user/insertUserRegCorpItInfo" method="post" enctype="multipart/form-data">
					<jsp:include page="/WEB-INF/views/include/userRegCorpIt.jsp"></jsp:include>
				</form>
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
		</div>
	</div>
</div>

