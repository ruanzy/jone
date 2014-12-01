<dl class='accordion'>
<dt class=header>${mname}</dt>
<#list menus as menu>
	<dd id='${menu.id?c}'><a url='${menu.url}'><i class="icon-folder-close"></i> ${menu.name}</a></dd>
</#list>