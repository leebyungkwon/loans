<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="title_wrap">
	<h5>법인회원 결격요건 업로드</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopupByReload();"></a>
</div>
<p class="popup_desc">
	결격요건 업로드시 법인회원 및 법인관리 리스트에서 다운로드 받으신 엑셀문서 양식을 유지해 주셔야 합니다.<br />
	※ 법인으로 회원가입된 정보만 결격요건 업로드에 해당됩니다. 
</p>
<form name="corpDisForm" id="corpDisForm" method="post" action="/admin/corpUsers/corpUsersDisExcelUpload" enctype="multipart/form-data">
	<table class="popup_table">
		<colgroup>
			<col width="170">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>엑셀업로드 *</th>
				<td class="file" id="sampleCheck">
					<input type="text" class="w50 file_input" readonly disabled>
					<input type="file" name="files" id="corpDisFile" onchange="goFileNmShow();" style="display: none;"/>
					<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#corpDisFile').click();">파일찾기</a>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="goCorpDisExcelUpload();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopupByReload();">취소</a>
</div>

