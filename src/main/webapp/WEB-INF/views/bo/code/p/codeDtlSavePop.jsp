<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>코드상세 저장</h2>
		</div>
		<div class="info_box">
			<form name="codeDtlSave" action="/system/code/codeDtlSave" method="POST">
				<input type="hidden" name="saveType" id="codeDtlCdSaveType" value="reg"/>
				<input type="hidden" name="codeMstCd" id="hCodeMstCd"/>
			
				<table class="info_box_table">
					<colgroup>
						<col width="100"/>
						<col width="300"/>
					</colgroup>
					<tr>
						<th>코드마스터ID</th>
						<td id="codeMstCdText"></td>
					</tr>
					<tr>
						<th class="pdt10">코드상세ID</th>
						<td class="pdt10">
							<input type="text" name="codeDtlCd" id="pCodeDtlCd" class="" placeholder="코드상세ID를 입력하세요." maxlength="100">
							<!-- <button type="button" id="goCodeDtlCdDupCheck">중복체크</button> -->
						</td>
					</tr>
					<tr>
						<th class="pdt10">코드상세명</th>
						<td class="pdt10"> 
							<input type="text" name="codeDtlNm" id="pCodeDtlNm" class="" placeholder="코드상세명을 입력하세요." maxlength="100">
						</td>
					</tr>
					<tr>
						<th class="pdt10">설명</th>
						<td class="pdt10">
							<textarea rows="5" cols="1" name="codeDtlDesc" id="pCodeDtlDesc" class="w100" placeholder="설명을 입력하세요."  maxlength="100"></textarea>
						</td>
					</tr>
					<!-- 
					<tr>
						<th class="pdt10">사용여부</th>
						<td class="pdt10">
							<span class="rabx1">
							    <input type="radio" name="useYn" value="Y" checked="checked">
							    <label title="예" for="radioY">예</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="useYn" value="N">
							    <label title="아니오" for="radioN">아니오</label>
							</span>
						</td>
					</tr>
					 -->
				</table>
			</form>
			<a href="javascript:void(0);" class="btn_inquiry" id="codeDtlSaveBtn">저장</a>
			<!-- <a href="javascript:void(0);" class="btn_inquiry" id="codeMstResetBtn">초기화</a> -->
		</div>
	</div>
</div>

<script type="text/javascript">
//var codeDtlCdDupCheckResult = "N";

(function() {
	/*
	//코드 마스터 아이디 변경 시 이벤트
	document.getElementById("pCodeDtlCd").onchange = function () {
		codeDtlCdDupCheckResult = "N";
	};
	
	//코드 상세 아이디 중복체크
	document.getElementById("goCodeDtlCdDupCheck").onclick = function () {
		var p = {
			  url		: "/system/code/codeDtlCdDupCheck"	
			, param		: {
					 codeMstCd : $("#hCodeMstCd").val()  
					,codeDtlCd : $("#pCodeDtlCd").val()
			  }
			, success 	: function (opt,result) {
				codeDtlCdDupCheckResult = "Y";
    	    }
		}
		AjaxUtil.post(p);
	};
	*/
	
	//저장
	$("#codeDtlSaveBtn").on("click",function(){
		/*
		if(codeDtlCdDupCheckResult == "N"){
			alert("코드상세ID 중복체크를 해주세요.");
			return;
		}
		*/
		var p = {
			  name 		: "codeDtlSave"
			, success 	: function (opt,result) {
				codeDtlGrid.refresh();
				//$("#btn_close").trigger("click");
    	    }
		}
		AjaxUtil.form(p);
	});
})();
</script>	
