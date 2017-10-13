define(['util'], function(util){
	var obj = {
		init: function(){
			util.render('html/treegrid.html', 'container');
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
			$('#treegrid').treegrid();
		}
	};
	return obj;
});