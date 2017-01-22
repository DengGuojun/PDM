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
ProductDescriptionTypeBean descTypeBean = (ProductDescriptionTypeBean) request.getAttribute("ProductDescriptionTypeBean");
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
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductDescriptionTypeList.do">商品描述类型列表</a>&nbsp;>&nbsp;</li>
			<% if(descTypeBean.getTypeId() > 0) {%>
			<li><%=descTypeBean.getTypeName() %>&nbsp;>&nbsp; </li>
			<li>修改商品描述类型</li>
			<%} else { %>
			<li>新建商品描述类型</li>
			<%} %>
		</ul>
	</div>
	<form id="formData" name="formData" method="post" action="/pdm/ProductDescriptionTypeManage.do" onsubmit="javascript:return checkForm('formData');">
		<div class="modify_form">
		<input type="hidden" id="typeId" name="typeId"value="<%=descTypeBean.getTypeId()%>" /></p>
    		<p>
      		<em class="int_label"><span>*</span>描述类型代码：</em>
			<input id="typeCode" name="typeCode" value="<%=descTypeBean.getTypeCode()%>" maxlength="50" checkStr="描述类型代码;character;true;;50" />  
    		<p>
      		<em class="int_label"><span>*</span>描述类型名称：</em>
			<input id="typeName" name="typeName" value="<%=descTypeBean.getTypeName()%>" maxlength="200" checkStr="描述类型名称;txt;true;;200" /> 
		</p>
	    <p>
	      <em class="int_label"><span>*</span>信息类型：</em>
			<select id="infoType" name="infoType">
				<%for(StatusBean<Integer, String> item : ProductPropertyConfig.INFO_TYPE_LIST) {%>
					<option value="<%=item.getStatus()%>" <%=item.getStatus()==descTypeBean.getInfoType()?"selected":"" %>><%=item.getValue() %></option>
				<%} %>
				</select>
			</p>
		    <p>
		      <em class="int_label"><span>*</span>优先级：</em>
				<input id="priority" name="priority" value="<%=descTypeBean.getPriority()%>" maxlength="6" checkStr="优先级;num;true;;6" />
			</p>
		    <p>
		      <em class="int_label">有效状态：</em>
			  <input type="checkbox" id="status" name="status" value="<%=Constants.STATUS_VALID%>" <%=(descTypeBean.getStatus() == Constants.STATUS_VALID) ? "checked" : ""%> />
			</p>
			</div>
			<div class="div_center">
				<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
				<input type="button" name="cancel" id="cancel" class="modifysubbtn" value="返回" onclick="window.history.back()" />
			</div>
	</form>
</body>
</html>