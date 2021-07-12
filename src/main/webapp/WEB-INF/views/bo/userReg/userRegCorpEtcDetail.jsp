<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//승인요청상태이면 수정 불가
	var plStat = "${result.userRegInfo.plStat}";
	if(plStat == "2"){
		$(".goFileDel").remove();
		$(".inputFile").each(function(){
			$(this).parent("td").html("-");
		});
	}
}

//수정
function goUserRegInfoUpdt() {
	if(confirm("저장하시겠습니까?")){
		goFileTypeListDisabled();
		
		var p = {
			  name 		: "userRegInfoUpdFrm"
			, success 	: function (opt,result) {
				goTab("5");
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

	<div class="tap_wrap">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="fileGrpSeq" value="${result.userRegInfo.fileSeq }"/>
		
		<div class="contents">
			<div class="data_wrap">
				<h3>1. 물적설비관련 서류</h3>
				<div id="table1">
					<table class="view_table">
						<colgroup>
							<col width="38%"/>
							<col width="62%"/>
						</colgroup>
						<tbody>
							<tr>
								<th class="acenter">물적 설비내역에 대한 증빙서류 *</th>
								<td>
									<c:choose>
										<c:when test="${result.userRegInfo.fileType21 ne null }">
											<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType21.fileSeq }">${result.userRegInfo.fileType21.fileFullNm }</a>
											<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType21.fileSeq }" data-fileType="21" data-essential="Y" data-realDel="Y">삭제</a>
										</c:when>
										<c:otherwise>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="21"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<th class="acenter">사무공간 / 전산설비 등의 임차계약서 사본 *</th>
								<td>
									<c:choose>
										<c:when test="${result.userRegInfo.fileType22 ne null }">
											<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType22.fileSeq }">${result.userRegInfo.fileType22.fileFullNm }</a>
											<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType22.fileSeq }" data-fileType="22" data-essential="Y" data-realDel="Y">삭제</a>
										</c:when>
										<c:otherwise>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="22"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<th class="acenter">부동산 등기부등본 *</th>
								<td>
									<c:choose>
										<c:when test="${result.userRegInfo.fileType23 ne null }">
											<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType23.fileSeq }">${result.userRegInfo.fileType23.fileFullNm }</a>
											<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType23.fileSeq }" data-fileType="23" data-essential="Y" data-realDel="Y">삭제</a>
										</c:when>
										<c:otherwise>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="23"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<h3>2. 사회적 신용</h3>
				<div id="table2">
					<table class="view_table">
						<colgroup>
							<col width="38%">
							<col width="62%">
						</colgroup>
						<tbody>
							<%-- 
							<tr>
								<th class="acenter">신청인의 사회적신용에 대한 결격사유없음 확인서 *</th>
								<td>
									<c:choose>
										<c:when test="${result.userRegInfo.fileType24 ne null }">
											<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType24.fileSeq }">${result.userRegInfo.fileType24.fileFullNm }</a>
											<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType24.fileSeq }" data-fileType="24" data-essential="Y" data-realDel="Y">삭제</a>
										</c:when>
										<c:otherwise>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="24"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							 --%>
							<tr>
								<th class="acenter">기업 신용정보조회서 *</th>
								<td>
									<c:choose>
										<c:when test="${result.userRegInfo.fileType29 ne null }">
											<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType29.fileSeq }">${result.userRegInfo.fileType29.fileFullNm }</a>
											<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType29.fileSeq }" data-fileType="29" data-essential="Y" data-realDel="Y">삭제</a>
										</c:when>
										<c:otherwise>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="29"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<h3>3. 기타</h3>
				<div id="table3">
					<table class="view_table">
						<colgroup>
							<col width="38%">
							<col width="62%">
						</colgroup>
						<tbody>
							<tr>
								<th class="acenter">대리인 신청 위임장 *</th>
								<td>
									<c:choose>
										<c:when test="${result.userRegInfo.fileType25 ne null }">
											<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType25.fileSeq }">${result.userRegInfo.fileType25.fileFullNm }</a>
											<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType25.fileSeq }" data-fileType="25" data-essential="Y" data-realDel="Y">삭제</a>
										</c:when>
										<c:otherwise>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="25"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<th class="acenter">위임인 인감증명서 *</th>
								<td>
									<c:choose>
										<c:when test="${result.userRegInfo.fileType26 ne null }">
											<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType26.fileSeq }">${result.userRegInfo.fileType26.fileFullNm }</a>
											<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType26.fileSeq }" data-fileType="26" data-essential="Y" data-realDel="Y">삭제</a>
										</c:when>
										<c:otherwise>
											<input type="text" class="w50 file_input" readonly disabled>
											<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
											<input type="hidden" name="fileTypeList" value="26"/>
											<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
	
			<div class="btn_wrap">
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
				<c:if test="${result.userRegInfo.plStat ne '2' }"> 
					<!-- 승인요청상태가 아닐 때만 수정/삭제 가능 -->
					<a href="javascript:void(0);" class="btn_blue btn_right" onclick="goUserRegInfoUpdt();">저장</a>
				</c:if>
			</div>
		</div>
	</form>
</div>

