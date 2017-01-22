package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.ArrayList;
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
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductItemEntityBean;
import com.lpmas.pdm.business.ProductImageBusiness;
import com.lpmas.pdm.business.ProductItemBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductImageConfig;

/**
 * Servlet implementation class ProductItemList
 */
@WebServlet("/pdm/ProductItemList.do")
public class ProductItemList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductItemList() {
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
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int pageNum = ParamKit.getIntParameter(request, "pageNum", PdmConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", PdmConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		String itemNumber = ParamKit.getParameter(request, "itemNumber", "").trim();
		if (StringKit.isValid(itemNumber)) {
			condMap.put("itemNumber", itemNumber);
		}
		String itemName = ParamKit.getParameter(request, "itemName", "").trim();
		if (StringKit.isValid(itemName)) {
			condMap.put("itemName", itemName);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		ProductItemBusiness business = new ProductItemBusiness();
		ProductImageBusiness imageBusiness = new ProductImageBusiness();
		List<ProductItemEntityBean> productItemEntityBeanList = new ArrayList<ProductItemEntityBean>();
		PageResultBean<ProductItemBean> result = business.getProductItemPageListByMap(condMap, pageBean);
		for(ProductItemBean bean : result.getRecordList()){
			ProductImageBean imageBean = imageBusiness.getProductImageByKey(bean.getItemId(), InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, ProductImageConfig.IMAGE_TYPE_PIC_1);
			ProductItemEntityBean entityBean = new ProductItemEntityBean(bean, imageBean);
			productItemEntityBeanList.add(entityBean);
		}
		request.setAttribute("ProductItemEntityList", productItemEntityBeanList);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "ProductItemList.jsp");
		rd.forward(request, response);
	}

}
