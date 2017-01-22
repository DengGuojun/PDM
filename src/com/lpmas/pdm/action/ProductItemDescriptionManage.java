package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductDescriptionBean;
import com.lpmas.pdm.bean.ProductDescriptionTypeBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.business.ProductDescriptionBusiness;
import com.lpmas.pdm.business.ProductDescriptionTypeBusiness;
import com.lpmas.pdm.business.ProductItemBusiness;
import com.lpmas.pdm.config.PdmConfig;

/**
 * Servlet implementation class CommodityDescriptionManage
 */
@WebServlet("/pdm/ProductItemDescriptionManage.do")
public class ProductItemDescriptionManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductItemDescriptionManage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int itemId = ParamKit.getIntParameter(request, "itemId", 0);
		AdminUserHelper adminHelper = new AdminUserHelper(request, response);
		ProductItemBusiness productItemBusiness = new ProductItemBusiness();
		ProductItemBean productItemBean = productItemBusiness.getProductItemByKey(itemId);

		List<ProductDescriptionBean> descriptionList = new ArrayList<ProductDescriptionBean>();

		if (itemId > 0) {
			ProductDescriptionBusiness business = new ProductDescriptionBusiness();
			descriptionList = business.getProductDescriptionListByInfoIdAndInfoType(itemId, InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
		}

		request.setAttribute("ProductItemBean", productItemBean);
		request.setAttribute("ProductItemDescriptionList", descriptionList);

		ProductDescriptionTypeBusiness descriptionTypeBusiness = new ProductDescriptionTypeBusiness();
		List<ProductDescriptionTypeBean> typeList = descriptionTypeBusiness
				.getProductDescriptionTypeListByInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
		request.setAttribute("ProductDescriptionTypeList", typeList);
		request.setAttribute("AdminUserHelper", adminHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/ProductItemDescriptionManage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		int itemId = ParamKit.getIntParameter(request, "itemId", 0);
		AdminUserHelper adminHelper = new AdminUserHelper(request, response);

		ProductDescriptionTypeBusiness descriptionTypeBusiness = new ProductDescriptionTypeBusiness();
		List<ProductDescriptionTypeBean> list = descriptionTypeBusiness.getProductDescriptionTypeListByInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);

		int result = 0;
		int userId = adminHelper.getAdminUserId();

		for (ProductDescriptionTypeBean descType : list) {
			ProductDescriptionBean bean = new ProductDescriptionBean();
			bean.setInfoId(itemId);
			bean.setInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
			bean.setDescCode(descType.getTypeCode());
			bean.setDescValue(ParamKit.getParameter(request, "DESC_" + descType.getTypeCode(), ""));

			ProductDescriptionBusiness business = new ProductDescriptionBusiness();
			if (business.getProductDescriptionByKey(itemId, InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, descType.getTypeCode()) != null) {
				bean.setModifyUser(userId);
				result = business.updateProductDescription(bean);
			} else {
				bean.setCreateUser(userId);
				result = business.addProductDescription(bean);
			}
		}
		request.setAttribute("AdminUserHelper", adminHelper);
		if (result > 0) {
			HttpResponseKit.alertMessage(response, "处理成功", "/pdm/ProductItemDescriptionManage.do?itemId=" + itemId);
		} else {
			HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
		}
	}
}
