package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class MaterialPropertyClientCache {

	// 从缓存中获取
	public MaterialPropertyBean getMaterialPropertyByKey(int materialId, String propertyCode) {
		String key = PdmCacheClientConfig.getMaterialPropertyCacheKey(materialId, propertyCode);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		MaterialPropertyBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, MaterialPropertyBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getMaterialPropertyByKey(materialId, propertyCode);
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
