<div id='logo'></div>
<ul class='nav'>
	<#list modules as module>
		<li id='m${module.id?c}' name='${module.name}'><i class="icon-user"></i> ${module.name}</li>
	</#list>
</ul>
<ul class='info'>
	<li>
	<dl id='asd' class='rzy-sidedown'><dt><i class="icon-user"></i> 欢迎您,${user} <i class="icon-angle-down"></i></dt><dd></dd></dl>
	</li>
	<li><a><i class="icon-envelope"></i> 信息</a>
		<span class='badge label-warning'>5</span>
	</li>
	<li><a><i class="icon-bell"></i> 更换样式</a>
	</li>
</ul>
