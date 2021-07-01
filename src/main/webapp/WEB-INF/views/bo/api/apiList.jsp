<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var apiGrid = Object.create(GRID);

function pageLoad(){
	apiGrid.set({
		  id			: "apiGrid"
  		, url			: "/system/code/codeMstList"
	    , width			: "100%"
  		, headCol		: ["등록일", "토큰"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"20%"		, align:"center"}
				,{type:"string"	, name:'key'		, index:'key'			, width:"20%"		, align:"center"}
			]
		, sortNm 		: "ins_dt"
		, sort 			: "DESC"
		, gridSearch 	: "searchDiv,searchBtn"
		, isPaging 		: true					
		, size 			: 10
	});
	
	//datepicker
	$("#date_cal01").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	:function(dateText1,inst) {
			$("#srchDate1").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal02").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	:function(dateText1,inst) {
			$("#srchDate2").val(dateText1);
			$(this).hide();
		}
	});
	
	
	
	$("#apiCode").on("click", function(){
		var p = {
			  url		: "/system/api/getApiCode"	
			, param		: {
				code	: ""
			}
			, success 	: function (opt,result) {
				console.log("##########", result);
				console.log("결과값 ===" + JSON.stringify(result));
				$("#resultCode").val(result.data.authorize_code);
		    }
		}
		AjaxUtil.post(p);
	});
	
	$("#apiKey").on("click", function(){
		var p = {
				  url		: "/system/api/getAuthToken"	
				, param		: {
					code	: 	$("#resultCode").val()
				}
				, success 	: function (opt,result) {
					console.log("##########", result);
					console.log("결과값 ===" + JSON.stringify(result));
			    }
			}
			AjaxUtil.post(p);
	});
	
	$("#apiHealthCheck").on("click", function(){
		var p = {
				  url		: "/system/api/getHealthCheck"	
				, param		: {}
				, success 	: function (opt,result) {
					alert(result.data);
			    }
			}
			AjaxUtil.post(p);
	});
	
	
	
	
}

//날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>API관리</h2>
		</div>
		<div class="info_box k_search" id="searchDiv">
			<input type="hidden" id="resultCode" />
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
					<th>등록일</th>
					<td colspan="6" class="long_input">
						<div class="input_wrap">
                			<input type="text" name="srchDate1" id="srchDate1" class="input_calendar" readonly="readonly">
                			<a class="calendar_ico" onclick="$('#date_cal01').show();"></a>
						 	<div id="date_cal01" class="calendar01"></div>
              			</div>
					  	~
					 	<div class="input_wrap mgr5">
							<input type="text" name="srchDate2" id="srchDate2" class="input_calendar" readonly="readonly">
							<a class="calendar_ico" onclick="$('#date_cal02').show();"></a>
							<div id="date_cal02" class="calendar01"></div>
						</div>
						<div class="date_btn">
							<a href="javascript:void(0);" onclick="goGetDate('today');">오늘</a>
							<a href="javascript:void(0);" onclick="goGetDate('1');">어제</a>
							<a href="javascript:void(0);" onclick="goGetDate('7');">1주일</a>
							<a href="javascript:void(0);" onclick="goGetDate('15');">15일</a>
							<a href="javascript:void(0);" onclick="goGetDate('oneMonthAgo');">1개월</a>
						</div>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiCode">코드조회</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiKey">key조회</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiHealthCheck">서버상태 확인</a>
			</div>
		</div>
		<div id="apiGrid" class="long_table"></div>
	</div>
</div>
