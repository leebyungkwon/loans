<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script>
function pageLoad(){

	$("#termsCheck").on("click", function(){
		if($("input:radio:checked").length == 2){
			if($("#radio02").is(":checked")){
				alert("필수적 개인정보 수집‧이용 동의서에 동의하지 않는 경우\n협회의 대출성상품 모집인 등록 시스템을 이용할 수 없습니다.");
				return false;
			}
			var optionTermsYn = $('input:radio[name="optionTermsYn"]:checked').val();
			location.href="/signup?optionTermsYn="+optionTermsYn;
		}else{
			alert("약관동의를 선택해 주세요.");	
		}
	});
}
</script>

<div class="page_title">
	<h2>약관동의</h2>
</div>
<div class="clause_wrap mgb20">
	<p class="tit">
		대출성 상품 모집인 등록 담당자<br />
		필수적 개인정보 수집‧이용 동의서
	</p>
	<p>
		여신금융협회(이하 “협회”)의 대출성 상품 모집인 등록 업무와 관련하여 협회가 귀하의 개인정보를 수집‧이용하고자 하는 경우에는<br />
		「개인정보 보호법」제15조, 제22조등에 따라 귀하의 동의를 얻어야 합니다.<br />
	</p>
	<p>
		이에 귀하는 협회가 아래의 내용과 같이 개인정보를 수집‧이용하는 것에 동의합니다.
	</p>
	<p>
		<span>1. 수집‧이용의 목적</span><br />
		- 협회의 대출성 상품 모집인 등록 시스템 이용을 위한 담당자 등록 및 관리<br />
		- 협회의 대출성 상품 모집인 등록 진행경과 등 안내
	</p>
	<p>
		<span>2. 수집‧이용할 항목</span><br />
		- 소속 회사명, 소속 부서명, 성명, 직위, 시스템 아이디, 이메일, 회사 전화번호
	</p>
	<p>
		<span>3. 보유 기간</span><br />
		- 위 개인정보는 수집‧이용에 관한 동의일부터 위 수집‧이용목적을 달성할 때까지 보유‧이용됩니다.
	</p>
	<p>
		<span>4. 동의를 거부할 권리 및 동의를 거부할 경우의 불이익</span><br />
		- 귀하는 협회의 개인정보 수집‧이용에 동의하지 않을 수 있습니다. <br />
		다만, 동의하지 않는 경우 협회의 대출성 상품 모집인 등록 시스템을 이용할 수 없습니다.
	</p>
</div>
<div class="btn_wrap aright w900p">
	<div class="input_radio_wrap">
		<input type="radio" name="radioTermsYn" id="radio01" value="Y">
		<label for="radio01">동의</label>
	</div>
	<div class="input_radio_wrap mgl20">
		<input type="radio" name="radioTermsYn" id="radio02" value="N">
		<label for="radio02">동의하지 않음</label>
	</div>
</div>

<div class="line900"></div>

<div class="clause_wrap mgb20">
	<p class="tit">
		선택적 개인정보 수집‧이용 동의서
	</p>
	<p>
		<span>1. 수집‧이용의 목적</span><br />
		- 협회의 대출성 상품 모집인 등록 시스템 이용을 위한 담당자 등록 및 관리<br />
		- 협회의 대출성 상품 모집인 등록 진행경과 등 안내
	</p>
	<p>
		<span>2. 수집‧이용할 항목</span><br />
		- 휴대폰 번호
	</p>
	<p>
		<span>3. 보유 기간</span><br />
		- 위 개인정보는 수집‧이용에 관한 동의일부터 위 수집‧이용목적을 달성할 때까지 보유‧이용됩니다.
	</p>
	<p>
		<span>4. 동의를 거부할 권리 및 동의를 거부할 경우의 불이익</span><br />
		- 귀하는 협회의 개인정보 수집‧이용에 동의하지 않을 수 있습니다. <br />
		다만, 동의하지 않는 경우 모집인 등록 진행경과 등의 안내를 받지 못할 수 있습니다.
	</p>
</div>
<div class="btn_wrap aright w900p">
	<div class="input_radio_wrap">
		<input type="radio" name="optionTermsYn" id="radio03" value="Y">
		<label for="radio03">동의</label>
	</div>
	<div class="input_radio_wrap mgl20">
		<input type="radio" name="optionTermsYn" id="radio04" value="N">
		<label for="radio04">동의하지 않음</label>
	</div>
</div>

<div class="btn_wrap">
	<a href="javascript:void(0);" class="btn_black" id="termsCheck">다음</a>
</div>