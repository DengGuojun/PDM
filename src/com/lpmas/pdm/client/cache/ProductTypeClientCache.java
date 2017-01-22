package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class ProductTypeClientCache {
	// 从缓存中获取
	public ProductTypeBean getProductTypeByKey(int typeId) {
		String key = PdmCacheClientConfig.getProductTypeCacheKey(typeId);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		ProductTypeBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, ProductTypeBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getProductTypeByKey(typeId);
			if (result != null) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				updated = true;
			}
			if (!updated) {
				localCache.cancelUpdate(key);
			}
		}

		return result;
	}

}
