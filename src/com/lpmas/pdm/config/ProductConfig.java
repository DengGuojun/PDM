package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class ProductConfig {

	public static final String NUMBER_RULE_DEFAULT_VALUE = "{PRODUCT_TYPE_1_NUMBER}{BRAND_CODE}{PRODUCT_TYPE_2_NUMBER}{PRODUCT_CODE}{SELLING_TYPE}{VERSION}";
	public static final String ITEM_NUMBER_RULE_DEFAULT_VALUE = "{PRODUCT_NUMBER}{SPECIFICATION}";
	
	public static final String SELLING_TYPE_CODE = "SELLING_TYPE";
	public static final String VERSION_CODE = "VERSION";

	public static final String QUALITY_LEVEL_SUPERFINE = "SUPERFINE";
	public static final String QUALITY_LEVEL_FIRST_CLASS = "FIRST_CLASS";
	public static final String QUALITY_LEVEL_SECOND_CLASS = "SECOND_CLASS";
	public static final String QUALITY_LEVEL_THIRD_CLASS = "THIRD_CLASS";
	public static List<StatusBean<String, String>> PRODUCT_QUALITY_LEVEL_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> PRODUCT_QUALITY_LEVEL_MAP = new HashMap<String, String>();

	public static final String USE_STATUS_MARKETABLE = "MARKETABLE";
	public static final String USE_STATUS_UNMARKETABLE = "UNMARKETABLE";
	public static List<StatusBean<String, String>> PRODUCT_USE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> PRODUCT_USE_STATUS_MAP = new HashMap<String, String>();

	static {
		initQualityLevelList();
		initQualityLevelMap();
		initUseStatusList();
		initUseStatusMap();
	}

	private static void initQualityLevelList() {
		PRODUCT_QUALITY_LEVEL_LIST = new ArrayList<StatusBean<String, String>>();
		PRODUCT_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_SUPERFINE, "特级"));
		PRODUCT_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_FIRST_CLASS, "一等品"));
		PRODUCT_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_SECOND_CLASS, "二等品"));
		PRODUCT_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_THIRD_CLASS, "三等品"));
	}

	private static void initQualityLevelMap() {
		PRODUCT_QUALITY_LEVEL_MAP = StatusKit.toMap(PRODUCT_QUALITY_LEVEL_LIST);
	}

	private static void initUseStatusList() {
		PRODUCT_USE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		PRODUCT_USE_STATUS_LIST.add(new StatusBean<String, String>(USE_STATUS_UNMARKETABLE, "不可销售"));
		PRODUCT_USE_STATUS_LIST.add(new StatusBean<String, String>(USE_STATUS_MARKETABLE, "可销售"));
	}

	private static void initUseStatusMap() {
		PRODUCT_USE_STATUS_MAP = StatusKit.toMap(PRODUCT_USE_STATUS_LIST);
	}
}
