<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="article_tit">
		<p class="tab">
		<a href="ProductItemManage.do?itemId=<%=bean.getItemId()%>&readOnly=<%=readOnly%>">基本信息</a>
		<a href="ProductItemStorageManage.do?itemId=<%=bean.getItemId()%>&readOnly=<%=readOnly%>">仓储信息</a>
		<a href="ProductItemImageManage.do?itemId=<%=bean.getItemId()%>&readOnly=<%=readOnly%>">商品项图片</a>
		<!-- <a href="ProductItemDescriptionManage.do?itemId=<%=bean.getItemId()%>&readOnly=<%=readOnly%>">商品项描述</a> -->
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>