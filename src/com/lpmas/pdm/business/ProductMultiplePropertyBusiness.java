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
import com.lpmas.framework.util.UuidKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductMultiplePropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.config.ProductPropertyConfig;
import com.lpmas.pdm.dao.ProductMultiplePropertyDao;
import com.lpmas.pdm.factory.ProductMultiplePropertyFactory;

public class ProductMultiplePropertyBusiness {
	public int addProductMultipleProperty(ProductMultiplePropertyBean bean) {
		ProductMultiplePropertyDao dao = new ProductMultiplePropertyDao();
		bean.setPropertyId(UuidKit.getUuid());		
		int result = dao.insertProductMultipleProperty(bean);
		if (result >= 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, bean.getInfoType(), bean.getInfoId(), 0, 0,
					LogInfoConfig.LOG_PRODUCT_MULTIPLE_PROPERTY, bean.getPropertyCode(), "");
		}
		return result;
	}

	public int updateProductMultipleProperty(ProductMultiplePropertyBean bean) {
		ProductMultiplePropertyDao dao = new ProductMultiplePropertyDao();
		ProductMultiplePropertyBean originalBean = dao.getProductMultiplePropertyByKey(bean.getPropertyId());		
		int result = dao.updateProductMultipleProperty(bean);
		if (result >= 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, bean.getInfoType(), bean.getInfoId(), 0, 0,
					LogInfoConfig.LOG_PRODUCT_MULTIPLE_PROPERTY, bean.getPropertyCode(), "");
		}
		return result;
	}

	public int deleteProductMultipleProperty(ProductMultiplePropertyBean bean) {
		ProductMultiplePropertyDao dao = new ProductMultiplePropertyDao();		
		int result = dao.deleteProductMultipleProperty(bean.getPropertyId());
		if (result >= 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendRemoveLog(bean, bean.getInfoType(), bean.getInfoId(),
					LogInfoConfig.LOG_PRODUCT_MULTIPLE_PROPERTY);
		}
		return result;
	}

	public ProductMultiplePropertyBean getProductMultiplePropertyByKey(String propertyId) {
		ProductMultiplePropertyDao dao = new ProductMultiplePropertyDao();
		return dao.getProductMultiplePropertyByKey(propertyId);
	}

	public List<ProductMultiplePropertyBean> getProductPropertyListByInfoIdAndInfoType(int infoId, int infoType) {
		ProductMultiplePropertyDao dao = new ProductMultiplePropertyDao();
		return dao.getProductMultiplePropertyListByInfoIdAndInfoType(infoId, infoType);
	}

	public PageResultBean<ProductMultiplePropertyBean> getProductMultiplePropertyPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		ProductMultiplePropertyDao dao = new ProductMultiplePropertyDao();
		return dao.getProductMultiplePropertyPageListByMap(condMap, pageBean);
	}

	public String displayPropertyInput(ProductPropertyTypeBean productPropertyTypeBean,
			List<ProductMultiplePropertyBean> list) {
		ProductMultiplePropertyFactory factory = new ProductMultiplePropertyFactory();
		MultiplePropertyInputDisplay display = factory
				.getMultiplePropertyInputDisplay(productPropertyTypeBean.getInputMethod());
		return display.getMultiplePropertyInputStr(productPropertyTypeBean, list);
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
	public ReturnMessageBean verifyProductInfoProperty(ProductPropertyTypeBean propertyType, String multiValue) {
		ReturnMessageBean result = new ReturnMessageBean();
		// 按约定的固定格式分隔
		String[] multiValueArray = multiValue.split(";");
		// 校验是否必填
		if(multiValueArray.length == 0 && propertyType.getIsRequired() == Constants.STATUS_VALID){
			result.setMessage("该字段是必填字段;");
			return result;
		}
		for (String multProperty : multiValueArray) {
			if (!StringKit.isValid(multProperty)) {
				continue;
			}
			String[] values = multProperty.split(",");
			for (String value : values) {
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
					ProductMultiplePropertyFactory multiplePropertyFactory = new ProductMultiplePropertyFactory();
					PropertyInputVerify propertyVerify = multiplePropertyFactory
							.getPropertyInputVerify(propertyType.getFieldType());
					if (propertyVerify != null) {
						result = propertyVerify.verifyProperty(value);
						return result;
					}
				}

			}
		}
		return result;
	}

}