package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.cache.MaterialPropertyCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.config.MaterialPropertyConfig;
import com.lpmas.pdm.dao.MaterialPropertyDao;
import com.lpmas.pdm.factory.MaterialPropertyFactory;
import com.lpmas.pdm.factory.ProductPropertyFactory;

public class MaterialPropertyBusiness {
	public int addMaterialProperty(MaterialPropertyBean bean) {
		MaterialPropertyDao dao = new MaterialPropertyDao();
		int result = dao.insertMaterialProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialId(), 0, 0,
					LogInfoConfig.LOG_MATERIAL_PROPERTY, bean.getPropertyCode(), "");
			refreshCache(bean);
		}
		return result;
	}

	public int updateMaterialProperty(MaterialPropertyBean bean) {
		MaterialPropertyDao dao = new MaterialPropertyDao();
		MaterialPropertyBean originalBean = dao.getMaterialPropertyByKey(bean.getMaterialId(), bean.getPropertyCode());
		int result = dao.updateMaterialProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialId(), 0, 0,
					LogInfoConfig.LOG_MATERIAL_PROPERTY, bean.getPropertyCode(), "");
			refreshCache(bean);
		}
		return result;
	}

	private Boolean refreshCache(MaterialPropertyBean bean) {
		MaterialPropertyCache cache = new MaterialPropertyCache();
		return cache.refreshMaterialPropertyCacheByKey(bean.getMaterialId(), bean.getPropertyCode());
	}

	public MaterialPropertyBean getMaterialPropertyByKey(int materialId, String propertyCode) {
		MaterialPropertyDao dao = new MaterialPropertyDao();
		return dao.getMaterialPropertyByKey(materialId, propertyCode);
	}

	public PageResultBean<MaterialPropertyBean> getMaterialPropertyPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		MaterialPropertyDao dao = new MaterialPropertyDao();
		return dao.getMaterialPropertyPageListByMap(condMap, pageBean);
	}

	public List<MaterialPropertyBean> getMaterialPropertyListByMaterialId(int materialId) {
		MaterialPropertyDao dao = new MaterialPropertyDao();
		return dao.getMaterialPropertyListByMaterialId(materialId);
	}

	public List<Integer> getMaterialPropertyIdListByPropertyCode(String propertyCode) {
		MaterialPropertyDao dao = new MaterialPropertyDao();
		return dao.getMaterialPropertyIdListByPropertyCode(propertyCode);
	}

	public String displayPropertyInput(MaterialPropertyTypeBean materialPropertyTypeBean,
			MaterialPropertyBean materialPropertyBean, boolean isSubType) {
		ProductPropertyFactory factory = new ProductPropertyFactory();
		PropertyInputDisplay display = factory.getPropertyInputDisplay(materialPropertyTypeBean.getInputMethod());
		return display.getPropertyInputStr(materialPropertyTypeBean, materialPropertyBean, isSubType);
	}

	public ReturnMessageBean verifyMaterialInfoProperty(Map<Integer, String> parameters) {
		ReturnMessageBean result = new ReturnMessageBean();
		String msg = "";
		MaterialPropertyTypeBusiness propertyBusiness = new MaterialPropertyTypeBusiness();
		for (int propertyId : parameters.keySet()) {
			MaterialPropertyTypeBean propertyType = propertyBusiness.getMaterialPropertyTypeByKey(propertyId);
			if (propertyType != null) {
				ReturnMessageBean msgTemp = verifyMaterialInfoProperty(propertyType, parameters.get(propertyId));
				if (StringKit.isValid(msgTemp.getMessage())) {
					msg += "[" + propertyType.getPropertyName() + "]" + msgTemp.getMessage() + ",";
				}
			}
		}
		if (msg.length() > 0) {
			msg = msg.substring(0, msg.length() - 1);
		}
		if (StringKit.isValid(msg)) {
			result.setMessage(msg);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public ReturnMessageBean verifyMaterialInfoProperty(MaterialPropertyTypeBean propertyType, String value) {
		ReturnMessageBean result = new ReturnMessageBean();
		// 校验是否必填
		if (propertyType.getIsRequired() == Constants.STATUS_VALID) {
			if (!StringKit.isValid(value)) {
				result.setMessage("该字段是必填字段;");
				return result;
			}
		}
		// 校验长度
		if (StringKit.isValid(propertyType.getInputStyle()) && StringKit.isValid(value)) {
			Map<String, String> inputStyleMap = JsonKit.toBean(propertyType.getInputStyle(), Map.class);
			int maxlength = StringKit.isValid(inputStyleMap.get(MaterialPropertyConfig.MAX_LENGTH))
					? Integer.parseInt(inputStyleMap.get(MaterialPropertyConfig.MAX_LENGTH)) : 0;
			int minlength = StringKit.isValid(inputStyleMap.get(MaterialPropertyConfig.MIN_LENGTH))
					? Integer.parseInt(inputStyleMap.get(MaterialPropertyConfig.MIN_LENGTH)) : 0;

			if (maxlength > 0) {
				if (value.length() > maxlength) {
					result.setMessage("最大只能录入" + maxlength + "个字");
					return result;
				}
			}
			if (minlength > 0) {
				if (value.length() < minlength) {
					result.setMessage("最小需要录入" + minlength + "个字");
					return result;
				}
			}
		}
		// 判断字段格式
		if (StringKit.isValid(propertyType.getFieldFormat()) && StringKit.isValid(value)) {
			String[] msgArr = new String[2];
			if (propertyType.getFieldFormat().indexOf("#") > 0) {
				msgArr = propertyType.getFieldFormat().split("#");
			} else {
				msgArr[0] = propertyType.getFieldFormat();
			}
			Pattern p = Pattern.compile(msgArr[0]);// 复杂匹配
			Matcher m = p.matcher(value);
			if (!m.matches()) {
				result.setMessage("格式不符！" + (StringKit.isValid(msgArr[1]) ? "正确格式例如：" + msgArr[1] : ""));
				return result;
			}
		}
		// 判断字段类型
		if (StringKit.isValid(value)) {
			MaterialPropertyFactory propertyFactory = new MaterialPropertyFactory();
			PropertyInputVerify propertyVerify = propertyFactory.getPropertyInputVerify(propertyType.getFieldType());
			if (propertyVerify != null) {
				result = propertyVerify.verifyProperty(value);
			}
		}
		return result;
	}

}