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
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.business.UnitInfoBusiness;

/**
 * Servlet implementation class UnitInfoJsonList
 */
@WebServlet("/pdm/UnitInfoJsonList.do")
public class UnitInfoJsonList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnitInfoJsonList() {
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

		int unitTypeId = ParamKit.getIntParameter(request, "unitTypeId", 0);
		UnitInfoBusiness business = new UnitInfoBusiness();
		List<UnitInfoBean> list = business.getUnitInfoByTypeId(unitTypeId);
		if (list.size() > 0) {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html;charset=utf-8");
			Map<String, List<UnitInfoBean>> map = new HashMap<String, List<UnitInfoBean>>();
			map.put("result", list);
			String result = JsonKit.toJson(map);
			writer.write(result);
			writer.flush();
			writer.close();
		}
	}

}
