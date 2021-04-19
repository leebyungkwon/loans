<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="article_right">
	<section class="k_inputform">
		<h3 class="article_tit"><span>코드 마스터 저장</span></h3>
		<fieldset>
			<form name="codeMstSave" action="/bo/system/code/codeMstSave" method="POST">
				<input type="hidden" name="saveType" id="codeMstCdSaveType" value="reg"/>
				<input type="hidden" id="codeMstCdDupCheckResult" value="N"/>
				
				<table class="inputTable" style="width:500px;">
					<colgroup>
						<col width="30%">
						<col width="70%">
					</colgroup>
					<tbody>
						<tr>
							<td class="leftTd"><span class="type">코드마스터ID</span></td>
							<td class="rightTd">
								<input type="text" name="codeMstCd" id="pCodeMstCd" placeholder="코드마스터ID를 입력하세요." class="" value="" maxlength="10" data-vd='{"type":"text","len":"1,10","req":true,"msg":"코드마스터ID를 입력하세요."}'>
								<button type="button" id="goCodeMstCdDupCheck">중복체크</button>
							</td>
						</tr>
						<tr>
							<td class="leftTd"><span class="type">코드마스터명</span></td>
							<td class="rightTd">
								<input type="text" name="codeMstNm" id="pCodeMstNm" placeholder="코드마스터명을 입력하세요." class="" value="" maxlength="100" data-vd='{"type":"text","len":"1,100","req":true,"msg":"코드마스터명을 입력하세요."}'>
							</td>
						</tr>
						<tr>
							<td class="leftTd"><span class="type">설명</span></td>
							<td class="rightTd">
								<textarea name="codeMstDesc" id="pCodeMstDesc" rows="5" cols="1" placeholder="설명을 입력하세요." class="field_inp" maxlength="100"></textarea>
							</td>
						</tr>
						<tr>
							<td class="leftTd"><span class="type">(RADIO)사용</span></td>
							<td class="rightTd">
								<span class="rabx1">
								    <input type="radio" name="useYn" id="radioY" value="Y" checked="checked">
								    <label title="예" for="radioY">예</label>
								</span>
								<span class="rabx1">
								    <input type="radio" name="useYn" id="radioN" value="N">
								    <label title="아니오" for="radioN">아니오</label>
								</span>
							</td>
						</tr>
					</tbody>
				</table>
				
				<div class="btn_bx">
					<button type="button" class="btn_black" id="codeMstSaveBtn">저장</button>
					<!-- <button type="button" class="btn_black" id="btn_close">닫기</button> -->
				</div>
			</form>
		</fieldset>
	</section>
</div>

<script type="text/javascript">
(function() {
	//코드 마스터 아이디 중복체크
	document.getElementById("goCodeMstCdDupCheck").onclick = function () {
		if($("#pCodeMstCd").val() == ""){
			$("#pCodeMstCd").focus();
			alert("코드마스터ID를 입력하세요.");
			return;
		}
		
		var p = {
			  url		: "/bo/system/code/codeMstCdDupCheck"	
			, param		: {
				  codeMstCd : $("#pCodeMstCd").val()
			  }
			, success 	: function (opt,result) {
				if(result.data > 0){
					$("#codeMstCdDupCheckResult").val("N");
					$("#pCodeMstCd").focus();
					alert("이미 존재하는 ID입니다.");
					return;
				}else{
					$("#codeMstCdDupCheckResult").val("Y");
					alert("성공!");
				}
    	    }
		}
		AjaxUtil.post(p);
	};
	
	//저장
	document.getElementById("codeMstSaveBtn").onclick = function () {
		if($("#codeMstCdDupCheckResult").val() == "N"){
			alert("코드마스터ID 중복체크를 해주세요.");
			return;
		}
		var p = {
			  name 		: "codeMstSave"
			, success 	: function (opt,result) {
				codeMstGrid.refresh();
				//$("#btn_close").trigger("click");
    	    }
		}
		AjaxUtil.form(p);
	};
	
	/*
	//닫기
	document.getElementById("btn_close").onclick = function () {
		$(".closeLayer").trigger("click");
	};
	*/
})();
</script>
