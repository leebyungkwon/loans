<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

var menualFileAttachId;

(function() {
	document.getElementById('btn_save').onclick = function () {
 		var p = {
			  name : 'save'
			, success : function (opt,result) {
				//Grid.refresh();
				//$("#btn_close").trigger('click');
    	    }
		}
		AjaxUtil.files(p);
	};
	document.getElementById('btn_close').onclick = function () {
		$(".closeLayer").trigger('click');
	};
	
	
	// 2021-04-09 첨부파일 업로드 전 OCR 검증 시작
/* 	$("#u_file").change(function(){
		console.log("언제 시작?");
 		var p = {
			  name : 'save'
			, success : function (opt,result) {
				if(result.message == "01"){
					alert("성공");
					//$("#test").css("dfsdf");
				}else{
					alert("실패");
					//$("#test").css("dfsdf");
				}
    	    }
		}
 		$("#docCheck").attr("action", "/system/templete/docCheck");
		AjaxUtil.files(p);
	}); */
	
	
	
	// 첨부파일 개별 검증 로직
	$("#autoCheck").on("change", function(){
		var docType = $(this).siblings().val()
 		var p = {
			  name : 'save'
			, success : function (opt,result) {
				if(result.message == "01"){
					alert("추출된 주민등록번호는 ["+result.data+"] 입니다.")
				}else{
					alert("실패");
				}
    	    }
		}
 		$("#docCheck").attr("action", "/system/templete/"+docType);
		AjaxUtil.files(p);
	});
	
	
	// 파일업로드시 파일 저장 후 검증에 필요한 데이터 전달
	$("#brFiles").on("change",function(){
 		var p = {
			  name : 'save'
			, success : function (opt,result) {
				// 파일 업로드 후 수동검증에 필요한 key
				menualFileAttachId = result.data.fileSeq;
    	    }
		}
 		$("#docCheck").attr("action", "/system/templete/verificationUpload");
		AjaxUtil.files(p);
		
	});
	
	
	// 개별 수동 검증
	$("#manualCheck").on("click", function(){
		console.log("수동검증 클릭했습니다." + menualFileAttachId);
		var param = {
				'fileSeq'	:	menualFileAttachId
		}
 		var p = {
			  param : param
			, url : "/system/templete/menualCheck"
			, success : function (opt,result) {
				alert("추출된 법인등록번호는 ["+result.data + "] 입니다.");
    	    }
		}
		
		AjaxUtil.post(p);
	});
	
	$("#cfFiles").on("change", function(){
		var docType = $(this).siblings().val()
 		var p = {
			  name : 'save'
			, success : function (opt,result) {
				console.log("결과값 :::: " , result);
				alert("문서번호 : "+result.data.cfNo+", \n과정명 : "+result.data.cfCurNm);
    	    }
		}
 		$("#docCheck").attr("action", "/system/templete/"+docType);
		AjaxUtil.files(p);
	});
	
	
	// 검증
	$("검증").on("click", function(){
		var ocrArrayParam = new Array();
		$("input:checkbox[name=ocrArray]:checked").each(function(){
			ocrArrayParam.push($(this).val());
		});		
	});
	
})();

</script>	
<div class="article_right">
	<section class="k_inputform">
			<h3  class="article_tit"><span>팝업템플릿2저장하기</span></h3>
			<fieldset>
			<!-- <form name="save" action="/system/templete/img" method="POST" enctype="multipart/form-data"> -->
			<form name="save" action="/system/templete/zip" method="POST" enctype="multipart/form-data" id="docCheck">
			<input type="hidden" name="boardNo" value=""/>
			<input type="hidden" name="boardType" value="NOTICE"/>
			<input type="hidden" name="docType" value=""/>
			<table class="inputTable" style="width:700px;">
				<colgroup>
					<col width="30%">
					<col width="60%">
					<col width="10%">
				</colgroup>
				<tbody>
					<tr>
						<td class="leftTd"><span class="type">제목</span></td>
						<td class="rightTd" colspan="2">
							<input type="text" name="boardTitle" id="u_title" placeholder="제목을 입력하세요." class="" >
						</td>
					</tr>
					<tr>
						<td class="leftTd"><span class="type">내용</span></td>
						<td class="rightTd" colspan="2">
							<textarea id="u_content" name="boardCnts" rows="5" cols="1" placeholder="내용을 입력하세요." class="field_inp" ></textarea>
						</td>
					</tr>
 					<tr>
						<td class="leftTd"><span class="type">(SELECT)사용</span></td>
						<td class="rightTd" colspan="2">
							<select name="useYn">
								<option value="Y">예</option>
								<option value="N">아니오</option>
							</select>
						</td>
					</tr>
<!-- 					<tr>
						<td class="leftTd"><span class="type">(RADIO)사용</span></td>
						<td class="rightTd" colspan="2">
							<span class="rabx1">
							    <input type="radio" name="useYn1" id="radioY" value="Y">
							    <label title="예" for="radioY">예</label>
							</span>
							<span class="rabx1">
							    <input type="radio" name="useYn1" id="radioN" value="N">
							    <label title="아니오" for="radioN">아니오</label>
							</span>
						</td>
					</tr> -->
					<tr>
						<td class="leftTd"><span class="type">(CHECKBOX)사용</span></td>
						<td class="rightTd" colspan="2">
							<span class="ckbx1">
						    	<input type="checkbox" name="useYn2" id="chkY" value="Y">
						    	<label class="checkbox" title="예" for="chkY">예</label>
							</span>
							<span class="ckbx1">
						    	<input type="checkbox" name="useYn2" id="chkN" value="N">
						    	<label class="checkbox" title="아니오" for="chkN">아니오</label>
							</span>
						</td>
					</tr>
					
					<tr>
						<td class="leftTd"><span class="type">압축파일업로드</span></td>
						<td class="rightTd">
							<input type="file" id="u_file" class="" name="files" multiple="multiple">
						</td>
					</tr>
					
					<tr>
						<td class="leftTd"><span class="type">주민등록증(자동)</span></td>
						<td class="rightTd">
							<input type="hidden" value="juminFiles">
							<input type="file" id="autoCheck" name="juminFiles" multiple="multiple">
							
						</td>
					</tr>
					
					<tr>
						<td class="leftTd"><span class="type">사업자등록증(수동)</span></td>
						<td class="rightTd">
							<input type="hidden" value="brFiles">
							<input type="file" id="brFiles" name="brFiles" multiple="multiple">
						</td>
						<td class="rightTd">
							<button type="button" id="manualCheck">수동검증</button>
						</td>
					</tr>
					
					<tr>
						<td class="leftTd"><span class="type">수료증(자동)</span></td>
						<td class="rightTd">
							<input type="hidden" value="cfFiles">
							<input type="file" id="cfFiles" name="cfFiles" multiple="multiple">
						</td>
					</tr>

					<!-- 
					<tr>
						<td class="leftTd"><span class="type">엑셀업로드</span></td>
						<td class="rightTd">
							<input type="file" id="u_file" class="" name="files" multiple="multiple">
						</td>
					</tr>
					 -->
				</tbody>
			</table>
			
			<div class="btn_bx">
				<button type="button" class="btn_black" id="btn_save">저장하기</button>
				<button type="button" class="btn_black" id="btn_close">닫기</button>
			</div>
		</form>
		</fieldset>
	</section>
</div>
