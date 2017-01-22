package com.lpmas.pdm.client.cache;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;
import com.lpmas.pdm.config.PdmCacheClientConfig;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class UnitInfoClientCache {

	// 从缓存中获取
	public List<UnitInfoBean> getUnitInfoListByTypeId(int typeId) {
		String key = PdmCacheClientConfig.getUnitInfoListCacheKey(typeId);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		List<UnitInfoBean> result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toList((String) object, UnitInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getUnitInfoListByTypeKey(typeId);
			if (result != null && !result.isEmpty()) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				updated = true;
			}
			if (!updated) {
				localCache.cancelUpdate(key);
				result = new ArrayList<UnitInfoBean>(1);
			}
		}

		return result;
	}

	public UnitInfoBean getUnitInfoByCode(String unitCode) {
		String key = PdmCacheClientConfig.getUnitInfoCacheKey(unitCode);

		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		UnitInfoBean result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, UnitInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			PdmServiceClient client = new PdmServiceClient();
			result = client.getUnitInfoByCode(unitCode);
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
