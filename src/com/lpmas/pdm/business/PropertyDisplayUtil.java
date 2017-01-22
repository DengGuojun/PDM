package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.Map;

import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.bean.MaterialPropertyTypeBean;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;

public class PropertyDisplayUtil {

	public static String displayPropertyInput(ProductPropertyTypeBean typeBean, ProductPropertyBean bean, boolean isSubType) {
		ProductPropertyBusiness propertyBusiness = new ProductPropertyBusiness();
		if(bean == null){
			bean = new ProductPropertyBean();
		}
		return propertyBusiness.displayPropertyInput(typeBean, bean, isSubType);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> inputStyle2Map(String inputStyle){
		Map<String, String> styleMap = new HashMap<String, String>();
		if(StringKit.isValid(inputStyle)){
			styleMap = JsonKit.toBean(inputStyle, Map.class);
		}
		return styleMap;
	}
	
	public static String displayPropertyInput(MaterialPropertyTypeBean typeBean, MaterialPropertyBean bean, boolean isSubType) {
		MaterialPropertyBusiness propertyBusiness = new MaterialPropertyBusiness();
		if(bean == null){
			bean = new MaterialPropertyBean();
		}
		return propertyBusiness.displayPropertyInput(typeBean, bean, isSubType);
	}
}
