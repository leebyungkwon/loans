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
				{type:"string"	,name:'comCode'			,index:'comCode'		,width:"8%"		,id:true		}
				,{type:"string"	,name:'comName'			,index:'comName'		,width:"20%"	,align:"center"	}
				,{type:"string"	,name:'plMerchantNo'	,index:'plMerchantNo'	,width:"15%"	,align:"center" }
				,{type:"string"	,name:'plBusinessNo'	,index:'plBusinessNo'	,width:"15%"	,align:"center" }		
				,{type:"string"	,name:'compPhoneNo'		,index:'compPhoneNo'	,width:"10%"	,align:"center" }		
			]
			, sortNm : "com_code"
			, sort : "DESC"
			, rowClick	: {color:"#ccc", retFunc : companyCodeDetail}
			, gridSearch : "search,searchBtn"
			, isPaging : true
			, size : 10
		});
		
		$("#companySaveBtn").on("click", function(){
			$("#companyCodeDetailFrm2").submit();
		});
		
		// 회원사 코드
	 	var companyCode = {
			useCode : false
			,code : 'COM001'
			,target : '#scomCode'
			,url : '/common/selectCompanyCodeList'
			,key : 'codeDtlCd'
			,value : 'codeDtlNm'
			,updData : ''
		};
		DataUtil.selectBox(companyCode);
	}
	
	function companyCodeDetail(idx, data){
		var comCode = companyCodeGrid.gridData[idx].comCode;
		$("#comCode").val(comCode);
		$("#companyCodeDetailFrm").submit();
	}

</script>

<form id="companyCodeDetailFrm2" method="post" action="/admin/company/companyCodeDetailPage">
</form>

<form id="companyCodeDetailFrm" method="post" action="/admin/company/companyCodeDetailInsPage">
	<input type="hidden" name="comCode" id="comCode"/>
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
						<select id="scomCode" name="scomCode"></select>
					</td>
				</tr>
			</table>	
		<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div id="companyCodeGrid"></div>
	
	<sec:authorize access="hasRole('SYSTEM', 'ADMIN')" >
		<div class="sorting_wrap">
				<a href="javascript:void(0);" id="companySaveBtn"  class="btn_gray btn_right">등록</a>
		</div>
	</sec:authorize>
</div>