<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){

		$("#AdminUpdateBtn").on("click", function(){
			var p = {
				name       : "saveAdminUpdateFrm"
				, success    : function (opt,result) {
					alert("호잇");
					location.href = "/member/admin/adminPage";
				}
			}
			AjaxUtil.files(p);
		});

		$("#AdminCancelBtn").on("click", function(){
			$("#adminDetailFrm").submit();
		});
	}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>관리자 수정</h2>
		</div>
	</div>
		
	<form id="adminDetailFrm" method="post" action="/member/admin/adminDetail">
		<input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
	</form>
   
   <form name="saveAdminUpdateFrm" id="saveAdminUpdateFrm" action="/member/admin/saveAdminUpdate" method="post" enctype="multipart/form-data">
      <input type="hidden" name="memberSeq" value="${adminInfo.memberSeq}"/>
      <div class="contents">
         <div id="table">
            <table class="view_table">
               <tr>
                  <th>아이디</th>
                  <td colspan="3">
                     <input type="text" name="memberId" value="${adminInfo.memberId}" readonly="readonly">
                  </td>
               </tr>
               <tr>
                  <th>패스워드</th>
                  <td colspan="3">
                     <input type="password" id="password" name="password" placeholder="8자리~20자리 (2종류 이상의 문자구성)" data-vd='{"type":"password","len":"8,20","req":true,"msg":"비밀번호를 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>패스워드 확인</th>
                  <td colspan="3">
                     <input type="password" id="passwordChk" name="passwordChk" placeholder="동일한 비밀번호를 입력" data-vd='{"type":"password","len":"1,20","req":true,"msg":"동일한 비밀번호를 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>부서명</th>
                  <td colspan="3">
                     <input type="text" id="deptNm" name="deptNm" placeholder="부서명 입력" data-vd='{"type":"text","len":"1,20","req":true,"msg":"부서명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>담당자명</th>
                  <td colspan="3">
                     <input type="text" id="memberName" name="memberName" placeholder="이름" data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>직위</th>
                  <td colspan="3">
                     <input type="text" id="positionNm" name="positionNm" placeholder="직위 입력" data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위명을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>이메일</th>
                  <td colspan="3">
                     <input type="text" id="email" name="email" placeholder="이메일 입력" data-vd='{"type":"email","len":"1,20","req":true,"msg":"이메일을 입력해 주세요"}'/>
                  </td>
               </tr>
               <tr>
                  <th>회사 전화번호</th>
                  <td colspan="3">
                     <input type="text" id="extensionNo" name="extensionNo" placeholder="회사 전화번호 입력" data-vd='{"type":"extensionNo","len":"1,20","req":true,"msg":"휴대폰 번호를 입력해 주세요"}' />
                  </td>
               </tr>
               <tr>
                  <th>휴대폰번호</th>
                  <td colspan="3">
                     <input type="text" id="mobileNo" name="mobileNo" placeholder="휴대폰번호 입력"  />
                  </td>
               </tr>
            </table>
         </div>
      </div>
   </form>
   
   <div class="btn_wrap">
      <a href="javascript:void(0);" id="AdminCancelBtn" class="btn_gray">취소</a>
      <a href="javascript:void(0);" id="AdminUpdateBtn" class="btn_black btn_right">저장</a>
   </div>
</div>