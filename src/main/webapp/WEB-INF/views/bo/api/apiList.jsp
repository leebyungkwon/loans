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
				 {type:"string"	, name:'insDt'			, index:'insDt'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'token'			, index:'token'			, width:"78%"		, align:"left"}
				,{type:"string"	, name:'regTimestamp'	, index:'regTimestamp'	, width:"12%"		, align:"center"}
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
	 	,changeMonth: true
		,changeYear	: true
		,yearRange	: "c-10:c+10"
		,onSelect	:function(dateText1,inst) {
			$("#srchDate1").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal02").datepicker({
		 dateFormat	: "yy-mm-dd"
	 	,changeMonth: true
		,changeYear	: true
		,yearRange	: "c-10:c+10"
		,onSelect	:function(dateText1,inst) {
			$("#srchDate2").val(dateText1);
			$(this).hide();
		}
	});
	
	
	insertCorp
	
	// 법인저장
	$("#insertCorp").on("click", function(){
		var p = {
			  url		: "/admin/corp/insertCheckCorp"	
			, param		: {}
			, success 	: function (opt,result) {
				console.log("결과값 ===" + JSON.stringify(result));
		    }
		}
		AjaxUtil.post(p);
	});
	
	// 서버상태 확인
	$("#apiHealthCheck").on("click", function(){
		var p = {
			  url		: "/system/api/getHealthCheck"	
			, param		: {}
			, success 	: function (opt,result) {
				console.log("결과값 ===" + JSON.stringify(result));
		    }
		}
		AjaxUtil.post(p);
	});
	
	// 코드조회
	$("#apiCode").on("click", function(){
		var p = {
			  url		: "/system/api/getApiCode"	
			, param		: {
				code	: ""
			}
			, success 	: function (opt,result) {
				console.log("결과값 ===" + JSON.stringify(result));
				alert("권한코드 :: " + result.data.code);
		    }
		}
		AjaxUtil.post(p);
	});
	

	// 토큰조회
	$("#apiToken").on("click", function(){
		var p = {
			  url		: "/system/api/getAuthToken"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				if(result.code == "success"){
					console.log("결과값1 ===" + JSON.stringify(result));
					alert("토큰결과값 :: " + JSON.stringify(result));
				}else{
					if(result.data == null){
						console.log("결과값2 ===" + JSON.stringify(result));
						alert("심각한 오류가 발생하였습니다");
					}else{
						console.log("결과값3 ===" + JSON.stringify(result));
						alert(result.data.resMsg);	
					}
				}
		    }
		}
		AjaxUtil.post(p);
	});
	
	
	
	// 가등록번호 조회 결과 팝업
	$("#apiPreSearch").on("click", function(){
		var plClass		=	$("#prePlClass").val();
		var preLcNum	=	$("#preLcNum").val();
		var conNum		=	$("#conNum").val();
		
		if(WebUtil.isNull(preLcNum)){
			alert("가등록번호를 입력해 주세요.");
			return false;
		}
		
		let p = {
			id : "crefiaRegPop"
			, params : {
				preLcNum	: preLcNum
				, conNum	: conNum
				, plClass	:	plClass
			}
			, url : "/system/api/apiPreSearchPopup"
			, success : function (opt,result) {
				console.log("###팝업 오픈 ###");
				$(".popup_inner").css("width","55%");
		    }
		}
		PopUtil.openPopup(p);
	});
	
	
	// 등록번호 조회 결과 팝업
	$("#apiSearch").on("click", function(){
		var plClass		=	$("#plClass").val();
		var lcNum		=	$("#lcNum").val();
		var conNum		=	$("#conNum").val();
		
		if(WebUtil.isNull(lcNum)){
			alert("등록번호를 입력해 주세요.");
			return false;
		}
		
		let p = {
			id : "crefiaRegPop"
			, params : {
				plRegistNo	: lcNum
				, conNum	: conNum
				, plClass	:	plClass
			}
			, url : "/system/api/apiSearchPopup"
			, success : function (opt,result) {
				console.log("###팝업 오픈 ###");
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
		
		<div class="info_box k_search" >
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
					<th>가등록번호</th>
					<td class="">
						<input type="text" id="preLcNum" >
					</td>
					<th>계약번호</th>
					<td class="">
						<input type="text" id="preConNum" >
					</td>
					<th>모집인 분류</th>
					<td class="half_input">
						<select id="prePlClass">
							<option value="1">개인</option>
							<option value="2">법인</option>
						</select>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="apiPreSearch">가등록조회</a>
		</div>
		
		
		<div class="info_box k_search" >
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
					<th>등록번호</th>
					<td class="">
						<input type="text" id="lcNum" >
					</td>
					
					<th>계약번호</th>
					<td class="">
						<input type="text" id="conNum" >
					</td>
					<th>모집인 분류</th>
					<td class="half_input">
						<select id="plClass">
							<option value="1">개인</option>
							<option value="2">법인</option>
						</select>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="apiSearch">등록조회</a>
		</div>
		
		
		
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="insertCorp">법인등록번호저장</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiHealthCheck">서버상태 확인</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiCode">코드조회</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiToken">토큰조회</a>
			</div>
		</div>
		<div id="apiGrid" class="long_table"></div>
	</div>
</div>
