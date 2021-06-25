<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="title_wrap">
	<h5>회원사 등록</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>

<form name="companyCodeSaveFrm" id="companyCodeSaveFrm" method="post" action="/admin/company/saveCompanyCode">
	<table class="popup_table">
		<colgroup>
			<col width="170">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>회원사코드</th>
				<td>
					<input type="text" name="comCode" placeholder="회원사코드를 입력해 주세요." maxlength="8" class="w70" data-vd='{"type":"text","len":"1,10","req":true,"msg":"회원사코드를 입력해 주세요."}'>
				</td>
			</tr>
			<tr>
				<th>회원사명</th>
				<td>
					<input type="text" name="comName" placeholder="회원사명을 입력해 주세요." maxlength="30" class="w70" data-vd='{"type":"text","len":"1,30","req":true,"msg":"회원사명을 입력해 주세요."}'>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="goCompanyCodeSave();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">취소</a>
</div>

