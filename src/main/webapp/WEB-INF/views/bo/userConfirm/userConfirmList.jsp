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
  		, headCol		: ["번호", "모집인 상태", "처리상태", "", "모집인 분류", "금융상품유형", "이름", "주민번호", "휴대폰번호", "법인명", "법인번호", "모집인 등록번호", "승인완료일", "결제완료일", "자격취득일"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"10px"		, id:true}
				,{type:"string"	, name:'plRegStatNm'	, index:'plRegStatNm'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plStatNm'		, index:'plStatNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'			, width:"10%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plProductNm'	, index:'plProductNm'		, width:"10%"		, align:"center"}
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
		, rowClick		: {retFunc : goUserConfirmDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
		, excel			: "/member/confirm/userConfirmListExcelDown"
	});
}

//모집인 조회 및 변경 row 클릭 이벤트
function goUserConfirmDetail(idx, data){
	var masterSeq 	= userRegGrid.gridData[idx].masterSeq;
	var plClass		= userRegGrid.gridData[idx].plClass;
	
	if(plClass == "1"){
		//개인
		$("#userConfirmDetailFrm").attr("action","/member/confirm/userConfirmIndvDetail");
	}else if(plClass == "2"){
		//법인
		$("#userConfirmDetailFrm").attr("action","/member/confirm/userConfirmCorpDetail");
	}
	
	$("#hMasterSeq").val(masterSeq);
	$("#userConfirmDetailFrm").submit();
}

//날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate2").val(result);
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
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<th>모집인 상태</th>
					<td class="half_input">
						<select name="">
							<option value="">전체</option>
						</select>
					</td>
					<th>처리상태</th>
					<td class="half_input">
						<select name="plStat">
							<option value="">전체</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>모집인분류</th>
					<td class="half_input">
						<select name="plClass">
							<option value="">전체</option>
							<option value="1">개인</option>
							<option value="2">법인</option>
						</select>
					</td>
					<th>금융상품유형</th>
					<td class="half_input">
						<select name="plProduct">
							<option value="">전체</option>
							<option value="1">대출</option>
							<option value="2">시설대여 및 연불판매</option>
							<option value="3">할부</option>
							<option value="4">어음할인</option>
							<option value="5">매출채권매입</option>
							<option value="6">지급보증</option>
							<option value="7">기타대출성 상품</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>검색어</th>
					<td class="half_input pdr0">
						<select>
							<option value="">이름</option>
						</select>
						<input type="text" name="">
					</td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th>조회</th>
					<td colspan="3" class="long_input">
						<select>
							<option value="">자격취득일</option>
						</select>
						<div class="input_wrap">
                			<input type="text" name="srchDate1" id="srchDate1" class="input_calendar" style="margin-left: 5px;">
                			<a class="calendar_ico" onclick="$('#date_cal01').show();"></a>
						 	<div id="date_cal01" class="calendar01"></div>
              			</div>
					  	~
					 	<div class="input_wrap mgr5">
							<input type="text" name="srchDate2" id="srchDate2" class="input_calendar">
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
				<p>총 : 몇건일까요</p>
			</div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_gray btn_small" onclick="$('#excelDown').trigger('click');">다운로드</a>
			</div>
		</div>
		<div id="userConfirmGrid" class="long_table"></div>
	</div>
</div>
