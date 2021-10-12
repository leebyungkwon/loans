<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/static/js/newUserReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
}

</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록신청 확인 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="fileGrpSeq" value="${result.userRegInfo.fileSeq }"/>
		
		<div class="contents">
			<div class="data_wrap">
				<h3>첨부서류</h3>
				<div id="table1">
					<table class="view_table">
						<colgroup>
							<col width="38%"/>
							<col width="62%"/>
						</colgroup>
						<tbody>
							<tr>
								<th class="acenter">물적설비 증빙서류 *</th>
								<td>
									<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType21.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType21.fileSeq }" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="21"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType21.fileSeq }">다운로드</a>
								</td>
							</tr>
							<tr>
								<th class="acenter">사무공간 배치현황 *</th>
								<td>
									<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType22.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType22.fileSeq }" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="22"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType22.fileSeq }">다운로드</a>
								</td>
							</tr>
							<tr>
								<th class="acenter">고정사업장 증빙서류 *</th>
								<td>
									<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType23.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType23.fileSeq }" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="23"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType23.fileSeq }">다운로드</a>
								</td>
							</tr>
							
							<tr>
								<th class="acenter">결격사유 없음 확인서 *</th>
								<td>
									<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType41.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType41.fileSeq }" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="41"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType41.fileSeq }">다운로드</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
	
			<div class="btn_wrap">
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
			</div>
		</div>
	</form>
</div>

