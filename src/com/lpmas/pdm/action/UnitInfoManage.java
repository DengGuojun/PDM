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
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.business.UnitInfoBusiness;
import com.lpmas.pdm.business.UnitTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class UnitInfoManage
 */
@WebServlet("/pdm/UnitInfoManage.do")
public class UnitInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(UnitInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnitInfoManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int unitId = ParamKit.getIntParameter(request, "unitId", 0);
		UnitInfoBean bean = new UnitInfoBean();
		UnitInfoBusiness business = new UnitInfoBusiness();
		if (unitId > 0) {
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.UNIT_INFO, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.UNIT_INFO, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getUnitInfoByKey(unitId);
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.UNIT_INFO, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
		}

		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		if (typeId <= 0) {
			typeId = bean.getTypeId();
		}
		if (typeId == 0) {
			HttpResponseKit.alertMessage(response, "参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		UnitTypeBusiness typeBusiness = new UnitTypeBusiness();
		UnitTypeBean typeBean = typeBusiness.getUnitTypeByKey(typeId);
		request.setAttribute("TypeId", typeId);
		request.setAttribute("UnitInfoBean", bean);
		request.setAttribute("UnitTypeBean", typeBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = this.getServletContext()
				.getRequestDispatcher(PdmConfig.PAGE_PATH + "UnitInfoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);

		UnitInfoBean bean = new UnitInfoBean();
		try {
			bean = BeanKit.request2Bean(request, UnitInfoBean.class);
			UnitInfoBusiness business = new UnitInfoBusiness();
			ReturnMessageBean messageBean = business.verifyUnitInfoProperty(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			if (bean.getUnitId() > 0) {
				if (!adminUserHelper.checkPermission(PdmResource.UNIT_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateUnitInfo(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.UNIT_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addUnitInfo(bean);
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/UnitInfoList.do?typeId=" + bean.getTypeId());
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
