<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

(function() {
	document.getElementById('btn_save').onclick = function () {
		var p = {
			  name : 'save'
			, success : function (opt,result) {
				Grid.refresh();
				$("#btn_close").trigger('click');
    	    }
		}
		AjaxUtil.form(p);
	};
	document.getElementById('btn_close').onclick = function () {
		$(".closeLayer").trigger('click');
	};
})();

</script>	
<div class="article_right">
	<section class="k_inputform">
		<h3  class="article_tit">엑셀 템플릿 저장하기</h3>
		<div class="r_left">
			<fieldset>
			<form name="save" action="/bo/templete/templeteSave" method="POST">
				<ul class="k_tb_lst">
					<li class="tit_row">
						<div class="in_td"><strong class="type">제목</strong></div>
						<div class="in_td">
							<div class="inpbx">
								<input type="text" name="boardTitle" id="u_title" placeholder="제목을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"제목을 입력하세요"}'>
							</div>
						</div>
					</li>
					<li class="tit_row">
						<div class="in_td"><strong class="type">설명</strong></div>
						<div class="in_td">
							<div class="inpbx">
								<input type="text" name="boardTitle" id="u_title" placeholder="설명을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"제목을 입력하세요"}'>
							</div>
						</div>
					</li>
					<li class="tit_row">
						<div class="in_td"><strong class="type">문서명</strong></div>
						<div class="in_td">
							<div class="inpbx">
								<input type="text" name="boardTitle" id="u_title" placeholder="문서명을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"제목을 입력하세요"}'>
							</div>
						</div>
					</li>
					<li class="tit_row">
						<div class="in_td"><strong class="type">(SELECT)사용</strong></div>
						<div class="in_td">
							<div class="">
								<select name="useYn">
									<option value="Y">예</option>
									<option value="N">아니오</option>
								</select>
							</div>
						</div>
					</li>
					
				</ul>
			</form>
			</fieldset>
		</div>
		<div class="r_right" style="width:800px;">
			<fieldset>
			<form name="save" action="/bo/templete/templeteSave" method="POST">
				<ul class="k_tb_lst">
					<li class="tit_row">
						<div class="in_td"><strong class="type">제목</strong></div>
						<div class="in_td">
							<div class="inpbx">
								<input type="text" name="boardTitle" id="u_title" placeholder="제목을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"제목을 입력하세요"}'>
							</div>
						</div>
						<div class="in_td"><strong class="type">제목</strong></div>
						<div class="in_td">
							<div class="inpbx">
								<input type="text" name="boardTitle" id="u_title" placeholder="제목을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"제목을 입력하세요"}'>
							</div>
						</div>
						<div class="in_td"><strong class="type">제목</strong></div>
						<div class="in_td">
							<div class="inpbx">
								<input type="text" name="boardTitle" id="u_title" placeholder="제목을 입력하세요." class="" value="${board.boardTitle}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"제목을 입력하세요"}'>
							</div>
						</div>
					</li>
				</ul>
			</form>
			</fieldset>
		</div>
	</section>
	<div class="btn_bx" style="float:left;">
		<button type="button" class="btn_black" id="btn_save">저장하기</button>
		<button type="button" class="btn_black" id="btn_close">닫기</button>
	</div>
</div>
