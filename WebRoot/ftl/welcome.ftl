<pre>
	基础开发平台<font color="red">B</font>ase <font color="red">D</font>eveloper <font
		color="red">P</font>latform
	
	1.零配置，全系统只有一个Filter
	
	Action是一个普通的pojo，与servlet解耦，无侵入 REST风格的访问方式
	
	2.WEB和Service分布式部署，基于netty框架的RPC
	
	Service是一个普通的pojo
	
	3.事务采用AOP实现无侵入设计。日志的记录采用AOP，异步阻塞队列实现。
	
	我们的宗旨意在开发一个公共通用的权限管理、日志管理、异常管理、事务管理、任务管理等模块，易于集成的
	
	开发平台.让我们的开发人员不再为琐碎的权限菜单、日志、异常、事务、任务而“费劲“，开发者只需关注业务的实现。
	
	目前支持MYSQL、ORACLE、SqlServer等主流数据库无缝切换，MONGODB测试中，敬请期待...
	
	如果你对该系统采用的技术感兴趣，请联系我。后续我们会不断重构代码重构。
</pre>
			<button type="button" onclick="a()">保存</button>
			<script type="text/javascript">
			function a(){
			$.dialog({
				title:'asd',
				width: 800,
				height:390,
				url:'view/income/list.html'
			});
			}
			</script>