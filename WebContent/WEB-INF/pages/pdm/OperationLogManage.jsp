<%@page import="com.lpmas.log.bean.DataLogContentBean"%>
<%@page import="com.lpmas.pdm.config.*"%>
<%@page import="com.lpmas.log.business.*"%>
<%@page import="com.lpmas.admin.config.OperationConfig"%>
<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@page import="com.lpmas.log.bean.DataLogBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.bean.StatusBean"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@page import="com.lpmas.system.bean.SysApplicationInfoBean"%>
<%@page import="com.fasterxml.jackson.core.type.*"%>
<%
    DataLogBean logBean = (DataLogBean)request.getAttribute("DataLogBean");
    AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
    Map<Integer, String> appMap = (Map<Integer, String>)request.getAttribute("ApplicationMap");  
%>
<%@ include file="../include/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看日志</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li>日志详情</li>
		</ul>
	</div>

		<div class="modify_form">
			<p>
				<em class="int_label">日志ID：</em>
				<input size="50" disabled="disabled" type="text" value="<%=logBean.getLogId()%>" />
			</p>
			<p>
				<em class="int_label">日志来源应用：</em>
				<input size="50" disabled="disabled" type="text" value="<%=appMap.get(logBean.getAppId())%>" />
			</p>
			<p>
				<em class="int_label">日志信息类型：</em>
				<input size="50" disabled="disabled" type="text" value="<%=InfoTypeConfig.INFO_TYPE_MAP.get(logBean.getInfoType()) %>" />
			</p>
			<p>
				<em class="int_label">日志信息ID_1：</em>
				<input size="50" disabled="disabled" type="text" value="<%=logBean.getInfoId1()%>" />
			</p>
			<p>
				<em class="int_label">日志信息ID_2：</em>
				<input size="50" disabled="disabled" type="text" value="<%=logBean.getInfoId2()%>" />
			</p>
			<p>
				<em class="int_label">日志信息ID_3：</em>
				<input size="50" disabled="disabled" type="text" value="<%=logBean.getInfoId3()%>" />
			</p>
			<p>
				<em class="int_label">字段_1：</em>
				<input size="50" disabled="disabled" type="text" value="<%=logBean.getField1()%>" />
			</p>
			<p>
				<em class="int_label">字段_2：</em>
				<input size="50" disabled="disabled" type="text" value="<%=logBean.getField2()%>" />
			</p>
			<p>
				<em class="int_label">字段_3：</em>
				<input size="50" disabled="disabled" type="text" value="<%=logBean.getField3()%>" />
			</p>
			<p>
				<em class="int_label">操作类型：</em>
				<input size="50" disabled="disabled" type="text" value="<%=OperationConfig.OPERATION_MAP.get(logBean.getOperationCode())%>" />
			</p>
			<p>
				<em class="int_label">日志内容：</em>
				<%if(StringKit.isValid(logBean.getLogContent())){
				DataLogContentConvertor helper = new DataLogContentConvertor(LogInfoDisConfig.CONFIG_VALUE_MAP);
				List<DataLogContentBean> list = JsonKit.toList(logBean.getLogContent(), DataLogContentBean.class);
				String content = helper.convertLogContentList2String(helper.convertLogContentList(list));
				%>
				<textarea disabled="disabled" cols="100" rows="10" ><%=content%></textarea>
				<%} %>
			</p>
			<p>
				<em class="int_label">日志记录时间：</em>
				<input size="50" disabled="disabled" type="text" value="<%=DateKit.formatTimestamp(logBean.getCreateTime(), DateKit.DEFAULT_DATE_TIME_FORMAT)%>" />
			</p>
			<p>
				<em class="int_label">操作者：</em>
				<input size="50" disabled="disabled" type="text" value="<%=adminCache.getAdminUserNameByKey(logBean.getCreateUser())%>" />
			</p>
		</div>

		<div class="div_center">
			 <input type="button" name="button" id="button" value="返回" onclick="javascript:location.href='OperationLogList.do'">
		</div>
</body>
</html>