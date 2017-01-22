package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.business.ProductItemBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class ProductItemCache {

	private static Logger log = LoggerFactory.getLogger(ProductItemCache.class);

	// 从缓存中获取
	public ProductItemBean getProductItemByKey(int productItemId) {
		ProductItemBean bean = null;
		String key = PdmCacheConfig.getProductItemCacheKey(productItemId);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get ProductItemBean from remote cache");
			bean = JsonKit.toBean((String) obj, ProductItemBean.class);
		} else {
			log.debug("set ProductItemBean to remote cache");
			ProductItemBusiness business = new ProductItemBusiness();
			bean = business.getProductItemByKey(productItemId);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	public ProductItemBean getProductItemByNumber(String productItemNumber) {
		ProductItemBean bean = null;
		String key = PdmCacheConfig.getProductItemCacheNumber(productItemNumber);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get ProductItemBean from remote cache");
			bean = JsonKit.toBean((String) obj, ProductItemBean.class);
		} else {
			log.debug("set ProductItemBean to remote cache");
			ProductItemBusiness business = new ProductItemBusiness();
			bean = business.getProductItemByNumber(productItemNumber);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}
	// 刷新缓存
	public Boolean refreshProductItemCacheByKey(int productItemId) {
		String key = PdmCacheConfig.getProductItemCacheKey(productItemId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}
	
	public Boolean refreshProductItemCacheByNumber(String productItemNumber) {
		String key = PdmCacheConfig.getProductItemCacheNumber(productItemNumber);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
