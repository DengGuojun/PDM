package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.List;

public class PropertyDispalyConfig {
	
	public static List<String> SUPPORTED_PROPERTY_BEAN_TYPE = new ArrayList<String>();
	
	static{
		SUPPORTED_PROPERTY_BEAN_TYPE.add("ProductPropertyTypeBean");
		SUPPORTED_PROPERTY_BEAN_TYPE.add("MaterialPropertyTypeBean");
	}

}
