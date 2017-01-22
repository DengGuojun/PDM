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
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductDescriptionTypeBean;
import com.lpmas.pdm.business.ProductDescriptionTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;

/**
 * Servlet implementation class ProductDescriptionTypeManage
 */
@WebServlet("/pdm/ProductDescriptionTypeManage.do")
public class ProductDescriptionTypeManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductDescriptionTypeManage.class);
	private static final long serialVersionUID = 1L;

	public ProductDescriptionTypeManage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		ProductDescriptionTypeBean bean = new ProductDescriptionTypeBean();
		
		if (typeId > 0) {
			ProductDescriptionTypeBusiness business = new ProductDescriptionTypeBusiness();
			bean = business.getProductDescriptionTypeByKey(typeId);
		} else {
			bean.setStatus(Constants.STATUS_VALID);
		}
		request.setAttribute("ProductDescriptionTypeBean", bean);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/ProductDescriptionTypeManage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		ProductDescriptionTypeBean bean = new ProductDescriptionTypeBean();
		AdminUserHelper adminHelper = new AdminUserHelper(request, response);
		try {
			bean = BeanKit.request2Bean(request, ProductDescriptionTypeBean.class);
			ProductDescriptionTypeBusiness business = new ProductDescriptionTypeBusiness();

			int result = 0;
			int userId = adminHelper.getAdminUserId();
			ReturnMessageBean messageBean = business.verifyProductDescriptionType(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			if (bean.getTypeId() > 0) {
				bean.setModifyUser(userId);
				result = business.updateProductDescriptionType(bean);
			} else {
				bean.setCreateUser(userId);
				result = business.addProductDescriptionType(bean);
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/ProductDescriptionTypeList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
