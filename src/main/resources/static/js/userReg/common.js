/*
 * 회원사 시스템 > 모집인 공통 스크립트
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
	
	//첨부파일 확장자 체크
	var ext 	= fileName.split(".").pop().toLowerCase();
	if(!Valid.fileCheck(ext,"N")){
		$(this).val("");
		$(this).prev().val("");
	}
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
	location.href = "/common/fileDown?fileSeq="+$(this).attr("data-fileSeq");
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

//첨부파일 필수 체크
function goFileEssentialChk(){
	var vdChkResult = 0;
	
	$(".inputFile").each(function(){
		if($(this).attr("data-essential") == "Y" && $(this).val() == ""){
			vdChkResult++;
		}
	});
	if(vdChkResult > 0){
		return false;
	}
	return true;
}

//모집인 등록 > 탭이동
function goTab(gubun){
	
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

//모집인 조회 및 변경 > 탭이동(상세)
function goTab2(gubun){
	
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

//모집인 조회 및 변경 > 탭이동(변경요청)
function goTab3(gubun){
	
	if(gubun == "1"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpChangeApply");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpImwonChangeApply");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpExpertChangeApply");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpItChangeApply");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/member/confirm/userConfirmCorpEtcChangeApply");
	}
	
	$("#pageFrm").submit();
}

//모집인 등록 > 목록 이동
function goUserRegInfoList(){
	location.href = "/member/user/userRegPage";
}

//모집인 조회 및 변경 > 목록 이동
function goUserConfirmList(){
	location.href = "/member/confirm/userConfirmPage";
}

//작성 영역 추가
function goHtmlAdd(callUrl,formUrl,dataWrapLen){
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

//작성 영역 삭제
function goCorpInfoRemove(obj){
	var dataWrapLen = $(".data_wrap").length;
	if(dataWrapLen == 1){
		alert("더 이상 삭제할 수 없습니다.");
		return;
	}
	$(obj).closest("form").remove();
}

//수동 등록
function goCorpInfoReg(obj){
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

/* ===============================================================================================================================
 * 상태 관련
 * ===============================================================================================================================
 */

//즉시취소 클릭 시
function goUserCancelPage(){
	var html = '';
	html += '<tr>';
	html += '<th>취소사유</th>';
	html += '<td colspan="3"><input type="text" name="plHistTxt" id="plHistTxt" class="w100" maxlength="200"/></td>';
	html += '</tr>';
	
	$("#table > table").append(html);
	$("#plHistTxt").focus();
	$("#userCancel").removeClass("btn_Lgray");
	$("#userCancel").addClass("btn_blue");
	$("#userCancel").attr("onclick","goUserCancel();");
}

//즉시취소 -> 등록신청취소건으로 은행연합회에 공유되고 해당 내용은 삭제되야 한다.
function goUserCancel(){
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("취소사유를 입력해 주세요.");
		$("#plHistTxt").focus();
		return;
	}
	if(confirm("취소하시겠습니까?")){
		var p = {
			  url		: "/member/user/updateUserStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '6'
				,plHistTxt	: $("#plHistTxt").val()
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("취소되었습니다.");
					goUserConfirmList();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//해지요청 클릭 시
function goUserDropApplyPage(){
	var html = '';
	html += '<tr>';
	html += '<th>해지사유</th>';
	html += '<td colspan="3">';
	html += '<select name="plHistCd" id="plHistCd" class="w100"></select>';
	html += '</td>';
	html += '</tr>';
	
	//해지사유(코드)
	var plHistCode = {
		 useCode 	: true
		,code 		: 'DRP001'
		,target 	: '#plHistCd'
		,updData 	: ''
	};
	DataUtil.selectBox(plHistCode);
	
	$("#table > table").append(html);
	$("#userChangeApply").remove();
	$("#userDropApply").removeClass("btn_black");
	$("#userDropApply").addClass("btn_blue");
	$("#userDropApply").attr("onclick", "goUserDropApply();");
}

//해지요청
function goUserDropApply(){
	if(WebUtil.isNull($("#plHistCd").val())){
		alert("해지사유를 선택해 주세요.");
		return;
	}
	if(confirm("모집인 해지를 요청하시겠습니까?")){
		var p = {
			  url		: "/member/user/userDropApply"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: '4'
				,plHistCd		: $("#plHistCd").val()
			}
			, success 	: function (opt,result) {
				goUserConfirmList();
		    }
		}
		AjaxUtil.post(p);
	}
}

//위반이력사항 코드 호출
function goCallViolationCd(){
	var violationCode = {
		 useCode 	: true
		,code 		: 'VIT001'
		,target 	: '.violationCd'
		,updData 	: ''
	};
	DataUtil.selectBox(violationCode);
}

//위반이력사항 영역 추가
function goViolationAdd(obj){
	var html = $(obj).parent().parent().clone();
	$("#table > table").append(html);
}

//위반이력사항 영역 삭제
function goViolationDel(obj){
	var violationAreaLen = $(".violationArea").length;
	if(violationAreaLen == 1){
		alert("더 이상 삭제할 수 없습니다.");
		return;
	}
	$(obj).closest("tr").remove();
}

/* ===============================================================================================================================
 * 이력보기
 * ===============================================================================================================================
 */
 
 function goUserStepHistoryShow(masterSeq){
	let p = {
	  	  id 		: "userStepHistoryPop"
		, url 		: "/member/user/userStepHistoryPopup?masterSeq="+masterSeq
	}
	PopUtil.openPopup(p);
}
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 


