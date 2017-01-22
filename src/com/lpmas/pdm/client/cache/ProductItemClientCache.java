package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class ProductItemClientCache {
	// 从缓存中获取
	public ProductItemBean getProductItemByKey(int productItemId) {
		String key = PdmCacheClientConfig.getProductItemCacheKey(productItemId);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		ProductItemBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, ProductItemBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getProductItemByKey(productItemId);
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

	public ProductItemBean getProductItemByNumber(String productItemNumber) {
		String key = PdmCacheClientConfig.getProductItemCacheNumber(productItemNumber);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		ProductItemBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, ProductItemBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getProductItemByNumber(productItemNumber);
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
