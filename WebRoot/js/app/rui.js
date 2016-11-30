define(['util','highlight'], function(util, hljs){
var obj = {
	init: function(){
		util.render('html/ruinav.html', 'sidebar');
		$('body').removeClass('hide-aside');
		util.render('html/rui/dialog.html', 'rui-content');
		$('pre code').each(function(i, block) {
			hljs.highlightBlock(block);
		});
	}
};
return obj;
});