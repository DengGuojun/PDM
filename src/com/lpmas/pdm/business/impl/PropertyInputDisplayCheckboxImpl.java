package com.lpmas.pdm.business.impl;

import java.util.HashMap;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.FreemarkerKit;
import com.lpmas.pdm.business.PropertyDisplayUtil;
import com.lpmas.pdm.business.PropertyInputDisplay;
import com.lpmas.pdm.config.PdmConfig;

public class PropertyInputDisplayCheckboxImpl extends PropertyInputDisplay {

	@Override
	public String getPropertyInputStr(Object PropertyTypeBean, Object PropertyBean, boolean isSubType) {
		if (!isSupportedBeanType(PropertyTypeBean))
			return "";

		String fileName = "checkboxInputDisplay.html";
		FreemarkerKit freemarkerKit = new FreemarkerKit();
		HashMap<String, Object> contentMap = freemarkerKit.getContentMap();
		contentMap.put("Property", PropertyBean);
		contentMap.put("PropertyType", PropertyTypeBean);
		contentMap.put("isTrue", Constants.SELECT_TRUE);
		contentMap.put("selectList", Constants.SELECT_LIST);
		contentMap.put("styleMap",
				PropertyDisplayUtil.inputStyle2Map((String) invoke(PropertyTypeBean, "getInputStyle")));
		contentMap.put("isSubType", isSubType);
		return freemarkerKit.mergeTemplate(contentMap, PdmConfig.TEMPLATE_PATH, fileName);
	}

}
