package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.cache.ProductPropertyCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.config.ProductPropertyConfig;
import com.lpmas.pdm.dao.ProductPropertyDao;
import com.lpmas.pdm.factory.ProductPropertyFactory;

public class ProductPropertyBusiness {
	public int addProductProperty(ProductPropertyBean bean) {
		ProductPropertyDao dao = new ProductPropertyDao();		
		int result = dao.insertProductProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, bean.getInfoType(), bean.getInfoId(), 0, 0, LogInfoConfig.LOG_PRODUCT_PROPERTY,
					bean.getPropertyCode(), "");
			refreshCache(bean);
		}
		return result;
	}

	public int updateProductProperty(ProductPropertyBean bean) {
		ProductPropertyDao dao = new ProductPropertyDao();
		ProductPropertyBean originalBean = dao.getProductPropertyByKey(bean.getInfoId(), bean.getInfoType(),
				bean.getPropertyCode());		
		int result = dao.updateProductProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, bean.getInfoType(), bean.getInfoId(), 0, 0,
					LogInfoConfig.LOG_PRODUCT_PROPERTY, bean.getPropertyCode(), "");
			refreshCache(bean);
		}
		return result;
	}

	private Boolean refreshCache(ProductPropertyBean bean) {
		ProductPropertyCache cache = new ProductPropertyCache();
		return cache.refreshProductPropertyCacheByKey(bean.getInfoId(), bean.getInfoType(), bean.getPropertyCode());
	}

	public ProductPropertyBean getProductPropertyByKey(int infoId, int infoType, String propertyCode) {
		ProductPropertyDao dao = new ProductPropertyDao();
		return dao.getProductPropertyByKey(infoId, infoType, propertyCode);
	}

	public List<ProductPropertyBean> getProductPropertyListByInfoIdAndInfoType(int infoId, int infoType) {
		ProductPropertyDao dao = new ProductPropertyDao();
		return dao.getProductPropertyListByInfoIdAndInfoType(infoId, infoType);
	}

	public PageResultBean<ProductPropertyBean> getProductPropertyPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ProductPropertyDao dao = new ProductPropertyDao();
		return dao.getProductPropertyPageListByMap(condMap, pageBean);
	}

	public String displayPropertyInput(ProductPropertyTypeBean productPropertyTypeBean,
			ProductPropertyBean productPropertyBean, boolean isSubType) {
		ProductPropertyFactory factory = new ProductPropertyFactory();
		PropertyInputDisplay display = factory.getPropertyInputDisplay(productPropertyTypeBean.getInputMethod());
		return display.getPropertyInputStr(productPropertyTypeBean, productPropertyBean, isSubType);
	}

	public ReturnMessageBean verifyProductInfoProperty(Map<Integer, String> parameters) {
		ReturnMessageBean result = new ReturnMessageBean();
		String msg = "";
		ProductPropertyTypeBusiness propertyBusiness = new ProductPropertyTypeBusiness();
		for (int propertyId : parameters.keySet()) {
			ProductPropertyTypeBean propertyType = propertyBusiness.getProductPropertyTypeByKey(propertyId);
			if (propertyType != null) {
				ReturnMessageBean msgTemp = verifyProductInfoProperty(propertyType, parameters.get(propertyId));
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
	public ReturnMessageBean verifyProductInfoProperty(ProductPropertyTypeBean propertyType, String value) {
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
			int maxlength = StringKit.isValid(inputStyleMap.get(ProductPropertyConfig.MAX_LENGTH))
					? Integer.parseInt(inputStyleMap.get(ProductPropertyConfig.MAX_LENGTH)) : 0;
			int minlength = StringKit.isValid(inputStyleMap.get(ProductPropertyConfig.MIN_LENGTH))
					? Integer.parseInt(inputStyleMap.get(ProductPropertyConfig.MIN_LENGTH)) : 0;

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
			ProductPropertyFactory propertyFactory = new ProductPropertyFactory();
			PropertyInputVerify propertyVerify = propertyFactory.getPropertyInputVerify(propertyType.getFieldType());
			if (propertyVerify != null) {
				result = propertyVerify.verifyProperty(value);
			}
		}
		return result;
	}

}