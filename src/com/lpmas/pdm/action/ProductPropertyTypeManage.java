package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductPropertyTypeBusiness;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

@WebServlet("/pdm/ProductPropertyTypeManage.do")
public class ProductPropertyTypeManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductPropertyTypeManage.class);
	private static final long serialVersionUID = 1L;

	public ProductPropertyTypeManage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int propertyId = ParamKit.getIntParameter(request, "propertyId", 0);
		int parentPropertyId = ParamKit.getIntParameter(request, "parentPropertyId", 0);
		ProductPropertyTypeBean bean = new ProductPropertyTypeBean();
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int typeId = 0;
		int parentTypeId = 0;
		List<ProductTypeBean> subProductTypeList = new ArrayList<ProductTypeBean>();
		ProductPropertyTypeBusiness business = new ProductPropertyTypeBusiness();
		if (propertyId > 0) {
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getProductPropertyTypeByKey(propertyId);
			parentPropertyId = bean.getParentPropertyId();
			//设置商品类型选择框
			typeId = bean.getTypeId();
			ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
			ProductTypeBean productTypeBean = productTypeBusiness.getProductTypeByKey(typeId);
			if(productTypeBean.getParentTypeId() == 0){
				parentTypeId = typeId;
				typeId = 0;
				request.setAttribute("ParentProductTypeBean", productTypeBean);
			}else{
				parentTypeId = productTypeBean.getParentTypeId();
				ProductTypeBean parentProductTypeBean = productTypeBusiness.getProductTypeByKey(parentTypeId);
				request.setAttribute("ParentProductTypeBean", parentProductTypeBean);
				request.setAttribute("ProductTypeBean", productTypeBean);
			}
			subProductTypeList = productTypeBusiness.getProductTypeListByParentId(parentTypeId);
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
			bean.setIsModifiable(Constants.STATUS_VALID);
		}
		if(parentPropertyId > 0){
			ProductPropertyTypeBean parentPropertyTypeBean = business.getProductPropertyTypeByKey(parentPropertyId);
			request.setAttribute("ParentPropertyTypeBean",parentPropertyTypeBean);
		}
		

		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		List<ProductTypeBean> productTypeList = productTypeBusiness.getProductTypeListByParentId(0);
		request.setAttribute("ProductTypeList", productTypeList);
		request.setAttribute("SubProductTypeList", subProductTypeList);
		request.setAttribute("TypeId", typeId);
		request.setAttribute("ParentTypeId", parentTypeId);
		request.setAttribute("ParentPropertyId", parentPropertyId);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("PropertyTypeBean", bean);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/ProductPropertyTypeManage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		ProductPropertyTypeBean propertyTypeBean = new ProductPropertyTypeBean();
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		try {
			propertyTypeBean = BeanKit.request2Bean(request, ProductPropertyTypeBean.class);
			//如果没有选择子级分类，则设置父级分类
			if(propertyTypeBean.getTypeId() == 0 ){
				int parentType = ParamKit.getIntParameter(request, "parentType", 0);
				propertyTypeBean.setTypeId(parentType);
			}
			ProductPropertyTypeBusiness business = new ProductPropertyTypeBusiness();
			int result = 0;
			int userId = adminUserHelper.getAdminUserId();

			ReturnMessageBean messageBean = business.verifyProductPropertyType(propertyTypeBean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (propertyTypeBean.getPropertyId() > 0) {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.UPDATE)) {
					return;
				}
				
				propertyTypeBean.setModifyUser(userId);
				result = business.updateProductPropertyType(propertyTypeBean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.CREATE)) {
					return;
				}
				propertyTypeBean.setCreateUser(userId);
				result = business.addProductPropertyType(propertyTypeBean);
			}
			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/ProductPropertyTypeList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
