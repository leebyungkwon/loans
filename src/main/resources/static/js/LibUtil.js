let LibUtil = {
	openPopup: function(opts){
 		if(document.querySelectorAll("#"+opts.id).length >= 1) return false;
 		let url = WebUtil.nvl(opts.url, "/common/openPopup");
		let p = { url : opts.page};
		p = WebUtil.getParamJson(opts.data , p);
		let params = {
			url : url
			, param : p
			, success : function(opt,data){
		    	const Element = document.querySelector('.layerPopupBase');
				let cloneEl = Element.cloneNode(true);
				cloneEl.className = 'layerPopup';
				cloneEl.id = opts.id;
				cloneEl.style.display = 'block';
				cloneEl.querySelector('.layerContent').innerHTML = data;
				//document.querySelector('body').appendChild(cloneEl);
				$('body').append(cloneEl);
				if(WebUtil.isNotNull(opts.data)){
					let inputEl = document.getElementById(opts.id).getElementsByTagName("input");
					for (let i=0;i<inputEl.length;i++) {
						if(WebUtil.isNotNull(opts.data[inputEl[i].name])){
							if(inputEl[i].type=='radio') 	if(inputEl[i].value == opts.data[inputEl[i].name]) inputEl[i].checked = true;
							if(inputEl[i].type=='checkbox') if(inputEl[i].value == opts.data[inputEl[i].name]) inputEl[i].checked = true;
							if(inputEl[i].type=='text')	{
								inputEl[i].value = (typeof opts.data[inputEl[i].name] === "string") ? opts.data[inputEl[i].name].unescapeHtml() : opts.data[inputEl[i].name];
							}
							if(inputEl[i].type=='hidden')	{
								inputEl[i].value = (typeof opts.data[inputEl[i].name] === "string") ? opts.data[inputEl[i].name].unescapeHtml() : opts.data[inputEl[i].name];
							}
						}
					}
					let textareaEl = document.getElementById(opts.id).getElementsByTagName("textarea");
					for (let i=0;i<textareaEl.length;i++) {
						if(WebUtil.isNotNull(opts.data[textareaEl[i].name])){
							textareaEl[i].value = opts.data[textareaEl[i].name].unescapeHtml();
						}
					}
					let selectEl = document.getElementById(opts.id).getElementsByTagName("select");
					for (let i=0;i<selectEl.length;i++) {
						if(WebUtil.isNotNull(opts.data[selectEl[i].name])){
							//console.log(selectEl[i].name,opts.data[selectEl[i].name], selectEl[i]);
							if(WebUtil.isNotNull(opts.data[selectEl[i].name])) selectEl[i].value = opts.data[selectEl[i].name];
						}
					}
				}

				cloneEl.querySelector(".closeLayer").onclick = function(){
					//this.parentElement.remove();
					this.parentElement.parentNode.removeChild(this.parentElement);
				};

				if (typeof popupInit === "function") {
					popupInit(opts.data);
	            }

	        }
		};
		AjaxUtil.get(params);
    }
	, closePopup: function(id){
		document.getElementById(id).remove();
	}
	, msgOpenPopup: function(message){

    	const Element = document.querySelector('.msgLayerPopupBase');
		let cloneEl = Element.cloneNode(true);
		cloneEl.className = 'msgLayerPopup';
		cloneEl.style.display = 'block';
		cloneEl.querySelector('.layerContent').innerHTML = message;
		//document.querySelector('body').appendChild(cloneEl);
		$('body').append(cloneEl);
		cloneEl.querySelector(".msgCloseLayer").onclick = function(){
			this.parentElement.remove();
		};
	}
	, getMessage: function(){
		return
	}
    , openWinPopup: function(opts){
   		let win = window.open(opts.popupUrl, "PopupWin", "width=500,height=600");
    }
	, datePicker: function(opt){
	    let thisDate = new Date();
	    let thisYear = thisDate.getFullYear();   //해당 연
	    let thisMonth = thisDate.getMonth() + 1; //해당 월
	    if(typeof opt != 'undefined' && typeof opt.target != 'undefined'){
	    	let w = '';
	    	if(opt.useRange){
	    		w = 'style="width:35% !important;"';
	    		if(opt.time)	w = 'style="width:25% !important;"';
	    	}

	    	if(typeof opt.startDtNm != 'undefined') {
	    		let startDt = (typeof opt.startDtVal === 'undefined') ? WebUtil.getDate("oneMonthAgo") :  opt.startDtVal;
	    		let startTm = (typeof opt.startTmVal === 'undefined') ? '00' : opt.startTmVal ;
	    		$(opt.target).append('<input name="'+opt.startDtNm+'" class="date sDatepicker" type="text" '+w+' value="'+startDt+'" readonly="readonly"/>');
	    		if(opt.time) {
	    			let t = $('<select name="'+opt.startTmNm+'" id="'+opt.startTmNm+'" style="width:9%;margin-left:1%;"></select>');
	    			$(opt.target).append(t);
	    			WebUtil.setHour(opt.startTmNm, 24, startTm);
	    		}
	    	}
	    	if(typeof opt.endDtNm != 'undefined') {
	    		let endtDt = (typeof opt.endDtVal === 'undefined') ? WebUtil.getDate('oneMonthLater') :  opt.endDtVal;
	    		if(opt.nextYear){
	    			endtDt = WebUtil.getDate(-365);
	    		}
	    		let endTm = (typeof opt.endTmVal === 'undefined') ? '23' : opt.endTmVal;
	    		$(opt.target).append(' ~ <input name="'+opt.endDtNm+'" class="date eDatepicker" type="text" '+w+' value="'+endtDt+'" readonly="readonly"/>');
	    		if(opt.time) {
	    			let t = $('<select name="'+opt.endTmNm+'" id="'+opt.endTmNm+'" style="width:9%;margin-left:1%;"></select>');
	    			$(opt.target).append(t);
	    			WebUtil.setHour(opt.endTmNm, 24,endTm);
	    		}
	    	}


	    	this.setDatePicker('range', opt.startDtNm, opt.endDtNm);
    	}else{
    		this.setDatePicker();
    		return;
    	}


	    $.datepicker.regional['ko'] = {
	       //Input Display Format 변경
	        dateFormat: 'yy-mm-dd'
	       //월 1일 이전, 월 말일 이후 빈칸에 이전달, 다음달 날짜 출력 여부
	       ,showOtherMonths: true
	       // 주(week)수 출력 여부
	       ,showWeek : false
	       // 달력에서 좌우 선택시 이동할 개월 수
	       ,stepMonths : 1
	       //년도 먼저 나오고, 뒤에 월 표시
	       ,showMonthAfterYear:true
	       //콤보박스에서 년 선택 가능
	       ,changeYear: true
	       //콤보박스에서 월 선택 가능
	       ,changeMonth: true
	       //달력의 년도 부분 뒤에 붙는 텍스트
	       ,yearSuffix: "년"
	       // next,prev 아이콘의 툴팁.
	       ,nextText: "다음 달"
	       ,prevText: "이전 달"
	       // 캘린더 하단에 버튼 패널을 표시한다.
	       ,showButtonPanel: false
	       // 오늘 날짜로 이동하는 버튼 패널
	       ,currentText: '오늘'
	       // 닫기 버튼 패널
	       ,closeText: '닫기'
	       //우측에 달력 icon 을 보인다.
	       //,showOn: 'both'
	       //우측 달력 icon 의 이미지 패스
	       //,buttonImage: '/images/common/calendar-icon.png'
	       // inputbox 뒤에 달력icon만 표시한다. ('...' 표시생략)
	       //,buttonImageOnly: true
	       // 달력의 월 부분 텍스트
	       ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12']
	       // 달력의 월 부분 Tooltip 텍스트
	       ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
	       // 달력의 요일 부분 텍스트
	       ,dayNamesMin: ['일','월','화','수','목','금','토']
	       // 달력의 요일 부분 Tooltip 텍스트
	       ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일']
	       // 년도 선택 셀렉트박스를 현재 년도에서 이전, 이후로 얼마의 범위를 표시할것인가. (10년전, 10년후 까지표시)
	       ,yearRange: 'c-10:c+10'
	       //애니메이션을 적용한다. show(디폴트), slideDown, fadeIn etc..
	       ,showAnim: "slideDown"
	       // 여러개월 달력을 표시 [줄,달력수]
	       ,numberOfMonths: 1
	    };
	    let datepicker = $.extend({}, $.datepicker.regional['ko'], opt);

	    $.datepicker.setDefaults(datepicker);

	    if(WebUtil.isNotNull(opt.useRange)){
	    	let m = ['1개월','3개월','6개월','1년'];
	    	let m_v = [30,60,180,365];

	    	let d = ['당일','1주일','1개월','3개월'];
	    	let d_v = [0,7,30,90];

	        let html = '';
			let selectList = [];
			let selectVal =  [];

			if(opt.range == 'month'){
				selectList = m;
				selectVal = m_v;
			}else if(opt.range == 'day'){
				selectList = d;
				selectVal = d_v;
			}
			if(selectList.length > 0){
				html += '<select style="width:23%" class=" ml1p dib" id="dateRange"><option value="" selected="selected">기간선택</option>';
				for(let i = 0; i < selectList.length; i++){
					html += '<option value="'+selectVal[i]+'">'+selectList[i]+'</option>';
				}
				html += '</select>';
			}

			if(opt.time) $('[name="'+opt.endDtNm+'"]').next().after(html);
			else		 $('[name="'+opt.endDtNm+'"]').after(html);

			$(opt.target).find("#dateRange").on('change', function(){
				$(opt.target).find('[name="'+opt.startDtNm+'"]').val(WebUtil.getDate(this.value));
				$(opt.target).find('[name="'+opt.endDtNm+'"]').val(WebUtil.getDate('today'));
			});

	    }
    }
    ,setDatePicker: function(typ, startDtNm, endDtNm){
    	if(typ == 'range'){
	    	//시작일의 초기값을 설정
		    $('[name="'+startDtNm+'"]').datepicker({
		          onSelect: function(dateText, inst) {
		        	let s = $('[name="'+startDtNm+'"]').val();
					let e = $('[name="'+endDtNm+'"]').val();
					if(s!=''&&e!=''&&s>e) {
						alert('종료일이 시작일 보다 빠릅니다.')
						$('[name="'+startDtNm+'"]').val(inst.lastVal);
						return false;
					};
		        }
		        , onClose: function( selectedDate ) {
		            $(".eDatepicker").datepicker( "option", "minDate", selectedDate );
		            // 달력 click시 초기화
		            $("#dateRange").val("");
		            // 시작일(sDatepicker) datepicker가 닫힐때
		            // 종료일(eDatepicker)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
		        }
		    });
		    //종료일의 초기값을 내일로 설정
		    $('[name="'+endDtNm+'"]').datepicker({
		         stepMonths : 2
		        , onSelect: function(dateText, inst) {
		        	let s = $('[name="'+startDtNm+'"]').val();
					let e = $('[name="'+endDtNm+'"]').val();
					if(s>e) {
						alert('종료일이 시작일 보다 빠릅니다.')
						$('[name="'+endDtNm+'"]').val(inst.lastVal);
						return false;
					};
		        }
		        , onClose: function( selectedDate ) {
		            $(".sDatepicker").datepicker( "option", "maxDate", selectedDate );
		            // 달력 click시 초기화
		            $("#dateRange").val("");
		            // 종료일(eDatepicker) datepicker가 닫힐때
		            // 시작일(eDatepicker)의 선택할수있는 최대 날짜(maxDate)를 선택한 종료일로 지정
		        }
		    });
    	}else{
    		$('.dDatepicker').datepicker();
    	}

    }
};
var messages = {
	COM0001: '저장되었습니다.',
	COM0002: '저장에 실패하였습니다.',
	COM0003: '사용 중인 아이디입니다.',
	COM0004: '사용 가능한 아이디입니다.',
	COM0005: '같은 해시태그가 존재합니다.',
	COM0006: '삭제되었습니다.',
	COM0007: '필수 첨부서류가 누락되었습니다.'
}
