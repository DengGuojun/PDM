package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductPropertyBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class ProductPropertyClientCache {
	// 从缓存中获取
	public ProductPropertyBean getProductPropertyByKey(int infoId, int infoType, String propertyCode) {
		String key = PdmCacheClientConfig.getProductPropertyCacheKey(infoId, infoType, propertyCode);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		ProductPropertyBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, ProductPropertyBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getProductPropertyByKey(infoId, infoType, propertyCode);
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
