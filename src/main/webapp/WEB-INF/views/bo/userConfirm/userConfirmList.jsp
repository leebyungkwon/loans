<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var userConfirmGrid = Object.create(GRID);

function pageLoad(){
	//모집인 조회 및 변경 그리드
	userConfirmGrid.set({
		  id			: "userConfirmGrid"
  		, url			: "/member/confirm/userConfirmList"
	    , width			: "100%"
  		, headCol		: ["", "접수번호", "", "모집인<br>상태", "","처리상태", "", "모집인<br>분류", "법인사용인<br>여부", "금융상품<br>유형", "이름", "주민번호", "휴대폰번호", "법인명", "법인번호", "승인<br>완료일", "결제<br>완료일", "자격<br>취득일"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"10px"		, id:true		 , hidden:true}
				,{type:"string"	, name:'masterToId'		, index:'masterToId'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plRegStat'		, index:'plRegStat'			, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plRegStatNm'	, index:'plRegStatNm'		, width:"8%"		, align:"center"}
				,{type:"string"	, name:'plStat'			, index:'plStat'			, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plStatNm'		, index:'plStatNm'			, width:"8%"		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'			, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'			, width:"8%"		, align:"center"}
				,{type:"string"	, name:'corpUserYn'		, index:'corpUserYn'		, width:"9%"		, align:"center"}
				,{type:"string"	, name:'plProductNm'	, index:'plProductNm'		, width:"8%"		, align:"center"}
				,{type:"string"	, name:'plMName'		, index:'plMName'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMZId'			, index:'plMZId'			, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plCellphone'	, index:'plCellphone'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'	, index:'plMerchantNo'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'creAppDate'		, index:'creAppDate'		, width:"8%"		, align:"center"}
				,{type:"string"	, name:'payRegDate'		, index:'payRegDate'		, width:"8%"		, align:"center"}
				,{type:"string"	, name:'creLicenseDate'	, index:'creLicenseDate'	, width:"8%"		, align:"center"}
			]
		, sortNm 		: "master_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : goUserConfirmDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
		, excel			: "/member/confirm/userConfirmListExcelDown"
		, excelBtnName	: "모집인조회및변경"
		, excelFileNm	: "모집인 조회 및 변경"
	});
	
	//모집인 상태
 	var plRegStatCode = {
		 useCode 	: true
		,code 		: 'REG001'
		,target 	: '#plRegStat'
		,property01 : 'Y'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plRegStatCode);
	
	//처리상태
 	var plStatCode = {
		 useCode 	: true
		,code 		: 'MAS001'
		,target 	: '#plStat'
		,property02 : 'Y'
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
	
	//금융상품유형
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
}

//모집인 조회 및 변경 row 클릭 이벤트
function goUserConfirmDetail(idx, data){
	var masterSeq 	= userConfirmGrid.gridData[idx].masterSeq;
	var plClass		= userConfirmGrid.gridData[idx].plClass;
	//var plRegStat	= userConfirmGrid.gridData[idx].plRegStat;
	//var plStat	= userConfirmGrid.gridData[idx].plStat;
	var url			= "";
	
	if(plClass == "1"){
		//개인
		url = "/member/confirm/userConfirmIndvDetail";
	}else if(plClass == "2"){
		//법인
		url = "/member/confirm/userConfirmCorpDetail";
	}
	
	$("#userConfirmDetailFrm").attr("action",url);
	$("#hMasterSeq").val(masterSeq);
	$("#userConfirmDetailFrm").submit();
}

//날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}
</script>

<form id="userConfirmDetailFrm" method="post">
	<input type="hidden" name="masterSeq" id="hMasterSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>모집인 조회 및 변경</h2>
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
					<th>모집인 상태</th>
					<td class="half_input">
						<select name="plRegStat" id="plRegStat"></select>
					</td>
					<th>처리상태</th>
					<td class="half_input">
						<select name="plStat" id="plStat"></select>
					</td>
					<th>모집인 분류</th>
					<td class="half_input">
						<select name="plClass" id="plClass"></select>
					</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td class="half_input">
						<select name="plProduct" id="plProduct"></select>
					</td>
					<th>이름</th>
					<td class="">
						<input type="text" name="plMName">
					</td>
					<th>주민등록번호</th>
					<td class="">
						<input type="text" name="plMZId" placeholder="-를 빼고 입력해주세요.">
					</td>
				</tr>
				<tr>
					<th>휴대폰번호</th>
					<td class="">
						<input type="text" name="plCellphone">
					</td>
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
					<th>조회</th>
					<td colspan="5" class="long_input">
						<select name="srchSelect2">
							<option value="">전체</option>
							<option value="creAppDate">승인완료일</option>
							<option value="payDate">결제완료일</option>
							<option value="creLicenseDate">자격취득일</option>
						</select>
						<div class="input_wrap">
                			<input type="text" name="srchDate1" id="srchDate1" class="input_calendar" readonly="readonly" style="margin-left: 5px;">
                			<a class="calendar_ico" onclick="$('#date_cal01').show();"></a>
						 	<div id="date_cal01" class="calendar01"></div>
              			</div>
					  	~
					 	<div class="input_wrap mgr5">
							<input type="text" name="srchDate2" id="srchDate2" class="input_calendar" readonly="readonly">
							<a class="calendar_ico" onclick="$('#date_cal02').show();"></a>
							<div id="date_cal02" class="calendar02"></div>
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
				<a href="javascript:void(0);" class="btn_gray btn_small" onclick="$('#excelDown').trigger('click');">다운로드</a>
			</div>
		</div>
		<div id="userConfirmGrid" class="long_table"></div>
	</div>
</div>
