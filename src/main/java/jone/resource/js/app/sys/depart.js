define(['util', 'grid'], function(util){
	var obj = {
		init: function(){
			var me = this;
			util.render('html/sys/depart.html', 'main');
			$( ".portlet" )
		      .addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
		      .find( ".portlet-header" )
		        .addClass( "ui-widget-header ui-corner-all" );
			me.tree();
			me.loadpermission();
				$('#addbtn').on('click', function(){me.add();});
				$('#savebtn').on('click', function(){me.setPermission();});

		},
		tree: function(){
			var me = this;
			var setting = {
					data: {
						simpleData: {
							enable: true,
							pIdKey: "pid"
						}
					},
					callback: {
						onClick: function(event, treeId, treeNode) {
							me.loadpermission();
						}
					}
				};
				$.ajax({
					url: 'dept/tree',
					type: 'post',
					async: false,
					dataType: 'json',
			        success: function(result){
			        	$.fn.zTree.init($("#treeDemo"), setting, result);
			        	var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
			        	var node = zTreeObj.getNodeByParam('id', 3);
			        	zTreeObj.selectNode(node);
			        	zTreeObj.setting.callback.onClick(null, zTreeObj.setting.treeId, node);
					}
				});
		},
		add: function(){
			var me = this;
			var html = util.tpl('html/sys/adddept.html');
        	var d = $.dialog({
        	    title: '新增部门',
        	    content: html,
        	    modal: true,
        	    width: 400,
				buttons: [
					{
						text: util.i18n('DONE'),
						click:function() {
							var flag = true;//me.addvalid();
							if(!flag){
								return false;
							}
							me.save(d);
						}
				    },
					{
						text: util.i18n('CANCEL'),
						click:function() {
							d.dialog( "close" );							
						}
				    }
				],
				open: function(){
		        	var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
		        	var selectedNode = zTreeObj.getSelectedNodes()[0];
					var id = selectedNode.id;
					var name = selectedNode.name;
					$('#addform #pid').val(id);
					$('#addform #pname').val(name);
				}
        	});
		},
		addvalid: function(){
			jQuery.validator.addMethod("pattern", function (value, element, params) {
				if (!params.test(value)) {
					return false;
				}
				return true;
			});
			jQuery.validator.addMethod("unique", function (value, element, params) {
				var ret = false;
				var data = {};
				data[element.name] = value;
				$.ajax({
					url: params,
					type: 'post',
					async: false,
					data: data,
					dataType: 'json',
			        success: function(result){
			        	ret = result;
					}
				});
			    return ret;
			});
			var v = jQuery("#form").validate({
				onkeyup:false,
				onfocusout:false,
				rules: {
					username: {
						required: true,
						minlength: 5,
						pattern: /^[a-zA-Z0-9\u4e00-\u9fa5_]{5,10}$/,
						unique: "user/validname"
					},
					password: {
						required: true,
						minlength: 6,
						pattern: /^[a-zA-Z0-9_]{6,12}$/
					},
					repassword: {
						required: true,
						minlength: 6,
						equalTo: "#password"
					}
				},
				messages: {
					username: {
						required: util.i18n('SYS_USER_VALID_USERNAME_REQUIRED'),
						minlength: util.i18n('SYS_USER_VALID_USERNAME_MINLENGTH'),
						pattern: util.i18n('SYS_USER_VALID_USERNAME_PATTERN'),
						unique: util.i18n('SYS_USER_VALID_USERNAME_UNIQUE')
					},
					password: {
						required: util.i18n('SYS_USER_VALID_PASSWORD_REQUIRED'),
						minlength: util.i18n('SYS_USER_VALID_PASSWORD_MINLENGTH'),
						pattern: util.i18n('SYS_USER_VALID_PASSWORD_PATTERN')
					},
					repassword: {
						required: util.i18n('SYS_USER_VALID_REPASSWORD_REQUIRED'),
						minlength: util.i18n('SYS_USER_VALID_REPASSWORD_MINLENGTH'),
						equalTo: util.i18n('SYS_USER_VALID_REPASSWORD_EQUALTO')
					}
				},
				showErrors:function(errorMap, errorList) {
					if(errorList.length > 0){
						var error = errorList[0].message;
						$('#errormsg').html(error);
						$('#error').css('visibility', 'visible');
					}else{
						$('#errormsg').html('');
						$('#error').css('visibility', 'hidden');
					}
				}
			});
			var mm = v.form();
			return mm;
		},
		save: function(d){
			var me = this;
			var url = "dept/add";
			$('#addform').ajaxSubmit({
				url: url,
				type: 'post',  
				dataType: 'json',
				success: function(data){
					d.dialog("close");
					
					$.alert(util.i18n('ADD_SUCCESS'), function () {
						me.tree();
		        	});
				},
				error: function(){
					d.dialog("close");
					$.alert(util.i18n('ADD_FAIL'), function () {
		        	    $('#datalist').grid('reload');
		        	});
				}
			});
		},
		loadpermission: function(roleid){
			var setting = {
				view: {
					dblClickExpand: false,
					showLine: true,
					showIcon: false,
					selectedMulti: false
				},
				check: {
					enable: true,
					chkboxType: { "Y" : "ps", "N" : "ps" }
				},
				data: {
					simpleData: {
						enable:true,
						idKey: "id",
						pIdKey: "pid",
						rootPId: ""
					}
				},
				callback: {
					beforeClick: function(treeId, treeNode) {
						var zTree = $.fn.zTree.getZTreeObj("tree");
						if (treeNode.isParent) {
							zTree.expandNode(treeNode);
							return false;
						} else {
							return true;
						}
					}
				}
			};
			var url = "dept/getPermission";
			var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
        	var selectedNode = zTreeObj.getSelectedNodes()[0];
			var id = selectedNode.id;
			var param = {id: id};
			$.ajax({
				url:url,
				type: 'post',
				async: false,
				data: param,
				dataType: 'json',
		        success: function(result){
		        	var t = $("#tree");
		        	$.each(result, function(idx, item){
		        		item['name'] = util.i18n('MENU_' + item['namecode']);
		        	});
		        	t = $.fn.zTree.init(t, setting, result);
				}
			});
		},
		setPermission: function(){
			var me = this;
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getCheckedNodes(true);
			var ids = [];
			for(var k in nodes){
				ids.push(nodes[k].id);
			}
			var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
        	var selectedNode = zTreeObj.getSelectedNodes()[0];
			var deptid = selectedNode.id;
			var url = "dept/setPermission";
			var param = {deptid: deptid, permissions: ids.join()};
			$.ajax({
				url:url,
				type: 'post',
				data: param,
				dataType: 'json',
		        success: function(result){
		        	me.loadpermission();
				}
			});
		},
		update: function(node){
			var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var id = $('#id').val();
			var name = $('#name').val();
			var node = zTreeObj.getNodeByParam("id", id, null);
			node.name = name;
			$('#form').ajaxSubmit({
				url: 'dept/update',
				type: 'post',  
				dataType: 'json',
				success: function(data){
					zTreeObj.updateNode(node);
					$.alert(util.i18n('ADD_SUCCESS'), function () {
		        	   // $('#datalist').grid('reload');
		        	});
				},
				error: function(){
		        	
				}
			});
		},
		resetpwd: function(username){
			var me = this;
			var html = util.tpl('html/sys/resetpwd.html', {username: username});
        	var d = $.dialog({
        	    title: util.i18n('SYS_USER_RESETPWD'),
        	    content: html,
        	    width: 400,
				buttons: [
					{
						text: util.i18n('DONE'),
						click:function() {
		        	    	var flag = me.resetpwdvalid();
		        	    	if(!flag){
		        	    		return false;
		        	    	}
		        	    	me.savepwd(d);
						}
				    },
					{
						text: util.i18n('CANCEL'),
						click:function() {
							d.dialog( "close" );							
						}
				    }
				]
        	});
		},
		savepwd: function(d){
			var url = "user/resetpwd";
			$('#form').ajaxSubmit({
				url: url,
				type: 'post',  
				dataType: 'json',
				success: function(){
					d.dialog("close");
					$.alert(util.i18n('SYS_USER_RESETPWD_SUCCESS'));
				},
				error: function(){
					d.dialog("close");
					$.alert(util.i18n('SYS_USER_RESETPWD_FAIL'));
				}
			});
		},
		resetpwdvalid: function(){
			jQuery.validator.addMethod("pattern", function (value, element, params) {
			    if (!params.test(value)) {
			        return false;
			    }
			    return true;
			});
			var v = jQuery("#form").validate({
				onkeyup:false,
				onfocusout:false,
				rules: {
					password: {
						required: true,
						minlength: 6,
						pattern: /^[a-zA-Z0-9_]{6,12}$/
					},
					confirm_password: {
						required: true,
						minlength: 6,
						equalTo: "#password"
					}
				},
				messages: {
					password: {
						required: util.i18n('SYS_USER_VALID_PASSWORD_REQUIRED'),
						minlength: util.i18n('SYS_USER_VALID_PASSWORD_MINLENGTH'),
						pattern: util.i18n('SYS_USER_VALID_PASSWORD_PATTERN')
					},
					confirm_password: {
						required: util.i18n('SYS_USER_VALID_REPASSWORD_REQUIRED'),
						minlength: util.i18n('SYS_USER_VALID_REPASSWORD_MINLENGTH'),
						equalTo: util.i18n('SYS_USER_VALID_REPASSWORD_EQUALTO')
					}
				},
				showErrors:function(errorMap, errorList) {
					if(errorList.length > 0){
						var error = errorList[0].message;
						$('#errormsg').html(error);
						$('#error').css('visibility', 'visible');
					}else{
						$('#errormsg').html('');
						$('#error').css('visibility', 'hidden');
					}
				}
			});
			var mm = v.form();
			return mm;
		},
		del: function(username){
        	$.confirm(util.i18n('SYS_USER_DELETE_CONFIRM'), function(v){
        		if(v){
    				var url = "user/del";
    				var param = {username: username};
    				$.ajax({
    					url:url,
    					type: 'post',
    					data: param,
    					dataType: 'json',
    			        success: function(result){
    			        	$('#datalist').grid('reload');
    					}
    				});
        		}
        	});
		},
		setRole: function(username){
			var me = this;
			var url = "user/getRoles";
			var param = {username: username};
			var data = {list: []};
			$.ajax({
				url:url,
				type: 'post',
				async: false,
				data: param,
				dataType: 'json',
		        success: function(result){
		        	data['list'] = result;
				}
			});
			var html = util.tpl('html/sys/setrole.html', data);
        	var d = $.dialog({
        	    title: util.i18n('SYS_USER_SETROLE'),
        	    content: html,
        	    width: 400,
				buttons: [
					{
						text: util.i18n('DONE'),
						click:function() {
							var roles = [];
							var _roles = $('#roles').find('input[name="role"]:checked');
							_roles.each(function(){    
								roles.push($(this).val());    
							});    
							me.saveRole(username, roles.join());
							d.dialog( "close" );	
						}
				    },
					{
						text: util.i18n('CANCEL'),
						click:function() {
							d.dialog( "close" );							
						}
				    }
				]
        	});
		},
		saveRole: function(username, roles){
			var url = "user/setRole";
			var param = {username: username, roles: roles};
			$.ajax({
				url:url,
				type: 'post',
				data: param,
				dataType: 'json',
		        success: function(result){
		        	$('#datalist').grid('refresh');
				}
			});
		}
	};
	return obj;
});