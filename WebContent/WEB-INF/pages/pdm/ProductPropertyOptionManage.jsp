<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@page import="com.lpmas.framework.web.ParamKit"%>
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
	ProductPropertyOptionBean propertyOptionBean = (ProductPropertyOptionBean) request.getAttribute("propertyOptionBean");
	//分类属性类型
	ProductPropertyTypeBean propertyTypeBean = (ProductPropertyTypeBean) request.getAttribute("PropertyTypeBean");
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	int propertyId = (Integer)request.getAttribute("PropertyId");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>编辑属性分类选项</title>
<%@ include file="../include/header.jsp"%>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="../js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductPropertyTypeList.do">商品属性列表</a>&nbsp;>&nbsp;</li>
			<li><%=propertyTypeBean.getPropertyName() %>&nbsp;>&nbsp;</li>
			<%if (propertyOptionBean != null && propertyOptionBean.getOptionId() > 0) {%>
			<li>修改商品属性选项信息</li>
			<%} else {%>
			<li>新增商品属性选项信息</li>
			<%}%>
		</ul>
	</div>
	<div class="article_tit">
		<p class="tab">
		<a href="ProductPropertyTypeManage.do?propertyId=<%=propertyId%>&readOnly=<%=readOnly%>">商品属性信息</a> 
		<a href="ProductPropertyOptionManage.do?propertyId=<%=propertyId%>&readOnly=<%=readOnly%>">商品属性选项</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<form id="formData" name="formData" method="post" action="/pdm/ProductPropertyOptionManage.do" onsubmit="javascript:return checkForm('formData');">
		<div class="modify_form">
	    		<p>
	      		<em class="int_label">属性代码：</em><em><%=propertyTypeBean.getPropertyName()%></em>
				<input type="hidden" id="propertyId" name="propertyId" value="<%=propertyId%>"> 
				<input type="hidden" id="optionId" name="optionId" value="<%=propertyOptionBean.getOptionId()%>" /> 
			</p>
	    		<p>
	    	  		<em class="int_label"><span>*</span>属性选项值：</em>
				<input type="text" name="optionValue" id="optionValue" value="<%=propertyOptionBean.getOptionValue()%>" maxlength="2000" checkStr="属性值;txt;true;;2000" />
			</p>
			<p>
	    	  		<em class="int_label"><span>*</span>属性选项显示内容：</em>
				<input type="text" name="optionContent" id="optionContent" value="<%=propertyOptionBean.getOptionContent()%>" maxlength="2000" checkStr="属性内容;txt;true;;2000" />
			</p>
			<p>
		      <em class="int_label">有效状态：</em>
		      <input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(propertyOptionBean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
		    </p>
		</div>
		<div class="div_center">
			<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
			<a href="ProductPropertyOptionList.do?propertyId=<%=propertyId%>&readOnly=<%=readOnly%>"><input type="button" name="cancel" id="cancel" class="modifysubbtn" value="返回" /></a>
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