<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">

function pageLoad(){
	
	// 회원가입 클릭
	$("#signupBtn").on("click", function(){
		if(confirm("회원가입을 진행 하시겠습니까?")){
			var id					= $("#memberId").val();
			var pw					= $("#password").val();               // 비밀번호
			var pwChk				= $("#passwordChk").val();            // 비밀번호 확인
			var fileName			= $("#fileName").val();
			var comCode				= $("#comCode").val();
			
			
			if (id.search(/\s/) != -1){ 
				alert("아이디는 공백 없이 입력해 주세요.");
				return false;
			}
			
			var checkCount = 0;
			
			if(comCode == 0){
				alert("회원사를 선택해 주세요.");
				return false;
			}
			if(WebUtil.isNull(id)){
				alert("아이디를 입력해 주세요.");
				$("#memberId").focus();
				return false;
			}
			if(/[0-9]/.test(pw)){ //숫자
			    checkCount++;
			}
			if(/[a-z]/.test(pw)){ //소문자
			    checkCount++;
			}
			if(/[A-Z]/.test(pw)){ //대문자
			    checkCount++;
			}
			if(/[~!@\#$%<>^&*\()\-=+_\’]/.test(pw)){ //특수문자
			    checkCount++;
			}
			if(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힝]/.test(pw)){ 
			    alert("비밀번호에 한글을 사용 할 수 없습니다.");
			    return false;
			}
			if(checkCount <= 1){ 
			    alert('비밀번호는 영문 대/소문자, 숫자, 특수문자 중 2개이상의 조합이여야만 합니다.');
			    return false;
			}
			if (pw.length < 8 || pw.length > 20){ 
				alert("비밀번호는 8자리 ~ 20자리 이내로 입력해 주세요.");
				return false;
			}
			if (/(\w)\1\1/.test(pw)){ 
				alert('같은 문자를 3번 이상 사용하실 수 없습니다.');
				return false;
			}
			if (pw.search(id) > -1){
				alert("비밀번호에 아이디가 포함되었습니다.");
				return false;
			}
			if (pw.search(/\s/) != -1){ 
				alert("비밀번호는 공백 없이 입력해 주세요.");
				return false;
			}
			if($("#checkId").val() == "N"){
				alert("중복체크를 실행해 주세요.");
				return false;
			}
			if(WebUtil.isNull(fileName)){
				alert("파일을 첨부해 주세요.");
				$("#fileName").focus();
				return false;
			}
			if( pw == pwChk ){
				var signupParam = {
					name : 'signup'
					,success: function(opt, result) {
						if(result.message == "success"){
							if(result.data == 0){
								alert("회원가입 신청이 완료되었습니다. \n승인 후에 로그인 가능합니다.");
								location.href="/login";
							}else if(result.data == 1){
								alert("이미 사용중인 아이디 입니다. \n중복체크를 다시 확인해 주세요.");
								return false;
							}else{
								alert(result.data);
								return false;
							}
						}else{
							alert(result.data[0].defaultMessage);
						}
					}
				}      
				AjaxUtil.files(signupParam);
			}else{
				$("#password").val("");
				$("#passwordChk").val("");
				alert("아이디 패스워드를 확인해 주세요.");
				return false;
			}
		}
	});
	
	// 첨부파일 삭제
	$("#fileDelete").on("click", function(){
		$("#fileName").val("");
	});
	
	// 첨부파일 찾기
	$("#fileSearch").on("click", function(){
		$("#u_file").click();
	});
	
	// 아이디 중복체크
	$("#idcheck").on("click", function(){
		var memberId = $("#memberId").val();
		if(WebUtil.isNull(memberId)){
			alert("아이디를 입력해 주세요.");
			return false;
		}
		
		var	param = {
				'memberId' : memberId
		}
	    var p = {
			param: param
			,url: "/idcheck"
            ,success: function(opt, result) {
            	if(result.message == "success"){
                	if(result.data > 0){
                        $("#memberId").val("");
                        $("#checkId").val("N");
                        alert("해당 아이디가 존재합니다.");    
                	}else{
                		$("#checkId").val("Y");
                		alert("사용가능 아이디 입니다.");
                	}
            	}else{
            		alert(result.data[0].defaultMessage);
            	}
   			}
		}
		AjaxUtil.post(p);
	});
	
	// 아이디 수정시 중복체크 value 변경
	$("#memberId").on("change", function(){
		$("#checkId").val("N");
	});
	
	// 첨부파일 찾기시 file tag 실행
	$("#u_file").on("change", function(){
		var ext = $(this).val().split(".").pop().toLowerCase();
		var excelYn = $(this).attr("data-excel");
		var fileSize = $("#u_file")[0].files[0].size;
		if(!Valid.fileCheck(fileSize, ext, excelYn)){
			return false;
		}
		
		var fileValue = $("#u_file").val().split("\\");
		var fileName = fileValue[fileValue.length-1];
		$("#fileName").val(fileName);
	});
	
	// 회원사 코드
 	var companyCode = {
		useCode : false
		,code : 'COM001'
		,target : '#comCode'
		,url : '/common/selectCompanyCodeList'
		,key : 'codeDtlCd'
		,value : 'codeDtlNm'
		,updData : ''
	};
	DataUtil.selectBox(companyCode);
}

function checkCapsLock(event){
	if(event.getModifierState("CapsLock")){
		$("#message").text("※ Caps Lock이 켜져 있습니다.");
	}else{
		$("#message").text("");
	}
}
</script>

<div class="page_title">
	<h2>회원가입</h2>
</div>
<form name="signup" id="signup" action="/signup" method="POST" enctype="multipart/form-data" >
	<input type="hidden" id="checkId" value="N"/>
	<input type="hidden" name="optionTermsYn" id="optionTermsYn" value="${termsData.optionTermsYn }"/>
	<div class="join_wrap">
		<table>
			<colgroup>
				<col width="200"/>
				<col width="*"/>
			</colgroup>
 			<tr>
				<th>회원사 선택</th>
				<td>
					<select id="comCode" name="comCode"></select>
				</td>
			</tr>
			<tr>
				<th>아이디</th>
				<td>
					<input type="text" id="memberId" name="memberId" placeholder="아이디 입력 (5~11자)" maxlength="11" data-vd='{"type":"id","len":"5,11","req":true,"msg":"아이디를 입력해 주세요"}'/>
					<a href="javascript:void(0);" id="idcheck" class="btn_gray btn_small">중복체크</a>
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" id="password" name="password" onkeyup="checkCapsLock(event);" maxlength="20" placeholder="8자리~20자리 (2종류 이상의 문자구성)"  data-vd='{"type":"text","len":"8,20","req":true,"msg":"비밀번호를 다시 입력해 주세요"}'/>
					<p class="noti">
						※ 알파벳 대문자, 알파벳 소문자, 특수문자, 숫자 중 2종류 이상을 선택하여 문자를 구성해야 합니다.<br>
						※ 아이디, 동일한 문자의 반복 및 연속된 3개의 숫자/문자는 사용이 불가능 합니다.
					</p>
					<div id="message" class="orange"></div>
				</td>
			</tr>
			<tr>
				<th>비밀번호 확인</th>
				<td>
					<input type="password" id="passwordChk" name="passwordChk" onkeyup="checkCapsLock(event);" maxlength="20" placeholder="동일한 비밀번호를 입력해 주세요."  data-vd='{"type":"text","len":"8,20","req":true,"msg":"동일한 비밀번호를 입력해 주세요"}' />
				</td>
			</tr>
			<tr>
				<th>부서명</th>
				<td>
					<input type="text" id="deptNm" name="deptNm" placeholder="부서명을 입력해주세요." data-vd='{"type":"text","len":"1,20","req":true,"msg":"부서명을 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>담당자명</th>
				<td>
					<input type="text" id="memberName" name="memberName" placeholder="이름을 입력해주세요." data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>직위</th>
				<td>
					<input type="text" id="positionNm" name="positionNm" placeholder="직위를 입력해주세요." data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위명을 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>이메일</th>
				<td>
					<input type="text" id="email" name="email" placeholder="이메일을 입력해주세요." maxlength="40" data-vd='{"type":"email","len":"1,40","req":true,"msg":"이메일을 입력해 주세요"}'/>
					<p class="noti">
						※ 가입 승인여부가 입력하신 이메일로 전송됩니다. 정확히 기입해 주세요.
					</p>
				</td>
			</tr>
			<tr>
				<th>회사 전화번호</th>
				<td>
					<input type="text" id="extensionNo" name="extensionNo" placeholder="회사전화번호를 입력해 주세요. (- 포함)" data-vd='{"type":"extensionNo","len":"1,20","req":true,"msg":"회사전화번호 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>휴대폰 번호</th>
				<td>
					<c:choose>
						<c:when test="${termsData.optionTermsYn eq 'N'}">
							<input type="text" id="mobileNo" name="mobileNo" placeholder="선택적약관 미동의" disabled/>
						</c:when>
						<c:otherwise>
							<input type="text" id="mobileNo" name="mobileNo" placeholder="휴대폰번호를 입력해 주세요. (- 포함)" data-vd='{"type":"mobileNo","len":"1,20","req":true,"msg":"휴대폰번호를 입력해 주세요"}'/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>첨부파일 (신청서)</th>
				<td id="fileTag">
					<input type="text" id="fileName" name="fileName" readonly="readonly" placeholder="파일을 첨부해 주세요."/>
					<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
					<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
					<input type="file" id="u_file" class="" name="files" style="display:none;">
				</td>
			</tr>
			<tr>
				<th>첨부파일 (양식)</th>
				<td id="fileTag">
					<!-- <a href="javascript:void(0);" class="btn_gray btn_small" onclick="saveToDisk('/static/sample/대출성상품모집인등록업무담당자신청서.hwp','담당자신청서_샘플')">양식1(한글파일)</a> -->
					<a href="javascript:void(0);" class="btn_gray btn_small" onclick="saveToDisk('/static/sample/대출성상품모집인등록업무담당자신청서.docx','담당자신청서_샘플')">양식2(워드파일)</a>
				</td>
			</tr>
		</table>
	</div>
</form>

<div class="btn_wrap">
	<a href="javascript:void(0);" class="btn_black" id="signupBtn" >회원가입 신청</a>
</div>