package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.WareInfoBean;
import com.lpmas.pdm.business.WareInfoBusniess;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class WareInfoCache {
	private static Logger log = LoggerFactory.getLogger(WareInfoCache.class);

	public WareInfoBean getWareInfoByKey(int wareType, int wareId) {
		WareInfoBean bean = null;
		String key = PdmCacheConfig.getWareInfoCacheKey(wareType, wareId);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get WareInfoBean from remote cache");
			bean = JsonKit.toBean((String) obj, WareInfoBean.class);
		} else {
			log.debug("set WareInfoBean to remote cache");
			WareInfoBusniess wareInfoBusniess = new WareInfoBusniess();
			bean = wareInfoBusniess.getWareInfoByKey(wareType,wareId);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	public WareInfoBean getWareInfoByKey(int wareType, String wareNumber) {
		WareInfoBean bean = null;
		String key = PdmCacheConfig.getWareInfoCacheKey(wareType, wareNumber);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get WareInfoBean from remote cache");
			bean = JsonKit.toBean((String) obj, WareInfoBean.class);
		} else {
			log.debug("set WareInfoBean to remote cache");
			WareInfoBusniess wareInfoBusniess = new WareInfoBusniess();
			bean = wareInfoBusniess.getWareInfoByKey(wareType, wareNumber);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refresWareInfoCacheByKey(int wareType, int wareId) {
		String key = PdmCacheConfig.getWareInfoCacheKey(wareType, wareId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

	// 刷新缓存
	public Boolean refresWareInfoCacheByKey(int wareType, String wareNumber) {
		String key = PdmCacheConfig.getWareInfoCacheKey(wareType, wareNumber);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
