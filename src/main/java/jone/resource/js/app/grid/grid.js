define(['util'], function(util){
	var obj = {
		init: function(){
			util.render('html/grid.html', 'container');
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
			$('#table').bootstrapTable({
			    url: 'data.json'
			});
		}
	};
	return obj;
});