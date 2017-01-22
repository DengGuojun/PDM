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
	List<UnitInfoBean> list = (List<UnitInfoBean>)request.getAttribute("UnitInfoList");
	
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
	UnitTypeBean typeBean = (UnitTypeBean)request.getAttribute("UnitTypeBean");
	int typeId = (Integer)request.getAttribute("TypeId");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>单位信息管理</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
<p class="article_tit">
	 <%=typeBean.getTypeName() %> - 单位信息列表
</p>
<form name="formSearch" method="post" action="UnitInfoList.do">
  <div class="search_form">
  	<em class="em1">单位信息代码：</em>
    <input type="text" name="unitCode" id="unitCode" value="<%=ParamKit.getParameter(request, "unitCode", "") %>" size="20"/>
    <em class="em1">单位信息名称：</em>
    <input type="text" name="unitName" id="unitName" value="<%=ParamKit.getParameter(request, "unitName", "") %>" size="20"/>
    <!-- 
    <em class="em1">有效状态：</em>
    <select name="status" id="status">
    	<%
    		int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
    		for(StatusBean<Integer, String> statusBean:Constants.STATUS_LIST){ %>
          <option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus()==status)?"selected":"" %>><%=statusBean.getValue() %></option>
        <%} %>
    </select>
     -->
    <input type="hidden" name="typeId" id="typeId" value="<%=typeId %>"/>
    <%if(adminUserHelper.hasPermission(PdmResource.UNIT_INFO, OperationConfig.SEARCH)){ %>
    <input name="" type="submit" class="search_btn_sub" value="查询"/>
    <%} %>
  </div>
  <table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>单位信息ID</th>
      <th>单位信息代码</th>
      <th>单位信息名称</th>
      <th>操作</th>
    </tr>
    <%
    for(UnitInfoBean bean:list){ 
    %>	
    <tr>
      <td><%=bean.getUnitId()%></td>
      <td><%=bean.getUnitCode() %></td>
      <td><%=bean.getUnitName() %></td>
      <td align="center">
      	<a href="/pdm/UnitInfoManage.do?unitId=<%=bean.getUnitId() %>&readOnly=true">查看</a>
      	<%if(adminUserHelper.hasPermission(PdmResource.UNIT_INFO, OperationConfig.UPDATE)){ %>
      	 | <a href="/pdm/UnitInfoManage.do?unitId=<%=bean.getUnitId() %>&readOnly=false">修改</a>
      	 <%} %>
      </td>
    </tr>	
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
	<%if(adminUserHelper.hasPermission(PdmResource.UNIT_INFO, OperationConfig.CREATE)){ %>
 		<input type="button" name="button" id="button" value="新建" onclick="javascript:location.href='UnitInfoManage.do?typeId=<%=typeId %>'">
 	<%} %>
  <input type="button" name="button" id="button" value="返回" onclick="javascript:location.href='UnitTypeList.do'">
</li>
<%@ include file="../include/page.jsp" %>
</ul>
</body>
</html>