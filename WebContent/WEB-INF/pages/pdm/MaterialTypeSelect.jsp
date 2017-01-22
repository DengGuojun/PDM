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
	List<MaterialTypeBean> MaterialTypeList = (List<MaterialTypeBean>) request.getAttribute("MaterialTypeList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择物料类型</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
	    <p>
	    		<span class="int_label">物料类型：</span>  
	      	<select name="typeId1" id="materialType" >
	      		<option value=0>请选择物料类型</option>
	      		<%for(MaterialTypeBean materialTypeBean : MaterialTypeList){ %><option id="typeName_<%=materialTypeBean.getTypeId() %>" value="<%=materialTypeBean.getTypeId() %>" ><%=materialTypeBean.getTypeName()%></option><%} %>
	      	</select>
	    </p>
	  <div class="div_center">
	  	<input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
	  </div>
</body>
<script>
function callbackTo(){
	var typeId = $("#materialType").val();
	if(typeId==0){
		alert("请选择物料类型");
		return false;
	}else{
		var typeName = $("#typeName_"+typeId).html();
		self.parent.<%=callbackFun %>(typeId,typeName);
		try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
	}
	
}
</script>
</html>