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
	    , check			: true
  		, headCol		: ["번호", "법인명", "법인번호", "등록경로", "금융감독원 승인여부", "등록일"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'corpSeq'			, index:'corpSeq'			, width:"10%"		, id:true}
				,{type:"string"	, name:'plMerchantName'		, index:'plMerchantName'	, width:"30%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'		, index:'plMerchantNo'		, width:"20%"		, align:"center"}
				,{type:"string"	, name:'pathTypNm'			, index:'pathTypNm'			, width:"20%"		, align:"center"}
				,{type:"string"	, name:'passYn'				, index:'passYn'			, width:"20%"		, align:"center"}
				,{type:"string"	, name:'regTimestamp'		, index:'regTimestamp'		, width:"20%"		, align:"center"}
			]
		, sortNm 		: "corp_seq"
		, sort 			: "DESC"
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, rowClick		: {retFunc : goCorpInfoSavePopup}
		, isPaging 		: true					//페이징여부
		, size 			: 10
	});
	
	//삭제
	$("#deleteCorp").on("click", function(){
		var chkedLen = $("tbody > tr > td > input:checkbox:checked").length;
		if(chkedLen == 0){
			alert("법인을 선택해 주세요.");
			return;
		}
		
		if(confirm("삭제하시겠습니까?")){
			var chkData = corpGrid.getChkData();
			var corpSeqArr = [];
			for(var i = 0;i < chkedLen;i++){
				corpSeqArr.push(chkData[i].corpSeq);
			}
			var p = {
				  url : "/admin/corp/deleteCorpInfo"	
				, param : {
					corpSeqArr : corpSeqArr
				}
				, success : function (opt,result) {
					corpGrid.refresh();
			    }
			}
			AjaxUtil.post(p);
		}
	});
}

//법인 등록,수정 팝업
function goCorpInfoSavePopup(idx, data) {
	var corpSeq = 0;
	
	if(idx >= 0){
		corpSeq = corpGrid.gridData[idx].corpSeq;
	}
	
	let p = {
		  id 		: "corpInfoSavePop"
		, url 		: "/admin/corp/corpSavePopup"
		, params	: {
			corpSeq : corpSeq
		}
		, success	: function(opt, result) { 
			$("#plMerchantName","#corpInfoSaveFrm").focus();
        }
	}
	PopUtil.openPopup(p);
}

//법인 저장
function goCorpInfoSave() {
	if(confirm("저장하시겠습니까?")){
		var saveCorpInfoParam = {
			 name 		: 'corpInfoSaveFrm'
			,data 		: WebUtil.getTagInParam("#corpInfoSaveFrm")
			,success	: function(opt, result) {
				if(WebUtil.isNull(result.message)){
					alert(result.data[0].defaultMessage);
				}else{
					if(result.data == 1){
						location.reload();
					}else{
						$("#plMerchantNo").val("");
					}
				}
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
					<td>
						<input type="text" name="plMerchantName">
					</td>
					<th>법인번호</th>
					<td>
						<input type="text" name="plMerchantNo">
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data total_result"></div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="goCorpInfoSavePopup('-1','');">등록</a>
				<!-- <a href="javascript:void(0);" class="btn_black btn_small mgr5" id="deleteCorp">삭제</a> -->
			</div>
		</div>
		<div id="corpGrid" class="long_table"></div>
	</div>
</div>
