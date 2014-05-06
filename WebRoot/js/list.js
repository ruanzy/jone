(function($){        
	$.fn.List = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.List.methods = {
		init: function(options) {
			var defaults = {
				url: null,
				data: [],
				pager: true,
				condition: null
			};
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				$(this).data('options', settings);
				var me = $(this).addClass('list').empty();
				var data = new Array();
				if(settings.url){
					me.data('url', settings.url);
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
				me.data('ds', data);
				me.data('total', data.total);
				me.data('page', 1);
				var html = new Array();
				for(var k =0; k < data['data'].length; k++){
					html.push('<li>');
					if(settings.render){
						html.push(settings.render(data['data'][k]));
					}
					html.push('</li>');
				}
				if(settings.pager){
					var total = parseInt(me.data('total'));
					html.push("<br><p><div class='pagebar'>");
					html.push(pager(total, 1, 10));
					html.push("</div></p>");
				}
				me.append(html.join(''));
				
				me.find('.pagebar').live('click', function(e){
					var t = $(e.target);
					if(t.hasClass('pn')){
						var p = parseInt(t.text());
						var params = {page:p,pagesize:10};
						if(settings.condition){
							if($.isFunction(settings.condition)){
								params = $.extend(params, settings.condition());
							}
						}
						var dd = ds(settings.url, params);
						me.data('page', p);
						me.empty();				
						var html = new Array();
						for(var k =0; k < dd['data'].length; k++){
							html.push('<li>');
							if(settings.render){
								html.push(settings.render(dd['data'][k]));
							}
							html.push('</li>');
						}
						if(settings.pager){
							var total = parseInt(dd['total']);
							html.push("<br><p><div class='pagebar'>");
							html.push(pager(total, p, 10));
							html.push("</div></p>");
						}
						me.append(html.join(''));
					}
				});
			});
        },
        reload: function(params){
        	return this.each(function(){
        		var opts = $(this).data('options');
        		var url = opts.url;
				var p = {page:1, pagesize:10};
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
				$(this).empty();				
				var html = new Array();
				for(var k =0; k < dd['data'].length; k++){
					html.push('<li>');
					if(opts.render){
						html.push(opts.render(dd['data'][k]));
					}
					html.push('</li>');
				}
				if(opts.pager){
					var total = parseInt(dd['total']);
					html.push("<br><p><div class='pagebar'>");
					html.push(pager(total, 1, 10));
					html.push("</div></p>");
				}
				$(this).append(html.join(''));
        	});
        },
        refresh: function(params){
        	return this.each(function(){
        		var opts = $(this).data('options');
        		var url = opts.url;
        		var page = parseInt($(this).data('page'));
				var p = {page:page, pagesize:10};
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
				$(this).empty();				
				var html = new Array();
				for(var k =0; k < dd['data'].length; k++){
					html.push('<li>');
					if(opts.render){
						html.push(opts.render(dd['data'][k]));
					}
					html.push('</li>');
				}
				if(opts.pager){
					var total = parseInt(dd['total']);
					html.push("<br><p><div class='pagebar'>");
					html.push(pager(total, page, 10));
					html.push("</div></p>");
				}
				$(this).append(html.join(''));        		
        	});
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
			code.push("</span>条记录&nbsp;&nbsp;");
			code.push("<span class=disabled>&laquo;&nbsp;上一页</span>");
			for(var k = 1; k <= pagecount; k++){				
				code.push("<span class='pn");
				if(page == k){
					code.push(" current");
				}
				code.push("'>");
				code.push(k);
				code.push("</span>");
			}
			code.push("<span class=disabled>下一页&nbsp;&raquo;</span>");
		}else{
			code.push("<font color=red>没有符合条件的数据</font>");
		}
		return code.join('');
	}
})(jQuery);