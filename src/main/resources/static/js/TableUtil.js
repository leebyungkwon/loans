var to = "";
let GRID = {
	id: this.id,
	url: this.url,
	init: true,
	search: "",
	orgData: "",
	opts: "",
	gridData: "",
	rowClick: false,
	sortNm: "",
	sort: "DESC",
	searchSeq: [],
	searchWidth: [],
	searchCol: [],
	searchDefault: [],
	isPaging: true,
	pageCnt: 0,
	page: 0,
	size: 10,
	params: {},
	gridSearch: "",
	check: false,
	excel: "",
	bodyRefresh: false,
	initTable: true,
	getId: function() {
		return this.id;
	},
	validation: function(obj) {
		if (obj.id == undefined) {
			alert("그리드 ID 입력하세요.");
			return;
		}
		if (obj.url == undefined) {
			alert("URL 입력하세요.");
			return;
		}
	},
	set: function(obj) {
		let _this = this;
		this.validation(obj);
		this.id = obj.id;
		this.url = obj.url;
		this.rowClick = obj.rowClick;
		this.search = (obj.search == undefined) ? "" : obj.search;
		this.sort = (obj.sort == undefined) ? "DESC" : obj.sort;
		this.sortNm = (obj.sortNm == undefined) ? "" : obj.sortNm;
		this.isPaging = (obj.isPaging == undefined) ? true : obj.isPaging;
		this.page = (obj.page == undefined) ? 0 : obj.page;
		this.size = (obj.size == undefined) ? 10 : obj.size;
		this.searchBtn = (obj.gridSearch == undefined) ? "" : obj.gridSearch.split(",")[1];
		this.gridSearch = (obj.gridSearch == undefined) ? "" : obj.gridSearch.split(",")[0];
		this.check = (obj.check == undefined) ? false : obj.check;
		this.excel = (obj.excel == undefined) ? false : obj.excel;
		this.initTable = (obj.initTable == undefined) ? true : obj.initTable;
		
		// 2021-06-28 엑셀다운로드 파일명 설정
		this.excelFileNm = (obj.excelFileNm == undefined) ? "엑셀다운로드" : obj.excelFileNm;
		
		
		if(h_gridPage > 0)	this.page = h_gridPage;
		this.params = {
			'page': this.page
			, 'isPaging': this.isPaging
			, 'size': this.size
			, 'sort': this.sortNm + ',' + this.sort
			, 'excelFileNm' : this.excelFileNm
		};

		this.opts = obj;
		if (this.initTable) this.getData();
		else this.returnData("");



		if (this.gridSearch != "" ){
			if (this.gridSearch != "" && this.searchBtn != "") {
				document.getElementById(this.searchBtn).onclick = function() { _this.getData('init'); };
			}
			if($("#"+this.gridSearch).length > 0){
				$("#"+this.gridSearch).find("input").each(function(index){
					$(this).on("keyup",function(){
						if (window.event.keyCode == 13) 	$("#"+_this.searchBtn).trigger("click");
					});
				});
	        }
		}
		
		if (this.excel != "") {
			let button = document.createElement("a");
			let btnText = document.createTextNode("엑셀다운로드");
			button.appendChild(btnText);
			button.className = "btn btn_home";
			button.id = "excelDown";
			button.style.display = "none";
			document.getElementById(this.searchBtn).after(button);
			button.onclick = function() {
				let param = "";
				if (this.gridSearch != "") {
					var idCheck = $(this).closest(".k_search");
					param = Object.assign({}, _this.params, WebUtil.getTagInParam(document.getElementById(idCheck.attr("id"))));
				}
				let p = {
					url: _this.excel
					, param: param
				}
				AjaxUtil.excel(p);
			};
		}

	},
	getData: function(type) {
		let _this = this;

		let param = "";
		if (this.gridSearch != "") {
			if (type != "undefined" && type == "init") {
				this.params.page = 0;
			}
		}
		param = Object.assign({}, this.params, WebUtil.getTagInParam(document.getElementById(this.gridSearch)));
		let p = {
			url: this.url
			, param: param
			, success: function(opt, result) {
				_this.returnData(result.data);
			}
		}
		AjaxUtil.post(p);
	},
	returnData: function(data) {
		this.orgData = data;
		this.gridData = data;
		let page_id = this.id + "_paging";
		if (null != document.getElementById(page_id)) {
			//document.getElementById(page_id).remove();
			//2021-05-14 IE 이슈로 인한 변경
			$("#"+page_id).remove();
		}
		if (!this.bodyRefresh) this.setFrame();
		this.setTable();
		if (this.initTable || data.length > 0) {
			if (data.length > 0 && this.isPaging) this.setPaging(data[0].totalCnt);
		}
		this.init = false;
		this.initTable = true;
		
		// 2021-06-29 조회 총 갯수 추가
		$(".total_result p").remove();
		var totCntTag = "";
		if(data[0] != null){
			totCntTag = "<p>총 : "+data[0].totalCnt+"건</p>";
		}else{
			totCntTag = "<p>총 : 0건</p>";
		}
		$(".total_result").append(totCntTag);
		
	},
	setFrame: function() {
		let _this = this;
		let table = "";
		if (this.search != "") {
			if (this.search.searchUse == "Y") {
				for (i in this.opts.bodyCol) {
					if (this.opts.bodyCol[i].search != undefined) {
						let typ = this.opts.bodyCol[i].search["type"];
						if (typ != undefined) {
							this.searchCol.push(this.opts.bodyCol[i]);
							this.searchSeq.push(i);
							this.searchDefault.push(this.opts.bodyCol[i]["search"]["default"]);
							this.searchWidth.push(this.opts.bodyCol[i]["search"]["width"]);
						}
					}
				}
				table += "<table class='searchTable' id='tbl_" + this.id + "_search' style='width:" + this.opts.width + "'>" + "<tbody></tbody></table>";
			}
		}

		table += "<table class='gridTableHead' id='tbl_" + this.id + "_head' style='width:" + this.opts.width + "'>"
			+ "<thead></thead></table>";


		table += "<table class='gridTableBody' id='tbl_" + this.id + "_body' style='width:" + this.opts.width + "'>"
			+ "<tbody></tbody></table>";
		document.getElementById(this.id).innerHTML = table;
	},
	setTable: function() {
		let hiddenCol = this.columnSetHiddenCol(this.opts.bodyCol);

		if (!this.bodyRefresh) {
			this.setHead("tbl_" + this.id + "_head", this.opts.headCol, hiddenCol, this.opts.bodyCol);
			this.bodyRefresh = true;
		}

		this.setBody("tbl_" + this.id + "_body", this.opts.bodyCol, hiddenCol);

		if (this.init && this.search != "") {
			if (this.search.searchUse == "Y") {
				document.getElementById(this.id + "_search").onclick = function() { _this.data(); };
			}
		}

	},
	setData: function() {
		this.gridData = [];
		let searchName = [];
		let searchWay = [];
		if (this.search != "") {
			if (this.search.searchUse == "Y") {
				for (i in this.searchCol) {
					searchName.push(this.searchCol[i]["name"]);
					searchWay.push(this.searchCol[i]["search"]["way"]);
				}
			}
		}
		let data = this.orgData;

		let emp = true;
		searchName.forEach(function(v, i) {
			let val = document.getElementById("tbl_" + _this.id + "_search_" + v).value;
			if (val != "전체") {
				emp = false;
				return;
			}
		});
		if (emp) {
			this.gridData = data;
		} else {
			this.gridData = data.filter(function(item) {
				let d = true;
				searchName.forEach(function(v, i) {
					if (!d) return;
					if (document.getElementById("tbl_" + _this.id + "_search_" + v).value != "전체") {
						let val = document.getElementById("tbl_" + _this.id + "_search_" + v).value;
						d = (item[v] === document.getElementById("tbl_" + _this.id + "_search_" + v).value);
					}
				});
				return d;
			});
		}

		/*
		for(i in data) {
			let inFalg = false;
			let searchValYn = false;
			let tempSearch = [];

			for(j in searchName){
				let search_val = document.getElementById("tbl_"+this.id+"_search_"+searchName[j]).value;
				if(search_val != "" && search_val != "전체"){
					tempSearch.push(this.searchCol[j]["name"]);
					searchValYn = true;
				}else{
					searchValYn = false;
				}
			}
			if(tempSearch.length > 0){
				let tempFlag = true;

				for(j in tempSearch){
					let search_val = document.getElementById("tbl_"+this.id+"_search_"+tempSearch[j]).value;

					 *  AND 로 검색


					if(tempFlag){
						if(search_val != "" && search_val != "전체"){
							if(data[i][tempSearch[j]] == search_val){
								console.log(tempSearch[j] +" = " + data[i][tempSearch[j]] + " , " + search_val);
								tempFlag = true;
							}else{
								tempFlag = false;
							}
						}
					}
					if(tempSearch.length-1 == j && tempFlag) inFalg = true;

					 *
					 *  OR 로 검색
					 *
					if(!inFalg){
						if(search_val != "" && search_val != "전체"){
							if(data[i][tempSearch[j]] == search_val){
								console.log("[ " + i + " ] 검색 값 :: " + tempSearch[j] + " =  [" + search_val + "] = [" + data[i][tempSearch[j]]+"]");
								inFalg = true;
							}else{
								inFalg = false;
								//continue;
							}
						}
					}

				}

			}else{
				inFalg = true;
			}

			if(inFalg){
				this.gridData.push(data[i]);
			}
		}
		*/
	},
	setPaging: function(totalCnt) {
		// 2021-05-27 조회 총 갯수 추가
		var totCntTag = "<p>총 : "+totalCnt+"건</p>";
		$(".total_result p").remove();
		$(".total_result").append(totCntTag);
		
		let _this = this;
		let page_id = this.id + "_paging";
		let current = parseInt(_this.params.page) + 1;
		this.pageCnt = Math.ceil(totalCnt / this.size);
		let tag = document.createElement("div");
		tag.className = "custom-pagination";
		tag.id = page_id;
		let s_tag = "";
		let start = (parseInt((current - 1) / this.size) * this.size) + 1;
		let end = Math.floor((start+this.size) / 10) * 10;
		end = (this.pageCnt>end) ? end : this.pageCnt; 
		if (this.pageCnt > 1) {
			if (1 < current){
				s_tag += '<span  class="pagination-first"><a class=""><</a></span>';
				s_tag += '<span  class="pagination-prev"><a class=""><</a></span>';
			} 
			for (let i = start; i <= end; i++) {
				s_tag += '<span class="num num' + i + '"><a class="page_num">' + i + '</a></span>';
			}
			if (this.pageCnt > current) {
				s_tag += '<span  class="pagination-next"><a class="">></a></span>';
				s_tag += '<span  class="pagination-last"><a class="">></a></span>';
			}
		} else {
			s_tag += '<span class="num num1"><a class="page_num">1</a></span>';
		}
		tag.innerHTML = s_tag;
		document.getElementById(this.id).appendChild(tag);


		let page = document.getElementById(page_id).querySelectorAll(".page_num");
		let first = document.getElementById(page_id).querySelector(".pagination-first");
		let prev = document.getElementById(page_id).querySelector(".pagination-prev");
		let next = document.getElementById(page_id).querySelector(".pagination-next");
		let last = document.getElementById(page_id).querySelector(".pagination-last");
		if (null != first) {
			first.addEventListener('click', function(event) {
				for (let __page = 0; __page < page; __page++) {
					__page.parentElement.classList.remove("current");
				}
				_this.params.page = 0;
				_this.getData();
			});
		}
		if (null != prev) {
			prev.addEventListener('click', function(event) {
				for (let __page = 0; __page < page; __page++) {
					__page.parentElement.classList.remove("current");
				}
				_this.params.page = _this.params.page - 1;
				_this.getData();
			});
		}
		if (null != next) {
			next.addEventListener('click', function(event) {
				for (let __page = 0; __page < page; __page++) {
					__page.parentElement.classList.remove("current");
				}
				_this.params.page = _this.params.page + 1;
				_this.getData();
			});
		}
		if (null != last) {
			last.addEventListener('click', function(event) {
				for (let __page = 0; __page < page; __page++) {
					__page.parentElement.classList.remove("current");
				}
				_this.params.page = _this.pageCnt-1;
				_this.getData();
			});
		}

		let idx = 1;

		for (let _page = 0; _page < page.length; _page++) {
			page[_page].addEventListener('click', function(event) {
				for (let __page = 0; __page < page.length; __page++) {
					page[__page].parentElement.classList.remove("current");
				}
				_this.params.page = this.text - 1;
				_this.getData();
			});
		}
		if (document.getElementById(page_id).querySelector(".num" + (parseInt(_this.params.page) + 1)) != null)
			document.getElementById(page_id).querySelector(".num" + (parseInt(_this.params.page) + 1)).className = "num current num" + _this.params.page + 1;
	},
	setSearch: function(id, headCol, data) {
		let tag = "<tr>";
		let colspan = 0;
		for (i in headCol) {
			for (j in this.searchSeq) {
				if (i == j) {
					colspan++;
					tag += "<td class='searchTableTop'>";
					tag += "<label>" + headCol[this.searchSeq[j]] + "</label><span>";

					let selecteName = (this.searchDefault[j] != undefined) ? this.searchDefault[j] : "";
					if (this.searchCol[j].search["type"] == "text") {
						tag += "<input type='text' name='" + id + "_" + this.searchCol[j].name + "' id='" + id + "_" + this.searchCol[j].name + "' style='margin-left:10px;width:80px;'/>";
					}
					else if (this.searchCol[j].search["type"] == "select") {
						tag += "<select name='" + id + "_" + this.searchCol[j].name + "' id='" + id + "_" + this.searchCol[j].name + "' style='margin-left:10px;width:" + this.searchWidth[j] + ";'>";
						let searchName = this.searchCol[j].name;
						let _name = [];
						let prev_name = "";
						tag += "<option>전체</option>";
						for (k in data) {
							if (prev_name != data[k][searchName]) {
								prev_name = data[k][searchName];
								let _name_flag = true;
								for (m in _name) {
									if (_name[m] == prev_name) {
										_name_flag = false;
										continue;
									}
								}
								if (_name_flag) _name.push(prev_name);
							}
						}
						for (k in _name) {
							let selected = (selecteName == _name[k]) ? "selected" : "";
							tag += "<option value='" + _name[k] + "' " + selected + ">" + _name[k] + "</option>";
						}
						tag += "</select>";
					}

					tag += "</span></td>";
				}
			}
		}

		tag += "</tr>";
		tag += "<tr class='searchTableSearch'><td id='" + this.id + "_search' colspan='" + colspan + "'>검  색</td></tr>"

		document.getElementById(id).getElementsByTagName("tbody")[0].innerHTML = tag;

	},
	setHead: function(id, headCol, hiddenCol, bodyCol) {
		let _this = this;
		let tag = "<tr>";
		let hiddenStr = "display:none;";
		if (this.check) tag += "<th style='text-align:center;width:10px;'><input type='checkbox'/></th>";
		for (i in headCol) {
			tag += "<th style='text-align:center; width:" + bodyCol[i].width + "; ";
			for (j in hiddenCol) if (i == hiddenCol[j]) tag += hiddenStr;
			tag += "'";
			tag += " data-col=" + WebUtil.trim(WebUtil.camel2Snake(this.opts.bodyCol[i].name, "")) + "";
			tag += " data-sort='DESC'";
			tag += ">";
			tag += headCol[i] + "</th>";
		}
		tag += "</tr>";
		let thead = document.getElementById(id).getElementsByTagName("thead")[0];
		thead.innerHTML = tag;
		if (this.opts.sortTable) {
			for (i in headCol) {
				if (this.check && i == 0) continue;
				thead.querySelectorAll("th")[i].onclick = function() {
					if (WebUtil.isNull(this.dataset.sort)) this.dataset.sort = 'ASC';
					else if (!WebUtil.isNull(this.dataset.sort) && this.dataset.sort == 'ASC') this.dataset.sort = 'DESC';
					else if (!WebUtil.isNull(this.dataset.sort) && this.dataset.sort == 'DESC') this.dataset.sort = 'ASC';
					_this.bodyRefresh = true;
					_this.params.sort = this.dataset.col + "," + this.dataset.sort;
					_this.params.page = 0;
					_this.getData();
				}

			}
		}

		if (this.check) {
			thead.getElementsByTagName("input")[0].onclick = function() {
				let headCheck = this.checked;
				if (this.type == 'checkbox') {
					let chkBody = document.getElementById("tbl_" + _this.id + "_body").getElementsByTagName("tbody")[0].getElementsByTagName("input");
					[].forEach.call(chkBody, function(el, idx) {
						if (headCheck) el.checked = true;
						else el.checked = false;
					});
				}
			};
		}
	},
	setBody: function(id, bodyCol, hiddenCol) {
		let _this = this;
		//this.setData();

		let tag = "";
		let hiddenStr = "display:none;";

		if (this.gridData == 0) {
			tag += "<tr><td colspan='" + bodyCol.length + "' style='text-align:center;font-weight:bold;'>데이터가 없습니다.</td></tr>";
			document.getElementById(id).getElementsByTagName("tbody")[0].innerHTML = tag;
		} else {
			for (i in this.gridData) {
				tag += "<tr>";
				if (this.check) tag += "<td class='noClick' style='text-align:center;width:10px;'><input type='checkbox'/></td>";
				for (j in bodyCol) {
					let typ = bodyCol[j].type;
					let name = bodyCol[j].name;
					let dataStr = (this.gridData[i][name] == undefined) ? "" : this.gridData[i][name];

					if (typeof dataStr == 'string') dataStr = dataStr.unescapeHtml();
					let classNm = "tbodyTd";
					if (bodyCol[j].button != undefined) classNm = "noClick";
					tag += "<td class='" + classNm + "' style='";

					for (k in hiddenCol) if (hiddenCol[k] == j) tag += hiddenStr;

					let align = (bodyCol[j].align == undefined) ? "center" : bodyCol[j].align;
					tag += "text-align:" + align + ";" + "width:" + bodyCol[j].width + ";'>";
					/*
					// 2021-05-17 null데이터 처리
					if(WebUtil.isNull(dataStr)){
						dataStr = '-';
					}
					*/
					tag += (typ == "text") ? "<input type='text' id='" + name + "' value='" + dataStr + "'/>" : dataStr;
					if (bodyCol[j].button != undefined) {
						for (key in bodyCol[j].button) {
							tag += "<button onclick=" + bodyCol[j].button[key] + "(" + i + ");>" + key + "</button>";
						}

					}
					tag += "</td>";

				}
				tag += "</tr>";
			}
			document.getElementById(id).getElementsByTagName("tbody")[0].innerHTML = tag;
			if (_this.rowClick) {
				let tbodyTr = document.getElementById(id).getElementsByTagName("tbody")[0].getElementsByTagName("tr");
				//let tbodyTr = document.getElementById(id).getElementsByTagName("tbody")[0].querySelectorAll('td:not([class*="tbodyCheck"])');
				[].forEach.call(tbodyTr, function(el, idx) {
					let _el = el.querySelectorAll('td:not([class*="noClick"])');
					el.style.cursor = "pointer";
					[].forEach.call(_el, function(subEl, index) {
						subEl.onclick = function() {
							_this.clickRemove();
							subEl.parentElement.style.background = _this.rowClick.color;
							subEl.parentElement.className = _this.id + "_selected";
							subEl.parentElement.dataset.row = idx;
							sessionStorage.setItem('P_'+window.location.pathname, JSON.stringify(WebUtil.getTagInParam($(".k_search"))));
							sessionStorage.setItem('H_'+window.location.pathname, $(".k_search").html());
							sessionStorage.setItem('PAGE_'+window.location.pathname, _this.params.page);
							if (_this.rowClick.retFunc != undefined) _this.rowClick.retFunc(idx, _this.gridData[idx]);
						}
					});

				});
			}
		}

	},
	columnSetHiddenCol: function(bodyCol) {
		let hiddenCol = [];
		for (i in bodyCol) {
			let hidden = bodyCol[i].hidden;
			if (hidden) hiddenCol.push(i);
		}
		return hiddenCol;
	},
	clearBody: function() {
		document.getElementById("tbl_" + this.id + "_body").getElementsByTagName("tbody")[0].innerHTML = "";
	},
	refresh: function() {
		this.getData();
	},
	clickRemove: function() {
		let _tr = document.getElementById(this.id).getElementsByTagName("tbody")[0].getElementsByTagName("tr");
		for (let i = 0; i < _tr.length; i++) {
			_tr[i].className = "";
			if (typeof _tr[i].style.background != undefined) _tr[i].style.background = "";
		}
	},
	getChkData: function() {
		let _this = this;
		let chkBody = document.getElementById("tbl_" + this.id + "_body").getElementsByTagName("tbody")[0].getElementsByTagName("input");
		let i = 0;
		let data = {};
		[].forEach.call(chkBody, function(el, idx) {
			if (el.checked) data[i++] = _this.gridData[idx];
		});
		return data;
	},
	getRowData: function() {
		let row = (document.getElementById("tbl_" + this.id + "_body").querySelector("." + this.id + "_selected") == null) ? "" : document.getElementById("tbl_" + this.id + "_body").querySelector("." + this.id + "_selected").dataset.row;
		return (row == "") ? "" : this.gridData[row];
	}
};
/*
let GRID = function () {
	let url = nu1ll;
	this.grid = function (id){
		console.log(id);
	}
	this.data = function (datas) {
		 console.log('GGGG');
	};
};
*/
//$.extend( GRID);
