<#assign columns=3>
<input type='hidden' name='user' value="${user}"/>
<table>
<#list roles as role>
<#if role_index%columns ==0>
	<tr>
</#if> 
		<td width=100>
			<input type='checkbox' name='role' value='${role.id}'/>${role.name}
		</td>
<#if (role_index + 1)%columns ==0>
	</tr>
</#if>
</#list>
</table>
<script type="text/javascript">
	var user = jQuery('input[name=user]').val();
	jQuery.ajax({
		url:'user/assignedroles',
		type: 'post',
		data:{user:user},
		cache: false,
		async:false,
		dataType:'json',
		success:function(data){
			jQuery('input[name=role]').each(function(i){
				var item = jQuery(this);
				if($.inArray(item.val(), data)!=-1){
					item.attr("checked", true);
				}
			});
		}
	});
</script>