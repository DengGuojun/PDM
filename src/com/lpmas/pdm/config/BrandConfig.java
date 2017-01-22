package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;

public class BrandConfig {
	
	public static List<StatusBean<String, String>> BRAND_PROPERTY_FIELD_LIST = new ArrayList<StatusBean<String, String>>();

	static {
		initBrandConfigList();
	}

	private static void initBrandConfigList() {
		BRAND_PROPERTY_FIELD_LIST = new ArrayList<StatusBean<String, String>>();
	}
}
