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
import com.lpmas.pdm.bean.BrandInfoBean;
import com.lpmas.pdm.business.BrandInfoBusiness;
import com.lpmas.pdm.config.BrandConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class BrandInfoManage
 */
@WebServlet("/pdm/BrandInfoManage.do")
public class BrandInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(BrandInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BrandInfoManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		List<StatusBean<String, String>> brandConfigStatusList = BrandConfig.BRAND_PROPERTY_FIELD_LIST;
		int brandId = ParamKit.getIntParameter(request, "brandId", 0);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		BrandInfoBean bean = new BrandInfoBean();
		if (brandId > 0) {
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.SEARCH)){
				return;
			}
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.UPDATE)) {
				return;
			}
			BrandInfoBusiness business = new BrandInfoBusiness();
			bean = business.getBrandInfoByKey(brandId);
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
		}
		request.setAttribute("BrandInfo", bean);
		request.setAttribute("BrandConfigStatusList", brandConfigStatusList);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(PdmConfig.PAGE_PATH + "BrandInfoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		BrandInfoBean bean = new BrandInfoBean();
		try {
			bean = BeanKit.request2Bean(request, BrandInfoBean.class);
			BrandInfoBusiness business = new BrandInfoBusiness();
			
			ReturnMessageBean messageBean = business.verifyBrandInfoProperty(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			
			int result = 0;
			if (bean.getBrandId() > 0) {
				if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateBrandInfo(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addBrandInfo(bean);
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/BrandInfoList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
