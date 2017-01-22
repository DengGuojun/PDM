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
	List<ProductItemEntityBean> list = (List<ProductItemEntityBean>)request.getAttribute("ProductItemEntityList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品项管理</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
</head>
<body class="article_bg">
<p class="article_tit">商品项列表</p>
<form name="formSearch" method="post" action="ProductItemList.do">
  <div class="search_form">
  	<em class="em1">商品项编码：</em>
    <input type="text" name="itemNumber" id="itemNumber" value="<%=ParamKit.getParameter(request, "itemNumber", "") %>" size="20"/>
    <em class="em1">商品项名称：</em>
    <input type="text" name="itemName" id="itemName" value="<%=ParamKit.getParameter(request, "itemName", "") %>" size="20"/>
    <em class="em1">有效状态：</em>
    <select name="status" id="status">
    	<%
    	int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
    	for(StatusBean<Integer, String> statusBean:Constants.STATUS_LIST){ %>
          <option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus()==status)?"selected":"" %>><%=statusBean.getValue() %></option>
        <%} %>
    </select>
     <%if(adminUserHelper.hasPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.SEARCH)){ %>
    <input name="" type="submit" class="search_btn_sub" value="查询"/>
    <%} %>
  </div>
  <table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>商品项ID</th>
      <th>商品缩略图</th>
      <th>商品项编码</th>
      <th>商品项名称</th>
      <th>规格</th>
      <th>是否可销售</th>
      <th>有效状态</th>
      <th>操作</th>
    </tr>
    <%
    for(ProductItemEntityBean bean:list){%> 
    <tr>
      <td><%=bean.getItemId() %></td>
      <td><img src="<%=FILE_URL%><%=bean.getImageThumbnailUrl()%>"/></td>
      <td><%=bean.getItemNumber() %></td>
      <td><%=bean.getItemName() %></td>
      <td><%=bean.getSpecification() %></td>
      <td><%=MapKit.getValueFromMap(bean.getUseStatus(), ProductConfig.PRODUCT_USE_STATUS_MAP)%></td>
      <td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
      <td align="center">
      	<a href="/pdm/ProductItemManage.do?itemId=<%=bean.getItemId() %>&readOnly=true">查看</a> 
      	 <%if(adminUserHelper.hasPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.UPDATE)){ %>
      	 | <a href="/pdm/ProductItemManage.do?itemId=<%=bean.getItemId() %>&readOnly=false">修改</a></td>
      	 <%} %>
    </tr>	
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
	<%if(adminUserHelper.hasPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.CREATE)){ %>
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
				href : 'ProductInfoSelect.do?callbackFun=selectProductInfo',
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
});
function selectProductInfo(value) {
	var url = "ProductItemManage.do?productId="+ value;
	window.location.href= url
}
</script>
</html>