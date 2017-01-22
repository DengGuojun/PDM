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
	List<ProductDescriptionBean> descriptionList = (List<ProductDescriptionBean>) request.getAttribute("ProductItemDescriptionList");
	ProductItemBean bean = (ProductItemBean) request.getAttribute("ProductItemBean");
	int itemId = bean.getItemId();

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
<title>商品项描述</title>
	<%@ include file="../include/header.jsp"%>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
</head>
<body class="article_bg">
	<p class="article_tit">
		<a href="ProductItemList.do"><img src="<%=STATIC_URL %>/images/return.png"/></a> 商品项列表 > <%=bean.getItemName()%> >
		<% if(!descriptionList.isEmpty()) { %>
			修改商品项描述
		<%} else { %>
			新建商品项描述
		<%} %>		
	</p>
	<%@ include file="../nav/ProductItemNav.jsp" %>
	<form id="formData" name="formData" method="post" action="/pdm/ProductItemDescriptionManage.do?" onsubmit="javascript:return checkForm('formData');">
		<input id="itemId" name="itemId" value="<%=itemId%>" type="hidden" />
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
				<a href="ProductItemList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
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