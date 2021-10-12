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
			<li><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.imwonList) > 0 }">
				<c:forEach var="corpImwonList" items="${result.imwonList }" varStatus="status">
					<form name="userRegInfoUpdFrm${corpImwonList.excSeq }" action="/member/newUser/newUpdateUserRegCorpImwonInfo" method="post" enctype="multipart/form-data">
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
											<td>${corpImwonList.excName }</td>
											<th>주민번호</th>
											<td>${corpImwonList.plMZId }</td>
										</tr>
										<tr>
											<th>직위</th>
											<td>${corpImwonList.positionNm }</td>
											<th>금융상품유형</th>
											<td>${result.userRegInfo.plProductNm }</td>
										</tr>
										<tr>
											<th>교육이수번호/인증서번호</th>
											<td colspan="3">${corpImwonList.plEduNo }</td>
										</tr>
										<tr>
											<th>경력시작일</th>
											<td>${corpImwonList.careerStartDate }</td>
											<th>경력종료일</th>
											<td>${corpImwonList.careerEndDate }</td>
										</tr>
										<tr>
											<th>상근여부</th>
											<td>${corpImwonList.fullTmStatNm }</td>
											<th>전문인력여부</th>
											<td>${corpImwonList.expertStatNm }</td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<h3>첨부서류</h3>
							<div id="table06">
								<table class="view_table">
									<colgroup>
										<col width="38%">
										<col width="62%">
									</colgroup>
									<tbody>
										<tr>
											<th class="acenter">이력서 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpImwonList.fileType7.fileFullNm }" data-fileSeq="${corpImwonList.fileType7.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="7"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType7.fileSeq }">다운로드</a>
											</td>
										</tr>
										
										<tr>
											<th class="acenter">개인정보필수동의서 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpImwonList.fileType34.fileFullNm }" data-fileSeq="${corpImwonList.fileType34.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="34"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType34.fileSeq }">다운로드</a>
											</td>
										</tr>
										
										<c:if test="${corpImwonList.careerTyp eq '1' }">
											<tr>
												<th class="acenter">인증서</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpImwonList.fileType13.fileFullNm }" data-fileSeq="${corpImwonList.fileType13.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="13"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType13.fileSeq }">다운로드</a>
												</td>
											</tr>
										</c:if>
										
										
										<c:if test="${corpImwonList.careerTyp eq '2' }">
											<tr>
												<th class="acenter">수료증</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpImwonList.fileType12.fileFullNm }" data-fileSeq="${corpImwonList.fileType12.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="12"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType12.fileSeq }">다운로드</a>
												</td>
											</tr>
											
											<tr>
												<th class="acenter">경력증명서</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpImwonList.fileType8.fileFullNm }" data-fileSeq="${corpImwonList.fileType8.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="8"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType8.fileSeq }">다운로드</a>
												</td>
											</tr>
											<tr>
												<th class="acenter">위탁계약서</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpImwonList.fileType11.fileFullNm }" data-fileSeq="${corpImwonList.fileType11.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="11"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType11.fileSeq }">다운로드</a>
												</td>
											</tr>
											<tr>
												<th class="acenter">위탁 금융상품직접판매업자 확인서</th>
												<td>
													<input type="text" class="w50 file_input" value="${corpImwonList.fileType28.fileFullNm }" data-fileSeq="${corpImwonList.fileType28.fileSeq }" readonly disabled>
													<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
													<input type="hidden" name="fileTypeList" value="28"/>
													<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType28.fileSeq }">다운로드</a>
												</td>
											</tr>
											
										</c:if>

<%-- 										<tr>
											<th class="acenter">인감증명서</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpImwonList.fileType10.fileFullNm }" data-fileSeq="${corpImwonList.fileType10.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="10"/>
												<c:choose>
													<c:when test="${corpImwonList.fileType10 ne null }">
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType10.fileSeq }">다운로드</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${corpImwonList.fileType10.fileSeq }" data-fileType="10" data-essential="N" data-realDel="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="10" data-essential="N">초기화</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr> --%>
										<tr>
											<th class="acenter">결격사유 없음 확인서</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpImwonList.fileType27.fileFullNm }" data-fileSeq="${corpImwonList.fileType27.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="27"/>
												<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType27.fileSeq }">다운로드</a>
											</td>
										</tr>
										<%-- 
										<tr>
											<th class="acenter">행정정보공동이용 사전동의서 *</th>
											<td>
												<input type="text" class="w50 file_input" value="${corpImwonList.fileType33.fileFullNm }" data-fileSeq="${corpImwonList.fileType33.fileSeq }" readonly disabled>
												<input type="file" name="files" class="inputFile pdfOnly" data-essential="Y" style="display: none;"/>
												<input type="hidden" name="fileTypeList" value="33"/>
												<c:choose>
													<c:when test="${corpImwonList.fileType33 ne null }">
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${corpImwonList.fileType33.fileSeq }">다운로드</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileDel" data-fileSeq="${corpImwonList.fileType33.fileSeq }" data-fileType="33" data-essential="Y" data-pdfOnly="Y" data-realDel="Y">삭제</a>
													</c:when>
													<c:otherwise>
														<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
														<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="33" data-essential="Y" data-pdfOnly="Y">초기화</a>
													</c:otherwise>
												</c:choose>
											</td>
										</tr> --%>

										
										
									</tbody>
								</table>
							</div>
						</div>
					</form>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<h2>
					데이터가 없습니다.
				</h2>
				
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
		</div>
	</div>
</div>

