<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<sec:authentication property="principal.username" var="tempMemberSeq" />
<script>
	function pageLoad(){
		<sec:authorize access="hasAnyRole('TEMP_MEMBER')">
			$("#adminDetailFrm").submit();
		</sec:authorize>
	}
</script>
<form id="adminDetailFrm" method="post" action="/member/admin/adminDetailUpdPage">
	<input type="hidden" name="memberSeq" value="${tempMemberSeq}"/>
	<input type="hidden" name="tempMemberCheck" value="Y"/>
</form>

