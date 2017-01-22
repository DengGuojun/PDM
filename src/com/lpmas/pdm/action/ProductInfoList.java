package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.ArrayList;
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
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductInfoEntityBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductImageBusiness;
import com.lpmas.pdm.business.ProductInfoBusiness;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductImageConfig;

/**
 * Servlet implementation class ProductInfoList
 */
@WebServlet("/pdm/ProductInfoList.do")
public class ProductInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductInfoList() {
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
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		String productNumber = ParamKit.getParameter(request, "productNumber", "").trim();
		if (StringKit.isValid(productNumber)) {
			condMap.put("productNumber", productNumber);
		}
		String productName = ParamKit.getParameter(request, "productName", "").trim();
		if (StringKit.isValid(productName)) {
			condMap.put("productName", productName);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		// 获取当前所有的商品分类
		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		List<ProductTypeBean> productTypeList = productTypeBusiness.getProductTypeAllList();
		// 转换成MAP,ID是KEY
		Map<Integer, ProductTypeBean> productTypeMap = ListKit.list2Map(productTypeList, "typeId");

		ProductInfoBusiness business = new ProductInfoBusiness();
		ProductImageBusiness imageBusiness = new ProductImageBusiness();
		List<ProductInfoEntityBean> productInfoEntityBeanList = new ArrayList<ProductInfoEntityBean>();
		PageResultBean<ProductInfoBean> result = business.getProductInfoPageListByMap(condMap, pageBean);
		for (ProductInfoBean bean : result.getRecordList()) {
			ProductImageBean imageBean = imageBusiness.getProductImageByKey(bean.getProductId(),
					InfoTypeConfig.INFO_TYPE_PRODUCT, ProductImageConfig.IMAGE_TYPE_PIC_1);
			ProductInfoEntityBean entityBean = new ProductInfoEntityBean(bean, imageBean);
			productInfoEntityBeanList.add(entityBean);
		}
		request.setAttribute("ProductInfoEntityList", productInfoEntityBeanList);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("ProductTypeMap", productTypeMap);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "ProductInfoList.jsp");
		rd.forward(request, response);
	}

}
