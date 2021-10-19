<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
var inactiveGrid = Object.create(GRID);

function pageLoad(){
	inactiveGrid.set({
		  id			: "inactiveGrid"
		, url			: "/admin/inactive/inactiveList"
	    , width			: "100%" 
	    , check			: true
		, headCol		: ["","아이디", "이름", "연락처", "구분", "이메일", "가입일", "휴면회원</br>전환일", "마지막</br>로그인일시"]
		, bodyCol		: 
			[
				 {type:"string"	, name:'userSeq'		, index:'userSeq'		, width:"5%"	, hidden:true  	, id:true}
				,{type:"string"	, name:'userId'			, index:'userId'		, width:"15%"	}
				,{type:"string"	, name:'userName'		, index:'userName'		, width:"10%"	}
				,{type:"string"	, name:'mobileNo'		, index:'mobileNo'		, width:"12%"	, align:"center"}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'		, width:"8%"	, align:"center"}
				,{type:"string"	, name:'email'			, index:'email'			, width:"15%"	}
				,{type:"string"	, name:'joinDt'			, index:'joinDt'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'inactiveDt'		, index:'inactiveDt'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'lastLoginDt'	, index:'lastLoginDt'	, width:"12%"	, align:"center"}
			]
		, rowClick		: {color:"#ccc", retFunc : inactiveDetail}
		, gridSearch 	: "search,searchBtn"
		, excel 		: "/admin/inactive/inactiveExcelListDown"
		, excelFileNm	: "휴면회원관리"
		, isPaging 		: true
		, size 			: 10
	});
	
	// 모집인 분류
 	var plClassCode = {
		 useCode 	: true
		,code 		: 'CLS001'
		,target 	: '#plClass'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plClassCode);
	
	
	//datepicker
	$("#date_cal01").datepicker({
		 dateFormat	: "yy-mm-dd"
	 	,changeMonth: true
		,changeYear	: true
		,onSelect	:function(dateText1,inst) {
			$("#srchDate1").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal02").datepicker({
		 dateFormat	: "yy-mm-dd"
	 	,changeMonth: true
		,changeYear	: true
		,onSelect	:function(dateText1,inst) {
			$("#srchDate2").val(dateText1);
			$(this).hide();
		}
	});
	
	
	
	
}

$(document).mouseup(function(e){
	var calendar01 = $(".calendar01");
	if(calendar01.has(e.target).length === 0){
		calendar01.hide();
	}
});


// 날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}

// 회원정보 상세보기
function inactiveDetail(idx, data){
	var userSeq = inactiveGrid.gridData[idx].userSeq;
	$("#userSeq").val(userSeq);
	$("#inactiveDetailFrm").submit();
}


// 로그인 차단 해제
function inactiveUpdate() {
	var chkedLen 	= $("#tbl_inactiveGrid_body tr td input:checkbox:checked").length;
	if(chkedLen == 0){
		alert("회원을 선택해 주세요.");
		return;
	}

	var chkData		= inactiveGrid.getChkData();
	var userSeqArr	= [];
	for(var i=0; i<chkedLen; i++){
		userSeqArr.push(chkData[i].userSeq);
	}
	
	if(confirm("휴면회원을 활성화 하시겠습니까?")){
		var p = {
			  url		: "/admin/inactive/boInactiveUser"	
			, param		: {
				userSeqArr 	: userSeqArr  
			}
			, success 	: function (opt,result) {
				console.log("#####" , result);
				inactiveGrid.refresh();
		    }
		}
		AjaxUtil.post(p);
	}
}

</script>

<form id="inactiveDetailFrm" method="post" action="/admin/inactive/inactiveDetail">
	<input type="hidden" name="userSeq" id="userSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>휴면회원관리</h2>
		</div>
		<div class="info_box k_search" id="search">
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
					<th>아이디</th>
					<td class="half_input">
						<input type="text" name="userId">
					</td>
					<th>이름</th>
					<td class="half_input">
						<input type="text" name="userName">
					</td>
					<th>연락처</th>
					<td class="half_input">
						<input type="text" name="mobileNo">
					</td>
				</tr>
				<tr>
					<th>구분</th>
					<td class="half_input">
						<select name="plClass" id="plClass"></select>
					</td>
					<th>이메일</th>
					<td class="half_input">
						<input type="text" name="email">
					</td>
				</tr>
				<tr>
					<th>전환일</th>
					<td colspan="6" class="long_input">
						<div class="input_wrap">
                			<input type="text" name="srchDate1" id="srchDate1" class="input_calendar" readonly="readonly" onclick="$('#date_cal01').show();">
                			<a class="calendar_ico" onclick="$('#date_cal01').show();"></a>
						 	<div id="date_cal01" class="calendar01"></div>
              			</div>
					  	~
					 	<div class="input_wrap mgr5">
							<input type="text" name="srchDate2" id="srchDate2" class="input_calendar" readonly="readonly" onclick="$('#date_cal02').show();">
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
				<a href="javascript:void(0);" class="btn_gray btn_small mgr5" onclick="inactiveUpdate();">휴면회원활성화</a>
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="$('#excelDown').trigger('click');">다운로드</a>
			</div>
		</div>
		<div id="inactiveGrid" class="long_table"></div>
	</div>
</div>

