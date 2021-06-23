<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
var userRegGrid = Object.create(GRID);

function pageLoad(){
	//모집인 등록 그리드
	userRegGrid.set({
		  id			: "userRegGrid"
  		, url			: "/member/user/userRegList"
	    , width			: "100%"
	    , check			: true					//체크박스 생성
  		, headCol		: ["번호", "담당자", "", "모집인분류", "법인사용인<br>여부", "취급상품", "이름", "주민번호", "휴대폰번호", "법인명", "법인번호", "등록일", "", "첨부서류", "", "승인상태"]
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'			, width:"10px"		, id:true}
				,{type:"string"	, name:'memberNm'		, index:'memberNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'			, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plClassNm'		, index:'plClassNm'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'corpUserYn'		, index:'corpUserYn'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plProductNm'	, index:'plProductNm'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMName'		, index:'plMName'			, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMZId'			, index:'plMZId'			, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plCellphone'	, index:'plCellphone'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName'	, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plMerchantNo'	, index:'plMerchantNo'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'regTimestamp'	, index:'regTimestamp'		, width:"15%"		, align:"center"}
				,{type:"string"	, name:'fileCompYn'		, index:'fileCompYn'		, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'fileCompTxt'	, index:'fileCompTxt'		, width:"10%"		, align:"center"}
				,{type:"string"	, name:'plStat'			, index:'plStat'			, width:"0%"		, align:"center" , hidden:true}
				,{type:"string"	, name:'plStatNm'		, index:'plStatNm'			, width:"10%"		, align:"center"}
			]
		, sortNm 		: "master_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : goUserRegDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
	});
	
	//처리상태
 	var plStatCode = {
		 useCode 	: true
		,code 		: 'MAS001'
		,target 	: '#plStat'
		,property01 : 'Y'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plStatCode);
	
	//모집인 분류
	var plClassCode = {
		 useCode 	: true
		,code 		: 'CLS001'
		,target 	: '#plClass'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plClassCode);
	
	//취급상품
	var plProductCode = {
		 useCode 	: true
		,code 		: 'PRD001'
		,target 	: '#plProduct'
		,updData 	: ''
		,defaultMsg : '전체'
	};
	DataUtil.selectBox(plProductCode);
	
	//datepicker
	$("#date_cal01").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	: function(dateText1,inst) {
			$("#srchDate1").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal02").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	: function(dateText1,inst) {
			$("#srchDate2").val(dateText1);
			$(this).hide();
		}
	});
}

$(document).mouseup(function(e){
	var calendar01 = $(".calendar01");
	if(calendar01.has(e.target).length === 0){
		calendar01.hide();
	}
});

//모집인 등록 row 클릭 이벤트
function goUserRegDetail(idx, data){
	var masterSeq 	= userRegGrid.gridData[idx].masterSeq;
	var plClass		= userRegGrid.gridData[idx].plClass;
	
	if(plClass == "1"){
		//개인
		$("#userRegDetailFrm").attr("action","/member/user/userRegIndvDetail");
	}else if(plClass == "2"){
		//법인
		$("#userRegDetailFrm").attr("action","/member/user/userRegCorpDetail");
	}
	
	$("#hMasterSeq").val(masterSeq);
	$("#userRegDetailFrm").submit();
}

//모집인 등록하기 팝업 열기
function goUserRegPopOpen() {
	let p = {
		  id 		: "userRegExcelUploadPop"
		, url 		: "/member/user/userRegExcelPopup"
		, success	: function(opt, result) { 
			
			$('input:radio[name="plClass"]').on("click", function(){
				var indv = '<a href="/static/sample/모집인등록_개인_샘플.xlsx" download class="btn_Lgray btn_small mgl5" id="indvSample">샘플 다운로드</a>';
				var corp = '<a href="/static/sample/모집인등록_법인_샘플.xlsx" download class="btn_Lgray btn_small mgl5" id="corpSample">샘플 다운로드</a>';
				if($('input:radio[name="plClass"]:checked').val() == "1"){
					$("#indvSample").remove();
					$("#corpSample").remove();
					$("#sampleCheck").append(indv);	
				}else{
					$("#indvSample").remove();
					$("#corpSample").remove();
					$("#sampleCheck").append(corp);
				}
			});
        }
	}
	PopUtil.openPopup(p);
}

//첨부파일명 보여주기
function goFileNmShow(obj) {
	var fileVal 	= $("#userRegFile").val().split("\\");
	var fileName 	= fileVal[fileVal.length - 1];
	$("#userRegFile").prev().val(fileName);
	
	//첨부파일 확장자 체크
	var ext = fileName.split(".").pop().toLowerCase();
	if(!Valid.fileCheck(ext,"Y")){
		$("#userRegFile").val("");
		$("#userRegFile").prev().val("");
	}
}

//모집인 등록하기
function goUserRegInfoExcelUpload() {
	if(!$('input[name="plClass"]').is(":checked")){
		alert("모집인유형을 선택해 주세요.");
		return;
	}else{
		var plClass = $('input[name="plClass"]:checked').val();
		
		if(plClass == "1"){
			$("#userRegInfoInsertFrm").attr("action","/member/user/insertUserRegIndvInfoByExcel");
		}else if(plClass == "2"){
			$("#userRegInfoInsertFrm").attr("action","/member/user/insertUserRegCorpInfoByExcel");
		}
	}
	if(WebUtil.isNull($("#userRegFile").val())){
		alert("엑셀 파일을 업로드해 주세요.");
		return;
	}
	if(confirm("모집인을 등록하시겠습니까?")){
		var p = {
			  name 		: "userRegInfoInsertFrm"
			, success 	: function (opt,result) {
				if(WebUtil.isNotNull(result.data)){
					
					var html = '';
					
					html += '<div class="title_wrap">';
					html += '<h5>모집인 등록</h5>';
					html += '<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>';
					html += '</div>';
					html += '<p class="popup_desc" style="line-height: 30px;">'+result.data+'</p>';
					html += '<div class="popup_btn_wrap">';
					html += '<a href="javascript:void(0);" class="pop_btn_black" onclick="PopUtil.closePopup();">확인</a>';
					html += '</div>';
					
					$(".popup_inner").empty();
					$(".popup_inner").append(html);
					$(".popup_wrap").show();
					
				}else{
					location.reload();	
				}
	 	    }
		}
		AjaxUtil.files(p);	
	}
}

//선택 승인요청 -> 필수 첨부파일 하나라도 없으면 요청 불가 *****
function goApplyAccept() {
	var chkedLen 	= $("tbody > tr > td > input:checkbox:checked").length;
	
	if(chkedLen == 0){
		alert("모집인을 선택해 주세요.");
		return;
	}
	
	var chkData 		= userRegGrid.getChkData();
	var masterSeqArr	= [];
	var fileChk			= 0;
	var pvtChk			= 0;
	
	for(var i = 0;i < chkedLen;i++){
		if(chkData[i].plStat != "2" && chkData[i].plStat != "10"){
			masterSeqArr.push(chkData[i].masterSeq);
		}
		if(chkData[i].plStat == "10"){
			pvtChk++;
		}
		if(chkData[i].fileCompYn == "N"){
			fileChk++;
		}
	}
	
	if(pvtChk > 0){
		alert("등록요건 불충족인 건이 존재합니다.");
		return;
	}
	if(fileChk > 0){
		alert("필수첨부서류 업로드가 미완료된 건이 존재합니다.");
		return;
	}
	if(masterSeqArr.length == 0){
		alert("선택하신 모집인은 이미 승인요청된 상태입니다.");
		return;
	}
	
	if(confirm("승인요청하시겠습니까?")){
		var p = {
			  url		: "/member/user/userAcceptApply"	
			, param		: {
				 masterSeqArr 	: masterSeqArr  
				,plStat			: '2'
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("승인요청되었습니다.");
					location.href = "/member/user/userRegPage";
				}else if(result.data == -2){
					alert("이미 승인완료된 모집인입니다.");
					userRegGrid.refresh();
				}else if(result.data == -1){
					alert("법인이 승인되지 않은 법인사용자가 존재합니다.");
					userRegGrid.refresh();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//날짜 가져오기
function goGetDate(opt) {
	var result = WebUtil.getDate(opt);
	$("#srchDate1").val(result);
	$("#srchDate2").val(WebUtil.getDate("today"));
}
</script>

<form id="userRegDetailFrm" method="post">
	<input type="hidden" name="masterSeq" id="hMasterSeq"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>모집인 등록</h2>
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
					<th>담당자</th>
					<td class="half_input">
						<input type="text" name="memberNm">
					</td>
					<th>모집인 분류</th>
					<td class="half_input">
						<select name="plClass" id="plClass"></select>
					</td>
					<th>취급상품</th>
					<td class="half_input">
						<select name="plProduct" id="plProduct"></select>
					</td>
				</tr>
				<tr>
					<th>승인상태</th>
					<td class="half_input">
						<select name="plStat" id="plStat"></select>
					</td>
					<th>첨부상태</th>
					<td class="half_input">
						<select name="fileCompYn">
							<option value="">전체</option>
							<option value="Y">완료</option>
							<option value="N">미완료</option>
						</select>
					</td>
				</tr>
				<tr>
				</tr>
				<tr>
					<th>이름</th>
					<td class="">
						<input type="text" name="plMName">
					</td>
					<th>주민등록번호</th>
					<td class="">
						<input type="text" name="plMZId" placeholder="-를 빼고 입력해주세요.">
					</td>
					<th>휴대폰번호</th>
					<td class="">
						<input type="text" name="plCellphone">
					</td>
				</tr>
				<tr>
					<th>법인명</th>
					<td class="">
						<input type="text" name="plMerchantName">
					</td>
					<th>법인번호</th>
					<td class="">
						<input type="text" name="plMerchantNo" placeholder="-를 빼고 입력해주세요.">
					</td>
				</tr>
				<tr>
					<th>등록기간 조회</th>
					<td colspan="5" class="long_input">
						<div class="input_wrap">
                			<input type="text" name="srchDate1" id="srchDate1" class="input_calendar" readonly="readonly">
                			<a class="calendar_ico" onclick="$('#date_cal01').show();"></a>
						 	<div id="date_cal01" class="calendar01"></div>
              			</div>
					  	~
					 	<div class="input_wrap mgr5">
							<input type="text" name="srchDate2" id="srchDate2" class="input_calendar" readonly="readonly">
							<a class="calendar_ico" onclick="$('#date_cal02').show();"></a>
							<div id="date_cal02" class="calendar01"></div>
						</div>
						<div class="date_btn">
							<a href="javascript:void(0);" onclick="goGetDate('today');">오늘</a>
							<a href="javascript:void(0);" onclick="goGetDate('1');">어제</a>
							<a href="javascript:void(0);" onclick="goGetDate('7');">1주일</a>
							<a href="javascript:void(0);" onclick="goGetDate('15');">15일</a>
							<a href="javascript:void(0);" onclick="goGetDate('oneMonthAgo');">1개월</a>
						</div>
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
				<a href="javascript:void(0);" class="btn_black btn_small mgr5" onclick="goUserRegPopOpen();">모집인 등록</a>
				<a href="javascript:void(0);" class="btn_gray btn_small" onclick="goApplyAccept();">선택 승인요청</a>
			</div>
		</div>
		<div id="userRegGrid" class="long_table"></div>
	</div>
</div>
