<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="title_wrap">
	<h5>법인 정보</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>

<form name="corpInfoSaveFrm" id="corpInfoSaveFrm" method="post" action="/admin/corp/saveCorpInfo" enctype="multipart/form-data">
	<input type="hidden" name="pathTyp" value="2"/>
	<table class="popup_table">
		<colgroup>
			<col width="170">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>법인명</th>
				<td>
					<input type="text" id="plMerchantName" name="plMerchantName" placeholder="법인명을 입력해 주세요." maxlength="20" value="${corpInfo.plMerchantName }" class="w100" data-vd='{"type":"text","len":"1,20","req":true,"msg":"법인명을 입력해 주세요."}'>
				</td>
			</tr>
			<tr>
				<th>법인번호</th>
				<td>
					<input type="text" id="plMerchantNo" name="plMerchantNo" placeholder="법인번호(- 포함) 14자리를 입력해 주세요." maxlength="14" value="${corpInfo.plMerchantNo }" class="w100" data-vd='{"type":"plMerchant","len":"14,14","req":true,"msg":"법인번호(- 포함) 14자리를 입력해 주세요."}'/>
				</td>
			</tr>
			<c:choose>
				<c:when test="${corpInfo.corpSeq ne null }">
					<c:if test="${corpInfo.pathTyp eq '2' }">
						<tr>
							<th>금융감독원 승인여부</th>
							<td>
								<div class="input_radio_wrap">
									<input type="radio" name="passYn" id="radio01" value="Y" <c:if test="${corpInfo.passYn eq 'Y' }">checked="checked"</c:if>>
									<label for="radio01">Y</label>
								</div>
								<div class="input_radio_wrap mgl20">
									<input type="radio" name="passYn" id="radio02" value="N" <c:if test="${corpInfo.passYn eq 'N' }">checked="checked"</c:if>>
									<label for="radio02">N</label>
								</div>
							</td>
						</tr>
					</c:if>
				</c:when>
				<c:otherwise>
					<tr>
						<th>금융감독원 승인여부</th>
						<td>
							<div class="input_radio_wrap">
								<input type="radio" name="passYn" id="radio01" value="Y">
								<label for="radio01">Y</label>
							</div>
							<div class="input_radio_wrap mgl20">
								<input type="radio" name="passYn" id="radio02" value="N" checked="checked">
								<label for="radio02">N</label>
							</div>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
			
			<%-- 
			<c:if test="${corpInfo.corpSeq ne null }">
				<tr>
					<th>사용여부</th>
					<td>
						<select name="useYn">
							<option value="Y" <c:if test="${corpInfo.useYn eq 'Y' }">selected="selected"</c:if>>사용</option>
							<option value="N" <c:if test="${corpInfo.useYn eq 'N' }">selected="selected"</c:if>>미사용</option>
						</select>
					</td>
				</tr>
			</c:if>
			 --%>
		</tbody>
	</table>
</form>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="goCorpInfoSave();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">취소</a>
</div>

