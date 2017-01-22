package com.lpmas.pdm.action;

import java.io.IOException;
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
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductTypeConfig;

/**
 * Servlet implementation class ProductTypeManage
 */
@WebServlet("/pdm/ProductTypeManage.do")
public class ProductTypeManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductTypeManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductTypeManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		int parentTypeId = ParamKit.getIntParameter(request, "parentTypeId", 0);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		ProductTypeBean bean = new ProductTypeBean();
		ProductTypeBusiness business = new ProductTypeBusiness();
		if (parentTypeId < 0) {
			HttpResponseKit.alertMessage(response, "参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		if (typeId > 0) {
			if (!readOnly && parentTypeId == 0
					&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && parentTypeId == 0
					&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.SEARCH)) {
				return;
			}
			if (!readOnly && parentTypeId > 0
					&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && parentTypeId > 0
					&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getProductTypeByKey(typeId);
		} else {
			if (parentTypeId == 0
					&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.CREATE)) {
				return;
			}
			if (parentTypeId > 0
					&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
		}

		List<ProductTypeBean> parentTreeList = business.getParentTreeListByKey(parentTypeId);
		request.setAttribute("ParentTreeList", parentTreeList);

		List<StatusBean<String, String>> productTypeConfigStatusList = ProductTypeConfig.PRODUCT_TYPE_PROPERTY_FIELD_LIST;
		request.setAttribute("ProductTypeConfigStatuslist", productTypeConfigStatusList);

		request.setAttribute("ProductTypeBean", bean);
		request.setAttribute("ParentTypeId", parentTypeId);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(
				PdmConfig.PAGE_PATH + "ProductTypeManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);

		ProductTypeBean bean = new ProductTypeBean();
		try {
			bean = BeanKit.request2Bean(request, ProductTypeBean.class);
			ProductTypeBusiness business = new ProductTypeBusiness();

			ReturnMessageBean messageBean = business.verifyProductTypeProperty(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			if (bean.getTypeId() > 0) {
				if (bean.getParentTypeId() == 0
						&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.UPDATE)) {
					return;
				}
				if (bean.getParentTypeId() > 0
						&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateProductType(bean);
			} else {
				if (bean.getParentTypeId() == 0
						&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.CREATE)) {
					return;
				}
				if (bean.getParentTypeId() > 0
						&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addProductType(bean);
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/pdm/ProductTypeList.do?parentTypeId=" + bean.getParentTypeId());
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
