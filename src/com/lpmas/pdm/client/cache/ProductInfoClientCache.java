package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class ProductInfoClientCache {

	// 从缓存中获取
	public ProductInfoBean getProductInfoByKey(int productId) {
		String key = PdmCacheClientConfig.getProductInfoCacheKey(productId);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		ProductInfoBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, ProductInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getProductInfoByKey(productId);
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
