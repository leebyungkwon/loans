<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script>

var Grid = Object.create(GRID);

function pageLoad(){
	Grid.set({
		  id		: "grid"
		, url		: "/member/company/companyList"
	    , width		: "100%"
		, headCol	: ["회원사", "아이디", "부서명", "담당자명", "직위", "회원가입일", "승인상태"]
		, bodyCol	: 
			[
				{type:"string"	, name:'comNm'			, index:'comNm'			, width:"15%"	}
				,{type:"string"	, name:'comId'			, index:'comId'			, width:"15%"	, align:"center"}
				,{type:"string"	, name:'comDeptNm'		, index:'comDeptNm'		, width:"15%"	, align:"center"}		
				,{type:"string"	, name:'managerNm'		, index:'managerNm'		, width:"10%"	, align:"center"}		
				,{type:"string"	, name:'test1'			, index:'test1'			, width:"10%"	, align:"center"}
				,{type:"string"	, name:'regDate'		, index:'regDate'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'approveCdNm'	, index:'approveCdNm'	, width:"10%"	, align:"center"}
			]
		, sortNm : "reg_date"
		, sort : "DESC"
		, size : 10
		, rowClick	: {color:"#ccc", retFunc : detailPop}
		, gridSearch : "search"
		, isPaging : true
	});
}

function detailPop(idx, data){
	let p = {
		id : "dtl_save"
		, data : data
		, curl : "/admin/recruit/p/recruitDtl"
	}
	LibUtil.openPopup(p);
}

</script>


<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회원사 관리</h2>
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
						</select>
					</td>
					<th>승인여부</th>
					<td>
						<select>
							<option value="">전체</option>
						</select>
					</td>
				</tr>
			</table>
			<a href="#" class="btn_inquiry">조회</a>
		</div>
	</div>
	<div id="grid"></div>
</div>
