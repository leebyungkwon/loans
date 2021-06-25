<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">
var companyCodeGrid = Object.create(GRID);

function pageLoad(){
	companyCodeGrid.set({
		  id		: "companyCodeGrid"
		, url		: "/admin/company/companyCodeList"
		, width		: "100%" 
		, headCol	: ["회원사코드","회원사명", "등록일"]
		, bodyCol	: 
		[
			 {type:"string"		,name:'comCode'			,index:'comCode'		,width:"3%"		,id:true		}
			,{type:"string"		,name:'comName'			,index:'comName'		,width:"20%"	,align:"center"	}
			,{type:"string"		,name:'regTimestamp'	,index:'regTimestamp'	,width:"10%"	,align:"center" }		
		]
		, sortNm 		: "com_code"
		, sort 			: "ASC"
		, gridSearch 	: "search,searchBtn"
		, isPaging 		: true
		, size 			: 10
	});
	
	//회원사 코드
 	var companyCode = {
		 useCode 	: false
		,code 		: 'COM001'
		,target 	: '#comCode'
		,url 		: '/common/selectCompanyCodeList'
		,key 		: 'codeDtlCd'
		,value 		: 'codeDtlNm'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(companyCode);
}

//회원사 등록 팝업 열기
function goCompanyCodeSavePopOpen() {
	let p = {
		  id 		: "companyCodeSavePop"
		, url 		: "/admin/company/companyCodeSavePopup"
		, success	: function(opt, result){
			$("input[name='comCode']","#companyCodeSaveFrm").focus();
		}
	}
	PopUtil.openPopup(p);
}

//회원사 등록
function goCompanyCodeSave() {
	
	var params = $('#companyCodeSaveFrm').serialize();
	var url = $('#companyCodeSaveFrm').attr("action");
	
	var p = {
		url		: url
		,param 		: params
		,success	: function(opt,result){
			if(result.code == "COM0001"){
				location.reload();
			}
		}
	}
	AjaxUtil.post(p);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>회원사 관리</h2>
		</div>
		<div class="info_box k_search" id="search">
			<table class="info_box_table" style="width: 90%;">
				<colgroup>
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<th>회원사코드</th>
					<td class="half_input">
						<select id="comCode" name="comCode"></select>
					</td>
					<th>회원사명</th>
					<td>
						<input type="text" id="comName" name="comName"/>
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
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="goCompanyCodeSavePopOpen();">등록</a>
			</div>
			</sec:authorize>
		</div>
		<div id="companyCodeGrid" class="long_table"></div>
	</div>
</div>

