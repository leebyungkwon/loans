<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>코드마스터 저장</h2>
		</div>
		<div class="info_box">
			<form name="codeMstSave" action="/system/code/codeMstSave" method="POST">
				<input type="hidden" name="saveType" id="codeMstCdSaveType" value="reg"/>
			
				<table class="info_box_table">
					<colgroup>
						<col width="100"/>
						<col width="300"/>
					</colgroup>
					<tr>
						<th>코드마스터ID</th>
						<td>
							<input type="text" name="codeMstCd" id="pCodeMstCd" class="" placeholder="코드마스터ID를 입력하세요." maxlength="10">
							<!-- <button type="button" id="goCodeMstCdDupCheck">중복체크</button> -->
						</td>
					</tr>
					<tr>
						<th class="pdt10">코드마스터명</th>
						<td class="pdt10"> 
							<input type="text" name="codeMstNm" id="pCodeMstNm" class="" placeholder="코드마스터명을 입력하세요." maxlength="100">
						</td>
					</tr>
					<tr>
						<th class="pdt10">설명</th>
						<td class="pdt10">
							<textarea rows="5" cols="1" name="codeMstDesc" id="pCodeMstDesc" class="w100" placeholder="설명을 입력하세요." maxlength="100"></textarea>
						</td>
					</tr>
					<!-- 
					<tr>
						<th class="pdt10">사용여부</th>
						<td class="pdt10">
							<span class="pdr20">
							    <input type="radio" name="useYn" value="Y" checked="checked">
							    <label title="예" for="radioY">예</label>
							</span>
							<span>
							    <input type="radio" name="useYn" value="N">
							    <label title="아니오" for="radioN">아니오</label>
							</span>
						</td>
					</tr>
					 -->
				</table>
			</form>
			<a href="javascript:void(0);" class="btn_inquiry" id="codeMstSaveBtn">저장</a>
			<!-- <a href="javascript:void(0);" class="btn_inquiry" id="codeMstResetBtn">초기화</a> -->
		</div>
	</div>
</div>

<script type="text/javascript">
//var codeMstCdDupCheckResult = "N";

(function() {
	/*
	//코드 마스터 아이디 변경 시 이벤트
	document.getElementById("pCodeMstCd").onchange = function () {
		codeMstCdDupCheckResult = "N";
	};
	
	//코드 마스터 아이디 중복체크
	document.getElementById("goCodeMstCdDupCheck").onclick = function () {
		var p = {
			  url		: "/system/code/codeMstCdDupCheck"	
			, param		: {
				  codeMstCd : $("#pCodeMstCd").val()
			  }
			, success 	: function (opt,result) {
				codeMstCdDupCheckResult = "Y";
    	    }
		}
		AjaxUtil.post(p);
	};
	*/
	//저장
	document.getElementById("codeMstSaveBtn").onclick = function () {
		/*
		if(codeMstCdDupCheckResult == "N"){
			alert("코드마스터ID 중복체크를 해주세요.");
			return;
		}
		*/
		var p = {
			  name 		: "codeMstSave"
			, success 	: function (opt,result) {
				codeMstGrid.refresh();
				//$("#btn_close").trigger("click");
    	    }
		}
		AjaxUtil.form(p);
	};
})();
</script>
