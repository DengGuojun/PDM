package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialImageBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class MaterialImageClientCache {
	// 从缓存中获取
	public MaterialImageBean getMaterialImageByKey(int materialId, String imageType) {
		String key = PdmCacheClientConfig.getMaterialImageCacheKey(materialId, imageType);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		MaterialImageBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, MaterialImageBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getMaterialImageByMaterialId(materialId, imageType);
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
