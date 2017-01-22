package com.lpmas.pdm.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.business.UnitInfoBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class UnitInfoCache {

	private static Logger log = LoggerFactory.getLogger(UnitInfoCache.class);

	// 从缓存中获取
	public List<UnitInfoBean> getUnitInfoListByTypeId(int typeId) {
		List<UnitInfoBean> result = null;
		String key = PdmCacheConfig.getUnitInfoListCacheKey(typeId);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get List<UnitTypeBean> from remote cache");
			result = JsonKit.toList((String) obj, UnitInfoBean.class);
		} else {
			log.debug("set List<UnitTypeBean> to remote cache");
			UnitInfoBusiness business = new UnitInfoBusiness();
			result = business.getUnitInfoByTypeId(typeId);
			if (result != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return result;
	}

	// 刷新缓存
	public Boolean refreshUnitInfoListCacheByKey(int typeId) {
		String key = PdmCacheConfig.getUnitInfoListCacheKey(typeId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

	public UnitInfoBean getUnitInfoByCode(String unitCode) {
		UnitInfoBean result = null;
		String key = PdmCacheConfig.getUnitInfoCacheKey(unitCode);
		RemoteCache remoteCache = RemoteCache.getInstance();

		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get UnitTypeBean from remote cache");
			result = JsonKit.toBean((String) obj, UnitInfoBean.class);
		} else {
			log.debug("set UnitTypeBean to remote cache");
			UnitInfoBusiness business = new UnitInfoBusiness();
			result = business.getUnitInfoByCode(unitCode);
			if (result != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return result;
	}

	// 刷新缓存
	public Boolean refreshUnitInfoCacheByCode(String unitCode) {
		String key = PdmCacheConfig.getUnitInfoCacheKey(unitCode);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}
}
