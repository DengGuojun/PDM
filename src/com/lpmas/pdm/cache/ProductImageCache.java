package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.business.ProductImageBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class ProductImageCache {

	private static Logger log = LoggerFactory.getLogger(ProductImageCache.class);

	// 从缓存中获取
	public ProductImageBean getProductImageByKey(int debugId, int debugType, String imageType) {
		ProductImageBean bean = null;
		String key = PdmCacheConfig.getProductImageCacheKey(debugId, debugType, imageType);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get ProductImageBean from remote cache");
			bean = JsonKit.toBean((String) obj, ProductImageBean.class);
		} else {
			log.debug("set ProductImageBean to remote cache");
			ProductImageBusiness business = new ProductImageBusiness();
			bean = business.getProductImageByKey(debugId, debugType, imageType);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refreshProductImageCacheByKey(int debugId, int debugType, String imageType) {
		String key = PdmCacheConfig.getProductImageCacheKey(debugId, debugType, imageType);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
