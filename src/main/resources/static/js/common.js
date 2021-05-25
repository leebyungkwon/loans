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
     })

     //레이어팝업 짤림 방지

     $('#pop_01').click(function(){
       $('.popup_wrap').show();
       $('.popup_wrap .popup_inner').addClass('height');
     });

     $('.popup_wrap .pop_close').click(function(){
       $(this).parents('.popup_wrap').hide();
       $('.popup_wrap .popup_inner').removeClass('height');
     });
     
     

   });

$(document).ready(function(){
   $('.btn_sort').click(function(){
     $('.btn_sort').toggleClass('on');
   });
});

})(jQuery);
