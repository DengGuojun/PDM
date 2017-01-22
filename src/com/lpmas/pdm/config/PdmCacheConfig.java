package com.lpmas.pdm.config;

import java.text.MessageFormat;

public class PdmCacheConfig {
	public static final String MATERIAL_INFO_KEY = "MATERIAL_INFO";
	public static final String MATERIAL_PROPERTY_KEY = "MATERIAL_PROPERTY";
	public static final String MATERIAL_TYPE_KEY = "MATERIAL_TYPE";
	public static final String MATERIAL_IMAGE_KEY = "MATERIAL_IMAGE";
	public static final String MATERIAL_INFO_NUMBER = "MATERIAL_INFO_NUMBER";

	public static final String PRODUCT_INFO_KEY = "PRODUCT_INFO";
	public static final String PRODUCT_ITEM_KEY = "PRODUCT_ITEM";
	public static final String PRODUCT_PROPERTY_KEY = "PRODUCT_PROPERTY";
	public static final String PRODUCT_TYPE_KEY = "PRODUCT_TYPE";
	public static final String PRODUCT_IMAGE_KEY = "PRODUCT_IMAGE";
	public static final String PRODUCT_ITEM_NUMBER = "PRODUCT_ITEM_NUMBER";

	public static final String UNIT_TYPE_ALL_LIST_KEY = "UNIT_TYPE_ALL_LIST";
	public static final String UNIT_INFO_LIST_KEY = "UNIT_INFO_LIST";
	public static final String UNIT_INFO_KEY = "UNIT_INFO";

	public static final String WARE_INFO_KEY = "WARE_INFO";
	public static final String WARE_INFO_NUMBER = "WARE_INFO_NUMBER";

	public static String getMaterialInfoCacheKey(int materialId) {
		return MessageFormat.format("{0}_{1}", MATERIAL_INFO_KEY, materialId);
	}

	public static String getMaterialInfoCacheNumber(String materialNumber) {
		return MessageFormat.format("{0}_{1}", MATERIAL_INFO_NUMBER, materialNumber);
	}

	public static String getMaterialPropertyCacheKey(int materialId, String propertyCode) {
		return MessageFormat.format("{0}_{1}_{2}", MATERIAL_PROPERTY_KEY, materialId, propertyCode);
	}

	public static String getMaterialTypeCacheKey(int typeId) {
		return MessageFormat.format("{0}_{1}", MATERIAL_TYPE_KEY, typeId);
	}

	public static String getMaterialImageCacheKey(int materialId, String imageType) {
		return MessageFormat.format("{0}_{1}_{2}", MATERIAL_IMAGE_KEY, materialId, imageType);
	}

	public static String getProductInfoCacheKey(int productId) {
		return MessageFormat.format("{0}_{1}", PRODUCT_INFO_KEY, productId);
	}

	public static String getProductItemCacheKey(int productItemId) {
		return MessageFormat.format("{0}_{1}", PRODUCT_ITEM_KEY, productItemId);
	}

	public static String getProductItemCacheNumber(String productItemNumber) {
		return MessageFormat.format("{0}_{1}", PRODUCT_ITEM_NUMBER, productItemNumber);
	}

	public static String getProductPropertyCacheKey(int infoId, int infoType, String propertyCode) {
		return MessageFormat.format("{0}_{1}_{2}_{3}", PRODUCT_PROPERTY_KEY, infoId, infoType, propertyCode);
	}

	public static String getProductTypeCacheKey(int typeId) {
		return MessageFormat.format("{0}_{1}", PRODUCT_TYPE_KEY, typeId);
	}

	public static String getProductImageCacheKey(int infoId, int infoType, String imageType) {
		return MessageFormat.format("{0}_{1}_{2}_{3}", PRODUCT_IMAGE_KEY, infoId, infoType, imageType);
	}

	public static String getUnitTypeAllListCacheKey() {
		return UNIT_TYPE_ALL_LIST_KEY;
	}

	public static String getUnitInfoListCacheKey(int unitTypeId) {
		return MessageFormat.format("{0}_{1}", UNIT_INFO_LIST_KEY, unitTypeId);
	}

	public static String getUnitInfoCacheKey(String unitCode) {
		return MessageFormat.format("{0}_{1}", UNIT_INFO_KEY, unitCode);
	}

	public static String getWareInfoCacheKey(int wareType, int wareId) {
		return MessageFormat.format("{0}_{1}_{2}", WARE_INFO_KEY, wareType, wareId);
	}

	public static String getWareInfoCacheKey(int wareType, String wareNumber) {
		return MessageFormat.format("{0}_{1}_{2}", WARE_INFO_NUMBER, wareType, wareNumber);
	}
}
