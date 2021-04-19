<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
var Grid = Object.create(GRID); 

function pageLoad(){
	Grid.set({
		  id		: "grid1"
  		, url		: "/bo/mem/list"
	    , width		: "100%"
  		, headCol	: ["번호", "아이디", "등록시간", "수정시간"]
  		, bodyCol	: 
  			[
				{type:"string"	, name:'id'			, index:'id'			, width:"10px"	}
			,	{type:"string"	, name:'email'		, index:'email'			, width:"30%"	, align:"center"}
			,	{type:"string"	, name:'regdate'	, index:'regdate'		, width:"30%"	, align:"center"}		
			,	{type:"string"	, name:'updatedate'	, index:'updatedate'	, width:"30%"	, sortable:false}	
			],
	});
}
</script>

<div class="article_right">
	<h3  class="article_tit">회원관리</h3>
	<div class="line"></div>
	<div class="k_search pc_view">
		<div class="btnbx">
			<a class="btn btn_home" href="/bo/board/reg">등록</a>
		</div>
	</div>
	<div  id="grid1"></div>
	
	
</div>