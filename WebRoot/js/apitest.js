$APITEST = {
	init: function(){
		var me = this;
		util.render('html/apitest.html', 'rui-content');
		$('body').addClass('hide-aside');
		$('#request').tabs({
			tabs: [
				{
					   title: '请求参数',
					   url: 'html/apitest/params.html',
					   open: function(){
							$('#addParam').on('click', function(){
								me.addParam();
							});
					   }
				},
				{
					title: 'Headers',
					url: 'html/apitest/headers.html',
					open: function(){
						$('#addHeader').on('click', function(){
							me.addHeader();
						});
					}
				}
			]
		});
		$('#btntest').on('click', function(){
			//me.test();
		});
	}
};