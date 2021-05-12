<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	var noticeListGrid = Object.create(GRID);
	
	function pageLoad(){
	
		noticeListGrid.set({
			  id      	 	: "noticeListGrid"
			, url      	 	: "/common/board/noticeList"
			, width     	: "100%"
			, headCol   : ["No.", "제목", "조회수", "등록일"]
			, bodyCol   : 
			[
				{type:"string"		, name:'noticeSeq'		, index:'noticeSeq'			, width:"10%"		, align:"center" 	, id:true }		// No.
				,{type:"string"		, name:'title'				, index:'title'					, width:"60%"		, align:"left"					}		// 제목
				,{type:"string"		, name:'viewCnt'			, index:'viewCnt'				, width:"15%"		, align:"center"				}		// 조회수
				,{type:"string"		, name:'regTimestamp'	, index:'regTimestamp'		, width:"15%"		, align:"center"				}		// 등록일
			]
				, sortNm 	: "notice_seq"
				, sort 	: "DESC"
				, size 	: 10
				, rowClick : {retFunc : detailPop}
				, isPaging : true
		});
		
		$("#noticeWriteBtn").on("click", function(){
			$("#noticeRegFrm").submit();
		});

	}

	// AdminDetail 페이지로 이동
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
	</div>
	
	<div id="noticeListGrid"></div>
	
	<sec:authorize access="hasRole('SYSTEM', 'ADMIN')" >
		<div class="btn_wrap">
			<a href="javascript:void(0);" id="noticeWriteBtn" class="btn_gray btn_right">글 쓰기</a>
		</div>
	</sec:authorize>

</div>