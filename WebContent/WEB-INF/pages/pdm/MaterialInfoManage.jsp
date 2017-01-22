<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@page import="com.lpmas.pdm.config.MaterialConfig"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.bean.StatusBean"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.pdm.bean.*"%>
<%@ page import="com.lpmas.pdm.business.*"%>
<%@ page import="com.lpmas.pdm.config.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%
	MaterialInfoBean bean = (MaterialInfoBean) request.getAttribute("marterialinfoBean");
	MaterialTypeBean typeBean = (MaterialTypeBean) request.getAttribute("typeBean");
	UnitInfoBean unitInfoBean = (UnitInfoBean)request.getAttribute("UnitInfoBean");
	AdminUserHelper adminHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	int materialId = bean.getMaterialId();
	List<MaterialPropertyTypeBean> materialPropertyTypeList = (List<MaterialPropertyTypeBean>) request
			.getAttribute("propertyTypeList");
	List<MaterialPropertyBean> materialPropertyList = (List<MaterialPropertyBean>) request
			.getAttribute("propertyList");
	Map<String, MaterialPropertyBean> materialPropertyMap = (Map<String, MaterialPropertyBean>) request
			.getAttribute("propertyMap");
	List<StatusBean<String, String>> useStatusList = (List<StatusBean<String, String>>) request
			.getAttribute("MaterialUseStatusList");
	Map<String, MaterialPropertyTypeBean> subPropertyTypeMap = (Map<String, MaterialPropertyTypeBean>) request
			.getAttribute("subPropertyTypeMap");
	String readOnly = ParamKit.getParameter(request, "readOnly", "false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<%@ include file="../include/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物料管理</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
     document.domain='<%=DOMAIN%>'; 
</script>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a>
		<ul class="art_nav">
			<li><a href="MaterialInfoList.do">物料列表</a>&nbsp;>&nbsp;</li>
			<%if (materialId > 0) {%>
			<li><%=bean.getMaterialName()%>&nbsp;>&nbsp;</li>
			<li>修改物料信息</li>
			<%} else {%>
			<li>新建物料信息<li>
			<%}%>
		</ul>
	</div>
	<%
		if (bean.getMaterialId() > 0) {
	%>
	<div class="article_tit">
		<p class="tab">
			<a
				href="MaterialInfoManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=<%=readOnly%>">基本信息</a>
			<a
				href="MaterialInfoStorageManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=<%=readOnly%>">仓储信息</a>
			<a
				href="MaterialInfoImageManage.do?materialId=<%=bean.getMaterialId()%>&readOnly=<%=readOnly%>">物料图片</a>
		</p>
		<script>
			tabChange('.tab', 'a');
		</script>
	</div>
	<%
		}
	%>
	<form id="formData" name="formData" method="post" action="MaterialInfoManage.do" onsubmit="javascript:return formOnSubmit();">
		<input type="hidden" name="materialId" id="materialId" value="<%=bean.getMaterialId()%>" /> 
		<input type="hidden" name="typeId1" id="typeId1" value="<%=bean.getTypeId1()%>" /> 
		<input type="hidden" name="materialNumber" id="materialNumber" value="<%=bean.getMaterialNumber()%>" />
		<div class="modify_form">
			<p>
				<em class="int_label"><span>*</span>物料名称：</em> <input type="text"
					name="materialName" id="materialName" size="50" maxlength="100"
					value="<%=bean.getMaterialName()%>" checkStr="物料名称;txt;true;;100" />
			</p>
			<p>
				<em class="int_label">物料编码：</em> <input type="text"
					name="materialNumber" id="materialNumber" size="50" maxlength="50"
					readonly value="<%=bean.getMaterialNumber()%>" /><em>（系统根据物料类型属性规则自动生成）</em>
			</p>
			<p>
				<em class="int_label">物料类型：</em> <em><%=typeBean.getTypeName()%></em>
			</p>
	    		<p>
	      	<em class="int_label"><span>*</span>型号规格：</em>    
	      	<input type="text" name="specification" id="specification" size="50" maxlength="50" value="<%=bean.getSpecification()%>" checkStr="规格;txt;true;;100"/>
	      	<em ><span>*</span>计量单位：</em>    
	      	<input type="hidden" name="unit" id="unit" value="<%=bean.getUnit() %>" />
	      	<input type="text" name="unitName" id="unitName" value="<%=unitInfoBean.getUnitName() %>" readOnly checkStr="计量单位;txt;true;;100" />
	    		</p>
			<p>
				<em class="int_label">净重：</em> <input type="text" name="netWeight"
					id="netWeight" size="50" maxlength="50"
					value="<%=bean.getNetWeight()%>" checkStr="净重;digit;false;;100" onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")'/>KG
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
				<em class="int_label"><span>*</span>使用状态：</em> <select name="useStatus"
					id="useStatus">
					<%
						for (StatusBean<String, String> statusBean : useStatusList) {
					%><option value="<%=statusBean.getStatus()%>"
						<%=(statusBean.getStatus().equals(bean.getUseStatus())) ? "selected" : ""%>><%=statusBean.getValue()%></option>
					<%
						}
					%>
				</select>
			</p>
			<p>
				<em class="int_label">有效状态：</em> <input type="checkbox" name="status"
					id="status" value="<%=Constants.STATUS_VALID%>"
					<%=(bean.getStatus() == Constants.STATUS_VALID) ? "checked" : ""%> />
			</p>
			<p class="p_top_border">
				<em class="int_label">备注：</em>
				<textarea name="memo" id="memo" cols="60" rows="3"
					checkStr="备注;txt;false;;1000"><%=bean.getMemo()%></textarea>
			</p>
		</div>
		<div>
			<%
				if (materialPropertyTypeList.size() > 0) {
			%>
			<div class="modify_form">
				<%
					for (MaterialPropertyTypeBean materialPropertyTypeBean : materialPropertyTypeList) {
							if (materialPropertyTypeBean.getParentPropertyId() == 0) {
				%>
				<p>
					<%if(materialPropertyTypeBean.getIsRequired()==Constants.STATUS_VALID){ %>
						<em class="int_label"><span>*</span><%=materialPropertyTypeBean.getPropertyName()%>
						:
					</em>
					<%}else{ %>
					<em class="int_label"> <%=materialPropertyTypeBean.getPropertyName()%>
						:
					</em>
					<%} %>
					
					<%
						out.print(PropertyDisplayUtil.displayPropertyInput(materialPropertyTypeBean,
											materialPropertyMap.get(materialPropertyTypeBean.getPropertyCode()), false));
									if (subPropertyTypeMap.get(materialPropertyTypeBean.getPropertyCode()) != null) {
										out.print(PropertyDisplayUtil.displayPropertyInput(
												subPropertyTypeMap.get(materialPropertyTypeBean.getPropertyCode()),
												materialPropertyMap.get(materialPropertyTypeBean.getPropertyCode()), true));
									}
					%>
				</p>
				<%
					}
				%>


				<%
					}
				%>
			</div>
			<%
				}
			%>
		</div>
		<div class="div_center">
			<input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" /> 
			<a href="MaterialInfoList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
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
		var readonly = "${readOnly}";
		if (readonly == 'true') {
			disablePageElement();
		}
	});
	function selectUnitInfo(unitCode, unitName) {
		$("#unit").val(unitCode);
		$("#unitName").val(unitName);
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