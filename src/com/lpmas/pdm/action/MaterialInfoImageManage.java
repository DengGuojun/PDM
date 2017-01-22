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
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialImageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.business.MaterialImageBusiness;
import com.lpmas.pdm.business.MaterialInfoBusiness;
import com.lpmas.pdm.config.MaterialImageConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialInfoThumbnailManage
 */
@WebServlet("/pdm/MaterialInfoImageManage.do")
public class MaterialInfoImageManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductInfoImageManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialInfoImageManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int materialId = ParamKit.getIntParameter(request, "materialId", 0);
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.UPDATE)) {
			return;
		}
		if (readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.SEARCH)) {
			return;
		}
		MaterialInfoBusiness materialInfoBusiness = new MaterialInfoBusiness();
		MaterialInfoBean materialInfoBean = materialInfoBusiness.getMaterialInfoByKey(materialId);
		MaterialImageBusiness materialImageBusiness = new MaterialImageBusiness();
		MaterialImageBean materialImageBean = materialImageBusiness.getMaterialImageByKey(materialId,
				MaterialImageConfig.IMAGE_TYPE_PIC_1);
		request.setAttribute("MaterialInfoBean", materialInfoBean);
		request.setAttribute("MaterialImageBean", materialImageBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/MaterialInfoImageManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.UPDATE)) {
			return;
		}
		int infoId = ParamKit.getIntParameter(request, "infoId", 0);
		if (infoId == 0) {
			HttpResponseKit.alertMessage(response, "物料ID错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

		MaterialImageBusiness business = new MaterialImageBusiness();
		MaterialImageBean bean = new MaterialImageBean();
		try {
			bean = BeanKit.request2Bean(request, MaterialImageBean.class);
			bean.setImageType(MaterialImageConfig.IMAGE_TYPE_PIC_1);
			int result = 0;
			if (bean.getMaterialId() != 0) {
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateMaterialImage(bean);
			} else {
				bean.setMaterialId(infoId);
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addMaterialImage(bean);
			}

			if (result > 0) {
				response.sendRedirect("/pdm/MaterialInfoImageManage.do?materialId=" + infoId);
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

}
