<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

var Grid = Object.create(GRID);

function pageLoad(){

	$( "#datepicker" ).datepicker({ dateFormat: 'yy-mm-dd' });

	var url = "/bo/templete/list";

	Grid.set({
		  id		: "grid"	//영역
  		, url		: url
	    , width		: "100%"
	    , check		: true		//체크박스 생성
  		, headCol	: ["번호", "타입", "제목", "등록시간", "등록시간", "수정", "파일", "경로"]	//헤더
  		, bodyCol	: //데이터 영역
  			[
				{type:"string"	, name:'boardNo'	, index:'boardNo'		, width:"10px"	, id:true}
				,{type:"string"	, name:'boardType'	, index:'boardType'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'boardTitle'	, index:'boardTitle'	, width:"30%"	, align:"center"}
				,{type:"string"	, name:'regDate'	, index:'regDate'		, width:"30%"	, sortTable:false}
				,{type:"string"	, name:'regDate'	, index:'regDate'		, width:"30%"	, sortTable:false, hidden:true}
				,{type:"string"	, name:'btn'		, index:'btn'			, width:"30%"	, sortTable:false
						, button:{"수정": "modify","삭제": "del"}
				}
				,{type:"string"	, name:'fileNm'		, index:'fileNm'		, width:"30%"	, sortTable:false, hidden:true}
				,{type:"string"	, name:'orgFile'	, index:'orgFile'		, width:"30%"	, sortTable:false, hidden:true}
			]
		, sortTable:true
		, sortNm : "board_no"				//정렬 필드
		, sort : "DESC"						//정렬 방법
		, rowClick	: { color:"#ccc",  retFunc : detailPop}		//클릭시 리턴
		, gridSearch : "searchDiv,searchBtnId"				//검색영역 (검색영역,검색버튼)
		, isPaging : true					//페이징여부
		, excel : "/bo/templete/excelDown"
		, initTable : false
		, size : 10
	});


	document.getElementById('btn_pop1').onclick = function () {
		console.log(Grid.getRowData().boardNo);
		let p = {
			id : 'testReg'
			, params : {"boardNo" : Grid.getRowData().boardNo}
			, page : "/bo/templete/p/popTempleteSave"
		}
		LibUtil.openPopup(p);
	};
	document.getElementById('btn_pop2').onclick = function () {
		let p = {
			id : 'testReg'
			, page : "/bo/templete/p/templeteFileSave"
		}
		LibUtil.openPopup(p);
	};

	document.getElementById('btn_page').onclick = function () {
		location.href = "/bo/board/noticeReg";
	};
}
function modify(idx){
	alert("수정 "+Grid.gridData[idx].boardNo);
}
function del(idx){
	alert("삭제 "+Grid.gridData[idx].boardNo);
}
function detail(idx, data){
	location.href = "/bo/templete/templeteSave?boardNo="+data.boardNo;
}
function detailPop(idx, data){
	let p = {
		id : "testReg"
		, data : data
		, url : "/bo/templete/p/templeteSave"
	}
	LibUtil.openPopup(p);
}
</script>

<div class="article_right">
	<h3  class="article_tit">템플릿 1</h3>
	<div class="k_search" id="searchDiv">
		<table class="searchbx" style="width:70%">
			<colgroup>
				<col width="5%">
				<col width="10%">
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
						<p class="red">제목</p>
					</td>
					<td><input type="text" name="boardTitle" class="" value=""></td>
					<td>
						<p>타입</p>
					</td>
					<td><input type="text" name="boardType" class=""></td>
					<td>
						<p>내용</p>
					</td>
					<td><input type="text" name="boardCnts" class=""></td>
					<td>
						<p>날짜</p>
					</td>
					<td><input type="text" name="" id="datepicker" class="" readonly></td>
				</tr>
			</tbody>
		</table>
		<div class="btnbx">
			<div>
			<button type="button" class="btn btn_home" id="searchBtnId">조회</button>
			</div>
			<div>
			<button type="button" class="btn btn_home" id="btn_page">[페이지이동]등록</button>
			</div>
			<div>
			<button type="button" class="btn btn_home" id="btn_pop1" >[레이어]등록</button>
			<button type="button" class="btn btn_home" id="btn_pop2" >[레이어]파일등록</button>
			</div>
		</div>
	</div>
	<div id="grid"></div>
</div>
