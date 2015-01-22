<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<style>
.price-choice-box {
    position: relative;
    z-index: 100;
    width: 270px;
    font-family: arial,helvetica,clean,"microsoft yahei","宋体",sans-serif;
}
.price-choice-box-expand .price-active {
    border-color: #A1A1A1;
}
.price-active {
    width: 228px;
    height: 24px;
    line-height: normal;
    padding: 8px 30px 0px 10px;
    font-size: 14px;
    font-weight: normal;
    cursor: pointer;
    color: #333;
}

.price-active-focus {
border: 1px solid #A1A1A1;
}
.mobile-number, .price-active {
    width: 248px;
    color: #333;
    font-weight: bold;
    height: 32px;
    outline: medium none;
    padding: 0px 10px;
    line-height: 32px;
    vertical-align: middle;
    border: 1px solid #DDD;
}
.clearfix:after {
    clear: both;
}
.clearfix:after {
    content: "";
    visibility: hidden;
    display: block;
    height: 0px;
    clear: both;
}
.clearfix:before, .clearfix:after {
    content: "";
    display: table;
}
.price-choice-box .arrow {
    position: absolute;
    width: 11px;
    height: 7px;
    background: url("css/mobile-recharge-price-arrow-up.png") no-repeat scroll 0% 0% transparent;
    top: 14px;
    right: 10px;
    cursor: pointer;
}
.price-choice-box .price-tab {
    position: absolute;
    left: 0px;
    top: 33px;
    border: 1px solid #A1A1A1;
    background: none repeat scroll 0% 0% #FFF;
    display: none;
    z-index: 100;
}
.price-choice-box .price-tab a .rebate {
    display: none;
}
.price-choice-box .rebate {
    float: right;
    color: #666;
    font-size: 12px;
}
.price-choice-box .price-tab .rebate em {
    color: #666;
}
.price-choice-box .rebate em {
    color: #FF6500;
    margin-left: 5px;
}
#price-list{
    display: none;
    width: 268px;
 border: 1px solid #A1A1A1;
  border-top: 0;
    font-size: 14px;
    font-weight: normal;
    overflow: hidden;
    color: #333;
    text-decoration: none;
}
/**a {
    color: #16387C;
    text-decoration: none;
}

.price-tab a:hover{
background: #ddd;
}**/

ul{padding:0;}
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
<div class="form-content form-content-product price-choice-box"
	id="price-choice-box">
	<div data-flag="30" data-price="3000" class="form-input price-active"
		id="price-active"><span class="price">30元</span>
	</div>
	<div id="price-list">
		<ul id="departtree" class="ztree"
			style="overflow:auto;"></ul>
	</div>
	<i class="arrow">&nbsp;</i>
</div>
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
	
	$('.price-choice-box').click(function(e){
		$(this).addClass('price-choice-box-expand');
		$('#price-list').show();
		e.stopPropagation();
	});
	$(document).click(function(){
		$('#price-list').hide();
		$('.price-choice-box').removeClass('price-choice-box-expand');
	});
	$('#price-list').delegate('a', 'click', function(e){
		$('#price-active').html($(this).html());
		$('#price-list').hide();
		$('.price-choice-box').removeClass('price-choice-box-expand');
		e.stopPropagation();
	});
</script>