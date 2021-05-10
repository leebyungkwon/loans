<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
	var companyCodeGrid = Object.create(GRID);
	function pageLoad(){
		companyCodeGrid.set({
			  id			: "companyCodeGrid"
			, url			: "/admin/company/companyCodeList"
			, width		: "100%" 
			, headCol	: ["번호","회원사(상호)", "법인등록번호", "사업자등록번호", "회사대표번호"]
			, bodyCol	: 
			[
				{type:"string"	,name:'comCode'			,index:'comCode'			,width:"8%"	,id:true			}
				,{type:"string"	,name:'comName'		,index:'comName'		,width:"20%"						}
				,{type:"string"	,name:'plMerchantNo'	,index:'plMerchantNo'	,width:"15%"	,align:"center" }
				,{type:"string"	,name:'plBusinessNo'	,index:'plBusinessNo'	,width:"15%"	,align:"center" }		
				,{type:"string"	,name:'compPhoneNo'	,index:'compPhoneNo'	,width:"10%"	,align:"center" }		
			]
			, sortNm : "com_code"
			, sort : "DESC"
			, rowClick	: {color:"#ccc", retFunc : companyCodeDetail}
			, gridSearch : "search,searchBtn"
			, isPaging : true
			, size : 10
		});
		
		$("#companySaveBtn").on("click", function(){
			$("#companyCodeDetailFrm").submit();
		});
		
	}
	
	function companyCodeDetail(idx, data){
		var memberSeq = companyCodeGrid.gridData[idx].memberSeq;
		$("#memberSeq").val(memberSeq);
		$("#companyDetailFrm").submit();
	}

</script>

<form id="companyCodeDetailFrm" method="post" action="/admin/company/companyCodeDetailPage">
	<input type="hidden" name="comCodeSeq" id="comCodeSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회원사 관리</h2>
		</div>
		<div class="info_box k_search" id="search">
			<table class="info_box_table">
				<colgroup>
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
				</tr>
			</table>	
		<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div id="companyCodeGrid"></div>
	
	<div class="sorting_wrap">
			<a href="javascript:void(0);" id="companySaveBtn"  class="btn_gray btn_right">등록</a>
			<a href="javascript:void(0);" id="companyUpdBtn"  class="btn_gray btn_right02">수정</a>		
	</div>
</div>
