<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean"  %>
<%@page import="com.lpmas.framework.util.*"%>
<%@page import="com.lpmas.framework.web.*"%>
<% 
List<BrandPropertyBean> list = (List<BrandPropertyBean>)request.getAttribute("BrandPropertyList");
List<StatusBean<String, String>> brandPropertyFieldList = (List<StatusBean<String, String>>)request.getAttribute("BrandPropertyFieldList");

int brandId = ParamKit.getIntParameter(request, "brandId", 0);

Map<String, String> map = new HashMap<String, String>();
if (list != null && list.size() > 0) {
	map = ListKit.list2Map(list, "PropertyCode", "PropertyValue");
}
String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
request.setAttribute("readOnly", readOnly);
%>
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>品牌属性管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a>
		<ul class="art_nav">
			<li><a href="BrandInfoList.do">品牌列表</a>&nbsp;>&nbsp;</li>
			<li>修改品牌属性</li>
		</ul>
	</div>
	<div class="article_tit">
		<p class="tab">
		<a href="BrandInfoManage.do?brandId=<%=brandId%>&readOnly=<%=readOnly%>">品牌信息管理</a> 
		<a href="BrandPropertyManage.do?brandId=<%=brandId%>&readOnly=<%=readOnly%>">品牌属性管理</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<form id="formData" name="formData" method="post" action="BrandPropertyManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="brandId" id="brandId" value="<%=brandId %>"/>
	  <div class="modify_form">
	    <%
		if(brandPropertyFieldList!=null && brandPropertyFieldList.size()>0){
			for (StatusBean<String, String> bean : brandPropertyFieldList) {
		%>
		    <p>
		      <em class="int_label"><%=bean.getValue() %>：</em>
		      <input type="text" name="<%=bean.getStatus()%>" id="<%=bean.getStatus()%> " size="20" maxlength="20" 
		      value="<%=MapKit.getValueFromMap(bean.getStatus(), map) %>" checkStr="品牌属性配置;text;true;;20"/><em><span>*</span></em>
		    </p>
	    <% 
			}
		}
	    %>
	  </div>
	  <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <input type="button" name="cancel" id="cancel" value="取消" onclick="javascript:history.back()">
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
});
</script>
</html>