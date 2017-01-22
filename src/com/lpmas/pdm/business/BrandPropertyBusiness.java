package com.lpmas.pdm.business;

import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.pdm.bean.BrandPropertyBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.BrandPropertyDao;

public class BrandPropertyBusiness {
	public int addBrandProperty(BrandPropertyBean bean) {
		BrandPropertyDao dao = new BrandPropertyDao();
		int result = dao.insertBrandProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_BRAND, result, LogInfoConfig.LOG_BRAND_PROPERTY);
		}
		return result;
	}

	public int updateBrandProperty(BrandPropertyBean bean) {
		BrandPropertyDao dao = new BrandPropertyDao();
		BrandPropertyBean originalBean = dao.getBrandPropertyByKey(bean.getBrandId(), bean.getPropertyCode());
		int result = dao.updateBrandProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean,InfoTypeConfig.INFO_TYPE_BRAND, bean.getBrandId(), LogInfoConfig.LOG_BRAND_PROPERTY);
		}
		return result;
	}

	public BrandPropertyBean getBrandPropertyByKey(int brandId, String propertyCode) {
		BrandPropertyDao dao = new BrandPropertyDao();
		return dao.getBrandPropertyByKey(brandId, propertyCode);
	}

	public List<BrandPropertyBean> getBrandPropertyListByBrandId(int brandId) {
		BrandPropertyDao dao = new BrandPropertyDao();
		return dao.getBrandPropertyListByBrandId(brandId);
	}

}