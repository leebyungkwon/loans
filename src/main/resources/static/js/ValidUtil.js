var Valid = {
    set: function(obj) {
		if(typeof obj.dataset.vd === 'undefined' || typeof obj.dataset.vd === null || typeof obj.dataset.vd === '') {
			return true;
		}

    	let el = JSON.parse(obj.dataset.vd);
    	let val = obj.value;
    	let len = el.len;
    	let msg = el.msg;
    	let eq = el.eq;
    	let req = el.req;
    	
    	if(!req) return true;
    	if(val == '') {
    		if(typeof msg === 'undefined' || typeof msg === null || typeof msg === '') msg = '필수값을 입력하세요.';
    		alert(msg);
    		$(obj).focus();
    		return false;
    	}
    	
    	if(typeof eq != 'undefined' && typeof eq != null && typeof eq != ''){
    		let e = document.getElementsByName(eq)[0].value;
    		if(e != val) {
    			alert(msg);
    			$(obj).focus();
    			return false;
    		}
    	}
    	let regexp = '';

    	if(typeof len != 'undefined' && typeof len != null && typeof len != '') {
    		let l = len.split(',');
    		if(l.length>0) {
    			if(typeof l[0] != 'undefined'){
    				if(l[0]>val.length){
    					alert('최소 ' + l[0]+ ' 자로 입력하세요.');
    					$(obj).focus();
    					return false;
    				}
    			}
    			if(typeof l[1] != 'undefined'){
    				if(l[1]<val.length){
    					alert('최대 ' + l[1]+ ' 자로 입력하세요.');
    					$(obj).focus();
    					return false;
    				}
    			}
    		}
		}
    	
    	switch (el.type){
	    	case 'plMerchant' :
	    		regexp = /^(\d{6})-(\d{7})*$/;
	    		if(!regexp.test(val))	alert('법인등록번호 형식을 확인해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	    		break;
	    	case 'pId' :
	    		regexp = /^(\d{6})-(\d{7})*$/;
	    		if(!regexp.test(val))	alert('주민번호 형식을 확인해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	    		break;
	    	case 'plBusiness' :
	    		regexp = /^(\d{3})-(\d{2})-(\d{5})*$/;
	    		if(!regexp.test(val))	alert('사업자등록번호 형식을 확인해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	    		break;
	    	case 'mobileNo' :
	    		regexp = /^\d{3}-\d{3,4}-\d{4}$/;
	    		if(!regexp.test(val))	alert('휴대폰번호 형식을 확인해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	    		break;
	    	case 'extensionNo' :
	    		regexp = /^\d{2,3}-\d{3,4}-\d{4}$/;
	    		if(!regexp.test(val))	alert('회사전화번호 형식을 확인해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	    		break;
	        case 'num' :
	        	regexp = /^[0-9]*$/;
	    		if(!regexp.test(val))	alert('숫자만 입력해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	            break;
	        case 'email' :
	        	regexp=/^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{1,5}$/;
	    		if(!regexp.test(val))	alert('이메일 형식으로 입력해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	            break;
	        case 'id' :
	        	regexp= /^[a-z][a-z0-9]{4,10}$/g;
	        	if(!regexp.test(val))	alert('ID는 영문(소문자),숫자 5~11 자리로 입력해 주세요.');
	        	else return true;
	        	$(obj).focus();
	        	return false;
	        	break;
	        case 'pw' :
	        	regexp=/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;
	    		if(!regexp.test(val))	alert('비밀번호는 최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 입력해 주세요.');
	    		else return true;
	    		$(obj).focus();
	    		return false;
	            break;
	        case 'text' :
	    		return true;
	            break;
			case 'fileupload' :
				regexp=/\.(jpg|gif|tif|bmp|png)$/i;
				if(!regexp.test(val))	alert('파일 형식을 확인해 주세요.');
	    		else return true;
	    		$(obj).focus();
				return false;
	            break;
	        default :
	        	alert('유효성검사 TYPE을 확인해 주세요.');
	        	return false;
    	}
    	
    },

 	fileCheck: function(fileSize, ext, excelYn){
		var maxSize = 20 * 1024 * 1024; //20MB
		if(fileSize > maxSize){
			alert("20MB 이하의 파일만 업로드 가능합니다.");
			return false;	
		}
		if(excelYn == "Y"){
			if($.inArray(ext, ["xls", "xlsx"]) == -1) {
				alert("엑셀파일만 첨부해 주세요.");
				return false;
			}
		}else{
			if($.inArray(ext, ["hwp", "jpg", "jpeg", "png", "gif", "bmp", "pdf", "zip"]) == -1) {
				alert("hwp, jpg, jpeg, png, gif, bmp, pdf, zip 파일만 첨부가능합니다.");
				return false;
			}
		}
		return true;
	}
};
