<dl class='accordion'>
<dt class=header><i class='icon-list-alt' style='font-size:18px;'></i>${mname}</dt>
<#list menus as menu>
	<dd id='${menu.id?c}'><a url='${menu.url}'><i class="icon-user"></i> ${menu.name}</a></dd>
</#list>