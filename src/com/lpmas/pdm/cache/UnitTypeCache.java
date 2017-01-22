package com.lpmas.pdm.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.UnitTypeBean;
import com.lpmas.pdm.business.UnitTypeBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class UnitTypeCache {

	private static Logger log = LoggerFactory.getLogger(UnitTypeCache.class);

	// 从缓存中获取
	public List<UnitTypeBean> getUnitTypeAllList() {
		List<UnitTypeBean> resutl = null;
		String key = PdmCacheConfig.getUnitTypeAllListCacheKey();

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get List<UnitTypeBean> from remote cache");
			resutl = JsonKit.toList((String) obj, UnitTypeBean.class);
		} else {
			log.debug("set List<UnitTypeBean> to remote cache");
			UnitTypeBusiness business = new UnitTypeBusiness();
			resutl = business.getUnitTypeAllList();
			if (resutl != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(resutl), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return resutl;
	}

	// 刷新缓存
	public Boolean refreshUnitTypeAllListCache() {
		String key = PdmCacheConfig.getUnitTypeAllListCacheKey();
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}
}
