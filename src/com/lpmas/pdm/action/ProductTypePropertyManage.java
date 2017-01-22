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

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.bean.ProductTypePropertyBean;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.business.ProductTypePropertyBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductConfig;
import com.lpmas.pdm.config.ProductTypeConfig;

@WebServlet("/pdm/ProductTypePropertyManage.do")
public class ProductTypePropertyManage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ProductTypePropertyManage() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_PROPERTY, OperationConfig.SEARCH)) {
			return;
		}
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		int parentTypeId = ParamKit.getIntParameter(request, "parentTypeId", 0);
		if (parentTypeId < 0) {
			HttpResponseKit.alertMessage(response, "参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		ProductTypePropertyBusiness productTypePropertyBusiness = new ProductTypePropertyBusiness();
		List<ProductTypePropertyBean> list = productTypePropertyBusiness.getProductTypePropertyListByTypeId(typeId);
		// 设置默认值，供用户参考填写
		List<ProductTypePropertyBean> defaultValuelist = new ArrayList<ProductTypePropertyBean>();
		ProductTypePropertyBean numberRuleBean = new ProductTypePropertyBean();
		numberRuleBean.setPropertyCode(ProductTypeConfig.NUMBER_RULE);
		numberRuleBean.setPropertyValue(ProductConfig.NUMBER_RULE_DEFAULT_VALUE);
		defaultValuelist.add(numberRuleBean);
		ProductTypePropertyBean itemNumberRuleBean = new ProductTypePropertyBean();
		itemNumberRuleBean.setPropertyCode(ProductTypeConfig.ITEM_NUMBER_RULE);
		itemNumberRuleBean.setPropertyValue(ProductConfig.ITEM_NUMBER_RULE_DEFAULT_VALUE);
		defaultValuelist.add(itemNumberRuleBean);
		
		
		request.setAttribute("ProductTypePropertyList", list);
		request.setAttribute("PropertyDefaultValueList", defaultValuelist);

		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		List<ProductTypeBean> parentTreeList = productTypeBusiness.getParentTreeListByKey(parentTypeId);
		request.setAttribute("ParentTreeList", parentTreeList);

		ProductTypeBean bean = new ProductTypeBean();
		bean = productTypeBusiness.getProductTypeByKey(typeId);
		request.setAttribute("ProductTypeBean", bean);

		request.setAttribute("TypeId", typeId);
		request.setAttribute("ParentTypeId", parentTypeId);
		request.setAttribute("ProductTypePropertyFieldList", ProductTypeConfig.PRODUCT_TYPE_PROPERTY_FIELD_LIST);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "ProductTypePropertyManage.jsp");
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		ProductTypePropertyBusiness business = new ProductTypePropertyBusiness();
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		List<StatusBean<String, String>> productTypeConfigStatuslist = ProductTypeConfig.PRODUCT_TYPE_PROPERTY_FIELD_LIST;
		for (StatusBean<String, String> statusBean : productTypeConfigStatuslist) {
			int result = 0;
			String propertyCode = statusBean.getStatus();
			ProductTypePropertyBean bean = new ProductTypePropertyBean();
			bean.setTypeId(typeId);
			bean.setPropertyCode(propertyCode);
			bean.setPropertyValue(ParamKit.getParameter(request, propertyCode, ""));

			if (business.getProductTypePropertyByKey(typeId, propertyCode) != null) {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_PROPERTY, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateProductTypeProperty(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_PROPERTY, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addProductTypeProperty(bean);
			}
			if (result == -1) {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		}
		HttpResponseKit.alertMessage(response, "处理成功", HttpResponseKit.ACTION_HISTORY_BACK);
	}
}
