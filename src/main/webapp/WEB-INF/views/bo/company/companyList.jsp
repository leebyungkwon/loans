<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script>

var companyGrid = Object.create(GRID);

function pageLoad(){
	companyGrid.set({
		  id		: "companyGrid"
		, url		: "/admin/company/companyList"
	    , width		: "100%"
		, headCol	: ["","회원사", "아이디", "부서명", "담당자명", "직위", "회원가입일", "승인상태"]
		, bodyCol	: 
			[
				 {type:"string"	, name:'memberSeq'		, index:'memberSeq'		, hidden:true  	, id:true		}
				,{type:"string"	, name:'comCode'		, index:'comCode'		, width:"15%"					}
				,{type:"string"	, name:'memberId'		, index:'memberId'		, width:"15%"	, align:"center"}
				,{type:"string"	, name:'deptNm'			, index:'deptNm'		, width:"15%"	, align:"center"}		
				,{type:"string"	, name:'memberName'		, index:'memberName'	, width:"10%"	, align:"center"}		
				,{type:"string"	, name:'positionNm'		, index:'positionNm'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'joinDt'			, index:'joinDt'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'apprYn'			, index:'apprYn'		, width:"10%"	, align:"center"}
			]
		, sortNm : "member_seq"
		, sort : "ASC"
		, size : 10
		, rowClick	: {color:"#ccc", retFunc : companyDetail}
		, gridSearch : "search,searctBtn"
		, isPaging : true
	});
}

function companyDetail(idx, data){
	var memberSeq = companyGrid.gridData[idx].memberSeq;
	$("#memberSeq").val(memberSeq);
	$("#companyDetailFrm").submit();
}

function fn_view(code){
    
    var form = document.getElementById("boardForm");
    var url = "<c:url value='/board/viewContent.do'/>";
    url = url + "?code=" + code;
    
    form.action = url;    
    form.submit(); 
}

</script>
<form id="companyDetailFrm" method="post" action="/admin/company/companyDetail">
	<input type="hidden" name="memberSeq" id="memberSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회원사 담당자 관리</h2>
		</div>
		<div class="info_box" id="search">
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
						<select>
							<option value="">전체</option>
							<option value="1">카카오</option>
							<option value="2">삼성</option>
							<option value="3">cu</option>
						</select>
					</td>
					<th>승인여부</th>
					<td>
						<select>
							<option value="">전체</option>
							<option value="1">미요청</option>
							<option value="2">승인요청</option>
							<option value="3">승인완료</option>
							<option value="3">반려</option>
						</select>
					</td>
				</tr>
			</table>	
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	<div id="companyGrid"></div>
</div>
