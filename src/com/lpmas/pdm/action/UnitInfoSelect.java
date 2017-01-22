package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.business.UnitInfoBusiness;
import com.lpmas.pdm.business.UnitTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class UnitInfoSelect
 */
@WebServlet("/pdm/UnitInfoSelect.do")
public class UnitInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnitInfoSelect() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.UNIT_TYPE, OperationConfig.SEARCH)) {
			return;
		}
		if (!adminUserHelper.checkPermission(PdmResource.UNIT_INFO, OperationConfig.SEARCH)) {
			return;
		}
		UnitTypeBusiness unitTypeBusiness = new UnitTypeBusiness();
		UnitInfoBusiness unitInfoBusiness = new UnitInfoBusiness();
		String unitCode = ParamKit.getParameter(request, "unitCode", "");
		int unitTypeId = 0;
		if (StringKit.isValid(unitCode)) {
			UnitInfoBean unitInfoBean = unitInfoBusiness.getUnitInfoByCode(unitCode);
			unitTypeId = unitInfoBean.getTypeId();
		}
		List<UnitTypeBean> unitTypeList = unitTypeBusiness.getUnitTypeAllList();
		// 检查这些TYPE里面有没有单位,没有就去掉这个类型不显示
		Iterator<UnitTypeBean> iterator = unitTypeList.iterator();
		while (iterator.hasNext()) {
			UnitTypeBean typeBean = iterator.next();
			List<UnitInfoBean> unitInfoList = unitInfoBusiness.getUnitInfoByTypeId(typeBean.getTypeId());
			if (unitInfoList == null || unitInfoList.isEmpty()) {
				iterator.remove();
			}
		}
		request.setAttribute("UnitTypeList", unitTypeList);
		request.setAttribute("UnitTypeId", unitTypeId);
		request.setAttribute("UnitCode", unitCode);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "UnitInfoSelect.jsp");
		rd.forward(request, response);
	}

}
