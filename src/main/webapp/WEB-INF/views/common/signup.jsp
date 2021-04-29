<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>

function pageLoad(){
	
	// 회원 가입 버튼 눌렀을때
	document.getElementById('btn_signup').onclick = function () {
		
		// 아이디 중복체크 
		var memberId = $("#memberId").val();
		var	param = {
			'memberId' : memberId
		} // end of param
	    var p = {
			param: param
			,url: "/idcheck" // 아이디 즁복 체크하러 다녀옴 (버튼 안눌렀을때 확인).
			,success: function(opt, result) {    
				if(result > 0) {
					alert("아이디 중복체크를 눌러주세요!!!");    
				} else { // 아이디 중복 없을시 이벤트 태움.
					var p2 = { // 회원가입 저장 이벤트
						name : 'signup'
					   ,success: function(opt, result) {
							alert("회원가입 성공");
							location.href="/login";
						} // end of p2 성공시 이벤트 - 회원가입 저장 이벤트
					} // end of p2 - 회원가입 저장 이벤트		
					AjaxUtil.files(p2); // 회원가입 저장 이벤트 이다.
				}            
			} // end of success - 아이디 중복체크용
			,error: function(error) {
				alert("아이디를 입력해주세요.");
			} // end of error - 아이디 중복체크용        
   		} // end of p - 아이디 중복체크용
		AjaxUtil.post(p);
	}; // end of 회원가입 버튼 눌렀을때
	
	// 첨부파일 삭제
	$("#fileDelete").on("click", function(){
		$("#fileName").val("memberId");
		
		// IE일 경우
		//$("#u_file").replaceWith( $("#u_file").clone(true) );
		$("#u_file").val("");
	});
	
	// 첨부파일 찾기
	$("#fileSearch").on("click", function(){
		$("#u_file").click();
	});
	
	// 양식 다운로드
	$("#sampleDown").on("click", function(){
		alert("양식다운로드 실행");
	});
	
	// 아이디 중복체크
	$("#idcheck").on("click", function(){
		var memberId = $("#memberId").val();
		var	param = {
				'memberId' : memberId
			} // end of param
	    var p = {
            	 param: param
				,url: "/idcheck"
              	,success: function(opt, result) {    
                   if(result > 0) {
                       alert("해당 아이디가 존재합니다.");    
                       $("#memberId").val("");
                   } else {
                       alert("사용가능 아이디 입니다.");
                   }            
               }, // end of success
               error: function(error) {
                   alert("아이디를 입력해주세요.");
               } // end of error        
   		} // end of p
		AjaxUtil.post(p);
	}); // end of 아이디 중복체크
	
	// 첨부파일 찾기시 file tag 실행
	$("#u_file").on("change", function(){
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

</script>

<div class="page_title">
	<h2>회원가입</h2>
</div>
<form name="signup" id="signup" action="/signup" method="POST" enctype="multipart/form-data" >
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
					<input type="text" id="memberId" name="memberId" placeholder="아이디" />
					<a href="javascript:void(0);" id="idcheck" class="btn_gray btn_small">중복체크</a>
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" id="password" name="password" placeholder="8자리~20자리 (2종류 이상의 문자구성)"/>
 					<p class="noti">
						알파벳 대문자, 알파벳 소문자, 특수문자, 숫자 중 2종류 이상을 선택하여 문자를 구성해야 합니다.<br />
						휴대폰 뒤 4자리, 생년월일, 아이디, 동일한 문자의 반복 및 연속된 3개의 숫자/문자는 사용불가능합니다.
					</p>
				</td>
			</tr>
			<tr>
				<th>비밀번호 확인</th>
				<td>
					<input type="password" id="password_chk" name="password_chk" placeholder="동일한 비밀번호를 입력" />
				</td>
			</tr>
			<tr>
				<th>부서명</th>
				<td>
					<input type="text" id="deptNm" name="deptNm" placeholder="부서명 입력">
				</td>
			</tr>
			<tr>
				<th>담당자명</th>
				<td>
					<input type="text" id="memberName" name="memberName" placeholder="이름">
				</td>
			</tr>
			<tr>
				<th>직위</th>
				<td>
					<input type="text" id="positionNm" name="positionNm" placeholder="직위 입력">
				</td>
			</tr>
			<tr>
				<th>이메일</th>
				<td>
					<input type="text" id="email" name="email" placeholder="이메일 입력">
					<p class="noti">
						가입 승인여부는 입력하신 이메일로 전송됩니다. 정확히 기입해 주세요.
					</p>
				</td>
			</tr>
			<tr>
				<th>전화번호</th>
				<td>
					<input type="text" id="mobileNo" name="mobileNo" placeholder="전화번호 입력">
				</td>
			</tr>
			<tr>
				<th>첨부파일 (신청서)</th>
				<td id="fileTag">
					<input type="text" id="fileName" name="fileName"><!-- readonly disabled -->
					<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
					<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
					<a href="javascript:void(0);" class="btn_gray btn_small" id="sampleDown">양식다운로드</a>
					<input type="file" id="u_file" class="" name="files" multiple="multiple" style="display:none;">
				</td>
			</tr>
		</table>
	</div>
</form>

<div class="btn_wrap">
	<a href="javascript:void(0);" class="btn_black" id="btn_signup" >회원가입 신청</a>
</div>

