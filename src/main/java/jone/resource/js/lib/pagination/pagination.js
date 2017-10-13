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
        edge: 1,
        display: 3
    };
    $.fn.pagination.methods = {
        init: function(option) {
            var _option = $.extend({},
            $.fn.pagination.defaults, option);
            return this.each(function() {
                var me = $(this);
                me.data['option'] = _option;
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
				remote(_option);
            });
        },
        option: function() {
            return this.data['option'];
        }
    };
    function remote(option) {
    	var page = option['page'];
        var pagesize = option['pagesize'];
        var pagesizeList = option['pagesizeList'];
        var url = option['url'];
        var edge = option['edge'];
        var display = option['display'];
        var callback = option['callback'];
        var params = {
            page: page,
            pagesize: pagesize
        };
		if (url) {
			$.ajax({
				url: url,
				type: 'post',
				dataType: 'json',
				data: params,
				success: function(data) {
					var total = data[totalName];
					if(total){
						callback && callback(data, params);
						getPage(data.total, pagesize, pagesizeList, page, display, edge);
					}else{
						alert('返回数据不符合格式');
					}
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
			getPage(data.total, pagesize, pagesizeList, page, display, edge);
		}
    }
    function getPage(total, pagesize, pagesizeList, current_page, num_display_entries, num_edge_entries) {
        var arr = [];
        var pagecount = Math.ceil(total / pagesize);
        if (current_page == 1) {
            arr.push('<span class="current prev">Prev</span>');
        } else {
            arr.push('<a href="javascript:void(0)">Prev</a>');
        }
        if(current_page != 1 && current_page >= 4 && pagecount != 4){
        	arr.push('<a href="javascript:void(0)">1</a>');
		}
        if(current_page-2 > 2 && current_page <= pagecount && pagecount > 5){
        	arr.push('<span style="font-size: .625rem;color: #9b9b9b;padding:0;">...</span>');
		}
		var start = current_page -2,end = current_page+2;
		if((start > 1 && current_page < 4)||current_page == 1){
			end++;
		}
		if(current_page > pagecount-4 && current_page >= pagecount){
			start--;
		}
		for (;start <= end; start++) {
			if(start <= pagecount && start >= 1){
				if(start != current_page){
					arr.push('<a href="javascript:void(0)">', start, '</a>');
				}else{
					arr.push('<span class="current">', start, '</span>');
				}
			}
		}
		if(current_page + 2 < pagecount - 1 && current_page >= 1 && pagecount > 5){
			arr.push('<span style="font-size: .625rem;color: #9b9b9b;padding:0;">...</span>');
		}
		if(current_page != pagecount && current_page < pagecount -2  && pagecount != 4){
			arr.push('<a href="javascript:void(0)">', pagecount, '</a>');
		}
        if (current_page == pagecount) {
            arr.push('<span class="current next">Next</span>');
        } else {
            arr.push('<a href="javascript:void(0)">Next</a>');
        }
        $("#page").html(arr.join(''));
        $("#page").append(getPagesize(pagesize, pagesizeList));
        $("#page").append(getInfo(total, current_page, pagecount));
        $("#page").data('page', current_page);
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
        var option = that.pagination('option');
        var $target = $(event.target);
        if (event.type == 'click') {
            if ($(event.target).is('a')) {
                var v = $target.text();
                if (v == 'Next') {
                    v = parseInt($("#page").data('page')) + 1;
                } else if (v == 'Prev') {
                    v = parseInt($("#page").data('page')) - 1;
                } else {
                    v = parseInt(v);
                }
                option.page = v;
                remote(option);
            }
        }
        if (event.type === 'change') {
            var pagesize = $target.val();
            option['pagesize'] = pagesize;
            option.page = 1;
            remote(option);
        }
    }
})(jQuery);