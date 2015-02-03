<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<style>
</style>
<div style="">
	<button id='addbtn' class='btn btn-success' funcid='102'>
		<i class="icon-plus-sign"></i> 增加
	</button>
	<button id='delbtn' class='btn btn-success' funcid='103'>
		<i class="icon-pencil"></i> 修改
	</button>
	<button id='assignbtn' class='btn btn-success' funcid='104'>
		<i class="icon-remove-sign"></i> 删除
	</button>
</div>
<button id='viewcode' class='btn btn-success'>
	<i class="icon-pencil"></i> 查看源码
</button>
<script type="text/javascript">
	$('#viewcode').click(function() {
		$.dialog({
			title : '查看源码',
			url : 'code.html',
			padding : '0',
			buttons : [ {
				text : '关闭',
				cls : 'btn-default',
				action : function(d) {
					d.close();
				}

			} ],
			onShow : function(){
				$.get('login2.html', function(data,status,xhr){
					var highLightCode2 = HighLighter.highLight('html', 'rDark', data);
					$('#code').html(highLightCode2);
				});
			}
		});
	});
</script>