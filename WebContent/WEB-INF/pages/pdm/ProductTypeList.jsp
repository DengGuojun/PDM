<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.bean.*"%>
<%@ page import="com.lpmas.framework.page.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.admin.config.*"%>
<%@ page import="com.lpmas.pdm.config.*"%>
<%@ page import="com.lpmas.pdm.bean.*"%>
<%
	AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	List<ProductTypeBean> list = (List<ProductTypeBean>) request.getAttribute("ProductTypeList");

	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	int parentTypeId = (Integer) request.getAttribute("ParentTypeId");
	List<ProductTypeBean> treeList = (List<ProductTypeBean>) request.getAttribute("ParentTreeList");
%>

<%@ include file="../include/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品类型管理</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
</head>
<body class="article_bg">
	<%
		if (parentTypeId > 0) {
	%>
	<div class="article_tit">
		<ul class="art_nav">
			<li><a href="ProductTypeList.do">商品类型管理</a></li>
			<%
				for (ProductTypeBean productTypeBean : treeList) {
			%>
			<li>&nbsp;>&nbsp;<%=productTypeBean.getTypeName()%></li>
			<%
				}
			%>
		</ul>
	</div>
	<%
		} else {
	%>
	<p class="article_tit">商品类型管理</p>
	<%
		}
	%>
	<form name="formSearch" method="post" action="ProductTypeList.do">
		<div class="search_form">
			<em class="em1">商品类型代码：</em> <input type="text" name="typeCode"
				id="typeCode"
				value="<%=ParamKit.getParameter(request, "typeCode", "")%>"
				size="20" /> <em class="em1">商品类型名称：</em> <input type="text"
				name="typeName" id="typeName"
				value="<%=ParamKit.getParameter(request, "typeName", "")%>"
				size="20" /> <em class="em1">有效状态：</em> <select name="status"
				id="status">
				<%
					int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
					for (StatusBean<Integer, String> statusBean : Constants.STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == status) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select> <input type="hidden" name="parentTypeId" id="parentTypdId"
				value="<%=parentTypeId%>" />
			<%
				if ((adminUserHelper.hasPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.SEARCH) && parentTypeId == 0)
						|| (adminUserHelper.hasPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.SEARCH)
								&& parentTypeId > 0)) {
			%>
			<input name="" type="submit" class="search_btn_sub" value="查询" />
			<%
				}
			%>
		</div>
		<table width="100%" border="0" cellpadding="0" class="table_style">
			<tr>
				<th>商品类型ID</th>
				<th>商品类型代码</th>
				<th>商品类型名称</th>
				<th>有效状态</th>
				<th>操作</th>
			</tr>
			<%
				for (ProductTypeBean bean : list) {
			%>
			<tr>
				<td><%=bean.getTypeId()%></td>
				<td><%=bean.getTypeCode()%></td>
				<td><%=bean.getTypeName()%></td>
				<td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
				<td align="center"><a
					href="/pdm/ProductTypeManage.do?typeId=<%=bean.getTypeId()%>&parentTypeId=<%=bean.getParentTypeId()%>&readOnly=true">查看</a>
					<%
						if ((adminUserHelper.hasPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.UPDATE)
									&& parentTypeId == 0)
									|| (adminUserHelper.hasPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.UPDATE)
											&& parentTypeId > 0)) {
					%>
					| <a
					href="/pdm/ProductTypeManage.do?typeId=<%=bean.getTypeId()%>&parentTypeId=<%=bean.getParentTypeId()%>&readOnly=false">修改</a>
					<%
						}
					%> <%
 					if (parentTypeId == 0 && adminUserHelper.hasPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.SEARCH)) {
					 %>
					| <a href="/pdm/ProductTypeList.do?parentTypeId=<%=bean.getTypeId()%>">查看子分类</a>
					<%
						}
					%></td>
			</tr>
			<%
				}
			%>
		</table>
	</form>
	<ul class="page_info">
		<li class="page_left_btn">
			<%
				if ((adminUserHelper.hasPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.CREATE) && parentTypeId == 0)
						|| (adminUserHelper.hasPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.CREATE)
								&& parentTypeId > 0)) {
			%>
			<input type="button" name="button" id="button" value="新建"
			onclick="javascript:location.href='ProductTypeManage.do?parentTypeId=<%=parentTypeId%>'">
			<%
				}
			%> <%
 	if (parentTypeId > 0) {
 %> <a href="ProductTypeList.do"><input
				type="button" name="button" id="button" value="返回"></a> <%
 	}
 %>
		</li>
		<%@ include file="../include/page.jsp"%>
	</ul>
</body>
</html>