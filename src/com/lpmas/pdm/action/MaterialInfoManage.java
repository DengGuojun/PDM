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
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.business.MaterialInfoBusiness;
import com.lpmas.pdm.business.MaterialPropertyBusiness;
import com.lpmas.pdm.business.MaterialPropertyTypeBusiness;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.business.UnitInfoBusiness;
import com.lpmas.pdm.config.MaterialConfig;
import com.lpmas.pdm.config.MaterialPropertyConfig;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.PdmResource;

/**
 * Servlet implementation class MaterialInfoManage
 */
@WebServlet("/pdm/MaterialInfoManage.do")
public class MaterialInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(MaterialInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialInfoManage() {
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
		int materialId = ParamKit.getIntParameter(request, "materialId", 0);
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		String page = "";

		MaterialTypeBusiness typeBusiness = new MaterialTypeBusiness();
		MaterialInfoBusiness infoBusiness = new MaterialInfoBusiness();

		if (typeId == 0 && materialId == 0) {
			HttpResponseKit.alertMessage(response, "商品类型选择有误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} else {
			MaterialInfoBean marterialinfoBean = new MaterialInfoBean();
			if (materialId > 0) {
				// 修改物料
				if (readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.SEARCH)) {
					return;
				}
				if (!readOnly && !adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.UPDATE)) {
					return;
				}
				marterialinfoBean = infoBusiness.getMaterialInfoByKey(materialId);
			} else {
				// 从选择物料类型跳回来,新建物料
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.CREATE)) {
					return;
				}
				marterialinfoBean.setTypeId1(typeId);
				marterialinfoBean.setStatus(Constants.STATUS_VALID);
			}
			// 获得物料类型
			MaterialTypeBean typeBean = typeBusiness.getMaterialTypeByKey(marterialinfoBean.getTypeId1());
			// 获得属性值列表
			MaterialPropertyBusiness propertyBusiness = new MaterialPropertyBusiness();
			List<MaterialPropertyBean> propertyList = propertyBusiness.getMaterialPropertyListByMaterialId(materialId);
			// 根据分类ID获取动态属性类型
			MaterialPropertyTypeBusiness materialPropertyTypeBusiness = new MaterialPropertyTypeBusiness();
			List<MaterialPropertyTypeBean> propertyTypeList = materialPropertyTypeBusiness
					.getMaterialPropertyTypeByType(marterialinfoBean.getTypeId1(),
							MaterialPropertyConfig.PROP_TYPE_MAIN);
			// 要放在页面的动态属性类型及其值的MAP
			Map<String, MaterialPropertyBean> propertyMap = new HashMap<String, MaterialPropertyBean>();
			if (!propertyList.isEmpty()) {
				propertyMap = ListKit.list2Map(propertyList, "propertyCode");
			}
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
			// 获取计量单位
			UnitInfoBean unitInfoBean = new UnitInfoBean();
			if (materialId > 0) {
				UnitInfoBusiness unitInfoBusiness = new UnitInfoBusiness();
				unitInfoBean = unitInfoBusiness.getUnitInfoByCode(marterialinfoBean.getUnit());
			}
			request.setAttribute("UnitInfoBean", unitInfoBean);

			// 设置页面
			page = "MaterialInfoManage.jsp";

			// 获取使用状态列表
			List<StatusBean<String, String>> materilUseStatusList = MaterialConfig.MATERIAL_USE_STATUS_LIST;
			// 组装页面数据
			request.setAttribute("subPropertyTypeMap", subPropertyTypeMap);
			request.setAttribute("MaterialUseStatusList", materilUseStatusList);
			request.setAttribute("marterialinfoBean", marterialinfoBean);
			request.setAttribute("propertyList", propertyList);
			request.setAttribute("propertyTypeList", propertyTypeList);
			request.setAttribute("typeBean", typeBean);
			request.setAttribute("propertyMap", propertyMap);
			request.setAttribute("adminUserHelper", adminUserHelper);
			request.setAttribute("materialId", materialId);
			request.setAttribute("typeId", marterialinfoBean.getTypeId1());
		}

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PdmConfig.PAGE_PATH + page);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int userId = adminUserHelper.getAdminUserId();
		MaterialInfoBean bean = new MaterialInfoBean();
		int materialId = ParamKit.getIntParameter(request, "materialId", 0);
		try {
			bean = BeanKit.request2Bean(request, MaterialInfoBean.class);
			MaterialInfoBusiness business = new MaterialInfoBusiness();

			ReturnMessageBean messageBean = business.verifyMaterialInfo(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 获取基本属性类型列表
			int materialTypeId = bean.getTypeId1();
			MaterialPropertyTypeBusiness materialPropertyTypeBusiness = new MaterialPropertyTypeBusiness();
			List<MaterialPropertyTypeBean> propertyTypeList = materialPropertyTypeBusiness
					.getMaterialPropertyTypeByType(materialTypeId, MaterialPropertyConfig.PROP_TYPE_MAIN);

			// 转化成Map
			Map<Integer, MaterialPropertyTypeBean> materialTypeMap = new HashMap<Integer, MaterialPropertyTypeBean>();

			// 从REQUEST中获取参数值Map
			// KEY是propery_code
			Map<Integer, String> parameterMap = new HashMap<Integer, String>();
			Map<String, String> subParameterMap = new HashMap<String, String>();
			// 子属性验证用
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
					if (StringKit.isValid(subValue)) {
						subParameterMap.put(propertyTypeBean.getPropertyCode(), subValue);
						subParameterMap4Validate.put(propertyTypeBean.getPropertyId(), subValue);
					}
				}

				materialTypeMap.put((Integer) propertyTypeBean.getPropertyId(), propertyTypeBean);

			}

			// 服务端数据验证
			// 校验物料动态属性值格式
			MaterialPropertyBusiness materialPropertyBusiness = new MaterialPropertyBusiness();
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

			// 处理基本信息
			// 判断是修改还是新增
			int result = 0;
			if (bean.getMaterialId() == 0) {
				// ID为0是新建
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.CREATE)) {
					return;
				}
				// 需要生成物料代码
				String materialNumber = business.generateMarterialNumber(bean);
				if (!StringKit.isValid(materialNumber)) {
					HttpResponseKit.alertMessage(response, "物料编码规则缺失", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				bean.setMaterialNumber(materialNumber);
				bean.setCreateUser(userId);
				result = business.addMaterialInfo(bean);
				materialId = result;
			} else {
				// 不为0是修改
				if (!adminUserHelper.checkPermission(PdmResource.MATERIAL_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(userId);
				result = business.updateMaterialInfo(bean);
			}
			// 处理商品基本信息结束
			// 判断处理结果
			if (result > 0) {
				// 已经成功修改或者新增了物料基本信息
				// 返回的RESULT是新建的主键
				// 处理商品动态基本属性

				// 更新商品动态基本属性类型
				// 查询判断是更新还是新建插入
				result = 0;
				Set<Integer> keySet = parameterMap.keySet();
				for (Integer propertyId : keySet) {
					String propertyValue = parameterMap.get(propertyId);
					MaterialPropertyBean propertyBean = null;
					propertyBean = materialPropertyBusiness.getMaterialPropertyByKey(materialId,
							materialTypeMap.get(propertyId).getPropertyCode());

					if (propertyBean != null) {
						// 更新
						propertyBean.setModifyUser(userId);
						propertyBean.setPropertyValue1(propertyValue);
						propertyBean.setPropertyValue2(subParameterMap.get(propertyBean.getPropertyCode()));
						result = materialPropertyBusiness.updateMaterialProperty(propertyBean);
					} else if (StringKit.isValid(propertyValue.trim())) {
						// 插入
						//只有当PROPERTYVALUE1不是空串时插入数据库
						propertyBean = new MaterialPropertyBean();
						propertyBean.setMaterialId(materialId);
						propertyBean.setCreateUser(userId);
						propertyBean.setPropertyCode(materialTypeMap.get(propertyId).getPropertyCode());
						propertyBean.setPropertyValue1(propertyValue);
						propertyBean.setPropertyValue2(subParameterMap.get(propertyBean.getPropertyCode()));
						result = materialPropertyBusiness.addMaterialProperty(propertyBean);
					}
					if (!(result >= 0)) {
						// 操作中出现异常
						HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					}
				}
				HttpResponseKit.alertMessage(response, "处理成功", "/pdm/MaterialInfoList.do");
			} else {
				// 操作中出现异常
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
			// 操作中出现异常
			HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
		}
	}

}
