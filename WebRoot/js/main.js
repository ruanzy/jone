require.config({
	paths : {
		jquery : "lib/jquery-1.11.0.min",
		bootstrap : "lib/bootstrap/js/bootstrap.min",
		i18n : "lib/jquery.i18n.properties.min",
		ztree : "lib/ztree/jquery.ztree.all.min",
		template : "lib/template",
		dialog : "lib/dialog/dialog",
		grid : "lib/grid/grid",
		tabs : "lib/tabs/tabs",
		highlight : "lib/highlight/highlight.pack",
		util : "lib/util",
	},
	shim : {
		"bootstrap" : [ "jquery", "css!lib/bootstrap/css/bootstrap.min", "css!lib/bootstrap/fonts/font-awesome.min"],
		"ztree" : [ "css!lib/ztree/zTreeStyle/zTreeStyle"],
		"highlight" : [ "css!lib/highlight/solarized-light"],
		"util" : [ "i18n", "template" ],
		"dialog" : [ "util", "css!lib/dialog/dialog"],
		"grid" : [ "css!lib/grid/grid"],
		"tabs" : [ "util", "css!lib/tabs/tabs" ],
		"jquery.validate.min" : [ "jquery" ]
	},
	map: {
	  '*': {
	    'css': 'css.min'
	  }
	}
});
require(['bootstrap'], function() {
	require(['app/index'], function(index) {
		index.init();
	});
});