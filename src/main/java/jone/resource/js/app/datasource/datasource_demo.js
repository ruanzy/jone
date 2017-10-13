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
			
			
			var data222 = [
	               		{name: "徐州", value: 79},
	               		{name: "句容", value: 75},
	               		{name: "无锡", value: 71},
	               		{name: "南京", value: 67},
	               		{name: "台州", value: 67},
	               		{name: "常州", value: 64},
	               		{name: "扬州", value: 64},
	               		{name: "绍兴", value: 63},
	               		{name: "金坛", value: 62},
	               		{name: "宜兴", value: 59},
	               		{name: "镇江", value: 59},
	               		{name: "张家港", value: 52},
	               		{name: "嘉兴", value: 51},
	               		{name: "苏州", value: 50},
	               		{name: "吴江", value: 47},
	               		{name: "宿迁", value: 43},
	               		{name: "江阴", value: 37},
	               		{name: "淮安", value: 36},
	               		{name: "泰州", value: 36},
	               		{name: "常熟", value: 36},
	               		{name: "连云港", value: 35},
	               		{name: "宁波", value: 33},
	               		{name: "昆山", value: 33},
	               		{name: "湖州", value: 32},
	               		{name: "太仓", value: 27},
	               		{name: "上海", value: 25},
	               		{name: "南通", value: 23},
	               		{name: "盐城", value: 15},
	               		{name: "舟山", value: 12},
	               		{name: "海门", value: 9},
	               		
	               		
	               		
	               		
			            
			            {name: "鄂尔多斯", value: 12},
			            {name: "招远", value: 12},
			            
			            {name: "齐齐哈尔", value: 14},
			            
			            {name: "赤峰", value: 16},
			            {name: "青岛", value: 18},
			            {name: "乳山", value: 18},
			            {name: "金昌", value: 19},
			            {name: "泉州", value: 21},
			            {name: "莱西", value: 21},
			            {name: "日照", value: 21},
			            {name: "胶南", value: 22},
			            
			            {name: "拉萨", value: 24},
			            {name: "云浮", value: 24},
			            {name: "梅州", value: 25},
			            {name: "文登", value: 25},
			            
			            {name: "攀枝花", value: 25},
			            {name: "威海", value: 25},
			            {name: "承德", value: 25},
			            {name: "厦门", value: 26},
			            {name: "汕尾", value: 26},
			            {name: "潮州", value: 26},
			            {name: "丹东", value: 27},
			            
			            {name: "曲靖", value: 27},
			            {name: "烟台", value: 28},
			            {name: "福州", value: 29},
			            {name: "瓦房店", value: 30},
			            {name: "即墨", value: 30},
			            {name: "抚顺", value: 31},
			            {name: "玉溪", value: 31},
			            {name: "张家口", value: 31},
			            {name: "阳泉", value: 31},
			            {name: "莱州", value: 32},
			            
			            {name: "汕头", value: 32},
			            
			            
			            {name: "湛江", value: 33},
			            {name: "揭阳", value: 34},
			            {name: "荣成", value: 34},
			            
			            {name: "葫芦岛", value: 35},
			            
			            {name: "东莞", value: 36},
			            {name: "河源", value: 36},
			            
			            
			            {name: "南宁", value: 37},
			            {name: "营口", value: 37},
			            {name: "惠州", value: 37},
			            
			            {name: "蓬莱", value: 37},
			            {name: "韶关", value: 38},
			            {name: "嘉峪关", value: 38},
			            {name: "广州", value: 38},
			            {name: "延安", value: 38},
			            {name: "太原", value: 39},
			            {name: "清远", value: 39},
			            {name: "中山", value: 39},
			            {name: "昆明", value: 39},
			            {name: "寿光", value: 40},
			            {name: "盘锦", value: 40},
			            {name: "长治", value: 41},
			            {name: "深圳", value: 41},
			            {name: "珠海", value: 42},
			            
			            {name: "咸阳", value: 43},
			            {name: "铜川", value: 44},
			            {name: "平度", value: 44},
			            {name: "佛山", value: 44},
			            {name: "海口", value: 44},
			            {name: "江门", value: 45},
			            {name: "章丘", value: 45},
			            {name: "肇庆", value: 46},
			            {name: "大连", value: 47},
			            {name: "临汾", value: 47},
			            
			            {name: "石嘴山", value: 49},
			            {name: "沈阳", value: 50},
			            
			            {name: "茂名", value: 50},
			            
			            {name: "长春", value: 51},
			            {name: "胶州", value: 52},
			            {name: "银川", value: 52},
			            
			            {name: "三门峡", value: 53},
			            {name: "锦州", value: 54},
			            {name: "南昌", value: 54},
			            {name: "柳州", value: 54},
			            {name: "三亚", value: 54},
			            {name: "自贡", value: 56},
			            {name: "吉林", value: 56},
			            {name: "阳江", value: 57},
			            {name: "泸州", value: 57},
			            {name: "西宁", value: 57},
			            {name: "宜宾", value: 58},
			            {name: "呼和浩特", value: 58},
			            {name: "成都", value: 58},
			            {name: "大同", value: 58},
			            
			            {name: "桂林", value: 59},
			            {name: "张家界", value: 59},
			            
			            {name: "北海", value: 60},
			            {name: "西安", value: 61},
			            
			            {name: "东营", value: 62},
			            {name: "牡丹江", value: 63},
			            {name: "遵义", value: 63},
			            
			            
			            
			            {name: "潍坊", value: 65},
			            {name: "重庆", value: 66},
			            
			            
			            {name: "滨州", value: 70},
			            {name: "贵阳", value: 71},
			            
			            {name: "本溪", value: 71},
			            {name: "克拉玛依", value: 72},
			            {name: "渭南", value: 72},
			            {name: "马鞍山", value: 72},
			            {name: "宝鸡", value: 72},
			            {name: "焦作", value: 75},
			            
			            {name: "北京", value: 79},
			            
			            {name: "衡水", value: 80},
			            {name: "包头", value: 80},
			            {name: "绵阳", value: 80},
			            {name: "乌鲁木齐", value: 84},
			            {name: "枣庄", value: 84},
			            {name: "杭州", value: 84},
			            {name: "淄博", value: 85},
			            {name: "鞍山", value: 86},
			            {name: "溧阳", value: 86},
			            {name: "库尔勒", value: 86},
			            {name: "安阳", value: 90},
			            {name: "开封", value: 90},
			            {name: "济南", value: 92},
			            {name: "德阳", value: 93},
			            {name: "温州", value: 95},
			            {name: "九江", value: 96},
			            {name: "邯郸", value: 98},
			            {name: "临安", value: 99},
			            {name: "兰州", value: 99},
			            {name: "沧州", value: 100},
			            {name: "临沂", value: 103},
			            {name: "南充", value: 104},
			            {name: "天津", value: 105},
			            {name: "富阳", value: 106},
			            {name: "泰安", value: 112},
			            {name: "诸暨", value: 112},
			            {name: "郑州", value: 113},
			            {name: "哈尔滨", value: 114},
			            {name: "聊城", value: 116},
			            {name: "芜湖", value: 117},
			            {name: "唐山", value: 119},
			            {name: "平顶山", value: 119},
			            {name: "邢台", value: 119},
			            {name: "德州", value: 120},
			            {name: "济宁", value: 120},
			            {name: "荆州", value: 127},
			            {name: "宜昌", value: 130},
			            {name: "义乌", value: 132},
			            {name: "丽水", value: 133},
			            {name: "洛阳", value: 134},
			            {name: "秦皇岛", value: 136},
			            {name: "株洲", value: 143},
			            {name: "石家庄", value: 147},
			            {name: "莱芜", value: 148},
			            {name: "常德", value: 152},
			            {name: "保定", value: 153},
			            {name: "湘潭", value: 154},
			            {name: "金华", value: 157},
			            {name: "岳阳", value: 169},
			            {name: "长沙", value: 175},
			            {name: "衢州", value: 177},
			            {name: "廊坊", value: 193},
			            {name: "菏泽", value: 194},
			            {name: "合肥", value: 229},
			            {name: "武汉", value: 273},
			            {name: "大庆", value: 279}
			        ];
			
			$('#datalist').grid({ data:data222,
				columns: [
							{ header: "城市" , field: 'name'},
							{ header: "PM" , field: 'value'},
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
				var html = util.tpl('html/datasource/add.html');
	        	var d = null;
	        	d = dialog({
	        	    title: util.i18n('SETTING'),
	        	    content: html,
	        	    padding: 0,
	        	    onshow: function () {
	        	    	//在弹出框里面绑定事件
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
	        	    	
	        	    	$(".ass01").on("click", function(){
	        	    		if($(".ass_list").is(":hidden")){
	        	    			$(".ass_list").show();
	        	    			$(".ass_list_table").show();
	        	    		}else{
	        	    			$(".ass_list").hide();
	        	    			$(".ass_list_table").hide();
	        	    		}
	        	    	});
	        	    	
	        	    	$(".ass_list li").on("click", function(){
	        	    		console.log("点击子节点");
	        	    		event.stopPropagation();//阻止事件冒泡
	        	    	});
	        	    	
	        	    	
	        	    	$(".ass_list_table").on("click", function(){
	        	    		event.stopPropagation();//阻止事件冒泡
	        	    	});
	        	    	
	        	    	$("#click_showTab").on("click", function(){
	        	    		var html = util.tpl('html/datasource/field.html');
	        	    		var d = null;
	        	        	d = dialog({
	        	        	    content: html,
	        	        	    padding: 0,
	        	        	    okValue: '确定',
	        	        	    ok: function () {
	        						
	        	        	    },
	        	        	    cancelValue: '取消',
	        	        	    cancel: $.noop
	        	        	});
	        	        	d.showModal();
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