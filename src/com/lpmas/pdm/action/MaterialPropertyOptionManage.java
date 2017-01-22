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
import com.lpmas.pdm.bean.MaterialPropertyOptionBean;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.business.MaterialPropertyOptionBusiness;
import com.lpmas.pdm.business.MaterialPropertyTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialPropertyOptionManage
 */
@WebServlet("/pdm/MaterialPropertyOptionManage.do")
public class MaterialPropertyOptionManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(MaterialPropertyOptionManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialPropertyOptionManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接受请求参数
		int propertyOptionId = ParamKit.getIntParameter(request, "propertyOptionId", 0);
		MaterialPropertyOptionBean materialPropertyOptionBean = new MaterialPropertyOptionBean();
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly
				&& !adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.UPDATE)) {
			return;
		}
		if (readOnly
				&& !adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.SEARCH)) {
			return;
		}		
		// 判断是修改还是新增
		if (propertyOptionId > 0) {
			// 修改
			MaterialPropertyOptionBusiness business = new MaterialPropertyOptionBusiness();
			materialPropertyOptionBean = business.getMaterialPropertyOptionByKey(propertyOptionId);
		} else {
			// 新增	
			materialPropertyOptionBean.setStatus(Constants.STATUS_VALID);
		}

		// 根据属性ID查找对应的属性
		int propertyId = ParamKit.getIntParameter(request, "propertyId", 0);
		if (propertyId <= 0) {
			propertyId = materialPropertyOptionBean.getPropertyId();
		}
		MaterialPropertyTypeBusiness typeBusiness = new MaterialPropertyTypeBusiness();
		MaterialPropertyTypeBean typeBean = typeBusiness.getMaterialPropertyTypeByKey(propertyId);

		// 页面数据装载
		request.setAttribute("PropertyId", propertyId);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("PropertyTypeBean", typeBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("propertyOptionBean", materialPropertyOptionBean);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/MaterialPropertyOptionManage.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int userId = adminUserHelper.getAdminUserId();
		if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.UPDATE)) {
			return;
		}
		
		MaterialPropertyOptionBean materialPropertyOptionBean = new MaterialPropertyOptionBean();
		try {
			materialPropertyOptionBean = BeanKit.request2Bean(request, MaterialPropertyOptionBean.class);
			MaterialPropertyOptionBusiness business = new MaterialPropertyOptionBusiness();
			ReturnMessageBean messageBean = business.verifyMaterialPropertyPropertyOption(materialPropertyOptionBean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 判断新增还是修改
			int result = 0;
			if (materialPropertyOptionBean.getOptionId() > 0) {
				// 修改
				materialPropertyOptionBean.setModifyUser(userId);
				result = business.updateMaterialPropertyOption(materialPropertyOptionBean);
			} else {
				// 新增
				materialPropertyOptionBean.setCreateUser(userId);
				result = business.addMaterialPropertyOption(materialPropertyOptionBean);
			}

			// 判断处理结果
			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/MaterialPropertyOptionList.do?propertyId="
						+ materialPropertyOptionBean.getPropertyId());
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
