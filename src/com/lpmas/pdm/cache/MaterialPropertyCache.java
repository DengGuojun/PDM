package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialPropertyBean;
import com.lpmas.pdm.business.MaterialPropertyBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class MaterialPropertyCache {

	private static Logger log = LoggerFactory.getLogger(MaterialPropertyCache.class);

	// 从缓存中获取
	public MaterialPropertyBean getMaterialPropertyByKey(int materialId, String propertyCode) {
		MaterialPropertyBean bean = null;
		String key = PdmCacheConfig.getMaterialPropertyCacheKey(materialId, propertyCode);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get MaterialPropertyBean from remote cache");
			bean = JsonKit.toBean((String) obj, MaterialPropertyBean.class);
		} else {
			log.debug("set MaterialPropertyBean to remote cache");
			MaterialPropertyBusiness business = new MaterialPropertyBusiness();
			bean = business.getMaterialPropertyByKey(materialId, propertyCode);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refreshMaterialPropertyCacheByKey(int materialId, String propertyCode) {
		String key = PdmCacheConfig.getMaterialPropertyCacheKey(materialId, propertyCode);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}
}
