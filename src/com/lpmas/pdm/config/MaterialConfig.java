package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class MaterialConfig {

	// 信息类型
	public static final int INFO_TYPE_MATERIAL = 1; // 物料信息
	
	public static final int SERIAL_NUMBER_LENGTH = 5;
	public static final String SERIAL_NUMBER_NAME = "SERIAL_NUMBER";
	public static final String NUMBER_RULE_NAME = "NUMBER_RULE";
	public static final String NUMBER_RULE_DEFAULT_VALUE = "{TYPE_CODE}_" + "{" + SERIAL_NUMBER_NAME + "}";

	public static final String QUALITY_LEVEL_SUPERFINE = "SUPERFINE";
	public static final String QUALITY_LEVEL_FIRST_CLASS = "FIRST_CLASS";
	public static final String QUALITY_LEVEL_SECOND_CLASS = "SECOND_CLASS";
	public static final String QUALITY_LEVEL_THIRD_CLASS = "THIRD_CLASS";
	public static List<StatusBean<String, String>> MATERIAL_QUALITY_LEVEL_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> MATERIAL_QUALITY_LEVEL_MAP = new HashMap<String, String>();

	public static final String USE_STATUS_MARKETABLE = "MARKETABLE";
	public static final String USE_STATUS_DISABLE = "DISABLE";
	public static final String USE_STATUS_ENABLE = "ENABLE";
	public static List<StatusBean<String, String>> MATERIAL_USE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> MATERIAL_USE_STATUS_MAP = new HashMap<String, String>();

	static {
		initQualityLevelList();
		initQualityLevelMap();
		initUseStatusList();
		initUseStatusMap();
	}

	private static void initQualityLevelList() {
		MATERIAL_QUALITY_LEVEL_LIST = new ArrayList<StatusBean<String, String>>();
		MATERIAL_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_SUPERFINE, "特级"));
		MATERIAL_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_FIRST_CLASS, "一等品"));
		MATERIAL_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_SECOND_CLASS, "二等品"));
		MATERIAL_QUALITY_LEVEL_LIST.add(new StatusBean<String, String>(QUALITY_LEVEL_THIRD_CLASS, "三等品"));
	}

	private static void initQualityLevelMap() {
		MATERIAL_QUALITY_LEVEL_MAP = StatusKit.toMap(MATERIAL_QUALITY_LEVEL_LIST);
	}

	private static void initUseStatusList() {
		MATERIAL_USE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		// MATERIAL_USE_STATUS_LIST.add(new StatusBean<String,
		// String>(USE_STATUS_NOT_SOLD, "不可销售"));
		MATERIAL_USE_STATUS_LIST.add(new StatusBean<String, String>(USE_STATUS_DISABLE, "禁用"));
		MATERIAL_USE_STATUS_LIST.add(new StatusBean<String, String>(USE_STATUS_ENABLE, "启用"));
	}

	private static void initUseStatusMap() {
		MATERIAL_USE_STATUS_MAP = StatusKit.toMap(MATERIAL_USE_STATUS_LIST);
	}
}
