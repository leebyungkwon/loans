/*
 * 회원사 시스템 > 모집인 등록 공통 스크립트
 */

//파일찾기
$(document).on("click",".goFileUpload",function(){
	$(this).prev().prev().click();
});

//첨부파일명 보여주기
$(document).on("change",".inputFile",function(){
	var fileVal 	= $(this).val().split("\\");
	var fileName 	= fileVal[fileVal.length - 1];
	$(this).prev().val(fileName);
});

//첨부파일 초기화
$(document).on("click",".goFileReset",function(){
	var fileType 	= $(this).attr("data-fileType");
	var essential 	= $(this).attr("data-essential");
	var targetArea 	= $(this).parent();
	
	var html = '';
					
	html += '<input type="text" class="w50 file_input" readonly disabled> '; //공백 제거 금지
	html += '<input type="file" name="files" class="inputFile" data-essential="'+essential+'" style="display: none;"/>';
	html += '<input type="hidden" name="fileTypeList" value="'+fileType+'"/>';
	html += '<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a> '; //공백 제거 금지
	html += '<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="'+fileType+'" data-essential="'+essential+'">초기화</a>';
	
	targetArea.html(html);
});

//첨부파일 삭제
$(document).on("click",".goFileDel",function(){
	var fileSeq 	= $(this).attr("data-fileSeq");
	var fileType 	= $(this).attr("data-fileType");
	var essential 	= $(this).attr("data-essential");
	var targetArea 	= $(this).parent();
	
	if(confirm("삭제하시겠습니까?")){
		var p = {
			  url		: "/common/fileDelete"
			, param		: {
				fileSeq : fileSeq
			}
			, success	: function(opt,result) {
				if(result.data > 0){
					alert("삭제되었습니다.");
					
					var html = '';
					
					html += '<input type="text" class="w50 file_input" readonly disabled> '; //공백 제거 금지
					html += '<input type="file" name="files" class="inputFile" data-essential="'+essential+'" style="display: none;"/>';
					html += '<input type="hidden" name="fileTypeList" value="'+fileType+'"/>';
					html += '<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a> '; //공백 제거 금지
					
					if(essential == "N"){
						html += '<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="'+fileType+'" data-essential="'+essential+'">초기화</a>';
					}
					
					targetArea.html(html);
				}
			}
		}
		AjaxUtil.post(p);
	}
});

//첨부파일 다운로드
$(document).on("click",".goFileDownload",function(){
	var p = {
		  url 			: "/common/fileDown"
		, contType		: "application/json; charset=UTF-8"
		, responseType	: "arraybuffer"
		, param 		: {
			fileSeq : $(this).attr("data-fileSeq")
		}
	}
	AjaxUtil.post(p);
});

//fileTypeList hidden disabled
function goFileTypeListDisabled(){
	$(".inputFile").each(function(){
		if(WebUtil.isNull($(this).val())){
			$(this).next().prop("disabled",true);
		}else{
			$(this).next().prop("disabled",false);
		}
	});
}

//탭이동
function goTab(gubun) {
	
	if(gubun == "1"){
		$("#pageFrm").attr("action","/member/user/userRegCorpDetail");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/member/user/userRegCorpImwonDetail");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/member/user/userRegCorpExpertDetail");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/member/user/userRegCorpItDetail");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/member/user/userRegCorpEtcDetail");
	}
	
	$("#pageFrm").submit();
}

//탭이동
function goTab2(gubun) {
	
	if(gubun == "1"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpDetail");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpImwonDetail");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpExpertDetail");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpItDetail");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpEtcDetail");
	}
	
	$("#pageFrm").submit();
}

//모집인 등록 목록
function goUserRegInfoList() {
	location.href = "/member/user/userRegPage";
}

//작성 영역 추가
function goHtmlAdd(callUrl,formUrl,dataWrapLen) {
	$.ajax({
		 type 		: "GET"
		,url 		: callUrl
		,dataType 	: "html"
		,error 		: function() {
			alert("jsp include fail");
		}
		,success : function(data){
			//dataWrapLen
			var formNo = dataWrapLen + 1;
			
			//추가
			$(".data_wrap:first").closest("form").before(data);
			//$("#target").before(data);
			
			//form 태그 감싸기
			var formNm = "userRegInfoInsertFrm"+formNo;
			$(".data_wrap:first").wrap('<form name="'+formNm+'" id="'+formNm+'" action="'+formUrl+'" method="post" enctype="multipart/form-data"></form>');
			//$(".data_wrap:last").wrap('<form name="'+formNm+'" id="'+formNm+'" action="'+formUrl+'" method="post" enctype="multipart/form-data"></form>');
		}
	});
}

//등록
function goCorpInfoReg(obj) {
	if(confirm("저장하시겠습니까?")){
		goFileTypeListDisabled();
		
		var formNm = $(obj).closest("form").attr("name");
		var p = {
			  name 		: formNm
			, success 	: function (opt,result) {
				location.reload();
	 	    }
		}
		AjaxUtil.files(p);
	}
}

//영역 삭제
function goCorpInfoRemove(obj) {
	var dataWrapLen = $(".data_wrap").length;
	if(dataWrapLen == 1){
		alert("더 이상 삭제할 수 없습니다.");
		return;
	}
	$(obj).closest("form").remove();
}





