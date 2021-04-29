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
  		, headCol		: ["번호", "담당자", "모집인분류", "취급상품", "이름", "주민번호", "휴대폰번호", "법인명", "법인번호", "등록일", "첨부서류", "승인상태"] /*"사용인이름", "사용인주민번호",*/
  		, bodyCol		: 
  			[
				 {type:"string"	, name:'masterSeq'		, index:'masterSeq'		, id:true}
				,{type:"string"	, name:'memberSeq'		, index:'memberSeq'		, align:"center"}
				,{type:"string"	, name:'plClass'		, index:'plClass'		, align:"center"}
				,{type:"string"	, name:'plProduct'		, index:'plProduct'		, align:"center"}
				,{type:"string"	, name:'plMName'		, index:'plMName'		, align:"center"}
				,{type:"string"	, name:'plMZId'			, index:'plMZId'		, align:"center"}
				,{type:"string"	, name:'plCellphone'	, index:'plCellphone'	, align:"center"}
				,{type:"string"	, name:'plMerchantName'	, index:'plMerchantName', align:"center"}
				,{type:"string"	, name:'plMerchantNo'	, index:'plMerchantNo'	, align:"center"}
				,{type:"string"	, name:'regTimestamp'	, index:'regTimestamp'	, align:"center"}
				,{type:"string"	, name:'fileSeq'		, index:'fileSeq'		, align:"center"}
				,{type:"string"	, name:'plRegStat'		, index:'plRegStat'		, align:"center"}
			]
		, sortNm 		: "master_seq"
		, sort 			: "DESC"
		, rowClick		: {retFunc : goUserRegDetail}
		, gridSearch 	: "searchDiv,searchBtn" //검색영역ID,조회버튼ID
		, isPaging 		: true					//페이징여부
		, size 			: 10
	});
}

//모집인 등록 row 클릭 이벤트
function goUserRegDetail(idx, data){
	var masterSeq = userRegGrid.gridData[idx].masterSeq;
	$("#hMasterSeq").val(masterSeq);
	$("#userRegDetailFrm").submit();
}

//모집인 등록하기 팝업 열기
function goUserRegPopOpen() {
	$("#userRegPopDiv").show();
}

//모집인 등록하기 팝업 닫기
function goUserRegPopClose() {
	$("#userRegPopDiv").hide();
}

//모집인 등록하기
function goUserReg() {
	
}

//선택 승인요청
function goApplyAccept() {
	var chkedLen 	= $("input:checkbox:checked").length;
	
	if(chkedLen == 0){
		alert("모집인을 선택해 주세요.");
		return;
	}
	
	var chkData 		= userRegGrid.getChkData();
	var masterSeqArr	= [];
	
	for(var i = 0;i < chkedLen;i++){
		masterSeqArr.push(chkData[i].masterSeq);
	}
	
	var p = {
		  url		: "/member/user/updatePlRegStat"	
		, param		: {
			masterSeqArr : masterSeqArr  
		}
		, success 	: function (opt,result) {
			if(result.data > 0){
				alert("승인요청되었습니다.");
				userRegGrid.refresh();
			}
	    }
	}
	AjaxUtil.post(p);
}
</script>

<form id="userRegDetailFrm" method="post" action="/member/user/userRegDetail">
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
					<col width="120">
					<col width="305">
					<col width="120">
					<col width="305">
				</colgroup>
				<tr>
					<th>담당자</th>
					<td class="half_input">
						<select>
							<option value="">전체</option>
						</select>
					</td>
					<th>모집인분류</th>
					<td class="half_input">
						<select>
							<option value="">전체</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>취급상품</th>
					<td class="half_input">
						<select>
							<option value="">전체</option>
						</select>
					</td>
					<th>검색어</th>
					<td class="half_input pdr0">
						<select>
							<option value="">전체</option>
						</select>
						<input type="text" name="ttt">
					</td>
				</tr>
				<tr>
					<th>첨부상태</th>
					<td class="half_input">
						<select>
							<option value="">전체</option>
							<option value="">완료</option>
							<option value="">미완료</option>
						</select>
					</td>
					<th>승인상태</th>
					<td class="half_input">
						<select name="plRegStat">
							<option value="">전체</option>
							<option value="1">미요청</option>
							<option value="2">승인요청</option>
							<option value="3">승인반려</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>등록기간 조회</th>
					<td colspan="3" class="long_input">
						<div class="input_wrap">
                			<input type="text" value="2020.05.19" class="input_calendar">
                			<a class="calendar_ico" onclick="$('#date_cal01').show();"></a>
						 	<div id="date_cal01" class="calendar01"></div>
              			</div>
					  	~
					 	<div class="input_wrap mgr5">
							<input type="text" value="2020.05.19" class="input_calendar">
							<a class="calendar_ico" onclick="$('#date_cal02').show();"></a>
							<div id="date_cal02" class="calendar02"></div>
						</div>
						<div class="date_btn">
							<a href="#">오늘</a>
							<a href="#">어제</a>
							<a href="#">1주일</a>
							<a href="#">15일</a>
							<a href="#">1개월</a>
						</div>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtn">조회</a>
		</div>
	</div>
	
	<div class="contents">
		<div class="sorting_wrap">
			<div class="data">
				<p>총 : 40건</p>
			</div>
			<div class="action">
				<a href="javascript:goUserRegPopOpen();" class="btn_black btn_small mgr5">모집인 등록하기</a> <!-- id="userRegPop" -->
				<a href="javascript:goApplyAccept();" class="btn_gray btn_small">선택 승인요청</a> <!-- id="applyAccept" -->
			</div>
		</div>
		<div id="userRegGrid" class="long_table"></div>
	</div>
</div>

<!-- 모집인 등록 팝업 -->
<div class="popup_wrap" id="userRegPopDiv">
	<div class="popup_inner">
		<div class="title_wrap">
			<h5>모집인 등록</h5>
			<a href="javascript:goUserRegPopClose();" class="pop_close"></a>
		</div>
		<p class="popup_desc">
			모집인 등록은 등록 유형에 따른 샘플파일을 다운로드 하시고 해당 양식에 따라 등록되어야 합니다.<br />
			엑셀 업로드 이후에는 각 모집인별로 첨부파일을 등록완료 후에 승인신청을 하셔야 합니다.
		</p>
		<table class="popup_table">
			<colgroup>
				<col width="170">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>모집인유형</th>
					<td>
						<div class="input_radio_wrap">
							<input type="radio" id="radio01" name="member_sel">
							<label for="radio01">개인</label>
						</div>
						<div class="input_radio_wrap mgl20">
							<input type="radio" id="radio02" name="member_sel">
							<label for="radio02">법인</label>
						</div>
						<div class="input_radio_wrap mgl20">
							<input type="radio" id="radio02" name="member_sel">
							<label for="radio02">법인소속 사용인</label>
						</div>
					</td>
				</tr>
				<tr>
					<th>엑셀업로드</th>
					<td class="file">
						<input type="text"class="w50 file_input"  value="" readonly disabled>
							<a href="#" class="btn_gray btn_del mgl5">삭제</a>
							<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
						<a href="#" class="btn_Lgray btn_small mgl5">샘플 다운로드</a>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="popup_btn_wrap">
			<a href="javascript:goUserReg();" class="pop_btn_black">저장</a>
			<a href="javascript:goUserRegPopClose();" class="pop_btn_white">취소</a>
		</div>
	</div>
</div>

