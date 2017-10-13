(function($){  
	$.fn.grid = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
    var I18N = {
		'zh' : {
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "尾页",
			pagepre : "每页",
			item : "项",
			emptyrecords : '没有找到记录!',
			recordtext : '共  {0} 项 当前第  {1} / {2} 页',
			recordtext2 : '显示 {0} 项  共  {1} 项'
		},
		'en' : {
			first : "first",
			prev : "prev",
			next : "next",
			last : "last",
			pagepre : "per page",
			item : "item",
			emptyrecords : 'No records found!',
			recordtext : 'Total {0} items page {1} / {2}',
			recordtext2 : 'show {0} item  total {1} items'
		}
	};
    $.fn.grid.editors = {
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
	$.fn.grid.defaults = {
		title : null,
		columns:[],
		condensed:false,
		bordered:true,
		striped:true,
		resizableColumns:false,
		detailview : false,
		onExpandRow : function(record, detailview) {},
        pageName: 'page',
        pageSizeName: 'pagesize',
        totalName: 'total',
		pagesize : 10,
		pglist : [10, 20, 50, 100],
		lang : window.localStorage.lang || 'zh',
		emptyrecords : '没有记录',
		recordtext : '显示 {0} - {1} 项  共  {2} 项',
		queryParam : {},
		processData : function(result){return result;},
		onRenderBefore : function(html) {return html;},
		onSelectRow : function(rowid, status) {},
		onComplete : function() {},
		beforeEdit : function(rowid) {},
	};
	function endEdit(jq) {
		var trs = jq.find('tbody tr');
		var editing = jq.find('tbody tr.editing');
		if(editing.length > 0){
			var ridx = trs.index(editing);
			jq.grid('saveRow', ridx);
		}
	}
	function beginEdit(jq, rowindex) {
		endEdit(jq);
        var tr = getTr(jq, rowindex);
		if (tr.hasClass("editing")) {
		    return;
		}
		tr.addClass("editing");
		createEditor(jq, rowindex);
	}
	function getTr(jq, rowindex) {
        var tr = $("tbody tr", jq).eq(rowindex);
       return tr;
	}
	function getRow(jq, rowindex) {
    	var rows = jq.data('rows');
    	return rows[rowindex];
	}
	function getEditors(opts){
    	var editors = null;
		$(opts.columns).each(function(){
    		var f = this.field;
			var editor = this.editor;
    		if(editor){
				if(!editors){
					editors = {};
				}
    			editors[f] = editor;
    		}
    	});
    	return editors;
	}
	function getEditor(jq, field){
		var opts = jq.data('options');
    	var editor = null;
		$(opts.columns).each(function(){
    		var f = this.field;
    		if(field == f){
    			editor = this.editor;
    			return false;
    		}
    	});
    	return editor;
	}
	function getFirstEditField(opts){
		var field = '';
		$(opts.columns).each(function(){
    		var f = this.field;
			var editor = this.editor;
    		if(editor){
    			field = f;
    			return false;
    		}
    	});
    	return field;
	}
	function createEditor(jq, rowindex) {
		var opts = jq.data('options');
		var first = getFirstEditField(opts);
		var tr = getTr(jq, rowindex);
		var row = getRow(jq, rowindex);
    	tr.find('td').each(function(){
    		var cell = $(this);
    		var field = $(this).attr('field');
    		var fieldValue = row[field];
    		//var updatedValue = updatedRowData[field];
    		var v0 = fieldValue;
//    		if(updatedValue){
//    			v0 = updatedValue;
//    		}
        	var editor = getEditor(jq, field);
			if(editor){
    			tr.attr('editable', 1);
				cell.html("");
				var padding = parseInt(cell.css('padding-left'));
				if(editor.type == 'text'){
					var h = cell.height() - 6;
					var style = "width:98%;height:" + h + "px;line-height:1;";
		    		var input = $('<input type="text" id="' + rowindex + '_' + field + '" class="form-control" style="' + style + '"/>');
		    		input.val(v0).appendTo(cell);
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
					ctrhtml.push('<input type="text" class="form-control" id="', rowindex, '_', field, '" />');
		    		var ctr = $(ctrhtml.join(''));
		    		ctr.val(v0).appendTo(cell);
		    		//ctr.datepicker();
				}else if(editor.type == 'selecttree'){
					var ctrhtml = [];
					ctrhtml.push('<input type="text" id="', rowindex, '_', field, '"/>');
		    		var ctr = $(ctrhtml.join(''));
		    		ctr.width(cell.width() - 2*padding).appendTo(cell);
		    		//ctr.selecttree(editor.option);
		    		//ctr.selecttree('setValue', v0);
				}else if(editor.type == 'selectbox'){
					var ctrhtml = [];
					ctrhtml.push('<input class="form-control" type="text" id="', rowindex, '_', field, '"/>');
		    		var ctr = $(ctrhtml.join(''));
		    		ctr.click(function() {
						return false;
					});
		    		ctr.width(cell.width() - 2*padding);
		    		ctr.appendTo(cell);
		    		//ctr.selectbox(editor.option);
		    		//ctr.selectbox('setValue', v0);
				}else if(editor.type == 'select'){
					var ctrhtml = [];
					var data = editor['options']['data'];
					var processData = editor['options']['processData'];
					var change = editor['options']['change'];
					var ds = processData(data);
					ctrhtml.push('<select id="', rowindex, '_', field, '" style="width : 99%;height : 100%;">');
					for(var k in ds){
						var v = ds[k]['value'];
						var t = ds[k]['text'];
						ctrhtml.push("<option value='", v, "'");
						if(v == v0){
							ctrhtml.push(' selected="selected"');
						}
						ctrhtml.push('>', t, '</option>');
					}
					ctrhtml.push('</select>');
		    		var ctr = $(ctrhtml.join(''));
		    		ctr.click(function() {
						return false;
					});
		    		ctr.width(cell.width() - 2*padding);
		    		ctr.appendTo(cell);
		    		ctr.change(function(){
		    			change.call(this);
		    		});
		    		
		    		//ctr.selectbox(editor.option);
		    		//ctr.selectbox('setValue', v0);
				}else if(editor.type == 'check'){
					var ctrhtml = [];
					ctrhtml.push('<input type="checkbox" id="', rowindex, '_', field, '"/>');
		    		var ctr = $(ctrhtml.join(''));
		    		ctr.appendTo(cell);
		    		ctr.attr('checked', (v0 == true) || (v0 == 'true'));
				}
			}
    	});
    	opts.beforeEdit(rowindex);
    	var fstid = '#' + rowindex + '_' + first;
    	if($(fstid).size() > 0){
    		$(fstid)[0].focus();
    	}
	}
	function loadData(url, param){
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
	function renderData(data){
		var grid = this;
		var opts = grid.data('options');
		var thead = grid.find('table.table thead');
		var tbody = grid.find('table.table tbody');
		var widget_foot = grid.find('.widget-foot').show();
		thead.find('.sm div').removeClass('checked').addClass('unchecked');
		var rows = new Array();
		rows = data.slice(0); 
		if(data.length > 0){
			tbody.html('');
			var bdhtml = body(rows, opts);
			if(opts.onRenderBefore){
				//bdhtml = opts.onRenderBefore(bdhtml);
			}
			for(var k in bdhtml){
				tbody.append(bdhtml[k]);
			}
			widget_foot.html('');
		}else{
			tbody.html('');
			var emptyrecords = I18N[opts.lang].emptyrecords;
			widget_foot.html('<div class="alert alert-warning">' + emptyrecords + '</div>');
		}
		this.data('rows', rows);
		this.data('allrow', rows);
	}
	
	function clickRow(){
		var grid = this;
		var opts = this.data('options');
		var thead = this.find('table.table thead');
		var tbody = this.find('table.table tbody');
		var editors = this.data('editors');
		tbody.click(function(e){
			var target = $(e.target);
			var tr = target.closest('tr');
			e.stopPropagation();
			var target = $(e.target);
			if(target.is("a") || target.is("button") || target.is("input")){
				e.stopPropagation();
				return;
			}
			if(target.is('td.detailview')){
				e.stopPropagation();
				return;
			}
			if(target.hasClass("expander")){
				e.stopPropagation();
				expander(grid, target);
				return false;
			}
			var trs = tbody.find('tr');
			var rowid = trs.index(tr);
			if(editors){
				beginEdit(grid, rowid);
			}
			if(opts.sm){
				if(opts.sm == 'm'){
					if(tr.hasClass('selected')){
						tr.removeClass('selected');
						tr.find('.sm div').removeClass('checked').addClass('unchecked');
						thead.find('.sm div').removeClass('checked').addClass('unchecked');
						opts.onSelectRow.call(grid, rowid, false);
					}else{
						tr.addClass('selected');
						tr.find('.sm div').removeClass('unchecked').addClass('checked');
						var len = tbody.find('.sm div.unchecked').length;
						if(len == 0){
							thead.find('.sm div').removeClass('unchecked').addClass('checked');
						}
						opts.onSelectRow.call(grid, rowid, true);
					}
				}else{
					if(tr.hasClass('selected')){
						tr.removeClass('selected');
						tr.find('.sm div').removeClass('checked').addClass('unchecked');
						opts.onSelectRow.call(grid, rowid, false);
					}else{
						tbody.find('.sm div').removeClass('checked').addClass('unchecked');
						tbody.find('tr').removeClass('selected');
						tr.addClass('selected');
						tr.find('.sm div').removeClass('unchecked').addClass('checked');
						opts.onSelectRow.call(grid, rowid, true);
					}
				}
			}
	    });
	}
	
	function expander(jq, expander){
		var opts = jq.data('options');
		if(expander.hasClass('collapse')){
			expander.removeClass('collapse').addClass('expand');
			var row = expander.parents('tr');
			var rowid = row.attr('idx');
			//var detail = opts.detailFormatter(rowid);
			var detailrow = row.next('tr').show();
			if(!detailrow.data('loaded')){
				var detailview = detailrow.find('td');//.html(detail);
				var record = jq.grid('getRowData', rowid);
				opts.onExpandRow(record, detailview);
				detailrow.data('loaded', true);
			}
		}else{
			expander.removeClass('expand').addClass('collapse');
			expander.parents('tr').next('tr').hide();
		}
	}
	
	function getInterval(current_page, pagecount, displaynum)  {
		var ne_half = Math.ceil(displaynum/2);
		var upper_limit = pagecount - displaynum + 1;
		var start = current_page>ne_half ? Math.max(Math.min(current_page-ne_half + 1, upper_limit), 1):1;
		var end = current_page>ne_half ? Math.min(current_page + ne_half - 1, pagecount) : Math.min(displaynum, pagecount);
		return [start,end];
	}
	
	function buildPager(){
		var grid = this;
		var opts = grid.data('options');
    	var total = parseInt(grid.data('total')); 
    	var page = parseInt(grid.data('page')); 
    	var pagesize = parseInt(opts['pagesize']);
		var widget_foot = grid.find('.widget-foot').show();
		var pc = Math.ceil(total/pagesize);
		var displaynum = 5;
		var interval = getInterval(page, pc, displaynum);
		var code = new Array();
		if(total == 0){
			widget_foot.html("<div style='color:red;margin: 10px;'>" + opts.emptyrecords + "</div>");
		}else{
			//if(total > pagesize){
				var begin = (page - 1)*pagesize + 1;
				var end = page*pagesize;
				if(page == pc){
					end = total;
				}
				var recordtext = I18N[opts.lang].recordtext;
				recordtext = String.format(recordtext, total, page, pc);
				/**if(begin == end){
					recordtext = I18N[opts.lang].recordtext2;
					recordtext = String.format(recordtext, begin, total);
				}**/
				var first = I18N[opts.lang].first;
				var prev = I18N[opts.lang].prev;
				var next = I18N[opts.lang].next;
				var last = I18N[opts.lang].last;
				var pagepre = I18N[opts.lang].pagepre;
				var item = I18N[opts.lang].item;
				code.push('<table width="100%"><tr>');
				code.push('<td>');
				code.push(recordtext);
				code.push('&nbsp;&nbsp;');
				//code.push(pagepre);
				//code.push('&nbsp;');
				code.push(getpglist(opts.pglist, opts.pagesize));
				//code.push('&nbsp;');
				//code.push(item);
				code.push('</td>');
				code.push('<td style="text-align:right">');
				code.push('<ul class="pagination pagination-sm">');
				code.push('<li');		
				if(page <= 1){
					code.push(' class="disabled"');		
				}
				code.push('><a href="javascript:void(0)" class="pager-first">', first ,'</a></li>');	
				code.push('<li');		
				if(page <= 1){
					code.push(' class="disabled"');		
				}
				code.push('><a href="javascript:void(0)" class="pager-prev">', prev ,'</a></li>');	
//				// 产生起始点
//				if (interval[0] > 0 && edgenum > 0)
//				{
//					var end = Math.min(edgenum, interval[0]);
//					for(var i = 0; i < end; i++) {
//						code.push('<li');	
//						if(i + 1 == page){
//							code.push(' class="active"');		
//						}
//						code.push('><a href="javascript:void(0)">', i + 1, '</a></li>');	
//					}
//					if(edgenum < interval[0])
//					{
//						code.push('<li><span class="ellipsis">...</span></li>');		
//					}
//				}
				// 产生内部的些链接
				for(var i = interval[0]; i <= interval[1]; i++) {
					code.push('<li');	
					if(i == page){
						code.push(' class="active"');		
					}
					code.push('><a href="javascript:void(0)" class="pager-page">', i, '</a></li>');		
				}
//				// 产生结束点
//				if (interval[1] < pc && edgenum > 0)
//				{
//					if(pc - edgenum > interval[1])
//					{
//						code.push('<li><a href="javascript:void(0)">...</a></li>');		
//					}
//					var begin = Math.max(pc - edgenum, interval[1]);
//					for(var i = begin; i < pc; i++) {
//						code.push('<li');	
//						if(i + 1 == page){
//							code.push(' class="active"');		
//						}
//						code.push('><a href="javascript:void(0)">', i + 1, '</a></li>');	
//					}
//					
//				}
				code.push('<li');		
				if(page == pc){
					code.push(' class="disabled"');		
				}
				code.push('><a href="javascript:void(0)" class="pager-next">', next ,'</a></li>');	
				code.push('<li');		
				if(page == pc){
					code.push(' class="disabled"');		
				}
				code.push('><a href="javascript:void(0)" class="pager-end">', last ,'</a></li>');	
				code.push('</ul>');
				code.push('</td>');
//				code.push('<td>');
//				code.push('<select class="form-control input-sm pg-selbox" style="width:60px;border-left:0;" value="', pagesize, '">');
//				code.push("<option role='option' value='5'");
//				if(pagesize == 5){
//					code.push(" selected='selected'");
//				}
//				code.push(">5</option>");
//				code.push("<option role='option' value='10'");
//				if(pagesize == 10){
//					code.push(" selected='selected'");
//				}
//				code.push(">10</option>");
//				code.push("<option role='option' value='20'");
//				if(pagesize == 20){
//					code.push(" selected='selected'");
//				}
//				code.push(">20</option>");
//				code.push("<option role='option' value='50'");
//				if(pagesize == 50){
//					code.push(" selected='selected'");
//				}
//				code.push(">50</option>");
//				code.push('</select>');
//				code.push('</td>');
//				code.push('<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>');
//				code.push('<td>Page</td>');
//				code.push('<td>');
//				code.push('<input class="form-control input-sm pg-input" type="text" style="width:40px;margin:0;" value="', page, '"/>');
//				code.push('</td>');
//				code.push('<td>of ', pc, '</td>');
//				code.push('<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>');
				code.push('</tr></table>');
			}
	    	widget_foot.html(code.join(''));
	    	widget_foot.find('input.pg-input').keypress( function(e) {
                var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
                if(key == 13) {
                	var p = page;
                	var v = $(this).val();
                	if(v.length > 0){
                		if(!isNaN(v) && v > 0){
                			p = Math.min(v, pc);
                		}
                	}
                    renderPage.call(grid, p);
                    return false;
                }
            });
		//}
	}
	function buildPager2(){
		var grid = this;
		var opts = grid.data('options');
    	var total = grid.data('total'); 
    	var page = grid.data('page'); 
    	var pagesize = opts['pagesize'];
		var widget_foot = grid.find('.widget-foot').show();
		var pc = Math.ceil(total/pagesize);
		var code = new Array();
		if(total == 0){
			widget_foot.html("<div style='color:red;margin: 10px;'>" + opts.emptyrecords + "</div>");
		}else{
			if(total > 10){
				var begin = (page - 1)*pagesize + 1;
				var end = page*pagesize;
				if(page == pc){
					end = total;
				}
				var recordtext = String.format(opts.recordtext, total, page, pc);
				code.push("<table align='center' style='margin: auto;'><tr>");		
				code.push("<td class='ui-pg-button ui-corner-all");		
				if(page <= 1){
					code.push(" ui-state-disabled' style='cursor: default;'");		
				}else{
					code.push("' style='cursor: pointer;'");		
				}
				code.push("><span class='ui-icon ui-icon-seek-first'></span></td>");				
				code.push("<td class='ui-pg-button ui-corner-all");		
				if(page <= 1){
					code.push(" ui-state-disabled' style='cursor: default;'");		
				}else{
					code.push("' style='cursor: pointer;'");		
				}
				code.push("><span class='ui-icon ui-icon-seek-prev'></span></td>");	
				code.push("<td class='ui-pg-button ui-state-disabled' style='width: 4px; cursor: default;'><span style='border-left: 1px solid #ccc;border-right: 1px solid #ccc;margin: 1px;'></span></td>");
				code.push("<td>Page <input class='ui-pg-input' type='text' size='2' maxlength='7' value='", page, "'/> of ", pc, "</td>");	
				code.push("<td class='ui-pg-button ui-state-disabled' style='width: 4px; cursor: default;'><span style='border-left: 1px solid #ccc;border-right: 1px solid #ccc;margin: 1px;'></span></td>");
				code.push("<td class='ui-pg-button ui-corner-all");		
				if(page == pc){
					code.push(" ui-state-disabled' style='cursor: default;'");	
				}else{
					code.push("' style='cursor: pointer;'");		
				}
				code.push("><span class='ui-icon ui-icon-seek-next'></span></td>");	
				code.push("<td class='ui-pg-button ui-corner-all");		
				if(page == pc){
					code.push(" ui-state-disabled' style='cursor: default;'");	
				}else{
					code.push("' style='cursor: pointer;'");		
				}
				code.push("><span class='ui-icon ui-icon-seek-end'></span></td>");	
				
				code.push("<td><select class='ui-pg-selbox' role='listbox'>");
				code.push("<option role='option' value='10'");
				if(pagesize == 10){
					code.push(" selected='selected'");
				}
				code.push(">10</option>");
				code.push("<option role='option' value='20'");
				if(pagesize == 20){
					code.push(" selected='selected'");
				}
				code.push(">20</option>");
				code.push("<option role='option' value='50'");
				if(pagesize == 50){
					code.push(" selected='selected'");
				}
				code.push(">50</option>");
				code.push("</select></td>");
				code.push("<td align='right' width=''>", recordtext, "</td>");
				code.push("</tr>");	
				code.push("</table>");	
			}
	    	widget_foot.html(code.join(''));
	    	widget_foot.find('input.ui-pg-input').keypress( function(e) {
                var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
                if(key == 13) {
                	var p = page;
                	var v = $(this).val();
                	if(!isNaN(v)){
                		p = Math.min(v, pc);
                	}
                    renderPage.call(grid, p);
                    return false;
                }
            });
		}
	}
	function renderPage(page){
		var grid = this;
		var opts = grid.data('options');
		var pagesize = opts['pagesize'];
		var baseparams = {};
		baseparams[opts['pageName']] = page;
		baseparams[opts['pageSizeName']] = opts.pagesize;
		if(opts.condition){
			if($.isFunction(opts.condition)){
				baseparams = $.extend(baseparams, opts.condition());
			}
		}
		if(opts.queryParam){
			baseparams = $.extend(baseparams, opts.queryParam);
		}
		if(opts.url){
			$.ajax({
				url:opts.url,
				type: 'post',
				cache: false,
				async: false,
				data: baseparams,
				dataType: 'json',
				success: function(result){			
					var data = opts.processData ? opts.processData(result) : result;
					if(opts.pager){	
						var total = data['total']; 
						//var rows = data['data']; 
						//var pc = Math.ceil(total/pagesize);
						grid.data('total', total);
						grid.data('page', page);
						renderData.call(grid, data['data']);
						buildPager.call(grid);
					}else{
						renderData.call(grid, data);
					}
				}
			});
		}else if(opts.data){
			var data = opts.processData ? opts.processData(opts.data) : opts.data;
			var total = data.length; 
			//var rows = data['data']; 
			grid.data('total', total);
			grid.data('page', page);
			renderData.call(grid, data);
			if(opts.pager){
				buildPager.call(grid);
			}
		}else{
			var data = [];
			var total = 0; 
			grid.data('total', total);
			grid.data('page', page);
			renderData.call(grid, data);
		}
		opts.onComplete(grid);
		//drog(grid.find('.table'));
	}
    function eventHandler(event) {
        var grid = event.data.grid;
		var opts = grid.data('options');
    	var total = grid.data('total'); 
    	var page = parseInt(grid.data('page')); 
    	var pagesize = parseInt(opts['pagesize']);
    	var pc = Math.ceil(total/pagesize);
        var target = $(event.target);
        if (event.type === 'click') {
            if(target.hasClass('pager-first')){
            	if(target.parent('li').hasClass('disabled')){
            		return false;
            	}
            	renderPage.call(grid, 1);
            }else if(target.hasClass('pager-prev')){
            	if(target.parent('li').hasClass('disabled')){
            		return false;
            	}
            	renderPage.call(grid, page - 1);
            }else if(target.hasClass('pager-next')){
            	if(target.parent('li').hasClass('disabled')){
            		return false;
            	}
            	renderPage.call(grid, page + 1);
            }else if(target.hasClass('pager-end')){
            	if(target.parent('li').hasClass('disabled')){
            		return false;
            	}
            	renderPage.call(grid, pc);
            }else if(target.hasClass('pager-page')){
            	var p = parseInt(target.html());
            	renderPage.call(grid, p);
            }
            
        }
        if (event.type === 'change') {
            if(target.hasClass('pg-selbox')){
            	var pagesizeItem = target.val();
            	opts['pagesize'] = pagesizeItem;
            	renderPage.call(grid, 1);
            }
        }
    }
    String.format = function() {
        if (arguments.length == 0)
            return null;
        var str = arguments[0]||"";
        for ( var i = 1; i < arguments.length; i++) {
            var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
            str = str.replace(re, arguments[i]);
        }
        return str;
    };
    function getpglist(pglist, pagesize){
    	var html = new Array();
		html.push('<select class="form-control input-sm pg-selbox" style="width:70px;display:inline-block;" value="', pagesize, '">');
		for(var k in pglist){
			html.push("<option role='option' value='",  pglist[k], "'");
			if(pagesize == pglist[k]){
				html.push(" selected='selected'");
			}
			html.push(">", pglist[k], "</option>");
		}
		html.push('</select>');
		return html.join('');
    }
	$.fn.grid.methods = {
		init: function(options) {
			var settings = $.extend({}, $.fn.grid.defaults, options);
			settings['pager'] = settings['pagesize'] > 0;
			//settings['emptyrecords'] = $.language.getText('EMPTY_RECORDS_TEXT');
			//settings['recordtext'] = $.language.getText('RECORDTEXT');
			return this.each(function(){
				var el = $(this);
				var pagesize = settings['pagesize'];
				el.data('options', settings);
				el.data('editors', getEditors(settings));
				$(this).data('inserted', []);
				$(this).data('deleted', []);
				$(this).data('updated', []);
				var changed = {'inserted' : [], 'deleted' : [], 'updated' : []};
				var html = new Array();
				if(settings.title){
//					html.push('<div class="panel panel-primary"><div class="panel-heading">');
//					html.push('<h3 class="panel-title"><i class="fa fa-table"></i> ');
//					html.push(settings.title);
//					html.push('</h3></div>');
				}
				if(pagesize > 0){
				}
				html.push("<table class='grid-table table");
				if(settings.condensed){
					html.push(" table-condensed");
				}
				if(settings.bordered){
					html.push(" table-bordered");
				}
				if(settings.striped){
					html.push(" table-striped");
				}
				html.push("' style='table-layout:fixed'>");
				html.push(header2(settings));
				html.push("<tbody>");
				html.push("</tbody>");
				html.push("</table>");
				//html.push("</div>");
				//html.push("</div>");
				//html.push("<div class='loading loadmask' style='display : none; position:absolute;z-index: 1000; border: none; margin: 0px; padding: 0px; width: 100%; height: //100%; top: 0px; left: 0px; opacity: .50; background-color: #ccc;'></div>");
				//html.push('<div class="loadingMsg loadmask-msg" style="display : none; top: 50%; left: 50%;"><div>loading...</div></div>');
				//html.push("</div>");
				if(settings.title){
//					html.push("</div>");
				}
				html.push('<div class="widget-foot" style=""></div>');
				var code = html.join('');
				el.html(code);
				if(settings.resizableColumns){
					el.find('.table').resizableColumns(); 
				}
				var widget_foot = el.find('.widget-foot').show();
		    	widget_foot
		        .bind('click', { grid: el }, function (event) { eventHandler(event); })
		        .bind('change', { grid: el }, function (event) { eventHandler(event); });
		    	var selbox = el.find('.pg-selbox');
		    	selbox.bind('change', function (event) { 
		    		var target = $(event.target);
		            var pagesizeItem = target.val();
		            settings['pagesize'] = pagesizeItem;
		            renderPage.call(el, 1);
		    	});
				var thead = el.find('thead');
				var tbody = el.find('tbody');
				if(settings.sm && settings.sm == 'm'){
					thead.find('.sm div').click(function(e){
						if($(this).hasClass('checked')){
							$(this).removeClass('checked').addClass('unchecked');
							tbody.find('.sm div').removeClass('checked').addClass('unchecked');
							var trs = tbody.find('tr').removeClass('selected');
							$.each(trs, function(){
								var tr = $(this);
								var rowid = parseInt(tr.attr('idx'));
								settings.onSelectRow.call(el, rowid, false);
							});
						}else{
							$(this).removeClass('unchecked').addClass('checked');
							tbody.find('.sm div').removeClass('unchecked').addClass('checked');
							var trs = tbody.find('tr').addClass('selected');
							$.each(trs, function(){
								var tr = $(this);
								var rowid = parseInt(tr.attr('idx'));
								settings.onSelectRow.call(el, rowid, true);
							});
						}
				    });
				}
				clickRow.call(el);
				renderPage.call(el, 1);
				var pager = settings.pager;
				el.data('pagesize', 10);
				var pageinfo = {'pagesize' : 10, 'page' : 1, 'total' : 0};
				var changed = {'inserted' : [], 'deleted' : [], 'updated' : []};
				
//				el.find('tbody tr').click(function(e){
//					e.stopPropagation();
//					var rows = $('tbody tr', el);
//					var idx = rows.index(this);
//					if($(this).hasClass('highlight')){
//						$(this).removeClass('highlight');
//						$(this).find('td.checkbox').removeClass('selected').html("<i class='icon-check-empty'></i>");
//					}else{
//						if(!settings.multiselect){
//							$(this).siblings('tr').removeClass('highlight');
//							$(this).siblings('tr').find('td.checkbox').removeClass('selected').html("<i class='icon-check-empty'></i>");
//						}
//						$(this).addClass('highlight');
//						$('td.checkbox', this).addClass('selected').html("<i class='icon-check'></i>");
//						settings.onSelectRow(idx);
//					}
//					var editing = $('tbody tr[editable=1]', el);
//					if(editing.length > 0){
//						var ridx = rows.index(editing);
//						el.table('saveRow', ridx);
//					}
//					if($(this).attr('editable') == 0){
//						el.table('editRow', idx);
//					}
//				});
				$(document).click(function() {
					endEdit(el);
				});	
				
				//$('tbody tr:odd', el).addClass('strips');
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
				/**$('div.pagination a', el).on('click',function(e){
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
					
					var opts = el.data('options');
					var baseparams = {page: p, pagesize: pagesize};
					if(opts.condition){
						if($.isFunction(opts.condition)){
							baseparams = $.extend(baseparams, opts.condition());
						}
					}
					if(opts.queryParam){
						baseparams = $.extend(baseparams, opts.queryParam);
					}
					var dd = ds(url, baseparams);
					el.data('ds', dd);
					el.data('rows', dd.data);
					el.data('allrow', dd.data);
					$('tbody',el).empty().append(body(dd.data, opts));
					$('div.pagination',el).empty().append(pager2(dd.total, p, pagesize));
					$('tbody tr:odd', el).addClass('strips');
				});**/
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
        showLoading: function(){
        	var loading = $(this).find('.loading').fadeIn();
        	var loadingMsg = $(this).find('.loadingMsg');
        	var w = loadingMsg.outerWidth();
        	var h = loadingMsg.outerHeight();
        	loadingMsg.css({marginTop: -h/2, marginLeft: -w/2}).show();
        },
        hideLoading: function(){
        	var loading = $(this).find('.loading');
        	var loadingMsg = $(this).find('.loadingMsg');
			loading.fadeOut("slow");
			loadingMsg.fadeOut("slow");
        	setTimeout(function(){
				loading.hide();
				loadingMsg.hide();
			}, 1000);
        },
        reload: function(params){
        	var g = this;
        	/**$.blockUI.defaults.overlayCSS.backgroundColor = '#ccc';
        	$.blockUI.defaults.overlayCSS.opacity = 0.6;
        	g.block({message : null});**/
        	renderPage.call(g, 1);
        	/**setTimeout(function(){
				g.unblock();
			}, 200);**/
        },
        clear: function(){
        	this.find('.widget-content tbody').empty();
        	this.find('.widget-foot').empty().hide();
        },
        refresh: function(params){
    		var g = this;
    		var page = parseInt(g.data('page'));
    		renderPage.call(g, page);
        },
        resetSelection: function(){
        	var grid = this;
    		var opts = grid.data('options');
    		var thead = grid.find('table.table thead');
    		var tbody = grid.find('table.table tbody');
    		var rows = tbody.find('tr');
        	var selected = grid.find('tbody tr.selected');
        	selected.each(function(){
				$(this).removeClass('selected');
				$(this).find('.sm div').removeClass('checked').addClass('unchecked');
				thead.find('.sm div').removeClass('checked').addClass('unchecked');
				var rowid = rows.index(this);
				opts.onSelectRow.call(grid, rowid, false);
        	});
        },
        getSelected: function(){
        	var ret = [];
        	var grid = this;
        	var rows = $('tbody tr', grid);
        	rows.each(function(){
        		if($(this).hasClass('selected')){
        			var rowid = rows.index(this);
        			var rowdata = grid.grid('getRowData', rowid);
        			ret.push(rowdata);   
        		}
        	});
        	return ret;
        },
        getSelected2: function(){
        	var ret = [];
        	var grid = this;
        	var rows = $('tbody tr', grid);
        	rows.each(function(){
        		if($(this).hasClass('selected')){
        			var rowid = rows.index(this);
        			ret.push(rowid);   
        		}
        	});
        	return ret;
        },
        endEdit: function(){
    		var trs = this.find('tbody tr');
    		var editing = this.find('tbody tr.editing');
    		if(editing.length > 0){
    			var ridx = trs.index(editing);
    			this.grid('saveRow', ridx);
    		}
        },
        getEditing: function(){
        	var editingrowid = -1;
        	var trs = $('tbody tr', this);
        	var editingrows = $('tbody tr[editable=1]', this);
        	if(editingrows.size() > 0){
        		editingrowid = trs.index(editingrows[0]);
        	}
        	return editingrowid;
        },
        addRow: function(rowid, record){
        	var opts = $(this).data('options');
        	var allrow = $(this).data('allrow') || [];
        	var len = allrow.length;
        	record['_r'] = 1;
        	allrow.splice(rowid, 0, record);  
        	
        	/**var inserted = $(this).data('inserted');
        	inserted.sort();
    		var min1 = inserted[0];
    		var max1 = inserted[inserted.length - 1];
    		if(rowid <= min1){
    			for(var k in inserted){
    				inserted[k] = inserted[k] + 1;
    			}
    		}else if(rowid <= max1){
    			for(var k in inserted){
    				if(inserted[k] >= rowid){
    					inserted[k] = inserted[k] + 1;
    				}
    			}
    		}
    		
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
    		
    		inserted.push(rowid);
    		**/
        	//var rows = $(this).data('rows');
			var code = new Array();
			code.push(buildRow(record, rowid, opts));
			if(rowid == 0){
				$('tbody',this).prepend(code.join(''));
			}else{
				$('tbody tr:eq(' + (rowid - 1) + ')',this).after(code.join(''));
        	}
        },
        appendRow: function(record){
        	var trs = $('tbody tr', this);
        	//var allrow = $(this).data('allrow') || [];
        	var len = trs.length;
			this.grid('addRow', len, record);
        },
        deleteRow: function(rowindex){
        	$('tbody tr:eq(' + rowindex + ')',this).remove();
        	var rows = $(this).data('allrow');
        	var len = rows.length;
        	var filterdata = [];
        	for(var i = 0; i < len; i++){
        		var r = rows[i];
        		if(r['_r'] != 3){
        			filterdata.push(r);
        		}
        	}
        	var rowData = filterdata[rowindex];
        	var _r = rowData['_r'];
        	if(_r && _r == 1){
        		rows.splice(rowindex, 1);
        	}else if(!_r || _r == 2){
        		rowData['_r'] = 3;
        	}
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
			    		var input = $('<input type="text" id="' + rowindex + '_' + field + '" class="form-control"/>');
			    		//input.width(cell.width())
			    		input.val(v0).appendTo(cell);
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
						ctrhtml.push('<input type="text" class="form-control" id="', rowindex, '_', field, '" />');
			    		var ctr = $(ctrhtml.join(''));
			    		ctr.val(v0).appendTo(cell);
			    		ctr.datepicker();
					}else if(editor.type == 'selecttree'){
						var ctrhtml = [];
						ctrhtml.push('<input type="text" id="', rowindex, '_', field, '"/>');
			    		var ctr = $(ctrhtml.join(''));
			    		ctr.width(cell.width() - 2*padding).appendTo(cell);
			    		ctr.selecttree(editor.option);
			    		ctr.selecttree('setValue', v0);
					}else if(editor.type == 'selectbox'){
						var ctrhtml = [];
						ctrhtml.push('<input class="form-control" type="text" id="', rowindex, '_', field, '"/>');
			    		var ctr = $(ctrhtml.join(''));
			    		ctr.click(function() {
							return false;
						});
			    		ctr.width(cell.width() - 2*padding);
			    		ctr.appendTo(cell);
			    		ctr.selectbox(editor.option);
			    		ctr.selectbox('setValue', v0);
					}
    			}
        	});
        },
        saveRow: function(rowindex){
        	var opts = $(this).data('options');
        	var rowData = this.grid('getRowData', rowindex);
        	var newData = jQuery.extend(true, {}, rowData);
        	var tr = $("tbody tr", this).eq(rowindex);
        	var editing = tr.attr('editable');
        	var changed = false;
        	if(editing == 1){
        		tr.attr('editable', 0);
        		tr.removeClass('editing');
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
							}else if(editor.type == 'select'){
								var v = ctr.val();
					    		if(render){
					    			cell.html(render(v));
					    		}else{				    			
					    			cell.html(v);
					    		}
								if(v != newData[field]){
									changed = true;
									newData[field] = v;
								}
							}else if(editor.type == 'check'){
								var v = ctr.is(':checked');
					    		if(render){
					    			cell.html(render(v));
					    		}else{				    			
					    			cell.html('' + v);
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
	        	/**var updated = $(this).data('updated');
	        	var inserted = $(this).data('inserted');
	        	//updated[rowindex] = newData;
	        	if($.inArray(rowindex, inserted) == -1){
	        		updated.push(rowindex);
	        	}**/
	        	if(!newData['_r']){
	        		newData['_r'] = 2;
	        	}
	        	$(this).data('allrow')[rowindex] = newData;
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
        	ret['inserted'] = [];
        	ret['deleted'] = [];
        	ret['updated'] = [];
        	var allrow = $(this).data('allrow');
        	for(var k in allrow){
        		var d = allrow[k];
        		if(d['_r']){
        			if(d['_r'] == 1){
        				ret['inserted'].push(d);
        			}
        			if(d['_r'] == 2){
        				ret['updated'].push(d);
        			}
        			if(d['_r'] == 3){
        				ret['deleted'].push(d);
        			}
        		}
        	}
        	return ret;
        },
        setQueryParam : function(param){
        	var opts = $(this).data('options');
        	opts.queryParam = param;
        },
        appendData : function(data){
        	var opts = $(this).data('options');
        	var allRow = $(this).data('allrow');
        	var html = buildRow(data, allRow.length, opts);
        	$('tbody', this).append(html);
        },
        getData : function(){
        	return $(this).data('allrow');
        }
	}; 
	function ds(url, param, processData){
		var ret = {total:0, data:[]};
		$.ajax({
			url:url,
			type: 'get',
			cache: false,
			async: false,
			data: param,
			dataType: 'json',
	        success: function(result){
	        	if(processData){
	        		ret = processData(result);
	        	}else{    		
	        		ret = result;
	        	}
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
		if(total > 0){
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
		}
		return code.join('');
	}
	
	
	function body(records, opts){
		var trs = new Array();
		if(records.length > 0){			
			$(records).each(function(index){
				trs.push(buildRow2(this, index, opts));
			});
		}else{
			//code.push("没有数据");
		}
		return trs;
	}
	function buildRow(record, index, opts){
		var colspan = opts.columns.length;
		var code = new Array();
		var span = $('<span>');
		code.push("<tr class='gridrow' idx='", index, "'");
		if((index + 1) % 2 == 0){
			//code.push(" class='strips'");
		}
		code.push(" editable=0>");
		if(opts.sm){
			if(opts.sm == 'm'){
				code.push("<td  class='sm'><div class='multi unchecked'></div></td>");
			}
			if(opts.sm == 's'){
				code.push("<td  class='sm'><div class='single unchecked'></div></td>");
			}
			colspan++;
		}
		if(opts.linenum){
			code.push("<td class='linenum'>", index + 1, "</td>");
			colspan++;
		}
		if(opts.detailview){
			code.push("<td align='center'><div class='expander collapse'></div></td>");
			colspan++;
		}
		$(opts.columns).each(function(){
			var f = this.field;
			var align = this.align;
			var tip = this.tip;
			var render = this.render;
			var w = this.width;
			var editor = this.editor;
			var v = record[f]==undefined?'':record[f];
			code.push("<td class='gridcell'");
			if(f){
				code.push(" field='", f, "'");
			}
			if(tip){
				code.push(" title='" + encode(span.text(v).html()) + "'");
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
				var cellvalue = render(v, record, index);
				code.push(cellvalue);
			}else{	
				var cellvalue = span.text(v).html();
				code.push(cellvalue);
			}
			//code.push("</span>");
			if(editor){
				//code.push("<div class='editorDiv' style='display:none;'></div>");
			}
			code.push("</td>");
		});
		code.push("</tr>");
		if(opts.detailview){
			code.push("<tr class='detailrow' style='display:none'><td colspan='", colspan, "'></td></tr>");
		}
		span.remove();
		return code.join('');
	}
	function buildRow2(record, index, opts){
		var trs = new Array();
		var colspan = opts.columns.length;
		var span = $('<span>');
		var tr = $('<tr class="gridrow" editable="0" idx="' + index + '">');
		if(opts.sm){
			if(opts.sm == 'm'){
				var td = $("<td  class='sm'><div class='multi unchecked'></div></td>");
				tr.append(td);
			}
			if(opts.sm == 's'){
				var td = $("<td  class='sm'><div class='single unchecked'></div></td>");
				tr.append(td);
			}
			colspan++;
		}
		if(opts.linenum){
			var td = $('<td class="linenum">').text(index + 1);
			tr.append(td);
			colspan++;
		}
		if(opts.detailview){
			var td = $("<td align='center'><div class='expander collapse'></div></td>");
			tr.append(td);
			colspan++;
		}
		$(opts.columns).each(function(){
			var f = this.field;
			var align = this.align;
			var tip = this.tip;
			var tipRender = this.tipRender;
			var render = this.render;
			var w = this.width;
			var editor = this.editor;
			var v = eval('record.' + f);
			v = v==undefined?'':v;
			var code = new Array();
			code.push("<td class='gridcell'");
			if(f){
				code.push(" field='", f, "'");
			}
			if(tip == false){
				
			}else{
				if(tipRender){
					var tipvalue = tipRender(v, record, index);
					code.push(" title='" + encode(span.text(tipvalue).html()) + "'");
				}else{
					code.push(" title='" + encode(span.text(v).html()) + "'");
				}
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
			var td = $(code.join(''));
			if(render){
				var cellvalue = render(v, record, index);
				td.append(cellvalue);
			}else{	
				var cellvalue = span.text(v).html();
				td.text(cellvalue);
			}
			tr.append(td);
		});
		trs.push(tr);
		if(opts.detailview){
			var tr = $('<tr class="detailrow" style="display:none"><td colspan="' + colspan + '"></td></tr>');
			trs.push(tr);
		}
		span.remove();
		return trs;
	}
	function header(opts){
		var code = new Array();
		
		code.push("<thead><tr>");
		if(opts.multiselect){
			code.push("<th align=center width=20 class='checkbox checkall'><input type='checkbox'></th>");
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
		//code.push("<table class='table1' style='border:none;'>");
		code.push("<thead><tr>");
		if(opts.sm){
			if(opts.sm == 'm'){
				code.push("<th class='sm'><div class='multi unchecked'></div></th>");
			}
			if(opts.sm == 's'){
				code.push("<th class='sm'><div></div></th>");
			}
		}
		if(opts.linenum){
			code.push("<th class='linenum'>#</th>");
		}
		if(opts.detailview){
			code.push("<th width='20'></th>");
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
				code.push(" style='text-align:" + align + "'");
			}
			code.push(">");
			//code.push("<span class='grid-resize' style='cursor: col-resize;'>&nbsp;</span>");
			code.push(h);
			code.push("</th>");
		});
		//code.push("<th align=center width=6 class='scrollbar'></th>");
		code.push("</tr>");
		//code.push("</thead></table>");
		return code.join('');
	}
	function encode(str){
		return str.replace(/'/g, "&#39;");
	}
	function drog(table){            
        var tdr = $(table).find('thead tr .grid-resize');            
        $(tdr).mousedown(function(event){
        	event.stopPropagation();
        	var th = $(this).parent('th');
        	var tr = th.parent('tr');
        	var width = th.width();  
        	var x0 = event.clientX;  
            $(tr).mousemove(function(e){
            	e.stopPropagation();
            	$('body').addClass('noselected');
            	var px = 0;
                var x1 = e.clientX;
                if(x1 > x0){
                	px = x1 - x0;
                	var d = Math.min(800, (width + px));
                	$(th).width(d);
                }else{
                	px = x0 - x1;
                	var d = Math.max(100, (width - px));
                	$(th).width(d);
                }
            });  
              
            $(document).mouseup(function(){  
            	$('body').removeClass('noselected');
            	$(tr).unbind('mousemove');    
            });   
        });  
    }  
})(jQuery);