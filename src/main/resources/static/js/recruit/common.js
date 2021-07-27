/*
 * 협회 시스템 > 모집인 조회 및 변경 / 승인처리
 */

//실무자 확인
$(document).on("click","#masterCheck",function(){
	
	var chk = $(this).is(":checked");
	
	if(chk){
		chk = "Y";
	}else{
		chk = "N";
	}
	
	var p = {
		  url		: "/admin/apply/applyCheck"	
		, param		: {
			 chkYn		: chk
			,masterSeq	: $("#masterSeq").val()
		}
		, success 	: function (opt,result) {

	    }
	}
	AjaxUtil.post(p);
});

//관리자 확인
$(document).on("click","#adminCheck",function(){
	
	var chk = $(this).is(":checked");
	
	if(chk){
		chk = "Y";
	}else{
		chk = "N";
	}
	
	var p = {
		  url		: "/admin/apply/applyAdminCheck"	
		, param		: {
			 adminChkYn 		: chk
			,masterSeq	: $("#masterSeq").val()
		}
		, success 	: function (opt,result) {

	    }
	}
	AjaxUtil.post(p);
});

//첨부파일 체크사항
$(document).on("change",".check_cd",function(){
	
	var id 		= $(this).attr("id");
	var code 	= id.replace("check_cd", "");
	var chk 	= $(this).is(":checked");
	var fileSeq = $(this).attr("data-fileSeq");
	var url 	= "";
	
	if(chk){
		url = "/admin/apply/insertApplyCheck";
	}else{
		url = "/admin/apply/deleteApplyCheck";
	}
	var p = {
		  url		: url	
		, loadYn	: false
		, param		: {
			 fileSeq 	: fileSeq
			,checkCd	: code
		}
		, success 	: function (opt,result) {
			if(result.data.code == "fail"){
				alert(result.data.message);
			}
	    }
	}
	AjaxUtil.post(p);
});

//첨부파일 다운로드
$(document).on("click",".goFileDownload",function(){
	location.href = "/common/fileDown?fileSeq="+$(this).attr("data-fileSeq");
});

//모집인 조회 및 변경 > 탭이동
function goTab2(gubun){
	if(gubun == "1"){
		$("#pageFrm").attr("action","/admin/recruit/recruitCorpDetail");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/admin/recruit/recruitCorpImwonDetail");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/admin/recruit/recruitCorpExpertDetail");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/admin/recruit/recruitCorpItDetail");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/admin/recruit/recruitCorpEtcDetail");
	}
	
	$("#pageFrm").submit();
}

//모집인 승인처리 > 탭이동
function goTab3(gubun){
	if(gubun == "1"){
		$("#pageFrm").attr("action","/admin/apply/applyCorpDetail");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/admin/apply/applyCorpImwonDetail");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/admin/apply/applyCorpExpertDetail");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/admin/apply/applyCorpItDetail");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/admin/apply/applyCorpEtcDetail");
	}
	
	$("#pageFrm").submit();
}

//모집인 조회 및 변경 > 목록 이동
function goRecruitList(){
	location.href = "/admin/recruit/recruitPage";
}

//모집인 승인처리 > 목록 이동
function goApplyList(){
	location.href = "/admin/apply/applyPage";
}

/* ===============================================================================================================================
 * OCR
 * ===============================================================================================================================
 */

function ocrSuccess(id){
	//console.log("id == "+id);
	var id 		= $("#"+id).attr("id");
	var code 	= id.replace("check_cd", "");
	var chk 	= $("#"+id).is(":checked");
	var fileSeq = $("#"+id).attr("data-fileSeq");
	var url 	= "";
	
	if(!chk){
		url = "/admin/apply/insertApplyCheck";
	}else{
		return false;
	}
	
	var p = {
		  url		: url	
		, async		: false
		, param		: {
			 fileSeq 	: fileSeq
			,checkCd	: code
		}
		, success 	: function (opt,result) {
			 $("#"+id).prop("checked", true);
	    }
	}
	AjaxUtil.post(p);
}

function ocrImwonSuccess(id, index){
	
	var id 		= $("#index"+index).find('#'+id).attr("id");
	var code 	= id.replace("check_cd", "");
	var chk 	= $("#index"+index).find('#'+id).is(":checked");
	var fileSeq = $("#index"+index).find('#'+id).attr("data-fileSeq");
	var url 	= "";
	
	if(!chk){
		url = "/admin/apply/insertApplyCheck";
	}else{
		return false;
	}
	
	var p = {
		  url		: url	
		, async		: false
		, param		: {
			 fileSeq 	: fileSeq
			,checkCd	: code
		}
		, success 	: function (opt,result) {
			 $("#index"+index).find('#'+id).prop("checked", true);
	    }
	}
	AjaxUtil.post(p);
}

/* ===============================================================================================================================
 * 이력보기
 * ===============================================================================================================================
 */

//이력보기
function goUserStepHistoryShow(masterSeq){
	let p = {
	  	  id 		: "userStepHistoryPop"
		, url 		: "/member/user/userStepHistoryPopup?masterSeq="+masterSeq
	}
	PopUtil.openPopup(p);
}

//변경사항 > 모집인 정보
function goRecruitHistoryShow(masterSeq, masterHistSeq, type, afterData){
	var encAfterData = encodeURIComponent(afterData);
	let p = {
	  	  id 		: "recruitHistoryPop"
		, url 		: "/admin/recruit/recruitHistoryPopup?masterSeq="+masterSeq+"&masterHistSeq="+masterHistSeq+"&histType="+type+"&afterData="+encAfterData
	}
	PopUtil.openPopup(p);
}

//변경사항 > 첨부파일
function goRecruitFileHistShow(fileGrpSeq, type){
	let p = {
	  	  id 		: "recruitFileHistoryPop"
		, url 		: "/admin/recruit/recruitFileHistoryPopup?fileGrpSeq="+fileGrpSeq+"&histType="+type
	}
	PopUtil.openPopup(p);
}


