package com.lpmas.pdm.business;

import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.pdm.bean.ProductTypePropertyBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.ProductTypePropertyDao;

public class ProductTypePropertyBusiness {
	public int addProductTypeProperty(ProductTypePropertyBean bean) {
		ProductTypePropertyDao dao = new ProductTypePropertyDao();		
		int result = dao.insertProductTypeProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getTypeId(), 0, 0,
					LogInfoConfig.LOG_PRODUCT_TYPE_PROPERTY, bean.getPropertyCode(), "");
		}
		return result;
	}

	public int updateProductTypeProperty(ProductTypePropertyBean bean) {
		ProductTypePropertyDao dao = new ProductTypePropertyDao();
		ProductTypePropertyBean originalBean = dao.getProductTypePropertyByKey(bean.getTypeId(), bean.getPropertyCode());		
		int result = dao.updateProductTypeProperty(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getTypeId(), 0, 0,
					LogInfoConfig.LOG_PRODUCT_TYPE_PROPERTY, bean.getPropertyCode(), "");
		}
		return result;
	}

	public ProductTypePropertyBean getProductTypePropertyByKey(int typeId, String propertyCode) {
		ProductTypePropertyDao dao = new ProductTypePropertyDao();
		return dao.getProductTypePropertyByKey(typeId, propertyCode);
	}

	public List<ProductTypePropertyBean> getProductTypePropertyListByTypeId(int typeId) {
		ProductTypePropertyDao dao = new ProductTypePropertyDao();
		return dao.getProductTypePropertyListByTypeId(typeId);
	}

}