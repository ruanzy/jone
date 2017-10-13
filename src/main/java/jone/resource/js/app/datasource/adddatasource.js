define(['util', 'grid','ztree'], function(util){
	var obj = {
		init: function(){
			
		
			
			util.render('html/datasource/datasource.html', 'sys_main');
			$('#restfulChart').on('click', function(){
				util.selectMenu('#datasource');
				require(['app/datasource/database'], function(datasoure) {
					database.init();
				});
			});
			$('#restfulChart').on('click', function(){
				util.selectMenu('#datasource');
				require(['app/datasource/database'], function(datasoure) {
					database.init();
				});
			});
			$('#datalist').grid({url : 'DatabaseSource/list',
				columns: [
							{ header: "数据源名称" , field: 'name'},
							{ header: "数据源名称" , field: 'database_name'},
							{ header: "注册时间" , field: 'create_time', align: 'center', width: 200}
//							,
//							{ header: "操作" , field: 'op', render : opRender, align: 'center', width: 200}
						]});

			
			
			var setting = {
					edit: {
						enable: true,
						showRemoveBtn: false,
						showRenameBtn: false,
						drag: {
						}
					},
					data: {
						keep: {
							parent: true,
							leaf: true
						},
						simpleData: {
							enable: true
						}
					},
					callback: {
						onDrop:zTreeOnDrop
						//onDragMove: MoveTest.dragMove,
						//onMouseUp: MoveTest.dom2Tree
					},
					view: {
						selectedMulti: false
					}
				};

			var zNodes =[
				{ name:"父节点1 - 展开", open:true,
					children: [
						{ name:"父节点11 - 折叠",
							children: [
								{ id:1,name:"叶子节点111"},
								{ id:2,name:"叶子节点112"},
								{ id:3,name:"叶子节点113"},
								{ id:4,name:"叶子节点114"}
							]},
						{ name:"父节点12 - 折叠",
							children: [
								{ id:5,name:"叶子节点121"},
								{ id:6,name:"叶子节点122"},
								{ id:7,name:"叶子节点123"},
								{ id:8,name:"叶子节点124"}
							]},
						{ name:"父节点13 - 没有子节点", isParent:true}
					]},
				{ name:"父节点2 - 折叠",
					children: [
						{ name:"父节点21 - 展开", open:true,
							children: [
								{ name:"叶子节点211"},
								{ name:"叶子节点212"},
								{ name:"叶子节点213"},
								{ name:"叶子节点214"}
							]},
						{ name:"父节点22 - 折叠",
							children: [
								{ name:"叶子节点221"},
								{ name:"叶子节点222"},
								{ name:"叶子节点223"},
								{ name:"叶子节点224"}
							]},
						{ name:"父节点23 - 折叠",
							children: [
								{ name:"叶子节点231"},
								{ name:"叶子节点232"},
								{ name:"叶子节点233"},
								{ name:"叶子节点234"}
							]}
					]},
				{ name:"父节点3 - 没有子节点", isParent:true}

			];
			
			
			function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
			    alert(treeNodes.length + "," + (targetNode ? (targetNode.tId + ", " + targetNode.name) : "isRoot" ));
			    var domId = "sourcetable_charts";
				var newDom = $("span[domId=" + treeNodes[0].id + "]");
				if (newDom.length > 0) {
					newDom.removeClass("domBtn_Disabled");
					newDom.addClass("domBtn");
				} else {
					$("#" + domId).append("<span class='domBtn' domId='" + treeNodes[0].id + "'>" + treeNodes[0].name + "</span>");
				}
			};
			
			$("#addsource").on("click",function(){

				var me = this;
				var html = util.tpl('html/datasource/addsource.html');
	        	var d = null;
	        	d = dialog({
	        	    title: util.i18n('SETTING'),
	        	    content: html,
	        	    padding: 0,
	        	    onshow: function () {
	        	    	//在弹出框里面绑定事件
	        	 
	        	    	$.ajax({

							type : 'post',
							dataType : 'json',
							url:'database/listAll',
							success : function(data) {
								var s="";
								$.each(arr, function(key, val) {
								
										s+="<option>"+val.name+"</option>";
								});
							
								$("#database_select").append(s);
							}
						
	        	    	});
	        	    	$.fn.zTree.init($("#sourceTableTree"), setting, zNodes);
	        	    	
	        	    	$(".addsource_maintab li,.addsource_condtab li").on("click", function(){
	        	    		$(this).siblings().removeClass("addsource_tabactive");
	        	    		$(this).addClass("addsource_tabactive");
	        	    	});
	        	    	
	        	    	//tab 1的点击事件
	        	    	$("#tab_href_chart").on("click", function(){
	        	    		$(".tab_chart").show();
	        	    		$(".tab_sql").hide();
	        	    	});
	        	    	//tab 2的点击事件
	        	    	$("#tab_href_sql").on("click", function(){
	        	    		$(".tab_sql").show();
	        	    		$(".sqltextarea").focus();//让输入框获取焦点
	        	    		$(".tab_chart").hide();
	        	    	});
	        	    	
	        	    	//cond tab 1的点击事件
	        	    	$("#condtab_href_field").on("click", function(){
	        	    		$(".condtab_field").siblings().hide();
	        	    		$(".condtab_field").show();
	        	    	});
	        	    	
	        	    	//cond tab 2的点击事件
	        	    	$("#condtab_href_cond").on("click", function(){
	        	    		$(".condtab_condition").siblings().hide();
	        	    		$(".condtab_condition").show();
	        	    	});
	        	    	
	        	    	//cond tab 3的点击事件
	        	    	$("#condtab_href_group").on("click", function(){
	        	    		$(".condtab_group").siblings().hide();
	        	    		$(".condtab_group").show();
	        	    	});
	        	    	
	        	    	//cond tab 4的点击事件
	        	    	$("#condtab_href_sql").on("click", function(){
	        	    		$(".condtab_sql").siblings().hide();
	        	    		$(".condtab_sql").show();
	        	    	});
	        	    },
	        	    okValue: '保存配置',
	        	    ok: function () {
						
	        	    },
	        	    cancelValue: '取消',
	        	    cancel: $.noop
	        	});
	        	d.showModal();
			});
			
			
			

			
			
			
		}
	};
	return obj;
});