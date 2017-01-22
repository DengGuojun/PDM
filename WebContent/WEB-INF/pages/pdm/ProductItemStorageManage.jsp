<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.business.*"  %>
<% 
	ProductItemBean bean = (ProductItemBean)request.getAttribute("ProductItem");
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<AdminGroupInfoBean> groupList = adminHelper.getUserGroupList();
	int productId = bean.getProductId();
	List<ProductPropertyTypeBean> productPropertyTypeList = (List<ProductPropertyTypeBean>) request.getAttribute("ProductPropertyTypeList");
	Map<String, ProductPropertyTypeBean> subPropertyTypeMap = (Map<String, ProductPropertyTypeBean>) request.getAttribute("SubPropertyTypeMap");
	List<ProductPropertyBean> productPropertyList = (List<ProductPropertyBean>) request.getAttribute("ProductPropertyList");
	Map<String, ProductPropertyBean> productPropertyMap = (Map<String, ProductPropertyBean>) request.getAttribute("ProductPropertyMap");
	List<ProductMultiplePropertyBean> productMultiplePropertyList = (List<ProductMultiplePropertyBean>) request.getAttribute("ProductMultiplePropertyList");
	Map<String, List<ProductMultiplePropertyBean>> productMultiplePropertyMap = (Map<String, List<ProductMultiplePropertyBean>> ) request.getAttribute("ProductMultiplePropertyMap");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
	
%>
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>商品管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/ui.js"></script>
	<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
	</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductItemList.do">商品项列表</a>&nbsp;>&nbsp;</li>
			<li><%=bean.getItemName()%>&nbsp;>&nbsp;</li>
			<li>修改商品项仓储属性</li>
		</ul>
	</div>
	<%@ include file="../nav/ProductItemNav.jsp" %>
	<form id="formData" name="formData" method="post" action="ProductItemStorageManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="itemId" id="itemId" value="<%=bean.getItemId() %>"/>
	  <div>
	  	<%
		if(productPropertyTypeList.size()>0){
			%>
			<div class="modify_form">
			<%for(ProductPropertyTypeBean typeBean : productPropertyTypeList) {%>
			<%if (typeBean.getParentPropertyId() == 0) { %>
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
					<% if(typeBean.getFieldStorage().length() != 0 ){
						out.print(MultiplePropertyDisplayUtil.displayMultiplePropertyInput(typeBean, productMultiplePropertyMap.get(typeBean.getPropertyCode())));
					} else {
						out.print(PropertyDisplayUtil.displayPropertyInput(typeBean, productPropertyMap.get(typeBean.getPropertyCode()),false));
						if(subPropertyTypeMap.get(typeBean.getPropertyCode()) != null){ 
							out.print(PropertyDisplayUtil.displayPropertyInput(subPropertyTypeMap.get(typeBean.getPropertyCode()), productPropertyMap.get(typeBean.getPropertyCode()),true));
						} 
					}%>
				</p>
			<%} %>	
			<%} %>
			</div>
			<div class="div_center">
			  	<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
			  	<a href="ProductItemList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
		    </div>
		<%} %>
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readonly = '${readOnly}';
	if(readonly=='true') {
		disablePageElement();
	}
});
</script>
</html>