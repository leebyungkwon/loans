<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="data_wrap">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
	<input type="hidden" name="fileGrpSeq" value="${result.userRegInfo.fileSeq }"/>

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
					<td><input type="text" name="excName" class="w100"></td>
					<th>주민번호</th>
					<td><input type="text" name="plMZId" class="w100"></td>
				</tr>
				<tr>
					<th>직위</th>
					<td><input type="text" name="positionNm" class="w100"></td>
					<th>금융상품유형</th>
					<td>${result.userRegInfo.plProductNm }</td>
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
				<tr>
					<th>상근여부</th>
					<td>
						<select name="fullTmStat">
							<c:forEach var="fullTmStatList" items="${result.fullTmStatList }">
								<option value="${fullTmStatList.codeDtlCd }">${fullTmStatList.codeDtlNm }</option>
							</c:forEach>
						</select>
					</td>
					<th>전문인력여부</th>
					<td><input type="text" class="w100" value="비상근">
						<select name="expertStat">
							<c:forEach var="expertStatList" items="${result.expertStatList }">
								<option value="${expertStatList.codeDtlCd }">${expertStatList.codeDtlNm }</option>
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
					<th class="acenter">대표자 이력서</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="7"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">대표자 경력증명서</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="8"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="9"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">인감증명서</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="10"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
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
					<th class="acenter">금융상품 유형, 내용에 대한 설명자료</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="11"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<h3>3. 교육이수관련 서류</h3>
	<div id="table08">
		<table class="view_table">
			<colgroup>
				<col width="38%">
				<col width="62%">
			</colgroup>
			<tbody>
				<tr>
					<th class="acenter">대표 교육과정 이수확인서 (경력)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="12"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">인증서(신규)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="13"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">대표 경력증명서</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="14"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<h3>4. 업무수행기준요건관련 서류</h3>
	<div id="table09">
		<table class="view_table">
			<colgroup>
				<col width="38%">
				<col width="62%">
			</colgroup>
			<tbody>
				<tr>
					<th class="acenter">업무수행기준요건관련 서류</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="15"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_wrap02">
		<div class="right">
			<a href="javascript:void(0);" class="btn_blue btn_middle mgr5" onclick="goCorpInfoReg(this);">저장</a>
			<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpInfoRemove(this);">삭제</a>
		</div>
	</div>
</div>