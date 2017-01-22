package com.lpmas.pdm.action;

import java.io.IOException;

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
import com.lpmas.pdm.bean.ProductPropertyOptionBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.business.ProductPropertyOptionBusiness;
import com.lpmas.pdm.business.ProductPropertyTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

@WebServlet("/pdm/ProductPropertyOptionManage.do")
public class ProductPropertyOptionManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductPropertyOptionManage.class);
	private static final long serialVersionUID = 1L;

	public ProductPropertyOptionManage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int propertyOptionId = ParamKit.getIntParameter(request, "propertyOptionId", 0);
		ProductPropertyOptionBean propertyOptionBean = new ProductPropertyOptionBean();
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly
				&& !adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.UPDATE)) {
			return;
		}
		if (propertyOptionId > 0) {
			ProductPropertyOptionBusiness business = new ProductPropertyOptionBusiness();
			propertyOptionBean = business.getProductPropertyOptionByKey(propertyOptionId);
		} else {
			propertyOptionBean.setStatus(Constants.STATUS_VALID);
		}

		int propertyId = ParamKit.getIntParameter(request, "propertyId", 0);
		if (propertyId <= 0) {
			propertyId = propertyOptionBean.getPropertyId();
		}
		request.setAttribute("PropertyId", propertyId);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		ProductPropertyTypeBusiness typeBusiness = new ProductPropertyTypeBusiness();
		ProductPropertyTypeBean typeBean = typeBusiness.getProductPropertyTypeByKey(propertyId);
		request.setAttribute("PropertyTypeBean", typeBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("propertyOptionBean", propertyOptionBean);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/ProductPropertyOptionManage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int userId = adminUserHelper.getAdminUserId();
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.UPDATE)) {
			return;
		}
		ProductPropertyOptionBean optionBean = new ProductPropertyOptionBean();
		try {
			optionBean = BeanKit.request2Bean(request, ProductPropertyOptionBean.class);
			ProductPropertyOptionBusiness business = new ProductPropertyOptionBusiness();
			ReturnMessageBean messageBean = business.verifyProductPropertyOption(optionBean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			if (optionBean.getOptionId() > 0) {
				optionBean.setModifyUser(userId);
				result = business.updateProductPropertyOption(optionBean);
			} else {
				optionBean.setCreateUser(userId);
				result = business.addProductPropertyOption(optionBean);
			}
			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/ProductPropertyOptionList.do?propertyId="
						+ optionBean.getPropertyId());
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
