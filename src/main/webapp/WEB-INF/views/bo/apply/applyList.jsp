<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var applyGrid = Object.create(GRID);

function pageLoad(){
	//모집인 조회 및 변경 그리드
	applyGrid.set({
		  id			: "applyGrid"
  		, url			: "/admin/apply/applyList"
	    , width			: "100%"
  		, headCol		: ["번호", "회원사", "", "담당자명","모집인 분류", "법인사용인<br>여부", "금융상품유형", "이름", "주민번호", "법인명", "법인번호", "요청일", "승인<br>남은일수", "실무자<br>확인", "승인상태"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"10px"		, id:true}
				,{type:"string"	, name:'comCodeNm'		, index:'comCodeNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'			, width:"10%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'memberNm'		, index:'memberNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'			, width:"8%"		, align:"center"}
				,{type:"string"	, name:'corpUserYn'		, index:'corpUserYn'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plProductNm'	, index:'plProductNm'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMName'		, index:'plMName'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMZId'			, index:'plMZId'			, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'	, index:'plMerchantNo'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'comRegDate'		, index:'comRegDate'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'comRegDateLimit', index:'comRegDateLimit'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'chkYnTxt'		, index:'chkYnTxt'		, width:"8%"		, align:"center"}
				,{type:"string"	, name:'plStatNm'		, index:'plStatNm'			, width:"8%"		, align:"center"}
			]
		, sortNm 		: "master_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : goApplyDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
		, excel			: "/admin/apply/applyListExcelDown"
		, excelFileNm	: "모집인 조회 및 변경"
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
	
	// 회원사 코드
 	var companyCode = {
		useCode : false
		,code : 'COM001'
		,target : '#comCode'
		,url : '/common/selectCompanyCodeList'
		,key : 'codeDtlCd'
		,value : 'codeDtlNm'
		,updData : ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(companyCode);
	
	// 승인 남은일 순
	$("#sortComRegDate").on("click", function(){
		if($("#sortName").val() == "master_seq"){
			$("#sortName").val("com_reg_date");	
		}else{
			$("#sortName").val("master_seq");
		}
		applyGrid.refresh();	
	});
}

//모집인 조회 및 변경 row 클릭 이벤트
function goApplyDetail(idx, data){
	var masterSeq 	= applyGrid.gridData[idx].masterSeq;
	var plClass		= applyGrid.gridData[idx].plClass;
	
	if(plClass == "1"){
		//개인
		$("#applyDetailFrm").attr("action","/admin/apply/applyIndvDetail");
	}else if(plClass == "2"){
		//법인
		$("#applyDetailFrm").attr("action","/admin/apply/applyCorpDetail");
	}
	
	$("#hMasterSeq").val(masterSeq);
	$("#applyDetailFrm").submit();
}

//날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}

</script>

<form id="applyDetailFrm" method="post">
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
					<th>회원사 선택</th>
					<td class="half_input">
						<select name="comCode" id="comCode"></select>
					</td>
					<th>담당자명</th>
					<td class="">
						<input type="text" name="memberNm">
					</td>
					<th>실무자 확인</th>
					<td class="half_input">
						<select name="chkYn" id="chkYn">
							<option value="">전체</option>
							<option value="Y">확인</option>
							<option value="N">미확인</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>모집인분류</th>
					<td class="half_input">
						<select name="plClass" id="plClass"></select>
					</td>
					<th>금융상품유형</th>
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
						<input type="text" name="plMZId">
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
						<input type="text" name="plMerchantNo">
					</td>
				</tr>
				<tr>
					<th>승인요청일</th>
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
				<input type="hidden" id="sortName" name="sortName" value="master_seq">
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data">
				<p>총 : 00건</p>
			</div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="$('#excelDown').trigger('click');">다운로드</a>
				<a href="javascript:void(0);" class="btn_sort" id="sortComRegDate"><span class="ico_check"></span> 승인 남은일 순</a>
			</div>
			</div>
		</div>
		<div id="applyGrid" class="long_table"></div>
	</div>
</div>
