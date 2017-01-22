package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.pdm.bean.MaterialTypePropertyBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.MaterialTypePropertyDao;

public class MaterialTypePropertyBusiness {
	public int addMaterialTypeProperty(MaterialTypePropertyBean bean) {
		MaterialTypePropertyDao dao = new MaterialTypePropertyDao();
		int result = dao.insertMaterialTypeProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getTypeId(), 0, 0,
					LogInfoConfig.LOG_MATERIAL_TYPE_PROPERTY, bean.getPropertyCode(), "");
		}
		return result;
	}

	public int updateMaterialTypeProperty(MaterialTypePropertyBean bean) {
		MaterialTypePropertyDao dao = new MaterialTypePropertyDao();
		MaterialTypePropertyBean originalBean = dao.getMaterialTypePropertyByKey(bean.getTypeId(), bean.getPropertyCode());
		int result = dao.updateMaterialTypeProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getTypeId(), 0, 0,
					LogInfoConfig.LOG_MATERIAL_TYPE_PROPERTY, bean.getPropertyCode(), "");
		}
		return result;
	}

	public MaterialTypePropertyBean getMaterialTypePropertyByKey(int typeId, String propertyCode) {
		MaterialTypePropertyDao dao = new MaterialTypePropertyDao();
		return dao.getMaterialTypePropertyByKey(typeId, propertyCode);
	}

	public PageResultBean<MaterialTypePropertyBean> getMaterialTypePropertyPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		MaterialTypePropertyDao dao = new MaterialTypePropertyDao();
		return dao.getMaterialTypePropertyPageListByMap(condMap, pageBean);
	}

	public List<MaterialTypePropertyBean> getMaterialTypePropertyListByTypeId(int materialTypeId) {
		MaterialTypePropertyDao dao = new MaterialTypePropertyDao();
		return dao.getMaterialTypePropertyListByTypeId(materialTypeId);
	}

}