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
					html.push("<div class=pager>");
					html.push(pager(total, 1, 10));
					html.push("</div>");
				}
				el.append(html.join(''));
				
				
				
				$('.grid-row').live('mouseenter', function(){
					$(this).addClass('strips');
				}).live('mouseleave', function(){
					$(this).removeClass('strips');
				});
				
				$('tbody tr:odd', el).addClass('strips');
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
					$('.pager',el).empty().append(pager(dd.total, p, pagesize));
					$('tbody tr:odd', el).addClass('strips');
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
				$('.pager',this).empty().append(pager(dd.total, 1, pagesize));
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
				$('.pager',this).empty().append(pager(dd.total, 1, pagesize));
				$('tbody tr:odd', this).addClass('strips');
        	});
        },
        getSelected: function(){
        	var ret = [];
        	var rows = null;
        	var grid = $(this);
        	this.each(function(){
        		rows = $('tbody :checkbox:checked', this).parent().parent();
        	});
        	rows.each(function() {   
        		var rowIndex = this.rowIndex - 1;   
        		ret.push(grid.data('ds').data[rowIndex]);   
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
	        	ret = result.data;
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
	
	
	function body(records, opts){
		var code = new Array();
		$(records).each(function(){
			var r = this;
			code.push(buildRow(r, opts));
		});
		return code.join('');
	}
	function buildRow(record, opts){
		var code = new Array();
		var span = $('<span>');
		code.push("<tr>");
		if(opts.selector&&(opts.selector == 'm')){
			code.push("<td align=center width=30><input type=checkbox hideFocus></td>");
		}
		else if(opts.selector&&(opts.selector == 's')){
			code.push("<td align=center width=30><input type=radio hideFocus name=ids></td>");
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
			code.push("<span class='content'>");
			if(render){
				code.push(render(v, record));
			}else{					
				code.push(span.text(v).html());
			}
			code.push("</span>");
			if(editor){
				code.push("<div class='editorDiv' style='display:none;'></div>");
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
			code.push("<th");
			if(w){
				code.push(" width=" + w);
			}
			code.push(">" + h + "</th>");
			
		});
		code.push("</tr></thead>");
		return code.join('');
	}
})(jQuery);