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
	List<UnitTypeBean> unitTypeList = (List<UnitTypeBean>) request.getAttribute("UnitTypeList");
	int unitTypeId= (Integer) request.getAttribute("UnitTypeId");
	String unitCode= (String) request.getAttribute("UnitCode");
	String itemId = ParamKit.getParameter(request, "itemId", "");
	String itemName = ParamKit.getParameter(request, "itemName","");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择计量单位</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
<body class="article_bg">
	    <p>
	    		<span class="int_label">计量单位：</span>    
	    		<input type="hidden" name="unitCode" id="unitCode" value="<%=unitCode %>" />
	      	<select name="unitType" id="unitType" onchange="unitSelectionChange()">
	      		<option value=0>请选择计量单位类型</option>
	      		<%for(UnitTypeBean unitTypeBean : unitTypeList){ %><option value="<%=unitTypeBean.getTypeId() %>" <%=unitTypeBean.getTypeId() ==  unitTypeId ? "selected" : ""%>><%=unitTypeBean.getTypeName()%></option><%} %>
	      	</select>
	      	<select name="unitInfo" id="unitInfo">
	      	</select>
	    </p>
	  <div class="div_center">
	  	<input type="submit" name="button" id="button" class="modifysubbtn" value="确定" onclick="callbackTo()" />
	  </div>
</body>
<script>
$(document).ready(function() {
	unitSelectionChange();
});
function unitSelectionChange(){
	var unitTypeId = $("#unitType").val();
	var unitCode = $("#unitCode").val();
	var params={  
	        'unitTypeId':unitTypeId  
	    };  
	    $.ajax({
	        type: 'get',
	        url: "/pdm/UnitInfoJsonList.do",
	        data: params,
	        dataType: 'json',
	        success: function(data){
		      	var sel2 = $("#unitInfo");  
	      		sel2.empty();  
		      	if(data==null) {
	      		 	sel2.append("<option value = '-1' >"+"单位信息为空"+"</option>");
	          	}else {
	          		var items=data.result;
		    	   		if(items!=null) {
		        	  		for(var i =0;i<items.length;i++) {
		          		var item=items[i];
		          		if(item.unitId == unitCode){
		          			sel2.append("<option value = '"+item.unitCode+"' selected>"+item.unitName+"</option>");
		          		}else{
		          			sel2.append("<option value = '"+item.unitCode+"' >"+item.unitName+"</option>");
		          		}
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
	var unitCode =  $("#unitInfo").val();
	var unitName = $("#unitInfo").find("option:selected").text();
	if(typeof(unitCode)=="undefined"||unitCode.trim() == ""||unitCode.trim()=="-1"||unitCode.trim()=="0"){
		alert("请选择计量单位");
		return false;
	}else{
		<%if(!itemId.equals("")||!itemName.equals("")){%>
		self.parent.<%=callbackFun %>(unitCode, unitName,'<%=itemId%>','<%=itemName%>');
		<%}else{%>
		self.parent.<%=callbackFun %>(unitCode, unitName);
		<%}%>
		try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
	
	}
	
}
</script>
</html>