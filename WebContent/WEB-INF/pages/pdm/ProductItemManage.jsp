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
	ProductInfoBean infoBean = (ProductInfoBean)request.getAttribute("ProductInfo");
	UnitInfoBean unitInfoBean = (UnitInfoBean)request.getAttribute("UnitInfoBean");
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<AdminGroupInfoBean> groupList = adminHelper.getUserGroupList();
	List<UnitTypeBean> unitTypeList = (List<UnitTypeBean>) request.getAttribute("UnitTypeList");
	List<StatusBean<String, String>> useStatusList = (List<StatusBean<String, String>>)request.getAttribute("ProductUseStatusList");
	int itemId = bean.getItemId();
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
	<title>商品项管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
	<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
	<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
	<script type="text/javascript" src="<%=STATIC_URL %>/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
	</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="ProductItemList.do">商品项列表</a>&nbsp;>&nbsp;</li>
			<% if(itemId > 0) {%>
			<li><%=bean.getItemName()%>&nbsp;>&nbsp;</li>
			<li>修改商品项信息</li>
			<%} else { %>
			<li>新建商品项信息</li>
			<%} %>
		</ul>
	</div>
	<%if(itemId > 0){%>
	<%@ include file="../nav/ProductItemNav.jsp" %>
	<%} %>
	<form id="formData" name="formData" method="post" action="ProductItemManage.do" onsubmit="javascript:return formOnSubmit();">
	  <input type="hidden" name="itemId" id="itemId" value="<%=bean.getItemId() %>"/>
	  <input type="hidden" name="itemNumber" id="itemNumber" value="<%=bean.getItemNumber()%>"/>
	  <input type="hidden" name="productId" id="productId" value="<%=bean.getProductId()%>"/>
	  <div class="modify_form">
	  	<p>
	      <em class="int_label">商品名称：</em>
	      <input type="text" name="productName" id="productName" size="50" maxlength="50" readonly value="<%=infoBean.getProductName() %>" />
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>商品项名称：</em>
	      <input type="text" name="itemName" id="itemName" size="50" maxlength="100" value="<%=bean.getItemName() %>" checkStr="商品项名称;txt;true;;100"/>
	    </p>
	    <p>
	      <em class="int_label">商品项编码：</em>
	      <input type="text" name="itemNumber" id="itemNumber" size="50" maxlength="50" readonly value="<%=bean.getItemNumber() %>" /><em>（系统根据商品类型属性规则自动生成）</em>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>型号规格编码：</em>    
	      <input type="text" name="specification" id="specification" size="20" maxlength="10" value="<%=bean.getSpecification()%>" checkStr="规格;txt;true;;100"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>型号规格描述：</em>    
	      <input type="text" name="specificationDesc" id="specificationDesc" size="50" maxlength="50" value="<%=bean.getSpecificationDesc()%>" checkStr="规格描述;txt;true;;100"/>
	      <em><span>*</span>销售计量单位：</em>    
	      <input type="text" name="unitName" id="unitName" value="<%=bean.getUnit()%>"  readOnly  checkStr="计量单位;txt;true;;100"/>
	       <input type="hidden" name="unit" id="unit" value="<%=bean.getUnit()%>"   checkStr="计量单位;txt;true;;100"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>净重：</em>    
	      	<input type="text" name="netWeight" id="netWeight" size="20" maxlength="50" value="<%=bean.getNetWeight()%>" checkStr="净重;num;true;;100" onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")'>KG
	    </p>
	    <p>
	  		<em class="int_label"><span>*</span>条形码：</em>
	  		<input type="text" name="barcode" id="barcode" value="<%=bean.getBarcode() %>"   size="50" checkStr="条形码;code;true;;50"/>	      
	  	</p>
	  	<p>
	      <em class="int_label"><span>*</span>是否保质期管理：</em>
	      <select name="hasGuaranteePeriod" id="hasGuaranteePeriod" onchange="checkGuaranteePeriod()">
	      		<option value="<%=Constants.STATUS_VALID%>" <%=bean.getGuaranteePeriod() >0 ? "selected" : ""%>>是</option>
	      		<option value="<%=Constants.STATUS_NOT_VALID%>" <%=bean.getGuaranteePeriod() <=0 ? "selected" : ""%>>否</option>
	      </select>
	      <span id="guaranteePeriodInput">
	      保质期(天):
	      <input type="text" name="guaranteePeriod" id="guaranteePeriod" size="20" maxlength="20" value="<%=(int)bean.getGuaranteePeriod()%>" checkStr="保质期;num;false;;100" onkeyup='this.value=this.value.replace(/[^0-9]/g,"")'>
	      </span>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>是否可销售：</em>
	      <select name="useStatus" id="useStatus">
	      		<%for(StatusBean<String, String> statusBean : useStatusList){ %><option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus().equals(bean.getUseStatus()))?"selected":"" %>><%=statusBean.getValue() %></option><%} %>
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
	  	<a href="ProductItemList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	  </div>
	</form>
</body>
<script type="text/javascript">
var unit = '${UnitInfoBean.unitId}';
$(document).ready(function() {
	$("#unitName").click(
		function() {
			unit = $("#unit").val();
			$.fancybox.open({
				href : 'UnitInfoSelect.do?callbackFun=selectUnitInfo&unitCode='+unit,
				type : 'iframe',
				width : 560,
				minHeight : 150
		});
	});
	checkGuaranteePeriod();
	var readonly = '${readOnly}';
	if(readonly=='true') {
		disablePageElement();
	};
});
function selectUnitInfo(unitCode, unitName) {
	$("#unit").val(unitCode);
	$("#unitName").val(unitName)
}

function checkGuaranteePeriod(){
	var hasGuaranteePeriod = $("#hasGuaranteePeriod").val();
	if(hasGuaranteePeriod == "1"){
		$("#guaranteePeriodInput").show();
		//如果之前没有保质期为空，则保质期管理文本框初始化为空值
		if(<%=bean.getGuaranteePeriod() <= 0%>){
			$("#guaranteePeriod").val("");
		}
	}else{
		$("#guaranteePeriodInput").hide();
	}
}

function formOnSubmit(){
	if(checkForm('formData')){
		var hasGuaranteePeriod = $("#hasGuaranteePeriod").val();
		if(hasGuaranteePeriod == "1"){
			var guaranteePeriod = $("#guaranteePeriod").val();
			if(guaranteePeriod == ""){
				alert("保质期必须填写");
				return false;
			}
			if(guaranteePeriod == "0"){
				alert("保质期必须大于0");
				return false;
			}
		}else{
			//如果没有保质期管理，保质期字段设为0
			$("#guaranteePeriod").val("-1");
		}
		return true;
	}else{
		return false;
	}
}
</script>
</html>