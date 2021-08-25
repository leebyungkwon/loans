<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="data_wrap">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
	<input type="hidden" name="fileGrpSeq" value=""/>
	
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
					<td><input type="text" name="operName" class="w100" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
					<th>주민번호</th>
					<td><input type="text" name="plMZId" class="w100" maxlength="14" placeholder="- 포함" data-vd='{"type":"pId","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
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
					<th class="acenter">자격확인 서류 또는 경력증명서</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="19"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="19" data-essential="N">초기화</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">상근임을 증빙할 수 있는 서류</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="20"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="20" data-essential="N">초기화</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">아웃소싱 업체 상주직원 관련 증빙서류</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="37"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="37" data-essential="N">초기화</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">개인정보필수동의서 *</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="38"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
						<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="38" data-essential="Y">초기화</a>
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