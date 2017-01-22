<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.config.*"  %>
<%@ page import="com.lpmas.file.client.*"  %>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.constant.info.*" %>
<%
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	MaterialInfoBean materialInfoBean = (MaterialInfoBean) request.getAttribute("MaterialInfoBean");
	MaterialImageBean materialImageBean = (MaterialImageBean) request.getAttribute("MaterialImageBean");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物料图片描述</title>
	<%@ include file="../include/header.jsp"%>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type="text/javascript">
		document.domain= '<%=DOMAIN%>'; //解决与iframe之间的跨域问题
	</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="MaterialInfoList.do">物料列表</a>&nbsp;>&nbsp;</li>
			<li><%=materialInfoBean.getMaterialName() %>&nbsp;>&nbsp;</li>
			<% if(materialImageBean != null) {%>
			<li>修改物料图片</li>
			<%}else{ %>
			<li>新建物料图片</li>
			<%}%>
		</ul>
	</div>
	<div class="article_tit">
		<p class="tab">
		<a href="MaterialInfoManage.do?materialId=<%=materialInfoBean.getMaterialId()%>&readOnly=<%=readOnly%>">物料信息</a> 
		<a href="MaterialInfoStorageManage.do?materialId=<%=materialInfoBean.getMaterialId()%>&readOnly=<%=readOnly%>">仓储信息</a>
		<a href="MaterialInfoImageManage.do?materialId=<%=materialInfoBean.getMaterialId()%>&readOnly=<%=readOnly%>">物料图片</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<div>
		<form method="post" target="uploadResultFrame" enctype="multipart/form-data" action="<%=FILE_URL%>/file/FileUploadManage.do?appCode=PDM&infoType1=<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>&infoId1=<%=materialInfoBean.getMaterialId()%>&fileId=<%=materialImageBean != null ? materialImageBean.getFileId() : 0%>">
			<input type="file" name="file" />
			<input type="SUBMIT" value="上传" />&nbsp;&nbsp;<em>(图片大小不超过512K)</em>
		</form>	
		<IFRAME id="uploadResultFrame" name="uploadResultFrame" src="about:blank" frameborder='0' style="display:none"></IFRAME>
	</div>
	<form id="formData" name="formData" method="post" action="MaterialInfoImageManage.do?" onsubmit="javascript:return checkForm('formData');" >
		<input id="materialId" name="materialId" value="<%=materialImageBean != null ? materialImageBean.getMaterialId() : 0%>" type="hidden" />
		<input id="infoId" name="infoId" value="<%=materialInfoBean.getMaterialId()%>" type="hidden" />
		<input type="hidden" id="fileId" name="fileId" value="<%=materialImageBean != null ? materialImageBean.getFileId() : ""%>" checkStr="图片;txt;true;;100"/>
		<%if(materialImageBean != null) {%>
			<img src="<%=FILE_URL%><%=new FileServiceClient().getFileUrlByKey(materialImageBean.getFileId())%>" />
		<%} %>
	</form>
</body>
<script>
$(document).ready(function() {
	var readonly = '${readOnly}';
	if(readonly=='true')
	{
		disablePageElement();
	}
});
</script>
</html>