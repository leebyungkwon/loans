<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

   var NoticeListGrid = Object.create(GRID);

   function pageLoad(){
      NoticeListGrid.set({
		   id      	 : "NoticeListGrid"
         , url       : "/admin/board/noticeList"
         , width     : "100%"
         , headCol   : ["No.", "제목", "조회수", "등록일"]
         , bodyCol   : 
                  [
                   {type:"string"   , name:'memberSeq'       , index:'memberSeq'        , width:"10%"   , align:"center" 	}		// No.
                  ,{type:"string"   , name:'extensionNo'     , index:'extensionNo'      , width:"60%"   , align:"left"		}      	// 제목
                  ,{type:"string"   , name:'mobileNo'        , index:'mobileNo'         , width:"15%"   , align:"center"	}      	// 조회수
                  ,{type:"string"   , name:'joinDt'          , index:'joinDt'           , width:"15%"   , align:"center"	}      	// 등록일
                  ]
         , sortNm 	: "member_seq"
         , sort 	: "DESC"
         , size 	: 10
         , rowClick : {retFunc : detailPop}
         , isPaging : true
      });
   }
   
   // AdminDetail 페이지로 이동
   function detailPop(idx, data){
      var memberSeq = NoticeListGrid.gridData[idx].memberSeq;
      $("#hMemberSeq").val(memberSeq);
      $("#noticeDetailFrm").submit();
   }
   
   // 글쓰기 페이지로 이동
   function noticeWrite(){
	   alert("나는 글쓰기 버튼!");
   }
   
</script>

<form id="noticeDetailFrm" method="post" action="/admin/board/noticeDetail">
   <input type="hidden" name="memberSeq" id="hMemberSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공지사항</h2>
		</div>
		<a href="javascript:noticeWrite();" class="btn_inquiry">글쓰기</a>
	</div>
	<div id="NoticeListGrid"></div>
</div>