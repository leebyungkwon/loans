<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script>
function pageLoad(){
   	document.getElementById('btn_login').onclick = function () {
		AjaxUtil.submit('login');
	};  
	
	// 개별 수동 검증
/*   	$("#btn_login").on("click", function(){
		var param = {
				'email'			: $("#user_id").val()
				, 'password'	:  $("#user_password").val()
		}
 		var p = {
			  param : param
			, url : "/j_spring_security"
			, success : function (opt,result) {
				var tt = "${requestScope.loginFailMsg}";
				console.log(".....", tt);
				console.log(".....1111    "+ tt);
    	    }
		}
		
		AjaxUtil.post(p);
	});   */
	
	
	
}
</script>
<div class="login_wrap">
	<h2 class="member_title ng-star-inserted">로그인</h2>
	<div class="tab_cnt">
		<form name="login" action="/j_spring_security" method="POST">
	        <div class="inpbx">
	            <input type="text" id="user_id" name="email" placeholder="아이디 (이메일)" />
			</div>
			<div class="inpbx">
				<input type="password" id="user_password" name="password" placeholder="비밀번호" />
			</div>
			
			<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
			<span class="txt_warning">입력하신 정보와 일치하는 계정이 없습니다.</br> 로그인 정보를 확인해주세요</span>
			<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
			</c:if>
			
			<c:if test="${not empty loginFailMsg}">
				<span class="txt_warning">${loginFailMsg }</span>
				<c:remove var="loginFailMsg" scope="session" />
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

