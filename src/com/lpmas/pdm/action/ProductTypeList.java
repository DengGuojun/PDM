package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class ProductTypeList
 */
@WebServlet("/pdm/ProductTypeList.do")
public class ProductTypeList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductTypeList() {
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
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		int parentTypeId = ParamKit.getIntParameter(request, "parentTypeId", 0);
		condMap.put("parentTypeId", String.valueOf(parentTypeId));
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (parentTypeId == 0 && !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.SEARCH)) {
			return;
		}
		if (parentTypeId > 0 && !adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.SEARCH)) {
			return;
		}
		String typeCode = ParamKit.getParameter(request, "typeCode", "").trim();
		if (StringKit.isValid(typeCode)) {
			condMap.put("typeCode", typeCode);
		}
		String typeName = ParamKit.getParameter(request, "typeName", "").trim();
		if (StringKit.isValid(typeName)) {
			condMap.put("typeName", typeName);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		ProductTypeBusiness business = new ProductTypeBusiness();
		PageResultBean<ProductTypeBean> result = business.getProductTypePageListByMap(condMap, pageBean);

		List<ProductTypeBean> parentTreeList = business.getParentTreeListByKey(parentTypeId);
		request.setAttribute("ParentTreeList", parentTreeList);
		request.setAttribute("ProductTypeList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("ParentTypeId", parentTypeId);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "ProductTypeList.jsp");
		rd.forward(request, response);
	}

}
