define([ 'util' ], function(util) {
	var obj = {
		init : function() {
			util.render('html/dialog.html', 'container');
			$('pre code').each(function(i, block) {
				hljs.highlightBlock(block);
			});
			$('.nav-tabs').on('click', 'li', function(e) {
				e.preventDefault();
				$(this).parent("ul").find("li").removeClass('active');
				$(this).addClass('active');
				var id = $(this).find("a").attr('href');
				$('.tab-content div').hide();
				$('.tab-content').find(id).show();
			});
			$('#alert').on('click', function(e) {
				$.alert('哈喽, 哈喽!', function() {
					alert('123');
				});

			});
			$('#confirm').on('click', function(e) {
				$.confirm('确定要干掉它吗?', function(v) {
					if (v) {
						alert('yes');
					} else {
						alert('no');
					}
				});
			});
			$('#dialog').on('click', function(e) {
				$.dialog({
					title : '自定义按钮',
					width : 300,
					content : 'html',
					buttons: [
				         {
				        	 text: '同意',
				        	 click: function () {
				                 this.title('你同意了');
				             }
				         },
				         {
				        	 text: '不同意',
				        	 click: function () {
				                 alert('你不同意');
				             }
				         }
				    ]
				});
			});
		}
	};
	return obj;
});