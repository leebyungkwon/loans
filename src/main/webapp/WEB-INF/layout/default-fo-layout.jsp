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
		<div class="popup popup_wrap" style="display: none;">
			<div class="bg"></div>
			<div class="wrap pdf_down popup_inner"></div> <!-- 팝업사이즈 class로 조절 -->
		</div>
		
		<tiles:insertAttribute name="header-fo" />
		<tiles:insertAttribute name="body-fo" />
		<tiles:insertAttribute name="footer-fo" />
		
		<div class="loading_wrap">
			<div class="loading_inner">
				<div class="loadingio-spinner-spinner-76v2yr0v1qm">
					<div class="ldio-ycr7xyx3w0m">
						<div></div>
						<div></div>
						<div></div>
						<div></div>
						<div></div>
						<div></div>
						<div></div>
						<div></div>
						<div></div>
						<div></div>
					</div>
				</div>
				<div class="loading_txt">Loading...</div>
			</div>
			<div style="position: fixed;top: 0px;left: 0px;width: 100%;height: 100%;z-index: 1;opacity: 0.2;background-color: rgb(0, 0, 0);"></div>
		</div>
	</body>
</html>