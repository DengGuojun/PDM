package com.lpmas.pdm.business;

import java.util.HashMap;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.pdm.bean.MaterialImageBean;
import com.lpmas.pdm.cache.MaterialImageCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.MaterialImageDao;

public class MaterialImageBusiness {
	public int addMaterialImage(MaterialImageBean bean) {
		MaterialImageDao dao = new MaterialImageDao();
		int result = dao.insertMaterialImage(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialId(),
					LogInfoConfig.LOG_MATERIAL_IMAGE);
			refreshCache(bean);
		}
		return result;
	}

	public int updateMaterialImage(MaterialImageBean bean) {
		MaterialImageDao dao = new MaterialImageDao();
		MaterialImageBean originalBean = dao.getMaterialImageByKey(bean.getMaterialId(), bean.getImageType());
		int result = dao.updateMaterialImage(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getMaterialId(),
					LogInfoConfig.LOG_MATERIAL_IMAGE);
			refreshCache(bean);
		}
		return result;
	}

	public Boolean refreshCache(MaterialImageBean bean) {
		MaterialImageCache cache = new MaterialImageCache();
		return cache.refreshMaterialImageCacheByKey(bean.getMaterialId(), bean.getImageType());
	}

	public MaterialImageBean getMaterialImageByKey(int materialId, String imageType) {
		MaterialImageDao dao = new MaterialImageDao();
		return dao.getMaterialImageByKey(materialId, imageType);
	}

	public PageResultBean<MaterialImageBean> getMaterialImagePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		MaterialImageDao dao = new MaterialImageDao();
		return dao.getMaterialImagePageListByMap(condMap, pageBean);
	}

}