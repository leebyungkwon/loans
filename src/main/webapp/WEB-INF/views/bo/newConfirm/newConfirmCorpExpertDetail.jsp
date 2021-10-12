<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newUserReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	goDatepickerDraw();
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
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('2');">대표자 및 임원관련<br />사항</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab2('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.expertList) > 0 }">
				<c:forEach var="corpExpertList" items="${result.expertList }" varStatus="status">
					<form name="userRegInfoUpdFrm${corpExpertList.expSeq }" action="/member/newUser/newUpdateUserRegCorpExpertInfo" method="post" enctype="multipart/form-data">
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
											<td>${corpExpertList.expName }</td>
											<th>주민번호</th>
											<td>${corpExpertList.plMZId }</td>
										</tr>
										<tr>
											<th>금융상품유형</th>
											<td colspan="3">${result.userRegInfo.plProductNm }</td>
										</tr>
										<tr>
											<th>교육이수번호/인증서번호</th>
											<td colspan="3">${corpExpertList.plEduNo }</td>
										</tr>
										<tr>
											<th>경력시작일</th>
											<td>${corpExpertList.careerStartDate }</td>
											<th>경력종료일</th>
											<td>${corpExpertList.careerEndDate }</td>
										</tr>
									</tbody>
								</table>
							</div>
					
							<h3>업무수행인력 관련 서류</h3>
							<div id="table10 eduFileTable">
								<table class="view_table">
									<colgroup>
										<col width="38%"/>
										<col width="62%"/>
									</colgroup>
									<tbody>
										<c:if test="${corpExpertList.careerTyp eq '1' }">
											<tr>
												<th class="acenter">인증서 *</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpExpertList.fileType17.fileFullNm }" data-fileSeq="${corpExpertList.fileType17.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="17"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpExpertList.fileType17.fileSeq }">다운로드</a>
												</td>
											</tr>
										</c:if>
										<c:if test="${corpExpertList.careerTyp eq '2' }">
											<tr>
												<th class="acenter">교육수료증 *</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpExpertList.fileType16.fileFullNm }" data-fileSeq="${corpExpertList.fileType16.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="16"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpExpertList.fileType16.fileSeq }">다운로드</a>
												</td>
											</tr>
											
											<tr>
												<th class="acenter">경력증명서</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpExpertList.fileType18.fileFullNm }" data-fileSeq="${corpExpertList.fileType18.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="18"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpExpertList.fileType18.fileSeq }">다운로드</a>
												</td>
											</tr>
											
											<tr>
												<th class="acenter">위탁계약서</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpExpertList.fileType39.fileFullNm }" data-fileSeq="${corpExpertList.fileType39.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="39"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpExpertList.fileType39.fileSeq }">다운로드</a>
												</td>
											</tr>
											<tr>
												<th class="acenter">위탁 금융상품직접판매업자 확인서</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpExpertList.fileType40.fileFullNm }" data-fileSeq="${corpExpertList.fileType40.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="40"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpExpertList.fileType40.fileSeq }">다운로드</a>
												</td>
											</tr>
										</c:if>

										<tr>
											<th class="acenter">상근입증서류 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpExpertList.fileType35.fileFullNm }" data-fileSeq="${corpExpertList.fileType35.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="35"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpExpertList.fileType35.fileSeq }">다운로드</a>
											</td>
										</tr>
										<tr>
											<th class="acenter">개인정보필수동의서 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpExpertList.fileType36.fileFullNm }" data-fileSeq="${corpExpertList.fileType36.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="36"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpExpertList.fileType36.fileSeq }">다운로드</a>
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

