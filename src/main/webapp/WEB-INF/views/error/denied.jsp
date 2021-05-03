<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script>
	function pageLoad(){
		
		
		<sec:authorize access="hasAnyRole('TEMP_MEMBER')">
			alert("???");
			location.href="/prevLogin";
		</sec:authorize>
		
	}
</script>

	접근 할 수 없는 페이지

