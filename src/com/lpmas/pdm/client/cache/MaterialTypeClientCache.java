package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class MaterialTypeClientCache {
	// 从缓存中获取
	public MaterialTypeBean getMaterialTypeByKey(int typeId) {
		String key = PdmCacheClientConfig.getMaterialTypeCacheKey(typeId);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		MaterialTypeBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, MaterialTypeBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getMaterialTypeByKey(typeId);
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
