<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
function relateSlide() {
	$("#relate_slide").slick({
		slidesToShow	: 6,
		slidesToScroll	: 1,
		autoplay		: true,
		autoplaySpeed	: 3000,
		speed			: 700,
		pauseOnFocus	: false,
		pauseOnHover	: true,
		pauseOnDotsHover: true,
		touchMove		: false,
		infinite		: true,
		arrows			: true,
		dots			: false,
		fade			: false,
		accessibility	: true
	});
}
</script>

		<div class="relate_wrap">
			<div id="relate_slide">
				<a href="https://www.kfb.or.kr" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner01.jpg" alt="은행연합회">
				</a>
				<a href="https://www.fsb.or.kr" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner02.jpg" alt="저축은행중앙회">
				</a>
				<a href="https://www.klia.or.kr/" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner03.jpg" alt="생명보험협회">
				</a>
				<a href="https://www.knia.or.kr/" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner04.jpg" alt="손해보험협회">
				</a>
				
				
				<!-- 
				
				<a href="#" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner05.jpg" alt="금융감독원 서민금융1332서비스 바로가기">
				</a>
				<a href="#" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner06.jpg" alt="신용카드사회공헌재단 바로가기">
				</a>
				<a href="#" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner01.jpg" alt="파인 금융소비자 정보포털 바로가기">
				</a>
				<a href="#" target="_blank" title="새창">
					<img src="/static/images/main/bottom_banner02.jpg" alt="금융감독원 바로가기">
				</a> -->
			</div>
		</div>
	</div> <!-- //id="contents" -->

	<div id="footer">
		<div class="footer_info">
			<div class="copyright">
				<p>
					<a href="/front/index" style="color: rgba(255, 255, 255, 0.6);">HOME</a> |
					<a href="/front/privacy" style="color: rgba(255, 255, 255, 0.6);">개인정보처리방침</a> | 
					<a href="/front/terms" style="color: rgba(255, 255, 255, 0.6);">이용약관</a> | 오시는 길 | 대표전화 02-2011-0700
				</p>
				<p>서울특별시 중구 다동길 43 (다동70번지) 한외빌딩12~13층 여신금융협회 (04521)</p>
				<p>상호명 : (사)한국여신전문금융업협회<span>사업자등록번호 : 202-82-05723</span><span>대표자 : 김주현</span></p>
				<p>COPYRIGHT © THE CREDIT FINANCE ASSOCIATION ALL RIGHTS RESERVED</p>
			</div>
			<div class="footer_logo">
				<a href="#">
					<img src="/static/images/common/fo/footer_logo.png" alt="여신금융협회">
				</a>
			</div>
		</div>
	</div>
</div> <!-- //id="wrap" -->


