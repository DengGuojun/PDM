package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class MaterialInfoClientCache {
	// 从缓存中获取
	public MaterialInfoBean getMaterialInfoByKey(int materialId) {
		String key = PdmCacheClientConfig.getMaterialInfoCacheKey(materialId);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		MaterialInfoBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, MaterialInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getMaterialInfoByKey(materialId);
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

	public MaterialInfoBean getMaterialInfoByNumber(String materialNumber) {
		String key = PdmCacheClientConfig.getMaterialInfoCacheNumber(materialNumber);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		MaterialInfoBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, MaterialInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getMaterialInfoByNumber(materialNumber);
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
