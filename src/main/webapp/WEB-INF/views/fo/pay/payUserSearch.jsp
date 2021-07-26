<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
function pageLoad() {
	//개인,법인 탭 클릭 이벤트
	$(".tap_wrap li").click(function(){
		var tap_index = $(this).index();
		$(".inquiry_wrap .tap_wrap li").removeClass("on").eq(tap_index).addClass("on");
		$(".tap_cont").removeClass("on").eq(tap_index).addClass("on");
	});
	
	//성별 탭 클릭 이벤트
	$(".gender_tap li").click(function(){
		$(".gender_tap li").removeClass("on");
		$(this).addClass("on");
		$("#gender").val($(this).attr("data-gender"));
	});
	
	//금융회사
 	var comCode = {
		 useCode 	: false
		,target 	: '.comCode'
		,url 		: '/common/selectCompanyCodeList'
		,key 		: 'codeDtlCd'
		,value 		: 'codeDtlNm'
		,updData 	: ''
		,defaultMsg : '계약금융회사를 선택해 주세요.'
	};
	DataUtil.selectBox(comCode);
}

//모집인 조회
function goPayUserSearch() {
	var tabIndex 	= $(".tap_wrap > ul > li.on").index();
	var formNm 		= $("form").eq(tabIndex).attr("name");
	var params 		= $("#"+formNm).serialize();
	var url 		= $("#"+formNm).attr("action");
	
	var p = {
		 url		: url
		,param 		: params
		,success	: function(opt,result){
			if(result.code != "fail"){
				goPayUserSearchResultPage(formNm,result.data);
			}
		}
	}
	AjaxUtil.post(p);
}

//조회 결과 있을 때
function goPayUserSearchResultPage(formNm,masterSeq) {
	$("#"+formNm).append('<input type="hidden" name="masterSeq" value="'+masterSeq+'">');
	$("#"+formNm).attr("action","/front/pay/payUserSearchResult");
	$("#"+formNm).submit();
}
</script>

<div class="inquiry_wrap">
	<div class="title">모집인 결제</div>
	<div class="tap_wrap">
		<ul>
			<li class="on">
				<a href="javascript:void(0);">개인</a>
			</li>
			<li>
				<a href="javascript:void(0);">법인</a>
			</li>
		</ul>
	</div>
	<div class="tap_cont on">
		<form name="indvUserSearchFrm" id="indvUserSearchFrm" method="post" action="/front/pay/payIndvUserSearch">
			<input type="hidden" name="gender" id="gender" value="M"/>
			
			<table class="payment_table">
				<colgroup>
					<col width="124"/>
					<col width="*"/>
				</colgroup>
				<tr>
					<th>금융회사 선택</th>
					<td>
						<select name="comCode" class="comCode" data-vd='{"type":"text","req":true,"msg":"금융회사를 선택해 주세요."}'></select>
					</td>
				</tr>
				<tr>
					<th>성명</th>
					<td>
						<input type="text" name="plMName" placeholder="이름을 입력해 주세요." maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'>
					</td>
				</tr>
				<tr>
					<th>휴대폰번호</th>
					<td>
						<input type="text" name="plCellphone" placeholder="예) 01077778888 (-제외)" maxlength="11" data-vd='{"type":"mobileNo","len":"1,11","req":true,"msg":"휴대폰번호를 입력해 주세요."}'>
					</td>
				</tr>
				<tr>
					<th>생년월일</th>
					<td>
						<input type="text" name="plMZIdFront" placeholder="예) 790625" maxlength="6" data-vd='{"type":"num","len":"6,6","req":true,"msg":"생년월일을 입력해 주세요."}'>
					</td>
				</tr>
				<tr>
					<th>성별</th>
					<td>
						<div class="gender_tap">
							<ul>
								<li class="on" data-gender="M">남</li>
								<li data-gender="F">여</li>
	                  		</ul>
						</div>
					</td>
				</tr>
			</table>
		</form>
		<div class="bottom_box">
			<ul>
				<li>
					<span class="dot"></span>
					<p>계약된 금융회사를 통해 신청 후에 승인완료 된 모집인에 대한 결제 페이지 입니다.</p>
				</li>
				<li>
					<span class="dot"></span>
					<p>조회 후에 수수료를 결제하시면 최종 등록번호 확인 및 모집인 등록증을 다운로드 받으 실 수 있습니다.</p>
				</li>
				<li>
					<span class="red_dot"></span>
					<p class="red">결제 후 환불은 불가능 합니다.</p>
				</li>
			</ul>
		</div>
	</div>
	<div class="tap_cont">
		<form name="corpUserSearchFrm" id="corpUserSearchFrm" method="post" action="/front/pay/payCorpUserSearch">
			<table class="payment_table">
				<colgroup>
					<col width="124"/>
					<col width="*"/>
				</colgroup>
				<tr>
					<th>금융회사 선택</th>
					<td>
						<select name="comCode" class="comCode" data-vd='{"type":"text","req":true,"msg":"금융회사를 선택해 주세요."}'></select>
					</td>
				</tr>
				<tr>
					<th>법인번호</th>
					<td>
						<input type="text" name="plMerchantNo" placeholder="예) 1234567890123 (-제외)" maxlength="13" data-vd='{"type":"plMerchant","len":"1,13","req":true,"msg":"법인번호를 입력해 주세요."}'>
					</td>
				</tr>
				<tr>
					<th>대표자 성명</th>
					<td>
						<input type="text" name="plCeoName" placeholder="이름을 입력해 주세요." maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"대표자명을 입력해 주세요."}'>
					</td>
				</tr>
			</table>
		</form>
		<div class="bottom_box">
			<ul>
				<li>
					<span class="dot"></span>
					<p>계약된 금융회사를 통해 신청 후에 승인완료 된 모집인에 대한 결제 페이지 입니다.</p>
				</li>
				<li>
					<span class="dot"></span>
					<p>조회 후에 수수료를 결제하시면 최종 등록번호 확인 및 모집인 등록증을 다운로드 받으 실 수 있습니다.</p>
				</li>
				<li>
					<span class="red_dot"></span>
					<p class="red">결제 후 환불은 불가능 합니다.</p>
				</li>
			</ul>
		</div>
	</div>
	<div class="btn_wrap">
		<a href="javascript:void(0);" class="btn_black_long" onclick="goPayUserSearch();">조회</a>
	</div>
</div>

      