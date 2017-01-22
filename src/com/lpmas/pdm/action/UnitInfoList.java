package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.business.UnitInfoBusiness;
import com.lpmas.pdm.business.UnitTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class UnitInfoList
 */
@WebServlet("/pdm/UnitInfoList.do")
public class UnitInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnitInfoList() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.UNIT_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		if(typeId == 0){
			HttpResponseKit.alertMessage(response, "参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("typeId", String.valueOf(typeId));
		String unitCode = ParamKit.getParameter(request, "unitCode", "").trim();
		if (StringKit.isValid(unitCode)) {
			condMap.put("unitCode", unitCode);
		}
		String unitName = ParamKit.getParameter(request, "unitName", "").trim();
		if (StringKit.isValid(unitName)) {
			condMap.put("unitName", unitName);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}
		
		UnitTypeBusiness typeBusiness = new UnitTypeBusiness();
		UnitTypeBean typeBean = typeBusiness.getUnitTypeByKey(typeId);
		request.setAttribute("UnitTypeBean", typeBean);
		request.setAttribute("TypeId", typeId);
		UnitInfoBusiness infoBusiness = new UnitInfoBusiness();
		PageResultBean<UnitInfoBean> result = infoBusiness.getUnitInfoPageListByMap(condMap, pageBean);
		request.setAttribute("UnitInfoList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "UnitInfoList.jsp");
		rd.forward(request, response);
	}

}
