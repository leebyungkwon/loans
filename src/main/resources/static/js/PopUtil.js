var PopUtil = {
	openPopup: function(opts){
		var p = { url : opts.url};
		//p = WebUtil.getParamJson(opts.params , p);
		var params = {
			url : opts.url
			, param : ""
			, success : function(opt,data){
		    	const Element = document.querySelector('.layerPopupBase');
				let cloneEl = Element.cloneNode(true);
				cloneEl.className = 'layerPopup';
				cloneEl.id = opts.id;
				cloneEl.style.display = 'block';
				cloneEl.querySelector('.layerContent').innerHTML = data;
				//document.querySelector('body').appendChild(cloneEl);
				$('body').append(cloneEl);
				
				cloneEl.querySelector(".closeLayerPopup").onclick = function(){
					this.parentElement.remove();
				};
	            if (typeof opts.success === "function") {
	                opts.success();
	            }
	        }
		};
		AjaxUtil.post(params);
    }
    , closePopup: function(){
    	alert(this.id);
    }
    ,openWinPopup: function(opts){
   		var win = window.open(opts.popupUrl, "PopupWin", "width=500,height=600");
    }
};
