<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
function pageLoad(){
	
}

</script>
<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>협회 관리자 업무분장</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<table>
			<thead>
			<tr>
				<th>회원사명</th>
				<th>실무자 이름1</th>
				<th>실무자 이름2</th>
				<th>실무자 이름3</th>
				<th>실무자 이름4</th>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td>롯데카드</td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
			</tr>
			<tr>
				<td>비씨카드</td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
			</tr>
			<tr>
				<td>삼성카드</td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
			</tr>
			<tr>
				<td>신한카드</td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
			</tr>
			</tbody>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="#" class="btn_black btn_right">저장</a>
		</div>
	</div>
</div>
