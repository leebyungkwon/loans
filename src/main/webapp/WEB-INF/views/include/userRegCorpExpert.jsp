<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="data_wrap">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
	<input type="hidden" name="fileGrpSeq" value=""/>

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
					<td colspan="3">
						<select name="careerTyp" class="careerTyp">
							<c:forEach var="careerTypList" items="${result.careerTypList }">
								<option value="${careerTypList.codeDtlCd }">${careerTypList.codeDtlNm }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="expName" class="w100" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
					<th>주민번호</th>
					<td><input type="text" name="plMZId" class="w100" maxlength="14" placeholder="- 포함" data-vd='{"type":"pId","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.userRegInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>교육이수번호/인증서번호</th>
					<td colspan="3"><input type="text" name="plEduNo" class="w100" maxlength="30" data-vd='{"type":"text","len":"10,30","req":true,"msg":"교육이수번호를 입력해 주세요."}'></td>
				</tr>
				<tr>
					<th>경력시작일</th>
					<td>
						<input type="text" name="careerStartDate" onclick="goDatepickerShow(this);" readonly="readonly" class="w100" maxlength="10" placeholder="- 포함">
						<div class="calendar01"></div>
					</td>
					<th>경력종료일</th>
					<td>
						<input type="text" name="careerEndDate" onclick="goDatepickerShow(this);" readonly="readonly" class="w100" maxlength="10" placeholder="- 포함">
						<div class="calendar01"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<h3>전문인력관련 서류</h3>
	<div id="table10">
		<table class="view_table eduFileTable">
			<colgroup>
				<col width="38%"/>
				<col width="62%"/>
			</colgroup>
			<tbody>
				<tr class="careerTypTwoTr" data-fileType="16" data-fileSeq="" style="display: none;">
					<th class="acenter">경력교육과정 수료증 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="16"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="16" data-essential="N">초기화</a>
					</td>
				</tr>
				<tr class="careerTypOneTr" data-fileType="17" data-fileSeq="">
					<th class="acenter">인증서(신규) *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="17"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="17" data-essential="Y">초기화</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">경력증명서</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="18"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="18" data-essential="N">초기화</a>
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