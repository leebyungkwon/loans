<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="title_wrap">
	<h5>개인회원 결격요건 업로드</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopupByReload();"></a>
</div>
<form name="indvDisForm" id="indvDisForm" method="post" action="/admin/users/indvUsersDisExcelUpload" enctype="multipart/form-data">
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
					<input type="file" name="files" id="indvDisFile" onchange="goFileNmShow();" style="display: none;"/>
					<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#indvDisFile').click();">파일찾기</a>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="goIndvDisExcelUpload();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopupByReload();">취소</a>
</div>

