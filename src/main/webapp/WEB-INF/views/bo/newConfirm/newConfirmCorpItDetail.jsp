<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
			<h2>모집인 조회 및 해지 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('3');">업무수행인력<br />관련 사항</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.itList) > 0 }">
				<c:forEach var="corpItList" items="${result.itList }" varStatus="status">
					<form name="userRegInfoUpdFrm${corpItList.operSeq }" action="/member/newUser/newUpdateUserRegCorpItInfo" method="post" enctype="multipart/form-data">
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
											<td>${corpItList.operName }</td>
											<th>주민번호</th>
											<td>${corpItList.plMZId }</td>
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
											<th class="acenter">자격확인 서류 또는 경력증명서 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpItList.fileType19.fileFullNm }" data-fileSeq="${corpItList.fileType19.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="19"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpItList.fileType19.fileSeq }">다운로드</a>
											</td>
										</tr>
										<tr>
											<th class="acenter">상근입증서류 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpItList.fileType20.fileFullNm }" data-fileSeq="${corpItList.fileType20.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="20"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpItList.fileType20.fileSeq }">다운로드</a>
											</td>
										</tr>
										<tr>
											<th class="acenter">아웃소싱업체 상주직원 증빙서류</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpItList.fileType37.fileFullNm }" data-fileSeq="${corpItList.fileType37.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="37"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpItList.fileType37.fileSeq }">다운로드</a>
											</td>
										</tr>
										<tr>
											<th class="acenter">개인정보 필수 동의서 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpItList.fileType38.fileFullNm }" data-fileSeq="${corpItList.fileType38.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="38"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpItList.fileType38.fileSeq }">다운로드</a>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</form>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<h2>데이터가 없습니다.</h2>
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
		</div>
	</div>
</div>

