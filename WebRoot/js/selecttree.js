(function($) {
	$.fn.selecttree = function(method) {
		if (typeof method == 'string') {
			return arguments.callee.methods[method].apply(this,
					Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return arguments.callee.methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist');
		}
	};
	$.fn.selecttree.defaults = {
		url : null,
		change : function(item) {
		}
	};
	$.fn.selecttree.methods = {
		init : function(options) {
			var opts = $.extend({}, $.fn.selecttree.defaults, options);
			return this.each(function() {
				if (this.tagName != 'INPUT') {
					return;
				}
				var me = $(this).hide();
				var disabled = me.attr('disabled');
				var rdm = new Date().getTime() + '_'
						+ Math.floor(Math.random() * (100 - 1 + 1) + 1);
				var dl = $("<dl class='selecttree'><dt id='dt_" + rdm
						+ "'></dt><dd><ul class='ztree' style='overflow:auto;'></ul></dd><i class='icon-angle-down'></i><div class='mask'></div></dl>");
				var p1 = [];
				p1.push("<div class='text'></div>");
				me.wrap(dl).after(p1.join(''));
				var txt = me.siblings("div.text");
				var dt = me.parent("dt").width(me.outerWidth() - 2).height(me.outerHeight() - 2);
				var H = dt.outerWidth();
				var dd = dt.siblings("dd").width(H - 2);
				var mask = dt.siblings("div.mask");
				if (disabled) {
					mask.show();
				}
				var treeDom = dt.siblings("dd").find('ul.ztree');
				
				
				function initTree(url) {
					var setting = {
						view : {
							dblClickExpand : false,
							showLine : true
						},
						data : {
							simpleData : {
								enable : true,
								pIdKey : "pid",
								rootPId : 0
							}
						},
						callback : {
							onClick : function (event, treeId, treeNode, clickFlag) {
								var id = treeNode.id;
								var name = treeNode.name;
								me.val(id);
								txt.html(name);
								dd.hide();
								dt.removeClass('expand');
							}
						}
					};
					var treeNodes;
					$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						dataType : "json",
						url : url,
						success : function(data) {
							treeNodes = data;
						}
					});
					var treeObj = $.fn.zTree.init(treeDom, setting, treeNodes);
					treeObj.expandAll(true);
				}
				var url = opts.url;
				if (url) {
					initTree(url);
				}
				dt.bind("click", function(e) {
					$(this).addClass('expand');
					dd.show();
					e.stopPropagation();
				});
				dd.bind("click", function(e) {
					e.stopPropagation();
				});
				$(document).click(function() {
					dd.hide();
					dt.removeClass('expand');
				});
			});
		},
		val : function(val) {
			if (val) {
				var data = this.data('list');
				this.val(val);
				var txt = this.siblings("input");
				$(data).each(function(i) {
					var t = this.text;
					var v = this.value;
					if (v == val) {
						txt.val(t);
						return;
					}
				});
			} else {
				return this.val();
			}
		}
	};
})(jQuery);