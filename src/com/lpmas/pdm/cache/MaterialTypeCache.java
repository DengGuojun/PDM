package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.business.MaterialTypeBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class MaterialTypeCache {

	private static Logger log = LoggerFactory.getLogger(MaterialTypeCache.class);

	// 从缓存中获取
	public MaterialTypeBean getMaterialTypeByKey(int typeId) {
		MaterialTypeBean bean = null;
		String key = PdmCacheConfig.getMaterialTypeCacheKey(typeId);
		RemoteCache remoteCache = RemoteCache.getInstance();

		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get MaterialTypeBean from remote cache");
			bean = JsonKit.toBean((String) obj, MaterialTypeBean.class);
		} else {
			log.debug("set MaterialTypeBean to remote cache");
			MaterialTypeBusiness business = new MaterialTypeBusiness();
			bean = business.getMaterialTypeByKey(typeId);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refreshMaterialTypeCacheByKey(int typeId) {
		String key = PdmCacheConfig.getMaterialTypeCacheKey(typeId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}
}
