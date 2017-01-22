package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialPropertyOptionBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.MaterialPropertyOptionDao;

public class MaterialPropertyOptionBusiness {
	public int addMaterialPropertyOption(MaterialPropertyOptionBean bean) {
		MaterialPropertyOptionDao dao = new MaterialPropertyOptionDao();
		int result = dao.insertMaterialPropertyOption(bean);
		if (result > 0) {
			bean.setOptionId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getOptionId(), 0, 0,
					LogInfoConfig.LOG_MATERIAL_PROPERTY_TYPE_OPTION);
		}
		return result;
	}

	public int updateMaterialPropertyOption(MaterialPropertyOptionBean bean) {
		MaterialPropertyOptionDao dao = new MaterialPropertyOptionDao();
		MaterialPropertyOptionBean originalBean = dao.getMaterialPropertyOptionByKey(bean.getOptionId());
		int result = dao.updateMaterialPropertyOption(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_MATERIAL, bean.getOptionId(),
					LogInfoConfig.LOG_MATERIAL_PROPERTY_TYPE_OPTION);
		}
		return result;
	}

	public MaterialPropertyOptionBean getMaterialPropertyOptionByKey(int optionId) {
		MaterialPropertyOptionDao dao = new MaterialPropertyOptionDao();
		return dao.getMaterialPropertyOptionByKey(optionId);
	}

	public PageResultBean<MaterialPropertyOptionBean> getMaterialPropertyOptionPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		MaterialPropertyOptionDao dao = new MaterialPropertyOptionDao();
		return dao.getMaterialPropertyOptionPageListByMap(condMap, pageBean);
	}

	public List<MaterialPropertyOptionBean> getMaterialPropertyOptionListByPropertyId(int propertyId) {
		MaterialPropertyOptionDao dao = new MaterialPropertyOptionDao();
		return dao.getMaterialPropertyOptionListByPropertyId(propertyId);
	}

	public ReturnMessageBean verifyMaterialPropertyPropertyOption(MaterialPropertyOptionBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getOptionContent())) {
			result.setMessage("属性选项显示内容必须填写");
		} else if (!StringKit.isValid(bean.getOptionValue())) {
			result.setMessage("属性选项值必须填写");
		}

		return result;
	}

}