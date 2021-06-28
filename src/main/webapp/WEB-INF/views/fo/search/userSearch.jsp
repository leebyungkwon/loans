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
}

//모집인 조회
function goUserSearch() {
	var tabIndex 	= $(".tap_wrap > ul > li.on").index();
	var formNm 		= $("form").eq(tabIndex).attr("name");
	var params 		= $("#"+formNm).serialize();
	var url 		= $("#"+formNm).attr("action");
	
	var p = {
		url		: url
		,param 		: params
		,success	: function(opt,result){
			if(result.code != "fail"){
				goUserSearchResultPage(formNm,result.data);
			}
		}
	}
	AjaxUtil.post(p);
}

//조회 결과 있을 때
function goUserSearchResultPage(formNm,masterSeq) {
	$("#"+formNm).append('<input type="hidden" name="masterSeq" value="'+masterSeq+'">');
	$("#"+formNm).attr("action","/front/search/userSearchResult");
	$("#"+formNm).submit();
}
</script>

<div class="inquiry_wrap">
	<div class="title">대출모집인 조회</div>
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
		<form name="indvUserSearchFrm" id="indvUserSearchFrm" method="post" action="/front/search/indvUserSearch">
			<div class="input_wrap clfix">
				<div class="left_box">
					<div class="input_box mgt0">
						<div class="value">
							<input type="text" name="masterToId" placeholder="등록번호" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"등록번호를 입력해 주세요."}'>
						</div>
					</div>
					<div class="input_box">
						<div class="value">
							<input type="text" name="plCellphone" placeholder="예) 010-1234-5678" maxlength="13" data-vd='{"type":"mobileNo","len":"1,13","req":true,"msg":"휴대폰번호를 입력해 주세요."}'>
						</div>
					</div>
				</div>
				<a href="javascript:void(0);" class="btn_login" onclick="goUserSearch();">조회</a>
			</div>
		</form>
		<div class="bottom_box">
			<ul>
				<li>
					<span class="dot"></span>
					<p>대출상담사 등록번호와 휴대전화번호를 모두 입력해야 조회 가능합니다.</p>
				</li>
				<li>
					<span class="dot"></span>
					<p>조회가능정보: 대충상담사의 등록번호, 성명, 사진, 계약금융회사, 소속법인, 계약일, 위반확정일<span class="red bold">*</span>, 위반사유 등</p>
				</li>
				<li>
					<span class="red bold">*</span><span class="medium"> 위반확정일이란</span><br />
					<p class="mgl11">금융회사가 사실관계를 확인한 후 대출상담사의 모범규준 위반사실을 확정한 날, 2018.6.1.이후 등록된 위반행위부터 위반확정일이 조회됨</p>
				</li>
			</ul>
		</div>
	</div>
	<div class="tap_cont">
		<form name="corpUserSearchFrm" id="corpUserSearchFrm" method="post" action="/front/search/corpUserSearch">
			<div class="input_wrap clfix">
				<div class="left_box">
					<div class="input_box mgt0">
						<div class="value">
							<input type="text" name="masterToId" placeholder="등록번호" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"등록번호를 입력해 주세요."}'>
						</div>
					</div>
					<div class="input_box">
						<div class="value">
							<input type="text" name="plMerchantName" placeholder="법인명" maxlength="30" data-vd='{"type":"text","len":"1,30","req":true,"msg":"법인명을 입력해 주세요."}'>
						</div>
					</div>
				</div>
				<a href="javascript:void(0);" class="btn_login" onclick="goUserSearch();">조회</a>
			</div>
		</form>
		<div class="bottom_box">
			<ul>
				<li>
					<span class="dot"></span>
					<p>법인의 등록번호와 법인명 모두 입력해야 조회 가능합니다.</p>
				</li>
				<li>
					<span class="dot"></span>
					<p>조회가능정보: 대충상담사의 등록번호, 성명, 사진, 계약금융회사, 소속법인, 계약일, 위반확정일<span class="red bold">*</span>, 위반사유 등</p>
				</li>
				<li>
					<span class="red bold">*</span><span class="medium"> 위반확정일이란</span><br />
					<p class="mgl11">금융회사가 사실관계를 확인한 후 대출상담사의 모범규준 위반사실을 확정한 날, 2018.6.1.이후 등록된 위반행위부터 위반확정일이 조회됨</p>
				</li>
			</ul>
		</div>
	</div>
</div>

