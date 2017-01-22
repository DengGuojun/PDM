package com.lpmas.pdm.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StatusKit;

public class LogInfoConfig {

	// 品牌操作日志配置项
	public static String LOG_BRAND_INFO = "BRAND_INFO";
	public static String LOG_BRAND_PROPERTY = "BRAND_PROPERTY";
	public static List<StatusBean<String, String>> LOG_BRAND_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> LOG_BRAND_MAP = new HashMap<String, String>();

	// 物料操作日志配置项
	public static String LOG_MATERIAL_TYPE = "MATERIAL_TYPE";
	public static String LOG_MATERIAL_TYPE_PROPERTY = "MATERIAL_TYPE_PROPERTY";
	public static String LOG_MATERIAL_INFO = "MATERIAL_INFO";
	public static String LOG_MATERIAL_PROPERTY_TYPE = "MATERIAL_PROPERTY_TYPE";
	public static String LOG_MATERIAL_PROPERTY_TYPE_OPTION = "MATERIAL_PROPERTY_TYPE_OPTION";
	public static String LOG_MATERIAL_PROPERTY = "MATERIAL_PROPERTY";
	public static String LOG_MATERIAL_IMAGE = "MATERIAL_IMAGE";
	public static List<StatusBean<String, String>> LOG_MATERIAL_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> LOG_MATERIAL_MAP = new HashMap<String, String>();

	// 商品操作日志配置项
	public static String LOG_FILE_INFO = "FILE_INFO";
	public static String LOG_PRODUCT_TYPE = "PRODUCT_TYPE";
	public static String LOG_PRODUCT_TYPE_PROPERTY = "PRODUCT_TYPE_PROPERTY";
	public static String LOG_PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";
	public static String LOG_PRODUCT_INFO = "PRODUCT_INFO";
	public static String LOG_PRODUCT_ITEM = "PRODUCT_ITEM";
	public static String LOG_PRODUCT_PROPERTY_TYPE = "PRODUCT_PROPERTY_TYPE";
	public static String LOG_PRODUCT_PROPERTY_TYPE_OPTION = "PRODUCT_PROPERTY_TYPE_OPTION";
	public static String LOG_PRODUCT_PROPERTY = "PRODUCT_PROPERTY";
	public static String LOG_PRODUCT_MULTIPLE_PROPERTY = "PRODUCT_MULTIPLE_PROPERTY";
	public static String LOG_PRODUCT_IMAGE = "PRODUCT_IMAGE";
	public static List<StatusBean<String, String>> LOG_PRODUCT_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> LOG_PRODUCT_MAP = new HashMap<String, String>();

	// 单位信息操作日志配置项
	public static String LOG_UNIT_TYPE = "UNIT_TYPE";
	public static String LOG_UNIT_INFO = "UNIT_INFO";
	public static List<StatusBean<String, String>> LOG_UNIT_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> LOG_UNIT_MAP = new HashMap<String, String>();

	// 条形码操作日志配置项
	public static String LOG_BARCODE_INFO = "BARCODE_INFO";
	public static List<StatusBean<String, String>> LOG_BARCODE_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> LOG_BARCODE_MAP = new HashMap<String, String>();

	// 总的日志类型配置列表
	public static List<StatusBean<String, String>> LOG_TYPE_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_TYPE_MAP = new HashMap<String, String>();

	// 总的日志类型信息配置列表
	public static List<StatusBean<Integer, String>> LOG_TYPE_INFO_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static Map<Integer, String> LOG_TYPE_INFO_MAP = new HashMap<Integer, String>();

	static {
		initLogBrandList();
		initLogBrandMap();
		initLogMaterialList();
		initLogMaterialMap();
		initLogProductList();
		initLogProductMap();
		initLogUnitList();
		initLogUnitMap();
		initLogBarcodeList();
		initLogBarcodeMap();
		initLogTypeList();
		initLogTypeMap();
		initLogTypeInfoList();
		initLogTypeInfoMap();
	}

	private static void initLogBrandList() {
		LOG_BRAND_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_BRAND_LIST.add(new StatusBean<String, String>(LOG_BRAND_INFO, "品牌信息"));
		LOG_BRAND_LIST.add(new StatusBean<String, String>(LOG_BRAND_PROPERTY, "品牌属性"));
	}

	private static void initLogBrandMap() {
		LOG_BRAND_MAP = StatusKit.toMap(LOG_BRAND_LIST);
	}

	private static void initLogMaterialList() {
		LOG_MATERIAL_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_MATERIAL_LIST.add(new StatusBean<String, String>(LOG_MATERIAL_TYPE, "物料类型"));
		LOG_MATERIAL_LIST.add(new StatusBean<String, String>(LOG_MATERIAL_TYPE_PROPERTY, "物料类型属性"));
		LOG_MATERIAL_LIST.add(new StatusBean<String, String>(LOG_MATERIAL_INFO, "物料信息"));
		LOG_MATERIAL_LIST.add(new StatusBean<String, String>(LOG_MATERIAL_PROPERTY_TYPE, "物料属性配置"));
		LOG_MATERIAL_LIST.add(new StatusBean<String, String>(LOG_MATERIAL_PROPERTY_TYPE_OPTION, "物料属性配置选项"));
		LOG_MATERIAL_LIST.add(new StatusBean<String, String>(LOG_MATERIAL_PROPERTY, "物料属性"));
		LOG_MATERIAL_LIST.add(new StatusBean<String, String>(LOG_MATERIAL_IMAGE, "物料图片"));
	}

	private static void initLogMaterialMap() {
		LOG_MATERIAL_MAP = StatusKit.toMap(LOG_MATERIAL_LIST);
	}

	private static void initLogProductList() {
		LOG_PRODUCT_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_TYPE, "商品类型"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_TYPE_PROPERTY, "商品类型属性"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_DESCRIPTION, "商品描述"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_INFO, "商品信息"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_ITEM, "商品项信息"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_PROPERTY_TYPE, "商品属性配置"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_PROPERTY_TYPE_OPTION, "商品属性配置选项"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_PROPERTY, "商品属性"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_MULTIPLE_PROPERTY, "商品多重属性"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_PRODUCT_IMAGE, "商品图片"));
		LOG_PRODUCT_LIST.add(new StatusBean<String, String>(LOG_FILE_INFO, "图片"));
	}

	private static void initLogProductMap() {
		LOG_PRODUCT_MAP = StatusKit.toMap(LOG_PRODUCT_LIST);
	}

	private static void initLogUnitList() {
		LOG_UNIT_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_UNIT_LIST.add(new StatusBean<String, String>(LOG_UNIT_TYPE, "单位类型"));
		LOG_UNIT_LIST.add(new StatusBean<String, String>(LOG_UNIT_INFO, "单位信息"));
	}

	private static void initLogUnitMap() {
		LOG_UNIT_MAP = StatusKit.toMap(LOG_UNIT_LIST);
	}

	private static void initLogBarcodeList() {
		LOG_BARCODE_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_BARCODE_LIST.add(new StatusBean<String, String>(LOG_BARCODE_INFO, "条形码信息"));
	}

	private static void initLogBarcodeMap() {
		LOG_BARCODE_MAP = StatusKit.toMap(LOG_BARCODE_LIST);
	}

	public static void initLogTypeList() {
		LOG_TYPE_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_TYPE_LIST = ListKit.combineList(LOG_BRAND_LIST, LOG_MATERIAL_LIST);
		LOG_TYPE_LIST = ListKit.combineList(LOG_TYPE_LIST, LOG_PRODUCT_LIST);
		LOG_TYPE_LIST = ListKit.combineList(LOG_TYPE_LIST, LOG_UNIT_LIST);
		LOG_TYPE_LIST = ListKit.combineList(LOG_TYPE_LIST, LOG_BARCODE_LIST);
	}

	public static void initLogTypeMap() {
		LOG_TYPE_MAP = StatusKit.toMap(LOG_TYPE_LIST);
	}

	public static void initLogTypeInfoList() {
		LOG_TYPE_INFO_LIST = new ArrayList<StatusBean<Integer, String>>();
		LOG_TYPE_INFO_LIST.add(new StatusBean<Integer, String>(Integer.valueOf(InfoTypeConfig.INFO_TYPE_BRAND), "品牌"));
		LOG_TYPE_INFO_LIST
				.add(new StatusBean<Integer, String>(Integer.valueOf(InfoTypeConfig.INFO_TYPE_MATERIAL), "物料"));
		LOG_TYPE_INFO_LIST
				.add(new StatusBean<Integer, String>(Integer.valueOf(InfoTypeConfig.INFO_TYPE_PRODUCT), "商品"));
		LOG_TYPE_INFO_LIST
				.add(new StatusBean<Integer, String>(Integer.valueOf(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM), "商品项"));
		LOG_TYPE_INFO_LIST.add(new StatusBean<Integer, String>(Integer.valueOf(InfoTypeConfig.INFO_TYPE_UNIT), "单位"));
		LOG_TYPE_INFO_LIST
				.add(new StatusBean<Integer, String>(Integer.valueOf(InfoTypeConfig.INFO_TYPE_BARCODE), "条形码"));
	}

	public static void initLogTypeInfoMap() {
		LOG_TYPE_INFO_MAP = StatusKit.toMap(LOG_TYPE_INFO_LIST);
	}

}
