package com.lpmas.pdm.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductTypeBusiness;

/**
 * Servlet implementation class ProductTypeJsonList
 */
@WebServlet("/pdm/ProductTypeJsonList.do")
public class ProductTypeJsonList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductTypeJsonList() {
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

		int parentTypeId = ParamKit.getIntParameter(request, "parentTypeId", 0);
		ProductTypeBusiness business = new ProductTypeBusiness();
		List<ProductTypeBean> list = business.getProductTypeListByParentId(parentTypeId);
		if (list.size() > 0) {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html;charset=utf-8");
			Map<String, List<ProductTypeBean>> map = new HashMap<String, List<ProductTypeBean>>();
			map.put("result", list);
			String result = JsonKit.toJson(map);
			writer.write(result);
			writer.flush();
			writer.close();
		}
	}

}
