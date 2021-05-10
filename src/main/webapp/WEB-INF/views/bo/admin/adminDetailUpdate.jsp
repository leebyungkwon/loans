<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
		
		var password         = $("#password").val();             // 비밀번호
		var passwordChk    = $("#passwordChk").val();        // 비밀번호 확인
		$("#adminUpdBtn").on("click", function(){
			if(confirm("정보를 수정 하시겠습니까?")){
				if( password == passwordChk ){
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
   
   <form name="saveAdminUpdateFrm" id="saveAdminUpdateFrm" action="/member/admin/saveAdminUpdate" method="post" enctype="multipart/form-data">
      <input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
      <input type="hidden" name="tempMemberCheck" value="${adminInfo.tempMemberCheck}"/>
      <div class="contents">
         <div id="table">
            <table class="view_table">
               <tr>
                  <th>아이디</th>
                  <td colspan="3">
                     <input type="text" name="memberId" value="${adminInfo.memberId}" readonly="readonly" class="w40" />
                  </td>
               </tr>
               <tr>
                  <th>패스워드</th>
                  <td colspan="3">
                     <input type="password" id="password" name="password" placeholder="8자리~20자리 (2종류 이상의 문자구성)" class="w40"  data-vd='{"type":"pw","len":"8,20","req":true,"msg":"비밀번호를 다시 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>패스워드 확인</th>
                  <td colspan="3">
                     <input type="password" id="passwordChk" name="passwordChk" placeholder="동일한 비밀번호를 입력" class="w40"  data-vd='{"type":"pw","len":"8,20","req":true,"msg":"동일한 비밀번호를 입력해 주세요"}' />
                  </td>
               </tr>
               <tr>
                  <th>부서명</th>
                  <td colspan="3">
                     <input type="text" id="deptNm" name="deptNm" placeholder="부서명 입력" value="${adminInfo.deptNm}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"부서명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>담당자명</th>
                  <td colspan="3">
                     <input type="text" id="memberName" name="memberName" placeholder="이름" value="${adminInfo.memberName}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>직위</th>
                  <td colspan="3">
                     <input type="text" id="positionNm" name="positionNm" placeholder="직위 입력" value="${adminInfo.positionNm}" class="w40" data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>이메일</th>
                  <td colspan="3">
                     <input type="text" id="email" name="email" placeholder="이메일 입력" value="${adminInfo.email}" class="w40" data-vd='{"type":"email","len":"1,20","req":true,"msg":"이메일을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>회사 전화번호</th>
                  <td colspan="3">
                     <input type="text" id="extensionNo" name="extensionNo" placeholder="회사 전화번호 입력" value="${adminInfo.extensionNo}" class="w40" data-vd='{"type":"num","len":"1,20","req":true,"msg":"회사전화번호 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>휴대폰번호</th>
                  <td colspan="3">
                     <input type="text" id="mobileNo" name="mobileNo" placeholder="휴대폰번호 입력" value="${adminInfo.mobileNo}" class="w40" data-vd='{"type":"num","len":"0,20", "msg":"휴대폰번호를 입력해 주세요"}'/>
                  </td>
               </tr>
            	<tr>
					<th>첨부 파일</th>
					<td id="fileTag">
						<input type="text" id="fileName" name="fileName" value="${file.fileFullNm}" class="w40" readonly="readonly" data-vd='{"type":"text","len":"1,60","req":true,"msg":"파일을 첨부해 주세요."}'>
						<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
						<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
						<input type="file" id="u_file" class="" name="files" multiple="multiple" style="display:none;">
					</td>
			</tr>
            </table>
         </div>
      </div>
   </form>
   
   <div class="btn_wrap">
		<c:if test="${adminInfo.tempMemberCheck ne 'Y' }">
			<a href="javascript:void(0);" id="adminCancelBtn" class="btn_gray">취소</a>
		</c:if>
      
      <a href="javascript:void(0);" id="adminUpdBtn" class="btn_black btn_right">저장</a>
   </div>
</div>