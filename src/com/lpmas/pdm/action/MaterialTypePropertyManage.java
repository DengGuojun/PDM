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
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.bean.MaterialTypePropertyBean;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.business.MaterialTypePropertyBusiness;
import com.lpmas.pdm.config.MaterialConfig;
import com.lpmas.pdm.config.MaterialTypeConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialTypePropertyManage
 */
@WebServlet("/pdm/MaterialTypePropertyManage.do")
public class MaterialTypePropertyManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialTypePropertyManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE_PROPERTY, OperationConfig.SEARCH)) {
			return;
		}
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);

		// 查找物料类型属性
		MaterialTypePropertyBusiness materialTypePropertyBusiness = new MaterialTypePropertyBusiness();
		List<MaterialTypePropertyBean> materialTypePropertyList = materialTypePropertyBusiness
				.getMaterialTypePropertyListByTypeId(typeId);
		// 设置默认值，供用户参考填写
		List<MaterialTypePropertyBean> defaultValuelist = new ArrayList<MaterialTypePropertyBean>();
		MaterialTypePropertyBean numberRuleBean = new MaterialTypePropertyBean();
		numberRuleBean.setPropertyCode(MaterialTypeConfig.NUMBER_RULE);
		numberRuleBean.setPropertyValue(MaterialConfig.NUMBER_RULE_DEFAULT_VALUE);
		defaultValuelist.add(numberRuleBean);
		MaterialTypePropertyBean serialNumberLengthBean = new MaterialTypePropertyBean();
		serialNumberLengthBean.setPropertyCode(MaterialTypeConfig.SERIAL_NUMBER_LENGTH);
		serialNumberLengthBean.setPropertyValue(MaterialConfig.SERIAL_NUMBER_LENGTH + "");
		defaultValuelist.add(serialNumberLengthBean);
		
		
		request.setAttribute("MaterialTypePropertyList", materialTypePropertyList);
		request.setAttribute("PropertyDefaultValueList", defaultValuelist);

		// 查询物料类型
		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		MaterialTypeBean materialTypeBean = materialTypeBusiness.getMaterialTypeByKey(typeId);
		request.setAttribute("MaterialTypeBean", materialTypeBean);

		request.setAttribute("TypeId", typeId);
		request.setAttribute("MaterialTypePropertyFieldList", MaterialTypeConfig.MATERIAL_TYPE_PROPERTY_FIELD_LIST);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + "MaterialTypePropertyManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		List<StatusBean<String, String>> materialTypeConfigStatuslist = MaterialTypeConfig.MATERIAL_TYPE_PROPERTY_FIELD_LIST;
		MaterialTypePropertyBusiness materialTypePropertyBusiness = new MaterialTypePropertyBusiness();

		for (StatusBean<String, String> statusBean : materialTypeConfigStatuslist) {
			int result = 0;
			String propertyCode = statusBean.getStatus();
			MaterialTypePropertyBean bean = new MaterialTypePropertyBean();
			bean.setTypeId(typeId);
			bean.setPropertyCode(propertyCode);
			bean.setPropertyValue(ParamKit.getParameter(request, propertyCode, ""));

			if (materialTypePropertyBusiness.getMaterialTypePropertyByKey(typeId, propertyCode) != null) {
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE_PROPERTY, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = materialTypePropertyBusiness.updateMaterialTypeProperty(bean);
			} else {
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_TYPE_PROPERTY, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = materialTypePropertyBusiness.addMaterialTypeProperty(bean);
			}
			if (result == -1) {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		}
		HttpResponseKit.alertMessage(response, "处理成功", HttpResponseKit.ACTION_HISTORY_BACK);
	}

}
