package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductPropertyOptionBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.ProductPropertyOptionDao;

public class ProductPropertyOptionBusiness {
	public int addProductPropertyOption(ProductPropertyOptionBean bean) {
		ProductPropertyOptionDao dao = new ProductPropertyOptionDao();
		int result = dao.insertProductPropertyOption(bean);
		if (result > 0) {
			bean.setOptionId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getOptionId(), 0, 0,
					LogInfoConfig.LOG_PRODUCT_PROPERTY_TYPE_OPTION);
		}
		return result;
	}

	public int updateProductPropertyOption(ProductPropertyOptionBean bean) {
		ProductPropertyOptionDao dao = new ProductPropertyOptionDao();
		ProductPropertyOptionBean originalBean = dao.getProductPropertyOptionByKey(bean.getOptionId());
		int result = dao.updateProductPropertyOption(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getOptionId(),
					LogInfoConfig.LOG_PRODUCT_PROPERTY_TYPE_OPTION);
		}
		return result;
	}

	public ProductPropertyOptionBean getProductPropertyOptionByKey(int optionId) {
		ProductPropertyOptionDao dao = new ProductPropertyOptionDao();
		return dao.getProductPropertyOptionByKey(optionId);
	}

	public List<ProductPropertyOptionBean> getProductPropertyOptionListByPropertyId(int propertyId) {
		ProductPropertyOptionDao dao = new ProductPropertyOptionDao();
		return dao.getProductPropertyOptionListByPropertyId(propertyId);
	}

	public PageResultBean<ProductPropertyOptionBean> getProductPropertyOptionPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		ProductPropertyOptionDao dao = new ProductPropertyOptionDao();
		return dao.getProductPropertyOptionPageListByMap(condMap, pageBean);
	}

	public ReturnMessageBean verifyProductPropertyOption(ProductPropertyOptionBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getOptionContent())) {
			result.setMessage("属性选项显示内容必须填写");
		} else if (!StringKit.isValid(bean.getOptionValue())) {
			result.setMessage("属性选项值必须填写");
		}

		return result;
	}
}