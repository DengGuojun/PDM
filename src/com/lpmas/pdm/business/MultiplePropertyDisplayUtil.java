package com.lpmas.pdm.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.framework.util.StringKit;
import com.lpmas.pdm.bean.ProductMultiplePropertyBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;

public class MultiplePropertyDisplayUtil {

	public static String displayMultiplePropertyInput(ProductPropertyTypeBean typeBean, List<ProductMultiplePropertyBean> list) {
		ProductMultiplePropertyBusiness multiplePropertyBusiness = new ProductMultiplePropertyBusiness();
		if (list == null ){
			list = new ArrayList<ProductMultiplePropertyBean>();
		}
		return multiplePropertyBusiness.displayPropertyInput(typeBean, list);
	}
	
	public static Map<String,String> inputStyle2Map(String inputStyle){
		Map<String, String> styleMap = new HashMap<String, String>();
		if(StringKit.isValid(inputStyle)){
			for(String styleStr : inputStyle.split(";")){
				if(styleStr.indexOf(":")>0 && styleStr.indexOf(":")!=styleStr.length()){
					String[] styleArr = styleStr.split(":");
					styleMap.put(styleArr[0], styleArr[1]);
				}
			}
		}
		return styleMap;
	}
}
