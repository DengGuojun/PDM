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
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	List<ProductInfoBean> list = (List<ProductInfoBean>)request.getAttribute("ProductInfoList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择商品</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="ProductInfoSelect.do?callbackFun=<%=callbackFun%>">
  <div class="search_form">
  	<em class="em1">商品编码：</em>
    <input type="text" name="productNumber" id="productNumber" value="<%=ParamKit.getParameter(request, "productNumber", "") %>" size="15"/>
    <em class="em1">商品名称：</em>
    <input type="text" name="productName" id="productName" value="<%=ParamKit.getParameter(request, "productName", "") %>" size="15"/>
    <input name="" type="submit" class="search_btn_sub" value="查询"/>
  </div>
  </form> 
  <table width="100%" border="0"  cellpadding="0" class="table_style">
    <tr>
    	  <th>选择</th>
      <th>商品ID</th>
      <th>商品编码</th>
      <th>商品名称</th>
    </tr>
    <%
    for(ProductInfoBean bean:list){%> 
    <tr>
    	  <td align="center"><input type="radio" name="productId" value="<%=bean.getProductId()%>"></td>
      <td><%=bean.getProductId() %></td>
      <td><%=bean.getProductNumber() %></td>
      <td><%=bean.getProductName() %></td>
    </tr>	
    <%} %>
  </table>
  <div class="div_center">
  	<input type="button" name="button" id="button" class="modifysubbtn" value="新建" onclick="callbackTo()" />
  </div>
<ul class="page_info">
<%@ include file="../include/page.jsp" %>
</ul>
</body>
<script>
function callbackTo(){
	var value = $('input:radio[name=productId]:checked').val();
	if (typeof(value) == 'undefined'){
		alert("请选择商品");
		return;
	}
	self.parent.<%=callbackFun %>(value);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>