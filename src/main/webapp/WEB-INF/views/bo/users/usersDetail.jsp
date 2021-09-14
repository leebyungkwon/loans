<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
function pageLoad(){
	
}



// 리스트 페이지 이동
function usersList(){
	history.back();
}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>회원관리 상세</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table">
			<table class="view_table" id="dtlTable">
				<tr>
					<th>회원사</th>
					<td colspan="3">${usersInfo.userId}</td>
				</tr>
				<tr>
					<th class="acenter">첨부파일</th>
					<td colspan="3">
						<a href="/common/fileDown?fileSeq=${file.fileSeq}">${file.fileFullNm}</a>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="btn_wrap">	
			<a href="javascript:void(0);" class="btn_gray" onclick="usersList();">목록</a>
		</div>
	</div>
</div>

