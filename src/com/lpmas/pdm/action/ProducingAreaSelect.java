package com.lpmas.pdm.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.ProductPropertyConfig;
import com.lpmas.region.bean.CityInfoBean;
import com.lpmas.region.bean.ProvinceInfoBean;
import com.lpmas.region.bean.RegionInfoBean;
import com.lpmas.region.client.RegionServiceClient;

/**
 * Servlet implementation class ProducingAreaSelect
 */
@WebServlet("/pdm/ProducingAreaSelect.do")
public class ProducingAreaSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProducingAreaSelect() {
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
		int provinceId = ParamKit.getIntParameter(request, "provinceId", 0);
		int cityId = ParamKit.getIntParameter(request, "cityId", 0);
		String currentRegion = ParamKit.getParameter(request, "value", "");

		RegionServiceClient client = new RegionServiceClient();
		List<ProvinceInfoBean> provinceList = client
				.getProvinceInfoListByCountryCode(ProductPropertyConfig.PRODUCING_AREA_COUNTRY_CODE_CHINA);       
		if (provinceId > 0 || cityId > 0) {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html;charset=utf-8");
			String result = "";
			if (provinceId > 0) {
				List<CityInfoBean> cityList = client.getCityInfoListByProvinceId(provinceId);
				Map<String, List<CityInfoBean>> map = new HashMap<String, List<CityInfoBean>>();
				map.put("result", cityList);
				result = JsonKit.toJson(map);
			}
			if (cityId > 0) {
				List<RegionInfoBean> regionList = client.getRegionInfoListByCityId(cityId);
				Map<String, List<RegionInfoBean>> map = new HashMap<String, List<RegionInfoBean>>();
				map.put("result", regionList);
				result = JsonKit.toJson(map);
			}
			writer.write(result);
			writer.flush();
			writer.close();
		} else {
			request.setAttribute("CurrentRegion", currentRegion);
			request.setAttribute("ProvinceId", provinceId);
			request.setAttribute("ProvinceList", provinceList);
			RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "ProducingAreaSelect.jsp");
			rd.forward(request, response);
		}
	}
}
