<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
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
	List<MaterialPropertyTypeBean> list = (List<MaterialPropertyTypeBean>) request
			.getAttribute("PropertyTypeList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	int parentPropertyId = (Integer) request.getAttribute("ParentPropertyId");
	MaterialPropertyTypeBean parentPropertyBean = (MaterialPropertyTypeBean) request
			.getAttribute("ParentPropertyBean");
	Map<Integer, MaterialTypeBean> materialTypeMap = (Map<Integer, MaterialTypeBean>) request
			.getAttribute("materialTypeMap");
	List<MaterialTypeBean> materialTypeList = (List<MaterialTypeBean>) request.getAttribute("materialTypeList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物料属性类型配置</title>
<%@ include file="../include/header.jsp"%>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
</head>
<body class="article_bg">
	<%
		if (parentPropertyId > 0) {
	%>
	<div class="article_tit">
		<ul class="art_nav">
			<li><a href="MaterialPropertyTypeList.do">物料属性类型配置列表</a></li>
			<li>&nbsp;>&nbsp;<%=parentPropertyBean.getPropertyName()%></li>
		</ul>
	</div>
	<%
		} else {
	%>
	<p class="article_tit">物料属性类型配置列表</p>
	<%
		}
	%>

	<form id="formData" name="formData"
		action="MaterialPropertyTypeList.do" method="post">
		<input type="hidden" name="parentPropertyId" id="parentPropertyId"
			value=<%=parentPropertyId%>>
		<div class="search_form">
			<em class="em1">属性代码：</em> <input type="text" id="propertyCode"
				name="propertyCode"
				value="<%=ParamKit.getParameter(request, "propertyCode", "")%>" />
			&nbsp; <em class="em1">属性名称：</em> <input type="text"
				id="propertyName" name="propertyName"
				value="<%=ParamKit.getParameter(request, "propertyName", "")%>" />
			&nbsp; <em class="em1">所属物料类型：</em> <select id="materialType"
				name="materialType">
				<option value="">请选择</option>
				<%
					for (MaterialTypeBean typeBean : materialTypeList) {
				%>
				<option value="<%=typeBean.getTypeId()%>"
					<%=ParamKit.getIntParameter(request, "materialType", 0) == (Integer) typeBean.getTypeId()
						? "selected" : ""%>><%=typeBean.getTypeName()%></option>
				<%
					}
				%>
			</select>&nbsp; <em class="em1">属性类型：</em> <select id="propertyType"
				name="propertyType">
				<option value="">请选择</option>
				<%
					for (StatusBean<Integer, String> propertyType : MaterialPropertyConfig.PROP_TYPE_LIST) {
				%>
				<option value="<%=propertyType.getStatus()%>"
					<%=ParamKit.getIntParameter(request, "propertyType", 0) == (Integer) propertyType.getStatus()
						? "selected" : ""%>><%=propertyType.getValue()%></option>
				<%
					}
				%>
			</select>&nbsp; <em class="em1">有效状态：</em> <select id="status" name="status">
				<%
					for (StatusBean statusBean : Constants.STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=ParamKit.getIntParameter(request, "status",
						Constants.STATUS_VALID) == ((Integer) statusBean.getStatus()) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
			<%
				if (adminUserHelper.hasPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.SEARCH)) {
			%>
			<input type="submit" name="submit1" value="查询" class="search_btn_sub">
			<%
				}
			%>
		</div>
		<table border=0 width="100%" cellpadding="0" class="table_style">
			<tr>
				<th width="5%">属性ID</th>
				<th width="15%">属性代码</th>
				<th width="15%">属性名称</th>
				<th width="10%">所属物料分类</th>
				<th width="10%">属性类型</th>
				<th width="10%">输入方式</th>
				<th width="10%">是否必填</th>
				<th width="15%"><div align="center">操作</div></th>
			</tr>
			<%
				for (MaterialPropertyTypeBean bean : list) {
			%>
			<tr>
				<td><%=bean.getPropertyId()%></td>
				<td><%=bean.getPropertyCode()%></td>
				<td><%=bean.getPropertyName()%></td>
				<td><%=MapKit.getValueFromMap(bean.getMaterialTypeId(), materialTypeMap)%></td>
				<td><%=MapKit.getValueFromMap(bean.getPropertyType(), MaterialPropertyConfig.PROP_TYPE_MAP)%></td>
				<td><%=MapKit.getValueFromMap(bean.getInputMethod(), MaterialPropertyConfig.INPUT_METHOD_MAP)%></td>
				<td><%=MapKit.getValueFromMap(bean.getIsRequired(), Constants.SELECT_MAP)%></td>
				<td align="center"><a
					href="/pdm/MaterialPropertyTypeManage.do?propertyId=<%=bean.getPropertyId()%>&parentPropertyId=<%=parentPropertyId%>&readOnly=true">查看</a>
					<%
						if (adminUserHelper.hasPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.UPDATE)) {
					%>
					| <a
					href="/pdm/MaterialPropertyTypeManage.do?propertyId=<%=bean.getPropertyId()%>&parentPropertyId=<%=parentPropertyId%>&readOnly=false">修改</a>
					<%
						}
					%> <%
 	if (parentPropertyBean == null) {
 %> | <a
					href="/pdm/MaterialPropertyTypeList.do?parentPropertyId=<%=bean.getPropertyId()%>">查看子属性</a>
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
				if (adminUserHelper.hasPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.CREATE)) {
			%>
			<input name="addBtn" type="button" class="modifysubbtn" class="BTN1"
			value="新建"
			onclick="javascript:document.location='/pdm/MaterialPropertyTypeManage.do?parentPropertyId=<%=parentPropertyId%>'">
			<%
				}
			%>
		</li>
		<li class="page_num">
			<%
				if (list != null && list.size() > 0) {
			%> <%@ include
				file="../include/page.jsp"%> <%
 	}
 %>
		</li>
	</ul>
</body>