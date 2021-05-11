<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="data_wrap">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
	<input type="hidden" name="fileGrpSeq" value="${result.userRegInfo.fileSeq }"/>

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
					<td><input type="text" name="operName" class="w100" maxlength="10"></td>
					<th>주민번호</th>
					<td><input type="text" name="plMZId" class="w100" maxlength="14" placeholder="- 포함"></td>
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
					<th class="acenter">경력증명서 (전산인력)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="19"/>
						<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
					</td>
				</tr>
				<tr>
					<th class="acenter">자격확인 서류(전산인력)</th>
					<td>
						<input type="text" class="w50 file_input" readonly disabled>
						<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
						<input type="hidden" name="fileTypeList" value="20"/>
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