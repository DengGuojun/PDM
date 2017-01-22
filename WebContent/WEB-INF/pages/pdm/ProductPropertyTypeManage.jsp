<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@page import="com.lpmas.pdm.bean.*"%>
<%@page import="com.lpmas.pdm.config.*"%>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@page import="com.lpmas.framework.util.*"%>
<%@page import="com.lpmas.framework.config.Constants"%>
<%@page import="com.lpmas.framework.bean.StatusBean"%>
<%@page import="com.lpmas.admin.business.AdminUserHelper"%>
<%	
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	ProductPropertyTypeBean propertyTypeBean = (ProductPropertyTypeBean) request.getAttribute("PropertyTypeBean");
	List<ProductTypeBean> productTypeList = (List<ProductTypeBean>) request.getAttribute("ProductTypeList");
	List<ProductTypeBean> subProductTypeList = (List<ProductTypeBean>) request.getAttribute("SubProductTypeList");
	int typeId = (Integer) request.getAttribute("TypeId");
	int parentTypeId = (Integer) request.getAttribute("ParentTypeId");
	ProductTypeBean parentProductTypeBean = (ProductTypeBean) request.getAttribute("ParentProductTypeBean");
	ProductTypeBean productTypeBean = (ProductTypeBean) request.getAttribute("ProductTypeBean");
	int parentPropertyId = (Integer) request.getAttribute("ParentPropertyId");
	ProductPropertyTypeBean parentPropertyTypeBean  = (ProductPropertyTypeBean) request.getAttribute("ParentPropertyTypeBean");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品属性配置</title>
<%@ include file="../include/header.jsp"%>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="../js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductPropertyTypeList.do">商品属性列表</a>&nbsp;>&nbsp;</li>
			<% if (parentPropertyTypeBean != null) {%>
			<li><%=parentPropertyTypeBean.getPropertyName()%>&nbsp;>&nbsp;</li>
			<%} %>
			<% if (propertyTypeBean.getPropertyId() > 0) {%>
			<li><%=propertyTypeBean.getPropertyName() %>&nbsp;>&nbsp;</li>
			<li>修改商品属性信息</li>
			<%} else { %>
			<li>新建商品属性信息</li>
			<%} %>
		</ul>
	</div>
	<%if(propertyTypeBean.getPropertyId() > 0 && propertyTypeBean.getInputMethod() == ProductPropertyConfig.INPUT_METHOD_SELECT && "".equals(propertyTypeBean.getFieldSource())){%>
	<div class="article_tit">
		<p class="tab">
		<a href="ProductPropertyTypeManage.do?propertyId=<%=propertyTypeBean.getPropertyId()%>&readOnly=<%=readOnly%>">商品属性信息</a>
		<a href="ProductPropertyOptionList.do?propertyId=<%=propertyTypeBean.getPropertyId()%>&readOnly=<%=readOnly%>">商品属性选项</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<form id="formData" name="formData" method="post" action="/pdm/ProductPropertyTypeManage.do" onsubmit="javascript:return checkForm('formData');">
		<div class="modify_form">
			<input type="hidden" name="parentPropertyId" id="parentPropertyId" value="<%=parentPropertyId%>" />
			<input type="hidden" name="propertyId" id="propertyId" value="<%=propertyTypeBean.getPropertyId()%>" />
			<%if(parentPropertyId > 0) { %>
      			<input type="hidden" name="propertyCode"  value="<%=parentPropertyTypeBean.getPropertyCode()%>"/>
    			<%} else{%>
    			<p>
      		<em class="int_label"><span>*</span>属性代码：</em>
				<input type="text" name="propertyCode" id="propertyCode" size="50" value="<%=propertyTypeBean.getPropertyCode()%>" maxlength="50" checkStr="属性代码;code;true;;50" <%=propertyTypeBean.getPropertyId() > 0 ? "readonly" : "" %> /> 
			</p>
			<%} %>
   		 	<p>
      		<em class="int_label"><span>*</span>属性名称：</em>
				<input type="text" name="propertyName" id="propertyName" value="<%=propertyTypeBean.getPropertyName()%>" maxlength="200" checkStr="属性名称;txt;true;;200" /> 
			</p>
			<%if(parentPropertyId > 0) { %>
      			<input type="hidden" name="typeId"  value="<%=parentPropertyTypeBean.getTypeId()%>"/>
      			<input type="hidden" name="infoType"  value="<%=parentPropertyTypeBean.getInfoType()%>"/>
      			<input type="hidden" name="propertyType"  value="<%=parentPropertyTypeBean.getPropertyType()%>"/>
    			<%} else{%>
			<p>
      		<em class="int_label"><span>*</span>商品分类：</em>
      			<%if(propertyTypeBean.getPropertyId() > 0) {%>
      			<em><%=parentProductTypeBean.getTypeName()%>
      			 	<%if(productTypeBean != null) {%>
      			 	-- <%=productTypeBean.getTypeName()%>
      			 	<%} %>
      			 </em>
      			<input type="hidden" name="typeId" value="<%=propertyTypeBean.getTypeId()%>" />
      			<%}else{ %>
				<select id="parentType" name="parentType" onchange="typeSelectionChange()" checkStr="商品分类;txt;true;;100">
					<option value="">请选择顶级分类</option>
					<%
						for (ProductTypeBean typeBean : productTypeList) {
					%>
					<option value="<%=typeBean.getTypeId()%>" <%=typeBean.getTypeId() == parentTypeId ? "selected" : ""%>><%=typeBean.getTypeName()%></option>
					<%
						}
					%>
				</select> 
				<select id="typeSelection" name="typeId" >
					<option value="0">全部</option>
					<%
						for (ProductTypeBean subTypeBean : subProductTypeList) {
					%>
					<option value="<%=subTypeBean.getTypeId()%>" <%=subTypeBean.getTypeId() == typeId ? "selected" : ""%>><%=subTypeBean.getTypeName()%></option>
					<%
						}
					%>
				</select> 
				<%} %>
			</p>
			<p>
      		<em class="int_label"><span>*</span>信息类型：</em>
      		<%if(propertyTypeBean.getPropertyId() > 0) {%>
      			<em><%=ProductPropertyConfig.INFO_TYPE_MAP.get(propertyTypeBean.getInfoType())%></em>
      			<input type="hidden" name="infoType" value="<%=propertyTypeBean.getInfoType() %>" />
     		<%}else{ %>
				<select id="infoType" name="infoType" checkStr="信息类型;txt;true;;100">
						<option value="">请选择</option>
						<%
							int infoType = propertyTypeBean.getInfoType();
							for (StatusBean<Integer, String> infoTypeItem : ProductPropertyConfig.INFO_TYPE_LIST) {
						%>
						<option value="<%=infoTypeItem.getStatus()%>" <%=infoType == infoTypeItem.getStatus() ? "selected" : ""%>><%=infoTypeItem.getValue()%></option>
						<%
							}
						%>
				</select> 
			<%} %>	
			</p>
			<p>
      		<em class="int_label"><span>*</span>属性类型：</em>
				<select id="propertyType" name="propertyType" checkStr="属性类型;txt;true;;100">
						<option value="">请选择</option>
						<%
							int propertyTypeVar = propertyTypeBean.getPropertyType();
							for (StatusBean<Integer, String> propertyType : ProductPropertyConfig.PROP_TYPE_LIST) {
						%>
						<option value="<%=propertyType.getStatus()%>" <%=propertyTypeVar == propertyType.getStatus() ? "selected" : ""%>><%=propertyType.getValue()%></option>
						<%
							}
						%>
				</select>
			</p>
			<%} %>
			<p>
      		<em class="int_label"><span>*</span>输入方式：</em>
			<select id="inputMethod" name="inputMethod" checkStr="输入方式;txt;true;;100">
						<option value="">请选择</option>
						<%
							int input = propertyTypeBean.getInputMethod();
							for (StatusBean<Integer, String> inputMethod : ProductPropertyConfig.INPUT_METHOD_LIST) {
						%>
						<option value="<%=inputMethod.getStatus()%>" <%=input == inputMethod.getStatus() ? "selected" : ""%>><%=inputMethod.getValue()%></option>
						<%
							}
						%>
				</select> 
			</p>
			<p>
      		<em class="int_label">文本框样式：</em>
				<input type="text" name="inputStyle" id="inputStyle" value="<%=HtmlStringKit.toHtml(propertyTypeBean.getInputStyle())%>" size="50" maxlength="2000" checkStr="文本框样式;txt;false;;2000"/>Json格式，如：{"maxlength":"100","size":"50"}
			</p>
			<p>
      		<em class="int_label">文本框说明：</em>
				<input type="text" name="inputDesc" id="inputDesc" value="<%=propertyTypeBean.getInputDesc()%>" size="50" maxlength="2000" checkStr="文本框说明;txt;false;;2000"/>
			</p>
			<p>
      		<em class="int_label"><span>*</span>数据字段类型：</em>
				<select id="fieldType" name="fieldType" checkStr="数据类型字段;txt;true;;100">
						<option value="">请选择</option>
						<%
							int fieldType = propertyTypeBean.getFieldType();
							for (StatusBean<Integer, String> fieldTypeBean : ProductPropertyConfig.FIELD_TYPE_LIST) {
						%>
						<option value="<%=fieldTypeBean.getStatus()%>" <%= fieldType == fieldTypeBean.getStatus() ? "selected" : ""%>><%=fieldTypeBean.getValue()%></option>
						<%
							}
						%>
				</select>
			</p>
			<p>
      		<em class="int_label">数据字段格式：</em>
				<input type="text" name="fieldFormat" id="fieldFormat" value="<%=propertyTypeBean.getFieldFormat()%>" size="50" maxlength="200" checkStr="数据字段格式;txt;false;;200"/>内容为正则表达式，用于输入时限制或提交时校验时使用。
			</p>
			<p>
      		<em class="int_label">数据字段来源：</em>
				<input type="text" name="fieldSource" id="fieldSource" value="<%=HtmlStringKit.toHtml(propertyTypeBean.getFieldSource())%>" size="50" maxlength="200" checkStr="数据字段格式;txt;false;;200"/>当输入方式为外部选择框时，需填写数据来源的URL
			</p>
			<p>
      		<em class="int_label">数据字段存储：</em>
				<input type="text" name="fieldStorage" id="fieldStorage" value="<%=HtmlStringKit.toHtml(propertyTypeBean.getFieldStorage())%>" size="50" maxlength="200" checkStr="数据字段格式;txt;false;;200"/>记录内容存储在数据表中的位置，采用json格式存储。如：{"relation": "ONE_TO_ONE/ONE_TO_MANY","table": "xxx"，"field":"xxx"}
			</p>
			<p>
      		<em class="int_label">默认值：</em>
				<input type="text" name="defaultValue" id="defaultValue" value="<%=propertyTypeBean.getDefaultValue()%>" maxlength="2000" checkStr="默认值;txt;false;;2000"/>
			</p>
			<p>
      		<em class="int_label">是否必填：</em>
				<input type="checkbox" id="isRequired" name="isRequired" value="<%=Constants.STATUS_VALID%>" <%=(propertyTypeBean.getIsRequired() == Constants.STATUS_VALID) ? "checked" : ""%> />
			</p>
			<p>
      		<em class="int_label">
				是否可修改：</em>
				<input type="checkbox" id="isModifiable" name="isModifiable" value="<%=Constants.STATUS_VALID%>" <%=(propertyTypeBean.getIsModifiable() == Constants.STATUS_VALID) ? "checked" : ""%> />
			</p>
			<p>
      		<em class="int_label">优先级：</em>
			<input type="text" name="priority" id="priority" value="<%=propertyTypeBean.getPriority()%>" maxlength="6" checkStr="优先级;num;false;;6"/>
			</p>
			<p>
      		<em class="int_label">有效状态：</em>
      		<input type="checkbox" id="status" name="status" value="<%=Constants.STATUS_VALID%>" <%=(propertyTypeBean.getStatus() == Constants.STATUS_VALID) ? "checked" : ""%> />
			</p>
			<p class="p_top_border">
      		<em class="int_label">备注：</em>
      		<textarea name="memo" id="memo" cols="60" rows="3"><%=propertyTypeBean.getMemo()%></textarea>
			</p>
			</div>
			<div class="div_center">
				<input  type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
				<a href="ProductPropertyTypeList.do"><input type="button" name="cancel" id="cancel" class="modifysubbtn" value="返回" /></a>
			</div>
	</form>
</body>
</html>
<script type="text/javascript">
$(document).ready(function() {
	var readonly = '${readOnly}';
	if(readonly=='true') {
		disablePageElement();
	}
});
function typeSelectionChange(){
	var parentTypeId=$("#parentType").val();  
	if(parentTypeId != null){
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
	      		 	sel2.append("<option value ='0'>全部</option>");
	          	}else {
	          		var items=data.result;
		    	    		if(items!=null) {
		    	    	  	sel2.append("<option value ='0'>全部</option>");
		        	  	for(var i =0;i<items.length;i++) {
		          		var item=items[i];
		            		sel2.append("<option value = '"+item.typeId+"'>"+item.typeName+"</option>");
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
}
</script>