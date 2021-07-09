<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var crefiaGrid = Object.create(GRID);

function pageLoad(){
	crefiaGrid.set({
		  id			: "crefiaGrid"
  		, url			: "/admin/crefia/crefiaList"
	    , width			: "100%"
	    , check			: true
  		, headCol		: ["", "그룹", "이름", "아이디", "등록일"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'memberSeq'		, index:'memberSeq'			, width:"10%"		, id:true	, hidden:true}
				,{type:"string"	, name:'creGrpNm'		, index:'creGrpNm'			, width:"15%"		, align:"center"		 }
				,{type:"string"	, name:'memberName'		, index:'memberName'		, width:"30%"		, align:"center"		 }
				,{type:"string"	, name:'memberId'		, index:'memberId'			, width:"20%"		, align:"center"		 }
				,{type:"string"	, name:'joinDt'			, index:'joinDt'			, width:"20%"		, align:"center"		 }
			]
		, sortNm 		: "member_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : crefiaDetail}
		, gridSearch 	: "searchDiv,searchBtn"
		, isPaging 		: true					
		, size 			: 10
	});
	
	// 협회 관리자 등록 팝업
	$("#regCrefia").on("click", function(){
		let p = {
			id : "crefiaRegPop"
			, params : {"memberSeq" : 0}
			, url : "/admin/crefia/crefiaSavePopup"
			, success : function (opt,result) {
				$(".popup_inner").css("width","55%");
				companyCodeCall();
		    }
		}
		PopUtil.openPopup(p);
	});
	
	// 협회 관리자 삭제
	$("#deleteCrefia").on("click", function(){
		var chkedLen = $("tbody > tr > td > input:checkbox:checked").length;
		if(chkedLen == 0){
			alert("관리자를 선택해 주세요.");
			return;
		}
		
		if(confirm("삭제하시겠습니까?")){
			var chkData = crefiaGrid.getChkData();
			var memberSeqArr = [];
			for(var i = 0;i < chkedLen;i++){
				memberSeqArr.push(chkData[i].memberSeq);
			}
			var p = {
				  url : "/admin/crefia/deleteCrefia"	
				, param : {
					memberSeqArr : memberSeqArr
				}
				, success : function (opt,result) {
					crefiaGrid.refresh();
			    }
			}
			AjaxUtil.post(p);
		}
	});
}

// 협회 관리자 상세
function crefiaDetail(idx, data){
	var memberSeq = crefiaGrid.gridData[idx].memberSeq;
	let p = {
		id : "crefiaSavePopup"
		, params : {"memberSeq" : memberSeq}
		, url : "/admin/crefia/crefiaSavePopup"
		, success : function (opt,result) {
			$(".popup_inner").css("width","55%");
			companyCodeCall();
	    }
	}
	PopUtil.openPopup(p);
}

//회원사 코드
function companyCodeCall(){
	var companyCode = {
		 useCode 	: true
		,code 		: 'CRE001'
		,target 	: '#popCreGrp'
		,updData 	: $("#hiddenCreGrp").val()
	};
	DataUtil.selectBox(companyCode);
}

// 협회 관리자 저장
function saveCrefia(){
	var password = $("#popPassword").val();
	var passwordChk = $("#popPasswordChk").val();
	var memberSeq = $("#hiddenMemberSeq").val();
	var memberName = $("#popMemberName").val();
	var memberId = $("#popMemberId").val();
	var creGrp = $("#popCreGrp").val();
	
	if(WebUtil.isNull(creGrp)){
		alert("그룹을 선택해 주세요.");
		$("#popCreGrp").focus();
		return false;
	}
	if(WebUtil.isNull(memberName)){
		alert("이름을 입력해 주세요.");
		$("#popMemberName").focus();
		return false;
	}
	if(WebUtil.isNull(memberId)){
		alert("아이디를 입력해 주세요.");
		$("#popMemberId").focus();
		return false;
	}
	var	param = {
		'creGrp'		: creGrp
		, 'memberId'	: memberId
		, 'memberName'	: memberName
	}
	if(WebUtil.isNull(memberSeq)){
		if(WebUtil.isNull(password)){
			alert("비밀번호를 입력해 주세요.");
			$("#popPassword").focus();
			return false;
		}
	}else{
		param.memberSeq = memberSeq
	}
	if(WebUtil.isNotNull(password)){
		if(password != passwordChk){
			alert("비밀번호를 확인해 주세요.");
			return false;
		}
		param.password = password
	}
	
	var checkCount 	= 0;

	if(/[0-9]/.test(password)){ //숫자
	    checkCount++;
	}
	if(/[a-z]/.test(password)){ //소문자
	    checkCount++;
	}
	if(/[A-Z]/.test(password)){ //대문자
	    checkCount++;
	}
	if(/[~!@\#$%<>^&*\()\-=+_\’]/.test(password)){ //특수문자
	    checkCount++;
	}
	if(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힝]/.test(password)){ 
	    alert("비밀번호에 한글을 사용 할 수 없습니다.");
	    return false;
	}
	if(checkCount <= 1){ 
	    alert('비밀번호는 영문 대/소문자, 숫자, 특수문자 중 2개이상의 조합이여야만 합니다.');
	    return false;
	}
	if (password.length < 8 || password.length > 20){ 
		alert("8자리 ~ 20자리 이내로 입력해주세요.");
		return false;
	}
	if (/(\w)\1\1/.test(password)){ 
		alert('같은 문자를 3번 이상 사용하실 수 없습니다.');
		return false;
	}
	if (password.search(memberId) > -1){
		alert("비밀번호에 아이디가 포함되었습니다.");
		return false;
	}
	if (password.search(/\s/) != -1){ 
		alert("비밀번호는 공백 없이 입력해주세요.");
		return false;
	}
	
	var crefiaParam = {
		param: param
		,url: "/admin/crefia/saveCrefia"
		,success: function(opt, result) {
			if(WebUtil.isNull(result.message)){
				alert(result.data[0].defaultMessage);
			}else{
				PopUtil.closePopup();
				location.reload();				
			}
    	}
	}
	AjaxUtil.post(crefiaParam);
}

function checkCapsLock(event){
	if(event.getModifierState("CapsLock")){
		$("#message").text("※ Caps Lock이 켜져 있습니다.");
	}else{
		$("#message").text("");
	}
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>협회 관리자 관리</h2>
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
					<th>그룹</th>
					<td class="half_input">
						<select name="creGrp">
							<option value="">전체</option>
							<option value="2">관리자</option>
							<option value="1">실무자</option>
						</select>
					</td>
					<th>아이디</th>
					<td class="half_input">
						<input type="text" name="memberId"/>
					</td>
				</tr>
				<tr>
					<th>이름</th>
					<td class="half_input">
						<input type="text" name="memberName"/>
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
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="regCrefia">등록</a>
				<!-- <a href="javascript:void(0);" class="btn_gray btn_small" id="deleteCrefia">삭제</a> -->
			</div>
		</div>
		<div id="crefiaGrid" class="long_table"></div>
	</div>
</div>
