$DOC = {
	init: function(){
		util.render('html/docnav.html', 'sidebar');
		$('body').removeClass('hide-aside');
		util.render('html/1.html', 'rui-content');
		$('.rui-tree a').click(function(){
			var M = $(this).attr('href').substring(1);
			$('#loading').show();
			$JONE[M].init();
		});
	}
};