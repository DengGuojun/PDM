package com.lpmas.pdm.action;

import java.io.IOException;
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
import com.lpmas.pdm.bean.BrandPropertyBean;
import com.lpmas.pdm.business.BrandPropertyBusiness;
import com.lpmas.pdm.config.BrandConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

@WebServlet("/pdm/BrandPropertyManage.do")
public class BrandPropertyManage extends HttpServlet {

	private static final long serialVersionUID = 7777587537852776797L;

	public BrandPropertyManage() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int brandId = ParamKit.getIntParameter(request, "brandId", 0);
		if (brandId > 0) {
			if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.UPDATE)) {
				return;
			}
			
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.CREATE)) {
				return;
			}
		}
		BrandPropertyBusiness business = new BrandPropertyBusiness();
		List<BrandPropertyBean> list = business.getBrandPropertyListByBrandId(brandId);
		
		request.setAttribute("BrandId", brandId);
		request.setAttribute("BrandPropertyList", list);
		request.setAttribute("BrandPropertyFieldList", BrandConfig.BRAND_PROPERTY_FIELD_LIST);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "BrandPropertyManage.jsp");
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		BrandPropertyBusiness business = new BrandPropertyBusiness();
		int brandId = ParamKit.getIntParameter(request, "brandId", 0);
		if (brandId > 0) {		
			if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.UPDATE)) {
				return;
			}
			
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.CREATE)) {
				return;
			}
		}
		List<StatusBean<String, String>> brandConfigStatuslist = BrandConfig.BRAND_PROPERTY_FIELD_LIST;
		for (StatusBean<String, String> statusBean : brandConfigStatuslist) {
			int result = 0;
			String propertyCode = statusBean.getStatus();
			BrandPropertyBean bean = new BrandPropertyBean();
			bean.setBrandId(brandId);
			bean.setPropertyCode(propertyCode);
			bean.setPropertyValue(ParamKit.getParameter(request, propertyCode, ""));

			if (business.getBrandPropertyByKey(brandId, propertyCode) != null) {
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateBrandProperty(bean);
			} else {
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addBrandProperty(bean);
			}
			if (result == -1) {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		}
		HttpResponseKit.alertMessage(response, "处理成功", HttpResponseKit.ACTION_HISTORY_BACK);
	}
}
