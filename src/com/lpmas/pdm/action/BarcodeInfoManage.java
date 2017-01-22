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
import com.lpmas.pdm.bean.BarcodeInfoBean;
import com.lpmas.pdm.business.BarcodeInfoBusiness;
import com.lpmas.pdm.config.BarcodeConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

@WebServlet("/pdm/BarcodeInfoManage.do")
public class BarcodeInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(UnitTypeManage.class);
	private static final long serialVersionUID = 1L;

	public BarcodeInfoManage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int barcodeId = ParamKit.getIntParameter(request, "barcodeId", 0);
		BarcodeInfoBean bean = new BarcodeInfoBean();
		BarcodeInfoBusiness business = new BarcodeInfoBusiness();
		if (barcodeId > 0) {
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.BARCODE_INFO, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.BARCODE_INFO, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getBarcodeInfoByKey(barcodeId);
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.BARCODE_INFO, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
		}
		// 获取使用状态列表
		List<StatusBean<String, String>> barcodeUseStatusList = BarcodeConfig.BARCODE_USE_STATUS_LIST;

		request.setAttribute("barcodeUseStatusList", barcodeUseStatusList);
		request.setAttribute("BarcodeInfo", bean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = this.getServletContext()
				.getRequestDispatcher(PdmConfig.PAGE_PATH + "BarcodeInfoManage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		BarcodeInfoBean bean = new BarcodeInfoBean();
		try {
			bean = BeanKit.request2Bean(request, BarcodeInfoBean.class);
			BarcodeInfoBusiness business = new BarcodeInfoBusiness();
			ReturnMessageBean messageBean = business.verifyBarcodeProperty(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			if (bean.getBarcodeId() > 0) {
				if (!adminUserHelper.checkPermission(PdmResource.BARCODE_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateBarcodeInfo(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.BARCODE_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addBarcodeInfo(bean);
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/BarcodeInfoList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
