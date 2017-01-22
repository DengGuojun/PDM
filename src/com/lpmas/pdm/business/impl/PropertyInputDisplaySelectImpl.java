package com.lpmas.pdm.business.impl;

import java.util.HashMap;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.FreemarkerKit;
import com.lpmas.pdm.business.PropertyDisplayUtil;
import com.lpmas.pdm.business.PropertyInputDisplay;
import com.lpmas.pdm.config.PdmConfig;

public class PropertyInputDisplaySelectImpl extends PropertyInputDisplay {

	@Override
	public String getPropertyInputStr(Object PropertyTypeBean, Object PropertyBean, boolean isSubType) {

		String fileName = "selectInputDisplay.html";
		FreemarkerKit freemarkerKit = new FreemarkerKit();
		HashMap<String, Object> contentMap = freemarkerKit.getContentMap();

		// ProductPropertyOptionBusiness optionBusiness = new
		// ProductPropertyOptionBusiness();
		// List<ProductPropertyOptionBean> optionList =
		// optionBusiness.getProductPropertyOptionListByPropertyId((Integer)invoke(PropertyTypeBean,"getPropertyId"));
		contentMap.put("Property", PropertyBean);
		// contentMap.put("ProductConfig", ProductConfig.class);
		contentMap.put("PropertyType", PropertyTypeBean);
		contentMap.put("isTrue", Constants.SELECT_TRUE);
		// contentMap.put("boolean", ProductPropertyConfig.FIELD_TYPE_BOOLEAN);
		contentMap.put("selectList", Constants.SELECT_LIST);
		contentMap.put("PropertyOptionList", getPropertyOptions(PropertyTypeBean));
		contentMap.put("styleMap",
				PropertyDisplayUtil.inputStyle2Map((String) invoke(PropertyTypeBean, "getInputStyle")));
		contentMap.put("isSubType", isSubType);
		return freemarkerKit.mergeTemplate(contentMap, PdmConfig.TEMPLATE_PATH, fileName);
	}

}
