<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="data_wrap">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
	<input type="hidden" name="fileGrpSeq" value=""/>

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
					<td><input type="text" name="excName" class="w100" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
					<th>주민번호</th>
					<td><input type="text" name="plMZId" class="w100" maxlength="14" placeholder="- 포함" data-vd='{"type":"text","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
				</tr>
				<tr>
					<th>직위</th>
					<td><input type="text" name="positionNm" class="w100" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위를 입력해 주세요."}'></td>
					<th>금융상품유형</th>
					<td>${result.userRegInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>교육이수번호</th>
					<td colspan="3"><input type="text" name="plEduNo" class="w100" maxlength="10" data-vd='{"type":"text","len":"10,30","req":true,"msg":"교육이수번호를 입력해 주세요."}'></td>
				</tr>
				<tr>
					<th>경력시작일</th>
					<td>
						<input type="text" name="careerStartDate" onclick="goDatepickerShow(this);" class="w100" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력시작일(- 포함)을 입력해 주세요."}'>
						<div id="date_cal01" class="calendar01"></div>
					</td>
					<th>경력종료일</th>
					<td>
						<input type="text" name="careerEndDate" onclick="goDatepickerShow(this);" class="w100" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력종료일(- 포함)을 입력해 주세요."}'>
						<div id="date_cal02" class="calendar01"></div>
					</td>
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
					<td>
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
					<th class="acenter">대표자 이력서 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="7"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">대표자 경력증명서 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="8"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="9"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">인감증명서 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="10"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">후견부존재증명서 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="27"/>
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
					<th class="acenter">위탁계약서 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="11"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="28"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="28" data-essential="N">초기화</a>
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
				<tr class="careerTypTwoTr" data-fileType="12" data-fileSeq="" style="display: none;">
					<th class="acenter">대표 경력교육과정 수료증(여신금융교육연수원)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="H" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="12"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="12" data-essential="H">초기화</a>
					</td>
				</tr>
				<tr class="careerTypTwoTr" data-fileType="30" data-fileSeq="" style="display: none;">
					<th class="acenter">대표 경력교육과정 수료증(보험개발원,한국금융연구원)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="H" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="30"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="30" data-essential="H">초기화</a>
					</td>
				</tr>
				<tr class="careerTypOneTr" data-fileType="13" data-fileSeq="">
					<th class="acenter">대표 인증서(신규) *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="13"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">대표 경력증명서 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
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
					<th class="acenter">업무수행기준요건관련 서류 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
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