<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="data_wrap">
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
					<td><input type="text" name="expName" class="w100" maxlength="20"></td>
					<th>주민번호</th>
					<td><input type="text" name="plMZId" class="w100" maxlength="14" placeholder="- 포함"></td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.userRegInfo.plProductNm }</td>
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
			</tbody>
		</table>
	</div>

	<h3>전문인력관련 서류</h3>
	<div id="table10">
		<table class="view_table">
			<colgroup>
				<col width="38%"/>
				<col width="62%"/>
			</colgroup>
			<tbody>
				<tr>
					<th class="acenter">이수확인서 (경력)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="16"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">인증서(신규)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="17"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">경력증명서 (업무인력)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="18"/>
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