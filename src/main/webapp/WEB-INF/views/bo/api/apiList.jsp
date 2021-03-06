<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<style>
#tbl_apiGrid_body{word-break: break-all;}
</style>

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
					apiGrid.refresh();
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
	
	// 개인 등록가능 여부조회
	$("#indvCheckLoan").on("click", function(){
		var plMName		= $("#plMName").val();
		var plMZId		= $("#plMZId").val();
		var plProduct	= $("#plProduct").val();
		var ci			= $("#ci").val();
		
		if(WebUtil.isNull(plMName)){
			alert("이름을 입력해 주세요.");
			return false;
		}
		if(WebUtil.isNull(plMZId)){
			alert("주민등록번호를 입력해 주세요.");
			return false;
		}
		if(WebUtil.isNull(plProduct)){
			alert("금융상품유형을 선택해 주세요.");
			return false;
		}
		if(WebUtil.isNull(ci)){
			alert("CI를 입력해 주세요.");
			return false;
		}
		
		let p = {
			id : "indvCheckLoan"
			, params : {
				plMName		: plMName
				, plMZId	: plMZId
				, plProduct	: plProduct
				, ci		: ci
				, plClass	: "1"
			}
			, url : "/system/api/checkLoan"
			, success : function (opt,result) {
				$(".popup_inner").css("width","55%");
		    }
		}
		PopUtil.openPopup(p);
	});
	
	// 법인 등록가능 여부조회
	$("#corpCheckLoan").on("click", function(){
		var plMerchantNo	= $("#plMerchantNo").val();
		var plMZId			= $("#repPlMZId").val();
		var plProduct		= $("#corpPlProduct").val();
		var ci				= $("#repCi").val();
		
		if(WebUtil.isNull(plMerchantNo)){
			alert("법인번호를 입력해 주세요.");
			return false;
		}
		if(WebUtil.isNull(plMZId)){
			alert("주민등록번호를 입력해 주세요.");
			return false;
		}
		if(WebUtil.isNull(plProduct)){
			alert("금융상품유형을 선택해 주세요.");
			return false;
		}
		if(WebUtil.isNull(ci)){
			alert("CI를 입력해 주세요.");
			return false;
		}
		
		let p = {
			id : "corpCheckLoan"
			, params : {
				plMerchantNo	: plMerchantNo
				, plMZId		: plMZId
				, plProduct		: plProduct
				, ci			: ci
				, plClass		: "2"
			}
			, url : "/system/api/checkLoan"
			, success : function (opt,result) {
				$(".popup_inner").css("width","55%");
		    }
		}
		PopUtil.openPopup(p);
	});
	
	// API수동가등록
	$("#apiPreReg").on("click", function(){
		var masterSeq = $("#masterSeq").val();
		if(WebUtil.isNull(masterSeq)){
			alert("시퀀스를 입력해 주세요.");
			return false;
		}
		var p = {
			  url		: "/system/api/apiPreReg"	
			, param		: {
				masterSeq	:	masterSeq
			}
			, success 	: function (opt,result) {
				console.log("?????", result);
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
	
	// TM가등록 및 본등록
	$("#apiTm").on("click", function(){
		var p = {
			url		: "/system/api/apiTmReg"	
			, param : {
				
			}
			, success 	: function (opt,result) {
				
		    }
		}
		AjaxUtil.post(p);
	});
	
	// 승인완료 계약건 기등록 조회
	$("#preRegContSearch").on("click", function(){
		var size = 0;
		if(WebUtil.isNotNull($("#size").val())){
			size = $("#size").val();
		}
		var p = {
			url		: "/system/api/preRegContSearch"	
			, param : {
				 sort 		: $("#sortName").val() + "," + $("#sort").val()
				,size 		: size
			}
			, success 	: function (opt,result) {
				
		    }
		}
		AjaxUtil.post(p);
	});
	
	// 암호화
	$("#crypto").on("click", function(){
		var srchDate1 =	$("#cryptoval").val();
		if(confirm("암호화 하시겠습니까?")){
			var p = {
					url		: "/system/crypt/crypt"	
				, param : {
					srchDate1	: srchDate1
				}
				, success 	: function (opt,result) {
					$("#cryptoResultVal").val(result.data.code);
			    }
			}
			AjaxUtil.post(p);
		}
	});
	
	// 복호화
	$("#decrypto").on("click", function(){
		var srchDate1 =	$("#cryptoval").val();
		if(confirm("복호화 하시겠습니까?")){
			var p = {
				  url		: "/system/crypt/decrypt"	
				, param : {
					srchDate1	: srchDate1
				}
				, success 	: function (opt,result) {
					$("#cryptoResultVal").val(result.data.code);
			    }
			}
			AjaxUtil.post(p);
		}
	});
	
	//비밀번호 암호화
	$("#cryptoPwd").on("click", function(){
		var cryptoPwdVal =	$("#cryptoPwdVal").val();
		
		if(WebUtil.isNull(cryptoPwdVal)){
			alert("암호화할 비밀번호를 입력해 주세요.");
			$("#cryptoPwdVal").focus();
			return false;
		}
		if(confirm("암호화 하시겠습니까?")){
			var p = {
					url		: "/system/crypt/cryptPwd"	
				, param : {
					srchDate1	: cryptoPwdVal
				}
				, success 	: function (opt,result) {
					$("#cryptoPwdResultVal").val(result.data.code);
			    }
			}
			AjaxUtil.post(p);
		}
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
					<th>이름</th>
					<td class="">
						<input type="text" id="plMName" >
					</td>
					<th>주민등록번호</th>
					<td class="">
						<input type="text" id="plMZId" placeholder="- 제외">
					</td>
					<th>금융상품유형</th>
					<td class="half_input">
						<select id="plProduct">
							<option value="01">대출</option>
							<option value="05">리스할부</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>CI</th>
					<td class="" colspan="5">
						<input type="text" id="ci" >
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" style="width:160px;" id="indvCheckLoan">(개인)등록가능 여부조회</a>
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
					<th>법인번호</th>
					<td class="">
						<input type="text" id="plMerchantNo" >
					</td>
					<th>대표자 주민등록번호</th>
					<td class="">
						<input type="text" id="repPlMZId" placeholder="- 제외">
					</td>
					<th>금융상품유형</th>
					<td class="half_input">
						<select id="corpPlProduct">
							<option value="01">대출</option>
							<option value="03">TM대출</option>
							<option value="05">리스할부</option>
							<option value="06">TM리스</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>CI</th>
					<td class="" colspan="5">
						<input type="text" id="repCi" >
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" style="width:160px;" id="corpCheckLoan">(법인)등록가능 여부조회</a>
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
					<th>master_seq</th>
					<td class="">
						<input type="text" id="masterSeq" >
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="apiPreReg">수동가등록</a>
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
					<td class="" colspan="6">
						결제완료, 자격취득 상태인데 등록번호가 없는 케이스에 등록번호 넣어주기(전체)
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
						등록번호가 존재하지만 계약번호가 없는케이스 자격취득<br>(모집인 상태가 승인완료/자격취득/결제완료이고, 처리상태가 완료인 건)
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" style="width:180px;" class="btn_inquiry" id="apiConNumReg">계약번호취득</a>
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
						금융상품유형이 TM인 계약건 등록번호 및 계약번호 넣어주기(결제한 내역이 있을경우)
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" style="width:180px;" class="btn_inquiry" id="apiTm">TM가등록 및 본등록</a>
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
					<th>order by</th>
					<td class=""> <!-- half_input -->
						<input type="text" id="sortName" style="width: 100px;">
						<select id="sort" style="width: 65px;">
							<option value="ASC">ASC</option>
							<option value="DESC">DESC</option>
						</select>
					</td>
					<th>limit</th>
					<td class="">
						<input type="text" id="size">
					</td>
				</tr>
				<tr>
					<th></th>
					<td class="" colspan="6">
						금융상품유형이 TM이 아니고 승인완료,완료인 계약건 조회 후 배치 테이블 저장,기등록 건일 때 자격취득(조건 미 입력 시 전제 건 저장됨)
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" style="width:180px;" class="btn_inquiry" id="preRegContSearch">승인완료 계약건 저장</a>
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
					<th>변환할데이터</th>
					<td class="">
						<input type="text" id="cryptoval" >
					</td>
					
					<th>결과</th>
					<td class="">
						<input type="text" id="cryptoResultVal" >
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" style="margin-right: 120px;" id="crypto">암호화</a>
			<a href="javascript:void(0);" class="btn_inquiry" id="decrypto">복호화</a>
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
					<th>변환할 비밀번호</th>
					<td class="">
						<input type="text" id="cryptoPwdVal" >
					</td>
					
					<th>결과</th>
					<td class="">
						<input type="text" id="cryptoPwdResultVal" >
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" style="margin-right: 120px;" id="cryptoPwd">암호화</a>
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
				<a href="javascript:void(0);" class="btn_blue btn_small mgr5" id="apiTokenReInsert">토큰재발급</a>
			</div>
		</div>
		<div id="apiGrid" class="long_table"></div>
	</div>
</div>
