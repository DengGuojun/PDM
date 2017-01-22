package com.lpmas.pdm.business;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.bean.WareInfoBean;

public class WareInfoBusniess {

	public WareInfoBean getWareInfoByKey(int wareType, int wareId) {
		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			return getWareInfoByKeyForMaterial(wareId);
		} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			return getWareInfoByKeyForProductItem(wareId);
		} else {
			return null;
		}
	}

	public WareInfoBean getWareInfoByKey(int wareType, String wareNumber) {
		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			return getWareInfoByNumberForMaterial(wareNumber);
		} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			return getWareInfoByNumberForProductItem(wareNumber);
		} else {
			return null;
		}
	}

	private WareInfoBean getWareInfoByKeyForProductItem(int wareId) {
		ProductItemBusiness productItemBusiness = new ProductItemBusiness();
		ProductItemBean productItemBean = productItemBusiness.getProductItemByKey(wareId);
		if (productItemBean == null)
			return null;
		return getWareInfoByProductItem(productItemBean);
	}

	private WareInfoBean getWareInfoByKeyForMaterial(int wareId) {
		MaterialInfoBusiness materialInfoBusiness = new MaterialInfoBusiness();
		MaterialInfoBean materialInfoBean = materialInfoBusiness.getMaterialInfoByKey(wareId);
		if (materialInfoBean == null)
			return null;
		return getWareInfoByMaterial(materialInfoBean);
	}

	private WareInfoBean getWareInfoByNumberForProductItem(String wareNumber) {
		ProductItemBusiness productItemBusiness = new ProductItemBusiness();
		ProductItemBean productItemBean = productItemBusiness.getProductItemByNumber(wareNumber);
		if (productItemBean == null)
			return null;
		return getWareInfoByProductItem(productItemBean);
	}

	private WareInfoBean getWareInfoByNumberForMaterial(String wareNumber) {
		MaterialInfoBusiness materialInfoBusiness = new MaterialInfoBusiness();
		MaterialInfoBean materialInfoBean = materialInfoBusiness.getMaterialInfoByNumber(wareNumber);
		if (materialInfoBean == null)
			return null;
		return getWareInfoByMaterial(materialInfoBean);
	}

	private WareInfoBean getWareInfoByMaterial(MaterialInfoBean materialInfoBean) {
		WareInfoBean result = new WareInfoBean();
		result.setWareType(InfoTypeConfig.INFO_TYPE_MATERIAL);
		result.setWareId(materialInfoBean.getMaterialId());
		result.setWareName(materialInfoBean.getMaterialName());
		result.setWareNumber(materialInfoBean.getMaterialNumber());
		result.setUnitCode(materialInfoBean.getUnit());
		result.setSpecification(materialInfoBean.getSpecification());
		result.setBarcode(materialInfoBean.getBarcode());

		// 处理类型
		String typeName = "";
		MaterialTypeBusiness materialTypeBusiness = new MaterialTypeBusiness();
		MaterialTypeBean materialTypeBean = materialTypeBusiness.getMaterialTypeByKey(materialInfoBean.getTypeId1());
		if (materialTypeBean != null)
			typeName += materialTypeBean.getTypeName() + "-";
		if (materialInfoBean.getTypeId2() > 0) {
			materialTypeBean = materialTypeBusiness.getMaterialTypeByKey(materialInfoBean.getTypeId2());
			if (materialTypeBean != null)
				typeName += materialTypeBean.getTypeName() + "-";
			if (materialInfoBean.getTypeId3() > 0) {
				materialTypeBean = materialTypeBusiness.getMaterialTypeByKey(materialInfoBean.getTypeId3());
				if (materialTypeBean != null)
					typeName += materialTypeBean.getTypeName();
			}
		}
		if (typeName.endsWith("-")) {
			typeName = typeName.substring(0, typeName.length() - 1);
		}
		result.setTypeName(typeName);

		// 处理单位
		UnitInfoBusiness unitInfoBusiness = new UnitInfoBusiness();
		UnitInfoBean unitInfoBean = unitInfoBusiness.getUnitInfoByCode(materialInfoBean.getUnit());
		if (unitInfoBean != null) {
			result.setUnitName(unitInfoBean.getUnitName());
			UnitTypeBusiness unitTypeBusiness = new UnitTypeBusiness();
			UnitTypeBean unitTypeBean = unitTypeBusiness.getUnitTypeByKey(unitInfoBean.getTypeId());
			if (unitTypeBean != null) {
				result.setUnitTypeName(unitTypeBean.getTypeName());
			}
		}
		return result;
	}

	private WareInfoBean getWareInfoByProductItem(ProductItemBean productItemBean) {
		ProductInfoBusiness productInfoBusiness = new ProductInfoBusiness();
		ProductInfoBean productInfoBean = productInfoBusiness.getProductInfoByKey(productItemBean.getProductId());
		if (productInfoBean == null)
			return null;

		WareInfoBean result = new WareInfoBean();
		result.setWareType(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM);
		result.setWareId(productItemBean.getItemId());
		result.setWareName(productItemBean.getItemName());
		result.setWareNumber(productItemBean.getItemNumber());
		result.setUnitCode(productItemBean.getUnit());
		result.setSpecification(productItemBean.getSpecification());
		result.setBarcode(productItemBean.getBarcode());
		result.setWareInfoId(productItemBean.getProductId());

		// 处理类型
		String typeName = "";
		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		ProductTypeBean productTypeBean = productTypeBusiness.getProductTypeByKey(productInfoBean.getTypeId1());
		if (productTypeBean != null)
			typeName += productTypeBean.getTypeName() + "-";
		if (productInfoBean.getTypeId2() > 0) {
			productTypeBean = productTypeBusiness.getProductTypeByKey(productInfoBean.getTypeId2());
			if (productTypeBean != null)
				typeName += productTypeBean.getTypeName() + "-";
			if (productInfoBean.getTypeId3() > 0) {
				productTypeBean = productTypeBusiness.getProductTypeByKey(productInfoBean.getTypeId3());
				if (productTypeBean != null)
					typeName += productTypeBean.getTypeName();
			}
		}
		if (typeName.endsWith("-")) {
			typeName = typeName.substring(0, typeName.length() - 1);
		}
		result.setTypeName(typeName);

		// 处理单位
		UnitInfoBusiness unitInfoBusiness = new UnitInfoBusiness();
		UnitInfoBean unitInfoBean = unitInfoBusiness.getUnitInfoByCode(productItemBean.getUnit());
		if (unitInfoBean != null) {
			result.setUnitName(unitInfoBean.getUnitName());
			UnitTypeBusiness unitTypeBusiness = new UnitTypeBusiness();
			UnitTypeBean unitTypeBean = unitTypeBusiness.getUnitTypeByKey(unitInfoBean.getTypeId());
			if (unitTypeBean != null) {
				result.setUnitTypeName(unitTypeBean.getTypeName());
			}
		}
		return result;
	}
}
