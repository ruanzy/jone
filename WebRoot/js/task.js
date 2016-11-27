$TASK = {
	init: function(){
		util.render('html/8.html', 'rui-content');
		$('#loading').hide();
		$('pre code').each(function(i, block) {
			hljs.highlightBlock(block);
		});
		$TASK.list();
		$('#add').on('click', $TASK.add);
	},
	list: function(){
		var me = this;
		$('#tasks').grid({
			sm : 'm',
			url : 'task/list',
			pagesize : 10,
			columns: [
				{ header: "name" , field: 'name'},
				{ header: "time" , field: 'time'},
				{ header: "amount" , field: 'amount'},
				{ header: "tax" , field: 'tax', render: taxRender},
				{ header: "memo" , field: 'memo'}
			]
		});
		function taxRender(v){
			if(v >= 50){
				return '<label class="label label-danger">' + v + '</label>';
			}
			return '<label class="label label-success">' + v + '</label>';
		}
	},
	add: function(){
		var me = this;
		$.dialog('html/addTask.html', {
			title:'增加任务',
			padding: 20,
			//closable: true,
			width: 600,
			buttons: [
				{
					text: '确定',
					cls: 'btn-primary',
					action: function(){
						$TASK.save(this);
					}
				},
				{
					text: '取消',
					cls: 'btn-default',
					action: function(){
						this.dialog('close');
					}
				}
			]
		});
	},
	save: function(dialog){
		var name = $('#name').val();
		var time = $('#time').val();
		var amount = $('#amount').val();
		var tax = $('#tax').val();
		var memo = $('#memo').val();
		var data = {};
		data['name'] = name;
		data['time'] = time;
		data['amount'] = amount;
		data['tax'] = tax;
		data['memo'] = memo;
		$.ajax({
			url: 'task/add',
			type: 'post',
			data: data,
			dataType: 'json',
	        success: function(result){
	        	dialog.dialog('close');
	        	$.noty('增加成功!');
	        	$('#tasks').grid('reload');
			}
		});
	}
};
$JONE['TASK'] = $TASK;