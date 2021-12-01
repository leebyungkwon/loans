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
		, sortNm 		: "reg_timestamp"
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
	
	// 법인저장
	$("#insertCorp").on("click", function(){
		var p = {
			  url		: "/admin/corp/insertCheckCorp"	
			, param		: {}
			, success 	: function (opt,result) {
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
					alert("토큰결과값 :: " + JSON.stringify(result));
				}else{
					if(result.data == null){
						alert("심각한 오류가 발생하였습니다");
					}else{
						alert(result.data.resMsg);	
					}
				}
		    }
		}
		AjaxUtil.post(p);
	});
	
	
	// 토큰재발급
	$("#apiTokenReInsert").on("click", function(){
		var p = {
			  url		: "/system/api/getReAuthToken"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				if(result.code == "success"){
					alert("토큰결과값 :: " + JSON.stringify(result));
				}else{
					if(result.data == null){
						alert("심각한 오류가 발생하였습니다");
					}else{
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
				, plClass	: plClass
			}
			, url : "/system/api/apiPreSearchPopup"
			, success : function (opt,result) {
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
				$(".popup_inner").css("width","55%");
		    }
		}
		PopUtil.openPopup(p);
	});
	
	
	// 위반이력 조회
	$("#vioSearch").on("click", function(){
		var plMZId = $("#ssn").val();
		
		let p = {
			id : "vioSearchPop"
			, params : {
				plMZId	: plMZId
			}
			, url : "/system/api/vioSearchPopup"
			, success : function (opt,result) {
				$(".popup_inner").css("width","55%");
		    }
		}
		PopUtil.openPopup(p);
	});
	
	
	// API수동배치
	$("#apiBatch").on("click", function(){
		var p = {
			  url		: "/system/api/apiBatch"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				
				console.log("결과값 :: " , result);
				
		    }
		}
		AjaxUtil.post(p);
	});
	
	
	// API수동본등록
	$("#apiReg").on("click", function(){
		var preLcNum = $("#regPreLcNum").val();
		if(WebUtil.isNull(preLcNum)){
			alert("가등록번호를 입력해 주세요.");
			return false;
		}
		var p = {
			  url		: "/system/api/apiReg"	
			, param		: {
				preLcNum	:	preLcNum
			}
			, success 	: function (opt,result) {
				console.log("?????", result);
		    }
		}
		AjaxUtil.post(p);
	});
	
 
	
	// 승인완료 자격취득 - 개인
	$("#apiIndvApplyReg").on("click", function(){
		var p = {
			  url		: "/system/api/apiIndvApplyReg"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				
				console.log("결과값 :: " , result);
				
		    }
		}
		AjaxUtil.post(p);
	});
	
	// 승인완료 자격취득 - 법인
	$("#apiCorpApplyReg").on("click", function(){
		var p = {
			  url		: "/system/api/apiCorpApplyReg"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				
				console.log("결과값 :: " , result);
				
		    }
		}
		AjaxUtil.post(p);
	});
	
	
	
	// 기등록자자격취득
	$("#apiPreRegApplyReg").on("click", function(){
		var p = {
			  url		: "/system/api/apiPreRegApplyReg"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				
				console.log("결과값 :: " , result);
				
		    }
		}
		AjaxUtil.post(p);
	});
	
	// 가등록 삭제
	$("#deletePreLoan").on("click", function(){
		var plClass			=	$("#delPlClass").val();
		var preLcNum		=	$("#delPreLcNum").val();
		
		console.log("plClass = " + plClass);
		console.log("preLcNum = " + preLcNum);
		
		if(confirm("가등록을 삭제하시겠습니까?")){
			var p = {
				  url		: "/system/api/deletePreLoan"	
				, param : {
					  plClass	: plClass
					, preLcNum	: preLcNum
				}
				, success 	: function (opt,result) {
					
					
			    }
			}
			AjaxUtil.post(p);
		}
	});
	
	
	// 계약번호취득
	$("#apiConNumReg").on("click", function(){
		var p = {
			  url		: "/system/api/apiConNumReg"	
			, param		: {
				
			}
			, success 	: function (opt,result) {
				
				console.log("결과값 :: " , result);
				
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
					<th>주민번호</th>
					<td class="">
						<input type="text" id="ssn" >
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="vioSearch">위반이력조회</a>
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
						<input type="text" id="delPreLcNum" >
					</td>
					<th>모집인 분류</th>
					<td class="half_input">
						<select id="delPlClass">
							<option value="1">개인</option>
							<option value="2">법인</option>
						</select>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="deletePreLoan">가등록삭제</a>
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
						<input type="text" id="regPreLcNum" >
					</td>
					<th></th>
					<td class="" colspan="3">
						상태가 자격취득이고 등록번호가 없는 케이스의 수동 API등록
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="apiReg">수동본등록</a>
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
					<th></th>
					<td class="">
						
					</td>
					<th></th>
					<td class="" colspan="3">
						#결제완료, 자격취득 상태인데 등록번호가 없는 케이스에 등록번호 넣어주기(전체)
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="apiBatch">본등록수동배치</a>
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
					<th></th>
					<td class="" colspan="3">
						승인완료 -> 자격취득(결제한 내역이 있을경우 - 개인) 
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" style="width:180px;" class="btn_inquiry" id="apiIndvApplyReg">(개인)승인완료자격취득</a>
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
					<th></th>
					<td class="" colspan="3">
						승인완료 -> 자격취득(결제한 내역이 있을경우 - 법인)
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" style="width:180px;" class="btn_inquiry" id="apiCorpApplyReg">(법인)승인완료자격취득</a>
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
					<th></th>
					<td class="" colspan="3">
						승인완료이면서 기등록자인 경우 자격취득(개인/법인 전체)
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" style="width:180px;" class="btn_inquiry" id="apiPreRegApplyReg">기등록자자격취득</a>
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
					<th></th>
					<td class="" colspan="3">
						등록번호가 존재하지만 계약번호가 없는케이스 자격취득(승인완료/자격취득/결제완료) pl_stat='9'
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" style="width:180px;" class="btn_inquiry" id="apiConNumReg">계약번호취득</a>
		</div>
		
		

	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<!-- <a href="javascript:void(0);" class="btn_black btn_small mgr5" id="insertCorp">법인등록번호저장</a> -->
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiHealthCheck">서버상태 확인</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiCode">코드조회</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiToken">토큰조회</a>
				<!-- <a href="javascript:void(0);" class="btn_black btn_small mgr5" id="apiTokenReInsert">토큰재발급</a> -->
			</div>
		</div>
		<div id="apiGrid" class="long_table"></div>
	</div>
</div>
