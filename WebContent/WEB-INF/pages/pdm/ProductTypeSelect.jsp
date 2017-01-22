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
	List<ProductTypeBean> topProductTypeList = (List<ProductTypeBean>) request.getAttribute("TopProductTypeList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择商品类型</title>
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
	    		<span class="int_label">商品类型：</span>    
	      	<select name="typeId1" id="parentType" onchange="typeSelectionChange()">
	      		<option value=0>请选择顶级类型</option>
	      		<%for(ProductTypeBean productTypeBean : topProductTypeList){ %><option value="<%=productTypeBean.getTypeId() %>" ><%=productTypeBean.getTypeName()%></option><%} %>
	      	</select>
	      	<select name="typeId2" id="typeSelection">
	      	</select>
	    </p>
	  <div class="div_center">
	  	<input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
	  </div>
</body>
<script>
function typeSelectionChange(){
	var parentTypeId=$("#parentType").val();  
	var params={  
	        'parentTypeId':parentTypeId  
	    };  
	    $.ajax({
	        type: 'get',
	        url: "/pdm/ProductTypeJsonList.do",
	        data: params,
	        dataType: 'json',
	        success: function(data){
		      	var sel2 = $("#typeSelection");  
	      		sel2.empty();  
		      	if(data==null) {
	      		 	sel2.append("<option value = '-1'>"+"商品类型为空"+"</option>");
	          	}else {
	          		var items=data.result;
		    	    		if(items!=null) {
		    	    			sel2.append("<option value = 0>请选择子类型</option>");
		        	 		for(var i =0;i<items.length;i++) {
		          		var item=items[i];
		            		sel2.append("<option value = '"+item.typeId+"' id = 'typeName_"+item.typeId+"'>"+item.typeName+"</option>");
		             	};
		       		} else{
		       			sel2.empty();  
		          	}
	          	}
		      	
	        },
	        error: function(){
	            return;
	        }
	    });
}
function callbackTo(){
	var typeId1 = $("#parentType").val();
	var typeId2 = $("#typeSelection").val();
	var typeName = $("#typeName_"+typeId2).html();
	if(typeId1==0 || typeId2==0){
		alert("请选择商品类型");
		return false;
	}else{
		self.parent.<%=callbackFun %>(typeId1, typeId2,typeName);
		try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
	}
	
}
</script>
</html>