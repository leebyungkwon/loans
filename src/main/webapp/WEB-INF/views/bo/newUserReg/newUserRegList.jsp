<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var newUserRegGrid = Object.create(GRID);

function pageLoad(){
	//모집인 등록 그리드
	newUserRegGrid.set({
		  id			: "newUserRegGrid"
  		, url			: "/member/newUser/newUserRegList"
	    , width			: "100%"
  		, headCol		: ["", "접수번호", "", "모집인<br>분류", "법인사용인<br>여부", "취급상품", "이름", "주민번호", "휴대폰번호", "법인명", "법인번호", "등록일", "", "승인상태"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"10px"		, id:true		 , hidden:true}
				,{type:"string"	, name:'masterToId'		, index:'masterToId'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'			, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'			, width:"8%"		, align:"center"}
				,{type:"string"	, name:'corpUserYn'		, index:'corpUserYn'		, width:"9%"		, align:"center"}
				,{type:"string"	, name:'plProductNm'	, index:'plProductNm'		, width:"8%"		, align:"center"}
				,{type:"string"	, name:'plMName'		, index:'plMName'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMZId'			, index:'plMZId'			, width:"16%"		, align:"center"}
				,{type:"string"	, name:'plCellphone'	, index:'plCellphone'		, width:"17%"		, align:"center"}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'	, index:'plMerchantNo'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'regTimestamp'	, index:'regTimestamp'		, width:"13%"		, align:"center"}
				,{type:"string"	, name:'plStat'			, index:'plStat'			, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plStatNm'		, index:'plStatNm'			, width:"8%"		, align:"center"}
			]
		//, sortNm 		: "master_seq"
		//, sort 		: "DESC"
		, rowClick		: {retFunc : goNewUserRegDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
	});
	
	//처리상태
 	var plStatCode = {
		 useCode 	: true
		,code 		: 'MAS001'
		,target 	: '#plStat'
		,property01 : 'Y'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plStatCode);
	
	//모집인 분류
	var plClassCode = {
		 useCode 	: true
		,code 		: 'CLS001'
		,target 	: '#plClass'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plClassCode);
	
	//취급상품
	var plProductCode = {
		 useCode 	: true
		,code 		: 'PRD001'
		,target 	: '#plProduct'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plProductCode);
	
	//datepicker
	$("#date_cal01").datepicker({
		 dateFormat	: "yy-mm-dd"
	 	,changeMonth: true
		,changeYear	: true
		,onSelect	: function(dateText1,inst) {
			$("#srchDate1").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal02").datepicker({
		 dateFormat	: "yy-mm-dd"
	 	,changeMonth: true
		,changeYear	: true
		,onSelect	: function(dateText1,inst) {
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

//모집인 등록 row 클릭 이벤트
function goNewUserRegDetail(idx, data){
	var masterSeq 	= newUserRegGrid.gridData[idx].masterSeq;
	var plClass		= newUserRegGrid.gridData[idx].plClass;
	
	if(plClass == "1"){
		//개인
		$("#userRegDetailFrm").attr("action","/member/newUser/newUserRegIndvDetail");
	}else if(plClass == "2"){
		//법인
		$("#userRegDetailFrm").attr("action","/member/newUser/newUserRegCorpDetail");
	}
	
	$("#hMasterSeq").val(masterSeq);
	$("#userRegDetailFrm").submit();
}

//날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}
</script>

<form id="userRegDetailFrm" method="post">
	<input type="hidden" name="masterSeq" id="hMasterSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>모집인 등록</h2>
		</div>
		<div class="info_box k_search" id="searchDiv">
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
					<th>모집인 분류</th>
					<td class="half_input">
						<select name="plClass" id="plClass"></select>
					</td>
					<th>취급상품</th>
					<td class="half_input">
						<select name="plProduct" id="plProduct"></select>
					</td>
					<th>승인상태</th>
					<td class="half_input">
						<select name="plStat" id="plStat"></select>
					</td>
				</tr>
				<tr>
					<th>이름</th>
					<td class="">
						<input type="text" name="plMName">
					</td>
					<th>주민등록번호</th>
					<td class="">
						<input type="text" name="plMZId" placeholder="-를 빼고 입력해주세요.">
					</td>
					<th>휴대폰번호</th>
					<td class="">
						<input type="text" name="plCellphone">
					</td>
				</tr>
				<tr>
					<th>법인명</th>
					<td class="">
						<input type="text" name="plMerchantName">
					</td>
					<th>법인번호</th>
					<td class="">
						<input type="text" name="plMerchantNo" placeholder="-를 빼고 입력해주세요.">
					</td>
				</tr>
				<tr>
					<th>접수번호</th>
					<td class="">
						<input type="text" name="masterToId">
					</td>
				</tr>
				<tr>
					<th>등록기간 조회</th>
					<td colspan="5" class="long_input">
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
				<input type="hidden" id="regPath" name="regPath" value="F" />
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
			</div>
		</div>
		<div id="newUserRegGrid" class="long_table"></div>
	</div>
</div>
