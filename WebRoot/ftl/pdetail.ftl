<table border=1>
	<tr>
		<th>商品名称</th>
		<th>商品规格</th>
		<th>商品计量单位</th>
		<th>采购数量</th>
		<th>采购价格</th>
		<th>采购金额</th>
	</tr>
<#list detaillist as detail>
	<tr>
		<td width=200>${detail.name}</td>
		<td width=120>${detail.spec}</td>
		<td width=120>${detail.unit}</td>
		<td width=100>${detail.purchase_num}</td>
		<td width=100>${detail.purchase_price}</td>
		<td width=100>${detail.purchase_money}</td>
	</tr>
</#list>
</table>