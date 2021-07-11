<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var apiGrid = Object.create(GRID);

function pageLoad(){
	apiGrid.set({
		  id			: "apiGrid"
  		, url			: "/system/api/selectApiList"
	    , width			: "100%"
  		, headCol		: ["등록일", "토큰", "시스템등록일시"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'insDt'			, index:'insDt'			, width:"20%"		, align:"center"}
				,{type:"string"	, name:'token'			, index:'token'			, width:"20%"		, align:"center"}
				,{type:"string"	, name:'regTimestamp'	, index:'regTimestamp'	, width:"20%"		, align:"center"}
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
				alert("권한코드 :: " + result.data.authorize_code);
		    }
		}
		AjaxUtil.post(p);
	});
	
	$("#apiToken").on("click", function(){
		var p = {
			  url		: "/system/api/getAuthToken"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				console.log("##########", result);
				console.log("결과값 ===" + JSON.stringify(result));
				alert("토큰결과값 :: " + JSON.stringify(result));
		    }
		}
		AjaxUtil.post(p);
	});
	
	$("#apiHealthCheck").on("click", function(){
		var p = {
			  url		: "/system/api/getHealthCheck"	
			, param		: {}
			, success 	: function (opt,result) {
				console.log("결과값 ===" + JSON.stringify(result));
				console.log("???? == ", result);
		    }
		}
		AjaxUtil.post(p);
	});
	
	//개인 등록가능 여부 조회 테스트
	$("#loanCheckTest").on("click", function(){
		var p = {
			  url		: "/system/api/loanCheckTest"	
			, param		: {}
			, success 	: function (opt,result) {
				console.log("결과값 ===" + JSON.stringify(result));
				console.log("loanCheckTest :: "+result);
				alert(result.data);
		    }
		}
		AjaxUtil.post(p);
	});
	
	//개인 등록가능 여부 조회 테스트
	$("#loanRegTest").on("click", function(){
		var p = {
			  url		: "/system/api/loanRegTest"	
			, param		: {}
			, success 	: function (opt,result) {
				console.log("결과값 ===" + JSON.stringify(result));
				console.log("loanRegTest :: "+result);
				alert(result.data);
		    }
		}
		AjaxUtil.post(p);
	});
	
	
	
	
	
	
	// 2021-07-11 가등록조회
	$("#apiPopup").on("click", function(){
		
		var preLcNum = $("#preLcNum").val();
		var lcNum = $("#lcNum").val();
		
		
		
		let p = {
			id : "apiPopup"
			, params : {
				searchPreLcNum	:	preLcNum
				searchLcNum		:	lcNum
			}
			, url : "/admin/api/apiPopup"
			, success : function (opt,result) {
				
				
				console.log("팝업실행");
				$(".popup_inner").css("width","55%");
				
		    }
		}
		PopUtil.openPopup(p);
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
	
	
	<div>
		<span>
			가등록번호 : <input type="text" id="preLcNum" value="" />
		</span>
		<span>
			등록번호 : <input type="text" id="lcNum" value="" />
		</span>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiCode">코드조회</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiToken">토큰조회</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiHealthCheck">서버상태 확인</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="loanCheckTest">개인등록여부조회</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="loanRegTest">가등록테스트</a>
				
				
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiPopup">팝업테스트</a>
				
			</div>
		</div>
		<div id="apiGrid" class="long_table"></div>
	</div>
</div>
