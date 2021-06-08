<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var eduGrid = Object.create(GRID);

function pageLoad(){
	//법인 그리드
	eduGrid.set({
		  id				: "eduGrid"
  		, url				: "/admin/edu/eduList"
	    , width			: "100%"
  		, headCol		: ["모집인 이름", "모집인 생년월일", "모집인 성별", "구분", "취급상품", "교육이수번호", "인증서번호", "수료일자", "등록일시"]
  		, bodyCol		: 
  			[
				{type:"string"	, name:'plMName'		, index:'plMName'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMBirth'			, index:'plMBirth'			, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMGender'		, index:'plMGender'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'careerTyp'		, index:'careerTyp'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plProduct'		, index:'plProduct'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plEduNo'			, index:'plEduNo'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plCrfNo'			, index:'plCrfNo'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plEduDate'		, index:'plEduDate'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'regTimestamp'	, index:'regTimestamp'	, width:"10%"		, align:"center" ,id:true}
			]
		, sortNm 		: "REG_TIMESTAMP"
		, sort 			: "DESC"
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
	});
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>교육이수번호 조회</h2>
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
					<th>모집인 이름</th>
					<td>
						<input type="text" name="plMName">
					</td>
					<th>교육이수번호</th>
					<td>
						<input type="text" name="plEduNo">
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
		</div>
		<div id="eduGrid" class="long_table"></div>
	</div>
</div>
