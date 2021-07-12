<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//대표자 및 임원관련 사항 엑셀 업로드
	$("#userRegImwonFile").on("change", function () {
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
			  name 		: "userRegCorpImwonInfoInsertFrm"
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
					goTab3("2");
				}
	 	    }
		}
		AjaxUtil.files(p);	
	});
	
	//datepicker
	goDatepickerDraw();
}

//수정
function goCorpImwonInfoUpdt(excSeq) {
	if(confirm("저장하시겠습니까?")){
		goFileTypeListDisabled();
		
		var formNm = "userRegInfoUpdFrm"+excSeq;
		var p = {
			  name 		: formNm
			, success 	: function (opt,result) {
				goTab3("2");
	 	    }
		}
		AjaxUtil.files(p);
	}
}

//삭제 -> 일단 delete로(2021.05.12)
function goCorpImwonInfoDel(excSeq) {
	if(confirm("정말 삭제하시겠습니까?")){
		var p = {
			  url		: "/member/user/deleteUserRegCorpImwonInfo"	
			, param		: {
				 masterSeq 	: "${result.userRegInfo.masterSeq }"
				,excSeq 	: excSeq  
			}
			, success 	: function (opt,result) {
				goTab3("2");
		    }
		}
		AjaxUtil.post(p);
	}
}

//추가
function goDataAreaAdd() {
	var callUrl = "/member/user/callUserRegCorpImwonForm?masterSeq="+"${result.userRegInfo.masterSeq }";
	var formUrl	= "/member/user/insertUserRegCorpImwonInfo";
	
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
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('1');">등록정보</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab3('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab3('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab3('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div id="file_table" class="mgt30">
		<form name="userRegCorpImwonInfoInsertFrm" id="userRegCorpImwonInfoInsertFrm" action="/member/user/insertUserRegCorpImwonInfoByExcel" method="post" enctype="multipart/form-data">
			<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
			<input type="hidden" name="plProduct" value="${result.userRegInfo.plProduct }"/>
			
			<table class="view_table">
				<tbody>
					<tr>
						<td class="pdr0">
							<input type="text" class="top_file_input file_input" readonly disabled>
							<input type="file" name="files" id="userRegImwonFile" style="display: none;"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegImwonFile').click();">파일찾기</a>
							<a href="/static/sample/모집인등록_대표자및임원_샘플.xlsx" download class="btn_Lgray btn_small mgl5" id="">샘플 다운로드</a>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div class="btn_wrap02" style="height: auto;">
		<p style="font: 500 14px/20px '맑은 고딕','dotum'; color: #222222; letter-spacing: -0.8px; text-align: left; margin: 20px 0px 0px 40px;">
			※ 모집인 등록 엑셀 서식 작성요령<br />
			- 구분 : 신규 = 1 / 경력 = 2<br />
			- 주민등록번호, 경력시작일, 경력종료일 입력 시 중간에 “-” 입력<br />
			- 상근여부 : 상근 = 1 / 비상근 = 2 <br />
			- 전문인력여부 : 전문인력 = 1 / 비전문인력 = 2
		</p>
		<div class="right" style="top: 74px;">
			<a href="javascript:void(0);" class="btn_gray btn_middle mgr5" onclick="goDataAreaAdd();">추가</a>
		</div>
	</div>
		
	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.imwonList) > 0 }">
				<c:forEach var="corpImwonList" items="${result.imwonList }" varStatus="status">
					<form name="userRegInfoUpdFrm${corpImwonList.excSeq }" action="/member/user/updateUserRegCorpImwonInfo" method="post" enctype="multipart/form-data">
						<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
						<input type="hidden" name="excSeq" value="${corpImwonList.excSeq }"/>
						<input type="hidden" name="fileGrpSeq" value="${corpImwonList.fileSeq }"/>
						
						<div class="data_wrap">
							<h3>기본정보</h3>
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
											<td colspan="3">${corpImwonList.careerTypNm }</td>
										</tr>
										<tr>
											<th>이름</th>
											<td><input type="text" name="excName" value="${corpImwonList.excName }" class="w100" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
											<th>주민번호</th>
											<td><input type="text" name="plMZId" value="${corpImwonList.plMZId }" class="w100" maxlength="14" placeholder="- 포함" data-vd='{"type":"pId","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
										</tr>
										<tr>
											<th>직위</th>
											<td><input type="text" name="positionNm" value="${corpImwonList.positionNm }" class="w100" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위를 입력해 주세요."}'></td>
											<th>금융상품유형</th>
											<td>${result.userRegInfo.plProductNm }</td>
										</tr>
										<tr>
											<th>교육이수번호/인증서번호</th>
											<td colspan="3">${corpImwonList.plEduNo }</td>
										</tr>
										<tr>
											<th>경력시작일</th>
											<td>
												<input type="text" name="careerStartDate" onclick="goDatepickerShow(this);" readonly="readonly" value="${corpImwonList.careerStartDate }" class="w100" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력시작일(- 포함)을 입력해 주세요."}'>
												<div id="date_cal01" class="calendar01"></div>
											</td>
											<th>경력종료일</th>
											<td>
												<input type="text" name="careerEndDate" onclick="goDatepickerShow(this);" readonly="readonly" value="${corpImwonList.careerEndDate }" class="w100" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력종료일(- 포함)을 입력해 주세요."}'>
												<div id="date_cal02" class="calendar01"></div>
											</td>
										</tr>
										<tr>
											<th>상근여부</th>
											<td>
												<select name="fullTmStat">
													<c:forEach var="fullTmStatList" items="${result.fullTmStatList }">
														<option value="${fullTmStatList.codeDtlCd }" <c:if test="${corpImwonList.fullTmStat eq fullTmStatList.codeDtlCd }">selected="selected"</c:if>>${fullTmStatList.codeDtlNm }</option>
													</c:forEach>
												</select>
											</td>
											<th>전문인력여부</th>
											<td>
												<select name="expertStat">
													<c:forEach var="expertStatList" items="${result.expertStatList }">
														<option value="${expertStatList.codeDtlCd }" <c:if test="${corpImwonList.expertStat eq expertStatList.codeDtlCd }">selected="selected"</c:if>>${expertStatList.codeDtlNm }</option>
													</c:forEach>
												</select>
											</td>
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
											<th class="acenter">대표자 이력서 *</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType7 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType7.fileSeq }">${corpImwonList.fileType7.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType7.fileSeq }" data-fileType="7" data-essential="Y">삭제</a>
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
											<th class="acenter">대표자 경력증명서 *</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType8 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType8.fileSeq }">${corpImwonList.fileType8.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType8.fileSeq }" data-fileType="8" data-essential="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="8"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<%-- 
										<tr>
											<th class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류 *</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType9 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType9.fileSeq }">${corpImwonList.fileType9.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType9.fileSeq }" data-fileType="9" data-essential="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="9"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										 --%>
										<tr>
											<th class="acenter">인감증명서</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType10 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType10.fileSeq }">${corpImwonList.fileType10.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType10.fileSeq }" data-fileType="10" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="10"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="10" data-essential="N">초기화</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">주민등록사본 *</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType30 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType30.fileSeq }">${corpImwonList.fileType30.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType30.fileSeq }" data-fileType="30" data-essential="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="30"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">결격요건 확인서 등 관련서류</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType27 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType27.fileSeq }">${corpImwonList.fileType27.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType27.fileSeq }" data-fileType="27" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="27"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="27" data-essential="N">초기화</a>
													</c:otherwise>
												</c:choose>
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
											<th class="acenter">위탁계약서</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType11 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType11.fileSeq }">${corpImwonList.fileType11.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType11.fileSeq }" data-fileType="11" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="11"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="11" data-essential="N">초기화</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType28 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType28.fileSeq }">${corpImwonList.fileType28.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType28.fileSeq }" data-fileType="28" data-essential="N">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="28"/>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="28" data-essential="N">초기화</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<h3>3. 교육이수관련 서류</h3>
							<div id="table08">
								<table class="view_table eduFileTable">
									<colgroup>
										<col width="38%">
										<col width="62%">
									</colgroup>
									<tbody>
										<c:if test="${corpImwonList.careerTyp eq '1' }">
											<tr class="careerTypOneTr" data-fileType="13" data-fileSeq="${corpImwonList.fileType13.fileSeq }">
												<th class="acenter">대표 인증서(신규) *</th>
												<td>
													<c:choose>
														<c:when test="${corpImwonList.fileType13 ne null }">
															<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType13.fileSeq }">${corpImwonList.fileType13.fileFullNm }</a>
															<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType13.fileSeq }" data-fileType="13" data-essential="Y">삭제</a>
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
										</c:if>
										<c:if test="${corpImwonList.careerTyp eq '2' }">
											<tr class="careerTypTwoTr" data-fileType="12" data-fileSeq="${corpImwonList.fileType12.fileSeq }">
												<th class="acenter">대표 경력교육과정 수료증 *</th>
												<td>
													<c:choose>
														<c:when test="${corpImwonList.fileType12 ne null }">
															<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType12.fileSeq }">${corpImwonList.fileType12.fileFullNm }</a>
															<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType12.fileSeq }" data-fileType="12" data-essential="Y">삭제</a>
														</c:when>
														<c:otherwise>
															<input type="text" class="w50 file_input" readonly disabled>
															<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
															<input type="hidden" name="fileTypeList" value="12"/>
															<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:if>
										<tr>
											<th class="acenter">대표 경력증명서 *</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType14 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType14.fileSeq }">${corpImwonList.fileType14.fileFullNm }</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${corpImwonList.fileType14.fileSeq }" data-fileType="14" data-essential="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<input type="text" class="w50 file_input" readonly disabled>
														<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
														<input type="hidden" name="fileTypeList" value="14"/>
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
									<a href="javascript:void(0);" class="btn_blue btn_middle mgr5" onclick="goCorpImwonInfoUpdt('${corpImwonList.excSeq }');">저장</a>
									<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpImwonInfoDel('${corpImwonList.excSeq }');">삭제</a>
								</div>
							</div>
						</div>
					</form>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<form name="userRegInfoInsertFrm1" action="/member/user/insertUserRegCorpImwonInfo" method="post" enctype="multipart/form-data">
					<jsp:include page="/WEB-INF/views/include/userRegCorpImwon.jsp"></jsp:include>
				</form>
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
		</div>
	</div>
</div>

