package com.lpmas.pdm.business;

import java.util.HashMap;

import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.pdm.bean.ProductImageBean;
import com.lpmas.pdm.cache.ProductImageCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.ProductImageDao;

public class ProductImageBusiness {
	public int addProductImage(ProductImageBean bean) {
		ProductImageDao dao = new ProductImageDao();		
		int result = dao.insertProductImage(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, bean.getInfoType(), bean.getInfoId(), LogInfoConfig.LOG_PRODUCT_IMAGE);
			refreshCache(bean);
		}
		return result;
	}

	public int updateProductImage(ProductImageBean bean) {
		ProductImageDao dao = new ProductImageDao();
		ProductImageBean originalBean = dao.getProductImageByKey(bean.getInfoId(), bean.getInfoType(), bean.getImageType());		
		int result = dao.updateProductImage(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, bean.getInfoType(), bean.getInfoType(),
					LogInfoConfig.LOG_PRODUCT_IMAGE);
			refreshCache(bean);
		}
		return result;
	}

	public Boolean refreshCache(ProductImageBean bean) {
		ProductImageCache cache = new ProductImageCache();
		return cache.refreshProductImageCacheByKey(bean.getInfoId(), bean.getInfoType(), bean.getImageType());
	}

	public ProductImageBean getProductImageByKey(int infoId, int infoType, String imageType) {
		ProductImageDao dao = new ProductImageDao();
		return dao.getProductImageByKey(infoId, infoType, imageType);
	}

	public PageResultBean<ProductImageBean> getProductImagePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ProductImageDao dao = new ProductImageDao();
		return dao.getProductImagePageListByMap(condMap, pageBean);
	}

}