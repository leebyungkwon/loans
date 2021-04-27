<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
function pageLoad(){
	document.getElementById('btn_signup').onclick = function () {
		AjaxUtil.submit('signup');
	};
	
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
	
	// 양식 다운로드
	$("#sampleDown").on("click", function(){
		alert("양식다운로드 실행");
	});
	
	// 아이디 중복체크
	$("#idcheck").on("click", function(){
		alert("아이디 중복체크 확인중");
		url : '/login'
	});
	
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
		,target : '#companyCd'
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
					<select id="companyCd" name="companyCd"></select>
				</td>
			</tr>
			<tr>
				<th>아이디</th>
				<td>
					<input type="text" id="user_id" name="userId" placeholder="아이디" />
					<a href="javascript:void(0);" id="idcheck" class="btn_gray btn_small">중복체크</a>
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" id="user_password" name="password" placeholder="8자리~20자리 (2종류 이상의 문자구성)"/>
 					<p class="noti">
						알파벳 대문자, 알파벳 소문자, 특수문자, 숫자 중 2종류 이상을 선택하여 문자를 구성해야 합니다.<br />
						휴대폰 뒤 4자리, 생년월일, 아이디, 동일한 문자의 반복 및 연속된 3개의 숫자/문자는 사용불가능합니다.
					</p>
				</td>
			</tr>
			<tr>
				<th>비밀번호 확인</th>
				<td>
					<input type="password" id="user_password_chk" name="password_chk" placeholder="동일한 비밀번호를 입력" />
				</td>
			</tr>
			<tr>
				<th>부서명</th>
				<td>
					<input type="text" id="dept_nm" name="deptNm" placeholder="부서명 입력">
				</td>
			</tr>
			<tr>
				<th>담당자명</th>
				<td>
					<input type="text" id="mem_nm" name="memNm" placeholder="이름">
				</td>
			</tr>
			<tr>
				<th>직위</th>
				<td>
					<input type="text" id="position_nm" name="positionNm" placeholder="직위 입력">
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
					<input type="text" id="mobile_no" name="mobileNo" placeholder="전화번호 입력">
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
	<a href="#" class="btn_black" id="btn_signup">회원가입 신청</a>
</div>

