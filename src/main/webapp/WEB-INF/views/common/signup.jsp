<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
function pageLoad(){
	document.getElementById('btn_login').onclick = function () {
		AjaxUtil.submit('signup');
	};
}
</script>
<div class="login_wrap ">
	<h2 class="member_title">회원가입</h2>
	<div class="tab_cnt signup_step">
		<form class="" name="signup" id="signup" action="/signup" method="POST">
			<h3 class="">로그인에 사용할<br>아이디를 입력해주세요.</h3>
	        <div class="inpbx">
	                <input type="text" id="user_id" name="email" placeholder="아이디 (이메일)" data-vd='{"type":"email","len":"8,20","req":true,"msg":"아이디를 입력하세요."}'/>
			</div>
			<h3 class="">로그인에 사용할<br>비밀번호를 입력해주세요.</h3>
			<div class="inpbx">
				<input type="password" id="user_password" name="password" placeholder="비밀번호" data-vd='{"type":"pw","len":"1,20","req":false,"msg":"비밀번호를 입력하세요."}'>
			</div>
			<p class="field_vali">
                <span class="on">영문포함</span>
                <span class="">숫자포함</span>
                <span class="">8~20자 이내</span>
            </p>
			<div class="inpbx">
				<input type="password" id="user_password_chk" name="password_chk" placeholder="비밀번호 확인" data-vd='{"type":"pw","len":"1,20","req":false,"eq":"password","msg":"비밀번호 일치 여부 확인 하세요."}'>
			</div>
			<p class="field_vali">
                <span class="">비밀번호 일치</span>
            </p>
            
			<h3 class="">이름을 입력하세요</h3>
			<div class="inpbx">
				<input type="text" id="name" name="memberNm" placeholder="이름">
			</div>
	        <button type="button" class="btn btn_login" id="btn_login">가입 하기</button>
	    </form>
	
	    <a href="/signup" class="member_go">계정이 없으신가요? 간편가입하기</a>
	    <ul class="linkarea">
	        <li><a href="">아이디 (이메일) 찾기</a></li>
	        <li><a href="">비밀번호 찾기</a></li>
	    </ul>
	</div>
</div>

