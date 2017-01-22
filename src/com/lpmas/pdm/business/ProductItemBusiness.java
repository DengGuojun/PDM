package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductTypePropertyBean;
import com.lpmas.pdm.cache.ProductItemCache;
import com.lpmas.pdm.cache.WareInfoCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.config.ProductConfig;
import com.lpmas.pdm.config.ProductTypeConfig;
import com.lpmas.pdm.dao.ProductItemDao;

public class ProductItemBusiness {
	public int addProductItem(ProductItemBean bean) {
		ProductItemDao dao = new ProductItemDao();
		int result = dao.insertProductItem(bean);
		if (result > 0) {
			bean.setItemId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, bean.getItemId(),
					LogInfoConfig.LOG_PRODUCT_ITEM);
			refreshCache(bean);
		}
		return result;
	}

	public int updateProductItem(ProductItemBean bean) {
		ProductItemDao dao = new ProductItemDao();
		ProductItemBean originalBean = dao.getProductItemByKey(bean.getItemId());
		int result = dao.updateProductItem(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, bean.getItemId(),
					LogInfoConfig.LOG_PRODUCT_ITEM);
			refreshCache(bean);
		}
		return result;
	}

	public Boolean refreshCache(ProductItemBean bean) {
		ProductItemCache cache = new ProductItemCache();
		WareInfoCache wareInfoCache = new WareInfoCache();
		wareInfoCache.refresWareInfoCacheByKey(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, bean.getItemId());
		wareInfoCache.refresWareInfoCacheByKey(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, bean.getItemNumber());
		return cache.refreshProductItemCacheByKey(bean.getItemId());
	}

	public ProductItemBean getProductItemByKey(int itemId) {
		ProductItemDao dao = new ProductItemDao();
		return dao.getProductItemByKey(itemId);
	}

	public ProductItemBean getProductItemByNumber(String itemNumber) {
		ProductItemDao dao = new ProductItemDao();
		return dao.getProductItemByNumber(itemNumber);
	}

	public List<ProductItemBean> getProductItemListByBarcode(String barcode) {
		ProductItemDao dao = new ProductItemDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("barcode", barcode);
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getProductItemListByMap(condMap);
	}

	public List<ProductItemBean> getProductItemListByProductId(int productId) {
		ProductItemDao dao = new ProductItemDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("productId", String.valueOf(productId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getProductItemListByMap(condMap);
	}

	public PageResultBean<ProductItemBean> getProductItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ProductItemDao dao = new ProductItemDao();
		return dao.getProductItemPageListByMap(condMap, pageBean);
	}

	public String generateItemNumber(ProductItemBean bean, ProductInfoBean productInfoBean) {
		String itemNumber = "";
		int typeId1 = productInfoBean.getTypeId1();
		int typeId2 = productInfoBean.getTypeId2();
		ProductTypePropertyBusiness typePropertyBusiness = new ProductTypePropertyBusiness();
		ProductTypePropertyBean typePropertyBean = typePropertyBusiness.getProductTypePropertyByKey(typeId2,
				ProductTypeConfig.ITEM_NUMBER_RULE);
		String numberRule = "";
		// 如果子类型的编码规则没有找到，则找父类型的编码规则
		if (typePropertyBean != null && StringKit.isValid(typePropertyBean.getPropertyValue())) {
			numberRule = typePropertyBean.getPropertyValue();
		} else {
			typePropertyBean = typePropertyBusiness.getProductTypePropertyByKey(typeId1,
					ProductTypeConfig.ITEM_NUMBER_RULE);
			if (typePropertyBean != null) {
				numberRule = typePropertyBean.getPropertyValue();
			}
		}
		// 如果都没有找到，则采用默认的编码规则
		if (typePropertyBean == null) {
			numberRule = ProductConfig.ITEM_NUMBER_RULE_DEFAULT_VALUE;
		}

		itemNumber = numberRule.replace("{PRODUCT_NUMBER}", productInfoBean.getProductNumber())
				.replace("{SPECIFICATION}", bean.getSpecification());
		return itemNumber;
	}

	public ReturnMessageBean verifyProductItem(ProductItemBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getItemNumber())) {
			result.setMessage("商品项编码生成错误");
		} else if (isDuplicateItemNumber(bean.getItemId(), bean.getItemNumber())) {
			result.setMessage("已存在相同的商品项编码");
		} else if (bean.getItemNumber().length() > 50) {
			result.setMessage("商品项编码不能大于50字");
		} else if (!StringKit.isValid(bean.getItemName())) {
			result.setMessage("商品名称必须填写");
		} else if (!StringKit.isValid(bean.getSpecification())) {
			result.setMessage("规格必须填写");
		} else if (!StringKit.isValid(bean.getSpecificationDesc())) {
			result.setMessage("规格描述必须填写");
		} else if (!StringKit.isValid(bean.getUnit())) {
			result.setMessage("计量单位必须填写");
		} else if (!StringKit.isValid(bean.getBarcode())) {
			result.setMessage("条形码必须填写");
		} else if (bean.getItemName().length() > 30) {
			result.setMessage("商品名称不能大于30字");
		} else if (!StringKit.isNull(bean.getMemo()) && bean.getMemo().length() > 1000) {
			result.setMessage("商品备注不能大于1000字");
		} else if (bean.getGuaranteePeriod() <= 0f && bean.getGuaranteePeriod() != -1f) {
			result.setMessage("保质期必须大于0");
		}
		ProductInfoBusiness productInfoBusiness = new ProductInfoBusiness();
		ProductInfoBean productInfoBean = productInfoBusiness.getProductInfoByKey(bean.getProductId());
		if (productInfoBean == null || productInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
			result.setMessage("商品信息无效");
		}

		return result;
	}

	public boolean isDuplicateItemNumber(int itemId, String itemNumber) {
		ProductItemBean bean = getProductItemByNumber(itemNumber);
		if (bean == null) {
			return false;
		} else {
			if (itemId > 0 && itemId == bean.getItemId()) {
				return false;
			}
		}
		return true;
	}

}