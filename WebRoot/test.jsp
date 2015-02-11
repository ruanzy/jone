<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<style>
.accordion {width: 213px; padding: 40px 28px 25px 0;border:0px solid #ccc;} 
ul.accordion {padding: 0; margin: 0; font-size: 1em; line-height: 0.5em; list-style: none;} 
ul.accordion li {} 
ul.accordion li a {line-height: 10px; font-size: 14px; padding: 10px 5px; color: #000; display: block;
text-decoration: none; font-weight: bolder;}
ul.accordion li a:hover {background-color:#675C7C;    color:white;}
ul.accordion ul { margin: 0; padding: 0;display: none;} 
ul.accordion ul li { margin: 0; padding: 0; clear: both;} 
ul.accordion ul li a { padding-left: 20px; font-size: 12px; font-weight: normal;}
ul.accordion ul li a:hover {background-color:#D3C99C; color:#675C7C;} 
ul.accordion ul ul li a {color:silver; padding-left: 40px;} 
ul.accordion ul ul li a:hover { background-color:#D3CEB8; color:#675C7C;} 
ul.accordion span{float:right;}
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
<ul class="accordion">
   <li><a href="#">首页</a></li>
   <li><a href="#">服务</a></li>
   <li><a href="#">案例</a></li>
   <li><a href="#">文章</a></a>
        <ul>
           <li><a href="#">XHTML/CSS</a></li>
           <li><a href="#">Javascript/Ajax/jQuery</a>
                <ul>
                    <li><a href="#">Cookies</a></li>
                    <li><a href="#">Event</a></li>
                    <li><a href="#">Games</a></li>
                    <li><a href="#">Images</a></li>
                </ul>
            </li>
            <li><a href="#">PHP/MYSQL</a></li>
            <li><a href="#">前端观察</a></li>
            <li><a href="#">HTML5/移动WEB应用</a></li>
        </ul>
    </li>
    <li><a href="#">关于</a></li>
</ul>

<table>
  <tr>
    <th>Column 1 Heading</th>
    <th>Column 2 Heading</th>
  </tr>
  <tr>
    <td>Row 1: Col 1</td>
    <td>Row 1: Col 2</td>
  </tr>
</table>

<script type="text/javascript">
	$(".accordion").accordion({
        speed: 500,
        closedSign: '+',
        openedSign: '-'
    });
	$('#viewcode').click(function() {
		$.dialog({
			title : '查看源码',
			url : 'code.html',
			padding : '0',
			buttons : [ {
				text : '关闭',
				cls : 'btn-default',
				action : function(d) {
					d.close();
				}

			} ],
			onShow : function(){
				$.get('login2.html', function(data,status,xhr){
					var highLightCode2 = HighLighter.highLight('html', 'rDark', data);
					$('#code').html(highLightCode2);
				});
			}
		});
	});
	var objDelete = {
	    text: "删除",
	    func: function() {
			this.remove();
	    }    
	}, objRubbish = {
	    text: "分配角色",
	    func: function() {
	        
	    }    
	};
	var mailMenuData = [
	    [objDelete, objRubbish]
	];
	$('tr').smartMenu(mailMenuData);
</script>