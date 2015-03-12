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
    $.fn.table.editors = {
	    text: {    
	    	init : function(cell, options) {
				var v0 = cell.html();
				cell.html("");
	    		var input = $('<input type="text" class="datagrid-editable-input"/>');
	    		input.width(cell.width() - 14).val(v0).appendTo(cell);
	    		input.focus();
	    		input.click(function() {
					return false;
				});
	    		input.blur(function() {
					var v = $(this).val();
					cell.html(v);
				});
			}
		},
	    combox: {    
	    	init : function(cell, options) {
				var v0 = cell.html();
				cell.html("");
	    		var combox = $('<input type="text" class="datagrid-editable-combox" style="width:50px;"/>').appendTo(cell);
	    		combox.AutoComplete2({
	    			width: 50,
	    		 	data:[{text : '正常', value : 1},{text : '锁定', value : 0},{text : '离职', value : 2}]
	    		});
			}
		}
    };
	$.fn.table.defaults = {
		columns:[],
		pager:true,
		onSelectRow : function(rowid) {}
	};
	$.fn.table.methods = {
		init: function(options) {
			var settings = $.extend({}, $.fn.table.defaults, options);
			return this.each(function(){
				var el = $(this);
				var pager = settings.pager;
				el.data('pagesize', 10);
				$(this).data('inserted', []);
				$(this).data('deleted', []);
				$(this).data('updated', []);
				var _ds;
				if(settings.url){
					el.data('url', settings.url);
					var baseparams = {};
					if(pager){
						baseparams['page'] = 1;
						baseparams['pagesize'] = 10;
					}
					if(settings.condition){
						if($.isFunction(settings.condition)){
							baseparams = $.extend(baseparams, settings.condition());
						}
					}
					_ds = ds(settings.url, baseparams);
				}else{
					_ds = settings.data;
				}
				var rows = [];
				if(pager){
					rows = _ds.data.slice(0); 
				}else{
					rows = _ds; 
				}
				$(this).data('options', settings);
				el.data('rows', rows);
				$(this).data('allrow', rows);
				el.data('ds', _ds);
				el.data('total', _ds.total);
				var html = new Array();
				html.push("<div class='grid'>");
				html.push("<div class='grid-head'>");
				html.push(header2(settings));
				html.push("</div>");
				html.push("<div class='grid-bd'>");
				html.push("<table class='table'>");
				//html.push(header(settings));
				html.push("<tbody>");
				html.push(body(rows, settings));
				html.push("</tbody>");
				html.push("</table>");
				html.push("</div>");
				html.push("</div>");
				if(settings.pager){
					var total = parseInt(el.data('total'));
					html.push("<div class='pagination'>");
					html.push(pager2(total, 1, 10));
					html.push("</div>");
				}
				el.append(html.join(''));
				/**var W = $('.grid-head', el).outerWidth();
				if(rows.length <= 10){
					$('.grid-bd', el).width(W - 17);
				}**/
				$('.grid-row').live('mouseenter', function(){
					$(this).addClass('strips');
				}).live('mouseleave', function(){
					$(this).removeClass('strips');
				});
				
				$('tbody tr', el).live('click', function(){
					var rows = $('tbody tr', el);
					var idx = rows.index(this);
					if($(this).hasClass('highlight')){
						$(this).removeClass('highlight');
						$(this).find('td.checkbox').removeClass('selected').html("<i class='icon-check-empty'></i>");
					}else{
						if(!settings.multiselect){
							$(this).siblings('tr').removeClass('highlight');
							$(this).siblings('tr').find('td.checkbox').removeClass('selected').html("<i class='icon-check-empty'></i>");
						}
						$(this).addClass('highlight');
						$('td.checkbox', this).addClass('selected').html("<i class='icon-check'></i>");
						settings.onSelectRow(idx);
					}
					var editing = $('tbody tr[editable=1]', el);
					if(editing.length > 0){
						var ridx = rows.index(editing);
						el.table('saveRow', ridx);
					}
					if($(this).attr('editable') == 0){
						el.table('editRow', idx);
					}
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

				$('.checkall', el).click(function(){
					if($(this).hasClass('selected')){
						$('.checkbox', el).removeClass('selected').html("<i class='icon-check-empty'></i>");
						$('.checkbox', el).parents('tr').removeClass('highlight');
					}else{				
						$('.checkbox', el).addClass('selected').html("<i class='icon-check'></i>");
						$('.checkbox', el).parents('tr').addClass('highlight');
					}
				});
				
				/**$('.pageNum', el).live('click',function(e){
					$('.checkbox', el).removeClass('selected').html("<i class='icon-check-empty'></i>");
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
				});**/
				$('div.pagination a', el).live('click',function(e){
					var p = 1;
					$('.checkbox', el).removeClass('selected').html("<i class='icon-check-empty'></i>");
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
					el.data('rows', dd.data);
					el.data('allrow', dd.data);
					var opts = el.data('options');
					$('tbody',el).empty().append(body(dd.data, opts));
					$('div.pagination',el).empty().append(pager2(dd.total, p, pagesize));
					$('tbody tr:odd', el).addClass('strips');
				});
				/**var len = settings.columns.length;
				$(settings.columns).each(function(index){
					var idx = index;
					if(settings.multiselect){
						idx += 1;
						len += 1;
					}
					if(settings.linenum){
						idx += 1;
						len += 1;
					}
					if(this.editor){					
						var selector = "tbody td:nth-child(" + len  + 'n + ' + (idx + 1) + ")";
						var cols = $(selector, el);
						var type = this.editor.type || 'text';
						var opts = this.editor.opts || {};
						cols.click(function() {
							var cell = $(this);
							$.fn.table.editors[type].init(cell, opts);
						});
					}
				});**/
			});
        },
        reload: function(params){
    		$('.checkbox', this).removeClass('selected').html("<i class='icon-check-empty'></i>");
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
			$(this).data('rows', dd.data);
			$(this).data('allrow', dd.data);
			$(this).data('inserted', []);
			$(this).data('deleted', []);
			$(this).data('updated', []);
			$('tbody',this).empty().append(body(dd.data, opts));
			$('div.pagination',this).empty().append(pager2(dd.total, 1, pagesize));
			$('tbody tr:odd', this).addClass('strips');
        },
        refresh: function(params){
    		$('.checkbox', this).removeClass('selected').html("<i class='icon-check-empty'></i>");
    		var opts = $(this).data('options');
    		var url = opts.url;
    		var page = parseInt($('div.pagination a.active', this).text());
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

			var dd = ds(url, p);
			$(this).data('ds', dd);
			$(this).data('rows', dd.data);
			$(this).data('allrow', dd.data);
			$(this).data('inserted', []);
			$(this).data('deleted', []);
			$(this).data('updated', []);
			$('tbody',this).empty().append(body(dd.data, opts));
			$('div.pagination',this).empty().append(pager2(dd.total, dd.page, pagesize));
				$('tbody tr:odd', this).addClass('strips');
        },
        getSelected: function(){
        	var ret = [];
        	var grid = this;
        	var rows = $('tbody tr', grid);
        	rows.each(function(){
        		if($(this).hasClass('highlight')){
        			var rowid = rows.index(this);
        			var rowdata = grid.table('getRowData', rowid);
        			ret.push(rowdata);   
        		}
        	});
        	return ret;
        },
        endEdit: function(){
        	var trs = $('tbody tr', this);
        	var editingrows = $('tbody tr[editable=1]', this);
        	if(editingrows.size() > 0){
        		var editingrowid = trs.index(editingrows[0]);
        		this.table('saveRow', editingrowid);
        	}
        },
        addRow: function(rowid, record){
        	this.table('endEdit');
        	var opts = $(this).data('options');
        	var allrow = $(this).data('allrow');
        	var len = allrow.length;
        	allrow.splice(rowid, 0, record);  
        	$(this).data('inserted').push(rowid);
        	var updated = $(this).data('updated');
        	updated.sort();
    		var min = updated[0];
    		var max = updated[updated.length - 1];
    		if(rowid <= min){
    			for(var k in updated){
    				updated[k] = updated[k] + 1;
    			}
    		}else if(rowid <= max){
    			for(var k in updated){
    				if(updated[k] >= rowid){
    					updated[k] = updated[k] + 1;
    				}
    			}
    		}
        	var rows = $(this).data('rows');
			var code = new Array();
			code.push(buildRow(record, 0, opts));
			if(rowid == 0){
				$('tbody',this).prepend(code.join(''));
			}else{
				$('tbody tr:eq(' + (rowid - 1) + ')',this).after(code.join(''));
        	}
			//this.table('editRow', rowid);
        },
        appendRow: function(record){
        	var len = $(this).data('allrow').length;
			this.table('addRow', len, record);
        },
        editRow: function(rowindex){
        	var opts = $(this).data('options');
        	var rowData = this.table('getRowData', rowindex);
        	var tr = $("tbody tr", this).eq(rowindex);
        	var updated = $(this).data('updated');
        	var updatedRowData = updated[rowindex] || {};
        	tr.find('td').each(function(){
        		var cell = $(this);
        		var field = $(this).attr('field');
        		var fieldValue = rowData[field];
        		var updatedValue = updatedRowData[field];
        		var v0 = fieldValue;
        		if(updatedValue){
        			v0 = updatedValue;
        		}
            	var editor = null;
    			$(opts.columns).each(function(){
            		var f = this.field;
            		if(field == f){
            			editor = this.editor;
            			return false;
            		}
            	});
    			if(editor){
	    			tr.attr('editable', 1);
					cell.html("");
					var padding = parseInt(cell.css('padding-left'));
					if(editor.type == 'text'){
			    		var input = $('<input type="text" id="' + rowindex + '_' + field + '"/>');
			    		input.width(cell.width() - 2*padding).val(v0).appendTo(cell);
			    		//input.focus();
			    		input.click(function() {
							return false;
						});
			    		input.blur(function() {
							var v = $(this).val();
							//cell.html(v);
						});
					}else if(editor.type == 'datebox'){
						var ctrhtml = [];
						ctrhtml.push('<input type="text" id="', rowindex, '_', field, '"');
						ctrhtml.push('class="Wdate" onFocus="WdatePicker({readOnly:true})"', '/>');
			    		var ctr = $(ctrhtml.join(''));
			    		ctr.width(cell.width() - 2*padding).val(v0).appendTo(cell);
					}else if(editor.type == 'selecttree'){
						var ctrhtml = [];
						ctrhtml.push('<input type="text" id="', rowindex, '_', field, '"/>');
			    		var ctr = $(ctrhtml.join(''));
			    		ctr.width(cell.width() - 2*padding).appendTo(cell);
			    		ctr.selecttree(editor.option);
			    		ctr.selecttree('setValue', v0);
					}else if(editor.type == 'selectbox'){
						var ctrhtml = [];
						ctrhtml.push('<input type="text" id="', rowindex, '_', field, '"/>');
			    		var ctr = $(ctrhtml.join(''));
			    		ctr.width(cell.width() - 2*padding).appendTo(cell);
			    		ctr.selectbox(editor.option);
			    		ctr.selectbox('setValue', v0);
					}
    			}
        	});
        },
        saveRow: function(rowindex){
        	var opts = $(this).data('options');
        	var rowData = this.table('getRowData', rowindex);
        	var newData = jQuery.extend(true, {}, rowData);
        	var tr = $("tbody tr", this).eq(rowindex);
        	var editing = tr.attr('editable');
        	var changed = false;
        	if(editing == 1){
        		tr.attr('editable', 0);
	        	tr.find('td').each(function(){
	        		var cell = $(this);
	        		var field = $(this).attr('field');
	        		if(field){
		            	var cm = null;
		    			$.each(opts.columns, function(){
		            		var f = this.field;
		            		if(field == f){
		            			cm = this;
		            			return false;
		            		}
		            	});
		    			if(cm.editor){
		    				var editor = cm.editor;
		    				var render = cm.render;
		    				var ctrid = rowindex + '_' + field;
		    				var ctr = $('#' + ctrid);
							if(editor.type == 'text'){
								var v = ctr.val();
								cell.html(v);
								if(v != newData[field]){
									changed = true;
									newData[field] = v;
								}
							}else if(editor.type == 'datebox'){
								var v = ctr.val();
								cell.html(v);
								if(v != newData[field]){
									changed = true;
									newData[field] = v;
								}
							}else if(editor.type == 'selecttree'){
					    		var v = ctr.selecttree('getValue');
					    		if(render){
					    			cell.html(render(v));
					    		}else{				    			
					    			cell.html(v);
					    		}
								if(v != newData[field]){
									changed = true;
									newData[field] = v;
								}
							}else if(editor.type == 'selectbox'){
					    		var v = ctr.selectbox('getValue');
					    		if(render){
					    			cell.html(render(v));
					    		}else{				    			
					    			cell.html(v);
					    		}
								if(v != newData[field]){
									changed = true;
									newData[field] = v;
								}
							}
		    			}
	        		}
	        	});
        	}
        	if(changed){
	        	var updated = $(this).data('updated');
	        	var inserted = $(this).data('inserted');
	        	//updated[rowindex] = newData;
	        	$(this).data('allrow')[rowindex] = newData;
	        	if($.inArray(rowindex, inserted) == -1){
	        		updated.push(rowindex);
	        	}
        	}
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
        getRowData: function(rowindex){
        	var rows = $(this).data('allrow');
        	return rows[rowindex];
        },
        getInserted: function(){
        	var allrow = $(this).data('allrow');
        	var inserted = $(this).data('inserted');
        	var ret = [];
        	for(var rowid in inserted){
        		ret.push(allrow[inserted[rowid]]);
        	}
        	return ret;
        },
        getUpdated: function(){
        	var allrow = $(this).data('allrow');
        	var updated = $(this).data('updated');
        	var ret = [];
        	for(var rowid in updated){
        		ret.push(allrow[updated[rowid]]);
        	}
        	return ret;
        },
        getDeleted: function(){
        	var allrow = $(this).data('allrow');
        	var deleted = $(this).data('deleted');
        	var ret = [];
        	for(var rowid in deleted){
        		ret.push(allrow[deleted[rowid]]);
        	}
        	return ret;
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
        	var ret = {};
        	ret['inserted'] = this.table('getInserted');
        	ret['deleted'] = this.table('getDeleted');
        	ret['updated'] = this.table('getUpdated');
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
		code.push(" editable=0>");
		if(opts.multiselect){
			code.push("<td align=center width=20 class='checkbox'><i class='icon-check-empty'></i></td>");
		}
		if(opts.linenum){
			code.push("<td align=center width=20 class='linenum'>", index + 1, "</td>");
		}
		$(opts.columns).each(function(){
			var f = this.field;
			var align = this.align;
			var tip = this.tip;
			var render = this.render;
			var w = this.width;
			var editor = this.editor;
			var v = record[f]==undefined?'':record[f];
			code.push("<td");
			if(f){
				code.push(" field='", f, "'");
			}
			if(tip){
				code.push(" title=" + span.text(v).html() + "");
			}
			if(w){
				code.push(" width=" + w);
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
		if(opts.multiselect){
			code.push("<th align=center width=20 class='checkbox checkall'><i class='icon-check-empty'></i></th>");
		}
		if(opts.linenum){
			code.push("<th align=center width=20 class='linenum'>#</th>");
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
	function header2(opts){
		var code = new Array();
		code.push("<table class='table' style='border:none;'>");
		code.push("<thead><tr>");
		if(opts.multiselect){
			code.push("<th align=center width=20 class='checkbox checkall'><i class='icon-check-empty'></i></th>");
		}
		if(opts.linenum){
			code.push("<th align=center width=20 class='linenum'>#</th>");
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
		//code.push("<th align=center width=6 class='scrollbar'></th>");
		code.push("</tr></thead></table>");
		return code.join('');
	}
})(jQuery);