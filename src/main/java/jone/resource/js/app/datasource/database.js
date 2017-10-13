define(
		[ 'util', 'grid'],
		function(util) {
			var obj = {
				init : function() {

					var me = this;
				
					util.render('html/datasource/database.html', 'sys_main');

				var grid=	$('#datalist').grid({
						sm:'s',
						url : 'database/list',
						columns : [ {
							header : util.i18n('DATABASE_NAME'),
							field : 'name',
							align : 'center'
						}, {
							header : util.i18n('IP'),
							field : 'ip'
						}, {
							header : util.i18n('PORT'),
							field : 'port',
							align : 'center'
						}, {
							header : util.i18n('USER_NAME'),
							field : 'user_name',
							align : 'center'
						}, {
							header : util.i18n('CREATE_TIME'),
							field : 'create_time',
						
							align : 'center'
						}, {
							header : util.i18n('CREATE_USER'),
							field : 'create_user',
							align : 'center'
						}
						// ,
						// { header: "操作" , field: 'op', render : opRender,
						// align: 'center', width: 200}
						]
					});

					$("#adddatabase").on("click", function() {
				
						me.add();

					});
					$("#query")
					.on("click",function(){
						
						$('#datalist').grid('reload');
					});
					$("#deletedatabase").on("click", function() {	
						var selected = $('#datalist').grid('getSelected');

					if (selected.length == 0) {
						dialog({
							title : util.i18n('ALTER'),
							content : util.i18n('SELECT_A_LINE'),
							okValue : util.i18n('DONE'),
							ok : function(data) {
							}
						});
						return;
					}
					var ids = [];
					for ( var k in selected) {
						ids.push(selected[k].id);
					}
					me.del(ids.join());});
					$("#modifydatabase").on("click", function() {
						var selected=$('#datalist').grid('getSelected');
					
						if(selected.length==0){

							 dialog({
								 title : util.i18n('ALTER'),
									content : util.i18n('SELECT_A_LINE'),
									okValue : util.i18n('DONE'),
									ok : function(data) {
									}
								});
							return;
						
						}
						if(selected.length>1){
							 dialog({
								 	title : util.i18n('ALTER'),
									content : util.i18n('SELECT_A_LINE'),
									okValue : util.i18n('DONE'),
									ok : function(data) {
									}
								});
							return;
						}
						$.ajax({
							type : 'post',
							dataType : 'json',
							url:'database/query',
							data:{id:selected[0].id},
							success : function(data) {
								me.modify(data);
							}
						});
						

						});
				},
				save : function(id) {
					var url = "database/add";
					$('#form').ajaxSubmit({url : url,
						type : 'post',
						dataType : 'json',
						success : function() {
							var d_success = dialog({
								title : util.i18n('ALTER'),
								content : util.i18n('SAVE_SUCCESSFUL'),
								okValue : util.i18n('DONE'),
								ok : function() {
									$('#datalist').grid('reload');
								}
							});
							d_success.showModal();
						},
						error : function() {
							var d_error = dialog({
								title : util.i18n('ALTER'),
								content : util.i18n('SAVE_FAILURE'),
								okValue : util.i18n('DONE'),
								ok : function() {
									$('#datalist').grid('reload');
								}
							});
							d_error.showModal();
						}});
				},
				del : function(id) {


		        	var d = dialog({
		        		title : util.i18n('ALTER'),
						content : util.i18n('DELETE_TITLE'),
						okValue : util.i18n('DONE'),
		        	    ok: function () {
		    				var url = "database/del";
		    				var param = {id: id};
		    				$.ajax({
		    					url:url,
		    					type: 'post',
		    					data: param,
		    					dataType: 'json',
		    			        success: function(result){
		    			        	$('#datalist').grid('reload');
		    					}
		    				});
		        	    },
						cancelValue : util.i18n('CANCEL'),
						cancel : $.noop
		        	});
		        	d.showModal();
				

				
				},
				test : function(d) {
					var url = "database/test";
					$('#form').ajaxSubmit({
						

						url : url,
						type : 'post',
						dataType : 'json',
						success : function(data) {
							if(data.error=="1"){
								var d_success = dialog({
									title : util.i18n('ALTER'),
									content : util.i18n('TEST_SUCCESSFUL'),
									okValue : util.i18n('DONE'),
									ok : function() {
										
										
									}
								});
								d_success.showModal();
							}else if(data.error=="0"){
								var d_success = dialog({
									title : util.i18n('ALTER'),
									content : util.i18n('TEST_FAILURE'),
									okValue : util.i18n('DONE'),
									ok : function() {
										
										
									}
								});
								d_success.showModal();
							}
							
							
							
						},
						error : function() {
							var d_error = dialog({
								title : util.i18n('ALTER'),
								content : util.i18n('TEST_FAILURE'),
								okValue : util.i18n('DONE'),
								ok : function() {
//									$('#datalist').grid('reload');
								}
							});
							d_error.showModal();
						}
					});
				},
				addvalid : function() {
					jQuery.validator.addMethod("pattern", function(value,
							element, params) {
						if (!params.test(value)) {
							return false;
						}
						return true;
					});
					jQuery.validator.addMethod("unique", function(value,
							element, params) {
						var ret = false;
						var data = {};
						data[element.name] = value;
						$.ajax({
							url : params,
							type : 'post',
							async : false,
							data : data,
							dataType : 'json',
							success : function(result) {
								ret = result;
							}
						});
						return ret;
					});
					jQuery.validator.addMethod("unique", function (value, element, params) {
						if($(id).val()!=""){
							return true;
						}
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
					var v = jQuery("#form")
							.validate(
									{
										onkeyup : false,
										onfocusout : false,
										rules : {
											name : {
												required : true,
												pattern : /^[a-zA-Z0-9\u4e00-\u9fa5_]{1,20}$/,
												unique: "database/validname"
											},
											database_name : {
												required : true,		
												pattern : /^[a-zA-Z0-9\u4e00-\u9fa5_]{1,50}$/
											},
											user_name : {
												required : true,
												pattern : /^[a-zA-Z0-9\u4e00-\u9fa5_]{1,50}$/
											},
											psw : {
												required : true,										
												pattern : /^[a-zA-Z0-9\u4e00-\u9fa5_]{1,50}$/
											},
											ip : {
												required : true,
												pattern : /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))|localhost$/
											},
											port : {
												required : true,
												pattern : /^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)/
											},
										},
										messages : {
											name : {
												required : util.i18n('NAME')+util.i18n('CANNOTBEEMPTY'),
												pattern : (util.i18n('NAME')+util.i18n('DATABASE_NAME_REG')).replace('1-50','1-20')													,
												unique : util.i18n("DATABASENAME_ALREADY_EXIST")
											},
											database_name : {
												required : util.i18n('CATALOG_NAME')+util.i18n('CANNOTBEEMPTY'),
												pattern : util.i18n('CATALOG_NAME')+util.i18n('DATABASE_NAME_REG')},
											psw : {
												required : util.i18n('PASSWORD')+util.i18n('CANNOTBEEMPTY'),
												pattern : util.i18n('PASSWORD')+util.i18n('DATABASE_NAME_REG')
											},
											port : {
												required : util.i18n('PORT')+util.i18n('CANNOTBEEMPTY'),
												pattern : util.i18n('PORT_EROOR')
											},
											ip : {
												required : util.i18n('IP')+util.i18n('CANNOTBEEMPTY'),
												pattern : util.i18n('IP_EROOR')
											}
										},
										showErrors : function(errorMap,
												errorList) {
											if (errorList.length > 0) {
												var error = errorList[0].message;
												$('#errormsg').html(error);
												$('#error').show();
											} else {
												$('#errormsg').html('');
												$('#error').hide();
											}
										}
									});
					var mm = v.form();
					return mm;
				},
				add : function() {
					var me = this;
					var html = util.tpl('html/datasource/adddatabase.html',{});
					var d = null;
					d = dialog({
						title : util.i18n('ADDDATABASE'),
						content : html,
						onshow:function(){
							
						},
						button : [

						{
							value : util.i18n('DONE'),
							callback : function() {
								 var flag = me.addvalid();
								 if(!flag){
								 return false;
								 }
								 me.save(this);
								 return true;
							},
							autofocus : true
						},
						{
							value : util.i18n('TEST_CONNECT'),
							callback : function() {
								 var flag = me.addvalid();
								 if(!flag){
									 return false;
								 }
								  me.test(this);
								 return false;
							},
							autofocus : true
						}
						]
					});
					d.showModal();
				},
				modify : function(user) {
					var me = this;
//					alert(user.name);
					var html = util.tpl('html/datasource/adddatabase.html',user);
					var d = null;
					d = dialog({
						title :  util.i18n('MODIFYDATABASE'),
						content : html,
						onshow:function(){
							$("#name").attr("readonly","readonly");
						},
						button : [

						{
							value : util.i18n('DONE'),
							callback : function() {
								 var flag = me.addvalid();
								 if(!flag){
								 return false;
								 }
								 me.save(this);
								 return true;
							},
							autofocus : true
						},
						{
							value : util.i18n('TEST_CONNECT'),
							callback : function() {
								 var flag = me.addvalid();
								 if(!flag){
									 return false;
								 }
								  me.test(this);
								 return false;
							},
							autofocus : true
						}
						]
					});
					d.showModal();
				}
			};
			return obj;
		});
