/*
 * 회원사 시스템 > 모집인 등록 공통 스크립트
 */

$(document).ready(function(){
	//파일찾기
	$(".goFileUpload").on("click", function () {
		$(this).prev().prev().click();
	});
	
	//첨부파일명 보여주기
	$(".inputFile").on("change", function () {
		var fileVal 	= $(this).val().split("\\");
		var fileName 	= fileVal[fileVal.length - 1];
		$(this).prev().val(fileName);
	});
	
	//첨부파일 삭제
	$(".goFileDel").on("click", function () {
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
						
						html += '<input type="text" class="w50 file_input" readonly disabled>';
						html += '<input type="file" name="files" class="inputFile" data-essential="'+essential+'" style="display: none;"/>';
						html += '<input type="hidden" name="fileTypeList" value="'+fileType+'"/>';
						html += '<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>';
						
						targetArea.html(html);
					}
				}
			}
			AjaxUtil.post(p);
		}
	});
	
	//첨부파일 다운로드
	$(".goFileDownload").on("click", function () {
		var fileSeq 	= $(this).attr("data-fileSeq");
		
		var p = {
			  url 			: "/common/fileDown"
			, contType		: "application/json; charset=UTF-8"
			, responseType	: "arraybuffer"
			, param 		: {
				fileSeq : fileSeq
			}
		}
		AjaxUtil.post(p);
	});
});

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

//모집인 등록 목록
function goUserRegInfoList() {
	location.href = "/member/user/userRegPage";
}









