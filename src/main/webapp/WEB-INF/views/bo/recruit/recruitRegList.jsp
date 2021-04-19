<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

var Grid = Object.create(GRID); 

function pageLoad(){
	Grid.set({
		  id		: "grid"
  		, url		: "/bo/manage/excelList"
	    , width		: "100%"
  		, headCol	: ["번호", "제목", "설명", "문서명", "등록일"]
  		, bodyCol	: 
  			[
				{type:"string"	, name:'excelMstId'	, index:'excelMstId'	, width:"5%"	}
				,{type:"string"	, name:'excelTitle'	, index:'excelTitle'	, width:"20%"	, align:"center"}
				,{type:"string"	, name:'excelDesc'	, index:'excelDesc'		, width:"35%"	, align:"center"}		
				,{type:"string"	, name:'docNm'		, index:'docNm'			, width:"20%"	, align:"center"}	
				,{type:"string"	, name:'regDate'	, index:'regDate'		, width:"15%"	, sortable:false}	
			]
		, sortNm : "excelMstId"
		, sort : "DESC"
		, rowClick	: {color:"#ccc", retFunc : detailPop}
		, gridSearch : "search"
	});
	
	document.getElementById('btn_pop').onclick = function () {
		let p = {
			id : 'excel_save'
			, url : "/bo/manage/p/excelSave"
		}
		LibUtil.openPopup(p);
	};
	

	var grpCode = {
		useCode : true
		,code : 'RECR_TYPE'
		,target : '#cntsTyp'
	};
	console.log(grpCode);
	DataUtil.selectBox(grpCode);
}
function detailPop(idx, data){
	let p = {
		id : "excel_save"
		, data : data
		, url : "/bo/manage/p/excelSave"
	}
	LibUtil.openPopup(p);
}
</script>

<div class="article_right">
	<h3  class="article_tit">모집인 등록</h3>
	<div class="k_search" id="search">
		<table class="searchbx" style="width:70%">
			<colgroup>
				<col width="10%">
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
					<td colspan="5"><input type="text" name="title" class="" value="" style="width:500px;"/></td>
				</tr>
			</tbody>
		</table>
		<div class="btnbx">
			<div>
			<button type="button" class="btn btn_home gridSearch">조회</button>
			<button type="button" class="btn btn_home" id="btn_pop" >등록</button>
			</div>
		</div>
	</div>
	<div id="grid"></div>
</div>