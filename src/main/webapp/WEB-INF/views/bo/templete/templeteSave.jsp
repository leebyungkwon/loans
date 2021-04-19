<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
function pageLoad(){
	document.getElementById('btn_reg').onclick = function () {
		AjaxUtil.formsubmit('reg');
	};
}
</script>	
<div class="article_right">
	<section class="k_inputform">
			<h3  class="article_tit">템플릿 1 저장하기</h3>
			<fieldset>
			<form name="reg" action="/bo/board/noticeReg" method="POST">
			<ul class="k_tb_lst">
				<li class="tit_row">
					<div class="in_td"><strong class="type">제목</strong></div>
					<div class="in_td">
						<div class="inpbx">
							<input type="text" name="boardTitle" id="u_title" placeholder="제목을 입력하세요." class="" value="${board.boardTitle}">
						</div>
					</div>
				</li>
				<li class="cnt_row chk">
					<div class="in_td"><strong class="type">내용</strong></div>
					<div class="in_td">
						<div class="inptxtbx">
							<textarea id="u_content" name="boardCnts" rows="5" cols="1" placeholder="내용을 입력하세요." class="field_inp">${board.boardCnts}</textarea>
						</div>
					</div>
				</li>
				
				<li class="add_img">
					<div class="in_td"><strong class="type">사진첨부</strong></div>
					<div class="in_td">
						<div class="inpfilebx">
						    <label for="u_file">사진첨부</label>
						    <input type="file" id="u_file" class="upload_hidden" multiple="">
						    <input disabled="disabled" class="upload_name">
						</div>
						
						<div class="select_file_area">
						    <ul class="file_lst">
						        <!---->
						    </ul>
						    <button type="button" class="btn_bor">삭제</button> 
						</div>
					</div>
				</li>
			</ul>
			<div class="btn_bx">
				<button type="button" class="btn_black" id="btn_reg">저장하기</button>
			</div>
		</form>
		</fieldset>
	</section>
</div>
