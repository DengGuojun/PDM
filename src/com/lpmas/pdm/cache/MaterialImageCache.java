package com.lpmas.pdm.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.pdm.bean.MaterialImageBean;
import com.lpmas.pdm.business.MaterialImageBusiness;
import com.lpmas.pdm.config.PdmCacheConfig;
import com.lpmas.pdm.config.PdmConfig;

public class MaterialImageCache {

	private static Logger log = LoggerFactory.getLogger(MaterialImageCache.class);

	// 从缓存中获取
	public MaterialImageBean getMaterialImageByKey(int materialId, String imageType) {
		MaterialImageBean bean = null;
		String key = PdmCacheConfig.getMaterialImageCacheKey(materialId, imageType);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(PdmConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get MaterialImageBean from remote cache");
			bean = JsonKit.toBean((String) obj, MaterialImageBean.class);
		} else {
			log.debug("set MaterialImageBean to remote cache");
			MaterialImageBusiness business = new MaterialImageBusiness();
			bean = business.getMaterialImageByKey(materialId, imageType);
			if (bean != null) {
				remoteCache.set(PdmConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refreshMaterialImageCacheByKey(int materialId, String imageType) {
		String key = PdmCacheConfig.getMaterialImageCacheKey(materialId, imageType);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(PdmConfig.APP_ID, key);
	}

}
