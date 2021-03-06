<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.admin.config.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.config.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean"  %>
<%@page import="com.lpmas.framework.util.*"%>
<%@page import="com.lpmas.framework.web.*"%>
<% 
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	MaterialTypeBean bean = (MaterialTypeBean)request.getAttribute("MaterialTypeBean");
	List<MaterialTypePropertyBean> list = (List<MaterialTypePropertyBean>)request.getAttribute("MaterialTypePropertyList");
	List<MaterialTypePropertyBean> defaultValueList = (List<MaterialTypePropertyBean>)request.getAttribute("PropertyDefaultValueList");
	List<StatusBean<String, String>> materialTypePropertyFieldList = (List<StatusBean<String, String>>)request.getAttribute("MaterialTypePropertyFieldList");
	int typeId = ParamKit.getIntParameter(request, "typeId", 0);
	Map<String, String> map = new HashMap<String, String>();
	if (list != null && list.size() > 0) {
		map = ListKit.list2Map(list, "PropertyCode", "PropertyValue");
	}
	Map<String, String> defaultValueMap = new HashMap<String, String>();
	if (defaultValueList != null && defaultValueList.size() > 0) {
		defaultValueMap = ListKit.list2Map(defaultValueList, "PropertyCode", "PropertyValue");
	}
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>物料类型属性管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="MaterialTypeList.do">物料类型管理</a>&nbsp;>&nbsp;</li>
			<li><%=bean.getTypeName() %>&nbsp;>&nbsp;</li>
			<li>修改物料类型属性</li>
		</ul>
	</div>
	<div class="article_tit">
		<p class="tab">
		<a href="MaterialTypeManage.do?typeId=<%=typeId%>&readOnly=<%=readOnly%>">物料分类管理</a> 
		<a href="MaterialTypePropertyManage.do?typeId=<%=typeId%>&readOnly=<%=readOnly%>">物料分类属性管理</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<form id="formData" name="formData" method="post" action="MaterialTypePropertyManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="typeId" id="typeId" value="<%=typeId %>"/>
	  <div class="modify_form">
	    <%
		if(materialTypePropertyFieldList!=null && materialTypePropertyFieldList.size()>0){
			for (StatusBean<String, String> statusBean : materialTypePropertyFieldList) {
		%>
		    <p>
		      <em class="int_label"><%=statusBean.getValue() %>：</em>
		      <input type="text" name="<%=statusBean.getStatus()%>" id="<%=statusBean.getStatus()%> " size="100" maxlength="100" 
		      value="<%=MapKit.getValueFromMap(statusBean.getStatus(), map) %>" checkStr="物料分类属性配置;text;false;;100"/>
		       <%=MapKit.getValueFromMap(statusBean.getStatus(), defaultValueMap) %>
		    </p>
	    <% 
			}
		}
	    %>
	  </div>
	  <div class="div_center">
	  <%if((adminUserHelper.hasPermission(PdmResource.MATERIAL_TYPE_PROPERTY, OperationConfig.CREATE) && map.size() == 0)
	  			|| (adminUserHelper.hasPermission(PdmResource.MATERIAL_TYPE_PROPERTY, OperationConfig.UPDATE) && map.size() > 0 )){ %>
	  	<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <%} %>	
	  <a href="MaterialTypeList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readonly = '${readOnly}';
	if(readonly=='true') {
		disablePageElement();
	}
});
</script>
</html>