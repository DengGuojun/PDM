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
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.business.UnitTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class UnitTypeManage
 */
@WebServlet("/pdm/UnitTypeManage.do")
public class UnitTypeManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(UnitTypeManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnitTypeManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		UnitTypeBean bean = new UnitTypeBean();
		UnitTypeBusiness business = new UnitTypeBusiness();
		if (typeId > 0) {
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.UNIT_TYPE, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.UNIT_TYPE, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getUnitTypeByKey(typeId);
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.UNIT_TYPE, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
		}
		request.setAttribute("UnitTypeBean", bean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = this.getServletContext()
				.getRequestDispatcher(PdmConfig.PAGE_PATH + "UnitTypeManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);

		UnitTypeBean bean = new UnitTypeBean();
		try {
			bean = BeanKit.request2Bean(request, UnitTypeBean.class);
			UnitTypeBusiness business = new UnitTypeBusiness();
			ReturnMessageBean messageBean = business.verifyUnitTypeProperty(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			if (bean.getTypeId() > 0) {
				if (!adminUserHelper.checkPermission(PdmResource.UNIT_TYPE, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateUnitType(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.UNIT_TYPE, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addUnitType(bean);
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/UnitTypeList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
