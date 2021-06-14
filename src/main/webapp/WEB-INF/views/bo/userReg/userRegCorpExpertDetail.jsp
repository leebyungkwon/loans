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
			$(this).parent("td").html("-");
		});
		
		//임시
		$("#file_table").remove();
		$(".btn_wrap02").remove();
		$(".data_wrap").addClass("mgt30");
	}
	
	//전문인력 엑셀 업로드
	$("#userRegExpertFile").on("change", function () {
		var fileVal 	= $(this).val().split("\\");
		var fileName 	= fileVal[fileVal.length - 1];
		$(this).prev().val(fileName);
		
		//첨부파일 확장자 체크
		var ext 	= fileName.split(".").pop().toLowerCase();
		if(!Valid.fileCheck(ext,"Y")){
			$(this).val("");
			$(this).prev().val("");
			return;
		}
		var p = {
			  name 		: "userRegCorpExpertInfoInsertFrm"
			, success 	: function (opt,result) {
				if(WebUtil.isNotNull(result.data)){
					
					var html = '';
					
					html += '<div class="title_wrap">';
					html += '<h5>전문인력 등록</h5>';
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
					goTab("3");
				}
	 	    }
		}
		AjaxUtil.files(p);	
	});
	
	//datepicker
	goDatepickerDraw();
}

//수정
function goCorpExpertInfoUpdt(expSeq) {
	if(confirm("저장하시겠습니까?")){
		goFileTypeListDisabled();
		
		var formNm = "userRegInfoUpdFrm"+expSeq;
		var p = {
			  name 		: formNm
			, success 	: function (opt,result) {
				goTab("3");
	 	    }
		}
		AjaxUtil.files(p);
	}
}

//삭제 -> 일단 delete로(2021.05.12)
function goCorpExpertInfoDel(expSeq) {
	if(confirm("정말 삭제하시겠습니까?")){
		var p = {
			  url		: "/member/user/deleteUserRegCorpExpertInfo"	
			, param		: {
				 masterSeq 	: "${result.userRegInfo.masterSeq }"
				,expSeq 	: expSeq  
			}
			, success 	: function (opt,result) {
				goTab("3");
		    }
		}
		AjaxUtil.post(p);
	}
}

//추가
function goDataAreaAdd() {
	var callUrl = "/member/user/callUserRegCorpExpertForm?masterSeq="+"${result.userRegInfo.masterSeq }";
	var formUrl	= "/member/user/insertUserRegCorpExpertInfo";
	
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
			<li class="on"><a href="javascript:void(0);" onclick="goTab('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div id="file_table" class="mgt30">
		<form name="userRegCorpExpertInfoInsertFrm" id="userRegCorpExpertInfoInsertFrm" action="/member/user/insertUserRegCorpExpertInfoByExcel" method="post" enctype="multipart/form-data">
			<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
			<input type="hidden" name="plProduct" value="${result.userRegInfo.plProduct }"/>
			
			<table class="view_table">
				<tbody>
					<tr>
						<td class="pdr0">
							<input type="text" class="top_file_input file_input" readonly disabled>
							<input type="file" name="files" id="userRegExpertFile" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegExpertFile').click();">파일찾기</a>
							<a href="/static/sample/모집인등록_전문성인력_샘플.xlsx" download class="btn_Lgray btn_small mgl5" id="">샘플 다운로드</a>
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
			<c:when test="${fn:length(result.expertList) > 0 }">
				<c:forEach var="corpExpertList" items="${result.expertList }" varStatus="status">
					<form name="userRegInfoUpdFrm${corpExpertList.expSeq }" action="/member/user/updateUserRegCorpExpertInfo" method="post" enctype="multipart/form-data">
						<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
						<input type="hidden" name="expSeq" value="${corpExpertList.expSeq }"/>
						<input type="hidden" name="fileGrpSeq" value="${corpExpertList.fileSeq }"/>
				
						<div class="data_wrap">
							<h3>기본정보</h3>
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
											<td><input type="text" name="expName" value="${corpExpertList.expName }" class="w100" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
											<th>주민번호</th>
											<td><input type="text" name="plMZId" value="${corpExpertList.plMZId }" class="w100" maxlength="14" placeholder="- 포함" data-vd='{"type":"pId","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
										</tr>
										<tr>
											<th>금융상품유형</th>
											<td colspan="3">${result.userRegInfo.plProductNm }</td>
										</tr>
										<tr>
											<th>교육이수번호</th>
											<td colspan="3">${corpExpertList.plEduNo }</td>
										</tr>
										<tr>
											<th>경력시작일</th>
											<td>
												<input type="text" name="careerStartDate" onclick="goDatepickerShow(this);" value="${corpExpertList.careerStartDate }" readonly="readonly" class="w100" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력시작일(- 포함)을 입력해 주세요."}'>
												<div id="date_cal01" class="calendar01"></div>
											</td>
											<th>경력종료일</th>
											<td>
												<input type="text" name="careerEndDate" onclick="goDatepickerShow(this);" value="${corpExpertList.careerEndDate }" readonly="readonly" class="w100" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력종료일(- 포함)을 입력해 주세요."}'>
												<div id="date_cal02" class="calendar01"></div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
					
							<h3>전문인력관련 서류</h3>
							<div id="table10 eduFileTable">
								<table class="view_table">
									<colgroup>
										<col width="38%"/>
										<col width="62%"/>
									</colgroup>
									<tbody>
										<c:if test="${corpExpertList.careerTyp eq '1' }">
											<tr class="careerTypOneTr" data-fileType="17" data-fileSeq="${corpExpertList.fileType17.fileSeq }">
												<th class="acenter">인증서(신규) *</th>
												<td>
													<c:choose>
														<c:when test="${corpExpertList.fileType17 ne null }">
															<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType17.fileSeq }">${corpExpertList.fileType17.fileFullNm }</a>
															<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpExpertList.fileType17.fileSeq }" data-fileType="17" data-essential="Y" data-realDel="Y">삭제</a>
														</c:when>
														<c:otherwise>
															<input type="text" class="w50 file_input" readonly disabled>
															<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
															<input type="hidden" name="fileTypeList" value="17"/>
															<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:if>
										<c:if test="${corpExpertList.careerTyp eq '2' }">
											<tr class="careerTypTwoTr" data-fileType="16" data-fileSeq="${corpExpertList.fileType16.fileSeq }">
												<th class="acenter">경력교육과정 수료증 *</th>
												<td>
													<c:choose>
														<c:when test="${corpExpertList.fileType16 ne null }">
															<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType16.fileSeq }">${corpExpertList.fileType16.fileFullNm }</a>
															<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpExpertList.fileType16.fileSeq }" data-fileType="16" data-essential="Y" data-realDel="Y">삭제</a>
														</c:when>
														<c:otherwise>
															<input type="text" class="w50 file_input" readonly disabled>
															<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
															<input type="hidden" name="fileTypeList" value="16"/>
															<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:if>
										<tr>
											<th class="acenter">경력증명서(업무인력) *</th>
											<td>
												<c:choose>
													<c:when test="${corpExpertList.fileType18 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType18.fileSeq }">${corpExpertList.fileType18.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpExpertList.fileType18.fileSeq }" data-fileType="18" data-essential="Y" data-realDel="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
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
									<a href="javascript:void(0);" class="btn_blue btn_middle mgr5" onclick="goCorpExpertInfoUpdt('${corpExpertList.expSeq }');">저장</a>
									<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpExpertInfoDel('${corpExpertList.expSeq }');">삭제</a>
								</div>
							</div>
						</div>
					</form>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<form name="userRegInfoInsertFrm1" action="/member/user/insertUserRegCorpExpertInfo" method="post" enctype="multipart/form-data">
					<jsp:include page="/WEB-INF/views/include/userRegCorpExpert.jsp"></jsp:include>
				</form>
			</c:otherwise>
		</c:choose>

		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
		</div>
	</div>
</div>

