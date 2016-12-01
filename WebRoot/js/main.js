require.config({
	paths : {
		jquery : "lib/jquery-1.11.0.min",
		bootstrap : "lib/bootstrap/js/bootstrap.min",
		template : "lib/template",
		util : "lib/util",
		dialog : "lib/dialog/dialog",
		grid : "lib/grid/grid",
		tabs : "lib/tabs/tabs",
		highlight : "lib/highlight/highlight.pack"
	},
	shim : {
		"bootstrap" : [ "jquery", "css!lib/bootstrap/css/bootstrap.min", "css!lib/bootstrap/fonts/font-awesome.min"],
		"highlight" : [ "css!lib/highlight/solarized-light"],
		"util" : {
			desp: [ "template"],
			exports:'util'
		},
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