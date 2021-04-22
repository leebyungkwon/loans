<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var codeMstGrid = Object.create(GRID);
var codeDtlGrid = Object.create(GRID);

function pageLoad(){
	//코드 마스터 그리드
	codeMstGrid.set({
		  id			: "codeMstGrid"
  		, url			: "/system/code/codeMstList"
	    , width			: "100%"
  		, headCol		: ["번호", "코드명", "코드설명"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'codeMstCd'		, index:'codeMstCd'		, id:true}
				,{type:"string"	, name:'codeMstNm'		, index:'codeMstNm'				 }
				,{type:"string"	, name:'codeMstDesc'	, index:'codeMstDesc'			 }
			]
		, sortNm 		: "code_mst_cd"
		, sort 			: "ASC"
		, rowClick		: {retFunc : goCodeDtlList}
		//, gridSearch 	: "search"
	});
	/*
	//코드 마스터 등록 버튼
	document.getElementById("goCodeMstSavePop").onclick = function () {
		let p = {
			  id 	: "codeMstSavePop"
			, url 	: "/bo/system/code/p/codeMstSavePop?saveType=reg"
		}
		LibUtil.openPopup(p);
	};

	//코드 상세 등록 버튼
	document.getElementById("goCodeDtlSavePop").onclick = function () {
		let p = {
			  id 	: "codeDtlSavePop"
			, url 	: "/bo/system/code/p/codeDtlSavePop"
		}
		LibUtil.openPopup(p);
	};
	*/
}

//코드 마스터 클릭 이벤트
function goCodeDtlList(idx, data){
	//saveType 변경
	$("#codeMstCdSaveType").val("upd");
	
	//코드 마스터 정보 보여주기
	$("#pCodeMstCd").val(data.codeMstCd);
	$("#pCodeMstCd").attr("readonly","readonly");
	//$("#codeMstCdDupCheckResult").val("Y");
	//$("#goCodeMstCdDupCheck").hide();
	$("#pCodeMstNm").val(data.codeMstNm);
	$("#pCodeMstDesc").val(data.codeMstDesc);
	$("#hCodeMstCd").val(data.codeMstCd);		//코드 상세 관련
	$("#codeMstCdText").text(data.codeMstCd);	//코드 상세 관련
	
	//코드 상세 그리드
	codeDtlGrid.set({
		  id			: "codeDtlGrid"
  		, url			: "/system/code/codeDtlList?codeMstCd="+data.codeMstCd
	    , width			: "100%"
  		, headCol		: ["번호", "코드명", "코드설명"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'codeDtlCd'		, index:'codeDtlCd'		, id:true}
				,{type:"string"	, name:'codeDtlNm'		, index:'codeDtlNm'				 }
				,{type:"string"	, name:'codeDtlDesc'	, index:'codeDtlDesc'			 }
			]
		, sortNm 		: "code_dtl_cd"
		, sort 			: "ASC"
		, rowClick		: {retFunc : goCodeDtlInfo}
	});
}

//코드 상세 클릭 이벤트
function goCodeDtlInfo(idx, data){
	//saveType 변경
	$("#codeDtlCdSaveType").val("upd");
	
	//코드 상세 정보 보여주기
	$("#pCodeDtlCd").val(data.codeDtlCd);
	$("#pCodeDtlCd").attr("readonly","readonly");
	//$("#codeDtlCdDupCheckResult").val("Y");
	//$("#goCodeDtlCdDupCheck").hide();
	$("#pCodeDtlNm").val(data.codeDtlNm);
	$("#pCodeDtlDesc").val(data.codeDtlDesc);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공통코드관리</h2>
		</div>
		<div class="info_box" id="search">
			<table class="info_box_table">
				<colgroup>
					<col width="80"/>
					<col width="200"/>
					<col width="80"/>
					<col width="200"/>
				</colgroup>
				<tr>
					<th>코드마스터ID</th>
					<td>
						<input type="text"/>
					</td>
				</tr>
			</table>
			<a href="#" class="btn_inquiry">조회</a>
		</div>
	</div>
	<div id="codeMstGrid"></div>
	<div id="codeDtlGrid"></div>
</div>

<%@include file="/WEB-INF/views/bo/code/p/codeMstSavePop.jsp"%>
<%@include file="/WEB-INF/views/bo/code/p/codeDtlSavePop.jsp"%>

