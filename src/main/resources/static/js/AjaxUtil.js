var AjaxUtil = {
    /**
     * GET 방식
     */
    get: function(opt) {
        // 응답 유형
        opt.resType = WebUtil.nvl(opt.resType, "json");
        var contType = WebUtil.nvl(opt.reqType, "application/x-www-form-urlencoded; charset=UTF-8");

        // 로그여부
        var loadYn = WebUtil.nvl(opt.loadYn, true);

        // 로딩바 열기
        this.openLoadBar(loadYn);

        // ajax 호출
        axios({
            method: "get",
            url: opt.url,
            params: opt.param,
            headers: { "Content-Type": contType }
        }).then(function(response) {
            AjaxUtil.closeLoadBar(loadYn);
            AjaxUtil.successHandler(opt, response.data);
        }).
        /*
        catch(function(error) {
            AjaxUtil.closeLoadBar(loadYn);
            AjaxUtil.errorHandler(opt.error, error);
        }).
        */
        then(function () {
            // 로딩바 닫기
            AjaxUtil.closeLoadBar(loadYn);
            if (typeof opt.complete === "function") {
                opt.complete();
            }
        });
    },

    /**
     * POST 방식
     */
    post: function(opt) {
        var param = null;
        var reqType = WebUtil.nvl(opt.reqType, "text/html");
        var contType = WebUtil.nvl(opt.reqType, "application/x-www-form-urlencoded; charset=UTF-8");
        var async = WebUtil.nvl(opt.async, true);
        var loadYn = WebUtil.nvl(opt.loadYn, true);
        var responseType = WebUtil.nvl(opt.responseType, "");

        // 파라미터 json 설정
        if (reqType == "json") {
            param = JSON.stringify(opt.param);
            contType = "application/json; charset=UTF-8";
        } else if (WebUtil.isObject(opt.param)){
            param = WebUtil.getParamUrl(opt.param);
        } else {
        	param = opt.param;
        }
        // 응답 유형
        opt.resType = WebUtil.nvl(opt.resType, "json");

        // 로딩바 열기
        this.openLoadBar(loadYn);

        // ajax 호출
        axios({
            method: "post",
            url: opt.url,
            data: param,
            async:async,
            responseType: responseType,
            headers: { "Content-Type": contType }
        }).then(function(response) {
			AjaxUtil.closeLoadBar(loadYn);
        	var status = WebUtil.nvl(response.data.status, 200);
        	console.log("#AjaxUtil.post()#" , response);
        	if(status == 200) {
            	if(WebUtil.isNotNull(response.data.code)){
            		if(WebUtil.isNotNull(response.data.message)) alert(response.data.message);
            		else alert(messages[response.data.code])
            	}
            	AjaxUtil.successHandler(opt, response.data);
        	}else{
        		if(WebUtil.isNotNull(response.data.code)){
            		if(WebUtil.isNotNull(response.data.message)) alert(response.data.message);
            		else alert(messages[response.data.code])
            	}else{
					alert("오류가 발생하였습니다.\n관리자에 문의해 주세요.");
				}
        	}

        })
        .catch(function(error) {
            AjaxUtil.closeLoadBar(loadYn);
            AjaxUtil.errorHandler(opt.error, error);
        })
        .then(function () {
            // 로딩바 닫기
            AjaxUtil.closeLoadBar(loadYn);

            if (typeof opt.complete === "function") {
                opt.complete();
            }
        });
    },
    excel: function(opt) {
        var param = null;
        var reqType = WebUtil.nvl(opt.reqType, "text/html");
		var loadYn = WebUtil.nvl(opt.loadYn, true);
        // 로딩바 열기
        this.openLoadBar(loadYn);
        
        // 2021-06-29 엑셀버튼명 추가
        var excelFileNm = opt.param.excelFileNm;
        if(WebUtil.isNull(excelFileNm)){
			excelFileNm = "엑셀다운로드";
		}

        param = JSON.stringify(opt.param);
        // ajax 호출
        axios({
            method: "post",
            url: opt.url,
            data: param,
            responseType: 'blob',
    		headers: {"Content-Type": "application/json"},
        }).then(function(response) {
			AjaxUtil.closeLoadBar(loadYn);
			
			// 2021-06-29 IE 엑셀다운로드 수정
		    if(window.navigator.msSaveOrOpenBlob){
				var aa = new Blob([response.data]);
				window.navigator.msSaveBlob(aa, excelFileNm+'.xlsx');
			}else{
				const url = window.URL.createObjectURL(new Blob([response.data], { type: response.headers['content-type'] }));	
			    const link = document.createElement('a');
			    link.href = url;
			    link.setAttribute('download', excelFileNm+'.xlsx');
			    document.body.appendChild(link);
			    link.click();
			}
        })
        /*
        .catch(function(error) {
            //AjaxUtil.closeLoadBar(loadYn);
            AjaxUtil.errorHandler(opt.error, error);
        })
        */
        .then(function () {
            // 로딩바 닫기
            AjaxUtil.closeLoadBar(loadYn);

            if (typeof opt.complete === "function") {
                opt.complete();
            }
        });
    },
    /**
     * Form 방식
     */
    form: function(opt) {
        let frm = document.getElementsByName(opt.name);
        if(frm.length!=1) return false;

        let f = true;
        $.each(frm[0].querySelectorAll('input, select'), function(key, value) {
        	f = Valid.set(value);
        	if(!f) return false;
		});
    	if(!f) return false;

        let p =  new URLSearchParams(new FormData(frm[0])).toString();
        let param = {
        	url : frm[0].action
        	, param : p
        	, success : opt.success
        }
        this.post(param);
    },

    /**
     * Form submit 방식
     */
    submit: function(name) {
        let frm = document.getElementsByName(name);
        if(frm.length!==1) return false;

        let f = true;
        $.each(frm[0].querySelectorAll('input'), function(key, value) {
        	if(!f) return false;
        	f = Valid.set(value);
		});

    	if(!f) return false;

		if(f) frm[0].submit();
    },

    /**
     * FormData 방식
     */
    mkform: function(opt) {
        var frm, input;
		frm = document.createElement('form');
		frm.method = "POST";
		frm.action = opt.url;
		document.body.appendChild(frm);

        //this.openLoadBar(loadYn);

		if (opt.param != null && typeof (opt.param) != "undefined") {
			$.each(opt.param, function(key, value) {
				input = document.createElement('input');
				input.setAttribute('type', 'hidden');
				input.setAttribute('name', typeof key == 'undefined' ? '' : key);
				input.setAttribute('value', value);
				frm.appendChild(input);
			});
		}
		frm.submit();
    },

    /**
     * Form submit 방식
     */
    files: function(opt) {
        let frm = document.getElementsByName(opt.name);
        if(frm.length!==1) return false;

        let f = true;
        $.each(frm[0].querySelectorAll('input'), function(key, value) {
        	if(!f) return false;
        	f = Valid.set(value);
		});
    	if(!f) return false;
        let formData = new FormData(frm[0]);
        
		var loadYn = WebUtil.nvl(opt.loadYn, true);
        // 로딩바 열기
        this.openLoadBar(loadYn);
        
        axios({
            method: "post",
            url: frm[0].action,
            data: formData,
            headers: { "Content-Type": "multipart/form-data" }
        }).then(function(response) {
            AjaxUtil.closeLoadBar(loadYn);
        	var status = WebUtil.nvl(response.data.status, 200);
        	if(status == 200) {
            	if(WebUtil.isNotNull(response.data.code)){
            		if(WebUtil.isNotNull(response.data.message)) alert(response.data.message);
            		else alert(messages[response.data.code])
            	}
            	AjaxUtil.successHandler(opt, response.data);
        	}else{
        		if(WebUtil.isNotNull(response.data.code)){
            		if(WebUtil.isNotNull(response.data.message)) alert(response.data.message);
            		else alert(messages[response.data.code])
            	}else{
					alert("오류가 발생하였습니다.\n관리자에 문의해 주세요.");
				}
        	}
        })
        .catch(function(error) {
            AjaxUtil.closeLoadBar(loadYn);
            AjaxUtil.errorHandler(opt.error, error);
        })
        
        .then(function () {
            // 로딩바 닫기
            AjaxUtil.closeLoadBar(loadYn);

            if (typeof opt.complete === "function") {
                opt.complete();
            }
        });
    },

    /**
     * 로딩바 열기
     */
    openLoadBar: function(loadYn) {
    	if(loadYn){
			$(".loading_wrap").show();
		}
    },

    /**
     * 로딩바 닫기
     */
    closeLoadBar: function(loadYn) {
    	if(loadYn){
			$(".loading_wrap").hide();
		}
    },

    /**
     * 성공 핸들러
     */
    successHandler: function(opt, resData) {
        if (typeof opt.success === "function") {
            opt.success(opt,resData);
        }
    },

    /**
     * 에러 핸들러
     */
    errorHandler: function(callback, error) {
		console.log(callback, error);
        var errMsg = "서버에 일시적인 문제가 생겼습니다.\n잠시후에 다시 이용해주세요.";

        if (error.response) {
            var resData = error.response.data;
            if (WebUtil.isObject(resData.errors[0]) && WebUtil.isNotNull(resData.errors[0].defaultMessage)) {
                errMsg = resData.errors[0].defaultMessage;
            }

        } else if (error.request) {
            var resTxt = error.request.responseText;

            if (WebUtil.isNotNull(resTxt)) {
                var resData = JSON.parse(resTxt);

                if (WebUtil.isObject(resData) && WebUtil.isNotNull(resData.message)) {
                    errMsg = resData.message;
                }
            }
        } else {
            alert("오류가 발생하였습니다. \n관리자에 문의해 주세요.");
        }

        errMsg = WebUtil.replaceAll(errMsg, "\\\\n", "\n");

        if (typeof callback === "function") {
            callback(errMsg);
        } else {
            errMsg = WebUtil.replaceAll(errMsg, "\n", "<br />");
            //LayerUtil.alert({ msg: errMsg });
            alert(errMsg);
            console.log(errMsg);
        }
    }

};
