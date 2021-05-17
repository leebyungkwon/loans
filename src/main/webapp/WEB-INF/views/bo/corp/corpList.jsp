<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var corpGrid = Object.create(GRID);

function pageLoad(){
	//법인 그리드
	corpGrid.set({
		  id			: "corpGrid"
  		, url			: "/admin/corp/corpList"
	    , width			: "100%"
  		, headCol		: ["번호", "법인명", "법인번호", "등록경로", "등록일"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'corpSeq'				, index:'corpSeq'				, width:"3%"		, id:true}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName'	, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'		, index:'plMerchantNo'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'pathTypNm'			, index:'pathTypNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'regTimestamp'		, index:'regTimestamp'		, width:"10%"		, align:"center"}
			]
		, sortNm 		: "corp_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : goCorpDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
	});
}

//법인 row 클릭 이벤트
function goCorpDetail(idx, data){
	var corpSeq = corpGrid.gridData[idx].corpSeq;
	let p = {
		id : "corpSavePop"
		, params : {"corpSeq" : corpSeq}
		, url : "/admin/corp/corpSavePopup"
		, success : function (opt,result) {
	    }
	}
	PopUtil.openPopup(p);
}

//법인 등록 팝업
function goCorpInfoSavePopup() {
	let p = {
		  id 		: "corpInfoSavePop"
		, url 		: "/admin/corp/corpSavePopup"
		, success	: function(opt, result) { 
			
        }
	}
	PopUtil.openPopup(p);
}

//법인 저장
function goCorpInfoSave() {
	if(confirm("저장하시겠습니까?")){
		$("#corpInfoSaveFrm").attr("action","/admin/corp/saveCorpInfo");

		var saveCorpInfoParam = {
			name : 'corpInfoSaveFrm'
			,data : WebUtil.getTagInParam("#corpInfoSaveFrm")
			,success: function(opt, result) {
				location.reload();
			}
		}      
		AjaxUtil.files(saveCorpInfoParam);
	}
}
</script>

<form id="corpDetailFrm" method="get">
	<input type="hidden" name="corpSeq" id="hCorpSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>법인 관리</h2>
		</div>
		<div class="info_box k_search" id="searchDiv">
			<table class="info_box_table" style="width: 90%;">
				<colgroup>
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<th>법인명</th>
					<td class="half_input">
						<input type="text" name="plMerchantName">
					</td>
					<th>법인번호</th>
					<td class="half_input">
						<input type="text" name="plMerchantNo">
					</td>
				</tr>
				<!-- 
				<tr>
					<th>사용여부</th>
					<td class="half_input">
						<select name="plProduct" id="plProduct"></select>
					</td>
					<th></th>
					<td></td>
				</tr>
				 -->
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data">
				<!-- <p>총 : 몇건일까요</p> -->
			</div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="goCorpInfoSavePopup();">등록</a>
			</div>
		</div>
		<div id="corpGrid" class="long_table"></div>
	</div>
</div>