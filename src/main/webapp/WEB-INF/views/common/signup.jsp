<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>

function pageLoad(){
	
	// 회원가입 클릭
	$("#signupBtn").on("click", function(){
		if(confirm("회원가입을 진행 하시겠습니까?")){
			var password       = $("#password").val();               // 비밀번호
			var passwordChk    = $("#passwordChk").val();            // 비밀번호 확인
		
			if($("#checkId").val() == "N"){
				alert("중복체크를 실행해 주세요.");
				return false;
			}
		
			if( password == passwordChk ){
				var signupParam = {
					name : 'signup'
					,success: function(opt, result) {
		 				location.href="/login";
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
		
		// IE일 경우
		//$("#u_file").replaceWith( $("#u_file").clone(true) );
		$("#u_file").val("");
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
            	if(result > 0){
                    $("#memberId").val("");
                    $("#checkId").val("N");
                    alert("해당 아이디가 존재합니다.");    
            	}else{
            		$("#checkId").val("Y");
            		alert("사용가능 아이디 입니다.");
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
					<input type="text" id="memberId" name="memberId" placeholder="아이디" data-vd='{"type":"text","len":"5,11","req":true,"msg":"아이디를 입력해 주세요"}'/>
					<a href="javascript:void(0);" id="idcheck" class="btn_gray btn_small">중복체크</a>
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" id="password" name="password" placeholder="최소8자, 문자/숫자/특수문자를 입력하세요."  data-vd='{"type":"pw","len":"8,20","req":true,"msg":"비밀번호를 다시 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>비밀번호 확인</th>
				<td>
					<input type="password" id="passwordChk" name="passwordChk" placeholder="동일한 비밀번호를 입력"  data-vd='{"type":"pw","len":"8,20","req":true,"msg":"동일한 비밀번호를 입력해 주세요"}' />
				</td>
			</tr>
			<tr>
				<th>부서명</th>
				<td>
					<input type="text" id="deptNm" name="deptNm" placeholder="부서명 입력" data-vd='{"type":"text","len":"1,20","req":true,"msg":"부서명을 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>담당자명</th>
				<td>
					<input type="text" id="memberName" name="memberName" placeholder="이름" data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>직위</th>
				<td>
					<input type="text" id="positionNm" name="positionNm" placeholder="직위 입력" data-vd='{"type":"text","len":"1,20","req":true,"msg":"직위명을 입력해 주세요"}'/>
				</td>
			</tr>
			<tr>
				<th>이메일</th>
				<td>
					<input type="text" id="email" name="email" placeholder="이메일 입력" data-vd='{"type":"email","len":"1,20","req":true,"msg":"이메일을 입력해 주세요"}'/>
					<p class="noti">
						가입 승인여부는 입력하신 이메일로 전송됩니다. 정확히 기입해 주세요.
					</p>
				</td>
			</tr>
			<tr>
				<th>회사 전화번호</th>
				<td>
					<input type="text" id="extensionNo" name="extensionNo" placeholder="-없이 입력해 주세요." data-vd='{"type":"num","len":"1,20","req":true,"msg":"회사전화번호 입력해 주세요"}'/>
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
							<input type="text" id="mobileNo" name="mobileNo" placeholder="-없이 입력해 주세요." data-vd='{"type":"num","len":"0,20", "msg":"휴대폰번호를 입력해 주세요"}'/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>첨부파일 (신청서)</th>
				<td id="fileTag">
					<input type="text" id="fileName" name="fileName" readonly="readonly"  placeholder="이미지 파일을 첨부해 주세요." data-vd='{"type":"fileupload","len":"1,60","req":true,"msg":"이미지 파일을 첨부해 주세요"}' />
					<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
					<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
					<a href="javascript:void(0);" class="btn_gray btn_small" onclick="saveToDisk('/static/sample/담당자신청서_샘플.png','담당자신청서_샘플')">샘플다운로드</a>
					<input type="file" id="u_file" class="" name="files" multiple="multiple" style="display:none;">
				</td>
			</tr>
		</table>
	</div>
</form>

<div class="btn_wrap">
	<a href="javascript:void(0);" class="btn_black" id="signupBtn" >회원가입 신청</a>
</div>