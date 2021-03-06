<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
var noticeListGrid = Object.create(GRID);

function pageLoad(){
	//공지사항 그리드
	noticeListGrid.set({
		  id      	 	: "noticeListGrid"
		, url      	 	: "/common/board/noticeList"
		, width     	: "100%"
		, headCol   	: ["", "제목", "대상", "조회수", "등록일"]
		, bodyCol   	: 
			[
				 {type:"string"		, name:'noticeSeq'		, index:'noticeSeq'		, width:"10%"		, align:"center" 	, id:true , hidden:true}
				,{type:"string"		, name:'title'			, index:'title'			, width:"60%"		, align:"left"							   }
				,{type:"string"		, name:'noticeDispCdNm'	, index:'noticeDispCdNm', width:"10%"		, align:"center"						   }
				,{type:"string"		, name:'viewCnt'		, index:'viewCnt'		, width:"10%"		, align:"center"						   }
				,{type:"string"		, name:'regTimestamp'	, index:'regTimestamp'	, width:"10%"		, align:"center"						   }
			]
		, sortNm 		: "notice_seq"
		, sort 			: "DESC"
		, size 			: 10
		, gridSearch 	: "searchDiv,searchBtn"
		, rowClick 		: {retFunc : detailPop}
		, isPaging 		: true
	});
	
	//대상 분류
	var noticeDispCdNm = {
		 useCode 	: true
		,code 		: 'NTC001'
		,target 	: '#noticeDispCd'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(noticeDispCdNm);
	
	//등록 버튼 클릭 이벤트
	$("#noticeWriteBtn").on("click", function(){
		$("#noticeRegFrm").submit();
	});
}

//공지사항 row 클릭 이벤트
function detailPop(idx, data){
	var noticeSeq = noticeListGrid.gridData[idx].noticeSeq;
	$("#hNoticeSeq").val(noticeSeq);
	$("#noticeDetailFrm").submit();
}
</script>

<!-- 글 상세보기 -->
<form id="noticeDetailFrm" method="post" action="/common/board/noticeDetailPage">
	<input type="hidden" name="noticeSeq" id="hNoticeSeq"/>
</form>

<!-- 글 등록 -->
<form id="noticeRegFrm" method="post" action="/common/board/noticeRegPage">
	<input type="hidden" name="noticeSeq" id="hNoticeSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공지사항</h2>
		</div>
		<div class="info_box k_search" id="searchDiv">
			<sec:authorize access="hasAnyRole('MEMBER')" >
				<input type="hidden" name="noticeDispCd" value="1" />
			</sec:authorize>
			
			<table class="info_box_table" style="width: 90%;">
				<colgroup>
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<sec:authorize access="hasAnyRole('SYSTEM', 'ADMIN')" >
						<th>대상</th>
						<td>
							<select name="noticeDispCd" id="noticeDispCd"></select>
						</td>
					</sec:authorize>
					<th>제목</th>
					<td>
						<input type="text" name="title">
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<sec:authorize access="hasAnyRole('SYSTEM', 'ADMIN')" >
				<div class="action">
					<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="noticeWriteBtn">등록</a>
				</div>
			</sec:authorize>
		</div>
		<div id="noticeListGrid"></div>
	</div>
</div>

