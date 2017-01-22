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
<%@ page import="com.lpmas.region.bean.*"  %>
<%
	int provinceId = (Integer) request.getAttribute("ProvinceId");
	String currentRegion = (String) request.getAttribute("CurrentRegion");
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	List<ProvinceInfoBean> provinceList = (List<ProvinceInfoBean>) request.getAttribute("ProvinceList");
%>

<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择地区</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<div>
    请选择地区：
    <select id="province" onchange="showCity()">
    		<option value="-1" >--请选择省--</option>
        <%for(ProvinceInfoBean provinceBean : provinceList){ %><option value="<%=provinceBean.getProvinceId() %>" <%=provinceBean.getProvinceId() == provinceId?"selected":"" %>><%=provinceBean.getProvinceName() %></option><%} %>
    </select>
    <select id="city" onchange="showDist()" >
        <option value="-1">--请选择城市--</option>
    </select>
    <select id="region">
        <option value="-1">--请选择区/县--</option>
    </select>
    <input type="button" value="添加产地"  onclick="confimSeleted()"/>
</div>
<hr>
<div id="newProducingArea">已选择产地:</div>
<br/>
<hr>
<div>
	<input type="hidden" id="currentRegion" value="<%=currentRegion%>"/>
	<input type="button" value="确认完成" onClick="javascript:callbackTo();"/>
	<input type="button" value="继续添加其他产地" onclick="continueSelect()"/>
</div>
<script>
$(document).ready(function() {
	var currentRegion = $("#currentRegion").val();
	currentRegion = currentRegion.replace(/,/g,"-");
	$("#currentRegion").val(currentRegion);
	var array = currentRegion.split(";");
	var result = "";
	for(var i=0;i<array.length;i++){
		if(array[i] != ""){
			regionInfo = array[i].replace(/-/g,"");
	        regionValue = array[i];
	        $("#newProducingArea").append("<div id="+regionValue+">"+regionInfo+"<em ><a href=# onclick=delArea('"+regionValue+"')>删除</a></em></div>");
	        result += array[i]+ "   ";
		}
    }
	
});
function showCity(){
	var provinceId = $("#province").val();
	var params={  
	        'provinceId':provinceId  
    };  
	$.ajax({
        type: 'get',
        url: "/pdm/ProducingAreaSelect.do",
        data: params,
        dataType: 'json',
        success: function(data){
	      	var sel2 = $("#city");  
      		sel2.empty();  
	      	if(data==null) {
      		 	sel2.append("<option value = '-1'>"+"城市列表为空"+"</option>");
          	}else {
          		sel2.append("<option value = '-1'>"+"--请选择城市--"+"</option>");
          		var items=data.result;
		    	    	if(items!=null) {
		        	  for(var i =0;i<items.length;i++) {
		          	var item=items[i];
		            sel2.append("<option value = '"+item.cityId+"'>"+item.cityName+"</option>");
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

function showDist(){
	var cityId = $("#city").val();
	var params={  
	        'cityId':cityId  
    };  
	$.ajax({
        type: 'get',
        url: "/pdm/ProducingAreaSelect.do",
        data: params,
        dataType: 'json',
        success: function(data){
	      	var sel2 = $("#region");  
      		sel2.empty();  
	      	if(data==null) {
      		 	sel2.append("<option value = '-1'>"+"城市列表为空"+"</option>");
          	}else {
          		sel2.append("<option value = '-1'>"+"--请选择区/县--"+"</option>");
          		var items=data.result;
		    	    	if(items!=null) {
		        	  for(var i =0;i<items.length;i++) {
		          	var item=items[i];
		            sel2.append("<option value = '"+item.regionId+"'>"+item.regionName+"</option>");
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

function confimSeleted(){
	var provinceId = $("#province").val();
	var cityId = $("#city").val();
	var regionId = $("#region").val();
	if (provinceId != -1 && cityId != -1 && regionId != -1) {
		var provinceName =  $("#province").find("option:selected").text();
		var cityName =  $("#city").find("option:selected").text();
		var regionName =  $("#region").find("option:selected").text();
		var regionInfo = provinceName + cityName + regionName;
		var regionValue =  provinceName + "-" + cityName + "-" + regionName;
		if($("#"+regionValue).html() != undefined){
			alert("已经选择该地区")
		}else {
			$("#newProducingArea").append("<div id="+regionValue+">"+regionInfo+"<em ><a href=# onclick=delArea('"+regionValue+"')>删除</a></em></div>");
			var currentRegion = $("#currentRegion").val();
			$("#currentRegion").val(currentRegion+";"+regionValue)
		}
		
	} else{
		alert("地区信息选择有误");
	}
}
function delArea(value){
	var currentRegion = $("#currentRegion").val();
	currentRegion = currentRegion.replace(new RegExp(value, "g"),"");
	$("#currentRegion").val(currentRegion);
	$("div#"+value).remove();
}
function continueSelect(){
	$("#province").find("option[value='-1']").attr("selected",true);
	$("#city").empty().append("<option value='-1'>--请选择城市--</option>");
	$("#region").empty().append("<option value='-1'>--请选择区/县--</option>");
}
function callbackTo(){
		var result = $("#currentRegion").val();
		result = result.replace(/-/g,",");
		self.parent.<%=callbackFun %>(result);
		try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
	    try{ jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</body>
</html>