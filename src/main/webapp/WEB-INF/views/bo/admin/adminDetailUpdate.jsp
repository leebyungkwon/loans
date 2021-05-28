<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
		
		$("#adminUpdBtn").on("click", function(){
			if(confirm("정보를 수정 하시겠습니까?")){
//////////////////////////////////////////////////////////////////////////////////////////////
				var id					= $("#memberId").val();
				var pw         		= $("#password").val();             // 비밀번호
				var pwChk    		= $("#passwordChk").val();        // 비밀번호 확인
				var checkCount 	= 0;

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
				}else {
					console.log("통과");
				}
				
				$("#saveAdminUpdateFrm").attr("action","/member/admin/saveAdminUpdate");
//////////////////////////////////////////////////////////////////////////////////////////////
				if( pw == pwChk ){
					var p = {
						name       : "saveAdminUpdateFrm"
						, success    : function (opt,result) {
							location.href = "/member/admin/adminPage";
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
		
		// 취소 버튼
		$("#adminCancelBtn").on("click", function(){
			$("#adminDetailFrm").submit();
		});
		
		// 첨부파일 삭제
		$("#fileDelete").on("click", function(){
			$("#fileName").val("");
			
			// IE일 경우
			//$("#u_file").replaceWith( $("#u_file").clone(true) );
			$("#u_file").val("");
		});
		
		// 첨부파일 찾기
		$("#fileSearch").on("click", function(){
			$("#u_file").click();
		});

		// 첨부파일 찾기시 file tag 실행
		$("#u_file").on("change", function(){
			var fileValue = $("#u_file").val().split("\\");
			var fileName = fileValue[fileValue.length-1];
			$("#fileName").val(fileName);
		});
		
		
		// 재승인요청
		$("#reApprBtn").on("click", function(){
			var memSeq = "${adminInfo.memberSeq}";
			
//////////////////////////////////////////////////////////////////////////////////////////////
			var id					= $("#memberId").val();
			var pw         		= $("#password").val();             // 비밀번호
			var pwChk    		= $("#passwordChk").val();        // 비밀번호 확인
			var fileName    		= $("#fileName").val();          // 첨부파일 체크
			var checkCount 	= 0;

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
			}else {
				console.log("통과");
			}
//////////////////////////////////////////////////////////////////////////////////////////////
			
			if(WebUtil.isNull(memSeq)){
				alert("오류가 발생하였습니다.");
				return false;
			}
			
			$("#saveAdminUpdateFrm").attr("action","/member/admin/reAppr");
			
			if( pw == pwChk ){
				if(confirm("재승인 요청을 하시겠습니까?")){
					var p = {
							name       : "saveAdminUpdateFrm"
							, success    : function (opt,result) {
								alert("재승인 요청이 완료되었습니다. \n승인 후에 로그인 가능합니다.")
								location.href="/logout";
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

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>관리자 수정</h2>
		</div>
	</div>
		
	<form id="adminDetailFrm" method="post" action="/member/admin/adminDetailPage">
		<input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
	</form>
   
   <form name="saveAdminUpdateFrm" id="saveAdminUpdateFrm" method="post" enctype="multipart/form-data">
      <input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
      <input type="hidden" name="tempMemberCheck" value="${adminInfo.tempMemberCheck}"/>
      <div class="contents">
         <div id="table">
            <table class="view_table">
               <tr>
                  <th>아이디</th>
                  <td colspan="3">
                     <input type="text" id="memberId" name="memberId" value="${adminInfo.memberId}" readonly="readonly" class="w40" />
                  </td>
               </tr>
               <tr>
                  <th>패스워드</th>
                  <td colspan="3">
                     <input type="password" id="password" name="password" placeholder="8자리~20자리 (2종류 이상의 문자구성)" class="w40" maxlength="20" data-vd='{"type":"text","len":"8,20","req":true,"msg":"비밀번호를 다시 입력해 주세요"}' />
                  </td>
               </tr>
               <tr>
                  <th>패스워드 확인</th>
                  <td colspan="3">
                     <input type="password" id="passwordChk" name="passwordChk" placeholder="동일한 비밀번호를 입력해 주세요." class="w40" maxlength="20" data-vd='{"type":"text","len":"8,20","req":true,"msg":"비밀번호를 다시 입력해 주세요"}' />
                  </td>
               </tr>
               <tr>
                  <th>부서명</th>
                  <td colspan="3">
                     <input type="text" id="deptNm" name="deptNm" placeholder="부서명을 입력해주세요." value="${adminInfo.deptNm}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"부서명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>담당자명</th>
                  <td colspan="3">
                     <input type="text" id="memberName" name="memberName" placeholder="이름을 입력해주세요." value="${adminInfo.memberName}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>직위</th>
                  <td colspan="3">
                     <input type="text" id="positionNm" name="positionNm" placeholder="직위를 입력해주세요." value="${adminInfo.positionNm}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>이메일</th>
                  <td colspan="3">
                     <input type="text" id="email" name="email" placeholder="이메일을 입력해주세요." maxlength="40" value="${adminInfo.email}" class="w40" data-vd='{"type":"email","len":"1,40","req":true,"msg":"이메일을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>회사 전화번호</th>
                  <td colspan="3">
                     <input type="text" id="extensionNo" name="extensionNo" placeholder="회사전화번호를 입력해 주세요. ( -포함)" value="${adminInfo.extensionNo}" class="w40" data-vd='{"type":"extensionNo","len":"1,20","req":true,"msg":"회사전화번호 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>휴대폰번호</th>
                  <td colspan="3">
                     <input type="text" id="mobileNo" name="mobileNo" placeholder="휴대폰번호를 입력해 주세요. ( -포함)" value="${adminInfo.mobileNo}" class="w40" data-vd='{"type":"mobileNo","len":"1,20","req":true,"msg":"휴대폰번호를 입력해 주세요"}'/>
                  </td>
               </tr>
            	<tr>
					<th>첨부 파일</th>
					<td id="fileTag">
						<input type="text" id="fileName" name="fileName" value="${file.fileFullNm}" class="w40" readonly="readonly" maxlength="60"   placeholder="이미지 파일을 첨부해 주세요." data-vd='{"type":"fileupload","len":"1,60","req":true,"msg":"이미지 파일을 첨부해 주세요"}' >
						<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
						<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
						<input type="file" id="u_file" class="" name="files" multiple="multiple" style="display:none;">
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
		</c:if>
		<c:if test="${adminInfo.tempMemberCheck eq 'Y' }">
			<a href="javascript:void(0);" id="reApprBtn" class="btn_gray">재승인요청</a>
		</c:if>
      
      <a href="javascript:void(0);" id="adminUpdBtn" class="btn_black btn_right">저장</a>
   </div>
</div>