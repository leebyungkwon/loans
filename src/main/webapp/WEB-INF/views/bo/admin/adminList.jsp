<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
var adminListGrid = Object.create(GRID);

function pageLoad(){
	adminListGrid.set({
		  id      	: "adminListGrid"
		, url      	: "/member/admin/adminList"
		, width     : "100%"
		, headCol   : ["","아이디", "부서명", "담당자명", "직위", "이메일", "회사 전화번호", "휴대폰번호", "가입일"]
		, bodyCol   : 
			[
				 {type:"string"   , name:'memberSeq'    , index:'memberSeq'     , hidden:true   , id:true 	    }
				,{type:"string"   , name:'memberId'     , index:'memberId'      , width:"10%"   , align:"center"}
				,{type:"string"   , name:'deptNm'       , index:'deptNm'      	, width:"10%"   , align:"center"}      
				,{type:"string"   , name:'memberName'   , index:'memberName'   	, width:"10%"   , align:"center"}
				,{type:"string"   , name:'positionNm'   , index:'positionNm'   	, width:"10%"   , align:"center"}
				,{type:"string"   , name:'email'        , index:'email'         , width:"13%"   , align:"center"}
				,{type:"string"   , name:'extensionNo'  , index:'extensionNo'   , width:"13%"   , align:"center"}
				,{type:"string"   , name:'mobileNo'     , index:'mobileNo'      , width:"13%"   , align:"center"}
				,{type:"string"   , name:'joinDt'       , index:'joinDt'    	, width:"10%"   , align:"center"}
			]
		, sortNm 	: "member_seq"
		, sort 		: "DESC"
		, size 		: 10
		, rowClick  : {retFunc : detailPop}
		, isPaging 	: true
	});
}
   
// AdminDetail 페이지로 이동
function detailPop(idx, data){
	var memberSeq = adminListGrid.gridData[idx].memberSeq;
	$("#hMemberSeq").val(memberSeq);
	$("#adminDetailFrm").submit();
}
</script>

<form id="adminDetailFrm" method="post" action="/member/admin/adminDetailPage">
	<input type="hidden" name="memberSeq" id="hMemberSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>관리자 조회 및 변경</h2>
		</div>
	</div>
	<div id="adminListGrid"></div>
</div>

