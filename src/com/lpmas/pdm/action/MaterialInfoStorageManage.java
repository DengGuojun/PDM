package com.lpmas.pdm.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.business.MaterialInfoBusiness;
import com.lpmas.pdm.business.MaterialPropertyBusiness;
import com.lpmas.pdm.business.MaterialPropertyTypeBusiness;
import com.lpmas.pdm.config.MaterialPropertyConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialInfoStorageManage
 */
@WebServlet("/pdm/MaterialInfoStorageManage.do")
public class MaterialInfoStorageManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(ProductInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialInfoStorageManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.UPDATE)) {
			return;
		}
		if (readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int materialId = ParamKit.getIntParameter(request, "materialId", 0);

		// 获取对应的物料
		MaterialInfoBusiness materialInfoBusiness = new MaterialInfoBusiness();
		MaterialInfoBean materialInfoBean = materialInfoBusiness.getMaterialInfoByKey(materialId);
		if (materialInfoBean == null) {
			HttpResponseKit.alertMessage(response, "物料信息缺失", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

		// 获取物料动态仓储属性类型
		MaterialPropertyTypeBusiness materialPropertyTypeBusiness = new MaterialPropertyTypeBusiness();
		List<MaterialPropertyTypeBean> propertyTypeList = materialPropertyTypeBusiness
				.getMaterialPropertyTypeByType(materialInfoBean.getTypeId1(), MaterialPropertyConfig.PROP_TYPE_STORAGE);
		// 获取动态属性的子属性
		Map<String, MaterialPropertyTypeBean> subPropertyTypeMap = new HashMap<String, MaterialPropertyTypeBean>();
		for (MaterialPropertyTypeBean propertyTypeBean : propertyTypeList) {
			if (propertyTypeBean.getParentPropertyId() == 0) {
				MaterialPropertyTypeBean suMaterialPropertyTypeBean = materialPropertyTypeBusiness
						.getMaterialPropertyTypeByParentId(propertyTypeBean.getPropertyId());
				if (suMaterialPropertyTypeBean != null) {
					subPropertyTypeMap.put(propertyTypeBean.getPropertyCode(), suMaterialPropertyTypeBean);
				}
			}
		}
		// 获取物料动态属性值
		MaterialPropertyBusiness materialPropertyBusiness = new MaterialPropertyBusiness();
		List<MaterialPropertyBean> propertyList = materialPropertyBusiness
				.getMaterialPropertyListByMaterialId(materialId);
		// 获得Map
		Map<String, MaterialPropertyBean> propertyTypeMap = ListKit.list2Map(propertyList, "propertyCode");

		// 组装页面数据
		request.setAttribute("subPropertyTypeMap", subPropertyTypeMap);
		request.setAttribute("adminUserHelper", adminUserHelper);
		request.setAttribute("materialId", materialId);
		request.setAttribute("materialInfoBean", materialInfoBean);
		request.setAttribute("propertyTypeList", propertyTypeList);
		request.setAttribute("propertyList", propertyList);
		request.setAttribute("propertyTypeMap", propertyTypeMap);

		// 请求转发
		RequestDispatcher rd = this.getServletContext()
				.getRequestDispatcher(PdmConfig.PAGE_PATH + "MaterialInfoStorageManage.jsp");
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
		int userId = adminUserHelper.getAdminUserId();
		int materialId = ParamKit.getIntParameter(request, "materialId", 0);

		// 根据物料ID查到对应的物料
		MaterialInfoBusiness materialInfoBusiness = new MaterialInfoBusiness();
		MaterialInfoBean materialInfoBean = new MaterialInfoBean();
		materialInfoBean = materialInfoBusiness.getMaterialInfoByKey(materialId);

		// 获取仓储属性类型列表
		int materialTypeId = materialInfoBean.getTypeId1();
		MaterialPropertyTypeBusiness materialPropertyTypeBusiness = new MaterialPropertyTypeBusiness();
		List<MaterialPropertyTypeBean> propertyTypeList = materialPropertyTypeBusiness
				.getMaterialPropertyTypeByType(materialTypeId, MaterialPropertyConfig.PROP_TYPE_STORAGE);

		// 转化成Map
		Map<Integer, MaterialPropertyTypeBean> materialTypeMap = new HashMap<Integer, MaterialPropertyTypeBean>();

		try {
			// 从REQUEST中获取参数值Map
			// KEY是propery_code
			Map<Integer, String> parameterMap = new HashMap<Integer, String>();
			Map<String, String> subParameterMap = new HashMap<String, String>();
			Map<Integer, String> subParameterMap4Validate = new HashMap<Integer, String>();
			for (MaterialPropertyTypeBean propertyTypeBean : propertyTypeList) {
				if (propertyTypeBean.getParentPropertyId() == PdmConfig.ROOT_PARENT_ID) {
					if (propertyTypeBean.getInputMethod() == MaterialPropertyConfig.INPUT_METHOD_CHECKBOX) {
						parameterMap.put(propertyTypeBean.getPropertyId(),
								ParamKit.getParameter(request, "PROPERTY_" + propertyTypeBean.getPropertyCode(), "0"));
					} else {
						parameterMap.put(propertyTypeBean.getPropertyId(),
								ParamKit.getParameter(request, "PROPERTY_" + propertyTypeBean.getPropertyCode(), ""));
					}
				} else {
					String subValue = ParamKit.getParameter(request,
							"SUB_PROPERTY_" + propertyTypeBean.getPropertyCode(), null);
					if (subValue != null) {
						subParameterMap.put(propertyTypeBean.getPropertyCode(), subValue);
						subParameterMap4Validate.put(propertyTypeBean.getPropertyId(), subValue);
					}
				}

				materialTypeMap.put((Integer) propertyTypeBean.getPropertyId(), propertyTypeBean);

			}

			// 服务端数据验证
			// 校验物料动态属性值格式
			MaterialPropertyBusiness materialPropertyBusiness = new MaterialPropertyBusiness();
			ReturnMessageBean messageBean = new ReturnMessageBean();
			messageBean = materialPropertyBusiness.verifyMaterialInfoProperty(parameterMap);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 子属性验证
			messageBean = new ReturnMessageBean();
			messageBean = materialPropertyBusiness.verifyMaterialInfoProperty(subParameterMap4Validate);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 更新商品动态基本属性类型
			// 查询判断是更新还是新建插入
			int result = 0;
			Set<Integer> keySet = parameterMap.keySet();
			for (Integer propertyId : keySet) {
				String propertyValue = parameterMap.get(propertyId);
				MaterialPropertyBean bean = null;
				bean = materialPropertyBusiness.getMaterialPropertyByKey(materialId,
						materialTypeMap.get(propertyId).getPropertyCode());
				if (bean != null) {
					// 更新
					bean.setModifyUser(userId);
					bean.setPropertyValue1(propertyValue);
					bean.setPropertyValue2(subParameterMap.get(bean.getPropertyCode()));
					result = materialPropertyBusiness.updateMaterialProperty(bean);
				} else if (StringKit.isValid(propertyValue.trim())) {
					// 插入
					//只有当PROPERTYVALUE1不是空串时插入数据库
					bean = new MaterialPropertyBean();
					bean.setMaterialId(materialId);
					bean.setCreateUser(userId);
					bean.setPropertyCode(materialTypeMap.get(propertyId).getPropertyCode());
					bean.setPropertyValue1(propertyValue);
					bean.setPropertyValue2(subParameterMap.get(bean.getPropertyCode()));
					result = materialPropertyBusiness.addMaterialProperty(bean);
				}
				if (!(result >= 0)) {
					// 操作中出现异常
					HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
					break;
				}
			}
			if (result >= 0)
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/MaterialInfoList.do");

		} catch (Exception e) {
			log.error("", e);
		}

	}

}
