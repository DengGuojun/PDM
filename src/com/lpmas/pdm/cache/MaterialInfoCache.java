package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.business.MaterialInfoBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class MaterialInfoCache {

	private static Logger log = LoggerFactory.getLogger(MaterialInfoCache.class);

	// 从缓存中获取
	public MaterialInfoBean getMaterialInfoByKey(int materialId) {
		MaterialInfoBean bean = null;
		String key = PdmCacheConfig.getMaterialInfoCacheKey(materialId);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get MaterialInfoBean from remote cache");
			bean = JsonKit.toBean((String) obj, MaterialInfoBean.class);
		} else {
			log.debug("set MaterialInfoBean to remote cache");
			MaterialInfoBusiness business = new MaterialInfoBusiness();
			bean = business.getMaterialInfoByKey(materialId);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}
	
	public MaterialInfoBean getMaterialInfoByNumber(String materialNumber) {
		MaterialInfoBean bean = null;
		String key = PdmCacheConfig.getMaterialInfoCacheNumber(materialNumber);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get MaterialInfoBean from remote cache");
			bean = JsonKit.toBean((String) obj, MaterialInfoBean.class);
		} else {
			log.debug("set MaterialInfoBean to remote cache");
			MaterialInfoBusiness business = new MaterialInfoBusiness();
			bean = business.getMaterialInfoByNumber(materialNumber);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refreshMaterialInfoCacheByKey(int materialId) {
		String key = PdmCacheConfig.getMaterialInfoCacheKey(materialId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}
	
	public Boolean refreshMaterialInfoCacheByNumber(String materialNumber) {
		String key = PdmCacheConfig.getMaterialInfoCacheNumber(materialNumber);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
