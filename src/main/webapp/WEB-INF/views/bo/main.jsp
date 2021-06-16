<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script>
function pageLoad(){
	<sec:authorize access="hasAnyRole('MEMBER')">
		location.href="/member/user/userRegPage";
	</sec:authorize>
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회원가입</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<table>
				<thead>
					<tr>
						<th>가승인 건수</th>
						<th>관리자 승인대기건수</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${result.memberNoRegCnt}</td>
						<td>${result.memberRegAdminCnt}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<div class="top_box">
		<div class="title">
			<h2>등록신청</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<table>
				<thead>
					<tr>
						<th>승인 요청 건수</th>
						<th>보완 요청 건수</th>
						<th>5일 이내 처리 필요건수</th>
						<th>관리자 승인대기건수</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${result.regCnt}</td>
						<td>${result.regUpdCnt}</td>
						<td>${result.regFiCnt}</td>
						<td>${result.regAdminCnt}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<div class="top_box">
		<div class="title">
			<h2>정보변경</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<table>
				<thead>
					<tr>
						<th>변경 요청 건수</th>
						<th>보완 요청 건수</th>
						<th>관리자 승인대기건수</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${result.updCnt}</td>
						<td>${result.updUpdCnt}</td>
						<td>${result.updAdminCnt}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<div class="top_box">
		<div class="title">
			<h2>해지신청</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<table>
				<thead>
					<tr>
						<th>해지 요청 건수</th>
						<th>보완 요청 건수</th>
						<th>관리자 승인대기건수</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${result.dropCnt}</td>
						<td>${result.dropUpdCnt}</td>
						<td>${result.dropAdminCnt}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>