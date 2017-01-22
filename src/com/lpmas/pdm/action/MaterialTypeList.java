package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

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
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialTypeList
 */
@WebServlet("/pdm/MaterialTypeList.do")
public class MaterialTypeList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialTypeList() {
		super();
		// TODO Auto-generated constructor stub
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
		if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE, OperationConfig.SEARCH)) {
			return;
		}
		// 分页信息
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		// 查询条件参数
		HashMap<String, String> condMap = new HashMap<String, String>();
		String typeCode = ParamKit.getParameter(request, "typeCode", "").trim();
		if (StringKit.isValid(typeCode)) {
			condMap.put("typeCode", typeCode);
		}
		String typeName = ParamKit.getParameter(request, "typeName", "").trim();
		if (StringKit.isValid(typeName)) {
			condMap.put("typeName", typeName);
		}
		int parentTypeId = ParamKit.getIntParameter(request, "parentTypeId", 0);
		condMap.put("parentTypeId", String.valueOf(parentTypeId));
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		// 初始化业务类
		MaterialTypeBusiness business = new MaterialTypeBusiness();
		PageResultBean<MaterialTypeBean> result = business.getMaterialTypePageListByMap(condMap, pageBean);

		// 获得以此类型ID为孩子的，从父到子的链表
		LinkedList<MaterialTypeBean> parentTreeList = business.getParentTreeListByKey(parentTypeId);
		request.setAttribute("ParentTreeList", parentTreeList);

		// 页面数据绑定
		request.setAttribute("MaterialTypeList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("parentTypeId", parentTypeId);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 页面转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "MaterialTypeList.jsp");
		rd.forward(request, response);

	}

}
