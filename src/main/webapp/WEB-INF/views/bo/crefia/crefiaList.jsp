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
  		, headCol		: ["번호", "그룹", "이름", "아이디", "등록일", ""]
  		, bodyCol		: 
  			[
				{type:"string"	, name:'memberSeq'		, index:'memberSeq'			, width:"10px"		, id:true}
				,{type:"string"	, name:'creGrpNm'		, index:'creGrpNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'memberName'		, index:'memberName'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'memberId'		, index:'memberId'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'joinDt'			, index:'joinDt'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'memberSeq'		, index:'memberSeq'			, width:"10%"		, hidden:true}
			]
		, sortNm 		: "member_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : crefiaDetail}
		, gridSearch 	: "searchDiv,searchBtn"
		, isPaging 		: true					
		, size 			: 10
	});
	
	// 협회 관리자 등록팝업
	$("#regCrefia").on("click", function(){
		let p = {
			id : "crefiaRegPop"
			, params : {"memberSeq" : 0}
			, url : "/admin/crefia/crefiaSavePopup"
			, success : function (opt,result) {
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

// 협회 관리자 관리 상세
function crefiaDetail(idx, data){
	var memberSeq = crefiaGrid.gridData[idx].memberSeq;
	let p = {
		id : "saveCrefiaPopup"
		, params : {"memberSeq" : memberSeq}
		, url : "/admin/crefia/saveCrefiaPopup"
		, success : function (opt,result) {
			companyCodeCall();
	    }
	}
	PopUtil.openPopup(p);
}

function companyCodeCall(){
	// 회원사 코드
	var companyCode = {
		useCode : true
		,code : 'CRE001'
		,target : '#popCreGrp'
		,updData : $("#hiddenCreGrp").val()
	};
	DataUtil.selectBox(companyCode);
}


// 협회관리자 저장
function crefiaSave(){
	var password = $("#popPassword").val();
	var passwordChk = $("#popPasswordChk").val();
	var memberSeq = $("#hiddenMemberSeq").val();
	var memberName = $("#popMemberName").val();
	var memberId = $("#popMemberId").val();
	var creGrp = $("#popCreGrp").val();
	
//////////////////////////////////////////////////////////////////////////////////////////////

	var pw = $("#popPasswordChk").val();
	var id = $("#popMemberId").val();
		
	var reg = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
	var hangulcheck = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
	
	 var num = pw.search(/[0-9]/g);
	 var eng = pw.search(/[a-z]/g);
	 var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	 var bigeng = pw.search(/[A-Z]/);
	 
	 if(pw.length < 8 || pw.length > 20){
	  alert("8자리 ~ 20자리 이내로 입력해주세요.");
	  return false;
	}else if(/(\w)\1\1/.test(pw)){
	 alert('같은 문자를 3번 이상 사용하실 수 없습니다.');
	 return false;
	 }else if(pw.search(id) > -1){
	 alert("비밀번호에 아이디가 포함되었습니다.");
	  return false;
	 }else if(pw.search(/\s/) != -1){
	 alert("비밀번호는 공백 없이 입력해주세요.");
	 return false;
	 }else if( (num < 0 && eng < 0 && bigeng < 0) || (eng < 0 && spe < 0 && bigeng < 0) || (spe < 0 && num < 0 && bigeng < 0) ){
	  alert("알파벳 대문자, 알파벳 소문자, 숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.");
	  return false;
	 }else if(hangulcheck.test(pw)){
	 alert("비밀번호에 한글을 사용 할 수 없습니다."); 
	 }else {
	 console.log("통과");
	 }


//////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
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
	var crefiaParam = {
		param: param
		,url: "/admin/crefia/saveCrefia"
		,success: function(opt, result) {
			if(result.code == "success"){
				PopUtil.closePopup();
				location.reload();
			}
    	}
	}
	AjaxUtil.post(crefiaParam);
	
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
					<col width="10%">
					<col width="23%">
					<col width="10%">
					<col width="23%">
					<col width="10%">
					<col width="23%">
				</colgroup>
				<tr>
					<th>이름</th>
					<td>
						<input type="text" name="memberName"/>
					</td>
					<th>아이디</th>
					<td>
						<input type="text" name="memberId"/>
					</td>
					<th>그룹</th>
					<td class="half_input">
						<select name="creGrp">
							<option value="">전체</option>
							<option value="2">관리자</option>
							<option value="1">실무자</option>
						</select>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data">
				<p>총 : 건수</p>
			</div>
			<div class="action">
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" id="regCrefia">등록하기</a>
				<a href="javascript:void(0);" class="btn_gray btn_small" id="deleteCrefia">삭제</a>
			</div>
		</div>
		<div id="crefiaGrid" class="long_table"></div>
	</div>
</div>
