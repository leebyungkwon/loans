<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var grid1 = Object.create(GRID);
var grid2 = Object.create(GRID);
function pageLoad(){
	grid2.set({
		  id			: "grid2"
		, url			: "/batch/batchErrHist"
	    , width			: "100%"
		, headCol		: ["스케줄명", "요청건수","성공건수","시작시간", "종료시간"]
		, bodyCol		: 
			[
				 {type:"string"	, name:'scheduleName'	, index:'scheduleName'		, width:"20%"		, align:"center"}
				,{type:"string"	, name:'reqCnt'			, index:'reqCnt'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'successCnt'		, index:'successCnt'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'startTime'		, index:'startTime'			, width:"30%"		, align:"center"}
				,{type:"string"	, name:'endTime'		, index:'endTime'			, width:"30%"		, align:"center"}
			]
		, sortNm 		: "reg_timestamp"
		, sort 			: "DESC"
		, isPaging 		: true					
		, size 			: 100
	});
}
function setBatchList(name){
	$("#scheduleName").val(name);
	grid1.set({
		  id			: "grid1"
  		, url			: "/batch/batchErr"
	    , width			: "120%"
  		, headCol		: ["스케줄명", "상태","시작시간","에러", "파라미터"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'scheduleName'	, index:'scheduleName'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'status'			, index:'status'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'startTime'		, index:'startTime'			, width:"20%"		, align:"center"}
				,{type:"string"	, name:'error'			, index:'error'				, width:"30%"		, align:"left"}
				,{type:"string"	, name:'param'			, index:'param'				, width:"30%"		, align:"left"}
			]
		, sortNm 		: "reg_timestamp"
		, sort 			: "DESC"
		, gridSearch 	: "searchDiv,searchBtn"
	});
	
}



</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>BATCH 오류 이력 관리</h2>
		</div>
	</div>
	<div class="top_box">
		<div class="info_box k_search" style="height: auto;overflow: hidden;">
		<c:forEach var="list" items="${batchList}">
			<a href="javascript:setBatchList('${list.scheduleName}')" class="btn_in">${list.scheduleName}</a>
		</c:forEach>
		</div>
	</div>

	<div class="info_box k_search" id="searchDiv">
		<input type="text" id="scheduleName" name="scheduleName"/>
	</div>
	
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="searchBtn">조회</a>
			</div>
		</div>
		<div id="grid1" class="long_table"></div>
	</div>
	
	<div class="top_box">
		<div class="title">
			<h2>BATCH 오류 일일 이력 관리</h2>
		</div>
	</div>
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
		</div>
		<div id="grid2" class="long_table"></div>
	</div>
</div>
