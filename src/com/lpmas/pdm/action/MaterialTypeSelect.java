package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialTypeSelect
 */
@WebServlet("/pdm/MaterialTypeSelect.do")
public class MaterialTypeSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialTypeSelect() {
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
		// 查出当前有的物料类型
		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		List<MaterialTypeBean> materialTypeList = materialTypeBusiness.getMaterialTypeAllList();
		// 设值
		request.setAttribute("MaterialTypeList", materialTypeList);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "MaterialTypeSelect.jsp");
		rd.forward(request, response);
	}

}
