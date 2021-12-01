<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var codeMstGrid = Object.create(GRID);
var codeDtlGrid = Object.create(GRID);

function pageLoad(){
	//코드마스터 그리드
	codeMstGrid.set({
		  id			: "codeMstGrid"
  		, url			: "/system/code/codeMstList"
	    , width			: "100%"
  		, headCol		: ["코드마스터ID", "코드마스터명", "사용여부", "설명"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'codeMstCd'		, index:'codeMstCd'		, id:true	 }
				,{type:"string"	, name:'codeMstNm'		, index:'codeMstNm'				 	 }
				,{type:"string"	, name:'useYn'			, index:'useYn'						 }
				,{type:"string"	, name:'codeMstDesc'	, index:'codeMstDesc'			 	 }
			]
		, sortNm 		: "code_mst_cd"
		, sort 			: "ASC"
		, rowClick		: {retFunc : goCodeDtlList}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: false					//페이징여부
	});
	
	//코드상세 그리드
	goCodeDtlGridDraw("");
	
	//코드마스터 저장
	$("#codeMstSaveBtn").on("click",function(){
		var params 	= $("#codeMstSave").serialize();
		var url 	= $("#codeMstSave").attr("action");
		
		var p = {
			url		: url
			,param 		: params
			,success	: function(opt,result){
				codeMstGrid.refresh();
			}
		}
		AjaxUtil.post(p);
	});
	
	//코드상세 저장
	$("#codeDtlSaveBtn").on("click",function(){
		var params 	= $("#codeDtlSave").serialize();
		var url 	= $("#codeDtlSave").attr("action");
		
		var p = {
			 url		: url
			,param 		: params
			,success	: function(opt,result){
				codeDtlGrid.refresh();
			}
		}
		AjaxUtil.post(p);
	});
}

//코드마스터 클릭 이벤트
function goCodeDtlList(idx, data){
	//표시
	$("#tbl_codeMstGrid_body > tbody > tr").eq(idx).css("background-color","#eee");
	
	//saveType 변경
	$("#codeMstCdSaveType").val("upd");
	
	//코드마스터 정보 보여주기
	$("#pCodeMstCd").val(data.codeMstCd);
	$("#pCodeMstCd").attr("readonly","readonly");
	$("#pCodeMstNm").val(data.codeMstNm);
	$("#codeMstSave").find("input[name='useYn']").removeAttr("checked");
	$("#masUse"+data.useYn).attr("checked","checked");
	$("#pCodeMstDesc").val(data.codeMstDesc);
	
	//코드 상세 관련
	$("#hCodeMstCd").val(data.codeMstCd);
	$("#codeMstCdText").text(data.codeMstCd);
	$("#pCodeDtlCd").removeAttr("readonly");
	$("#pCodeDtlCd").val("");
	$("#pCodeDtlNm").val("");
	$("#codeDtlSave").find("input[name='useYn']").removeAttr("checked");
	$("#dtlUseY").attr("checked","checked");
	$("#pCodeDtlDesc").val("");
	
	//코드상세 그리드
	goCodeDtlGridDraw(data.codeMstCd);
}

//코드상세 그리드
function goCodeDtlGridDraw(codeMstCd) {
	codeDtlGrid.set({
		  id			: "codeDtlGrid"
		, url			: "/system/code/codeDtlList?codeMstCd="+codeMstCd
	    , width			: "100%"
		, headCol		: ["코드상세ID", "코드상세명", "설명"]
		, bodyCol		: 
			[
				 {type:"string"	, name:'codeDtlCd'		, index:'codeDtlCd'		, id:true	 }
				,{type:"string"	, name:'codeDtlNm'		, index:'codeDtlNm'				 	 }
				,{type:"string"	, name:'useYn'			, index:'useYn'						 }
				,{type:"string"	, name:'codeDtlDesc'	, index:'codeDtlDesc'			 	 }
			]
		, sortNm 		: "code_dtl_cd"
		, sort 			: "ASC"
		, rowClick		: {retFunc : goCodeDtlInfo}
		, gridSearch 	: "," 					//검색영역ID,조회버튼ID
		, isPaging 		: false					//페이징여부
	});
}

//코드상세 클릭 이벤트
function goCodeDtlInfo(idx, data){
	//표시
	$("#tbl_codeDtlGrid_body > tbody > tr").eq(idx).css("background-color","#eee");
	
	//saveType 변경
	$("#codeDtlCdSaveType").val("upd");
	
	//코드상세 정보 보여주기
	$("#pCodeDtlCd").val(data.codeDtlCd);
	$("#pCodeDtlCd").attr("readonly","readonly");
	$("#pCodeDtlNm").val(data.codeDtlNm);
	$("#codeDtlSave").find("input[name='useYn']").removeAttr("checked");
	$("#dtlUse"+data.useYn).attr("checked","checked");
	$("#pCodeDtlDesc").val(data.codeDtlDesc);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공통코드관리</h2>
		</div>
		<div class="info_box k_search" id="searchDiv">
			<table class="info_box_table">
				<colgroup>
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<th>코드마스터ID</th>
					<td>
						<input type="text" name="codeMstCd"/>
					</td>
					<th>코드마스터명</th>
					<td>
						<input type="text" name="codeMstNm"/>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div id="codeMstGrid" style="width: 49%; float: left; height: 650px; overflow: scroll;"></div>
	<div id="codeDtlGrid" style="width: 49%; float: right; height: 650px; overflow: scroll;"></div>
</div>

<div class="cont_area">
	<div class="top_box" style="width: 49%; float: left;">
		<div class="title">
			<h2>코드마스터 저장</h2>
		</div>
		<div class="info_box" style="height: 320px;">
			<form name="codeMstSave" id="codeMstSave" action="/system/code/codeMstSave" method="POST">
				<input type="hidden" name="saveType" id="codeMstCdSaveType" value="reg"/>
			
				<table class="info_box_table">
					<colgroup>
						<col width="150"/>
						<col width="300"/>
					</colgroup>
					<tr>
						<th>코드마스터ID *</th>
						<td>
							<input type="text" name="codeMstCd" id="pCodeMstCd" maxlength="10" data-vd='{"type":"text","len":"1,10","req":true,"msg":"코드마스터ID를 입력해 주세요."}'>
						</td>
					</tr>
					<tr>
						<th class="pdt10">코드마스터명 *</th>
						<td class="pdt10"> 
							<input type="text" name="codeMstNm" id="pCodeMstNm" maxlength="100" data-vd='{"type":"text","len":"1,100","req":true,"msg":"코드마스터명을 입력해 주세요."}'>
						</td>
					</tr>
					<tr>
						<th class="pdt10">사용여부 *</th>
						<td class="pdt10"> 
							<input type="radio" name="useYn" id="masUseY" value="Y" style="width: auto;" checked="checked"><label for="masUseY">사용</label>
							<input type="radio" name="useYn" id="masUseN" value="N" style="width: auto;"><label for="masUseN">사용안함</label>
						</td>
					</tr>
					<tr>
						<th class="pdt10">설명</th>
						<td class="pdt10">
							<textarea rows="5" cols="1" name="codeMstDesc" id="pCodeMstDesc" class="w100" maxlength="100"></textarea>
						</td>
					</tr>
				</table>
			</form>
			<a href="javascript:void(0);" class="btn_inquiry" id="codeMstSaveBtn">저장</a>
		</div>
	</div>
	
	<div class="top_box" style="width: 49%; float: right;">
		<div class="title">
			<h2>코드상세 저장</h2>
		</div>
		<div class="info_box" style="height: 320px;">
			<form name="codeDtlSave" id="codeDtlSave" action="/system/code/codeDtlSave" method="POST">
				<input type="hidden" name="saveType" id="codeDtlCdSaveType" value="reg"/>
				<input type="hidden" name="codeMstCd" id="hCodeMstCd"/>
			
				<table class="info_box_table">
					<colgroup>
						<col width="150"/>
						<col width="300"/>
					</colgroup>
					<tr>
						<th>코드마스터ID *</th>
						<td id="codeMstCdText">코드마스터를 선택해 주세요.</td>
					</tr>
					<tr>
						<th class="pdt10">코드상세ID *</th>
						<td class="pdt10">
							<input type="text" name="codeDtlCd" id="pCodeDtlCd" maxlength="10" data-vd='{"type":"text","len":"1,10","req":true,"msg":"코드상세ID를 입력해 주세요."}'>
						</td>
					</tr>
					<tr>
						<th class="pdt10">코드상세명 *</th>
						<td class="pdt10"> 
							<input type="text" name="codeDtlNm" id="pCodeDtlNm" maxlength="100" data-vd='{"type":"text","len":"1,100","req":true,"msg":"코드상세명을 입력해 주세요."}'>
						</td>
					</tr>
					<tr>
						<th class="pdt10">사용여부 *</th>
						<td class="pdt10"> 
							<input type="radio" name="useYn" id="dtlUseY" value="Y" style="width: auto;" checked="checked"><label for="dtlUseY">사용</label>
							<input type="radio" name="useYn" id="dtlUseN" value="N" style="width: auto;"><label for="dtlUseN">사용안함</label>
						</td>
					</tr>
					<tr>
						<th class="pdt10">설명</th>
						<td class="pdt10">
							<textarea rows="5" cols="1" name="codeDtlDesc" id="pCodeDtlDesc" class="w100" maxlength="100"></textarea>
						</td>
					</tr>
				</table>
			</form>
			<a href="javascript:void(0);" class="btn_inquiry" id="codeDtlSaveBtn">저장</a>
		</div>
	</div>
</div>


