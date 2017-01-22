<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<ProductDescriptionBean> descriptionList = (List<ProductDescriptionBean>) request.getAttribute("ProductInfoDescriptionList");
	ProductInfoBean bean = (ProductInfoBean) request.getAttribute("ProductInfoBean");
	int productId = bean.getProductId();

	List<ProductDescriptionTypeBean> descTypelist = (List<ProductDescriptionTypeBean>) request.getAttribute("ProductDescriptionTypeList");
	Map<String, String> map = new HashMap<String, String>();
	if (!descriptionList.isEmpty()) {
		map = ListKit.list2Map(descriptionList, "descCode", "descValue");
	}
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品信息描述</title>
	<%@ include file="../include/header.jsp"%>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductInfoList.do">商品列表</a>&nbsp;>&nbsp;</li>
			<% if(!descriptionList.isEmpty()) {%>
			<li><%=bean.getProductName()%>&nbsp;>&nbsp;</li>
			<li>修改商品描述</li>
			<%} else { %>
			<li>新建商品描述</li>
			<%} %>
		</ul>
	</div>
	<%@ include file="../nav/ProductInfoNav.jsp" %>
	<form id="formData" name="formData" method="post" action="/pdm/ProductInfoDescriptionManage.do?" onsubmit="javascript:return checkForm('formData');">
		<input id="productId" name="productId" value="<%=productId%>" type="hidden" />
		<%
		if(descTypelist.size()>0){
			%>
			<div class="modify_form">
			<%
			for (ProductDescriptionTypeBean descType : descTypelist) {
			%>
		    		<p>
			   		<em class="int_label"><%=descType.getTypeName()%>：</em>
					<input type="text" id="DESC_<%=descType.getTypeCode()%> " name="DESC_<%=descType.getTypeCode()%>"
						value="<%=map.get(descType.getTypeCode()) == null ? "" : map.get(descType.getTypeCode())%>" size="80"/>	
				</p>
			<%}%>
			</div>
			<div class="div_center">
				<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
				<input type="button" name="cancel" id="cancel" class="modifysubbtn" value="返回" onclick="window.history.back()" />
			</div>
		<%} %>
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