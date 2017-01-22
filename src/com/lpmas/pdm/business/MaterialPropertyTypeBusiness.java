package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialPropertyOptionBean;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.MaterialPropertyTypeDao;

public class MaterialPropertyTypeBusiness {
	public int addMaterialPropertyType(MaterialPropertyTypeBean bean) {
		MaterialPropertyTypeDao dao = new MaterialPropertyTypeDao();
		int result = dao.insertMaterialPropertyType(bean);
		if (result > 0) {
			bean.setPropertyId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getPropertyId(), 0, 0,
					LogInfoConfig.LOG_MATERIAL_PROPERTY_TYPE);
		}
		return result;
	}

	public int updateMaterialPropertyType(MaterialPropertyTypeBean bean) {
		MaterialPropertyTypeDao dao = new MaterialPropertyTypeDao();
		if (bean.getStatus() == Constants.STATUS_NOT_VALID) {
			MaterialPropertyOptionBusiness materialPropertyOptionBusiness = new MaterialPropertyOptionBusiness();
			List<MaterialPropertyOptionBean> materialPropertyOptionList = materialPropertyOptionBusiness
					.getMaterialPropertyOptionListByPropertyId(bean.getPropertyId());
			for (MaterialPropertyOptionBean materialPropertyOptionBean : materialPropertyOptionList) {
				materialPropertyOptionBean.setModifyUser(bean.getModifyUser());
				materialPropertyOptionBean.setStatus(Constants.STATUS_NOT_VALID);
				materialPropertyOptionBusiness.updateMaterialPropertyOption(materialPropertyOptionBean);
			}
		}
		MaterialPropertyTypeBean originalBean = dao.getMaterialPropertyTypeByKey(bean.getPropertyId());
		int result = dao.updateMaterialPropertyType(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getPropertyId(),
					LogInfoConfig.LOG_MATERIAL_PROPERTY_TYPE);
		}
		return result;
	}

	public MaterialPropertyTypeBean getMaterialPropertyTypeByKey(int propertyId) {
		MaterialPropertyTypeDao dao = new MaterialPropertyTypeDao();
		return dao.getMaterialPropertyTypeByKey(propertyId);
	}

	public MaterialPropertyTypeBean getMaterialPropertyTypeByParentId(int parentId) {
		MaterialPropertyTypeDao dao = new MaterialPropertyTypeDao();
		return dao.getMaterialPropertyTypeByParentId(parentId, Constants.STATUS_VALID);
	}

	public PageResultBean<MaterialPropertyTypeBean> getMaterialPropertyTypePageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		MaterialPropertyTypeDao dao = new MaterialPropertyTypeDao();
		return dao.getMaterialPropertyTypePageListByMap(condMap, pageBean);
	}

	public List<MaterialPropertyTypeBean> getMaterialPropertyTypeListByMaterialTypeId(int materialTypeId) {
		MaterialPropertyTypeDao dao = new MaterialPropertyTypeDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("materialTypeId", String.valueOf(materialTypeId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getMaterialPropertyTypeListByMap(condMap);
	}

	public List<MaterialPropertyTypeBean> getMaterialPropertyTypeByType(int materialTypeId, int propertyType) {
		MaterialPropertyTypeDao dao = new MaterialPropertyTypeDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("materialTypeId", String.valueOf(materialTypeId));
		condMap.put("propertyType", String.valueOf(propertyType));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getMaterialPropertyTypeListByMap(condMap);
	}

	public ReturnMessageBean verifyProductPropertyType(MaterialPropertyTypeBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getPropertyCode())) {
			result.setMessage("属性代码必须填写");
		} else if (!isValidPropertyCode(bean.getPropertyCode())) {
			result.setMessage("属性代码格式不符合规范");
		} else if (!StringKit.isValid(bean.getPropertyName())) {
			result.setMessage("属性名称必须填写");
		} else if (bean.getMaterialTypeId() < 0) {
			result.setMessage("物料分类必须填写");
		} else if (bean.getParentPropertyId() != 0) {
			MaterialPropertyTypeBean materialPropertyTypeBean = getMaterialPropertyTypeByKey(
					bean.getParentPropertyId());
			if (materialPropertyTypeBean.getStatus() == Constants.STATUS_NOT_VALID) {
				result.setMessage("父属性无效,不允许对子属性进行操作");
			}
		}

		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		MaterialTypeBean typeBean = materialTypeBusiness.getMaterialTypeByKey(bean.getMaterialTypeId());
		if (typeBean == null || typeBean.getStatus() == Constants.STATUS_NOT_VALID) {
			result.setMessage("物料类型无效");
		}
		return result;
	}

	public boolean isValidPropertyCode(String propertyCode) {
		if (!propertyCode.matches("^[a-zA-Z0-9_]+$")) {
			return false;
		}
		return true;
	}

}