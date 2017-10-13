define(['util'], function(util){
	var obj = {
		init: function(){
			util.render('html/start.html', 'container');
			$("#myModal").on("click", function() {
				var html = util.tpl('html/remote2.html', {username : 'ruanzy'});
				//$.alert('aaa', function(){alert('哈哈');});
				/**$.confirm('aaa', function(v){
					if(v == 'yes'){alert('哈哈');}
					else{alert('呵呵');}
				});**/
				var d = $.dialog({
					title: '下载试用',
					width: 400,
					content: html,
					modal: true,
					buttons: {
						"确定": function() {
				          alert( "Create" );
				        },
				        '取消': function() {
				          d.dialog( "close" );
				        }
				      },
				});
				/**BootstrapDialog.show({
		            title: '下载试用',
		            draggable: true,
		            message: $('<div></div>').load('html/remote2.html'),
		            buttons: [{
		                label: '取消',
		                action: function(dialog) {
		                	dialog.close();
		                }
		            }, {
		                label: '确定',
		                cssClass: 'btn-primary',
		                action: function(dialog) {
		                    dialog.setTitle('Title 2');
		                }
		            }]
		        });**/
			});
		},
		wdg: function(){
			$.widget( "custom.alert", $.ui.dialog, {
			    close: function() {
			        this.element.remove();
			    }
			});
		}
	};
	return obj;
});