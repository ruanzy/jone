define(['util', 'grid', 'ztree'], function(util){
	var obj = {
		init: function(){
			var me = this;
			util.render('html/sys/rolemgr.html', 'main');
			$('#addrole').on('click', function(){me.add();});
			$('#del').on('click', function(){me.del();});
			me.list();
		},
		list: function(){
			var me = this;
			$('#rolelist').grid({
				url : 'role/list',
				pagesize : 10,
				columns: [
					{ header: util.i18n('SYS_ROLE_ROLENAME'), field: 'name'},
					{ header: util.i18n('OPERATE'), field: 'op', render : opRender}
				]
			});
			function nameRender(v, r){
				var name = r.name;
				var namecode = r.namecode;
				return util.i18n('SYS_ROLE_' + namecode);
			}
			function opRender(v, r){
				var id = r.id;
				var op = new Array();
				var openPermission = $('<a href="javascript:void(0);">' + util.i18n('SYS_ROLE_SETPERMISSION') + '</a>');
				openPermission.on('click', function(){
					me.openPermission(id);
				});
				var span = $('<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>');
				var del = $('<a href="javascript:void(0);">' + util.i18n('DELETE') + '</a>');
				del.on('click', function(){
					me.del(id);
				});
				op.push(openPermission, span, del);
				return op;
			}
		},
		add: function(d){
			var me = this;
			var html = util.tpl('html/sys/addrole.html');
        	var d = $.dialog({
        	    title: util.i18n('SYS_ROLE_ADD'),
        	    content: html,
        	    width: 400,
				buttons: [
					{
						text: util.i18n('DONE'),
						click:function() {
		        	    	var flag = me.addvalid();
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
				]
        	});
		},
		save: function(d){
			var url = "role/add";
			$('#form').ajaxSubmit({
				url: url,
				type: 'post',  
				dataType: 'json',
				success: function(){
					d.dialog("close");
					$.alert(util.i18n('ADD_SUCCESS'), function () {
		        	    $('#rolelist').grid('reload');
		        	});
				},
				error: function(){
					d.dialog("close");
					$.alert(util.i18n('ADD_FAIL'), function () {
		        	    $('#rolelist').grid('reload');
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
					rolename: {
						required: true,
						minlength: 2,
						pattern: /^[a-zA-Z0-9\u4e00-\u9fa5_]{2,10}$/,
						unique: "role/validname"
					}
				},
				messages: {
					rolename: {
						required: util.i18n('SYS_ROLE_VALID_ROLENAME_REQUIRED'),
						minlength: util.i18n('SYS_ROLE_VALID_ROLENAME_MINLENGTH'),
						pattern: util.i18n('SYS_ROLE_VALID_ROLENAME_PATTERN'),
						unique: util.i18n('SYS_ROLE_VALID_ROLENAME_UNIQUE')
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
		openPermission: function(roleid){
			var me = this;
        	var d = $.dialog({
        	    title: util.i18n('SYS_ROLE_SETPERMISSION'),
        	    content: '<ul id="tree" class="ztree" style="width:100%; height: 350px; overflow:auto;"></ul>',
        	    width: 300,
				buttons: [
					{
						text: util.i18n('DONE'),
						click:function() {
							var treeObj = $.fn.zTree.getZTreeObj("tree");
							var nodes = treeObj.getCheckedNodes(true);
							var ids = [];
							for(var k in nodes){
								ids.push(nodes[k].id);
							}
							me.setPermission(roleid, ids.join());
							d.dialog( "close" );	
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
        	    	me.loadpermission(roleid);
        	    }
        	});
		},
		del: function(id){
        	$.confirm(util.i18n('SYS_USER_DELETE_CONFIRM'), function(v){
        		if(v){
    				var url = "role/del";
    				var param = {id: id};
    				$.ajax({
    					url:url,
    					type: 'post',
    					data: param,
    					dataType: 'json',
    			        success: function(result){
    			        	$('#rolelist').grid('reload');
    					}
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
			var url = "role/getPermission";
			var param = {roleid: roleid};
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
		setPermission: function(roleid, permissions){
			var url = "role/setPermission";
			var param = {roleid: roleid, permissions: permissions};
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
	};
	return obj;
});