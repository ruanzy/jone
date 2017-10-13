$.widget("ui.dialog", $.ui.dialog, {
	close : function() {
		this.element.remove();
	}
});
$.widget("custom.dropdown", {
	options : {
		
	},
	_create : function() {
		this.element.on('click', function(e) {
			var menu = $(this).siblings('.dropdown-menu').toggle();
			menu.on('click', 'li', function(e) {
				e.stopPropagation();
			});
			$(document).on('mouseup', function(e) {
				menu.hide();
			});
		});
	},
	_destroy : function() {

	},
});
$.widget("ui.tabs", $.ui.tabs,
{
	exists : function(title) {
		var el = this.element.find('.ui-tabs-anchor:contains('
				+ title + ')');
		return el.length > 0;
	},
	select : function(title) {
		var el = this.element;
		var as = el.find('.ui-tabs-anchor');
		var a = el.find('.ui-tabs-anchor:contains(' + title
				+ ')');
		var index = as.index(a);
		el.tabs("option", "active", index);
	},
	add : function(opt) {
		var el = this.element;
		var len = el.find(".ui-tabs-tab").length;
		var title = opt.title;
		var path = opt.path;
		var content = opt.content;
		//var padding = opt.padding;
		var closable = opt.closable;
		var href = 'tab_' + len;
		var tabTemplate = "<li path='#{path}'><a href='#{href}'>#{title}</a>";
		if (closable) {
			tabTemplate += " <span class='ui-icon ui-icon-close' role='presentation'></span>";
		}
		tabTemplate += "</li>";
		var li = $(tabTemplate.replace(/#\{path\}/g, path)
				.replace(/#\{href\}/g, '#' + href).replace(
						/#\{title\}/g, title));
		el.find(".ui-tabs-nav").append(li);
		var tabContentHtml = $('<div id="' + href + '">'
				+ content + "</div>");
		el.append(tabContentHtml);
		el.tabs("refresh");
		el.tabs("option", "active", len);
		el.on("click", "span.ui-icon-close", function() {
			var panelId = $(this).closest("li").remove().attr(
					"aria-controls");
			$("#" + panelId).remove();
			el.tabs("refresh");
		});
	}
});
/**
$.extend({
	alert : function(msg, callback) {
		var d = $("<div>").html(msg);
		return d.dialog({
			title : '提示',
			width : 350,
			modal : true,
			buttons : {
				"确定" : function() {
					d.dialog("close");
					callback && callback.call(d);
				}
			},
		});
	},
	confirm : function(msg, callback) {
		var d = $("<div>").html(msg);
		return d.dialog({
			title : '确认',
			width : 350,
			// dialogClass: "ui-dialog2",
			// classes: {
			// "ui-dialog-titlebar-close": "close"
			// },
			modal : true,
			buttons : {
				"确定" : function() {
					d.dialog("close");
					callback && callback.call(d, true);
				},
				'取消' : function() {
					d.dialog("close");
					callback && callback.call(d, false);
				}
			},
		});
	},
	dialog : function(option) {
		var d = $("<div>").html(option.content);
		option.modal = true;
		return d.dialog(option);
	}
});
**/