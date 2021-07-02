<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var eduGrid = Object.create(GRID);

function pageLoad(){
	//교육이수번호 그리드
	eduGrid.set({
		  id			: "eduGrid"
  		, url			: "/admin/edu/eduList"
	    , width			: "100%"
  		, headCol		: ["모집인 이름", "모집인 생년월일", "모집인 성별", "구분", "취급상품", "교육이수번호", "인증서번호", "수료일자", "등록일시"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'userName'		, index:'userName'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'userBirth'		, index:'userBirth'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'userSex'		, index:'userSex'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'careerTypNm'	, index:'careerTypNm'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'processCd'		, index:'processCd'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'deplomaNo'		, index:'deplomaNo'		, width:"20%"		, align:"center"}
				,{type:"string"	, name:'examCertNo'		, index:'examCertNo'	, width:"20%"		, align:"center"}
				,{type:"string"	, name:'compDate'		, index:'compDate'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'createDate'		, index:'createDate'	, width:"15%"		, align:"center" , id:true}
			]
		, sortNm 		: "CREATE_DATE"
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
					<col width="10%">
					<col width="20%">
					<col width="15%">
					<col width="20%">
				</colgroup>
				<tr>
					<th>모집인 이름</th>
					<td>
						<input type="text" name="userName">
					</td>
					<th>교육이수번호/인증서번호</th>
					<td>
						<input type="text" name="srchInput">
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
