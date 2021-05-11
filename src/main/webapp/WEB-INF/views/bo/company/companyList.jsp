<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
var companyGrid = Object.create(GRID);

function pageLoad(){
	
	companyGrid.set({
		  id		: "companyGrid"
		, url		: "/admin/mng/companyList"
	    , width		: "100%" 
	    , check		: true
		, headCol	: ["","회원사", "아이디", "부서명", "담당자명", "직위", "회원가입일", "승인상태"]
		, bodyCol	: 
			[
				 {type:"string"	, name:'memberSeq'		, index:'memberSeq'		, hidden:true  	, id:true		}
				,{type:"string"	, name:'comCodeNm'		, index:'comCodeNm'		, width:"15%"					}
				,{type:"string"	, name:'memberId'		, index:'memberId'		, width:"15%"	, align:"center"}
				,{type:"string"	, name:'deptNm'			, index:'deptNm'		, width:"15%"	, align:"center"}		
				,{type:"string"	, name:'memberName'		, index:'memberName'	, width:"10%"	, align:"center"}		
				,{type:"string"	, name:'positionNm'		, index:'positionNm'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'joinDt'			, index:'joinDt'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'apprStat'		, index:'apprStat'		, width:"10%"	, align:"center"}
			]
		, sortNm : "member_seq"
		, sort : "ASC"
		, rowClick	: {color:"#ccc", retFunc : companyDetail}
		, gridSearch : "search,searchBtn"
		, excel : "/admin/mng/excelDown"
		, isPaging : true
		, size : 10
	});
}

function companyDetail(idx, data){
		var memberSeq = companyGrid.gridData[idx].memberSeq;
		$("#memberSeq").val(memberSeq);
		$("#companyDetailFrm").submit();
}

function deleteCompany() {
	var chekedelete 	= $("input:checkbox:checked").length;
		
	if(chekedelete == 0){
		alert("담당자를 선택해 주세요.");
		return;
	}
	
	var chekeData		= companyGrid.getChkData();
	var memberSeqArr		= [];
	
	for(var i = 0;i < chekedelete;i++){
		memberSeqArr.push(chekeData[i].memberSeq);
	}
	
	if(confirm(memberSeqArr+" 삭제 처리 하시겠습니까?")){
		var p = {
			  url		: "/admin/mng/deleteCompany"	
			, param		: {
				memberSeqArr : memberSeqArr  
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("삭제 되었습니다.");
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
			<table class="info_box_table">
				<colgroup>
					<col width="80"/>
					<col width="200"/>
					<col width="80"/>
					<col width="200"/>
				</colgroup>
				<tr>
					<th>회원사</th>
					<td>
						<select name="comCode">
							<option value="0">전체</option>
							<option value="1">현대카드</option>
							<option value="2">볼보파이낸셜</option>
							<option value="3">우체국</option>						
						</select>
					</td>
					<th>승인여부</th>
					<td>
						<select name="apprStat">
							<option value="">전체</option>
							<option value="1">미승인</option>
							<option value="2">가승인</option>
							<option value="3">승인</option>						
</select>
					</td>
				</tr>
			</table>	
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
			<div class="sorting_wrap">
			<div class="action">
				<a href="javascript:alert('붙여야해!~');" class="btn_gray btn_small mgl5">다운로드</a>
			</div>
		</div>
	</div>
		<div id="companyGrid"></div>
	<div class="sorting_wrap">
		<div class="action">
			<a href="javascript:void(0);" class="btn_gray btn_small mgl5" onclick="deleteCompany();">삭제</a>
		</div>
	</div>
</div>
