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
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductPropertyTypeBusiness;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

@WebServlet("/pdm/ProductPropertyTypeList.do")
public class ProductPropertyTypeList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductPropertyTypeList() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_PROPERTY_TYPE, OperationConfig.SEARCH)) {
			return;
		}
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
		String infoType = ParamKit.getParameter(request, "infoType", "").trim();
		if (StringKit.isValid(infoType)) {
			condMap.put("infoType", infoType);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		List<ProductTypeBean> subProductTypeList = new ArrayList<ProductTypeBean>();
		int productType = ParamKit.getIntParameter(request, "productType2", 0);
		int parentProductType = ParamKit.getIntParameter(request, "productType1", 0);
		if (productType == 0) {
			productType = parentProductType;
		}
		if (productType != 0) {
			condMap.put("productType", String.valueOf(productType));
		}

		ProductPropertyTypeBusiness business = new ProductPropertyTypeBusiness();
		PageResultBean<ProductPropertyTypeBean> result = business
				.getProductPropertyTypePageListByMap(condMap, pageBean);

		if (parentPropertyId > 0) {
			ProductPropertyTypeBean parentPropertyBean = business.getProductPropertyTypeByKey(parentPropertyId);
			request.setAttribute("ParentPropertyBean", parentPropertyBean);
		}
		// 获取当前所有的商品分类
		List<ProductTypeBean> productTypeList = productTypeBusiness.getProductTypeAllList();
		// 转换成MAP,ID是KEY
		Map<Integer, ProductTypeBean> productTypeMap = new HashMap<Integer, ProductTypeBean>();
		for (ProductTypeBean typeBean : productTypeList) {
			productTypeMap.put(typeBean.getTypeId(), typeBean);
		}
		// 获取商品类型列表
		List<ProductTypeBean> parentProductTypeList = productTypeBusiness.getProductTypeListByParentId(0);
		if(parentProductType != 0 ){
			subProductTypeList =  productTypeBusiness.getProductTypeListByParentId(parentProductType);
		}
		request.setAttribute("ParentProductTypeList", parentProductTypeList);
		request.setAttribute("SubProductTypeList", subProductTypeList);
		request.setAttribute("ProductTypeList", productTypeList);
		request.setAttribute("ProductTypeMap", productTypeMap);
		request.setAttribute("ParentPropertyId", parentPropertyId);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("PropertyTypeList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/ProductPropertyTypeList.jsp");
		rd.forward(request, response);
	}

}
