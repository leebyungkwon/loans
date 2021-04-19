<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
	
		<div class="article_right">
			<section class="k_inputform">
				<h3 class="tit">입력 양식</h3>
				<fieldset>
				<ul class="k_tb_lst">
					<li class="chk">
						<div class="in_td"><strong class="type">라디오</strong></div>
						<div class="in_td">
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼1">라디오버튼1</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼2">라디오버튼2</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼3">라디오버튼3</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼4">라디오버튼4</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼1">라디오버튼1</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼1">라디오버튼1</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼1">라디오버튼1</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="undefined">
							    <label title="라디오버튼1">라디오버튼1</label>
							</span>
						</div>
					</li>
					<li class="user">
		                <div class="in_td"><strong class="type">성명</strong></div>
						<div class="in_td">
							이병권
						</div>
					</li>
					<li class="user_email">
		                <div class="in_td"><strong class="type">이메일</strong></div>
						<div class="in_td">
							kfgabiz@nate.com
							<a href="">이메일 변경하기</a>
						</div>
					</li>
					<li class="tit_row">
						<div class="in_td"><strong class="type">제목</strong></div>
						<div class="in_td">
							<div class="inpbx">
								<input type="text" id="u_title" formcontrolname="title" placeholder="제목을 입력하세요." class="">
							</div>
						</div>
					</li>
					<li class="cnt_row chk">
						<div class="in_td"><strong class="type">내용</strong></div>
						<div class="in_td">
							<div class="inptxtbx">
								<textarea id="u_content" rows="5" cols="1" placeholder="내용을 입력하세요." formcontrolname="contents" class="field_inp ng-untouched ng-pristine ng-invalid"></textarea>
							</div>
						</div>
						<div class="ckemail">
							<span _ngcontent-fav-c86="" class="ckbx1">
						    	<input _ngcontent-fav-c86="" type="checkbox">
						    	<label _ngcontent-fav-c86="" title="이메일 받기">이메일 받기</label>
							</span>
						</div>
					</li>
					<li class="add_img">
						<div class="in_td"><strong class="type">사진첨부</strong></div>
						<div class="in_td">
								<ruler-file-upload _nghost-fav-c133=""><div class="inpfilebx">
							    <label for="u_file">사진첨부</label>
							    <input type="file" id="u_file" class="upload_hidden" multiple="">
							    <input disabled="disabled" class="upload_name">
							    
							</div>
							
							<div class="select_file_area">
							    <ul class="file_lst">
							        <!---->
							    </ul>
							    <button type="button" class="btn_bor">삭제</button> 
							</div></ruler-file-upload>
							
							<ul class="info_lst">
								<li>상품 불량 및 오배송의 경우, 해당 제품 사진을 등록 부탁드립니다.</li>
								<li class="point">파일명은 영문만 가능하며, 파일당 최대 10MB 의<br> 용량 제한이 있습니다.</li>
								<li>가로사이즈가 450pixel을 초과하는 경우 자동으로<br> 450픽셀로 조정됩니다.</li>
								<li>첨부파일은 최대 2개까지 등록가능합니다.</li>
							</ul>
						</div>
					</li>
				</ul>
				<div class="btn_bx">
					<button type="submit" class="btn_black">등록하기</button>
				</div>
			</fieldset>
			</section>
		</div>
	</div>
