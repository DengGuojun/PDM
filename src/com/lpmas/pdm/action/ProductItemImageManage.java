package com.lpmas.pdm.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.business.ProductImageBusiness;
import com.lpmas.pdm.business.ProductItemBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;
import com.lpmas.pdm.config.ProductImageConfig;

/**
 * Servlet implementation class ProductItemImageManage
 */
@WebServlet("/pdm/ProductItemImageManage.do")
public class ProductItemImageManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductItemImageManage.class);
	private static final long serialVersionUID = 1L;

	public ProductItemImageManage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int itemId = ParamKit.getIntParameter(request, "itemId", 0);
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.UPDATE)) {
			return;
		}
		if (readOnly && !adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.SEARCH)) {
			return;
		}
		ProductItemBusiness productItemBusiness = new ProductItemBusiness();
		ProductItemBean productItemBean = productItemBusiness.getProductItemByKey(itemId);
		ProductImageBusiness productImageBusiness = new ProductImageBusiness();
		ProductImageBean productImageBean = productImageBusiness.getProductImageByKey(itemId,
				InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, ProductImageConfig.IMAGE_TYPE_PIC_1);
		request.setAttribute("ProductItemBean", productItemBean);
		request.setAttribute("ProductImageBean", productImageBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/ProductItemImageManage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.PRODUCT_ITEM_INFO, OperationConfig.UPDATE)) {
			return;
		}
		int itemId = ParamKit.getIntParameter(request, "itemId", 0);
		if (itemId == 0) {
			HttpResponseKit.alertMessage(response, "商品项ID错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		ProductImageBusiness business = new ProductImageBusiness();
		ProductImageBean bean = new ProductImageBean();
		try {
			bean = BeanKit.request2Bean(request, ProductImageBean.class);
			bean.setInfoType(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
			bean.setImageType(ProductImageConfig.IMAGE_TYPE_PIC_1);
			int result = 0;
			if (bean.getInfoId() != 0) {
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateProductImage(bean);
			} else {
				bean.setInfoId(itemId);
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addProductImage(bean);
			}

			if (result > 0) {
				response.sendRedirect("/pdm/ProductItemImageManage.do?itemId=" + itemId);
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
