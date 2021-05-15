function makeFrame(url,target) 
{ 
    ifrm = document.createElement( "IFRAME" ); 
    ifrm.setAttribute( "style", "display:none;" ) ;
    ifrm.setAttribute( "src", url ) ; 
    ifrm.setAttribute( "name", target) ; 
    ifrm.style.width = 0+"px"; 
    ifrm.style.height = 0+"px"; 
    document.body.appendChild( ifrm ) ; 
} 

function removeiframe() {
	var iframes = document.getElementsByTagName('iframe');
	for (var i = 0; i < iframes.length; i++) {
	    iframes[i].parentNode.removeChild(iframes[i]);
	}
}

function saveToDisk(fileURL, fileName) {
	// for non-IE
	if (!(window.ActiveXObject || "ActiveXObject" in window)) {
		var save = document.createElement('a');
		save.href = fileURL;
		save.target = '_blank';
		save.download = fileName || fileURL;
		var evt = document.createEvent('MouseEvents');
		evt.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0,
		false, false, false, false, 0, null);
		save.dispatchEvent(evt);
		(window.URL || window.webkitURL).revokeObjectURL(save.href);
	}else if ( window.ActiveXObject || "ActiveXObject" in window ){ // for IE
		makeFrame(fileURL,fileName);
		alert('"' + fileName +'" 파일을 다운로드 합니다.');
		var _window = window.open(fileURL, fileName);
		_window.document.close();
		_window.document.execCommand('SaveAs', true, fileName || fileURL)
		_window.close();
		removeiframe();
	}
}