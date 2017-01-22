package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.List;

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
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.business.MaterialPropertyTypeBusiness;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialPropertyTypeManage
 */
@WebServlet("/pdm/MaterialPropertyTypeManage.do")
public class MaterialPropertyTypeManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(MaterialPropertyTypeManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialPropertyTypeManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取页面参数
		int propertyId = ParamKit.getIntParameter(request, "propertyId", 0);
		int parentPropertyId = ParamKit.getIntParameter(request, "parentPropertyId", 0);
		MaterialPropertyTypeBean bean = new MaterialPropertyTypeBean();
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);

		int typeId = 0;
		MaterialPropertyTypeBusiness business = new MaterialPropertyTypeBusiness();
		// 判断是新建还是修改
		if (propertyId > 0) {
			// 修改
			if (!readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getMaterialPropertyTypeByKey(propertyId);
			parentPropertyId = bean.getParentPropertyId();
			// 设置物料类型选择框
			typeId = bean.getMaterialTypeId();
			MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
			MaterialTypeBean materialTypeBean = materialTypeBusiness.getMaterialTypeByKey(typeId);
			request.setAttribute("MaterialTypeBean", materialTypeBean);
		} else {
			// 新建
			if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.CREATE)) {
				return;
			}
			bean.setStatus(Constants.STATUS_VALID);
			bean.setIsModifiable(Constants.STATUS_VALID);
		}
		// 有父属性的话执行
		if (parentPropertyId > 0) {
			MaterialPropertyTypeBean parentPropertyTypeBean = business.getMaterialPropertyTypeByKey(parentPropertyId);
			request.setAttribute("ParentPropertyTypeBean", parentPropertyTypeBean);
		}
		// 获得所有的物料类型
		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		List<MaterialTypeBean> materialTypeList = materialTypeBusiness.getMaterialTypeAllList();
		// 绑定页面数据
		request.setAttribute("MaterialTypeList", materialTypeList);
		request.setAttribute("TypeId", typeId);
		request.setAttribute("ParentPropertyId", parentPropertyId);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("PropertyTypeBean", bean);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "/MaterialPropertyTypeManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MaterialPropertyTypeBean propertyTypeBean = new MaterialPropertyTypeBean();
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);

		try {
			// 页面参数绑定到对象
			propertyTypeBean = BeanKit.request2Bean(request, MaterialPropertyTypeBean.class);
			MaterialPropertyTypeBusiness business = new MaterialPropertyTypeBusiness();

			int propertyId = propertyTypeBean.getPropertyId();
			int result = 0;
			int userId = adminUserHelper.getAdminUserId();

			// 服务端数据验证
			ReturnMessageBean messageBean = business.verifyProductPropertyType(propertyTypeBean);
			// 非空字符串的话，证明验证失败
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 判断是新增还是修改
			if (propertyId > 0) {
				// 修改
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.UPDATE)) {
					return;
				}
				propertyTypeBean.setModifyUser(userId);
				result = business.updateMaterialPropertyType(propertyTypeBean);
			} else {
				// 新增
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_PROPERTY_TYPE, OperationConfig.CREATE)) {
					return;
				}
				propertyTypeBean.setCreateUser(userId);
				result = business.addMaterialPropertyType(propertyTypeBean);
			}
			// 判定执行结果
			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/MaterialPropertyTypeList.do");
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
