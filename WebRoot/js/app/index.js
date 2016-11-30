define(['util', 'dialog'], function(util){
	var obj = {
		init: function(){
			util.render('html/header.html', 'header');
			util.render('html/footer.html', 'footer');
			this.bindEvent();
		},
		bindEvent: function(){
			$('#m_doc').on('click', function(){
				require(['app/doc'], function(doc) {
					doc.init();
				});
			});
			$('#m_apitest').on('click', function(){
				require(['app/apitest'], function(apitest) {
					apitest.init();
				});
			});
			$('#m_rui').on('click', function(){
				require(['app/rui'], function(rui) {
					rui.init();
				});
			});
			$('#m_doc').trigger('click');
			$('#about').on('click', function(){
				$.noty('增加成功!');
			});
		}
	};
	return obj;
});