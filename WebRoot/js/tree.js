(function($){        
	function buildNode(node, dep, path, last, end, settings) {
		var html = [];
		var text = node.name;
		var children = node.children;
		html.push("<li class='tree-node'", "path='", path, "'>");
		html.push("<div class='tree-node-view'>");
		for (var j = 0; j < dep; j++) {
			if(!end[j]){        		
				html.push("<span class='tree-node-indent'></span>");
			}else{
				html.push("<span class='node-indent-space'></span>");
			}
		}
		if (children) {
			if(last){
				html.push("<span class='tree-node-toggle elbow-end-plus collapse'></span>");
			}else{
				html.push("<span class='tree-node-toggle elbow-plus collapse'></span>");
			}
		} else {
			if(last){
				html.push("<span class='tree-node-elbow-end'></span>");
			}else{
				html.push("<span class='elbow'></span>");
			}
		}
		if(settings.showIcon){		
			html.push("<span class='tree-node-icon");
			if (children) {
				html.push(" folder'>");
			} else {
				html.push(" file'>");
			}
			html.push("</span>");
		}
		if(settings.checkbox){			
			html.push("<span id='", node.id, "_cb' class='tree-node-cb unchecked'></span>");
		}
		html.push("<span class='tree-node-text'>");
		html.push(text);
		html.push('(' + node.id + ')');
		html.push("</span>");
		if(settings.remove){			
			html.push("<span id='remove_", node.id, "' class='tree-node-remove' title='remove'></span>");
		}
		html.push("</div>");
		if (children) {			
			end.push(last);
			html.push("<ul class=hide level='", (dep + 1), "'>");
			for ( var k = 0, l = children.length; k < l; k++) {
				var p = path + '|' + children[k].id;
				html.push(buildNode(children[k], (dep + 1), p, k == (l - 1), end, settings));
			}
			html.push("</ul>");
			end.pop();
		}
		html.push("</li>");
		return html.join("");
	}
	$.fn.Tree = function(method) {
		if (typeof method == 'string') {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return arguments.callee.methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };
	$.fn.Tree.methods = {
		init: function(options) {
			var defaults = {
				data: null,
				url: null,
				checkbox: false,
				showIcon: true,
				onload: null,
				contextmenu:null
			};
			var settings = $.extend({}, defaults, options);
			return this.each(function(){
				$(this).data('options', settings);
				var el = $(this).attr('level', 0).empty();
				var html = new Array();
				var ds = new Array();
				if(settings.url){
					$.ajax({
						url: settings.url,
						type: 'post',
						cache: false,
						async: false,
						dataType: 'json',
				        success: function(result){
				        	ds = p2s(result);
						}
					});
				}else{
					ds = settings.data;
				}
				for(var k = 0, len = ds.length; k < len; k++){
					html.push(buildNode(ds[k], 0, ds[k].id, k == (len - 1), [],  settings));
				}
				el.append(html.join(""));
				if(settings.onload){
					settings.onload();
				}
				$('.tree-node-view', el).click(function(e) {
					var t = $(e.target);
					if(t.hasClass('tree-node-text')){
						$('.tree-node-selected', el).removeClass('tree-node-selected');
						t.addClass('tree-node-selected');
					}
					if(t.hasClass('tree-node-toggle')){
						t.parent().siblings('ul').toggle();
						var icon = t.siblings('.tree-node-icon');
						if (t.hasClass('expand')) {
							t.removeClass('expand').addClass('collapse');
							if (t.hasClass('elbow-end-minus')) {
								t.removeClass('elbow-end-minus').addClass('elbow-end-plus');
							}else{
								t.removeClass('elbow-minus').addClass('elbow-plus');
							}
							icon.removeClass('folder-open').addClass('folder');
						} else {
							t.removeClass('collapse').addClass('expand');
							if (t.hasClass('elbow-end-plus')) {
								t.removeClass('elbow-end-plus').addClass('elbow-end-minus');
							}else{
								t.removeClass('elbow-plus').addClass('elbow-minus');
							}
							icon.removeClass('folder').addClass('folder-open');
						}
					}
				});
				$('.tree-node-view', el).dblclick(function(e) {
					var t = $(e.target);
					if(t.hasClass('tree-node-text')){
						t.parent().siblings('ul').slideToggle(300);
						var icon = t.siblings('.tree-node-icon');
						var toggle = t.siblings('.tree-node-toggle');
						if (toggle.hasClass('expand')) {
							toggle.removeClass('expand').addClass('collapse');
							if (toggle.hasClass('elbow-end-minus')) {
								toggle.removeClass('elbow-end-minus').addClass('elbow-end-plus');
							}else{
								toggle.removeClass('elbow-minus').addClass('elbow-plus');
							}
							if (icon.hasClass('folder-open')) {
								icon.removeClass('folder-open').addClass('folder');
							}
						} else {
							toggle.removeClass('collapse').addClass('expand');
							if (toggle.hasClass('elbow-end-plus')) {
								toggle.removeClass('elbow-end-plus').addClass('elbow-end-minus');
							}else{
								toggle.removeClass('elbow-plus').addClass('elbow-minus');
							}
							if (icon.hasClass('folder')) {
								icon.removeClass('folder').addClass('folder-open');
							}
						}
					}
				});
				$('.tree-node-remove', el).click(function(e) {
					var nodeid = $(this).attr('id').substring(7);
					$.confirm({msg:'确定要干掉它吗?<font color=red>(注意:如果该节点有后代节点将一并被删除)<font>',callback:function(btn){
						if(btn){
							settings.remove(nodeid);
						}
					}});
					
				});
				function bubble(li) {
					var flag = false;
					var siblings = li.siblings('li');
					$(siblings).each(
							function() {
								var cp = $(this).attr('path');
								var checked = $("li[path='" + cp + "']").children('div').children(
										'.tree-node-cb').hasClass('checked');
								if (checked) {
									flag = true;
									return;
								}
							});
					if (!flag) {
						var path = li.attr('path');
						var idx = li.attr('path').lastIndexOf('|');
						if (idx != -1) {
							var pp = path.substring(0, idx);
							var pli = $("li[path='" + pp + "']");
							if (pli) {
								pli.children('div').children('.tree-node-cb').removeClass('checked')
										.addClass('unchecked');
								bubble(pli);
							}
						}
					}
				}
				$('.tree-node-cb').click(
					function() {
						var li = $(this).parent().parent('li');
						if ($(this).hasClass('checked')) {
							//所有的后代节点取消选中
							li.find('.tree-node-cb')
									.removeClass('checked').addClass('unchecked');
							//若兄弟节点都已取消选中,则父节点取消选中.递归直到root节点
							bubble(li);
						} else {
							//所有的后代节点被选中
							li.find('.tree-node-cb')
									.removeClass('unchecked').addClass('checked');
							//祖代节点被选中
							li.parents('li').children().children('.tree-node-cb')
							.removeClass('unchecked').addClass('checked');
						}
					}
				);
			});
        },
        expand: function(level){
        	for(var k = 0; k <= level; k++){
        		var ul = $('ul[level=' + k + ']', this).show();
        		var t = $('.tree-node-toggle', ul);
        		var icon = $('.tree-node-icon', ul);
        		if (t.hasClass('collapse')) {
        			t.removeClass('collapse').addClass('expand');
        			if (t.hasClass('elbow-end-plus')) {
        				t.removeClass('elbow-end-plus').addClass('elbow-end-minus');
        			}else{
        				t.removeClass('elbow-plus').addClass('elbow-minus');
        			}
        			if (icon.hasClass('folder')) {
        				icon.removeClass('folder').addClass('folder-open');
        			}
        		}
        	}
        },
        expandAll: function(){
        	$('.tree-node-toggle', this).each(function(){
        		if($(this).hasClass('collapse')){
        			this.click();
        		}
        	});        		
        },
        checkedNodes: function(){
        	var ret = [];
        	$('.tree-node-cb.checked', this).each(function(){
        		var id = $(this).attr('id');
        		var len = id.length - 3;
        		ret.push(id.substring(0, len));
        	});
        	return ret;
        },
        checked: function(arr){
        	$(arr).each(function(){
        		var id = this + '_cb';
        		$('#' + id).removeClass('unchecked').addClass('checked');
        	});
        },
        refresh: function(){
        	var option = $(this).data('options');
        	$(this).Tree('init', option);
        }
	}; 
})(jQuery);
function p2s(data) {
	var i,l;
	if (!data) return [];
	var r = [];
	var t = [];
	for (i=0, l=data.length; i<l; i++) {
		t[data[i]["id"]] = data[i];
	}
	for (i=0, l=data.length; i<l; i++) {
		if (t[data[i]["pid"]]) {
			if (!t[data[i]["pid"]]["children"])
				t[data[i]["pid"]]["children"] = [];
			t[data[i]["pid"]]["children"].push(data[i]);
		} else {
			r.push(data[i]);
		}
	}
	return r;
}