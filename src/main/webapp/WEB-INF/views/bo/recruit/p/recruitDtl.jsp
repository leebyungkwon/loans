<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
(function() {
	document.getElementById('btn_save').onclick = function () {
 		var p = {
			  name : 'save'
			, success : function (opt,result) {
				Grid.refresh();
				$("#btn_close").trigger('click');
    	    }
		}
		AjaxUtil.files(p);
	};
	document.getElementById('btn_close').onclick = function () {
		$(".closeLayer").trigger('click');
	};
})();

</script>	
<div class="article_right">
	<section class="k_inputform">
		<h3  class="article_tit"><span>모집인 저장</span></h3>
		<fieldset>
			<form name="save" action="/bo/recruit/save" method="POST" enctype="multipart/form-data">
			
			<input type="hidden" name="recrNo" id="recrNo"/>
			<table class="inputTable" style="width:100%;">
				<colgroup>
					<col width="20%">
					<col width="80%">
				</colgroup>
					<tr>
						<td class="leftTd"><span class="type">이름</span></td>
						<td class="rightTd">
							<input type="text" name="recrNm" id="recrNm" placeholder="이름을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력하세요"}'>
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">연락처</span></td>
						<td class="rightTd">
							<input type="text" name="recrMobile" id="recrMobile" placeholder="연락처를 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"연락처을 입력하세요"}'>
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">이메일</span></td>
						<td class="rightTd">
							<input type="text" name="recrEmail" id="recrEmail" placeholder="이메일을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,35","req":true,"msg":"이메일을 입력하세요"}'>
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">유형</span></td>
						<td class="rightTd">
							<select name="recrType">
								<option value="01">개인</option>
								<option value="02">법인</option>
								<option value="03">법인 사용인</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">단계</span></td>
						<td class="rightTd">
							<select name="recrStep">
								<option value="01">작성</option>
								<option value="02">승인요청</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">이미지1</span></td>
						<td class="rightTd">
							<input type="file" id="u_file" class="" name="files" multiple="multiple">
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">이미지2</span></td>
						<td class="rightTd">
							<input type="file" id="u_file" class="" name="files" multiple="multiple">
						</td>
					</tr>
					<!-- 
					<tr>
						<td class="leftTd"><span class="type">사용</span></td>
						<td class="rightTd">
							<select name="useYn" disabled>
								<option value="Y">예</option>
								<option value="N">아니오</option>
							</select>
						</td>
					</tr>
					 -->
			</table>
			
			<div class="btn_bx">
				<button type="button" class="btn_black" id="btn_save">저장하기</button>
				<button type="button" class="btn_black" id="btn_close">닫기</button>
			</div>
			</form>
		</fieldset>
	</section>
</div>
