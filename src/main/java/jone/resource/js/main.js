require.config({
	paths : {
		jquery : "lib/jquery-1.11.0.min",
		jqueryui : "lib/jqueryui/jquery-ui.min",
		jqueryuiext : "lib/jqueryui/jquery-ui.ext",
		bootstrap : "lib/bootstrap/js/bootstrap.min",
		artDialog : "lib/artDialog/dist/dialog-plus-min",
		artDialogext : "lib/artDialog/dist/dialog-ext-min",
		My97DatePicker : "lib/My97DatePicker/WdatePicker",
		i18n : "lib/jquery.i18n.properties.min",
		select2 : "lib/select2/select2.min",
		ztree : "lib/ztree/jquery.ztree.all.min",
		ztreeExt : "lib/ztree/jquery.ztree.exedit.min",
		form : "lib/jquery.form",
		validate : "lib/jquery.validate.min",
		webuploader : "lib/webuploader/webuploader.min",
		template : "lib/template",
		dialog : "lib/dialog/dialog",
		rdate : "lib/rdate/jquery.datetimepicker",
		grid : "lib/grid/rgrid",
		biggrid : "lib/grid/biggrid",
		tabs : "lib/tabs/tabs",
		highlight : "lib/highlight/highlight.pack",
		patch : "lib/patch",
		util : "lib/util",
		echarts : "lib/echarts/echarts.min",
		macarons : "lib/echarts/macarons",
		editableSelect : "lib/editableSelect/jquery-editable-select",
		jsonview : "lib/jsonview/dist/jquery.jsonview",
		bootstrapDialog : "lib/bootstrap-dialog/js/bootstrap-dialog",
		toastr : "lib/toastr/toastr.min",
		treegrid : "lib/treegrid/js/jquery.treegrid.min",
		metisMenu : "lib/metisMenu/metisMenu.min",
		pagination : "lib/pagination/mypagination",
		slimscroll : "lib/jquery.slimscroll.min",
		steps : "lib/jquery.steps.min",
		iCheck : "lib/iCheck/icheck.min"
	},
	shim : {
		"bootstrap" : [ "jquery", "css!lib/bootstrap/fonts/font-awesome.min"],
		"artDialog" : [ "jquery", "css!lib/artDialog/css/dialog"],
		"artDialogext" : [ "artDialog"],
		"bootstrapDialog" : [ "bootstrap", "css!lib/bootstrap-dialog/css/bootstrap-dialog"],
		"treegrid" : [ "jquery", "css!lib/treegrid/css/jquery.treegrid"],
		"toastr" : [ "jquery", "css!lib/toastr/toastr"],
		"jqueryui" : [ "jquery"],
		"pagination" : [ "jquery", "css!lib/pagination/mypagination"],
		"jqueryuiext" : [ "jqueryui", "css!lib/jqueryui/jquery-ui" ],
		"ztree" : [ "css!lib/ztree/metroStyle/metroStyle" ],
		"select2" : [ "jquery", "css!lib/select2/select2" ],
		"highlight" : [ "css!lib/highlight/github-gist"],
		"webuploader" : [ "css!lib/webuploader/webuploader", "css!../css/uploader"],
		"util" : [ "template" ],
		"patch" : [ "i18n"],
		"dialog" : [ "util", "css!lib/dialog/dialog"],
		"rdate" : [ "css!lib/rdate/jquery.datetimepicker"],
		"grid" : [ "jquery", "css!lib/grid/grid"],
		"biggrid" : [ "css!lib/grid/grid"],
		"tabs" : [ "util", "css!lib/tabs/tabs" ],
		"form" : [ "jquery", "validate" ],
		"slimscroll" : [ "jquery" ],
		"steps" : [ "jquery" ],
		"editableSelect" : [ "jquery", "css!lib/editableSelect/jquery-editable-select"],
		"jsonview" : ["css!lib/jsonview/dist/jquery.jsonview"],
		"metisMenu" : [ "css!lib/metisMenu/metisMenu", "css!lib/metisMenu/mm-vertical" ],
		"iCheck" : [ "jquery", "css!lib/iCheck/custom.css"],
		"echarts" : ["jquery"]
	},
	map: {
	  '*': {
	    'css': 'css.min'
	  }
	}
});
require(['bootstrap', 'treegrid', 'jqueryuiext', 'dialog', 'select2',  'grid', 'form', 'echarts', 'My97DatePicker', 'ztree', 'highlight', 'toastr', 'metisMenu', 'pagination', 'slimscroll', 'steps', 'iCheck'], function() {
	require(['patch', 'app/index', 'macarons'], function(_, index) {
		index.init();
	});
});