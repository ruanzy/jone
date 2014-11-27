<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head></head>
<body>
<ul id="tree"></ul>
<input type="hidden" name='roleid' value='${param.id}'/>		
		<script type="text/javascript">

$(function(){
		$("#tree").Tree({
    		url:"role/allres?_=" + new Date().getTime(),
    		checkbox: true,
    		onload:init
		});
	
	function init(){
		$.ajax({ 
			url: "role/ownres", 
			async: false,
			type: 'post',
			data: {role:${param.id}},
			dataType: 'json',
			success: function(d){
				$("#tree").Tree('checked', d);
	  		}
	  	});
	}	
	

});
</script>
</body>
</html>