package com.lpmas.pdm.business.impl;

import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.FreemarkerKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.pdm.bean.ProductMultiplePropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.business.MultiplePropertyDisplayUtil;
import com.lpmas.pdm.business.MultiplePropertyInputDisplay;
import com.lpmas.pdm.config.PdmConfig;
import com.lpmas.pdm.config.ProductPropertyConfig;

public class MultiplePropertyInputDisplayOutsourceImpl extends MultiplePropertyInputDisplay {

	@Override
	public String getMultiplePropertyInputStr(ProductPropertyTypeBean productPropertyTypeBean,
			List<ProductMultiplePropertyBean> list) {
		String fileName = null;
		fileName = "outsourceInputDisplay4Multiple.html";
		FreemarkerKit freemarkerKit = new FreemarkerKit();
		HashMap<String, Object> contentMap = freemarkerKit.getContentMap();
		String multipleValue = "";
		for(ProductMultiplePropertyBean bean : list){
			if(StringKit.isValid(bean.getPropertyValue1())){
				multipleValue += bean.getPropertyValue1();
			}
			if(StringKit.isValid(bean.getPropertyValue2())){
				multipleValue += "," +bean.getPropertyValue2();
			}
			if(StringKit.isValid(bean.getPropertyValue3())){
				multipleValue += "," +bean.getPropertyValue3();
			}
			multipleValue += ";";
		}
		if(multipleValue.length()>0){
			multipleValue = multipleValue.substring(0, multipleValue.length() -1);
		}
		contentMap.put("multipleValue", multipleValue);
		contentMap.put("productPropertyType", productPropertyTypeBean);
		contentMap.put("outerUrl", productPropertyTypeBean.getFieldSource());
		contentMap.put("isTrue", Constants.SELECT_TRUE);
		contentMap.put("number", ProductPropertyConfig.FIELD_TYPE_NUMBER);
		contentMap.put("styleMap", MultiplePropertyDisplayUtil.inputStyle2Map(productPropertyTypeBean.getInputStyle()));
		return freemarkerKit.mergeTemplate(contentMap, PdmConfig.TEMPLATE_PATH, fileName);
	}
}
