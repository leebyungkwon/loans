<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">

function pageLoad(){

}

</script>


<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>협회 관리자 업무분장</h2>
		</div>
		<div class="info_box k_search" id="searchDiv">
			<table class="info_box_table" style="width: 90%;">
				<colgroup>
					<col width="10%">
					<col width="23%">
					<col width="10%">
					<col width="23%">
					<col width="10%">
					<col width="23%">
				</colgroup>
				<tr>
					<th>이름</th>
					<td>
						<input type="text" name="memberName"/>
					</td>
					<th>아이디</th>
					<td>
						<input type="text" name="memberId"/>
					</td>
					<th>그룹</th>
					<td class="half_input">
						<select name="creGrp">
							<option value="">전체</option>
							<option value="2">관리자</option>
							<option value="1">실무자</option>
						</select>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
</div>
