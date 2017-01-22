<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@page import="com.lpmas.pdm.config.ProductConfig"%>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ page import="com.lpmas.pdm.business.*"  %>
<% 
	ProductInfoBean bean = (ProductInfoBean)request.getAttribute("ProductInfo");
	ProductTypeBean typeBean1 = (ProductTypeBean)request.getAttribute("ProductTypeBean1");
	ProductTypeBean typeBean2 = (ProductTypeBean)request.getAttribute("ProductTypeBean2");
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<AdminGroupInfoBean> groupList = adminHelper.getUserGroupList();
	List<BrandInfoBean> brandList = (List<BrandInfoBean>) request.getAttribute("BrandList");
	BrandInfoBean brandInfoBean = (BrandInfoBean) request.getAttribute("BrandInfoBean");
	List<StatusBean<String, String>> qualityLevelList = (List<StatusBean<String, String>>)request.getAttribute("QualityLevelList");
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
	<script type="text/javascript" src="<%=STATIC_URL %>/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
	</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductInfoList.do">商品列表</a>&nbsp;>&nbsp;</li>
			<% if(productId > 0) {%>
			<li><%=bean.getProductName()%>&nbsp;>&nbsp;</li>
			<li>修改商品信息</li>
			<%} else { %>
			<li>新建商品信息</li>
			<%} %>
		</ul>
	</div>
	<%if(bean.getProductId() > 0){%>
	<%@ include file="../nav/ProductInfoNav.jsp" %>
	<%} %>
	<form id="formData" name="formData" method="post" action="ProductInfoManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="productId" id="productId" value="<%=bean.getProductId() %>"/>
	  <input type="hidden" name="typeId1" id="typeId1" value="<%=bean.getTypeId1() %>"/>
	  <input type="hidden" name="typeId2" id="typeId2" value="<%=bean.getTypeId2() %>"/>
	  <div class="modify_form">
	    <p>
	      <em class="int_label"><span>*</span>商品名称：</em>
	      <input type="text" name="productName" id="productName" size="50" maxlength="100" value="<%=bean.getProductName() %>" checkStr="商品名称;txt;true;;100"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>商品编码：</em>
	      <input type="text" name="productNumber" id="productNumber" size="50" maxlength="50" readonly value="<%=bean.getProductNumber() %>" /><em>（系统根据规则自动生成）</em>
	    </p>
	    <p>
	    		<em class="int_label"><span>*</span>品牌：</em>    
	      	<select name="brandId" id="brandId">
	      		<%for(BrandInfoBean brandBean : brandList){ %><option value="<%=brandBean.getBrandId() %>" <%=(brandBean.getBrandId()==bean.getBrandId())?"selected":"" %>><%=brandBean.getBrandName() %></option><%} %>
	      	</select>
	    </p>
	    <p>
	    		<em class="int_label">商品类型：</em> 
	    		<em><%=typeBean1.getTypeName() %>  <%=typeBean2.getTypeName()%></em> 
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>质量等级：</em>    
	      	<select name="qualityLevel" id="qualityLevel">
	      		<%for(StatusBean<String, String> statusBean : qualityLevelList){ %><option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus().equals(bean.getQualityLevel()))?"selected":"" %>><%=statusBean.getValue() %></option><%} %>
	      	</select>
	    </p>
	    <p>
	      <em class="int_label">有效状态：</em>
	      <input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
	    </p>
	    <p class="p_top_border">
	      <em class="int_label">备注：</em>
	      <textarea name="memo" id="memo" cols="60" rows="3" checkStr="备注;txt;false;;1000"><%=bean.getMemo() %></textarea>
	    </p>
	  </div>
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
					if(subPropertyTypeMap.get(typeBean.getPropertyCode()) != null ){ 
						out.print(PropertyDisplayUtil.displayPropertyInput(subPropertyTypeMap.get(typeBean.getPropertyCode()), productPropertyMap.get(typeBean.getPropertyCode()),true));
					} 
				}%>
			</p>
			<%} %>	
			<%} %>
			</div>
		<%} %>
	  </div>
	  <div class="div_center">
	  	<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  	<a href="ProductInfoList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
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