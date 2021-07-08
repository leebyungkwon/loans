(function($){
	$(document).ready(function(){
		//레이어팝업 짤림 방지
		$(window).resize(function(){
			var pop_height = $('.popup_wrap .height').height()+70;
			var win_height = window.innerHeight;
			if(pop_height >= win_height){
				$('.popup_wrap .popup_inner').addClass('resize')
			}else{
				$('.popup_wrap .popup_inner').removeClass('resize')
			}
		});
		
		/*
		$('.popup_wrap .pop_close').click(function(){
			$(this).parents('.popup_wrap').hide();
			$('.popup_wrap .popup_inner').removeClass('height');
		});
		*/
		
		//승인남은일수 클릭 이벤트
		$('.btn_sort').click(function(){
			$('.btn_sort').toggleClass('on');
		});
	});
	
	//엔터키 연타 방지(2021-07-08)
	$(document).on("keydown","a",function(e){
		//alert("e.which :: "+e.which);
		if(e.which == 13){
			e.preventDefault();
		}
	});

})(jQuery);

