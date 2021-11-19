<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style>
span.txt {
    font: 900 14px/36px 'dotum','맑은 고딕';
    text-align: center;
    width: 120px;
    }
</style>
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
	
	
	$("#updateBtn").on("click", function(){
		var p = {
			  url		: "/batch/refreshBatch"	
			, param		: {
				scheduleSeq 	: $("#scheduleSeq").val()
				,param 	: $("#param").val()
			}
			, success 	: function (opt,result) {
				grid1.refresh();
				$("#scheduleSeq").val("");
				$("#param").val("");
		    }
		}
		AjaxUtil.post(p);
	});
	
}
function setBatchList(name){
	$("#scheduleName").val(name);
	grid1.set({
		  id			: "grid1"
  		, url			: "/batch/batchErr"
	    , width			: "120%"
  		, headCol		: ["key","스케줄명", "상태","등록시간","시작시간","에러", "파라미터"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'scheduleSeq'	, index:'scheduleSeq'		, width:"10%"		, align:"center", hidden:true}
				,{type:"string"	, name:'scheduleName'	, index:'scheduleName'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'status'			, index:'status'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'regTime'		, index:'regTime'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'startTime'		, index:'startTime'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'error'			, index:'error'				, width:"30%"		, align:"left"}
				,{type:"string"	, name:'param'			, index:'param'				, width:"30%"		, align:"left"}
			]
		, sortNm 		: "reg_timestamp"
		, sort 			: "DESC"
		, rowClick		: {retFunc : updateBatch}
		, gridSearch 	: "searchDiv,searchBtn"
	});
	
}

function updateBatch(idx, data){
	var scheduleSeq 	= grid1.gridData[idx].scheduleSeq;
	var param		= grid1.gridData[idx].param;
	$("#scheduleSeq").val(scheduleSeq);
	$("#param").val(param);
	
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
		<a href="javascript:setBatchList('')" class="btn_in">전체</a>
		<c:forEach var="list" items="${batchList}">
			<a href="javascript:setBatchList('${list.scheduleName}')" class="btn_in">${list.scheduleName}</a>
		</c:forEach>
		</div>
	</div>

	<div class="top_box">
		<div class="info_box k_search" style="height: auto;overflow: hidden;">
			<span class="txt">SCHEDULE SEQ </span> <input type="text" name="scheduleSeq" id="scheduleSeq"  style="width:50px;height:35px;" readonly=true>
			<span class="txt" style="margin-left:20px;"> 파라미터 </span> <input type="text" name="param" id="param" style="width:800px;height:35px;"> 
			<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="updateBtn" style="margin-left: 20px;">수정</a>
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
