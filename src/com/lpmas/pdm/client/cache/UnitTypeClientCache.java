package com.lpmas.pdm.client.cache;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class UnitTypeClientCache {

	// 从缓存中获取
	public List<UnitTypeBean> getUnitTypeAllList() {
		String key = PdmCacheClientConfig.getUnitTypeAllListCacheKey();

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		List<UnitTypeBean> result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toList((String) object, UnitTypeBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getUnitTypeAllList();
			if (result != null && !result.isEmpty()) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				updated = true;
			}
			if(!updated){
				localCache.cancelUpdate(key);
				result = new ArrayList<UnitTypeBean>(1);
			}
		}

		return result;
	}

}
