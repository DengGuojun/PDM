<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.business.*"  %>
<% 
	List<MaterialTypeBean> marterialTypeList = (List<MaterialTypeBean>) request.getAttribute("marterialTypeList");
%>
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>物料管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
</head>
<body class="article_bg">
	<p class="article_tit">
		<a href="MaterialInfoList.do"><img src="<%=STATIC_URL %>/images/return.png"/></a> 物料列表 > 新建物料信息
	</p>
	<form id="formData" name="formData" method="get" action="MaterialInfoManage.do" onsubmit="javascript:return checkForm('formData');">
	    <p>
	    		<em class="int_label">物料类型：</em>    
	      	<select name="typeId" id="parentType">
	      		<option value="">请选择物料分类</option>
	      		<%for(MaterialTypeBean materialTypeBean : marterialTypeList){ %><option value="<%=materialTypeBean.getTypeId() %>" ><%=materialTypeBean.getTypeName()%></option><%} %>
	      	</select>
	    </p>
	  <div class="div_center">
	  	<input type="submit" name="button" id="submit" class="modifysubbtn" value="提交" />
	  	<input type="button" name="button" id="button" value="取消" onclick="javascript:history.back()">
	  </div>
	</form>
</body>
</html>