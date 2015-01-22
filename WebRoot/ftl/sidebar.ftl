<dl class='accordion' id='accordion1'>
<#list all as res> 
<#if res.type=='1'>
<dt><i class="${res.iconcls}"></i> ${res.name}</dt>
<dd>
<#list all as res2>
<#if res2.type=='2' && res2.pid == res.id>
<a id='${res2.id?c}' url='${res2.url}'><i class="${res2.iconcls}"></i> ${res2.name}<b class="icon-angle-right"></b></a>
</#if>
</#list>
</dd>
</#if>
</#list>