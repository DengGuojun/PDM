package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.BrandInfoBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.bean.ProductTypePropertyBean;
import com.lpmas.pdm.cache.ProductInfoCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.config.ProductConfig;
import com.lpmas.pdm.config.ProductTypeConfig;
import com.lpmas.pdm.dao.ProductInfoDao;

public class ProductInfoBusiness {
	public int addProductInfo(ProductInfoBean bean) {
		ProductInfoDao dao = new ProductInfoDao();
		int result = dao.insertProductInfo(bean);
		if (result > 0) {
			bean.setProductId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getProductId(),
					LogInfoConfig.LOG_PRODUCT_INFO);
			refreshCache(bean);
		}
		return result;
	}

	public int updateProductInfo(ProductInfoBean bean) {
		ProductInfoDao dao = new ProductInfoDao();
		ProductInfoBean originalBean = dao.getProductInfoByKey(bean.getProductId());
		int result = dao.updateProductInfo(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getProductId(),
					LogInfoConfig.LOG_PRODUCT_INFO);
			refreshCache(bean);
		}
		return result;
	}

	public Boolean refreshCache(ProductInfoBean bean) {
		ProductInfoCache cache = new ProductInfoCache();
		return cache.refreshProductInfoCacheByKey(bean.getProductId());
	}

	public ProductInfoBean getProductInfoByKey(int productId) {
		ProductInfoDao dao = new ProductInfoDao();
		return dao.getProductInfoByKey(productId);
	}

	public ProductInfoBean getProductInfoByNumber(String productNumber) {
		ProductInfoDao dao = new ProductInfoDao();
		return dao.getProductInfoByNumber(productNumber);
	}

	public PageResultBean<ProductInfoBean> getProductInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ProductInfoDao dao = new ProductInfoDao();
		return dao.getProductInfoPageListByMap(condMap, pageBean);
	}

	public List<ProductInfoBean> getProductInfoListByBandId(int brandId) {
		ProductInfoDao dao = new ProductInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("brandId", String.valueOf(brandId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getProductInfoListByMap(condMap);
	}

	public String generateProductNumber(ProductInfoBean bean, String productCode, String sellingType, String version) {
		String productNumber = "";
		int typeId1 = bean.getTypeId1();
		int typeId2 = bean.getTypeId2();
		ProductTypeBusiness typeBusiness = new ProductTypeBusiness();
		ProductTypeBean typeBean1 = typeBusiness.getProductTypeByKey(typeId1);
		ProductTypeBean typeBean2 = typeBusiness.getProductTypeByKey(typeId2);
		ProductTypePropertyBusiness typePropertyBusiness = new ProductTypePropertyBusiness();
		ProductTypePropertyBean typePropertyBean = typePropertyBusiness.getProductTypePropertyByKey(typeId2,
				ProductTypeConfig.NUMBER_RULE);
		String numberRule = "";
		// 如果子类型的编码规则没有找到，则找父类型的编码规则
		if (typePropertyBean != null && StringKit.isValid(typePropertyBean.getPropertyValue())) {
			numberRule = typePropertyBean.getPropertyValue();
		} else {
			typePropertyBean = typePropertyBusiness.getProductTypePropertyByKey(typeId1, ProductTypeConfig.NUMBER_RULE);
			if (typePropertyBean != null) {
				numberRule = typePropertyBean.getPropertyValue();
			}
		}
		// 如果都没有找到，则采用默认的编码规则
		if (typePropertyBean == null) {
			numberRule = ProductConfig.NUMBER_RULE_DEFAULT_VALUE;
		}
		if (typeBean1 != null && typeBean2 != null) {
			BrandInfoBusiness brandInfoBusiness = new BrandInfoBusiness();
			BrandInfoBean brandInfoBean = brandInfoBusiness.getBrandInfoByKey(bean.getBrandId());
			productNumber = numberRule.replace("{PRODUCT_TYPE_1_NUMBER}", typeBean1.getTypeCode())
					.replace("{BRAND_CODE}", brandInfoBean.getBrandCode())
					.replace("{PRODUCT_TYPE_2_NUMBER}", typeBean2.getTypeCode())
					.replace("{PRODUCT_CODE}", productCode)
					.replace("{SELLING_TYPE}", sellingType)
					.replace("{VERSION}", version);
		}
		return productNumber;
	}

	public ReturnMessageBean verifyProductInfo(ProductInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getProductNumber())) {
			result.setMessage("商品编码生成错误");
		} else if (isDuplicateProductNumber(bean.getProductId(), bean.getProductNumber())) {
			result.setMessage("已存在相同的商品编码");
		} else if (bean.getProductNumber().length() > 50) {
			result.setMessage("商品项编码不能大于50字");
		} else if (!StringKit.isValid(bean.getProductName())) {
			result.setMessage("商品名称必须填写");
		} else if (bean.getBrandId() == 0) {
			result.setMessage("品牌必须填写");
		} else if (!StringKit.isValid(bean.getQualityLevel())) {
			result.setMessage("质量等级必须填写");
		} else if (bean.getProductName().length() > 30) {
			result.setMessage("商品名称不能大于30字");
		} else if (!StringKit.isNull(bean.getMemo()) && bean.getMemo().length() > 1000) {
			result.setMessage("商品备注不能大于1000字");
		}
		if (bean.getStatus() == Constants.STATUS_NOT_VALID && bean.getProductId() > 0) {
			ProductItemBusiness itemBusiness = new ProductItemBusiness();
			List<ProductItemBean> itemList = itemBusiness.getProductItemListByProductId(bean.getProductId());
			if (!itemList.isEmpty()) {
				result.setMessage("该商品下包含商品项，不能设置为无效");
			}
		}
		// 检查关联的对象是否有效
		BrandInfoBusiness brandInfoBusiness = new BrandInfoBusiness();
		BrandInfoBean brandInfoBean = brandInfoBusiness.getBrandInfoByKey(bean.getBrandId());
		if (brandInfoBean == null || brandInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
			result.setMessage("品牌信息无效");
		}
		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		ProductTypeBean typeBean = productTypeBusiness.getProductTypeByKey(bean.getTypeId2());
		if (typeBean == null || typeBean.getStatus() == Constants.STATUS_NOT_VALID) {
			result.setMessage("商品类型无效");
		}
		return result;
	}

	public boolean isDuplicateProductNumber(int productId, String productNumber) {
		ProductInfoBean bean = getProductInfoByNumber(productNumber);
		if (bean == null) {
			return false;
		} else {
			if (productId > 0 && productId == bean.getProductId()) {
				return false;
			}
		}
		return true;
	}
}