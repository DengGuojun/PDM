<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.business.*"  %>
<%@ page import="com.lpmas.framework.web.*"%>
<% 
	MaterialInfoBean bean = (MaterialInfoBean)request.getAttribute("materialInfoBean");
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	int materialId = bean.getMaterialId();
	List<MaterialPropertyTypeBean> materialPropertyTypeList = (List<MaterialPropertyTypeBean>) request.getAttribute("propertyTypeList");
	List<MaterialPropertyBean> materialPropertyList = (List<MaterialPropertyBean>) request.getAttribute("propertyList");
	Map<String, MaterialPropertyBean> materialPropertyMap = (Map<String, MaterialPropertyBean>) request.getAttribute("propertyTypeMap");
	Map<String,MaterialPropertyTypeBean> subPropertyTypeMap = (Map<String,MaterialPropertyTypeBean>)request.getAttribute("subPropertyTypeMap");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
	
%>
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>物料管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/ui.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a>
		<ul class="art_nav">
			<li><a href="MaterialInfoList.do">物料列表</a>&nbsp;>&nbsp;</li>
			<li><%=bean.getMaterialName()%>&nbsp;>&nbsp;</li>
			<li>修改物料仓储信息</li>
		</ul>
	</div>
	<div class="article_tit">
		<p class="tab">
		<a href="MaterialInfoManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=<%=readOnly%>">物料信息</a>
		<a href="MaterialInfoStorageManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=<%=readOnly%>">仓储信息</a> 
		<a href="MaterialInfoImageManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=<%=readOnly%>">物料图片</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<form id="formData" name="formData" method="post" action="MaterialInfoStorageManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="materialId" id="materialId" value="<%=bean.getMaterialId() %>"/>
	  <div>
	  	<%
		if(materialPropertyTypeList.size()>0){
			%>
			<div class="modify_form">
			<%for(MaterialPropertyTypeBean typeBean : materialPropertyTypeList) {
				if(typeBean.getParentPropertyId()==0){%>
				<p>
     				<%if(typeBean.getIsRequired()==Constants.STATUS_VALID){ %>
						<em class="int_label"><span>*</span><%=typeBean.getPropertyName()%>
						:
					</em>
					<%}else{ %>
					<em class="int_label"> <%=typeBean.getPropertyName()%>
						:
					</em>
					<%} %>
					<%out.print(PropertyDisplayUtil.displayPropertyInput(typeBean, materialPropertyMap.get(typeBean.getPropertyCode()),false)); 
					if(subPropertyTypeMap.get(typeBean.getPropertyCode()) != null ){ 
							out.print(PropertyDisplayUtil.displayPropertyInput(subPropertyTypeMap.get(typeBean.getPropertyCode()), materialPropertyMap.get(typeBean.getPropertyCode()),true));
						}%>
				</p>
					
				<% }%>
				
			<%} %>
			</div>
			<div class="div_center">
	  			<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  			<a href="MaterialInfoList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	 		</div>
		<%} %>
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