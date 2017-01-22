package com.lpmas.pdm.client.cache;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.WareInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class WareInfoClientCache {


	public WareInfoBean getWareInfoByKey(int wareType, int wareId) {
		WareInfoBean result = null;
		String key = PdmCacheClientConfig.getWareInfoCacheKey(wareType, wareId);
		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, WareInfoBean.class);
		} catch (NeedsRefreshException nre) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getWareInfoByKey(wareType, wareId);
			if (result != null) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				updated = true;
			}
			if(!updated){
				localCache.cancelUpdate(key);
			}
		}
		return result;
	}

	public WareInfoBean getWareInfoByKey(int wareType, String wareNumber) {
		WareInfoBean result = null;
		String key = PdmCacheClientConfig.getWareInfoCacheKey(wareType, wareNumber);
		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, WareInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getWareInfoByKey(wareType, wareNumber);
			if (result != null) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				updated = true;
			}
			if(!updated){
				localCache.cancelUpdate(key);
			}
		}
		return result;
	}

}
