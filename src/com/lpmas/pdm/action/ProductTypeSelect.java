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
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class ProductTypeSelect
 */
@WebServlet("/pdm/ProductTypeSelect.do")
public class ProductTypeSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductTypeSelect() {
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
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_1, OperationConfig.SEARCH)) {
			return;
		}
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_TYPE_2, OperationConfig.SEARCH)) {
			return;
		}
		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		List<ProductTypeBean> topProductTypeList = productTypeBusiness.getProductTypeListByParentId(0);
		request.setAttribute("TopProductTypeList", topProductTypeList);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "ProductTypeSelect.jsp");
		rd.forward(request, response);
	}

}
