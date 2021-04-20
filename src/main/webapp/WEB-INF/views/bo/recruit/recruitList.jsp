<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

var Grid = Object.create(GRID); 
function pageLoad(){
	Grid.set({
		  id		: "grid"
  		, url		: "/admin/recruit/recruitList"
	    , width		: "100%"
  		, headCol	: ["번호", "이름", "유형", "연락처", "이메일", "단계", "등록일", "오류내용", "첨부서류", "사용여부"]
  		, bodyCol	: 
  			[
				{type:"string"	, name:'recrNo'		, index:'recrNo'	, width:"5%"	}
				,{type:"string"	, name:'recrNm'		, index:'recrNm'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'recrType'	, index:'recrType'	, width:"5%"	, align:"center"}		
				,{type:"string"	, name:'recrMobile'	, index:'recrMobile', width:"15%"	, align:"center"}		
				,{type:"string"	, name:'recrEmail'	, index:'recrEmail'	, width:"23%"	, align:"center"}
				,{type:"string"	, name:'recrStep'	, index:'recrStep'	, width:"5%"	, align:"center"}
				,{type:"string"	, name:'recrRegDt'	, index:'recrRegDt'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'error'		, index:'error'		, width:"20%"	, align:"center"}	
				,{type:"string"	, name:'attach'		, index:'attach'	, width:"18%"	, align:"center"}	
				,{type:"string"	, name:'useYn'		, index:'useYn'		, width:"0px"	, hidden:true	}	
			]
		, sortNm : "recr_no"
		, sort : "DESC"
		, size : 10
		, rowClick	: {color:"#ccc", retFunc : detailPop}
		, gridSearch : "search"
	});
	
	document.getElementById('btn_pop').onclick = function () {
		let p = {
			id : 'excel_save'
			, curl : "/admin/recruit/p/recruitReg"
		}
		LibUtil.openPopup(p);
	};
	
	document.getElementById('btn_req').onclick = function () {
		alert('전체 승인요청');
	};
	
	var grpCode = {
		useCode : true
		,code : 'RECR_TYPE'
		,target : '#recrType'
	};
	console.log(grpCode);
	DataUtil.selectBox(grpCode);
	
	
	
	// 검증 테스트용
	$("#btn_ocr").on("click", function(){
		console.log("검증 시작 ");
		var param = {
				'recruitNo'	:	'RE20210420'
		}
 		var p = {
			  param : param
			, url : "/admin/recruit/ocrTest"
			, success : function (opt,result) {
				
				console.log("####" , result);
				
    	    }
		}
		
		AjaxUtil.post(p);
	});
	
}
function detailPop(idx, data){
	let p = {
		id : "dtl_save"
		, data : data
		, curl : "/admin/recruit/p/recruitDtl"
	}
	LibUtil.openPopup(p);
}
</script>

<div class="article_right">
	<h3  class="article_tit">모집인 등록</h3>
	
	<section class="k_dashboard">
	    <ul>
	        <li>
	            <a href="">
	                <span>전체</span>
	                <em class="">30</em>
	            </a>
	        </li>
	        <li>
	            <a href="n">
	                <span>승인요청</span>
	                <em>0</em>
	            </a>
	        </li>
	        <li>
	            <a href="">
	                <span>작성중</span>
	                <em>0</em>
	            </a>
	        </li>
	        <li>
	            <a href="">
	                <span>오류</span>
	                <em>0</em>
	            </a>
	        </li>
	    </ul>
	</section>
	
	<div class="k_search" id="search">
		<table class="searchbx" style="width:70%">
			<colgroup>
				<col width="5%">
				<col width="10%">
				<col width="5%">
				<col width="10%">
				<col width="5%">
				<col width="10%">
			</colgroup>
			<tbody>
				<tr>
					<td>
						<p class="red">이름</p>
					</td>
					<td><input type="text" name="title" class="" value="" style="width:100px;"/></td>
					<td>
						<p class="red">유형</p>
					</td>
					<td>
						<select id="recrType" name="recrType">
						</select>
					</td>
					<td>
						<p class="red">단계</p>
					</td>
					<td>
						<select name="recrStep">
							<option value="">전체</option>
							<option value="01">작성중</option>
							<option value="02">승인요청</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btnbx">
			<div>
			<button type="button" class="btn btn_home gridSearch">조회</button>
			<button type="button" class="btn btn_home" id="btn_pop">모집인 등록</button>
			<button type="button" class="btn btn_home" id="btn_req">선택승인요청</button>
			<button type="button" class="btn btn_home" id="btn_ocr">검증테스트</button>
			</div>
		</div>
	</div>
	<div id="grid"></div>
</div>