define(['util'], function(util){
	var obj = {
		init: function(){
			var me = this;
			util.render('html/demo.html', 'container');
			var H = $(window).height();
			$("#dbtree .panel-body").height(H - 50 - 40);
			$("#main").height(H - 50 - 40 -30);
			$(window).resize(function() {
				var H = $(window).height();
				$("#dbtree .panel-body").height(H - 50 - 40);
				$("#main").height(H - 50 - 40 -30);
				$("#accordion").accordion("refresh");
			});
			var icons = {
				      header: "ui-icon-plus",
				      activeHeader: "ui-icon-minus"
				    };
			$("#accordion").accordion({
				icons: icons,
				heightStyle: "fill"
			});	
			$("#accordion ul > li > a").on('click', function(){
				var title = $(this).text();
				var href = $(this).attr('rel');
				var path = $(this).attr('path');
				breadcrumb(title, path);
			});
			var tabs = $("#tabs").tabs({
				active: 0,
				activate: function(event, ui) {
					var init = ui.newTab.attr("init");
					if(!init){
						var path = ui.newTab.attr("path");
						if(path){
							require([path], function(o) {
								o.init();
								ui.newTab.attr("init", true);
							});
						}
					}
				}
			});
			function breadcrumb(text, path){
				$('#breadcrumb').html(text);
				require([path], function(o) {
					o.init();
				});
			}
			function addTab(title, href, path) {
				//var title = t + '@' + db;
				var exists = $("#tabs").tabs("exists", title);
				if(exists){
				  $("#tabs").tabs("select", title);
				  return;
				}
				var content = util.tpl(href);
			  	var opt = {
			      title: title,
			      content: content,
			      path: path,
			      closable: true
			  	};
			  	$("#tabs").tabs("add", opt);
			}
		},
		list: function(){
			var me = this;
			$('#datalist').grid({
				url : 'user/list',
				columns: [
					{ header: util.i18n('SYS_USER_USERNAME'), field: 'username'},
					{ header: util.i18n('SYS_USER_REGTIME'), field: 'regtime', align: 'center', width: 200},
					{ header: util.i18n('OPERATE'), field: 'op', render : opRender, align: 'center', width: 200}
				]
			});
			function stateRender(v){
				return (v == 1)? ('<label class="label label-success">正常 </label>'): ('<label class="label label-danger">注销</label>');
			}
			function opRender(v, r){
				var state = r.state;
				var name = r.username;
				var op = new Array();
				if(state == 1){
					var setrole = $('<a href="javascript:void(0);">' + util.i18n('SYS_USER_SETROLE') + '</a>');
					setrole.on('click', function(){
						me.setRole(name);
					});
					var space1 = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
					var pwd = $('<a href="javascript:void(0);">' + util.i18n('SYS_USER_RESETPWD') + '</a>');
					pwd.on('click', function(){
						me.resetpwd(name);
					});
					var space2 = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
					var del = $('<a href="javascript:void(0);">' + util.i18n('DELETE') + '</a>');
					del.on('click', function(){
						me.del(name);
					});
					op.push(setrole, space1, pwd, space2, del);
				}
				return op;
			}
		}
	};
	return obj;
});