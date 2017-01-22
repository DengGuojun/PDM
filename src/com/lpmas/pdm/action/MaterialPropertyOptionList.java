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
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialPropertyOptionBean;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.business.MaterialPropertyOptionBusiness;
import com.lpmas.pdm.business.MaterialPropertyTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialPropertyOptionList
 */
@WebServlet("/pdm/MaterialPropertyOptionList.do")
public class MaterialPropertyOptionList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialPropertyOptionList() {
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
		if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.SEARCH)) {
			return;
		}
		// 分页数据
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		// 接收页面数据
		String propertyId = ParamKit.getParameter(request, "propertyId", "").trim();
		MaterialPropertyTypeBusiness materialPropertyTypeBusiness = new MaterialPropertyTypeBusiness();
		// 查询对应的物料属性分类是否存在
		if (StringKit.isValid(propertyId)) {
			MaterialPropertyTypeBean typeBean = materialPropertyTypeBusiness
					.getMaterialPropertyTypeByKey(Integer.parseInt(propertyId));
			request.setAttribute("PropertyTypeBean", typeBean);
		} else {
			HttpResponseKit.alertMessage(response, "物料属性ID不存在", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

		// 存在对应的物料分类
		// 处理查询条件
		HashMap<String, String> condMap = new HashMap<String, String>();
		if (StringKit.isValid(propertyId)) {
			condMap.put("propertyId", propertyId);
		}
		String optionValue = ParamKit.getParameter(request, "optionValue", "").trim();
		if (StringKit.isValid(optionValue)) {
			condMap.put("optionValue", optionValue);
		}
		String optionContent = ParamKit.getParameter(request, "optionContent", "").trim();
		if (StringKit.isValid(optionContent)) {
			condMap.put("optionContent", optionContent);
		}

		// 查询出对应的现象
		MaterialPropertyOptionBusiness materialPropertyOptionBusiness = new MaterialPropertyOptionBusiness();
		PageResultBean<MaterialPropertyOptionBean> result = materialPropertyOptionBusiness
				.getMaterialPropertyOptionPageListByMap(condMap, pageBean);

		// 装载页面数据
		request.setAttribute("PropertyOptionList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/MaterialPropertyOptionList.jsp");
		rd.forward(request, response);
	}

}
