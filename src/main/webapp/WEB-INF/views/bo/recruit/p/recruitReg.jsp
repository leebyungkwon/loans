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
		<h3  class="article_tit"><span>모집인 등록</span></h3>
		<fieldset>
			<form name="save" action="/bo/recruit/excel" method="POST" enctype="multipart/form-data">
			<table class="inputTable" style="width:500px;">
				<colgroup>
					<col width="30%">
					<col width="70%">
				</colgroup>
				<tbody>
					<tr>
						<td class="leftTd"><span class="type">유형</span></td>
						<td class="rightTd">
							<span class="rabx1">
							    <input type="radio" name="useYn1" id="radioY" value="Y">
							    <label title="예" for="radioY">개인</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="useYn1" id="radioN" value="N">
							    <label title="아니오" for="radioN">법인</label>
							</span>
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">엑셀업로드</span></td>
						<td class="rightTd">
							<input type="file" id="u_file" class="" name="files" multiple="multiple">
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="btn_bx">
				<button type="button" class="btn_black" id="btn_save">저장하기</button>
				<button type="button" class="btn_black" id="btn_close">닫기</button>
			</div>
			</form>
		</fieldset>
	</section>
</div>
