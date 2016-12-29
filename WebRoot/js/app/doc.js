define(['util', 'highlight', 'app/task'], function(util, hljs, task){
	var obj = {
		init: function(){
			util.render('html/docnav.html', 'sidebar');
			$('body').removeClass('hide-aside');
			util.render('html/doc.html', 'rui-content');
			$('#loading').hide();
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
			task.init();
		}
	};
	return obj;
});