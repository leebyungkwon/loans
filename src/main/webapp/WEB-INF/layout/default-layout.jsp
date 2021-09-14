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
		<!--<meta name="viewport" content="width=device-width, initial-scale=1.0">-->
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<tiles:insertAttribute name="css" />
		<tiles:insertAttribute name="js" />
		<script>
		var h_gridPage = 0;
		window.onload = function() {
			
			if('${historyback}' == 'Y'){
		    	$(".k_search").html(sessionStorage.getItem('H_'+window.location.pathname));
		    	var b = JSON.parse(sessionStorage.getItem('P_'+window.location.pathname));
		    	h_gridPage = sessionStorage.getItem('PAGE_'+window.location.pathname)
		    	WebUtil.setHistoryParam(b);
		    	removeBackHistory();
		    	pageLoad();
			}else{
				if(null != sessionStorage.getItem('H_'+window.location.pathname) &&
						null != sessionStorage.getItem('P_'+window.location.pathname) &&
						null != sessionStorage.getItem('PAGE_'+window.location.pathname)){
						window.onpageshow = function(event) {
						    if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
						    	$(".k_search").html(sessionStorage.getItem('H_'+window.location.pathname));
						    	var b = JSON.parse(sessionStorage.getItem('P_'+window.location.pathname));
						    	h_gridPage = sessionStorage.getItem('PAGE_'+window.location.pathname)
						    	WebUtil.setHistoryParam(b);
						    	removeBackHistory();
						    	pageLoad();
						    }else{
						    	removeBackHistory();
						    	pageLoad();
						    }
						}
					}else{
						removeBackHistory();
						pageLoad();
					}
			}
			
			menuActiveFunction();
		}
		function removeBackHistory(){
	    	sessionStorage.removeItem('H_'+window.location.pathname);
	    	sessionStorage.removeItem('P_'+window.location.pathname);
	    	sessionStorage.removeItem('PAGE_'+window.location.pathname);
		}
		</script>
	</head>
	<body>
		<div class="popup_wrap">
			<div class="popup_inner"></div>
		</div>
		
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="left" />
		<tiles:insertAttribute name="body" />
		<tiles:insertAttribute name="footer" />
		
		<!-- 
		<div class="layerPopupBase" style="display:none;">
			<button type="button" class="pop_btn_close closeLayer">X</button>
	    	<div class="layerContent">
	    	</div>
		</div>
		<div class="msgLayerPopupBase" style="display:none;">
			<button type="button" class="pop_btn_close msgCloseLayer">X</button>
	    	<div class="layerContent">
	    	</div>
		</div>
		 -->
		
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
		
		<!-- <div class="wrapperModal"></div> -->
	</body>
</html>