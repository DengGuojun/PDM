package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.business.ProductTypeBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class ProductTypeCache {

	private static Logger log = LoggerFactory.getLogger(ProductTypeCache.class);

	// 从缓存中获取
	public ProductTypeBean getProductTypeByKey(int typeId) {
		ProductTypeBean bean = null;
		String key = PdmCacheConfig.getProductTypeCacheKey(typeId);
		RemoteCache remoteCache = RemoteCache.getInstance();

		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get ProductTypeBean from remote cache");
			bean = JsonKit.toBean((String) obj, ProductTypeBean.class);
		} else {
			log.debug("set ProductTypeBean to remote cache");
			ProductTypeBusiness business = new ProductTypeBusiness();
			bean = business.getProductTypeByKey(typeId);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refreshProductTypeCacheByKey(int typeId) {
		String key = PdmCacheConfig.getProductTypeCacheKey(typeId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
