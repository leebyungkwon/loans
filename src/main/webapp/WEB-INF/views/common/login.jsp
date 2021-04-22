<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script>
function pageLoad(){
    	document.getElementById('btn_login').onclick = function () {
		AjaxUtil.submit('login');
	};   
	
/*     	$("#btn_login").on("click", function(){
		var param = {
				'email'			: $("#user_id").val()
				, 'password'	:  $("#user_password").val()
		}
 		var p = {
			  param : param
			, url : "/j_spring_security"
			, success : function (opt,result) {
				AjaxUtil.submit('login');
    	    }
		}
		
		AjaxUtil.post(p);
	});   
	  */
	
	
}
</script>


	<div id="container">
		<div class="page_title">
			<h2>로그인</h2>
			<p class="sub">
				<span>리스할부모집인 등록관리 시스템입니다.</span><br />
				비밀번호 5회 오류 시, 30분동안 로그인이 제한됩니다.
			</p>
		</div>
		<div class="login_wrap">
			<form name="login" action="/j_spring_security" method="POST">
				<div class="input_wrap clfix">
					<div class="f-left w80">
						<div class="input_box mgt0">
							<div class="tit">아이디</div>
							<div class="value">
								<input type="text" id="user_id" name="email" placeholder="아이디 (이메일)" />
							</div>
						</div>
						<div class="input_box">
							<div class="tit">비밀번호</div>
							<div class="value">
								<input type="password" id="user_password" name="password" placeholder="비밀번호" />
							</div>
						</div>
					</div>
					<a href="javascript:void(0);" class="btn_login" id="btn_login">로그인</a>
				</div>
				
				<!-- 로그인 실패 관련 메세지 영역 필요함 -->
				<c:if test="${not empty loginFailMsg}">
					<span class="txt_warning">${loginFailMsg }</span>
					<c:remove var="loginFailMsg" scope="session" />
				</c:if>
				
				<div class="bottom_box clfix">
					<p class="f-left">
						<input type="checkbox" id="test1" class="check">
						<label for="test1">아이디 저장</label> <!-- id/for 값 변경하셔도됩니다 -->
					</p>
					<div class="f-right right_cont">
						<ul>
							<li>
								<a href="/signup">회원가입</a>
							</li>
						</ul>
					</div>
				</div>
			</form>
		</div>
	</div>
