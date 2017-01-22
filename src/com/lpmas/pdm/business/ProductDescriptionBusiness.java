package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.pdm.bean.ProductDescriptionBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.ProductDescriptionDao;

public class ProductDescriptionBusiness {
	public int addProductDescription(ProductDescriptionBean bean) {
		ProductDescriptionDao dao = new ProductDescriptionDao();
		int result = dao.insertProductDescription(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, bean.getInfoType(), bean.getInfoId(), LogInfoConfig.LOG_PRODUCT_DESCRIPTION);
		}
		return result;
	}

	public int updateProductDescription(ProductDescriptionBean bean) {
		ProductDescriptionDao dao = new ProductDescriptionDao();
		ProductDescriptionBean originalBean = dao.getProductDescriptionByKey(bean.getInfoId(), bean.getInfoType(),
				bean.getDescCode());
		int result = dao.updateProductDescription(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, bean.getInfoType(), bean.getInfoId(),
					LogInfoConfig.LOG_PRODUCT_DESCRIPTION);
		}
		return result;
	}

	public ProductDescriptionBean getProductDescriptionByKey(int infoId, int infoType, String descCode) {
		ProductDescriptionDao dao = new ProductDescriptionDao();
		return dao.getProductDescriptionByKey(infoId, infoType, descCode);
	}

	public PageResultBean<ProductDescriptionBean> getProductDescriptionPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ProductDescriptionDao dao = new ProductDescriptionDao();
		return dao.getProductDescriptionPageListByMap(condMap, pageBean);
	}

	public List<ProductDescriptionBean> getProductDescriptionListByInfoIdAndInfoType(int infoId, int infoType) {
		ProductDescriptionDao dao = new ProductDescriptionDao();
		return dao.getProductDescriptionListByInfoIdAndInfoType(infoId, infoType);
	}

}