<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
var companyGrid = Object.create(GRID);

function pageLoad(){
	//회원사 담당자 관리 조회 그리드
	companyGrid.set({
		  id			: "companyGrid"
		, url			: "/admin/mng/companyList"
	    , width			: "100%" 
	    , check			: true
		, headCol		: ["","회원사", "아이디", "부서명", "담당자명", "직위", "가입일", "승인상태"]
		, bodyCol		: 
			[
				 {type:"string"	, name:'memberSeq'		, index:'memberSeq'		, width:"10%"	, hidden:true  	, id:true}
				,{type:"string"	, name:'comCodeNm'		, index:'comCodeNm'		, width:"25%"	, align:"center"}
				,{type:"string"	, name:'memberId'		, index:'memberId'		, width:"15%"	, align:"center"}
				,{type:"string"	, name:'deptNm'			, index:'deptNm'		, width:"15%"	, align:"center"}
				,{type:"string"	, name:'memberName'		, index:'memberName'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'positionNm'		, index:'positionNm'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'joinDt'			, index:'joinDt'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'apprStatNm'		, index:'apprStatNm'	, width:"10%"	, align:"center"}
			]
		, sortNm 		: "member_seq"
		, sort 			: "DESC"
		, rowClick		: {color:"#ccc", retFunc : companyDetail}
		, gridSearch 	: "search,searchBtn"
		, excel 		: "/admin/mng/excelDown"
		, excelFileNm	: "회원사담당자관리"
		, isPaging 		: true
		, size 			: 10
	});
	
	//회원사
	var companyCode = {
		useCode 	: false
		,code 		: 'COM001'
		,target 	: '#comCode'
		,url 		: '/common/selectCompanyCodeList'
		,key 		: 'codeDtlCd'
		,value		: 'codeDtlNm'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(companyCode);
	
	//승인여부
	var codeDtlNmCode = {
		 useCode 	: true
		,code 		: 'MEM001'
		,target 	: '#apprStat'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(codeDtlNmCode);
}

//회원사 담당자 상세보기
function companyDetail(idx, data){
	var memberSeq = companyGrid.gridData[idx].memberSeq;
	$("#memberSeq").val(memberSeq);
	$("#companyDetailFrm").submit();
}

//회원사 담당자 삭제
function deleteCompany() {
	var chekedelete 	= $("tbody > tr > td > input:checkbox:checked").length;
		
	if(chekedelete == 0){
		alert("담당자를 선택해 주세요.");
		return;
	}
	
	var chekeData		= companyGrid.getChkData();
	var memberSeqArr	= [];
	
	for(var i = 0;i < chekedelete;i++){
		memberSeqArr.push(chekeData[i].memberSeq);
	}
	
	if(confirm("삭제 처리 하시겠습니까?")){
		var p = {
			  url		: "/admin/mng/deleteCompany"	
			, param		: {
				memberSeqArr : memberSeqArr  
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("삭제되었습니다.");
					companyGrid.refresh();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}
</script>

<form id="companyDetailFrm" method="post" action="/admin/mng/companyDetail">
	<input type="hidden" name="memberSeq" id="memberSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회원사 담당자 관리</h2>
		</div>
		<div class="info_box k_search" id="search">
			<table class="info_box_table" style="width: 90%;">
				<colgroup>
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<th>회원사</th>
					<td class="half_input">
						<select name="comCode" id="comCode"></select>
					</td>
					<th>승인여부</th>
					<td class="half_input">
						<select name="apprStat" id="apprStat"></select>
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
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="$('#excelDown').trigger('click');">다운로드</a>
				<!-- <a href="javascript:void(0);" class="btn_gray btn_small" onclick="deleteCompany();">삭제</a> -->
			</div>
		</div>
		<div id="companyGrid" class="long_table"></div>
	</div>
</div>

