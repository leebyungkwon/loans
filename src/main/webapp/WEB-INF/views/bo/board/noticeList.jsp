<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
var Grid = Object.create(GRID); 
function pageLoad(){
	Grid.set({
		  id		: "grid"
  		, url		: "/bo/board/list"
	    , width		: "100%"
  		, headCol	: ["번호", "타입", "제목", "등록시간"]
  		, bodyCol	: 
  			[
				{type:"string"	, name:'id'			, index:'id'			, width:"10px"	}
				,{type:"string"	, name:'boardType'	, index:'boardType'		, width:"20%"	, align:"center"}
				,{type:"string"	, name:'boardTitle'	, index:'boardTitle'	, width:"40%"	, align:"center"}		
				,{type:"string"	, name:'regDate'	, index:'regDate'		, width:"30%"	, sortable:false}	
			]
		, sortNm : "id"
		, sort : "DESC"
		, rowClick	: {color:"#ff9716c2", retFunc : move}
		, gridSearch : "search"
	});
	

	document.getElementById('btn_reg').onclick = function () {
		let p = {
			id : 'testReg'
			, url : "/bo/board/p/noticeReg"
		}
		LibUtil.openPopup(p);
	};
}
function move(idx, data){
	
	//location.href = "/bo/board/noticeReg?id="+data.id;
}
</script>

<div class="article_right">
	<h3  class="article_tit">공지사항</h3>
	<div class="k_search" id="search">
		<table class="searchbx">
			<colgroup>
				<col width="9%">
				<col width="13%">
				<col width="9%">
				<col width="13%">
				<col width="9%">
				<col width="13%">
				<col width="9%">
				<col width="13%">
			</colgroup>
			<tbody>
				<tr>
					<td>
						<p class="red">제목</p>
					</td>
					<td><input type="text" name="boardTitle" class="" value=""></td>
					<td>
						<p>타입</p>
					</td>
					<td><input type="text" name="boardType" class=""></td>
					<td>
						<p>내용</p>
					</td>
					<td><input type="text" name="boardCnts" class=""></td>
				</tr>
			</tbody>
		</table>
		<div class="btnbx">
			<button type="button" class="btn btn_home gridSearch">조회</button>
			<a class="btn btn_home" href="/bo/board/noticeReg">등록</a>
			<a class="btn btn_home" id="btn_reg" >등록2</a>
		</div>
	</div>
	<div id="grid"></div>
</div>