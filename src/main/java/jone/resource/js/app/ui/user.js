define(['util', 'ztree'], function(util){
	var obj = {
		init: function(){
			var me = this;
			util.render('html/sys/usermgr.html', 'sys_main');
			$('#add').on('click', function(){me.add();});
			$('#del').on('click', function(){me.del();});
			me.list();
		},
		list: function(){
			var me = this;
			var deptarr = {};
			var deptmap = {};
			$.ajax({
				url: 'dept/tree',
				type: 'post',
				async: false,
				dataType: 'json',
				success: function(result){
					deptarr = result;
					deptmap = getMap(result);
				}
			});
			function getMap(arr){
				var map = {};
	        	for(var k in arr){
	        		var id = arr[k].id;
	        		map[id] = arr[k];
	        	}
	        	return map;
	        }
			$('#datalist1').grid({
				url : 'user/list',
				columns: [
					{ header: util.i18n('SYS_USER_USERNAME'), field: 'username'},
					{ header: '部门', field: 'depart', render : deptRender},
					{ header: util.i18n('SYS_USER_REGTIME'), field: 'regtime', align: 'center', width: 200},
					{ header: util.i18n('OPERATE'), field: 'op', render : opRender, align: 'center', width: 200}
				]
			});
			function getPath(r){
				var arr = [];
				var level = r.level;
				var node = r;
				arr.push(node.name);
	        	for(var k = level; k > 1; k--){
	        		node = deptmap[node.pid];
	        		var name = node.name;
	        		arr.push(name);
	        	}
	        	return arr.join('/');
	        }
			function deptRender(v, r){
				var arr = [];
				var depid = r.depart;
				var node = deptmap[depid];
				var level = node.deep;
				arr.push(node.name);
	        	for(var k = level; k > 0; k--){
	        		node = deptmap[node.pid];
	        		var name = node.name;
	        		arr.push(name);
	        	}
	        	return arr.reverse().join('/');
	        }
			function stateRender(v){
				return (v == 1)? ('<label class="label label-success">正常 </label>'): ('<label class="label label-danger">注销</label>');
			}
			function opRender(v, r){
				var state = r.state;
				var name = r.username;
				var op = new Array();
				if(state == 1){
					var setrole = $('<a href="javascript:void(0);">' + util.i18n('SYS_USER_SETROLE') + '</a>');
					setrole.on('click', function(){
						me.setRole(name);
					});
					var space1 = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
					var pwd = $('<a href="javascript:void(0);">' + util.i18n('SYS_USER_RESETPWD') + '</a>');
					pwd.on('click', function(){
						me.resetpwd(name);
					});
					var space2 = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
					var del = $('<a href="javascript:void(0);">' + util.i18n('DELETE') + '</a>');
					del.on('click', function(){
						me.del(name);
					});
					op.push(setrole, space1, pwd, space2, del);
				}
				return op;
			}
			
			var me = this;
			
			$("#userpager").pagination({
				pageSize: 10,
				remote: {
					url: 'user/list',
					pageParams: function(data){
						return {
							page: data.pageIndex + 1,
							pagesize: data.pageSize
						};
					},
					success: function (data) {
						data.deptRender = deptRender;
						var html = util.tpl('html/sys/usertpl.html', data);
						$("#showaspanel").html(html);
						$("#countC1").html(data.total);
						$("dd.deluser").on('click', function() {
							var username = $(this).attr('data');
							me.del(username);
						});
						$("dd.resetpwd").on('click', function() {
							var username = $(this).attr('data');
							me.resetpwd(username);
						});
						$("dd.setRole").on('click', function() {
							var username = $(this).attr('data');
							me.setRole(username);
						});
					}
				}
			}).on("pageClicked", function (event, data) {
				//alert(data.pageIndex);
			});
		},
		add: function(){
			var me = this;
			var html = util.tpl('html/sys/adduser.html');
        	var d = null;
        	$.dialog({
        	    title: util.i18n('SYS_USER_ADD'),
        	    content: html,
				buttons: [
			         {
			        	 text: util.i18n('DONE'),
			        	 click: function () {
		        	    	var flag = me.addvalid();
		        	    	this.close();
		        	    	me.save(this);
			             }
			         },
			         {
			        	 text: util.i18n('CANCEL'),
			        	 click: function () {
			        		 this.close();
			             }
			         }
			    ],
				onshow: function(){
					var setting = {
							data: {
								simpleData: {
									enable: true,
									pIdKey: "pid"
								}
							},
							callback: {
								onClick: function(event, treeId, treeNode) {
									event.stopPropagation();
									if(!treeNode.isParent){							
										var id = treeNode.id;
										var name = treeNode.name;
										$('#dept').val(name);
										$('#deptid').val(id);
									}else{
										alert('只能选择最后一级节点');
									}
								}
							}
						};
						$.ajax({
							url: 'http://localhost:8089/dept/tree',
							type: 'post',
							async: false,
							dataType: 'json',
					        success: function(result){
					        	$.fn.zTree.init($("#treeDemo"), setting, result);
							}
						});
						function onBodyDown(event) {
							if (!(event.target.id == "menuContent" || $(event.target).parents("#deptwrapper").length > 0)) {
								hideMenu();
							}
						}
						function hideMenu() {
							$("#menuContent").fadeOut("fast");
							$(document).unbind("mouseup", onBodyDown);
						}
					$('#dept').click(function(e){
						e.stopPropagation();
						var cityObj = $("#dept");
						$("#menuContent").css({left:0, top:cityObj.outerHeight() + 1}).slideDown("fast");
						$(document).bind("mouseup", onBodyDown);
					});
				}
        	});
        	d.showModal();
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
						$('#error').show();
					}else{
						$('#errormsg').html('');
						$('#error').hide();
					}
				}
			});
			var mm = v.form();
			return mm;
		},
		save: function(d){
			var url = "user/save";
			$('#form').ajaxSubmit({
				url: url,
				type: 'post',  
				dataType: 'json',
				success: function(data){
					var d_success = dialog({
		        	    title: util.i18n('ALERT'),
		        	    content: util.i18n('ADD_SUCCESS'),
		        	    okValue: util.i18n('DONE'),
		        	    ok: function () {
		        	    	//$('#datalist').grid('reload');
		        	    	$("#userpager").pagination('remote');
		        	    },
		        	    cancelValue: util.i18n('CANCEL'),
		        	    cancel: $.noop
		        	});
		        	d_success.showModal();
				},
				error: function(){
		        	var d_error = dialog({
		        	    title: util.i18n('ALERT'),
		        	    content: util.i18n('ADD_FAIL'),
		        	    okValue: util.i18n('DONE'),
		        	    ok: function () {
		        	    	$('#datalist').grid('reload');
		        	    },
		        	    cancelValue: util.i18n('CANCEL'),
		        	    cancel: $.noop
		        	});
		        	d_error.showModal();
				}
			});
		},
		resetpwd: function(username){
			var me = this;
			var html = util.tpl('html/sys/resetpwd.html', {username: username});
			var d = null;
        	d = dialog({
        	    title: util.i18n('SYS_USER_RESETPWD'),
        	    content: html,
        	    okValue: util.i18n('DONE'),
        	    ok: function () {
        	    	var flag = me.resetpwdvalid();
        	    	if(!flag){
        	    		return false;
        	    	}
        	    	me.savepwd(this);
        	    },
        	    cancelValue: util.i18n('CANCEL'),
        	    cancel: $.noop
        	});
        	d.showModal();
		},
		savepwd: function(dialog){
			var url = "user/resetpwd";
			$('#form').ajaxSubmit({
				url: url,
				type: 'post',  
				dataType: 'json',
				success: function(){
					dialog().title(util.i18n('ALERT')).content(util.i18n('SYS_USER_RESETPWD_SUCCESS')).showModal();
				},
				error: function(){
					dialog().title(util.i18n('ALERT')).content(util.i18n('SYS_USER_RESETPWD_FAIL')).showModal();
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
						$('#error').show();
					}else{
						$('#errormsg').html('');
						$('#error').hide();
					}
				}
			});
			var mm = v.form();
			return mm;
		},
		del: function(username){
        	var d = null;
        	d = dialog({
        	    title: util.i18n('ALERT'),
        	    content: util.i18n('SYS_USER_DELETE_CONFIRM'),
        	    okValue: util.i18n('DONE'),
        	    ok: function () {
    				var url = "user/del";
    				var param = {username: username};
    				$.ajax({
    					url:url,
    					type: 'post',
    					data: param,
    					dataType: 'json',
    			        success: function(result){
    			        	$("#userpager").pagination('remote');
    					}
    				});
        	    },
        	    cancelValue: util.i18n('CANCEL'),
        	    cancel: $.noop
        	});
        	d.showModal();
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
			var d = null;
        	d = dialog({
        	    title: util.i18n('SYS_USER_SETROLE'),
        	    content: html,
        	    okValue: util.i18n('DONE'),
        	    ok: function () {
					var roles = [];
					var _roles = $('#roles').find('input[name="role"]:checked');
					_roles.each(function(){    
						roles.push($(this).val());    
					});    
					me.saveRole(username, roles.join());
        	    },
        	    cancelValue: util.i18n('CANCEL'),
        	    cancel: $.noop
        	});
        	d.showModal();
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