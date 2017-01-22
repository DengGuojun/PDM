package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.business.ProductPropertyBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class ProductPropertyCache {
	private static Logger log = LoggerFactory.getLogger(ProductPropertyCache.class);

	// 从缓存中获取
	public ProductPropertyBean getProductPropertyByKey(int debugId, int debugType, String propertyCode) {
		ProductPropertyBean bean = null;
		String key = PdmCacheConfig.getProductPropertyCacheKey(debugId, debugType, propertyCode);
		RemoteCache remoteCache = RemoteCache.getInstance();

		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get ProductPropertyBean from remote cache");
			bean = JsonKit.toBean((String) obj, ProductPropertyBean.class);
		} else {
			log.debug("set ProductPropertyBean to remote cache");
			ProductPropertyBusiness business = new ProductPropertyBusiness();
			bean = business.getProductPropertyByKey(debugId, debugType, propertyCode);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refreshProductPropertyCacheByKey(int debugId, int debugType, String propertyCode) {
		String key = PdmCacheConfig.getProductPropertyCacheKey(debugId, debugType, propertyCode);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
