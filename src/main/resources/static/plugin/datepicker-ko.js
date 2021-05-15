/* Korean initialisation for the jQuery calendar extension. */
/* Written by DaeKwon Kang (ncrash.dk@gmail.com), Edited by Genie and Myeongjin Lee. */
( function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define( [ "../widgets/datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}( function( datepicker ) {

datepicker.regional.ko = {
	closeText: "닫기",
	prevText: "이전달",
	nextText: "다음달",
	currentText: "오늘",
	monthNames: [ '<span class="month_num">01</span>JAN',
	'<span class="month_num">02</span>FEB',
	'<span class="month_num">03</span>MAR',
	'<span class="month_num">04</span>APR',
	'<span class="month_num">05</span>MAY',
	'<span class="month_num">06</span>JUN',
	'<span class="month_num">07</span>JUL',
	'<span class="month_num">08</span>AUG',
	'<span class="month_num">09</span>SEP',
	'<span class="month_num">10</span>OCT',
	'<span class="month_num">11</span>NOV',
	'<span class="month_num">12</span>DEC' ],
	monthNamesShort: [ "1월","2월","3월","4월","5월","6월",
	"7월","8월","9월","10월","11월","12월" ],
	dayNames: [ "일요일","월요일","화요일","수요일","목요일","금요일","토요일" ],
	dayNamesShort: [ "일","월","화","수","목","금","토" ],
	dayNamesMin: [ "일","월","화","수","목","금","토" ],
	weekHeader: "주",
	dateFormat: "yy. mm. dd.",
	firstDay: 0,
	isRTL: false,
	showMonthAfterYear: true,
	showYearNames: false,
	yearSuffix: "" };



datepicker.setDefaults( datepicker.regional.ko );

return datepicker.regional.ko;

} ) );
