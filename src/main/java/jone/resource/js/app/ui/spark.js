define([ 'util', 'toastr' ], function(util, toastr) {
	var obj = {
		init : function() {
			var me = this; 
			util.render('html/spark.html', 'container');
			$('#spark').on('click', function(){me.spark();});
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
			$('#datalist').grid({
				url : 'user/list',
				columns: [
					{ header: util.i18n('SYS_USER_USERNAME'), field: 'username'},
					{ header: '部门', field: 'depart', render : deptRender},
					{ header: util.i18n('SYS_USER_REGTIME'), field: 'regtime', align: 'center', width: 200},
					{ header: util.i18n('OPERATE'), field: 'op', render : opRender, align: 'center', width: 200}
				]
			});
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
		},
		add: function(){
			var me = this;
			var html = util.tpl('html/sys/adduser.html');
        	var d = $.dialog({
        	    title: util.i18n('SYS_USER_ADD'),
        	    content: html,
        	    width: 450,
        	    padding: '20px 50px 0 50px',
				buttons: [
			         {
			        	 text: util.i18n('DONE'),
			        	 cls: 'btn-primary',
			        	 click: function () {
		        	    	var flag = me.addvalid();
		        	    	if(flag){
		        	    		me.save(this);
		        	    	}
			             }
			         },
			         {
			        	 text: util.i18n('CANCEL'),
			        	 click: function () {
			        		 this.close();
			             }
			         }
			    ],
			    open: function(){
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
							url: 'dept/tree',
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
		},
		addds: function(){
			var me = this;
			var html = util.tpl('html/sys/addds.html');
        	var d = $.dialog({
        	    title: '新增数据源',
        	    content: html,
        	    width: 1000,
			    open: function(){
					$("#example-basic").steps({
						headerTag: "h3",
						bodyTag: "section",
						autoFocus: true,
						labels: {
							previous: '上一步',
							next: '下一步',
							finish: '完&nbsp;&nbsp;&nbsp;成'
						}
					});
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
			var url = "user/add";
			$('#form').ajaxSubmit({
				url: url,
				type: 'post',  
				dataType: 'json',
				success: function(data){
					d.close();
					toastr.success('新增用户成功!', '提示');
					$('#datalist').grid('reload');
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
			        	 cls: 'btn-primary',
			        	 click: function () {
			        	    	var flag = me.resetpwdvalid();
			        	    	if(!flag){
			        	    		return false;
			        	    	}
			        	    	me.savepwd(this);
			             }
			         },
			         {
			        	 text: util.i18n('CANCEL'),
			        	 click: function () {
			        		 this.close();
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
					d.close();
					toastr.success(util.i18n('SYS_USER_RESETPWD_SUCCESS'), '提示');
				},
				error: function(){
					d.close();
					toastr.warn(util.i18n('SYS_USER_RESETPWD_FAIL'), '提示');
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
    			        	toastr.success('删除用户成功!', '提示');
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
        	    padding: '20px 50px 0 50px',
				buttons: [
			         {
			        	 text: util.i18n('DONE'),
			        	 cls: 'btn-primary',
			        	 click: function () {
							var roles = [];
							var _roles = $('#roles').find('input[name="role"]:checked');
							_roles.each(function(){    
								roles.push($(this).val());    
							});    
							me.saveRole(username, this);
			             }
			         },
			         {
			        	 text: util.i18n('CANCEL'),
			        	 click: function () {
			        		 this.close();
			             }
			         }
			    ],
			    open: function(){
			    	$('input').iCheck({
			    		checkboxClass: 'icheckbox_square-green',
			    		radioClass: 'iradio_square-green',
			    		increaseArea: '20%' // optional
			    	});		    	
			    }
        	});
		},
		saveRole: function(username, d){
			var roles = [];
			var _roles = $('#roles').find('input[name="role"]:checked');
			_roles.each(function(){    
				roles.push($(this).val());    
			});
			var url = "user/setRole";
			var param = {username: username, roles: roles.join()};
			$.ajax({
				url:url,
				type: 'post',
				data: param,
				dataType: 'json',
		        success: function(result){
		        	d.close();
		        	toastr.success('角色设置成功!', '提示');
		        	$('#datalist').grid('refresh');
				}
			});
		},
		spark: function(){
			var url = "user/spark";
			var param = {};
			$.ajax({
				url:url,
				type: 'post',
				data: param,
				dataType: 'json',
		        success: function(result){
		        	$.alert(result);
				}
			});
		}
	};
	return obj;
});