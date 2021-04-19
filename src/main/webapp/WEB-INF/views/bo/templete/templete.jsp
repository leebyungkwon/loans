<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

var Grid = Object.create(GRID); 

function pageLoad(){
	var jpa_url = "/system/templete/listJpa";
	var mybatis_url = "/system/templete/list";
	var url = mybatis_url;
	Grid.set({
		  id		: "grid"
  		, url		: url
	    , width		: "100%"
	    , check		: true
  		, headCol	: ["번호", "타입", "제목", "등록시간", "첨부"]
  		, bodyCol	: 
  			[
				{type:"string"	, name:'boardNo'	, index:'boardNo'		, width:"10px"	, id:true}
				,{type:"string"	, name:'boardType'	, index:'boardType'		, width:"20%"	, align:"center"}
				,{type:"string"	, name:'boardTitle'	, index:'boardTitle'	, width:"40%"	, align:"center"}		
				,{type:"string"	, name:'regDate'	, index:'regDate'		, width:"30%"	, sortable:false}
				,{type:"string"	, name:'attchNo3'	, index:'attchNo3'		, width:"30%"	, sortable:false}	
			]
		, sortNm : "board_no"
		, sort : "DESC"
		, rowClick	: {/* color:"#ccc",  */retFunc : detail}
		, gridSearch : "search"
		, isPaging : true
		, excel : "/system/templete/excelDown"
	});
	
	document.getElementById('btn_pop1').onclick = function () {
		let p = {
			id : 'testReg'
			, url : "/system/templete/p/templeteSave"
		}
		LibUtil.openPopup(p);
	};
	document.getElementById('btn_pop2').onclick = function () {
		let p = {
			id : 'testReg'
			, url : "/system/templete/p/templeteFileSave"
		}
		LibUtil.openPopup(p);
	};
	document.getElementById('btn_page').onclick = function () {
		location.href = "/system/board/noticeReg";
	};
}
function detail(idx, data){
	location.href = "/system/templete/templeteSave?id="+data.id;
}
function detailPop(idx, data){
	let p = {
		id : "testReg"
		, data : data
		, url : "/system/templete/p/templeteSave"
	}
	LibUtil.openPopup(p);
}
</script>

<div class="article_right">
	<h3  class="article_tit">템플릿 1</h3>
	<div class="k_search" id="search">
		<table class="searchbx" style="width:60%">
			<colgroup>
				<col width="5%">
				<col width="10%">
				<col width="5%">
				<col width="10%">
				<col width="5%">
				<col width="10%">
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
			<div>
			<button type="button" class="btn btn_home gridSearch">조회</button>
			</div>
			<div>
			<button type="button" class="btn btn_home" id="btn_page" >[페이지이동]등록</button>
			<button type="button" class="btn btn_home" id="btn_pop1" >[레이어]등록</button>
			<button type="button" class="btn btn_home" id="btn_pop2" >[레이어]파일등록</button>
			</div>
		</div>
	</div>
	<div id="grid"></div>
</div>