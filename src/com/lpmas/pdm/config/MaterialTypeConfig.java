package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;

public class MaterialTypeConfig {

	public static final String NUMBER_RULE = "NUMBER_RULE";
	public static final String SERIAL_NUMBER_LENGTH = "SERIAL_NUMBER_LENGTH";
	// public static final String NUMBER_RULE="TypeCode#"+SERIAL_NUMBER_NAME;

	public static List<StatusBean<String, String>> MATERIAL_TYPE_PROPERTY_FIELD_LIST = new ArrayList<StatusBean<String, String>>();

	static {
		initMaterialTypeConfigList();
	}

	private static void initMaterialTypeConfigList() {
		MATERIAL_TYPE_PROPERTY_FIELD_LIST = new ArrayList<StatusBean<String, String>>();
		MATERIAL_TYPE_PROPERTY_FIELD_LIST.add(new StatusBean<String, String>(NUMBER_RULE, "物料编码规则"));
		MATERIAL_TYPE_PROPERTY_FIELD_LIST.add(new StatusBean<String, String>(SERIAL_NUMBER_LENGTH, "流水号长度"));
	}

}
