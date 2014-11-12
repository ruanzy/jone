<#assign columns=3>
<input type='hidden' name='user' value="${user}"/>
<table>
<#list roles as role>
<#if role_index%columns ==0>
	<tr>
</#if>
		<td width=100>
			<input type='checkbox' name='role' value='${role.id}' <#if role.checked==1>checked</#if>/>${role.name}
		</td>
<#if (role_index + 1)%columns ==0>
	</tr>
</#if>
</#list>
</table>