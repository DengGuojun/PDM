<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<% 
    BarcodeInfoBean bean = (BarcodeInfoBean)request.getAttribute("BarcodeInfo");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	int barcodeId = bean.getBarcodeId();
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	List<StatusBean<String, String>> barcodeUseStatusList = (List<StatusBean<String, String>>) request
			.getAttribute("barcodeUseStatusList");
	request.setAttribute("readOnly", readOnly);
%>
<%@ include file="../include/header.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>条形码管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
</head>
<body class="article_bg">
    <div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="BarcodeInfoList.do">条形码列表</a>&nbsp;>&nbsp;</li>
			<% if(barcodeId > 0) {%>
			<li><%=bean.getBarcode() %>&nbsp;>&nbsp;</li>
			<li>修改条形码信息</li>
			<%}else{ %>
			<li>新建条形码信息</li>
			<%}%>
		</ul>
	</div>
    <form id="formData" name="formData" method="post" action="BarcodeInfoManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="barcodeId" id="barcodeId" value="<%=barcodeId %>"/>
	  <div class="modify_form">
	    <p>
	      <em class="int_label"><span>*</span>条形码：</em>
	      <input type="text" name="barcode" id="barcode" size="20" maxlength="50" value="<%=bean.getBarcode() %>" checkStr="条形码;code;true;;50"/>
	    </p>	    
	   <p>
				<em class="int_label"><span>*</span>使用状态：</em> <select name="useStatus"
					id="useStatus">
					<%
						for (StatusBean<String, String> statusBean : barcodeUseStatusList) {
					%><option value="<%=statusBean.getStatus()%>"
						<%=(statusBean.getStatus().equals(bean.getUseStatus())) ? "selected" : ""%>><%=statusBean.getValue()%></option>
					<%
						}
					%>
				</select>
		</p>
	    <p>
	      <em class="int_label">有效状态：</em>
	      <input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
	    </p>
	    <p class="p_top_border">
	      <em class="int_label">备注：</em>
	      <textarea name="memo" id="memo" cols="60" rows="3" checkStr="备注;txt;false;;1000"><%=bean.getMemo() %></textarea>
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
	var readonly = '${readOnly}';
	if(readonly=='true') {
		disablePageElement();
	}
});
</script>
</html>