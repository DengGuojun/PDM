<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="article_tit">
	<p class="tab">
	<a href="ProductInfoManage.do?productId=<%=bean.getProductId()%>&readOnly=<%=readOnly%>">基本信息</a>
	<a href="ProductInfoStorageManage.do?productId=<%=bean.getProductId()%>&readOnly=<%=readOnly%>">仓储信息</a>
	<!--  <a href="ProductInfoImageManage.do?productId=<%=bean.getProductId()%>&readOnly=<%=readOnly%>">商品图片</a>-->
	<!-- <a href="ProductInfoDescriptionManage.do?productId=<%=bean.getProductId()%>&readOnly=<%=readOnly%>">商品描述</a> -->
	</p>
	<script>
	tabChange('.tab', 'a');
	</script>
</div>