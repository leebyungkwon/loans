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
  		, headCol		: ["", "접수번호","등록번호" ,"회원사", "모집인<br>상태", "처리상태", "", "모집인<br>분류", "법인사용인<br>여부", "금융상품<br>유형", "이름", "주민번호", "휴대폰번호", "법인명", "법인번호", "승인<br>완료일", "결제<br>완료일", "자격<br>취득일"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"10px"		, id:true		 , hidden:true}
				,{type:"string"	, name:'masterToId'		, index:'masterToId'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'plRegistNo'		, index:'plRegistNo'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'comCodeNm'		, index:'comCodeNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plRegStatNm'	, index:'plRegStatNm'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plStatNm'		, index:'plStatNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'			, width:"10%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'corpUserYn'		, index:'corpUserYn'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plProductNm'	, index:'plProductNm'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'plMName'		, index:'plMName'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMZId'			, index:'plMZId'			, width:"12%"		, align:"center"}
				,{type:"string"	, name:'plCellphone'	, index:'plCellphone'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'	, index:'plMerchantNo'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'creAppDate'		, index:'creAppDate'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'payRegDate'		, index:'payRegDate'		, width:"12%"		, align:"center"}
				,{type:"string"	, name:'creLicenseDate'	, index:'creLicenseDate'	, width:"12%"		, align:"center"}
			]
		, rowClick		: {retFunc : goRecruitDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
		, excel			: "/admin/recruit/recruitListExcelDown"
		, excelFileNm	: "모집인조회및변경"
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
	
	// 정보변경여부 체크
	$("#updFileCheck").on("click", function(){
		recruitGrid.refresh();
	})
	
	
	// 결제일순 정렬
	$("#sortPayDate").on("click", function(){
		if($("#searchPayDate").val() == "cre_app_date"){
			$("#searchPayDate").val("order_pay_date");	
		}else{
			$("#searchPayDate").val("cre_app_date");
		}
		recruitGrid.refresh();
	});
	
	
	// 2021-09-27 버튼활성화
	if($("#searchPayDate").val() == "order_pay_date"){
		$("#sortPayDate").addClass("on");
	}
	
}

$(document).mouseup(function(e){
	var calendar01 = $(".calendar01");
	if(calendar01.has(e.target).length === 0){
		calendar01.hide();
	}
});

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
					<th>모집인 상태</th>
					<td class="half_input">
						<select name="plRegStat" id="plRegStat"></select>
					</td>
					<th>처리상태</th>
					<td class="half_input">
						<select name="plStat" id="plStat"></select>
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
				</tr>
				<tr>
					<th>이름</th>
					<td class="">
						<input type="text" name="plMName">
					</td>
					<th>휴대폰번호</th>
					<td class="">
						<input type="text" name="plCellphone">
					</td>
					<th>주민번호</th>
					<td class="">
						<input type="text" name="plMZId" placeholder="-를 빼고 입력해주세요.">
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
					<th>등록번호</th>
					<td class="">
						<input type="text" name="plRegistNo">
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
							<option value="regDate">등록일</option>
						</select>
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
					<th>정보변경여부</th>
					<td class="half_input">
						<input type="checkbox" id="updFileCheck" name="updFileCheck"/>
						
						
<!-- 						<select name="updFileCheck" id="updFileCheck">
							<option value="">전체</option>
							<option value="Y">Y</option>
							<option value="N">N</option>
						</select> -->
					</td>
				</tr>
				<input type="hidden" id="searchPayDate" name="searchPayDate" value="cre_app_date">
				<input type="hidden" id="regPath" name="regPath" value="B" />
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="$('#excelDown').trigger('click');">다운로드</a>
				<a href="javascript:void(0);" class="btn_sort" id="sortPayDate"><span class="ico_check"></span>결제일 정렬</a>
			</div>
		</div>
		<div id="recruitGrid" class="long_table"></div>
	</div>
</div>
