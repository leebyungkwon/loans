<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var recruitGrid = Object.create(GRID);

function pageLoad(){
	//모집인 조회 및 변경 그리드
	recruitGrid.set({
		  id			: "recruitGrid"
  		, url			: "/admin/recruit/recruitList"
	    , width			: "100%"
  		, headCol		: ["번호", "회원사", "모집인 상태", "처리상태", "", "모집인 분류", "금융상품유형", "이름", "주민번호", "휴대폰번호", "법인명", "법인번호", "모집인 등록번호", "승인완료일", "결제완료일", "자격취득일"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"10px"		, id:true}
				,{type:"string"	, name:'comCodeNm'		, index:'comCodeNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plRegStatNm'	, index:'plRegStatNm'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plStatNm'		, index:'plStatNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'			, width:"10%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plProductNm'	, index:'plProductNm'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'plMName'		, index:'plMName'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMZId'			, index:'plMZId'			, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plCellphone'	, index:'plCellphone'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'	, index:'plMerchantNo'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plRegistNo'		, index:'plRegistNo'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'creAppDate'		, index:'creAppDate'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'payDate'		, index:'payDate'			, width:"15%"		, align:"center"}
				,{type:"string"	, name:'creLicenseDate'	, index:'creLicenseDate'	, width:"15%"		, align:"center"}
			]
		, sortNm 		: "master_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : goRecruitDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
		, excel			: "/admin/recruit/recruitListExcelDown"
		, excelFileNm	: "모집인 조회 및 변경"
	});
	
	//모집인 상태
 	var plRegStatCode = {
		 useCode 	: true
		,code 		: 'REG001'
		,target 	: '#plRegStat'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plRegStatCode);
	
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
}

//모집인 조회 및 변경 row 클릭 이벤트
function goRecruitDetail(idx, data){
	var masterSeq 	= recruitGrid.gridData[idx].masterSeq;
	var plClass		= recruitGrid.gridData[idx].plClass;
	
	if(plClass == "1"){
		//개인
		$("#recruitDetailFrm").attr("action","/admin/recruit/recruitIndvDetail");
	}else if(plClass == "2"){
		//법인
		$("#recruitDetailFrm").attr("action","/admin/recruit/recruitCorpDetail");
	}
	
	$("#hMasterSeq").val(masterSeq);
	$("#recruitDetailFrm").submit();
}

//날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}
</script>

<form id="recruitDetailFrm" method="post">
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
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<th>회원사 선택</th>
					<td class="half_input">
						<select name="comCode" id="comCode"></select>
					</td>
					<th>모집인 상태</th>
					<td class="half_input">
						<select name="plRegStat" id="plRegStat"></select>
					</td>
				</tr>
				<tr>
					<th>처리상태</th>
					<td class="half_input">
						<select name="plStat" id="plStat"></select>
					</td>
					<th>모집인분류</th>
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
				</tr>
				<tr>
					<th>주민등록번호</th>
					<td class="">
						<input type="text" name="plMZId">
					</td>
					<th>연락처</th>
					<td class="">
						<input type="text" name="plCellphone">
					</td>
				</tr>
				<tr>
					<th>조회</th>
					<td colspan="3" class="long_input">
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
			<div class="data">
				<p>총 : 00건</p>
			</div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_gray btn_small" onclick="$('#excelDown').trigger('click');">다운로드</a>
			</div>
		</div>
		<div id="recruitGrid" class="long_table"></div>
	</div>
</div>
