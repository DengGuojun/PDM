<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.config.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.config.*"  %>
<%@ page import="com.lpmas.framework.web.*"  %>
<% 
	MaterialTypeBean bean = (MaterialTypeBean)request.getAttribute("MaterialTypeBean");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	int typeId = bean.getTypeId();
	List<StatusBean<String, String>> MaterialTypeConfigStatuslist = (List<StatusBean<String, String>>) request.getAttribute("MaterialTypeConfigStatuslist");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>物料类型管理</title>
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
			<% if(typeId > 0) {%> 
			<li><%=bean.getTypeName() %>&nbsp;>&nbsp;</li>
			<li>修改物料类型</li>
			<%}else{ %>
			<li>新建物料类型</li>
			<%}%>
		</ul>
	</div>
 <%if(typeId > 0){%>
	<div class="article_tit">
		<p class="tab">
		<a href="MaterialTypeManage.do?typeId=<%=typeId%>&readOnly=<%=readOnly%>">物料分类管理</a> 
		<%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_TYPE_PROPERTY, OperationConfig.SEARCH)){ %>
		<a href="MaterialTypePropertyManage.do?typeId=<%=typeId%>&readOnly=<%=readOnly%>">物料分类属性管理</a>
		<%} %>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<form id="formData" name="formData" method="post" action="MaterialTypeManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="typeId" id="typeId" value="<%=typeId %>"/>
	  <div class="modify_form">
	    <p>
	      <em class="int_label"><span>*</span>分类代码：</em>
	      <input type="text" name="typeCode" id="typeCode" size="20" maxlength="10" value="<%=bean.getTypeCode() %>" checkStr="分类代码;code;true;;10"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>分类名称：</em>
	      <input type="text" name="typeName" id="typeName" size="30" maxlength="100" value="<%=bean.getTypeName() %>" checkStr="分类名称;txt;true;;100"/>
	    </p>
	    <p>
	      <em class="int_label">有效状态：</em>
	      <input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
	    </p>
	  </div>
	  <div class="div_center">
	  	<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
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