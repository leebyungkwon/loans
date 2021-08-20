var WebUtil = {

    /**
     * null 체크
     * str : 문자열
     */
    isNull: function(str) {
        var type = typeof str;

        if (type === "undefined" || str === null) {
            return true;
        }

        if (type === "string" && this.trim(str) == "") {
            return true;
        }

        return false;
    },

    /**
     * not null 체크
     * str : 문자열
     */
    isNotNull: function(str) {
        return !this.isNull(str);
    },

    /**
     * object 체크
     * obj : 오브젝트
     */
    isObject: function(obj) {
        if (obj !== null && typeof obj === "object") {
            return true;
        }

        return false;
    },

    /**
     * 숫자 체크 체크
     * num : 숫자
     */
    isNumber: function(num) {
        var regex = /^[0-9]+$/;
        return regex.test(num);
    },

    /**
     * 좌우 공백제거
     * str : 문자열
     */
    trim: function(str) {
        if (typeof str === "string") {
            return str.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
        }

        return str;
    },

    /**
     * null 값 바꾸기
     * str : 문자열
     * val : null 대체값
     */
    nvl: function(str, val) {
        if (this.isNull(str)) {
            return val;
        }

        return this.trim(str);
    },

    /**
     * 전체 치환
     * str     : 문자열
     * pattern : 패턴
     * re      : 치환될 문자
     */
    replaceAll: function(str, pattern, re) {
        var type = typeof str;

        if (type === "string" || type === "number") {
            var temp = this.trim(str) + "";

            if (temp != "") {
                return temp.replace(new RegExp(pattern, "g"), re);
            }
        }

        return str;
    },

    /**
     * 파라미터 URL 구하기
     */
    getParamUrl: function(obj) {
        var result = "";

        if (this.isObject(obj)) {
            var m = 0;

            for (var key in obj) {
                if (m > 0) {
                    result += "&"
                }

                result += key + "=" + encodeURIComponent(obj[key]);

                m++;
            }
        }

        return result;
    },

    getParamJson: function(obj, mObj) {
   	 	var result = {};
		if (this.isObject(mObj)) {
			result = mObj;
        }
        if (this.isObject(obj)) {
            for (var key in obj) {
                result[key] = obj[key];
        	}
        }
        return result;
    },
	setHistoryParam: function(param){
		var t = this;
		var key = Object.keys(param);
		
		$(".k_search").find('input[name] , select[name]').filter(function(index, selector){
			var selectorTag = $(selector).prop('tagName');
			var selectorName = $(selector).prop('name');
			if(t.isNotNull(selectorName)){
				var selectorType = $(selector).prop('type');
				for(k in key){
					if(selectorType == 'checkbox') $(".k_search").find("input:checkbox[name='"+selectorName+"']").prop("checked",false);
					
					if(selectorName == key[k] && selectorType == 'radio') $(".k_search").find("input:radio[name='"+selectorName+"']:radio[value='"+param[key[k]]+"']").prop('checked', true);
			 		else if(selectorName == key[k] && selectorType == 'checkbox') $(".k_search").find("input:checkbox[name='"+selectorName+"']").prop("checked",true);
			 		else if(selectorName == key[k] && selectorTag == 'SELECT') 	$(".k_search").find('select[name="'+selectorName+'"]').val(param[key[k]]);
					else if(selectorName == key[k]){
						$(".k_search").find('input[name='+selectorName+']').val(param[key[k]]);
					}
				}
			}
		});
	},
    getTagInParam: function(tag) {
		var t = this;
        var result = {};
        var checkName = new Array();
		$(tag).find('input[name] , select[name]').filter(function(index, selector){
			var selectorTag = $(selector).prop('tagName');
			var selectorName = $(selector).prop('name');

			if(t.isNotNull(selectorName)){
				var selectorType = $(selector).prop('type');

				var value = '';

	        	var checkData = new Array();
				if(selectorType == 'radio') 		value = $(tag).find('input[name="'+selectorName+'"]:checked').val();
		 		else if(selectorType == 'checkbox') ($(this).is(':checked')) ? checkName.push(selectorName) : '';
		 		else if(selectorTag == 'SELECT') 	value = $(tag).find('select[name="'+selectorName+'"]').val();
		 		else								value = $(tag).find('input[name='+selectorName+']').val();

		 		if(selectorType != 'checkbox')	(value != undefined) ? result[selectorName] = value : '';
			}

			//if(checkData.length > 0) result[checkName] = checkData;
		});
		//checkName = Array.from(new Set(checkName))
		/*
		checkName = Array.from(new Set(checkName))
		for(var i in checkName){
			let checkData = new Array();
			$('input:checkbox[name="'+checkName[i]+'"]').each(function(){
				if($(this).is(':checked')) checkData.push($(this).val());
			});
			result[checkName[i]] = checkData;
		}
		*/
		for(var i in checkName){
			var checkData = "";
			$('input:checkbox[name="'+checkName[i]+'"]').each(function(idx){
				if($(this).is(':checked')) {
					if(checkData == '')	checkData = checkData + $(this).val();
					else checkData = checkData + "," + $(this).val();
				}
			});
			result[checkName[i]] = checkData;
		}
        return result;
    },
    getDate: function(d) {
        var result 		= '';
		var nowDate 	= new Date();
        if(d == 'today'){
			var toYear 	= nowDate.getFullYear();
			var toMonth = nowDate.getMonth() + 1; //0 ~ 11(0 : 1월)
			var toDay 	= nowDate.getDate();
			if(toMonth < 10){ toMonth = "0" + toMonth; }
			if(toDay < 10) { toDay = "0" + toDay; }
			result = toYear + "-" + toMonth + "-" + toDay;
        }else if(d == 'yesterday'){
		   	var yesterday 	= new Date(nowDate.setDate(nowDate.getDate() - 1));
		    var toYear 		= yesterday.getFullYear();
			var toMonth 	= yesterday.getMonth() + 1;
			var toDay 		= yesterday.getDate();
			if(toMonth < 10){ toMonth = "0" + toMonth; }
			if(toDay < 10) { toDay = "0" + toDay; }
		    result = toYear + "-" + toMonth + "-" + toDay;
        }else if(d == 'dayFirst'){
		    var toYear 	= nowDate.getFullYear();
			var toMonth = nowDate.getMonth() + 1;
			if(toMonth < 10){ toMonth = "0" + toMonth; }
		    result = toYear + "-" + toMonth + "-01";
        }else if(d == 'monFirst'){
		    var toYear = nowDate.getFullYear();
		    result = toYear + "-" + "01-01";
        }else if(d == 'oneMonthAgo'){
        	var toYear 	= Number(nowDate.getFullYear());
			var toMonth = Number(nowDate.getMonth() + 1) - 1;
			var toDay 	= Number(nowDate.getDate());
			if(toMonth == 0){
				toMonth = "12";
				toYear 	= nowDate.getFullYear() -1;
			}else if(toMonth < 9){
				toMonth = "0" + Number(toMonth);
			}
			if(toDay < 10) { toDay = "0" + toDay; }
			result = toYear + "-" + toMonth + "-" + toDay;
        }else if(d == 'oneMonthLater'){
        	var toYear 	= Number(nowDate.getFullYear());
			var toMonth = Number(nowDate.getMonth() + 1) + 1;
			var toDay 	= Number(nowDate.getDate());
			if(toMonth == 13){
				toMonth = "01";
				toYear 	= nowDate.getFullYear() + 1;
			}else if(toMonth < 11){
				toMonth = "0" + toMonth;
			}
			if(toDay < 10) { toDay = "0" + toDay; }
			result = toYear + "-" + toMonth + "-" + toDay;
        }else{
			var rDate = nowDate.getTime() - (d * 24 * 60 * 60 * 1000);
			nowDate.setTime(rDate);
			var rYear = nowDate.getFullYear();
			var rMonth = nowDate.getMonth() + 1;
			var rDay = nowDate.getDate();
			if(rMonth < 10){ rMonth = "0" + rMonth; }
			if(rDay < 10) { rDay = "0" + rDay; }
			result = rYear + "-" + rMonth + "-" + rDay;
        }
        return result;
    },
    setHour: function(id, hour, selected){
		var sel = document.getElementById(id);
		for(var i=0; i<hour; i++){
			var opt = document.createElement('option');
			var txt = (i<10) ? '0'+i : i;
			opt.text = txt;
			opt.value = txt;
			sel.add(opt);
			if (sel.options[i].value == selected)  sel.options[i].selected = "selected";
		}
		return sel;
    },

    // 금액표시
    numberWithCommas: function(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

	camel2Snake: function(s, u) {
		let r = '';

		if (s !== undefined && s !== null) {
			let sp = s.split(/(?=[A-Z])/);
			if (u !== undefined && u !== null && u) {
				r = sp.join('_').toUpperCase();
			} else {
				r = sp.join('_').toLowerCase();
			}
		}

		return r;
	}

};

String.prototype.escapeHtml = function(){
  return this.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/\"/g, "&quot;");
};

String.prototype.unescapeHtml = function(){
  return this.replace(/&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g, "\"");
};




