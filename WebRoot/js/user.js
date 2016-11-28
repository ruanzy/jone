$USER = {
	init: function(){
		$('body').addClass('hide-aside');
		util.render('html/user.html', 'rui-content');
	}
};