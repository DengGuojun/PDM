package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.business.MaterialPropertyTypeBusiness;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialPropertyTypeList
 */
@WebServlet("/pdm/MaterialPropertyTypeList.do")
public class MaterialPropertyTypeList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialPropertyTypeList() {
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
		if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.SEARCH)) {
			return;
		}
		// 接收分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		// 处理查询条件
		HashMap<String, String> condMap = new HashMap<String, String>();
		int parentPropertyId = ParamKit.getIntParameter(request, "parentPropertyId", 0);
		condMap.put("parentPropertyId", String.valueOf(parentPropertyId));
		String propertyCode = ParamKit.getParameter(request, "propertyCode", "").trim();
		if (StringKit.isValid(propertyCode)) {
			condMap.put("propertyCode", propertyCode);
		}
		String propertyName = ParamKit.getParameter(request, "propertyName", "").trim();
		if (StringKit.isValid(propertyName)) {
			condMap.put("propertyName", propertyName);
		}
		String propertyType = ParamKit.getParameter(request, "propertyType", "").trim();
		if (StringKit.isValid(propertyType)) {
			condMap.put("propertyType", propertyType);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}
		String materialType = ParamKit.getParameter(request, "materialType", "").trim();
		if (StringKit.isValid(materialType)) {
			condMap.put("materialType", materialType);
		}
		// 查出结果
		MaterialPropertyTypeBusiness business = new MaterialPropertyTypeBusiness();
		PageResultBean<MaterialPropertyTypeBean> result = business.getMaterialPropertyTypePageListByMap(condMap,
				pageBean);

		// 查出物料类型并转化成MAP
		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		List<MaterialTypeBean> materialTypeList = materialTypeBusiness.getMaterialTypeAllList();
		Map<Integer, String> materialTypeMap = ListKit.list2Map(materialTypeList, "typeId", "typeName");

		// 有父属性的话查出父属性
		if (parentPropertyId > 0) {
			MaterialPropertyTypeBean parentPropertyBean = business.getMaterialPropertyTypeByKey(parentPropertyId);
			request.setAttribute("ParentPropertyBean", parentPropertyBean);
		}

		// 绑定页面数据
		request.setAttribute("materialTypeMap", materialTypeMap);
		request.setAttribute("materialTypeList", materialTypeList);
		request.setAttribute("ParentPropertyId", parentPropertyId);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("PropertyTypeList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/MaterialPropertyTypeList.jsp");
		rd.forward(request, response);
	}

}
