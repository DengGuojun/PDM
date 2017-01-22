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
	List<MaterialPropertyOptionBean> list = (List<MaterialPropertyOptionBean>) request.getAttribute("PropertyOptionList");
	MaterialPropertyTypeBean propertyTypeBean = (MaterialPropertyTypeBean) request.getAttribute("PropertyTypeBean");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	String propertyId = ParamKit.getParameter(request, "propertyId","").trim();
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物料属性配置选项</title>
<%@ include file="../include/header.jsp"%>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="MaterialPropertyTypeList.do">物料属性列表</a>&nbsp;>&nbsp;</li>
			<li><%=propertyTypeBean.getPropertyName() %>&nbsp;>&nbsp;</li>
			<li>物料属性选项信息列表</li>
		</ul>
	</div>
	<div class="article_tit">
		<p class="tab">
		<a href="MaterialPropertyTypeManage.do?propertyId=<%=propertyId%>&readOnly=<%=readOnly%>">物料属性信息</a> 
		<a href="MaterialPropertyOptionList.do?propertyId=<%=propertyId%>&readOnly=<%=readOnly%>">物料属性选项</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<table border=0 width="100%" cellpadding="0" class="table_style">
		<tr>
			<th width="5%">选项ID</th>
			<th width="12%">选型值</th>
			<th width="15%">选项内容</th>
			<th width="15%">有效状态</th>
			<th width="10%"><div align="center">操作</div></th>
		</tr>
		<%
			for (MaterialPropertyOptionBean bean : list) {
	    %>
   		<tr>
			<td><%=bean.getOptionId()%></td>
			<td><%=bean.getOptionValue()%></td>
			<td><%=bean.getOptionContent()%></td>
			<td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
			<td align="center">
			<%if(readOnly.equals("true")){ %>
			<a href="/pdm/MaterialPropertyOptionManage.do?propertyOptionId=<%=bean.getOptionId()%>&readOnly=<%=readOnly%>">查看</a>
			<%} else if(adminUserHelper.hasPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.UPDATE) && readOnly.equals("false")){ %>
			<a href="/pdm/MaterialPropertyOptionManage.do?propertyOptionId=<%=bean.getOptionId()%>&readOnly=<%=readOnly%>">修改</a>
			<%} %> 
			</td>
		</tr>
		<%
			}
		%>
	</table>
	<ul class="page_info">
	    <li class="page_left_btn">
	    		<%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.UPDATE) && readOnly.equals("false")){ %>
			<input name="addBtn" type="button" class="BTN1" value="新增" onclick="javascript:document.location='/pdm/MaterialPropertyOptionManage.do?propertyId=<%=propertyId%>'">
			<%} %> 
	    </li>
	    <li class="page_num">
	    	<%if (list != null && list.size() > 0) {%> 
			<%@ include file="../include/page.jsp"%> 
			<%}%>
	    </li>
	</ul>
</body>