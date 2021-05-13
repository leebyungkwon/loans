var DataUtil = {
	selectBox: function(obj) {
        var result = "";
        if (WebUtil.isObject(obj)) {
        	obj.success = (typeof (obj.success) === "function") ? obj.success : this.successSelectBox;
        	var param = {}
        	if(obj.useCode){
        		obj.property01 = (typeof (obj.property01) == "undefined") ? '' : obj.property01;
        		obj.property02 = (typeof (obj.property02) == "undefined") ? '' : obj.property02;
        		obj.property03 = (typeof (obj.property03) == "undefined") ? '' : obj.property03;
        		obj.property04 = (typeof (obj.property04) == "undefined") ? '' : obj.property04;
        		obj.property05 = (typeof (obj.property05) == "undefined") ? '' : obj.property05;
        		obj.defaultMsg = (typeof (obj.defaultMsg) == "undefined") ? '' : obj.defaultMsg;
				params = {
					url : '/common/selectCommonCodeList'
					, param : {codeMstCd : obj.code , property01 : obj.property01, property02 : obj.property02, property03 : obj.property03, property04 : obj.property04, property05 : obj.property05}
					, target : obj.target
					, success : obj.success
					, updData : obj.updData
					, useCode : obj.useCode
					, defaultMsg : obj.defaultMsg
					, key : obj.key
					, value : obj.value
				}
        	}else{
        		obj.defaultMsg = (typeof (obj.defaultMsg) == "undefined") ? '' : obj.defaultMsg;
				params = {
					url : obj.url
					, param : obj.param
					, target : obj.target
					, success : obj.success
					, updData : obj.updData
					, useCode : obj.useCode
					, defaultMsg : obj.defaultMsg
					, key : obj.key
					, value : obj.value
				}
        	}
        	
			AjaxUtil.post(params);
        }
        return result;
    }
    ,successSelectBox: function(opt, data) {
    	var key = 'codeDtlCd';
    	var value = 'codeDtlNm';
    	if(!opt.useCode){
    		key = opt.key;
    		value = opt.value;
    	}
        var html = '';
		var codeList = data.data;
		if(codeList.length > 0){
			if(WebUtil.isNotNull(opt.defaultMsg))	html += '<option value="">'+opt.defaultMsg+'</option>';
			else							html += '<option value="">선택해 주세요</option>';
			for(var i = 0; i < codeList.length; i++){
				if(codeList[i][key] == opt.updData){
					html += '<option value="'+codeList[i][key]+'" selected="selected">'+codeList[i][value]+'</option>';
				}else{
					html += '<option value="'+codeList[i][key]+'">'+codeList[i][value]+'</option>';
				}
			}
		}else{
			html += '<option value="">없음</option>';
		}
		$(opt.target).empty();
		$(opt.target).append(html);
    }
};
