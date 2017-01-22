package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;

public class ProductTypeConfig {
	
	public static final String NUMBER_RULE = "NUMBER_RULE";
	public static final String ITEM_NUMBER_RULE = "ITEM_NUMBER_RULE";
	
	public static List<StatusBean<String, String>> PRODUCT_TYPE_PROPERTY_FIELD_LIST = new ArrayList<StatusBean<String, String>>();

	static {
		initProductTypeConfigList();
	}

	private static void initProductTypeConfigList() {
		PRODUCT_TYPE_PROPERTY_FIELD_LIST = new ArrayList<StatusBean<String, String>>();
		PRODUCT_TYPE_PROPERTY_FIELD_LIST.add(new StatusBean<String,String>(NUMBER_RULE, "商品编码规则"));
		PRODUCT_TYPE_PROPERTY_FIELD_LIST.add(new StatusBean<String,String>(ITEM_NUMBER_RULE, "商品项编码规则"));
	}
}
