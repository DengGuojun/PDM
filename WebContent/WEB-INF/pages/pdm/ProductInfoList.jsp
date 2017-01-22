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
<%@ page import="com.lpmas.file.client.*"  %>
<%
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<ProductInfoEntityBean> list = (List<ProductInfoEntityBean>)request.getAttribute("ProductInfoEntityList");
	Map<Integer, ProductTypeBean> productTypeMap = (Map<Integer, ProductTypeBean>)request.getAttribute("ProductTypeMap");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品管理</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<p class="article_tit">商品列表</p>
<form name="formSearch" method="post" action="ProductInfoList.do">
  <div class="search_form">
  	<em class="em1">商品编码：</em>
    <input type="text" name="productNumber" id="productNumber" value="<%=ParamKit.getParameter(request, "productNumber", "") %>" size="20"/>
    <em class="em1">商品名称：</em>
    <input type="text" name="productName" id="productName" value="<%=ParamKit.getParameter(request, "productName", "") %>" size="20"/>
    <em class="em1">有效状态：</em>
    <select name="status" id="status">
    	<%
    	int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
    	for(StatusBean<Integer, String> statusBean:Constants.STATUS_LIST){ %>
          <option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus()==status)?"selected":"" %>><%=statusBean.getValue() %></option>
        <%} %>
    </select>
    <%if(adminUserHelper.hasPermission(PdmResource.PRODUCT_INFO, OperationConfig.SEARCH)){ %>
    <input name="" type="submit" class="search_btn_sub" value="查询"/>
    <%} %>
  </div>
  <table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>商品ID</th>
      <!-- <th>商品缩略图</th> -->
      <th>商品编码</th>
      <th>商品名称</th>
      <th>类型</th>
      <th>有效状态</th>
      <th>操作</th>
    </tr>
    <%
    for(ProductInfoEntityBean bean:list){%> 
    <tr>
      <td><%=bean.getProductId() %></td>
      <!--  <td><img src="<%=FILE_URL%><%=bean.getImageThumbnailUrl()%>"/></td>-->
      <td><%=bean.getProductNumber() %></td>
      <td><%=bean.getProductName() %></td>
      <td><%=productTypeMap.get(bean.getTypeId1()).getTypeName()%> - <%=productTypeMap.get(bean.getTypeId2()).getTypeName() %></td>
      <td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
      <td align="center">
        <a href="/pdm/ProductInfoManage.do?productId=<%=bean.getProductId() %>&readOnly=true">查看</a> 
        <%if(adminUserHelper.hasPermission(PdmResource.PRODUCT_INFO, OperationConfig.UPDATE)){ %>
      	 | <a href="/pdm/ProductInfoManage.do?productId=<%=bean.getProductId() %>&readOnly=false">修改</a> 
      	<%} %> 
      </td>
    </tr>	
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
	<%if(adminUserHelper.hasPermission(PdmResource.PRODUCT_INFO, OperationConfig.CREATE)){ %>
  	<input type="button" name="button" id="button" value="新建" >
  	<%} %>
</li>
<%@ include file="../include/page.jsp" %>
</ul>
</body>
<script type='text/javascript'>
$(document).ready(function() {
	$("#button").click(
		function() {
			$.fancybox.open({
				href : 'ProductTypeSelect.do?callbackFun=selectProductType',
				type : 'iframe',
				width : 560,
				minHeight : 150
		});
	});
});
function selectProductType(typeId1, typeId2) {
	var url = "ProductInfoManage.do?typeId1="+ typeId1 + "&typeId2=" + typeId2;
	window.location.href= url
}
</script>
</html>