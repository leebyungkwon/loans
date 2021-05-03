<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

   var adminListGrid = Object.create(GRID);

   function pageLoad(){
      adminListGrid.set({
         id      : "adminListGrid"
         , url      : "/member/admin/adminList"
         , width      : "100%"
         , headCol   : ["","아이디", "부서명", "담당자명", "직위", "이메일", "회사전화번호", "휴대폰번호", "가입일"]
         , bodyCol   : 
                  [
                   {type:"string"   , name:'memberSeq'    	, index:'memberSeq'     	, hidden:true     , id:true 	      }
                  ,{type:"string"   , name:'memberId'      		, index:'memberId'      		, width:"10%"   , align:"center"}      // 아이디
                  ,{type:"string"   , name:'deptNm'         	, index:'deptNm'      		, width:"10%"   , align:"center"}      // 부서명      
                  ,{type:"string"   , name:'memberName'     , index:'memberName'   	, width:"10%"   , align:"center"}      // 담당자명
                  ,{type:"string"   , name:'positionNm'     	, index:'positionNm'   		, width:"10%"   , align:"center"}      // 직위
                  ,{type:"string"   , name:'email'         		, index:'email'         			, width:"13%"   , align:"center"}      // 이메일
                  ,{type:"string"   , name:'extensionNo'   		, index:'extensionNo'   		, width:"13%"   , align:"center"}      // 회사전화번호
                  ,{type:"string"   , name:'mobileNo'      		, index:'mobileNo'      		, width:"13%"   , align:"center"}      // 휴대폰번호
                  ,{type:"string"   , name:'joinDt'         		, index:'joinDt'    				, width:"10%"   , align:"center"}      // 가입일
                  ]
         , sortNm : "member_seq"
         , sort : "DESC"
         , size : 10
         , rowClick   : {retFunc : detailPop}
         , gridSearch : "" //검색영역ID,조회버튼ID
         , isPaging : true
      });
   }
   
   // AdminDetail 페이지로 이동
   function detailPop(idx, data){
      var memberSeq = adminListGrid.gridData[idx].memberSeq;
      $("#hMemberSeq").val(memberSeq);
      $("#adminDetailFrm").submit();
   }
   
   	/*	 
 	// 체크박스 선택 삭제
	function goCheckBoxDeleteBtn() {
		var chkedLen 	= $("input:checkbox:checked").length;
		
		if(chkedLen == 0){
			alert("삭제하고 싶은 데이터를 선택해 주세요.");
			return;
		}
		
		var chkData 		= adminListGrid.getChkData();
		var memberSeqArr	= [];
		
		for(var i = 0;i < chkedLen;i++){
			memberSeqArr.push(chkData[i].memberSeq);
		}
		
		var p = {
			  url		: "/member/admin/adminCheckDelete"	
			, param		: {
				memberSeq : memberSeqArr
			}
			,success: function(opt, result) {    
	            if(result > 0) {
	                alert("삭제를 실패하셨습니다.");
	            } else {
	                alert("삭제를 성공적으로 완료하였습니다.");
	                adminListGrid.refresh();
	            }            
	        }
		}
	AjaxUtil.post(p);
	} 
	*/
   
</script>

<form id="adminDetailFrm" method="post" action="/member/admin/adminDetail">
	<input type="hidden" name="memberSeq" id="hMemberSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>관리자 조회 및 변경</h2>
		</div>
	<%-- 	<div class="info_box" id="searchDiv">
			<form id="searchFrm" method="post" action="/member/admin/adminList">
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
								<option value="1">여신</option>
								<option value="2">베이직</option>
								<option value="3">우체국</option>
							</select>
						</td>
						<th>승인여부</th>
						<td>
							<select name="apprStat">
								<option value="">전체</option>
								<option value="N">승인요청</option>
								<option value="Y">승인완료</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
				<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div> --%>
			<!--<a href="javascript:goCheckBoxDeleteBtn();" class="btn_inquiry" id="goCheckBoxDeleteBtn">삭제</a> -->
	</div>
	<div id="adminListGrid"></div>
</div>