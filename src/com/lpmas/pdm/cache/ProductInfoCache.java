package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.business.ProductInfoBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class ProductInfoCache {

	private static Logger log = LoggerFactory.getLogger(ProductInfoCache.class);

	// 从缓存中获取
	public ProductInfoBean getProductInfoByKey(int productId) {
		ProductInfoBean bean = null;
		String key = PdmCacheConfig.getProductInfoCacheKey(productId);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get ProductInfoBean from remote cache");
			bean = JsonKit.toBean((String) obj, ProductInfoBean.class);
		} else {
			log.debug("set ProductInfoBean to remote cache");
			ProductInfoBusiness business = new ProductInfoBusiness();
			bean = business.getProductInfoByKey(productId);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;

	}

	// 刷新缓存
	public Boolean refreshProductInfoCacheByKey(int productId) {
		String key = PdmCacheConfig.getProductInfoCacheKey(productId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
