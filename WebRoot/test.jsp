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
<input id='mbmb' type="text"/>
<script type="text/javascript">
	$('#mbmb').selectbox({
		ds : [{text:'男', value:1}, {text:'女', value:2}]
	});
	$('#mbmb').selectbox('setValue', 2);
	$('#addbtn').click(function(){
		alert($('#mbmb').selectbox('getValue'));
	});
</script>