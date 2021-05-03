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
    		return false;
    	}
    	
    	if(typeof eq != 'undefined' && typeof eq != null && typeof eq != ''){
    		let e = document.getElementsByName(eq)[0].value;
    		if(e != val) {
    			alert(msg);
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
    					return false;
    				}
    			}
    			if(typeof l[1] != 'undefined'){
    				if(l[1]<val.length){
    					alert('최대 ' + l[1]+ ' 자로 입력하세요.');
    					return false;
    				}
    			}
    		}
		}
    	
    	switch (el.type){
	        case 'num' :
	        	regexp = /^[0-9]*$/;
	    		if(!regexp.test(val) )	alert('숫자만 입력하세요');
	    		else return true;
	    		return false;
	            break;
	        case 'email' :
	        	regexp=/^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{1,5}$/;
	    		if(!regexp.test(val) )	alert('이메일 형식으로 입력하세요');
	    		else return true;
	    		return false;
	            break;
	        case 'pw' :
	        	regexp=/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;
	    		if(!regexp.test(val) )	alert('비밀번호는 최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 입력하세요.');
	    		else return true;
	    		return false;
	            break;
	        case 'extensionNo' :
	        	regexp=/^\d{2,3}\d{3,4}\d{4}$/;
	        	if(!regexp.test(val) )	alert('회사 전화번호를 다시 입력해 주세요.');
	        	else return true;
	        	return false;
	        	break;
	        case 'text' :
	    		return true;
	            break;
	        default :
	        	alert('유효성검사 TYPE을 확인 하세요');
	        	return false;
    	}
    	
    }
};
