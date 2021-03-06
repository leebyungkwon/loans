<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
		
		$("#adminUpdBtn").on("click", function(){
			if(confirm("정보를 수정 하시겠습니까?")){
				var id				= $("#memberId").val();
				var pw         		= $("#password").val();             // 비밀번호
				var pwChk    		= $("#passwordChk").val();			// 비밀번호 확인
				var fileName		= $("#fileName").val();
				var checkCount 	= 0;
				
				<sec:authorize access="hasAnyRole('TEMP_MEMBER', 'MEMBER')">
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
						alert("8자리 ~ 20자리 이내로 입력해주세요.");
						return false;
					}
					if (/(\w)\1\1/.test(pw)){ 
						alert('같은 문자를 3번 이상 사용하실 수 없습니다.');
						return false;
					}
					if (pw.search(id) > -1){
						alert("비밀번호에 아이디가 포함되었습니다.");
						alert(id)
						return false;
					}
					if (pw.search(/\s/) != -1){ 
						alert("비밀번호는 공백 없이 입력해주세요.");
						return false;
					}
				</sec:authorize>
				
				if(WebUtil.isNull(fileName)){
					alert("파일을 첨부해 주세요.");
					$("#fileName").focus();
					return false;
				}
				
				$("#saveAdminUpdateFrm").attr("action","/member/admin/saveAdminUpdate");
				
				if( pw == pwChk ){
					var p = {
						name       : "saveAdminUpdateFrm"
						, success    : function (opt,result) {
							if(WebUtil.isNull(result.message)){
								alert(result.data[0].defaultMessage);
							}else{
								if("${prevMn}" == "mng"){
									location.href="/admin/mng/companyPage"
								}else{
									location.href="/member/admin/adminPage"
								}
							}
						}
					}
					AjaxUtil.files(p);
				} else {
					alert("비밀번호를 틀리셨습니다. 비밀번호를 다시 입력해 주세요!");
					$("#password").val("");
					$("#passwordChk").val("");
					return false;
				}
			}
		});
		
		
		/* ================================= Test =========================== */
		
		$('input:radio[name="optionTermsYn"]').on("click", function(){
			if($('input:radio[name="optionTermsYn"]:checked').val() == "Y"){
				$("#mobileNo").attr("disabled", false); 
				$("#mobileNo").attr("placeholder", "휴대폰번호를 입력해 주세요. (- 포함)"); 
				var aa = '{"type":"mobileNo","len":"1,20","req":true,"msg":"휴대폰번호를 입력해 주세요"}';
				$("#mobileNo").attr("data-vd", aa);
				alert("선택적 약관동의를 동의 하셨습니다.")
			}else{
				$("#mobileNo").attr("disabled", true); 
				$("#mobileNo").attr("placeholder", "선택적약관 미동의"); 
				$("#mobileNo").removeAttr("data-vd");
				alert("선택적 약관동의가 해제 되셨습니다.")
			}
		});
		
		
		/* ================================= Test =========================== */
		
		// 취소 버튼
		$("#adminCancelBtn").on("click", function(){
			if("${prevMn}" == "mng"){
				location.href="/admin/mng/companyPage"
			}else{
				location.href="/member/admin/adminPage"
			}
		});
		
		// 첨부파일 삭제
		$("#fileDelete").on("click", function(){
			var fileSeq = $("#fileSeq").val();
			var fileName = $("#fileName").val();
			
			if(WebUtil.isNull(fileName)){
				alert("파일을 첨부해 주세요.");
				$("#fileName").focus();
				return false;
			}
			
			if(confirm("첨부파일을 삭제하시겠습니까?")){
				var p = {
					url : "/common/fileRealDelete"
					, param : {
						'fileSeq'	:	fileSeq	
					}
					, success : function(opt, result){
						alert("첨부파일이 삭제되었습니다.");
						$("#fileName").val("");
						$("#fileDelete").remove();
					}
				}
				AjaxUtil.post(p);
			}
		});
		
		// 첨부파일 찾기
		$("#fileSearch").on("click", function(){
			$("#u_file").click();
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
		
		
		// 재승인요청
		$("#reApprBtn").on("click", function(){
			var memSeq 			= "${adminInfo.memberSeq}";
			var id				= $("#memberId").val();
			var pw         		= $("#password").val();             // 비밀번호
			var pwChk    		= $("#passwordChk").val();        	// 비밀번호 확인
			var fileName   		= $("#fileName").val();          	// 첨부파일 체크
			var checkCount 		= 0;

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
				alert("8자리 ~ 20자리 이내로 입력해주세요.");
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
				alert("비밀번호는 공백 없이 입력해주세요.");
				return false;
			}
			if(WebUtil.isNull(memSeq)){
				alert("오류가 발생하였습니다.");
				return false;
			}
			if(WebUtil.isNull(fileName)){
				alert("파일을 첨부해 주세요.");
				$("#fileName").focus();
				return false;
			}
			
			$("#saveAdminUpdateFrm").attr("action","/member/admin/reAppr");
			
			if( pw == pwChk ){
				if(confirm("재승인 요청을 하시겠습니까?")){
					var p = {
							name       : "saveAdminUpdateFrm"
							, success    : function (opt,result) {
								if(result.data > 0){
									alert("재승인 요청이 완료되었습니다. \n승인 후에 로그인 가능합니다.")
									location.href="/logout";
								}else{
									alert("잘못된 확장자의 첨부파일을 등록 하였습니다.[0003]");
									return false;
								}
								
								
							}
						}
						AjaxUtil.files(p);
					}
				}else {
					alert("비밀번호를 틀리셨습니다. 비밀번호를 다시 입력해 주세요!");
					$("#password").val("");
					$("#passwordChk").val("");
					return false;
				}
		});
		
	}
	
	function checkCapsLock(event){
		if(event.getModifierState("CapsLock")){
			$("#message").text("※ Caps Lock이 켜져 있습니다.");
		}else{
			$("#message").text("");
		}
	}
	
</script>

<form id="adminDetailFrm" method="post" action="/member/admin/adminDetailPage">
	<input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<c:choose>
				<c:when test="${prevMn eq 'mng' }">
					<h2>회원사 담당자 관리</h2>
				</c:when>
				<c:otherwise>
					<h2>관리자 조회 및 변경</h2>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
		
	<form name="saveAdminUpdateFrm" id="saveAdminUpdateFrm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
		<input type="hidden" name="tempMemberCheck" value="${adminInfo.tempMemberCheck}"/>
		
		<div class="contents">
			<div id="table">
				<table class="view_table">
					<tr>
						<th>아이디</th>
						<td>
							<input type="text" id="memberId" name="memberId" value="${adminInfo.memberId}" readonly="readonly" class="w40" />
						</td>
					</tr>
					
					<sec:authorize access="hasAnyRole('TEMP_MEMBER', 'MEMBER')">
					<tr>
						<th>비밀번호</th>
						<td>
							<input type="password" id="password" name="password" onkeyup="checkCapsLock(event);" placeholder="8자리~20자리 (2종류 이상의 문자구성)" class="w40" maxlength="20"  />
							<p class="noti" style="margin-top: 5px;">
								※ 알파벳 대문자, 알파벳 소문자, 특수문자, 숫자 중 2종류 이상을 선택하여 문자를 구성해야 합니다.<br>
								※ 아이디, 동일한 문자의 반복 및 연속된 3개의 숫자/문자는 사용이 불가능 합니다.
							</p>
							<div id="message" class="orange"></div>
						</td>
					</tr>
					<tr>
						<th>비밀번호 확인</th>
						<td>
							<input type="password" id="passwordChk" name="passwordChk" onkeyup="checkCapsLock(event);" placeholder="동일한 비밀번호를 입력해 주세요." class="w40" maxlength="20"  />
						</td>
					</tr>
					</sec:authorize>
					<tr>
						<th>부서명</th>
						<td>
							<input type="text" id="deptNm" name="deptNm" placeholder="부서명을 입력해주세요." value="${adminInfo.deptNm}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"부서명을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
                   		<th>담당자명</th>
						<td>
							<input type="text" id="memberName" name="memberName" placeholder="이름을 입력해주세요." value="${adminInfo.memberName}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>직위</th>
						<td>
							<input type="text" id="positionNm" name="positionNm" placeholder="직위를 입력해주세요." value="${adminInfo.positionNm}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위명을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td>
							<input type="text" id="email" name="email" placeholder="이메일을 입력해주세요." maxlength="40" value="${adminInfo.email}" class="w40" data-vd='{"type":"email","len":"1,40","req":true,"msg":"이메일을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>회사 전화번호</th>
						<td>
							<input type="text" id="extensionNo" name="extensionNo" maxlength="13" placeholder="회사전화번호를 입력해 주세요. ( -포함)" value="${adminInfo.extensionNo}" class="w40" data-vd='{"type":"extensionNo","len":"1,20","req":true,"msg":"회사전화번호 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>휴대폰 번호</th>
						<td>
							<input type="text" id="mobileNo" name="mobileNo" <c:if test="${adminInfo.optionTermsYn eq 'N' }">disabled</c:if> placeholder="선택적약관 미동의" maxlength="13" value="${adminInfo.mobileNo}" class="w40" />
						</td>
					</tr>
					<tr>
						<th>선택적 약관동의</th>
						<td>
							<div class="input_radio_wrap">
								<input type="radio" name="optionTermsYn" id="radio01" value="Y" <c:if test="${adminInfo.optionTermsYn eq 'Y' }">checked="checked"</c:if>>
								<label for="radio01">Y</label>
							</div>
							<div class="input_radio_wrap mgl20">
								<input type="radio" name="optionTermsYn" id="radio02" value="N" <c:if test="${adminInfo.optionTermsYn eq 'N' }">checked="checked"</c:if>>
								<label for="radio02">N</label>
							</div>
						</td>
					</tr>
					
					<tr>
						<th>첨부파일</th>
						<td id="fileTag">
							<input type="text" id="fileName" name="fileName" value="${file.fileFullNm}" class="w40" readonly="readonly">
							<input type="hidden" id="fileSeq" value="${file.fileSeq}" />
							<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
							<c:if test="${!empty file}">
								<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
							</c:if>
							<input type="file" id="u_file" class="" name="files" style="display:none;">
						</td>
					</tr>
					<tr>
						<c:choose>
							<c:when test="${adminInfo.apprStat eq 2}">
								<th>가승인 사유</th>
								<td colspan="3">
									${adminInfo.msg}
								</td>
							</c:when>
						</c:choose>
					</tr>
				</table>
			</div>
		</div>
	</form>
   
   <div class="btn_wrap">
		<c:if test="${adminInfo.tempMemberCheck ne 'Y' }">
			<a href="javascript:void(0);" id="adminCancelBtn" class="btn_gray">취소</a>
			<a href="javascript:void(0);" id="adminUpdBtn" class="btn_blue btn_right">저장</a>
		</c:if>
		<c:if test="${adminInfo.tempMemberCheck eq 'Y' }">
			<a href="javascript:void(0);" id="reApprBtn" class="btn_gray">재승인요청</a>
		</c:if>
	</div>
</div>

