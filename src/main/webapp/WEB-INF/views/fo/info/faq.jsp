<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
function pageLoad() {
	$(".popup_list_area .mother_list").click(function(){
		var pop_list_index = $(this).parent().index();
		if($(this).parent().hasClass("active") == true){
			$(".popup_list_area > ul > li").removeClass("active");
			$(this).parent().find('.contents_list').stop().slideUp(200);
		}else{
			$('.popup_list_area > ul > li').parent().find(".contents_list").stop().slideUp(200);
			$('.popup_list_area > ul > li').removeClass("active").eq(pop_list_index).addClass("active");
			$(this).parent().find(".contents_list").stop().slideDown(200);
		}
	});
}
</script>

<div class="inquiry_wrap">
	<div class="title">자주 묻는 질문</div>
	<div class="popup_list_area">
		<ul>
			<li>
				<div class="mother_list">
					<span class="tag">질문</span>
					<p class="list_title">대출모집인과 대출상담사의 차이가 무엇인가요?</p>
					<a href="javascript:void(0);" class="ctgr_down"></a>
				</div>
				<div class="contents_list clfix" style="display: none;">
					대출모집인은 대출상담사와 대출모집법인을 의미합니다.<br />
					대출상담사는 금융회사 또는 대출모집법인과 대출모집업무 위탁계약을 체결하고 해당 금융업협회에 등록한 개인입니다.<br />
					대출모집법인은 금융회사와 대출모집위탁계약을 체결하고 해당 금융업협회에 등록한 상법상 법인입니다.<br /><br />
					[대출모집인 제도 모범규준 제3조 제2~4호]
				</div>
			</li>
			<li>
				<div class="mother_list">
					<span class="tag">질문</span>
					<p class="list_title">대출상담사로부터 대출상담을 받았습니다. 해당 대출상담사가 금융업협회에 등록된 대출상담사인지 어떻게 확인할 수 있나요?</p>
					<a href="javascript:void(0);" class="ctgr_down"></a>
				</div>
				<div class="contents_list clfix" style="display: none;">
					대출모집인은 대출상담사와 대출모집법인을 의미합니다.<br />
					대출상담사는 금융회사 또는 대출모집법인과 대출모집업무 위탁계약을 체결하고 해당 금융업협회에 등록한 개인입니다.<br />
					대출모집법인은 금융회사와 대출모집위탁계약을 체결하고 해당 금융업협회에 등록한 상법상 법인입니다.<br /><br />
					[대출모집인 제도 모범규준 제3조 제2~4호]
				</div>
			</li>
			<li>
				<div class="mother_list">
					<span class="tag">질문</span>
					<p class="list_title">대출상담사는 금융회사 직원인가요?</p>
					<a href="javascript:void(0);" class="ctgr_down"></a>
				</div>
				<div class="contents_list clfix" style="display: none;">
					대출모집인은 대출상담사와 대출모집법인을 의미합니다.<br />
					대출상담사는 금융회사 또는 대출모집법인과 대출모집업무 위탁계약을 체결하고 해당 금융업협회에 등록한 개인입니다.<br />
					대출모집법인은 금융회사와 대출모집위탁계약을 체결하고 해당 금융업협회에 등록한 상법상 법인입니다.<br /><br />
					[대출모집인 제도 모범규준 제3조 제2~4호]
				</div>
			</li>
			<li>
				<div class="mother_list">
					<span class="tag">질문</span>
					<p class="list_title">대출상담사가 개인정보를 유출시켜서 손해가 발생할 경우 어떻게 하나요?</p>
					<a href="javascript:void(0);" class="ctgr_down"></a>
				</div>
				<div class="contents_list clfix" style="display: none;">
					대출모집인은 대출상담사와 대출모집법인을 의미합니다.<br />
					대출상담사는 금융회사 또는 대출모집법인과 대출모집업무 위탁계약을 체결하고 해당 금융업협회에 등록한 개인입니다.<br />
					대출모집법인은 금융회사와 대출모집위탁계약을 체결하고 해당 금융업협회에 등록한 상법상 법인입니다.<br /><br />
					[대출모집인 제도 모범규준 제3조 제2~4호]
				</div>
			</li>
			<li>
				<div class="mother_list">
					<span class="tag">질문</span>
					<p class="list_title">대출모집인이 대출 전 수수료를 요구할 경우에는 어떻게 하나요?</p>
					<a href="javascript:void(0);" class="ctgr_down"></a>
				</div>
				<div class="contents_list clfix" style="display: none;">
					대출모집인은 대출상담사와 대출모집법인을 의미합니다.<br />
					대출상담사는 금융회사 또는 대출모집법인과 대출모집업무 위탁계약을 체결하고 해당 금융업협회에 등록한 개인입니다.<br />
					대출모집법인은 금융회사와 대출모집위탁계약을 체결하고 해당 금융업협회에 등록한 상법상 법인입니다.<br /><br />
					[대출모집인 제도 모범규준 제3조 제2~4호]
				</div>
			</li>
		</ul>
	</div>
</div>

