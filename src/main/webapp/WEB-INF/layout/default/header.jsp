<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script>

</script>

<div class="wrap">
	<div class="header">
		<div class="inner">
			<h1>
				<c:choose>
					<c:when test="${sessionScope.member ne null }">
						<c:choose>
							<c:when test="${sessionScope.member.creYn eq 'Y' }">
								<a href="/main">
									<img src="/static/images/common/top_logo_ov.png" alt="" />
								</a>
							</c:when>
							<c:otherwise>
								<a href="/member/user/userRegPage">
									<img src="/static/images/common/top_logo_ov.png" alt="" />
								</a>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<a href="/login">
							<img src="/static/images/common/top_logo_ov.png" alt="" />
						</a>
					</c:otherwise>
				</c:choose>
			</h1>
			<c:if test="${sessionScope.member ne null }">
				<div class="log_menu">
					<span>${member.memberName }님</span>
					<div class="logout">
						<a href="/logout">로그아웃</a>
					</div>
				</div>
			</c:if>
		</div>
	</div>
	
	<div class="container">
