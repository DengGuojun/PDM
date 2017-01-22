package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class ProductImageClientCache {

	// 从缓存中获取
	public ProductImageBean getProductImageByKey(int infoId, int infoType, String imageType) {
		String key = PdmCacheClientConfig.getProductImageCacheKey(infoId, infoType, imageType);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		ProductImageBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, ProductImageBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getProductItemImageByProductItemId(infoId, imageType);
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
