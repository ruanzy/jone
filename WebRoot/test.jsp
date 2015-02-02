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
<select id='mbmb' style='width:200px;'>
	<option value="1">1</option>
	<option value="2" selected="selected">2</option>
	<option value="3">3</option>
	<option value="13">13</option>
	<option value="32">32</option>
	<option value="45">45</option>
	<option value="52">52</option>
	<option value="83">83</option>
	<option value="92">92</option>
</select>
<script type="text/javascript">
	$('#mbmb').selectbox();
	$('#mbmb').selectbox('setValue', 3);
	$('#addbtn').click(function(){
		alert($('#mbmb').val());
	});
</script>