<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>메인페이지</title>
		<!--<meta name="viewport" content="width=device-width, initial-scale=1.0">-->
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<tiles:insertAttribute name="css" />
		<link rel="stylesheet" type="text/css" href="/static/css/login.css">
		<tiles:insertAttribute name="js" />
		<script>
		window.onload = function() { 
			pageLoad();
		}
		</script>
	</head>
	<body >
		<tiles:insertAttribute name="header" />
		
		<section class="k_no_wrap">
			<div class="login_wrap">		
				<tiles:insertAttribute name="body" />
			</div>
		</section>
		<!-- body -->

		<tiles:insertAttribute name="footer" />
		<!-- /WEB-INF/views/common/layout/footer.jsp -->
	</body>
</html>