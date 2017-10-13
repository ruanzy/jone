(function($) {
    $.fn.pagination = function(method) {
        if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
    $.fn.pagination.defaults = {
    	page: 1,
        pagesize: 10,
        pagesizeList: [5, 10, 20, 50],
        totalName: 'total',
		total: 0,
		rows: [],
        edge: 1,
        display: 3,
        callback: function(data){}
    };
    $.fn.pagination.methods = {
        init: function(option) {
            var _option = $.extend({}, $.fn.pagination.defaults, option);
            return this.each(function() {
                var me = $(this);
                me.data('option', _option);
                me.on('click', {
                    page: me
                },
                function(event) {
                    eventHandler(event);
                }).on('change', {
                    page: me
                },
                function(event) {
                    eventHandler(event);
                });
				me.pagination('remote', 1);
            });
        },
        option: function() {
            return this.data['option'];
        },
		remote: function(page, param) {
			var $this = this;
			var option = this.data('option');
			var totalName = option['totalName'];
			var pagesize = option['pagesize'];
			var pagesizeList = option['pagesizeList'];
			var url = option['url'];
			var edge = option['edge'];
			var display = option['display'];
			var callback = option['callback'];
			var page = page || 1;
			var params = {
				page: page,
				pagesize: pagesize
			};
			$.extend(params, param);
			if (url) {
				$.ajax({
					url: url,
					type: 'post',
					dataType: 'json',
					data: params,
					success: function(data) {
						var total = data[totalName];
						if(total == undefined){
							alert('返回数据不符合格式');
						}else{
							callback && callback(data, params);
							var html = getPage(total, pagesize, pagesizeList, page, display, edge);
							$this.html(html);
							//$("#page").append(getPagesize(pagesize, pagesizeList));
							//$("#page").append(getInfo(total, current_page, pc));
							//$("#page").data('page', current_page);
							option['total'] = total;
							option['page'] = page;							
						}
					},
					error: function(data) {
						console.log(data);
					}
				});
			}else{
				var d = option['data'];
				var start = (page - 1)*pagesize;
				var end = page*pagesize;
				var data = {
					total: d.total,
					data: d.slice(start,end)
				};
				callback && callback(data, params);
				var html = getPage(data.total, pagesize, pagesizeList, page, display, edge);
				$this.html(html);
				//$("#page").append(getPagesize(pagesize, pagesizeList));
				//$("#page").append(getInfo(total, current_page, pc));
				//$("#page").data('page', current_page);
				option['total'] = total;
				option['page'] = page;
			}
		}
	};
    
    function interval(page, pc)
	{
    	var arr = [];
		arr[0] = Math.max(Math.min((page - 2), (pc - 5)), 1);
		arr[1] = Math.min(Math.max((page + 2), 5), pc);
		return arr;
	}
    
    function getPage(total, pagesize, pagesizeList, current_page, num_display_entries, num_edge_entries) {
    	var html = [];
    	var pc = Math.ceil(total / pagesize);
		if(pc > 1){
			html.push('<div class="page-bar">');
			html.push('<span class="page-prev');
			if (current_page == 1) {
				html.push(' disable');
			}
			html.push('">&laquo;</span>');
			var arr = interval(current_page, pc);
			if (arr[0] == 2)
			{
				html.push('<span class="page-item');
				if(current_page == 1){
					html.push(' active');
				}
				html.push('">1</span>');
			}
			if (arr[0] > 2)
			{
				html.push('<span class="page-item');
				if(current_page == 1){
					html.push(' active');
				}
				html.push('">1</span>');
				html.push('<span class="hellip">&hellip;</span>');
			}
			for (var i = arr[0]; i <= arr[1]; i++)
			{
				html.push('<span class="page-item');
				if(current_page == i){
					html.push(' active');
				}
				html.push('">', i, '</span>');
			}
			if (arr[1] <= pc - 2)
			{
				html.push('<span class="hellip">&hellip;</span>');
			}
			if (arr[1] < pc)
			{
				html.push('<span class="page-item">', pc, '</span>');
			}
			html.push('<span class="page-next');
			if (current_page == pc) {
				html.push(' disable');
			}
			html.push('">&raquo;</span>');
			html.push('</div>');
		}
		return html.join('');
    }

    function getPagesize(pagesize, pagesizeList) {
        var arr = [];
        arr.push('<select>');
        for (var m = 0; m < pagesizeList.length; m++) {

            arr.push('<option value="', pagesizeList[m], '"');
            if (pagesize == pagesizeList[m]) {
                arr.push(' selected="selected"');
            }
            arr.push('>', pagesizeList[m], '</option>');
        }
        arr.push('</select>');
        return arr.join('');
    }

    function getInfo(total, cur, pagecount) {
        var info = '共 {total} 项 当前第 {cur} / {pagecount} 页';
        return info.replace('{total}', total).replace('{cur}', cur).replace('{pagecount}', pagecount);
    }
    function eventHandler(event) {
        var that = event.data.page;
        var option = that.data('option');
		var pc = Math.ceil(option.total / option.pagesize);
        var $target = $(event.target);
        if (event.type == 'click') {
            if ($target.hasClass('page-item')) {
                var v = $target.text();
                v = parseInt(v);
                that.pagination('remote', v);
            }
			if ($target.hasClass('page-prev')) {
				var p = option.page;
				if(p > 1){
					p = parseInt(p) - 1;
					that.pagination('remote', p);
				}
            }
			if ($target.hasClass('page-next')) {
				var p = option.page;
				if(p < pc){
					p = parseInt(p) + 1;
					that.pagination('remote', p);
				}
            }
        }
        if (event.type === 'change') {
            var pagesize = $target.val();
            option['pagesize'] = pagesize;
            option.page = 1;
            that.pagination('remote', 1);
        }
    }
})(jQuery);