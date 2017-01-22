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
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialTypeManage
 */
@WebServlet("/pdm/MaterialTypeManage.do")
public class MaterialTypeManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(MaterialTypeManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialTypeManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);

		MaterialTypeBusiness business = new MaterialTypeBusiness();
		MaterialTypeBean bean = new MaterialTypeBean();
		// typeId=0表示这不是一次修改，是一次新建的请求
		if (typeId > 0) {
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE, OperationConfig.SEARCH)){
				return;
			}
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE, OperationConfig.UPDATE)) {
				return;
			}		
			bean = business.getMaterialTypeByKey(typeId);
		} else {
			if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
		}

		// 绑定页面数据
		request.setAttribute("MaterialTypeBean", bean);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		// 转发
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(
				PdmConfig.PAGE_PATH + "MaterialTypeManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		MaterialTypeBean bean = new MaterialTypeBean();

		try {
			bean = BeanKit.request2Bean(request, MaterialTypeBean.class);
			MaterialTypeBusiness business = new MaterialTypeBusiness();

			// 服务端数据验证
			ReturnMessageBean messageBean = business.verifyMaterialType(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 判断是新建还是修改
			int result = 0;
			if (bean.getTypeId() > 0) {
				// 修改页面
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateMaterialType(bean);
			} else {
				// 新建页面
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addMaterialType(bean);
			}

			// 判断新建或者修改是否成功
			if (result > 0) {
				// 成功返回物料类型页面
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/MaterialTypeList.do");
			} else {
				// 失败浏览器返回
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}

		} catch (Exception e) {
			log.error("", e);
		}
	}

}
