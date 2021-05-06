<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script>
function pageLoad(){
	
	// 아이디 저장 cookie 체크
	var rememberCheck = $.cookie('rememberCheck');
	if(rememberCheck == "Y"){
		$("#rememberId").prop("checked", true);
		$("#memberId").val($.cookie('rememberId'));
	}
	
	var failMsg = "${loginFailMsg}";
	if(WebUtil.isNotNull(failMsg)){
		alert(failMsg);
	}
	
	$("#loginBtn").on("click", function(){
   		var memberId = $("#memberId").val();
   		var password = $("#password").val();
   		
   		if(WebUtil.isNull(memberId)){
   			alert("아이디를 입력해 주세요.");
   			$("#memberId").focus();
   			return false;
   		}
   		
   		if(WebUtil.isNull(password)){
   			alert("비밀번호를 입력해 주세요.");
   			$("#password").focus();
   			return false;
   		}
   		
   		if($("#rememberId").is(":checked")){
   			// 30일 뒤에 만료되는 쿠키 생성 
   			$.cookie('rememberCheck', 'Y', { expires: 30 });
   			$.cookie('rememberId', memberId, { expires: 30 });
   		}else{
   			$.removeCookie('rememberCheck');
   			$.removeCookie('rememberId');
   		}
   		
		AjaxUtil.submit('login');		
	});
	

	
}
</script>

	<div class="container">
		<div class="page_title">
			<h2>로그인</h2>
		</div>
		<div class="login_wrap">
			<form name="login" action="/j_spring_security" method="POST">
				<div class="input_wrap clfix">
					<div class="f-left w80">
						<div class="input_box mgt0">
							<div class="tit">아이디</div>
							<div class="value">
								<input type="text" id="memberId" name="memberId" placeholder="아이디" />
							</div>
						</div>
						<div class="input_box">
							<div class="tit">비밀번호</div>
							<div class="value">
								<input type="password" id="password" name="password" placeholder="비밀번호" />
							</div>
						</div>
					</div>
					<a href="javascript:void(0);" class="btn_login" id="loginBtn">로그인</a>
				</div>
				
				<div class="bottom_box clfix">
					<p class="f-left">
						<input type="checkbox" id="rememberId" class="check">
						<label for="rememberId">아이디 저장</label>
					</p>
					<div class="f-right right_cont">
						<ul>
							<li>
								<a href="/terms">회원가입</a>
							</li>
						</ul>
					</div>
				</div>
			</form>
		</div>
	</div>
