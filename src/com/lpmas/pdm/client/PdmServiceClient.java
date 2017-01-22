package com.lpmas.pdm.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.component.ComponentClient;
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
import com.lpmas.pdm.component.PdmServicePrx;
import com.lpmas.pdm.config.PdmClientConfig;
import com.lpmas.pdm.config.PdmConfig;

public class PdmServiceClient {

	private static Logger log = LoggerFactory.getLogger(PdmServiceClient.class);

	private String rpc(String method, String params) {
		ComponentClient client = new ComponentClient();
		PdmServicePrx pdmService = (PdmServicePrx) client.getProxy(PdmConfig.APP_ID, PdmServicePrx.class);
		String result = pdmService.rpc(method, params);
		log.debug("result : {}", result);
		return result;
	}

	public MaterialInfoBean getMaterialInfoByKey(int materialId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("materialId", String.valueOf(materialId));
		MaterialInfoBean materialInfoBean = JsonKit
				.toBean(rpc(PdmClientConfig.GET_MATERIAL_BY_KEY, JsonKit.toJson(param)), MaterialInfoBean.class);
		return materialInfoBean.getMaterialId() == materialId ? materialInfoBean : null;
	}

	public ProductItemBean getProductItemByKey(int productItemId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("productItemId", String.valueOf(productItemId));
		ProductItemBean productItemBean = JsonKit
				.toBean(rpc(PdmClientConfig.GET_PRODUCT_ITEM_BY_KEY, JsonKit.toJson(param)), ProductItemBean.class);
		return productItemBean.getItemId() == productItemId ? productItemBean : null;
	}

	public ProductInfoBean getProductInfoByKey(int productId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("productId", String.valueOf(productId));
		ProductInfoBean ProductInfoBean = JsonKit
				.toBean(rpc(PdmClientConfig.GET_PRODUCT_INFO_BY_KEY, JsonKit.toJson(param)), ProductInfoBean.class);
		return ProductInfoBean.getProductId() == productId ? ProductInfoBean : null;
	}

	public MaterialTypeBean getMaterialTypeByKey(int typeId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("typeId", String.valueOf(typeId));
		MaterialTypeBean materialTypeBean = JsonKit
				.toBean(rpc(PdmClientConfig.GET_MATERIAL_TYPE_BY_KEY, JsonKit.toJson(param)), MaterialTypeBean.class);
		return materialTypeBean.getTypeId() == typeId ? materialTypeBean : null;
	}

	public ProductTypeBean getProductTypeByKey(int typeId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("typeId", String.valueOf(typeId));
		ProductTypeBean productTypeBean = JsonKit
				.toBean(rpc(PdmClientConfig.GET_PRODUCT_TYPE_BY_KEY, JsonKit.toJson(param)), ProductTypeBean.class);
		return productTypeBean.getTypeId() == typeId ? productTypeBean : null;
	}

	public ProductImageBean getProductItemImageByProductItemId(int productItemId, String imgType) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(productItemId));
		paramMap.put("infoType", InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM + "");
		paramMap.put("imgType", imgType);
		String remoteResult = rpc(PdmClientConfig.GET_PRODUCT_ITEM_IMG_BY_KEY, JsonKit.toJson(paramMap));
		ProductImageBean productImageBean = JsonKit.toBean(remoteResult, ProductImageBean.class);
		return productImageBean.getInfoId() == productItemId ? productImageBean : null;
	}

	public MaterialImageBean getMaterialImageByMaterialId(int materialId, String imgType) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(materialId));
		paramMap.put("imgType", imgType);
		String remoteResult = rpc(PdmClientConfig.GET_MATERIAL_IMG_BY_KEY, JsonKit.toJson(paramMap));
		MaterialImageBean materialImageBean = JsonKit.toBean(remoteResult, MaterialImageBean.class);
		return materialImageBean.getMaterialId() == materialId ? materialImageBean : null;
	}

	public List<UnitTypeBean> getUnitTypeAllList() {
		String remoteResult = rpc(PdmClientConfig.GET_UNITTYPE_ALL_LIST, "");
		return JsonKit.toList(remoteResult, UnitTypeBean.class);
	}

	public List<UnitInfoBean> getUnitInfoListByTypeKey(int unitTypeId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitTypeId", String.valueOf(unitTypeId));
		String remoteResult = rpc(PdmClientConfig.GET_UNITINFO_LIST_BY_TYPE_ID, JsonKit.toJson(paramMap));
		return JsonKit.toList(remoteResult, UnitInfoBean.class);
	}

	public UnitInfoBean getUnitInfoByCode(String unitCode) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitCode", unitCode);
		String remoteResult = rpc(PdmClientConfig.GET_UNITINFO_BY_CODE, JsonKit.toJson(paramMap));
		UnitInfoBean unitInfoBean = JsonKit.toBean(remoteResult, UnitInfoBean.class);
		return unitInfoBean.getUnitCode().equals(unitCode) ? unitInfoBean : null;
	}

	public MaterialPropertyBean getMaterialPropertyByKey(int materialId, String propertyCode) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("materialId", String.valueOf(materialId));
		paramMap.put("propertyCode", propertyCode);
		String remoteResult = rpc(PdmClientConfig.GET_MATERIAL_PROPERTY_BY_KEY, JsonKit.toJson(paramMap));
		MaterialPropertyBean materialPropertyBean = JsonKit.toBean(remoteResult, MaterialPropertyBean.class);
		return materialPropertyBean.getMaterialId() == materialId ? materialPropertyBean : null;
	}

	public ProductPropertyBean getProductPropertyByKey(int infoId, int infoType, String propertyCode) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("infoId", String.valueOf(infoId));
		paramMap.put("infoType", String.valueOf(infoType));
		paramMap.put("propertyCode", propertyCode);
		String remoteResult = rpc(PdmClientConfig.GET_PRODUCT_PROPERTY_BY_KEY, JsonKit.toJson(paramMap));
		ProductPropertyBean productPropertyBean = JsonKit.toBean(remoteResult, ProductPropertyBean.class);
		return productPropertyBean.getInfoId() == infoId ? productPropertyBean : null;
	}

	public MaterialInfoBean getMaterialInfoByNumber(String materialNumber) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("materialNumber", materialNumber);
		MaterialInfoBean materialInfoBean = JsonKit
				.toBean(rpc(PdmClientConfig.GET_MATERIAL_BY_NUMBER, JsonKit.toJson(param)), MaterialInfoBean.class);
		return materialInfoBean.getMaterialNumber().equals(materialNumber) ? materialInfoBean : null;
	}

	public ProductItemBean getProductItemByNumber(String productItemNumber) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("productItemNumber", productItemNumber);
		ProductItemBean productItemBean = JsonKit
				.toBean(rpc(PdmClientConfig.GET_PRODUCT_ITEM_BY_NUMBER, JsonKit.toJson(param)), ProductItemBean.class);
		return productItemBean.getItemNumber().equals(productItemNumber) ? productItemBean : null;
	}

	public WareInfoBean getWareInfoByKey(int wareType, int wareId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("wareId", String.valueOf(wareId));
		param.put("wareType", String.valueOf(wareType));
		WareInfoBean wareInfoBean = JsonKit.toBean(rpc(PdmClientConfig.GET_WAREINFO_BY_KEY, JsonKit.toJson(param)),
				WareInfoBean.class);
		return wareInfoBean.getWareId() == wareId ? wareInfoBean : null;
	}

	public WareInfoBean getWareInfoByKey(int wareType, String wareNumber) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("wareNumber", wareNumber);
		param.put("wareType", String.valueOf(wareType));
		WareInfoBean wareInfoBean = JsonKit.toBean(rpc(PdmClientConfig.GET_WAREINFO_BY_NUMBER, JsonKit.toJson(param)),
				WareInfoBean.class);
		return wareInfoBean.getWareNumber().equals(wareNumber) ? wareInfoBean : null;
	}

}
