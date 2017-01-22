<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.config.*"  %>
<%@ page import="com.lpmas.file.client.*"  %>
<%@ page import="com.lpmas.constant.info.*" %>
<%
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	ProductInfoBean bean = (ProductInfoBean) request.getAttribute("ProductInfoBean");
	ProductImageBean productImageBean = (ProductImageBean) request.getAttribute("ProductImageBean");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品图片描述</title>
	<%@ include file="../include/header.jsp"%>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; //解决与iframe之间的跨域问题
	</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductInfoList.do">商品列表</a>&nbsp;>&nbsp;</li>
			<li><%=bean.getProductName()%>&nbsp;>&nbsp;</li>
			<% if(productImageBean != null) { %>
			<li>修改商品图片</li>
			<%} else { %>
			<li>新建商品图片</li>
			<%} %>		
		</ul>
	</div>
	<%@ include file="../nav/ProductInfoNav.jsp" %>
	<div>
		<form method="post" target="uploadResultFrame" enctype="multipart/form-data" action="<%=FILE_URL%>/file/FileUploadManage.do?appCode=PDM&infoType1=<%=InfoTypeConfig.INFO_TYPE_PRODUCT %>&infoId1=<%=bean.getProductId()%>&fileId=<%=productImageBean != null ? productImageBean.getFileId() : 0%>">
			<input type="file" name="file" />
			<input type="submit" value="上传" />&nbsp;&nbsp;<em>(图片大小不超过512K)</em>
		</form>	
		<IFRAME id="uploadResultFrame" name="uploadResultFrame" src="about:blank" frameborder='0' style="display:none"></IFRAME>
	</div>
	<form id="formData" name="formData" method="post" action="/pdm/ProductInfoImageManage.do?" onsubmit="javascript:return checkForm('formData');" >
		<input id="infoId" name="infoId" value="<%=productImageBean != null ? productImageBean.getInfoId() : 0%>" type="hidden" />
		<input id="productId" name="productId" value="<%=bean.getProductId()%>" type="hidden" />
		<input type="hidden" id="fileId" name="fileId" value="<%=productImageBean != null ? productImageBean.getFileId() : ""%>" checkStr="图片;txt;true;;100"/>
		<%if(productImageBean != null) {%>
			<img src="<%=FILE_URL%><%=new FileServiceClient().getFileUrlByKey(productImageBean.getFileId())%>" />
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