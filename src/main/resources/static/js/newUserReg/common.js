/*
 * 회원사 시스템 > 모집인 공통 스크립트
 */

//첨부파일 다운로드
$(document).on("click",".goFileDownload",function(){
	location.href = "/common/fileDown?fileSeq="+$(this).attr("data-fileSeq");
});

//모집인 등록 > 탭이동
function goTab(gubun){
	
	if(gubun == "1"){
		$("#pageFrm").attr("action","/member/newUser/newUserRegCorpDetail");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/member/newUser/newUserRegCorpImwonDetail");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/member/newUser/newUserRegCorpExpertDetail");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/member/newUser/newUserRegCorpItDetail");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/member/newUser/newUserRegCorpEtcDetail");
	}
	
	$("#pageFrm").submit();
}

//모집인 조회 및 변경 > 탭이동(상세)
function goTab2(gubun){
	
	if(gubun == "1"){
		$("#pageFrm").attr("action","/member/newConfirm/newConfirmCorpDetail");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/member/newConfirm/newConfirmCorpImwonDetail");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/member/newConfirm/newConfirmCorpExpertDetail");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/member/newConfirm/newConfirmCorpItDetail");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/member/newConfirm/newConfirmCorpEtcDetail");
	}
	
	$("#pageFrm").submit();
}

//모집인 조회 및 변경 > 탭이동(변경요청)
function goTab3(gubun){
	
	if(gubun == "1"){
		$("#pageFrm").attr("action","/member/newConfirm/newUserConfirmCorpChangeApply");
	}else if(gubun == "2"){
		$("#pageFrm").attr("action","/member/newConfirm/newUserConfirmCorpImwonChangeApply");
	}else if(gubun == "3"){
		$("#pageFrm").attr("action","/member/newConfirm/newUserConfirmCorpExpertChangeApply");
	}else if(gubun == "4"){
		$("#pageFrm").attr("action","/member/newConfirm/newUserConfirmCorpItChangeApply");
	}else if(gubun == "5"){
		$("#pageFrm").attr("action","/member/newConfirm/newUserConfirmCorpEtcChangeApply");
	}
	
	$("#pageFrm").submit();
}

//모집인 등록 > 목록 이동
function goUserRegInfoList(){
	location.href = "/member/newUser/newUserRegPage";
}

//모집인 조회 및 변경 > 목록 이동
function goUserConfirmList(){
	location.href = "/member/newConfirm/newConfirmPage";
}

/* ===============================================================================================================================
 * 상태 관련
 * ===============================================================================================================================
 */

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
			  url		: "/member/newConfirm/newUserDropApply"	
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

//해지요청취소
function goUserDropApplyCancel(){
	if(confirm("해지요청을 취소하시겠습니까?")){
		var p = {
			  url		: "/member/newConfirm/newUserDropApplyCancel"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: '9'
			}
			, success 	: function (opt,result) {
				goUserConfirmList();
		    }
		}
		AjaxUtil.post(p);
	}
	
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
 
/* ===============================================================================================================================
 * datepicker
 * ===============================================================================================================================
 */
 
//datepicker 그리기
function goDatepickerDraw(){
	$(".calendar01").datepicker({
		 dateFormat	: "yy-mm-dd"
		,changeMonth: true
		,changeYear	: true
		//,yearRange: "c-10:c+10"
		,onSelect	: function(dateText1,inst) {
			$(this).prev().val(dateText1);
			$(this).hide();
		}
	});
}

//datepicker 보여주기
function goDatepickerShow(obj){
	$(obj).next().show();
}

//datepicker 숨기기
$(document).mouseup(function(e){
	var calendar01 = $(".calendar01");
	if(calendar01.has(e.target).length === 0){
		calendar01.hide();
	}
});
 

 
 
 /*

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
	var html = $(".violationArea").last().clone();
	$("#table > table").append(html);
	$(".violationArea").last().find("td").find("select").find("option").eq(0).attr("selected","selected");
}
*/

//위반이력사항 영역 삭제
function goViolationDel(obj){
	var violationAreaLen = $(".violationArea").length;
	if(violationAreaLen == 1){
		alert("더 이상 삭제할 수 없습니다.");
		return;
	}
	$(obj).closest("tr").remove();
}

//위반이력사항 데이터 삭제요청
function goViolationDataDelApply(violationSeq,obj){
	if(confirm("위반이력 삭제를 요청하시겠습니까?")){
		var p = {
			  url		: "/member/newConfirm/newApplyDeleteViolationInfo"	
			, param		: {
				  violationSeq 	: violationSeq
			}
			, success 	: function (opt,result) {
				if(result.code == "success"){
					$(obj).closest("tr").find("td").addClass("red");
					$(obj).closest("tr").find("td").find("a:last").remove();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//위반이력사항 데이터 삭제
function goViolationDataDel(vioNum,violationSeq,obj){
	
	if(WebUtil.isNull(vioNum)){
		alert("위반이력번호를 확인해 주세요.");
		return false;
	}
	
	if(WebUtil.isNull(violationSeq)){
		alert("위반이력일련번호를 확인해 주세요.");
		return false;
	}	
	
	if(confirm("요청과 동시에 은행연합회 API가 발송됩니다.\n위반이력을 삭제하시겠습니까?")){
		var p = {
			  url		: "/member/newConfirm/newDeleteNewUserViolationInfo"	
			, param		: {
				  violationSeq 	: violationSeq
				  , vioNum		: vioNum
			}
			, success 	: function (opt,result) {
				
				$(obj).closest("tr").remove();
				
				var violationAreaLen 	= $(".violationArea").length;
				
				if(violationAreaLen == 0){
					goViolationAdd(obj);
				}
				
				/*
				var violationAreaLen 	= $(".violationArea").length;
				var aLen 				= $(obj).closest("tr").find("td").find("a").length;
				
				if(aLen == 2){
					$(".violationArea").eq(violationAreaLen - 2).find("td").find("a").before('<a href="javascript:void(0);" class="btn_Lgray btn_add mgl5 mgt7" onclick="goViolationAdd(this);">+</a> '); //공백 제거 금지
				}else if(violationAreaLen == 1){
					goViolationAdd(obj);
				}
				
				$(obj).closest("tr").remove();
				*/
		    }
		}
		AjaxUtil.post(p);
	}
}

 
 
 
 
 
 
 


