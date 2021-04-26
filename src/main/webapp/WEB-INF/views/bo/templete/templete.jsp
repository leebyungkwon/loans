<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

var Grid = Object.create(GRID);

function pageLoad(){
	Grid.set({
		  id		: "grid"	//영역
  		, url		: "/system/templete/list"
	    , width		: "100%"
	    , check		: true		//체크박스 생성
  		, headCol	: ["번호", "타입", "제목", "등록시간", "등록시간", "수정"]	//헤더
  		, bodyCol	: //데이터 영역
  			[
				{type:"string"	, name:'boardNo'	, index:'boardNo'		, width:"10px"	, id:true}
				,{type:"string"	, name:'boardType'	, index:'boardType'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'boardTitle'	, index:'boardTitle'	, width:"30%"	, align:"center"}
				,{type:"string"	, name:'regDate'	, index:'regDate'		, width:"30%"	, sortTable:false}
				,{type:"string"	, name:'regDate'	, index:'regDate'		, width:"30%"	, sortTable:false, hidden:true}
				,{type:"string"	, name:'btn'		, index:'btn'			, width:"30%"	, sortTable:false, button:{"수정": "modify","삭제": "del"}}
			]
		, sortTable:true
		, sortNm : "board_no"				//정렬 필드
		, sort : "DESC"						//정렬 방법
		, rowClick	: {/* color:"#ccc",  */retFunc : detailPop}		//클릭시 리턴
		, gridSearch : "searchDiv,searchBtnId"				//검색영역 (검색영역,검색버튼)
		, isPaging : true					//페이징여부
		, excel : "/system/templete/excelDown"
		//, initTable : false
		, size : 10
	});


	document.getElementById('btn_pop1').onclick = function () {
		let p = {
			id : 'testReg'
			, url : "/system/templete/p/templeteSave"
		}
		LibUtil.openPopup(p);
	};
	document.getElementById('btn_pop2').onclick = function () {
		let p = {
			id : 'testReg'
			, url : "/system/templete/p/templeteFileSave"
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
	location.href = "/system/templete/templeteSave?boardNo="+data.boardNo;
}
function detailPop(idx, data){
	let p = {
		id : "testReg"
		, data : data
		, url : "/system/templete/p/templeteSave"
	}
	LibUtil.openPopup(p);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>템플릿 1</h2>
		</div>
		<div class="info_box k_search" id="searchDiv">
			<table class="info_box_table">
				<colgroup>
					<col width="80"/>
					<col width="200"/>
					<col width="80"/>
					<col width="200"/>
					<col width="80"/>
					<col width="200"/>
				</colgroup>
				<tr>
					<th>제목</th>
					<td>
						<input type="text" name="boardTitle"/>
					</td>
					<th>타입</th>
					<td>
						<input type="text" name="boardType"/>
					</td>
					<th>내용</th>
					<td>
						<input type="text" name="boardCnts"/>
					</td>
				</tr>
			</table>
			<a href="javascript:void(0);" class="btn_inquiry" id="searchBtnId">조회</a>
			<a href="javascript:void(0);" class="btn_inquiry" id="btn_page">[페이지이동]등록</a>
			<a href="javascript:void(0);" class="btn_inquiry" id="btn_pop1">[레이어]등록</a>
			<a href="javascript:void(0);" class="btn_inquiry" id="btn_pop2">[레이어]파일등록</a>
		</div>
	</div>
	<div id="grid"></div>
</div>

