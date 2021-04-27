var DataUtil = {
	selectBox: function(obj) {
        var result = "";
        if (WebUtil.isObject(obj)) {
        	obj.success = (typeof (obj.success) === "function") ? obj.success : this.successSelectBox;
        	var param = {}
        	if(obj.useCode){
        		obj.propertie01 = (typeof (obj.propertie01) == "undefined") ? '' : obj.propertie01;
        		obj.propertie02 = (typeof (obj.propertie02) == "undefined") ? '' : obj.propertie02;
        		obj.propertie03 = (typeof (obj.propertie03) == "undefined") ? '' : obj.propertie03;
        		obj.propertie04 = (typeof (obj.propertie04) == "undefined") ? '' : obj.propertie04;
        		obj.propertie05 = (typeof (obj.propertie05) == "undefined") ? '' : obj.propertie05;
        		obj.defaultMsg = (typeof (obj.defaultMsg) == "undefined") ? '' : obj.defaultMsg;
				params = {
					url : '/common/selectCommonCompanyCodeList'
					, param : {codeMstCd : obj.code , propertie01 : obj.propertie01, propertie02 : obj.propertie02, propertie03 : obj.propertie03, propertie04 : obj.propertie04, propertie05 : obj.propertie05}
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
