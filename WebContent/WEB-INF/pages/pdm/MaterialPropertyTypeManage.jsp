<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@page import="com.lpmas.pdm.bean.*"%>
<%@page import="com.lpmas.pdm.config.*"%>
<%@page import="com.lpmas.framework.util.*"%>
<%@page import="com.lpmas.framework.config.Constants"%>
<%@page import="com.lpmas.framework.bean.StatusBean"%>
<%@page import="com.lpmas.admin.business.AdminUserHelper"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%	
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	MaterialPropertyTypeBean propertyTypeBean = (MaterialPropertyTypeBean) request.getAttribute("PropertyTypeBean");
	List<MaterialTypeBean> materialTypeList = (List<MaterialTypeBean>) request.getAttribute("MaterialTypeList");
	int typeId = (Integer) request.getAttribute("TypeId");
	MaterialTypeBean materialTypeBean = (MaterialTypeBean) request.getAttribute("MaterialTypeBean");
	int parentPropertyId = (Integer) request.getAttribute("ParentPropertyId");
	MaterialPropertyTypeBean parentPropertyTypeBean  = (MaterialPropertyTypeBean) request.getAttribute("ParentPropertyTypeBean");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物料属性配置</title>
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
			<li><a href="MaterialPropertyTypeList.do">物料属性列表</a>&nbsp;>&nbsp;</li>
			<%if (parentPropertyTypeBean != null) {%> 
			<li><%=parentPropertyTypeBean.getPropertyName() %><li>&nbsp;>&nbsp;
			<%} %>
			<% if (propertyTypeBean.getPropertyId() > 0) {%>
			<li><%=propertyTypeBean.getPropertyName() %>&nbsp;>&nbsp;</li>
			<li>修改分类属性信息</li>
			<%} else {%>
			<li>新增分类属性信息</li>
			<%}%>
		</ul>
	</div>
	<%if(propertyTypeBean.getPropertyId() > 0 && propertyTypeBean.getInputMethod() == MaterialPropertyConfig.INPUT_METHOD_SELECT && "".equals(propertyTypeBean.getFieldSource())){%>
	<div class="article_tit">
		<p class="tab">
		<a href="MaterialPropertyTypeManage.do?propertyId=<%=propertyTypeBean.getPropertyId()%>&readOnly=<%=readOnly%>">物料属性信息</a> 
		<a href="MaterialPropertyOptionList.do?propertyId=<%=propertyTypeBean.getPropertyId()%>&readOnly=<%=readOnly%>">物料属性选项</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<form id="formData" name="formData" method="post" action="MaterialPropertyTypeManage.do" onsubmit="javascript:return checkForm('formData');">
		<div class="modify_form">
			<input type="hidden" name="parentPropertyId" id="parentPropertyId" value="<%=parentPropertyId%>" />
			<input type="hidden" name="propertyId" id="propertyId" value="<%=propertyTypeBean.getPropertyId()%>" />
			<%if(parentPropertyId > 0) { %>
      			<input type="hidden" name="propertyCode"  value="<%=parentPropertyTypeBean.getPropertyCode()%>"/>
    			<%} else{%>
    			<p>
      		<em class="int_label"><span>*</span>属性代码：</em>
				<input type="text" name="propertyCode" id="propertyCode" size="50" value="<%=propertyTypeBean.getPropertyCode()%>" maxlength="50" checkStr="属性代码;code;true;;50" />
			</p>
			<%} %>
   		 	<p>
      		<em class="int_label"><span>*</span>属性名称：</em>
				<input type="text" name="propertyName" id="propertyName" value="<%=propertyTypeBean.getPropertyName()%>" maxlength="200" checkStr="属性名称;txt;true;;200" />
			</p>
			<%if(parentPropertyId > 0) { %>
      			<input type="hidden" name="materialTypeId"  value="<%=parentPropertyTypeBean.getMaterialTypeId()%>"/>
      			<input type="hidden" name="propertyType"  value="<%=parentPropertyTypeBean.getPropertyType()%>"/>
    			<%} else{%>
			<p>
      		<em class="int_label"><span>*</span>物料分类：</em>
      			<%if(propertyTypeBean.getPropertyId() > 0) {%>
      				<em><%=materialTypeBean.getTypeName()%></em>
      				<input type="hidden" name="materialTypeId" value="<%=propertyTypeBean.getMaterialTypeId()%>" />
      			<%}else{ %>
					<select id="materialTypeId" name="materialTypeId">
					<option value="">请选择物料分类</option>
					<%
						for (MaterialTypeBean typeBean : materialTypeList) {
					%>
					<option value="<%=typeBean.getTypeId()%>" <%=typeBean.getTypeId() == propertyTypeBean.getMaterialTypeId() ? "selected" : ""%>><%=typeBean.getTypeName()%></option>
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
							for (StatusBean<Integer, String> propertyType : MaterialPropertyConfig.PROP_TYPE_LIST) {
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
							for (StatusBean<Integer, String> inputMethod : MaterialPropertyConfig.INPUT_METHOD_LIST) {
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
							for (StatusBean<Integer, String> fieldTypeBean : MaterialPropertyConfig.FIELD_TYPE_LIST) {
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
				<a href="MaterialPropertyTypeList.do"><input type="button" name="cancel" id="cancel" class="modifysubbtn" value="返回"  /></a>
			</div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readonly = '${readOnly}';
	if(readonly=='true')
	{
		disablePageElement();
	}
});
</script>
</html>