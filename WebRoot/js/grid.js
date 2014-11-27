(function($){        
	$.fn.Grid = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.Grid.methods = {
		init: function(options) {
			var defaults = {
				title:'Grid',
				columns:[],
				pager:true
			};
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				var el = $(this).addClass('grid');
				el.data('page', 1);
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
				var rows = data.data.slice(0); 
				$(this).data('options', settings);
				el.data('rows', rows);
				el.data('ds', data);
				el.data('total', data.total);
				var html = new Array();
				html.push(header(settings));
				html.push("<tbody>");
				html.push(body(data.data, settings));
				html.push("</tbody>");
				html.push("</table>");
				if(settings.pager){
					var total = parseInt(el.data('total'));
					html.push("<div class='pagebar'>");
					html.push(pager(total, 1, 10));
					html.push("</div>");
				}
				el.append(html.join(''));
				
				if(settings.tbar){
					$(settings.tbar).each(function(){
						var text = this.text;
						var icon = this.icon;
						var handler = this.handler;
						$('div.toolbar', el).append("<div class=separator>&nbsp;</div>");
						var tool = $("<a class='linkbtn'><span class='" + icon + "'></span>" + text + "</a>");
						tool.click(handler);
						$('div.toolbar', el).append(tool);
					});
				}
				
				$('.grid-row').live('mouseenter', function(){
					$(this).addClass('strips');
				}).live('mouseleave', function(){
					$(this).removeClass('strips');
				});
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
				$('table.dataview tbody td[editable]').live('dblclick', function(){
					var me = $(this);
					var w = me.width();
					var row = me.parent('tr');
					row.addClass('editing');
					var rowIndex = row[0].rowIndex;
					var f = me.attr('field');
					var contentSpan = $(this).find('.content');
					var editorDiv = $(this).find('.editorDiv').show().empty();
					var content = contentSpan.text();
					//content = el.data('rows')[rowIndex][f];
					contentSpan.hide();
					var editor = getEditor(f);
					var render = getRender(f);
					if(editor){
						if(editor == 'text'){
							var el = $("<input class='editor txt' type='text'>").width(w - 4).val(content).appendTo(editorDiv);
							//el.focus();
							/**el.blur(function(){
								var v = $(this).val();
								contentSpan.text(v).show();
								editorDiv.data('v', v);
								editorDiv.hide();
								me.live('dblclick', dblclick);
								me.addClass('dirty');
							});**/
						}
						if(editor == 'combox'){
							var el = $("<input type='text' class='editor combox' style='width:80%'/>").val(content).appendTo(editorDiv);
							el.AutoComplete({data:[{text:'正常',value:1}, {text:'禁止',value:0}]});
							//el.focus();
							/**el.blur(function(){
								window.setTimeout(function(){
									var v = el.AutoComplete('val');
									editorDiv.data('v', v);
									if(render){
										contentSpan.html(render(v)).show();
									}
									editorDiv.hide();
									me.live('dblclick', dblclick);
									me.addClass('dirty');									
								},1000);  
							});**/
						}
						if(editor == 'datebox'){
							var el = $("<input class='editor datebox' type='text'>").width(w - 4).val(content).appendTo(editorDiv);
							el.Datepicker({format:'yyyy-MM-dd hh:mm:ss'});
							//el.focus();
							/**el.blur(function(){
								window.setTimeout(function(){
									var v = el.val();
									editorDiv.data('v', v);
									contentSpan.text(v).show();
									editorDiv.hide();
									me.live('dblclick', dblclick);
									me.addClass('dirty');									
								},200);  
							});**/
						}
					}
					me.unbind('dblclick');
				});
				/**$('table.dataview tbody tr', el).live('click', function(){
					$(this).toggleClass('selected-tr');
					if($(this).hasClass('selected-tr')){
						$(':checkbox', this).attr("checked",true);	
						var allchecked = true;
						$('table.dataview tbody :checkbox', el).each(function(){
							if(!this.checked){
								allchecked = false;
								return;
							}
						});
						if(allchecked){
							$(':checkbox[name=checkall]', el).attr("checked",true);	
						}
					}else{
						$(':checkbox', this).attr("checked",false);
						$(':checkbox[name=checkall]', el).attr("checked",false);
					}
				});**/
				$(':checkbox[name=checkall]', el).click(function(){
					$('table.dataview tr :checkbox', el).attr("checked",this.checked);
					/**if(this.checked){
						$('table.dataview tbody tr', el).addClass('selected-tr');
					}else{
						$('table.dataview tbody tr', el).removeClass('selected-tr');
					}**/
				});
				
				$('.changepage', el).live('click',function(e){
					$(':checkbox[name=checkall]', el).attr("checked",false);
					var p = 1;
					var page = parseInt(el.data('page'));
					var pagesize = parseInt(el.data('pagesize'));
					var total = parseInt(el.data('total'));
					var url = el.data('url');
					var t = e.target;
					if($(t).is('.first')){
						p = 1;
					}
					if($(t).is('.pre')){
						p = page - 1;
					}
					if($(t).is('.next')){
						p = page + 1;
					}
					if($(t).is('.last')){
						p = Math.ceil(total/pagesize);
					}
					el.data('page', p);
					var baseparams = {page: p, pagesize: pagesize};
					if(settings.condition){
						if($.isFunction(settings.condition)){
							baseparams = $.extend(baseparams, settings.condition());
						}
					}
					var dd = ds(url, baseparams);
					el.data('ds', dd);
					var pp = parseInt(el.data('page'));
					var ps = parseInt(el.data('pagesize'));
					var opts = el.data('options');
					$('table.dataview tbody',el).empty();
					$('table.dataview tbody',el).append(body(dd.data, opts));
					$('.pagebar',el).empty();
					$('.pagebar',el).append(pager(dd.total, pp, ps));
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
				$(this).data('page', 1);
				$(this).data('pagesize', 10);
				var dd = ds(url, p);
				$(this).data('ds', dd);
				$(this).data('total', dd.total);
				$('table.dataview tbody',this).empty();
				$('table.dataview tbody',this).append(body(dd.data, opts));
				$('.pagebar',this).empty();
				$('.pagebar',this).append(pager(dd.total, 1, pagesize));
        	});
        },
        refresh: function(params){
        	return this.each(function(){
        		$(':checkbox[name=checkall]', this).attr("checked",false);
        		var opts = $(this).data('options');
        		var url = opts.url;
        		var page = parseInt($(this).data('page'));
        		var pagesize = parseInt($(this).data('pagesize'));
        		var p = {page:page, pagesize:pagesize};
        		if(params){
        			p = $.extend(p, params);
        		}else{
        			if(opts.condition){
        				if($.isFunction(opts.condition)){
        					p = $.extend(p, opts.condition());
        				}
        			}
        		}
        		$(this).data('page', page);
        		$(this).data('pagesize', 10);
        		var dd = ds(url, p);
        		$(this).data('ds', dd);
        		$(this).data('total', dd.total);
        		$('table.dataview tbody',this).empty();
        		$('table.dataview tbody',this).append(body(dd.data, opts));
        		$('.pagebar',this).empty();
        		$('.pagebar',this).append(pager(dd.total, page, pagesize));
        	});
        },
        getSelected: function(){
        	var ret = [];
        	var rows = null;
        	var grid = $(this);
        	this.each(function(){
        		rows = $('table.dataview tbody :checkbox:checked', this).parent().parent();
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
		var ds = {total:0, data:[]};
		$.ajax({
			url:url,
			type: 'post',
			cache: false,
			async: false,
			data: param,
			dataType: 'json',
	        success: function(result){
	        	ds = result;
			}
		});
		return ds;
	}
	function pager(total, page, pagesize){
		var code = new Array();
		if(total>0){
			var pagecount = Math.ceil(total/pagesize);
			code.push("总共<span class=total>");
			code.push(total);
			code.push("</span>记录&nbsp;&nbsp;");
			code.push("当前<span class=current>");
			code.push(page);
			code.push("</span>");
			code.push("/<span class=pagecount>");
			code.push(pagecount);
			code.push("</span>页&nbsp;&nbsp;");
			code.push("<span class=changepage>");
			code.push("<span class=first");
			if(page == 1){
				code.push(" disabled=disabled");
			}
			code.push(">首页</span>&nbsp;&nbsp;");
			code.push("<span class=pre");
			if(page == 1){
				code.push(" disabled=disabled");
			}
			code.push(">上一页</span>&nbsp;&nbsp;");
			code.push("<span class=next");
			if(page == pagecount){
				code.push(" disabled=disabled");
			}
			code.push(">下一页</span>&nbsp;&nbsp;");
			code.push("<span class=last");
			if(page == pagecount){
				code.push(" disabled=disabled");
			}
			code.push(">尾页</span>&nbsp;&nbsp;");
			code.push("</span>");
		}else{
			code.push("<font color=red>没有符合条件的数据</font>");
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
		code.push("<tr class='grid-row'>");
		if(opts.selector&&(opts.selector == 'm')){
			code.push("<td class='grid-cell' align=center width=30><input type=checkbox hideFocus></td>");
		}
		else if(opts.selector&&(opts.selector == 's')){
			code.push("<td class='grid-cell' align=center width=30><input type=radio hideFocus name=ids></td>");
		}
		$(opts.columns).each(function(){
			var f = this.field;
			var align = this.align;
			var tip = this.tip;
			var render = this.render;
			var editor = this.editor;
			var v = record[f]==undefined?'':record[f];
			code.push("<td class='grid-cell'");
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
		code.push("<div class='header'>", "<span class=icon-grid></span>", "<strong>", opts.title, "</strong>", "</div>");
		var tbar = opts.tbar;
		if(tbar&&tbar.length>0){
			code.push("<div class='toolbar'></div>");
		}
		code.push("<table class='dataview'>");
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