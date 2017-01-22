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
	List<MaterialTypeBean> list = (List<MaterialTypeBean>)request.getAttribute("MaterialTypeList");

	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
	int parentTypeId = (Integer)request.getAttribute("parentTypeId");
	List<MaterialTypeBean> treeList = (List<MaterialTypeBean>)request.getAttribute("ParentTreeList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物料分类管理</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
<p class="article_tit">
	物料分类列表 
	<% for(MaterialTypeBean materialTypeBean : treeList) {%>
		> <%=materialTypeBean.getTypeName()%>
	<%} %>	
</p>
<form name="formSearch" method="post" action="MaterialTypeList.do">
  <div class="search_form">
  	<em class="em1">分类代码：</em>
    <input type="text" name="typeCode" id="typeCode" value="<%=ParamKit.getParameter(request, "typeCode", "") %>" size="20"/>
    <em class="em1">分类名称：</em>
    <input type="text" name="typeName" id="typeName" value="<%=ParamKit.getParameter(request, "typeName", "") %>" size="20"/>
    <em class="em1">有效状态：</em>
    <select name="status" id="status">
    	<%
    	int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
    	for(StatusBean<Integer, String> statusBean:Constants.STATUS_LIST){ %>
          <option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus()==status)?"selected":"" %>><%=statusBean.getValue() %></option>
        <%} %>
    </select>
    <input type="hidden" name="parentTypeId" id="parentTypdId" value="<%=parentTypeId %>" />
     <%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_TYPE, OperationConfig.SEARCH)){ %>
    		<input name="" type="submit" class="search_btn_sub" value="查询"/>
     <%} %>
  </div>
  <table width="100%" border="0"  cellpadding="0" class="table_style">
    <tr>
      <th>分类ID</th>
      <th>分类代码</th>
      <th>分类名称</th>
      <th>有效状态</th>
      <th>操作</th>
    </tr>
    <%
    for(MaterialTypeBean bean:list){ 
    %>	
    <tr>
      <td><%=bean.getTypeId()%></td>
      <td><%=bean.getTypeCode() %></td>
      <td><%=bean.getTypeName() %></td>
      <td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
      <td align="center">
      	<a href="/pdm/MaterialTypeManage.do?typeId=<%=bean.getTypeId() %>&parentTypeId=<%=bean.getParentTypeId()%>&readOnly=true">查看</a>
      	<%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_TYPE, OperationConfig.UPDATE)){ %>
      	 | <a href="/pdm/MaterialTypeManage.do?typeId=<%=bean.getTypeId() %>&parentTypeId=<%=bean.getParentTypeId()%>&readOnly=false">修改</a>
      	<%} %>
      </td>
    </tr>	
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
	<%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_TYPE, OperationConfig.CREATE)){ %>
  		<input type="button" name="button" id="button" value="新建" onclick="javascript:location.href='MaterialTypeManage.do?parentTypeId=<%=parentTypeId %>'">
  	<%} %>	
  	<%if(parentTypeId>0){ %>
  		<input type="button" name="button" id="button" value="返回" onclick="javascript:location.href='MaterialTypeList.do?typeId=<%=parentTypeId%>'">
  	<%} %>
</li>
<%@ include file="../include/page.jsp" %>
</ul>
</body>
</html>