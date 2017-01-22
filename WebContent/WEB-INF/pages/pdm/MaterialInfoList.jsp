<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.bean.*"%>
<%@ page import="com.lpmas.framework.page.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.admin.config.*"%>
<%@ page import="com.lpmas.pdm.config.*"%>
<%@ page import="com.lpmas.pdm.bean.*"%>

<%
	AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	List<MaterialInfoBean> list = (List<MaterialInfoBean>) request.getAttribute("MaterialList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	List<MaterialInfoEntityBean> materialInfoEntityList = (List<MaterialInfoEntityBean>) request
			.getAttribute("materialInfoEntityList");
	Map<Integer, MaterialTypeBean> materialTypeMap = (Map<Integer, MaterialTypeBean>)request.getAttribute("MaterialTypeMap");
	Map<Integer, UnitInfoBean> unitInfoMap = (Map<Integer, UnitInfoBean>)request.getAttribute("UnitInfoMap");
%>

<%@ include file="../include/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物料管理</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>

<body class="article_bg">
	<p class="article_tit">物料列表</p>
	<form name="formSearch" method="post" action="MaterialInfoList.do">
		<div class="search_form">
			<em class="em1">物料编码：</em> <input type="text" name="materialNumber"
				id="materialNumber"
				value="<%=ParamKit.getParameter(request, "materialNumber", "")%>"
				size="20" /> <em class="em1">物料名称：</em> <input type="text"
				name="materialName" id="materialName"
				value="<%=ParamKit.getParameter(request, "materialName", "")%>"
				size="20" /> <em class="em1">有效状态：</em> <select name="status"
				id="status">
				<%
					int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
					for (StatusBean<Integer, String> statusBean : Constants.STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == status) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
			<%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_INFO, OperationConfig.SEARCH)){ %> 
			<input name="" type="submit" class="search_btn_sub" value="查询" />
			<%} %>
		</div>
		<table width="100%" border="0"  cellpadding="0"
			class="table_style">
			<tr>
				<th>物料ID</th>
				<th>物料缩略图</th>
				<th>物料编码</th>
				<th>物料名称</th>
				<th>物料类型</th>
				<th>物料规格</th>
				<th>计量单位</th>
				<th>使用状态</th>
				<th>有效状态</th>
				<th>操作</th>
			</tr>
			<%
				for (MaterialInfoEntityBean bean : materialInfoEntityList) {
			%>
			<tr>
				<td><%=bean.getMaterialId()%></td>
				<td><img src="<%=FILE_URL%><%=bean.getImageThumbnailUrl()%>" /></td>
				<td><%=bean.getMaterialNumber()%></td>
				<td><%=bean.getMaterialName()%></td>
				<td><%=materialTypeMap.get(bean.getTypeId1()).getTypeName() %></td>
				<td><%=bean.getMaterialInfoBean().getSpecification()%></td>
				<%if(!"".equals(bean.getMaterialInfoBean().getUnit())){ %> 
				<td><%=unitInfoMap.get(bean.getMaterialInfoBean().getUnit()).getUnitName()%></td>
				 <%}else{ %> <td>未录入</td> <%} %>
				<td><%=MapKit.getValueFromMap(bean.getMaterialInfoBean().getUseStatus(),
						MaterialConfig.MATERIAL_USE_STATUS_MAP)%></td>
				<td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
				<td align="center">
				<a href="/pdm/MaterialInfoManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=true">查看</a>
				<%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_INFO, OperationConfig.UPDATE)){ %>
				 | <a href="/pdm/MaterialInfoManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=false">修改</a>
				<%} %>
				</td>
			</tr>
			<%
				}
			%>
		</table>
	</form>
	<ul class="page_info">
		<li class="page_left_btn">
		<%if(adminUserHelper.hasPermission(PdmResource.MATERIAL_INFO, OperationConfig.CREATE)){ %>
		<input type="button" name="button" id="button" value="新建"></li>
		<%} %>
		<%@ include file="../include/page.jsp"%>
	</ul>
</body>
<script type='text/javascript'>
$(document).ready(function() {
	$("#button").click(
		function() {
			$.fancybox.open({
				href : 'MaterialTypeSelect.do?callbackFun=selectMaterialType',
				type : 'iframe',
				scrolling : 'auto',
				width : 560,
				minHeight : 150
		});
	});
});
function selectMaterialType(typeId) {
	var url = "MaterialInfoManage.do?typeId="+ typeId;
	window.location.href= url
}
</script>
</html>