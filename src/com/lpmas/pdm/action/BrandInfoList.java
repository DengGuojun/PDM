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
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.BrandInfoBean;
import com.lpmas.pdm.business.BrandInfoBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class BrandInfoList
 */
@WebServlet("/pdm/BrandInfoList.do")
public class BrandInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BrandInfoList() {
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
		if (!adminUserHelper.checkPermission(PdmResource.BRAND_INFO, OperationConfig.SEARCH)) {
			return;
		}
		
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		String brandCode = ParamKit.getParameter(request, "brandCode", "").trim();
		if (StringKit.isValid(brandCode)) {
			condMap.put("brandCode", brandCode);
		}
		String brandName = ParamKit.getParameter(request, "brandName", "").trim();
		if (StringKit.isValid(brandName)) {
			condMap.put("brandName", brandName);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		BrandInfoBusiness business = new BrandInfoBusiness();
		PageResultBean<BrandInfoBean> result = business.getBrandInfoPageListByMap(condMap, pageBean);
		request.setAttribute("BrandList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "BrandInfoList.jsp");
		rd.forward(request, response);
	}

}
