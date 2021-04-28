<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script>
function pageLoad(){
	document.getElementById('btn_login').onclick = function () {
		var param = {
			"loginId" : $("#loginId").val()
			,	"pwd" : $("#pwd").val()
		}
		var p = {
				url: "/login"
				, param: param
				, success: function(opt, result) {
					AjaxUtil.submit('login');
				}
		}
		AjaxUtil.post(p);
	};
}
</script>
<div class="login_wrap">
	<h2 class="member_title ng-star-inserted">로그인</h2>
	<div class="tab_cnt">
		<form name="login" action="/j_spring_security" method="POST">
	        <div class="inpbx">
	            <input type="text" id="loginId" name="loginId" placeholder="아이디" data-vd='{"type":"text","len":"1,20","req":true,"msg":"아이디를 입력하세요"}'/>
			</div>
			<div class="inpbx">
				<input type="password" id="pwd" name="pwd" placeholder="비밀번호" data-vd='{"type":"text","len":"1,20","req":true,"msg":"비밀번호를 입력하세요"}'>
			</div>

			<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
			<span class="txt_warning">입력하신 정보와 일치하는 계정이 없습니다.</br> 로그인 정보를 확인해주세요</span>
			<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
			</c:if>
	        <button type="button" class="btn btn_login" id="btn_login">로그인하기</button><!---->

	    </form>

	    <a href="/signup" class="member_go">계정이 없으신가요? 간편가입하기</a>
	    <ul class="linkarea">
	        <li><a href="./find?findType=id">아이디 (이메일) 찾기</a></li>
	        <li><a href="./find?findType=password">비밀번호 찾기</a></li>
	    </ul>
	</div>
</div>

