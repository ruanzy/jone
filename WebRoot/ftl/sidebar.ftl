<dl class='accordion'>
<dt>${mname}</dt>
<#list menus as menu>
	<dd id='${menu.id?c}'><a url='${menu.url}'><i class="${menu.icon}"></i> ${menu.name}<b class="icon-angle-right"></b></a></dd>
</#list>