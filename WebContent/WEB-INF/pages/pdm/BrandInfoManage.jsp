<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<% 
	BrandInfoBean bean = (BrandInfoBean)request.getAttribute("BrandInfo");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<AdminGroupInfoBean> groupList = adminUserHelper.getUserGroupList();
	List<StatusBean<String, String>> brandConfigStatuslist = (List<StatusBean<String, String>>) request.getAttribute("BrandConfigStatusList");
	int brandId = bean.getBrandId();
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>品牌管理</title>
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
			<% if(brandId > 0) {%>
			<li><%=bean.getBrandName() %>&nbsp;>&nbsp;</li>
			<li>修改品牌信息</li>
			<%}else{ %>
			<li>新建品牌信息</li>
			<%}%>
		</ul>
	</div>
	<%if(brandId > 0 && brandConfigStatuslist.size() > 0){%>
	<div class="article_tit">
		<p class="tab">
		<a href="BrandInfoManage.do?brandId=<%=brandId%>&readOnly=<%=readOnly%>">品牌信息管理</a> 
		<a href="BrandPropertyManage.do?brandId=<%=brandId%>&readOnly=<%=readOnly%>">品牌置属性管理</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<form id="formData" name="formData" method="post" action="BrandInfoManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="brandId" id="brandId" value="<%=bean.getBrandId() %>"/>
	  <div class="modify_form">
	    <p>
	      <em class="int_label"><span>*</span>品牌代码：</em>
	      <input type="text" name="brandCode" id="brandCode" size="20" maxlength="10" value="<%=bean.getBrandCode() %>" checkStr="品牌代码;code;true;;10"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>品牌名称：</em>
	      <input type="text"  name="brandName" id="brandName" size="30" maxlength="100" value="<%=bean.getBrandName() %>" checkStr="品牌名称;txt;true;;100"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>所属用户组：</em>    
	      	<select  name="groupId" id="groupId">
	      		<%for(AdminGroupInfoBean groupBean : groupList){ %><option value="<%=groupBean.getGroupId() %>" <%=(groupBean.getGroupId()==bean.getGroupId())?"selected":"" %>><%=groupBean.getGroupName() %></option><%} %>
	      	</select>
	    </p>
	    <p>
	      <em class="int_label">品牌Logo:</em>    
     	  <input  type="text" name="brandLogo" id="brandLogo" size="100" maxlength="200" value="<%=bean.getBrandLogo() %>" checkStr="品牌Logo;txt;false;;200"/>
	    </p>
	    <p>
	      <em class="int_label">有效状态：</em>
	      <input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
	    </p>
	    <p class="p_top_border">
	      <em class="int_label">备注：</em>
	      <textarea  name="memo" id="memo" cols="60" rows="3" checkStr="备注;txt;false;;1000"><%=bean.getMemo() %></textarea>
	    </p>
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