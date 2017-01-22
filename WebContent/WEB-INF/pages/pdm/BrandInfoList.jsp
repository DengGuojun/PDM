<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
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
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<BrandInfoBean> list = (List<BrandInfoBean>)request.getAttribute("BrandList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>品牌管理</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
<p class="article_tit">品牌列表</p>
<form name="formSearch" method="post" action="BrandInfoList.do">
  <div class="search_form">
  	<em class="em1">品牌代码：</em>
    <input type="text" name="brandCode" id="brandCode" value="<%=ParamKit.getParameter(request, "brandCode", "") %>" size="20"/>
    <em class="em1">品牌名称：</em>
    <input type="text" name="brandName" id="brandName" value="<%=ParamKit.getParameter(request, "brandName", "") %>" size="20"/>
    <em class="em1">品牌状态：</em>
    <select name="status" id="status">
    	<%
    	int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
    	for(StatusBean<Integer, String> statusBean:Constants.STATUS_LIST){ %>
        <option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus()==status)?"selected":"" %>><%=statusBean.getValue() %></option>
    <%} %>
    </select>
    <%if(adminUserHelper.hasPermission(PdmResource.BRAND_INFO, OperationConfig.SEARCH)){ %>
    <input name="" type="submit" class="search_btn_sub" value="查询"/>
    <%} %>
  </div>
  <table width="100%" border="0"  cellpadding="0" class="table_style">
    <tr>
      <th>品牌ID</th>
      <th>品牌Logo</th>
      <th>品牌代码</th>
      <th>品牌名称</th>
      <th>有效状态</th>
      <th>操作</th>
    </tr>
    <%
    for(BrandInfoBean bean:list){%> 
    <tr>
      <td><%=bean.getBrandId() %></td>
      <td><img src="<%=bean.getBrandLogo() %>"  width="100" height="100" ></td>
      <td><%=bean.getBrandCode() %></td>
      <td><%=bean.getBrandName() %></td>
      <td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
      <td align="center">
      	<a href="/pdm/BrandInfoManage.do?brandId=<%=bean.getBrandId()%>&readOnly=true">查看</a> 
      	<%if(adminUserHelper.hasPermission(PdmResource.BRAND_INFO, OperationConfig.UPDATE)){ %>
      	| <a href="/pdm/BrandInfoManage.do?brandId=<%=bean.getBrandId()%>&readOnly=false">修改</a>
      	<%} %>
      </td>
    </tr>	
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
	<%if(adminUserHelper.hasPermission(PdmResource.BRAND_INFO, OperationConfig.CREATE)){ %>
  	<input type="button" name="button" id="button" value="新建" onclick="javascript:location.href='BrandInfoManage.do'">
  	<%} %>
</li>
<%@ include file="../include/page.jsp" %>
</ul>
</body>
</html>