<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<style>
.selectmenu {
	position: relative;
	z-index: 100;
	width: 270px;
	font-family: arial, helvetica, clean, "microsoft yahei", "宋体",
		sans-serif;
}

.selectmenu dt {
	height: 24px;
	line-height: normal;
	padding: 8px 30px 0px 10px;
	font-size: 14px;
	font-weight: normal;
	cursor: pointer;
	color: #333;
	border: 1px solid #ccc;
}

.selectmenu dt.expand {
	border-color: #A1A1A1;
}

.selectmenu i {
	position: absolute;
	width: 11px;
	height: 7px;
	top: 12px;
	right: 10px;
	cursor: pointer;
}

.selectmenu dd {
	position: absolute;
	left: 0px;
	top: 33px;
	border: 1px solid #A1A1A1;
	background: none repeat scroll 0% 0% #FFF;
	display: none;
	z-index: 100;
	width: 268px;
}

/**a {
    color: #16387C;
    text-decoration: none;
}

.price-tab a:hover{
background: #ddd;
}**/
ul {
	padding: 0;
}
</style>
<div style="">
	<button id='addbtn' class='btn btn-success' funcid='102'>
		<i class="icon-plus-sign"></i> 增加
	</button>
	<button id='delbtn' class='btn btn-success' funcid='103'>
		<i class="icon-pencil"></i> 修改
	</button>
	<button id='assignbtn' class='btn btn-success' funcid='104'>
		<i class="icon-remove-sign"></i> 删除
	</button>
</div>
<dl class="selectmenu">
	<dt></dt>
	<dd>
		<ul id="departtree" class="ztree" style="overflow:auto;"></ul>
	</dd>
	<i class="icon-angle-down"></i>
</dl>
<script type="text/javascript">
	var setting = {
		view : {
			dblClickExpand : false,
			showLine : true
		},
		data : {
			simpleData : {
				enable : true,
				pIdKey : "pid",
				rootPId : 0
			}
		}
	};

	function initTree(selectNodeId) {
		var treeNodes;
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			url : "depart/tree",
			success : function(data) {
				treeNodes = data;
			}
		});
		$.fn.zTree.init($("#departtree"), setting, treeNodes);
		var treeObj = $.fn.zTree.getZTreeObj("departtree");
		treeObj.expandAll(true);
		var node = treeObj.getNodeByParam("id", selectNodeId);
		treeObj.selectNode(node, false);
	}

	initTree(1);
	var sm = $('.selectmenu');
	var dt = $('dt', sm);
	var list = $('dd', sm);
	dt.click(function(e) {
		$(this).addClass('expand');
		list.show();
		e.stopPropagation();
	});
	$(document).click(function() {
		list.hide();
		dt.removeClass('expand');
	});
	list.delegate('a', 'click', function(e) {
		dt.html($(this).html());
		list.hide();
		dt.removeClass('expand');
		e.stopPropagation();
	});
</script>