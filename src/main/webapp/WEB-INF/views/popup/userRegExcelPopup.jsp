<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="title_wrap">
	<h5>모집인 등록</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>
<p class="popup_desc">
	모집인 등록은 등록 유형에 따른 샘플파일을 다운로드 하시고 해당 양식에 따라 등록되어야 합니다.<br />
	엑셀 업로드 이후에는 각 모집인별로 첨부파일을 등록완료 후에 승인신청을 하셔야 합니다.
</p>
<form name="userRegInfoInsertFrm" id="userRegInfoInsertFrm" method="post" enctype="multipart/form-data">
	<table class="popup_table">
		<colgroup>
			<col width="170">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>모집인유형 *</th>
				<td>
					<div class="input_radio_wrap">
						<input type="radio" name="plClass" id="radio01" value="1" onclick="sampleDownPop()">
						<label for="radio01">개인</label>
					</div>
					<div class="input_radio_wrap mgl20">
						<input type="radio" name="plClass" id="radio02" value="2" onclick="sampleDownPop()">
						<label for="radio02">법인</label>
					</div>
				</td>
			</tr>
			<tr>
				<th>엑셀업로드 *</th>
				<td class="file" id="sampleCheck">
					<input type="text" class="w50 file_input" readonly disabled>
					<input type="file" name="files" id="userRegFile" onchange="goFileNmShow();" style="display: none;"/>
					<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegFile').click();">파일찾기</a>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="goUserRegInfoExcelUpload();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">취소</a>
</div>

