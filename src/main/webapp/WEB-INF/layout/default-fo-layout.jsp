<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<title>여신금융협회 대출모집인</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<tiles:insertAttribute name="css-fo" />
		<tiles:insertAttribute name="js-fo" />
		<script>
		window.onload = function() {
			pageLoad();
			menuActiveFunction();
			relateSlide();
		}
		</script>
	</head>
	<body>
		<div class="popup_wrap">
			<div class="popup_inner"></div>
		</div>
		
		<tiles:insertAttribute name="header-fo" />
		<tiles:insertAttribute name="body-fo" />
		<tiles:insertAttribute name="footer-fo" />
	</body>
</html>