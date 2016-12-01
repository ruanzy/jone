require.config({
	paths : {
		jquery : "lib/jquery-1.11.0.min",
		bootstrap : "lib/bootstrap/js/bootstrap.min",
		util : "lib/util",
		dialog : "lib/dialog/dialog",
		tabs : "lib/tabs/tabs",
		highlight : "lib/highlight/highlight.pack"
	},
	shim : {
		"bootstrap" : [ "jquery" ],
		"tabs" : [ "util" ],
		"jquery.validate.min" : [ "jquery" ]
	}
});
require(['bootstrap'], function(index) {
	require(['app/index'], function(index) {
		index.init();
	});
});