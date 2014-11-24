(function($){        
	$.fn.table = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.table.methods = {
		init: function(options) {
			var defaults = {
				columns:[],
				pager:true
			};
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				var el = $(this).addClass('grid');
				el.data('pagesize', 10);
				var data = new Array();
				if(settings.url){
					el.data('url', settings.url);
					var baseparams = {page: 1, pagesize: 10};
					if(settings.condition){
						if($.isFunction(settings.condition)){
							baseparams = $.extend(baseparams, settings.condition());
						}
					}
					data = ds(settings.url, baseparams);
				}else{
					data = settings.data;
				}
				if(!data.data){
					data.data = [];
				}
				var rows = data.data.slice(0); 
				$(this).data('options', settings);
				el.data('rows', rows);
				el.data('ds', data);
				el.data('total', data.total);
				var html = new Array();
				html.push("<table class='table'>");
				html.push(header(settings));
				html.push("<tbody>");
				html.push(body(data.data, settings));
				html.push("</tbody>");
				html.push("</table>");
				if(settings.pager){
					var total = parseInt(el.data('total'));
					html.push("<div class='pagination'>");
					html.push(pager2(total, 1, 10));
					html.push("</div>");
				}
				el.append(html.join(''));
				
				
				
				$('.grid-row').live('mouseenter', function(){
					$(this).addClass('strips');
				}).live('mouseleave', function(){
					$(this).removeClass('strips');
				});
				
				//$('tbody tr:odd', el).addClass('strips');
				function getEditor(field){
		        	var editor = null;
					$(settings.columns).each(function(){
		        		var f = this.field;
		        		if(field == f){
		        			editor = this.editor;
		        			return false;
		        		}
		        	});
		        	return editor;
				}
				function getRender(field){
					var render = null;
					$(settings.columns).each(function(){
						var f = this.field;
						if(field == f){
							render = this.render;
							return false;
						}
					});
					return render;
				}
				function getFieldValue(rowindex, field){
					var v = null;
					var rows = settings.rows;
					$(rows).each(function(){
						var idx = this.idx;
						if(rowindex == idx){
							v = this.record[field];
							return false;
						}
					});
		        	return v;
				}

				$(':checkbox[name=checkall]', el).click(function(){
					$('tbody tr :checkbox', el).attr("checked",this.checked);
				});
				
				$('.pageNum', el).live('click',function(e){
					$(':checkbox[name=checkall]', el).attr("checked",false);
					var p = parseInt($(this).attr('p'));
					var pagesize = parseInt(el.data('pagesize'));
					var url = el.data('url');
					
					var baseparams = {page: p, pagesize: pagesize};
					if(settings.condition){
						if($.isFunction(settings.condition)){
							baseparams = $.extend(baseparams, settings.condition());
						}
					}
					var dd = ds(url, baseparams);
					el.data('ds', dd);
					var opts = el.data('options');
					$('tbody',el).empty().append(body(dd.data, opts));
					$('.pager',el).empty().append(pager2(dd.total, p, pagesize));
					$('tbody tr:odd', el).addClass('strips');
				});
				$('div.pagination a', el).live('click',function(e){
					var p = 1;
					$(':checkbox[name=checkall]', el).attr("checked",false);
					var curpage = parseInt($('div.pagination a.active', el).text());
					if($(this).hasClass('prev')){
						p = curpage - 1;
					}else if($(this).hasClass('next')){
						p = curpage + 1;
					}else{
						p = parseInt($(this).text());
					}
					var pagesize = parseInt(el.data('pagesize'));
					var url = el.data('url');
					
					var baseparams = {page: p, pagesize: pagesize};
					if(settings.condition){
						if($.isFunction(settings.condition)){
							baseparams = $.extend(baseparams, settings.condition());
						}
					}
					var dd = ds(url, baseparams);
					el.data('ds', dd);
					var opts = el.data('options');
					$('tbody',el).empty().append(body(dd.data, opts));
					$('div.pagination',el).empty().append(pager2(dd.total, p, pagesize));
					$('tbody tr:odd', el).addClass('strips');
				});
				var len = settings.columns.length;
				$(settings.columns).each(function(index){
					var idx = index;
					if(settings.selector){
						idx += 1;
						len += 1;
					}
					if(this.editor){					
						var selector = "tbody td:nth-child(" + len  + 'n + ' + (idx + 1) + ")";
						var cols = $(selector, el);
						cols.click(function() {
							var cell = $(this);
							var text = cell.html();
							cell.html("");
							var editor = $("<input type='text' class='txt'/>").width(cell.width() - 14).val(text).appendTo(cell);
							// 是文本框插入之后被选中
							editor.get(0).select();
							editor.click(function() {
								return false;
							});
							editor.blur(function() {
								var v = $(this).val();
								cell.html(v);
							});
						});
					}
				});
			});
        },
        reload: function(params){
        	return this.each(function(){
        		$(':checkbox[name=checkall]', this).attr("checked",false);
        		var opts = $(this).data('options');
        		
        		var url = opts.url;
				var pagesize = parseInt($(this).data('pagesize'));
				var p = {page:1, pagesize:pagesize};
				if(params){
					p = $.extend(p, params);
				}else{
					if(opts.condition){
						if($.isFunction(opts.condition)){
							p = $.extend(p, opts.condition());
						}
					}
				}
				var dd = ds(url, p);
				$(this).data('ds', dd);
				$('tbody',this).empty().append(body(dd.data, opts));
				$('div.pagination',this).empty().append(pager2(dd.total, 1, pagesize));
				$('tbody tr:odd', this).addClass('strips');
        	});
        },
        refresh: function(params){
        	return this.each(function(){
        		$(':checkbox[name=checkall]', this).attr("checked",false);
        		var opts = $(this).data('options');
        		var url = opts.url;
        		var page = parseInt($(this).data('page'));
        		var pagesize = parseInt($(this).data('pagesize'));
        		var p = {page:1, pagesize:pagesize};
        		if(params){
        			p = $.extend(p, params);
        		}else{
        			if(opts.condition){
        				if($.isFunction(opts.condition)){
        					p = $.extend(p, opts.condition());
        				}
        			}
        		}

 				var dd = ds(url, p);
				$(this).data('ds', dd);
				$('tbody',this).empty().append(body(dd.data, opts));
				$('div.pagination',this).empty().append(pager2(dd.total, 1, pagesize));
				$('tbody tr:odd', this).addClass('strips');
        	});
        },
        getSelected: function(){
        	var ret = [];
        	var checked = $('tbody :checkbox:checked', this);
        	var grid = this;
        	checked.each(function() {   
        		var idx = $(this).val();   
        		ret.push(grid.data('ds').data[idx]);   
        	});  
        	return ret;
        },
        addRow: function(record, index){
        	index = index ? index : 0;
        	record['_s'] = 'added';
        	var opts = $(this).data('options');
        	var rows = $(this).data('rows');
			var code = new Array();
			code.push(buildRow(record, opts));
			$('table.dataview tbody',this).prepend(code.join(''));
			rows.splice(index, 0, record); 
        },
        editRow: function(index, field, value){
        	var rows = $(this).data('rows');
        	var record = rows[index];
        	var _s = record['_s'];
        	if(!_s){
        		record['_s'] = 'modified';
        	}
        	record[field] = value;
        },
        save: function(data){
        	var grid = $(this);
        	var opts = grid.data('options');
        	var addUrl = opts.addUrl;
        	if(addUrl){
	        	var arr = grid.Grid('getChanged').reverse();
	        	var added = [];
	        	for(var k = 0, len = arr.length; k < len; k++){
	        		if(arr[k]['_s']=='added'){
						added.push(arr[k]);
	        		}
	        	}
	        	var data = JSON.stringify(added);
	        	$.ajax({
					url: addUrl,
					type: 'post',
					data: {data:data},
					dataType: 'json',
					success: function(result){
						grid.Grid('refresh');
					}
	        	});
        	}else{
        		$.alert({msg:'请设置addUrl'});
        	}
        },
        getFieldValue: function(rowindex, field){
        	var rows = $(this).data('options').rows;
        	return rows[rowindex][field];
        },
		getEditor: function(field){
        	var editor = null;
        	var opts = $(this).data('options');
			$(opts.columns).each(function(){
        		var f = this.field;
        		if(field == f){
        			editor = this.editor;
        			return false;
        		}
        	});
        	return editor;
		},
		getRender: function(field){
			var render = null;
			var opts = $(this).data('options');
			$(opts.columns).each(function(){
				var f = this.field;
				if(field == f){
					render = this.render;
					return false;
				}
			});
			return render;
		},
        getChanged: function(){
        	var ret = new Array();
        	var rows = $(this).data('rows');
        	var editingrows = $('tr.editing', this);
        	editingrows.each(function(){
        		var idx = this.rowIndex;
        		var tds = $(this).find('td[field]');
        		var obj = {};
        		tds.each(function(){
        			var f = $(this).attr('field');
        			var editor = $(this).find('.editor');
        			var v = editor.val();
        			if(editor.hasClass('combox')){
        				v = editor.AutoComplete('val');
        			}
        			obj[f] = v;
        		});
        		var mk = $.extend({}, rows[idx - 1], obj);
        		ret.push(mk);
        	});
        	$(this).data('rows').length = 0;
        	return ret;
        }
	}; 
	function ds(url, param){
		var ret = {total:0, data:[]};
		$.ajax({
			url:url,
			type: 'post',
			cache: false,
			async: false,
			data: param,
			dataType: 'json',
	        success: function(result){
	        	ret = result;
			}
		});
		return ret;
	}
	function pager(total, page, pagesize){
		var code = new Array();
		if(total>0){
			
				var pn = Math.ceil(total/pagesize);
				if(page > 1){
					code.push("<span class='pageNum' p='");
					code.push(page - 1);
					code.push("'>上一页</span>");
				}else{
					code.push("<span class='disableNum'>上一页</span>");
				}
				if(pn<=10){
					for (var i=1; i <= pn; i++) {
				
						if(i == page){
							code.push("<span class='cPageNum'>");
							code.push(i);
							code.push("</span>");
						}else{
							code.push("<span class='pageNum' p='");
							code.push(i);
							code.push("'>");
							code.push(i);
							code.push("</span>");
						}
					}
				
				}else{
					if(page > 4){
						code.push("<span class='pageNum' p='1'>1</span>");
						code.push(" ... ");
						for (var i= page - 3; i < page; i++) {
							code.push("<span class='pageNum' p='");
							code.push(i);
							code.push("'>");
							code.push(i);
							code.push("</span>");
						}
						code.push("<span class='cPageNum'>" + page +"</span>");
					}else{
						for (var i=1; i <= page; i++) {
							if(i == page){
								code.push("<span class='cPageNum'>");
								code.push(i);
								code.push("</span>");
							}else{
								code.push("<span class='pageNum' p='");
								code.push(i);
								code.push("'>");
								code.push(i);
								code.push("</span>");
							}
						}
					}
					if(page + 3 >= pn){
						for (var i = page +1; i <= pn; i++) {
							code.push("<span class='pageNum' p='");
							code.push(i);
							code.push("'>");
							code.push(i);
							code.push("</span>");
						}
					}else{
						for (var i = page +1; i <= page + 3; i++) {
							code.push("<span class='pageNum' p='");
							code.push(i);
							code.push("'>");
							code.push(i);
							code.push("</span>");
						}
						code.push(" ... ");
						code.push("<span class='pageNum' p='");
						code.push(pn);
						code.push("'>");
						code.push(pn);
						code.push("</span>");
					}
				}
				if(page < pn){
					code.push("<span class='pageNum' p='");
					code.push(page + 1);
					code.push("'>下一页</span>");
				}else{
					code.push("<span class='disableNum'>下一页</span>");
				}
		}
	
		return code.join('');
	}
	
	function pager2(total, page, pagesize){
		var pc = Math.ceil(total/pagesize);
		var code = new Array();
		if(page == 1){
			code.push("<span>Prev</span>");
		}else{
			code.push("<a class='prev' href='javascript:;'>Prev</a>");		
		}
		if(pc <= 7){
			for (var i = 1; i <= pc; i++) {
				code.push("<a");
				if(i == page){
					code.push(" class='active'");
				}
				code.push(" href='javascript:;'>");
				code.push(i);
				code.push("</a>");
			}
		}else{			
			if(page <= 4){
				for (var i = 1; i <= 5; i++) {
					code.push("<a");
					if(i == page){
						code.push(" class='active'");
					}
					code.push(" href='javascript:;'>");
					code.push(i);
					code.push("</a>");
				}
				code.push("<span>...</span>");
				code.push("<a href='javascript:;'>" + pc + "</a>");
			}else{
				code.push("<a href='javascript:;'>1</a>");
				code.push("<span>...</span>");
				var begin = page -2;
				var end = page + 2;
				if(end +2 > pc){
					begin = pc -4;
					end = pc;
				}
				for (var i = begin; i <= end; i++) {
					code.push("<a");
					if(i == page){
						code.push(" class='active'");
					}
					code.push(" href='javascript:;'>");
					code.push(i);
					code.push("</a>");
				}
				if(page + 3 < pc){
					code.push("<span>...</span>");
				}
				if(end < pc){
					code.push("<a href='javascript:;'>" + pc + "</a>");
				}
			}
		}
		if(page == pc){
			code.push("<span>Next</span>");
		}else{
			code.push("<a class='next' href='javascript:;'>Next</a>");		
		}
		code.push("<span>共" + total + "条</span>");
		return code.join('');
	}
	
	
	function body(records, opts){
		var code = new Array();
		$(records).each(function(index){
			code.push(buildRow(this, index, opts));
		});
		return code.join('');
	}
	function buildRow(record, index, opts){
		var code = new Array();
		var span = $('<span>');
		code.push("<tr");
		if((index + 1) % 2 == 0){
			code.push(" class='strips'");
		}
		code.push(">");
		if(opts.selector&&(opts.selector == 'm')){
			code.push("<td align=center width=30><input type=checkbox hideFocus value=" + index + "></td>");
		}
		else if(opts.selector&&(opts.selector == 's')){
			code.push("<td align=center width=30><input type=radio hideFocus name=ids value=" + index + "></td>");
		}
		$(opts.columns).each(function(){
			var f = this.field;
			var align = this.align;
			var tip = this.tip;
			var render = this.render;
			var editor = this.editor;
			var v = record[f]==undefined?'':record[f];
			code.push("<td");
			if(f){
				code.push(" field='", f, "'");
			}
			if(tip){
				code.push(" title=" + span.text(v).html() + "");
			}
			if(align){
				code.push(" align='" + align + "'");
			}
			if(editor){
				code.push(" editable='true'");
			}
			code.push(">");
			//code.push("<span class='content'>");
			if(render){
				code.push(render(v, record));
			}else{					
				code.push(span.text(v).html());
			}
			//code.push("</span>");
			if(editor){
				//code.push("<div class='editorDiv' style='display:none;'></div>");
			}
			code.push("</td>");
		});
		code.push("</tr>");
		span.remove();
		return code.join('');
	}
	function header(opts){
		var code = new Array();
		
		code.push("<thead><tr>");
		if(opts.selector&&(opts.selector == 'm')){
			code.push("<th align=center width=30><input type=checkbox name='checkall' hideFocus></th>");
		}
		else if(opts.selector&&(opts.selector == 's')){
			code.push("<th align=center width=30></th>");
		}
		$(opts.columns).each(function(){
			var h = this.header;
			var w = this.width;
			var align = this.align;
			code.push("<th");
			if(w){
				code.push(" width=" + w);
			}
			if(align){
				code.push(" align='" + align + "'");
			}
			code.push(">" + h + "</th>");
			
		});
		code.push("</tr></thead>");
		return code.join('');
	}
})(jQuery);