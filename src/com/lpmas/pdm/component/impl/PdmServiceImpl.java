package com.lpmas.pdm.component.impl;

import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialImageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.bean.WareInfoBean;
import com.lpmas.pdm.cache.MaterialImageCache;
import com.lpmas.pdm.cache.MaterialInfoCache;
import com.lpmas.pdm.cache.MaterialPropertyCache;
import com.lpmas.pdm.cache.MaterialTypeCache;
import com.lpmas.pdm.cache.ProductImageCache;
import com.lpmas.pdm.cache.ProductInfoCache;
import com.lpmas.pdm.cache.ProductItemCache;
import com.lpmas.pdm.cache.ProductPropertyCache;
import com.lpmas.pdm.cache.ProductTypeCache;
import com.lpmas.pdm.cache.UnitInfoCache;
import com.lpmas.pdm.cache.UnitTypeCache;
import com.lpmas.pdm.cache.WareInfoCache;
import com.lpmas.pdm.component._PdmServiceDisp;
import com.lpmas.pdm.config.PdmClientConfig;

import Ice.Current;

public class PdmServiceImpl extends _PdmServiceDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String getProductItemInfoByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		ProductItemCache cache = new ProductItemCache();
		ProductItemBean bean = cache.getProductItemByKey(Integer.valueOf(paramMap.get("productItemId")));
		if (bean == null) {
			bean = new ProductItemBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getMaterialInfoByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		MaterialInfoCache cache = new MaterialInfoCache();
		MaterialInfoBean bean = cache.getMaterialInfoByKey(Integer.valueOf(paramMap.get("materialId")));
		if (bean == null) {
			bean = new MaterialInfoBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getMaterialTypeByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		MaterialTypeCache cache = new MaterialTypeCache();
		MaterialTypeBean bean = cache.getMaterialTypeByKey(Integer.valueOf(paramMap.get("typeId")));
		if (bean == null) {
			bean = new MaterialTypeBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getProductTypeByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		ProductTypeCache cache = new ProductTypeCache();
		ProductTypeBean bean = cache.getProductTypeByKey(Integer.valueOf(paramMap.get("typeId")));
		if (bean == null) {
			bean = new ProductTypeBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getMaterialImageByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		MaterialImageCache cache = new MaterialImageCache();
		MaterialImageBean imageBean = cache.getMaterialImageByKey(Integer.valueOf(paramMap.get("id")),
				paramMap.get("imgType"));
		if (imageBean == null)
			imageBean = new MaterialImageBean();
		return JsonKit.toJson(imageBean);
	}

	private String getProductItemImageByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		ProductImageCache cache = new ProductImageCache();
		ProductImageBean imageBean = cache.getProductImageByKey(Integer.valueOf(paramMap.get("id")),
				Integer.valueOf(paramMap.get("infoType")), paramMap.get("imgType"));
		if (imageBean == null)
			imageBean = new ProductImageBean();
		return JsonKit.toJson(imageBean);
	}

	private String getProductInfoByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		ProductInfoCache cache = new ProductInfoCache();
		ProductInfoBean bean = cache.getProductInfoByKey(Integer.valueOf(paramMap.get("productId")));
		if (bean == null) {
			bean = new ProductInfoBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getUnitTypeAllList() {
		UnitTypeCache cache = new UnitTypeCache();
		List<UnitTypeBean> unitTypeList = cache.getUnitTypeAllList();
		return JsonKit.toJson(unitTypeList);
	}

	private String getUnitInfoListByTypeId(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		UnitInfoCache cache = new UnitInfoCache();
		List<UnitInfoBean> list = cache.getUnitInfoListByTypeId(Integer.valueOf(paramMap.get("unitTypeId")));
		return JsonKit.toJson(list);
	}

	private String getUnitInfoByCode(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		UnitInfoCache cache = new UnitInfoCache();
		UnitInfoBean bean = cache.getUnitInfoByCode(paramMap.get("unitCode"));
		if (bean == null) {
			bean = new UnitInfoBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getMaterialPropertyByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		int materialId = Integer.valueOf(paramMap.get("materialId"));
		String propertyCode = paramMap.get("propertyCode");
		MaterialPropertyCache cache = new MaterialPropertyCache();
		MaterialPropertyBean bean = cache.getMaterialPropertyByKey(materialId, propertyCode);
		if (bean == null) {
			bean = new MaterialPropertyBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getProductPropertyByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		int infoId = Integer.valueOf(paramMap.get("infoId"));
		int infoType = Integer.valueOf(paramMap.get("infoType"));
		String propertyCode = paramMap.get("propertyCode");
		ProductPropertyCache cache = new ProductPropertyCache();
		ProductPropertyBean bean = cache.getProductPropertyByKey(infoId, infoType, propertyCode);
		if (bean == null) {
			bean = new ProductPropertyBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getMaterialInfoByNumber(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		MaterialInfoCache cache = new MaterialInfoCache();
		MaterialInfoBean bean = cache.getMaterialInfoByNumber(paramMap.get("materialNumber"));
		if (bean == null) {
			bean = new MaterialInfoBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getProductItemInfoByNumber(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		ProductItemCache cache = new ProductItemCache();
		ProductItemBean bean = cache.getProductItemByNumber(paramMap.get("productItemNumber"));
		if (bean == null) {
			bean = new ProductItemBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getWareInfoByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		int wareId = Integer.valueOf(paramMap.get("wareId"));
		int wareType = Integer.valueOf(paramMap.get("wareType"));
		WareInfoCache cache = new WareInfoCache();
		WareInfoBean bean = cache.getWareInfoByKey(wareType, wareId);
		if (bean == null) {
			bean = new WareInfoBean();
		}
		return JsonKit.toJson(bean);
	}

	private String getWareInfoByNumber(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		String wareNumber = paramMap.get("wareNumber");
		int wareType = Integer.valueOf(paramMap.get("wareType"));
		WareInfoCache cache = new WareInfoCache();
		WareInfoBean bean = cache.getWareInfoByKey(wareType, wareNumber);
		if (bean == null) {
			bean = new WareInfoBean();
		}
		return JsonKit.toJson(bean);
	}

	@Override
	public String rpc(String method, String params, Current __current) {
		String result = "";
		if (method.equals(PdmClientConfig.GET_MATERIAL_BY_KEY)) {
			result = getMaterialInfoByKey(params);
		} else if (method.equals(PdmClientConfig.GET_PRODUCT_ITEM_BY_KEY)) {
			result = getProductItemInfoByKey(params);
		} else if (method.equals(PdmClientConfig.GET_MATERIAL_TYPE_BY_KEY)) {
			result = getMaterialTypeByKey(params);
		} else if (method.equals(PdmClientConfig.GET_PRODUCT_TYPE_BY_KEY)) {
			result = getProductTypeByKey(params);
		} else if (method.equals(PdmClientConfig.GET_PRODUCT_ITEM_IMG_BY_KEY)) {
			result = getProductItemImageByKey(params);
		} else if (method.equals(PdmClientConfig.GET_MATERIAL_IMG_BY_KEY)) {
			result = getMaterialImageByKey(params);
		} else if (method.equals(PdmClientConfig.GET_PRODUCT_INFO_BY_KEY)) {
			result = getProductInfoByKey(params);
		} else if (method.equals(PdmClientConfig.GET_UNITTYPE_ALL_LIST)) {
			result = getUnitTypeAllList();
		} else if (method.equals(PdmClientConfig.GET_UNITINFO_LIST_BY_TYPE_ID)) {
			result = getUnitInfoListByTypeId(params);
		} else if (method.equals(PdmClientConfig.GET_MATERIAL_PROPERTY_BY_KEY)) {
			result = getMaterialPropertyByKey(params);
		} else if (method.equals(PdmClientConfig.GET_PRODUCT_PROPERTY_BY_KEY)) {
			result = getProductPropertyByKey(params);
		} else if (method.equals(PdmClientConfig.GET_UNITINFO_BY_CODE)) {
			result = getUnitInfoByCode(params);
		} else if (method.equals(PdmClientConfig.GET_MATERIAL_BY_NUMBER)) {
			result = getMaterialInfoByNumber(params);
		} else if (method.equals(PdmClientConfig.GET_PRODUCT_ITEM_BY_NUMBER)) {
			result = getProductItemInfoByNumber(params);
		} else if (method.equals(PdmClientConfig.GET_WAREINFO_BY_KEY)) {
			result = getWareInfoByKey(params);
		} else if (method.equals(PdmClientConfig.GET_WAREINFO_BY_NUMBER)) {
			result = getWareInfoByNumber(params);
		}
		return result;
	}

}
