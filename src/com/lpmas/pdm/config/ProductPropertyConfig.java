package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class ProductPropertyConfig {

	// 属性类型
	public static final int PROP_TYPE_MAIN = 1; // 基本属性
	public static final int PROP_TYPE_STORAGE = 2; // 仓储属性

	public static List<StatusBean<Integer, String>> PROP_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> PROP_TYPE_MAP = new HashMap<Integer, String>();

	public static List<StatusBean<Integer, String>> INFO_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> INFO_TYPE_MAP = new HashMap<Integer, String>();

	// 输入方式
	public static final int INPUT_METHOD_TEXT = 1;// 文本输入
	public static final int INPUT_METHOD_SELECT = 2;// 选择框输入
	public static final int INPUT_METHOD_DATE = 3;// 日期输入
	public static final int INPUT_METHOD_CHECKBOX = 4;// 复选框输入
	public static final int INPUT_METHOD_TEXTAREA = 5;// textarea
	public static final int INPUT_METHOD_BOX = 6; // 弹窗选择

	public static List<StatusBean<Integer, String>> INPUT_METHOD_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> INPUT_METHOD_MAP = new HashMap<Integer, String>();

	// 字段类型
	public static final int FIELD_TYPE_DATE = 1;// 日期类型
	public static final int FIELD_TYPE_BOOLEAN = 2;// 布尔值类型
	public static final int FIELD_TYPE_TEXT = 3;// 文本类型
	public static final int FIELD_TYPE_NUMBER = 4;// 数值类型

	public static List<StatusBean<Integer, String>> FIELD_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> FIELD_TYPE_MAP = new HashMap<Integer, String>();

	// 是否可修改
	public static final int PROPERTY_MODIFIABLE = 1; // 可修改属性
	public static final int PROPERTY_UNMODIFIABLE = 0; // 可修改属性
	
	public static List<StatusBean<Integer, String>> PROPERTY_MODIFI_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> PROPERTY_MODIFI_MAP = new HashMap<Integer, String>();

	public static final String MAX_LENGTH = "maxlength";
	public static final String MIN_LENGTH = "minlength";
	
	public static final String PRODUCING_AREA_COUNTRY_CODE_CHINA = "086";

	static {
		initPropertyTypeList();
		initPropertyTypeMap();

		initInfoTypeList();
		initInfoTypeMap();

		initInputMethodList();
		initInputMethodMap();

		initFieldTypeList();
		initFieldTypeMap();
		
		initPropertyModifiList();
		initPropertyModifiMap();
	}

	private static void initPropertyTypeList() {
		PROP_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		PROP_TYPE_LIST.add(new StatusBean<Integer, String>(PROP_TYPE_MAIN, "基本属性"));
		PROP_TYPE_LIST.add(new StatusBean<Integer, String>(PROP_TYPE_STORAGE, "仓储属性"));
	}

	private static void initPropertyTypeMap() {
		PROP_TYPE_MAP = StatusKit.toMap(PROP_TYPE_LIST);
	}

	private static void initInfoTypeList() {
		INFO_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		INFO_TYPE_LIST.add(new StatusBean<Integer, String>(InfoTypeConfig.INFO_TYPE_PRODUCT, "商品"));
		INFO_TYPE_LIST.add(new StatusBean<Integer, String>(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, "商品项"));
	}

	private static void initInfoTypeMap() {
		INFO_TYPE_MAP = StatusKit.toMap(INFO_TYPE_LIST);
	}

	private static void initInputMethodList() {
		INPUT_METHOD_LIST = new ArrayList<StatusBean<Integer, String>>();
		INPUT_METHOD_LIST.add(new StatusBean<Integer, String>(INPUT_METHOD_TEXT, "文本"));
		INPUT_METHOD_LIST.add(new StatusBean<Integer, String>(INPUT_METHOD_SELECT, "选择框"));
		INPUT_METHOD_LIST.add(new StatusBean<Integer, String>(INPUT_METHOD_DATE, "日期"));
		INPUT_METHOD_LIST.add(new StatusBean<Integer, String>(INPUT_METHOD_CHECKBOX, "复选框"));
		INPUT_METHOD_LIST.add(new StatusBean<Integer, String>(INPUT_METHOD_TEXTAREA, "文本框"));
		INPUT_METHOD_LIST.add(new StatusBean<Integer, String>(INPUT_METHOD_BOX, "弹窗选择"));
	}

	private static void initInputMethodMap() {
		INPUT_METHOD_MAP = StatusKit.toMap(INPUT_METHOD_LIST);
	}

	private static void initFieldTypeList() {
		FIELD_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		FIELD_TYPE_LIST.add(new StatusBean<Integer, String>(FIELD_TYPE_DATE, "日期"));
		FIELD_TYPE_LIST.add(new StatusBean<Integer, String>(FIELD_TYPE_BOOLEAN, "布尔值"));
		FIELD_TYPE_LIST.add(new StatusBean<Integer, String>(FIELD_TYPE_TEXT, "文本"));
		FIELD_TYPE_LIST.add(new StatusBean<Integer, String>(FIELD_TYPE_NUMBER, "数值"));
	}

	private static void initFieldTypeMap() {
		FIELD_TYPE_MAP = StatusKit.toMap(FIELD_TYPE_LIST);
	}
	
	private static void initPropertyModifiList() {
		PROPERTY_MODIFI_LIST = new ArrayList<StatusBean<Integer, String>>();
		PROPERTY_MODIFI_LIST.add(new StatusBean<Integer, String>(PROPERTY_MODIFIABLE, "可修改"));
		PROPERTY_MODIFI_LIST.add(new StatusBean<Integer, String>(PROPERTY_UNMODIFIABLE, "不可修改"));
	}

	private static void initPropertyModifiMap() {
		PROPERTY_MODIFI_MAP = StatusKit.toMap(PROPERTY_MODIFI_LIST);
		
	}
}
