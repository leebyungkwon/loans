<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//전문인력 엑셀 업로드
	$("#").on("change", function () {
		alert("업무수행에 필요한 전문성을 갖춘 인력에 관한 사항 엑셀 업로드 시작");
	});
}

//수정
function goUserRegInfoUpdt() {
	//수정
	var p = {
		  name 		: "userRegInfoUpdFrm"
		, success 	: function (opt,result) {
			if(result.data > 0){
				alert("저장되었습니다.");
				location.reload();
			}
 	    }
	}
	AjaxUtil.files(p);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap">
		<ul>
			<li class="on"><a href="/member/user/userRegCorpDetail" class="single">등록정보</a></li>
			<li><a href="member_regist_corporate_tap02.html">대표자 및 임원관련<br />사항</a></li>
			<li><a href="member_regist_corporate_tap03.html">전문성 인력에<br />관한 사항</a></li>
			<li><a href="member_regist_corporate_tap04.html">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="member_regist_corporate_tap05.html" class="single">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div class="btn_wrap02">
		<div class="right">
			<a href="javascript:void(0);" class="btn_blue btn_middle" onclick="">저장</a>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
		<div class="contents">
			<div id="table">
				<table class="view_table">
					
				</table>
			</div>
	
			<h3></h3>
			<div id="">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					
				</table>
			</div>
	
			<div class="btn_wrap">
				<a href="javascript:void(0);" class="btn_gray" onclick="">목록</a>
			</div>
		</div>
	</form>
</div>

