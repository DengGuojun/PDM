<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.*"  %>
<%@ page import="com.lpmas.framework.page.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.web.*"  %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.admin.config.*"  %>
<%@ page import="com.lpmas.pdm.config.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%
	List<ProductDescriptionTypeBean> list = (List<ProductDescriptionTypeBean>) request.getAttribute("ProductDescriptionTypeList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品描述类型管理</title>
<%@ include file="../include/header.jsp"%>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
	<p class="article_tit">商品描述类型列表</p>
	<form id="formData" name="formData" action="/pdm/ProductDescriptionTypeList.do" method="post">
		<div class="search_form">
			<em class="em1">描述类型代码：</em><input id="typeCode" name="typeCode" value="<%=ParamKit.getParameter(request, "typeCode", "")%>" /> &nbsp;
			<em class="em1">描述类型名称：</em><input id="typeName" name="typeName" value="<%=ParamKit.getParameter(request, "typeName", "")%>" /> &nbsp;
			<em class="em1">信息类型：</em>
			<select id="infoType" name="infoType">
				<option value="">请选择</option>
				<%
					for (StatusBean<Integer, String> status : ProductPropertyConfig.INFO_TYPE_LIST) {
				%>
				<option value="<%=status.getStatus()%>" <%=ParamKit.getIntParameter(request, "infoType", 0) == status.getStatus() ? "selected" : ""%>><%=status.getValue()%></option>
				<%} %>
			</select>
			&nbsp;
			<em class="em1">有效状态：</em>
			<select id="status" name="status">
				<%
					for (StatusBean<Integer, String> status : Constants.STATUS_LIST) {
				%>
				<option value="<%=status.getStatus()%>" <%=ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID) == status.getStatus() ? "selected" : ""%>><%=status.getValue()%></option>
				<%
					}
				%>
			</select>
			<input type="submit" name="submit1" value="查找" onclick="return checkText()" class="search_btn_sub">
		</div>
		<table border=0 width="100%" cellpadding="0"  class="table_style">
			<tr>
				<th>描述类型ID</th>
				<th>描述类型代码</th>
				<th>描述类型名称</th>
				<th>信息类型</th>
				<th>优先级</th>
				<th>有效状态</th>
				<th><div align="center">操作</div></th>
			</tr>
			<%
			if (list != null && list.size() > 0) {
				for (ProductDescriptionTypeBean bean : list) {
		    %>
    			<tr>
    				<td><%=bean.getTypeId()%></td>
				<td><a href="/pdm/ProductDescriptionTypeManage.do?typeId=<%=bean.getTypeId()%>"><%=bean.getTypeCode()%></a></td>
				<td><%=bean.getTypeName()%></td>
				<td><%=MapKit.getValueFromMap(bean.getInfoType(), ProductPropertyConfig.INFO_TYPE_MAP)%></td>
				<td><%=bean.getPriority() %></td>
				<td><%=MapKit.getValueFromMap(bean.getStatus(), Constants.STATUS_MAP)%></td>
				<td align="center">
				<a href="/pdm/ProductDescriptionTypeManage.do?typeId=<%=bean.getTypeId()%>">修改</a>
				</td>
			</tr>
			<%
				}
			}
			%>
		</table>
	</form>
	<ul class="page_info">
	    <li class="page_left_btn">
			<input name="button" class="modifysubbtn" type="button" value="新增" onclick="javascript:location.href='/pdm/ProductDescriptionTypeManage.do'"> 
	    </li>
	    <li class="page_num">
	    	<%if (list != null && list.size() > 0) {%> 
			<%@ include file="../include/page.jsp"%> 
			<%}%>
	    </li>
	</ul>
</body>
</html>