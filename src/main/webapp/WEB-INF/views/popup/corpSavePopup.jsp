<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="title_wrap">
	<h5>법인 정보</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>

<form name="corpInfoSaveFrm" id="corpInfoSaveFrm" method="post">
	<input type="hidden" name="corpSeq" value="${corpInfo.corpSeq }"/>
	<input type="hidden" name="pathTyp" value="2"/>
	
	<table class="popup_table">
		<colgroup>
			<col width="170">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>법인명</th>
				<td class="file">
					<input type="text" name="plMerchantName" value="${corpInfo.plMerchantName }" class="w50 file_input" data-vd='{"type":"text","len":"1,30","req":true,"msg":"법인명을 입력해 주세요."}'>
				</td>
			</tr>
			<tr>
				<th>법인번호</th>
				<td class="file">
					<input type="text" name="plMerchantNo" value="${corpInfo.plMerchantNo }" class="w50 file_input" data-vd='{"type":"text","len":"14,14","req":true,"msg":"법인번호(- 포함)를 입력해 주세요."}'>
				</td>
			</tr>
			<c:if test="${corpInfo.corpSeq ne null }">
				<tr>
					<th>사용여부</th>
					<td class="file">
						<select name="useYn">
							<option value="Y" <c:if test="${corpInfo.useYn eq 'Y' }">selected="selected"</c:if>>사용</option>
							<option value="N" <c:if test="${corpInfo.useYn eq 'N' }">selected="selected"</c:if>>미사용</option>
						</select>
					</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="goCorpInfoSave();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">취소</a>
</div>

