<%@page import="com.lpmas.pdm.cache.UnitInfoCache"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
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
	String selectedItem = ParamKit.getParameter(request, "selectedItem", "");
	List<String> selectedItemList = new ArrayList<String>();
	if(!selectedItem.trim().equals("")){
		selectedItemList = ListKit.string2List(selectedItem, ",");
	}
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	String openOrPop = ParamKit.getParameter(request, "openOrPop", "open");
	boolean isMultiple = ParamKit.getBooleanParameter(request, "isMultiple",true);
	AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	List<MaterialInfoBean> list = (List<MaterialInfoBean>) request.getAttribute("MaterialList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	List<MaterialInfoEntityBean> materialInfoEntityList = (List<MaterialInfoEntityBean>) request
			.getAttribute("materialInfoEntityList");
	Map<Integer, MaterialTypeBean> materialTypeMap = (Map<Integer, MaterialTypeBean>)request.getAttribute("MaterialTypeMap");
	Map<Integer, Map<String, MaterialPropertyBean>> materialPropertyMap = (Map<Integer, Map<String, MaterialPropertyBean>>)request.getAttribute("MaterialPropertyMap");
	Map<Integer, UnitInfoBean> unitInfoMap = (Map<Integer, UnitInfoBean>)request.getAttribute("UnitInfoMap");
%>

<%@ include file="../include/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加物料</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">
document.domain='<%=DOMAIN%>';
</script>
</head>

<body class="article_bg">
	<p class="article_tit">添加物料</p>
	<form name="formSearch" method="post" action="MaterialInfoSelect.do?selectedItem=<%=selectedItem%>&callbackFun=<%=callbackFun%>&openOrPop=<%=openOrPop%>">
		<div class="search_form">
			<em class="em1">物料编码：</em> <input type="text" name="materialNumber" id="materialNumber" value="<%=ParamKit.getParameter(request, "materialNumber", "")%>" size="20" />
		    <em class="em1">物料名称：</em> <input type="text" name="materialName" id="materialName" value="<%=ParamKit.getParameter(request, "materialName", "")%>" size="20" /> 
			<input type="hidden" name="isMultiple" value="<%=isMultiple %>"/>	
			<input name="" type="submit" class="search_btn_sub" value="查询" />
		</div>
		<table width="100%" border="0"  cellpadding="0"
			class="table_style">
			<tr>
			<%if(isMultiple){%>
			<th><input type="checkbox" id="selectAll" onchange="checkAll(this)">选择</th>
			<%}else{%>
			<th>选择</th>
			<%} %>
		      <th>外观图</th>
		      <th>物料编码</th>
		      <th>物料名称</th>
		      <th>型号规格</th>
		      <th>操作</th>
		    </tr>
  <%Map<String,Object> itemValueMap = new HashMap<String,Object>();
  UnitInfoCache unitCache = new UnitInfoCache();%>
  <%
    for(MaterialInfoEntityBean bean:materialInfoEntityList){
    	itemValueMap.put("itemId", String.valueOf(bean.getMaterialId()));
    	itemValueMap.put("itemTypeName", materialTypeMap.get(bean.getTypeId1()).getTypeName());
    	itemValueMap.put("itemNumber", bean.getMaterialNumber());
    	itemValueMap.put("itemName", bean.getMaterialName());
    	itemValueMap.put("itemGuaranteePeriod", bean.getMaterialInfoBean().getGuaranteePeriod());
    	itemValueMap.put("itemSpecification", bean.getMaterialInfoBean().getSpecification());
    	itemValueMap.put("itemTypeId",String.valueOf(materialTypeMap.get(bean.getTypeId1()).getTypeId()));
    	itemValueMap.put("itemThumbPic",bean.getImageThumbnailUrl());
    	itemValueMap.put("itemPropertyList",materialPropertyMap.get(bean.getMaterialId()));
    	itemValueMap.put("wareType", InfoTypeConfig.INFO_TYPE_MATERIAL);
    	itemValueMap.put("itemUnit", bean.getMaterialInfoBean().getUnit());
    	UnitInfoBean unit = unitCache.getUnitInfoByCode(bean.getMaterialInfoBean().getUnit());
    	itemValueMap.put("itemUnitName", unit==null? "":unit.getUnitName());
    %> 
    <tr>
    <%if(isMultiple){%>
			<%if(selectedItemList.contains((bean.getMaterialId()+"").trim())){ %>
   			<td><input type="checkbox" name="select" selected="selected" onclick="alert('此物料您已经选择,请勿重复选择!');return false;"/></td>
      		<%}else{ %>
      		<td><input type="checkbox" name="select" /></td>
      		<%} %>
	<%}else{%>
			<td><input type="radio" name="radio" /></td>
	<%} %>
      <td><img src="<%=FILE_URL%><%=bean.getImageThumbnailUrl()%>"/></td>
      <td><%=bean.getMaterialNumber() %></td>
      <td><%=bean.getMaterialName() %></td>
      <td><%=bean.getMaterialInfoBean().getSpecification() %></td>
      <td align="center">
      	<a href="/pdm/MaterialInfoManage.do?materialId=<%=bean.getMaterialId() %>&readOnly=true" target="_Blank">查看</a></td>
      <td><input type="hidden" id="itemValueJson" value='<%=JsonKit.toJson(itemValueMap)%>' ></td>
    </tr>
    <%itemValueMap.clear();} %>
		</table>
	</form>
	<ul class="page_info">
		<li class="page_left_btn">
		<input type="button" name="button" id="button" class="modifysubbtn" onclick="excuteCallBack()" value="添加" /></li>
		<%@ include file="../include/page.jsp"%>
	</ul>
	<input type="button"  class="btn_fixed" onclick="excuteCallBack()" value="添加" /></li>
</body>
<script type='text/javascript'>
function excuteCallBack(){
	<%if(isMultiple){%>
	var checkboxs = $('input[name=select]:checked');
	var itemValue = new Array();
	if(checkboxs.length>0){
		checkboxs.each(function(){
			itemValue.push($(this).parent().parent().find('#itemValueJson').val());
		});
	<%}else{%>
	var radio = $('input:radio[name=radio]:checked');
	var itemValue = new Array();
	if(radio.length==1){
		itemValue.push(radio.parent().parent().find('#itemValueJson').val());
	<%} %>
		<%
		if(openOrPop.equals("open")){%>
		window.opener.<%=callbackFun%>(itemValue);
		self.close();
		<%}else if(openOrPop.equals("pop")){%>
		self.parent.<%=callbackFun%>(itemValue);
		try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
		<%}%>
	}else{
		alert("请选择物料!");
	}
}
</script>
</html>