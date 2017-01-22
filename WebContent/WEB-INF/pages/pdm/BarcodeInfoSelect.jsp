<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	List<BarcodeInfoBean> list = (List<BarcodeInfoBean>)request.getAttribute("BarcodeList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	int barcodeId = (Integer)request.getAttribute("barcodeId");
%>  
<%@ include file="../include/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加条形码</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="BarcodeInfoSelect.do">
  <div class="search_form">
  	<em class="em1">条形码：</em>
    <input type="text" name="barcode" id="barcode" value="<%=ParamKit.getParameter(request, "barcode", "") %>"  placeholder="请输入条形码"size="20"/>
    <input type="hidden" name="barcodeId" value="<%=barcodeId %>" />
    <input type="hidden" name="callbackFun" value="<%=callbackFun %>" />
    <%if(adminUserHelper.hasPermission(PdmResource.BARCODE_INFO, OperationConfig.SEARCH)){ %>
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
    <%} %>
  </div>
  <table width="100%" border="0"  cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>条形码ID</th>
      <th>条形码</th>
    </tr>
    <%
    for(BarcodeInfoBean bean:list){%> 
    <tr>
      <td align="center"><input type="radio" name="barcodeId" value="<%=bean.getBarcodeId()%>" <%=barcodeId==bean.getBarcodeId() ? "checked" : ""%>></td>
      <td><%=bean.getBarcodeId() %></td>
      <td id="barcode_<%=bean.getBarcodeId()%>"><%=bean.getBarcode()%></td>
    </tr>	
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
</li>
<%@ include file="../include/page.jsp" %>
</ul>
</body>
<script>
function callbackTo(){
	var barcodeId = $('input:radio[name=barcodeId]:checked').val();
	if (typeof(barcodeId) == 'undefined'){
		alert("请选择条形码");
		return;
	}
	var barcode = $("#barcode_"+barcodeId).html();
	self.parent.<%=callbackFun %>(barcodeId, barcode);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>