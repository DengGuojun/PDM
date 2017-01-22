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
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductItemEntityBean;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductImageBusiness;
import com.lpmas.pdm.business.ProductInfoBusiness;
import com.lpmas.pdm.business.ProductItemBusiness;
import com.lpmas.pdm.business.ProductPropertyBusiness;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductImageConfig;

/**
 * Servlet implementation class ProductItemSelect
 */
@WebServlet("/pdm/ProductItemSelect.do")
public class ProductItemSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductItemSelect() {
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

		Map<Integer, Map<String, ProductPropertyBean>> productItemPropertyMap = new HashMap<Integer, Map<String, ProductPropertyBean>>();
		ProductPropertyBusiness productPropertyBusiness = new ProductPropertyBusiness();

		ProductItemBusiness business = new ProductItemBusiness();
		ProductImageBusiness imageBusiness = new ProductImageBusiness();
		List<ProductItemEntityBean> productItemEntityBeanList = new ArrayList<ProductItemEntityBean>();
		PageResultBean<ProductItemBean> result = business.getProductItemPageListByMap(condMap, pageBean);
		for (ProductItemBean bean : result.getRecordList()) {
			ProductImageBean imageBean = imageBusiness.getProductImageByKey(bean.getItemId(),
					InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, ProductImageConfig.IMAGE_TYPE_PIC_1);
			ProductItemEntityBean entityBean = new ProductItemEntityBean(bean, imageBean);
			productItemEntityBeanList.add(entityBean);
			//根据商品ID查商品项属性
			productItemPropertyMap.put(bean.getItemId(),
					ListKit.list2Map(productPropertyBusiness.getProductPropertyListByInfoIdAndInfoType(bean.getItemId(),
							InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM), "propertyCode"));
		}

		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		List<ProductTypeBean> productTypeList = productTypeBusiness.getProductTypeAllList();
		Map<Integer, ProductTypeBean> productTypeMap = ListKit.list2Map(productTypeList, "typeId");

		ProductInfoBusiness productInfoBusiness = new ProductInfoBusiness();
		Map<Integer, ProductInfoBean> productInfoMap = new HashMap<Integer, ProductInfoBean>();
		for (ProductItemEntityBean entityBean : productItemEntityBeanList) {
			productInfoMap.put(entityBean.getProductId(),
					productInfoBusiness.getProductInfoByKey(entityBean.getProductId()));
		}

		request.setAttribute("ProductInfoMap", productInfoMap);
		request.setAttribute("ProductTypeMap", productTypeMap);
		request.setAttribute("ProductItemEntityList", productItemEntityBeanList);
		request.setAttribute("ProductItemPropertyMap", productItemPropertyMap);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "ProductItemSelect.jsp");
		rd.forward(request, response);
	}

}
