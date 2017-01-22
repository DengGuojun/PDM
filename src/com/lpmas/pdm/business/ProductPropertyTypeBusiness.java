package com.lpmas.pdm.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductPropertyOptionBean;
import com.lpmas.pdm.bean.ProductPropertyTypeBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.ProductPropertyTypeDao;
import com.lpmas.pdm.dao.ProductTypeDao;

public class ProductPropertyTypeBusiness {
	public int addProductPropertyType(ProductPropertyTypeBean bean) {
		ProductPropertyTypeDao dao = new ProductPropertyTypeDao();
		int result = dao.insertProductPropertyType(bean);
		if (result > 0) {
			bean.setPropertyId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, bean.getInfoType(), bean.getPropertyId(), 0, 0,
					LogInfoConfig.LOG_PRODUCT_PROPERTY_TYPE);
		}
		return result;
	}

	public int updateProductPropertyType(ProductPropertyTypeBean bean) {
		ProductPropertyTypeDao dao = new ProductPropertyTypeDao();
		if (bean.getStatus() == Constants.STATUS_NOT_VALID) {
			ProductPropertyOptionBusiness productPropertyOptionBusiness = new ProductPropertyOptionBusiness();
			List<ProductPropertyOptionBean> productPropertyOptionList = productPropertyOptionBusiness
					.getProductPropertyOptionListByPropertyId(bean.getPropertyId());
			for (ProductPropertyOptionBean productPropertyOptionBean : productPropertyOptionList) {
				productPropertyOptionBean.setModifyUser(bean.getModifyUser());
				productPropertyOptionBean.setStatus(Constants.STATUS_NOT_VALID);
				productPropertyOptionBusiness.updateProductPropertyOption(productPropertyOptionBean);
			}
		}
		ProductPropertyTypeBean originalBean = dao.getProductPropertyTypeByKey(bean.getPropertyId());
		int result = dao.updateProductPropertyType(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, bean.getInfoType(), bean.getPropertyId(),
					LogInfoConfig.LOG_PRODUCT_PROPERTY_TYPE);
		}
		return result;
	}

	public ProductPropertyTypeBean getProductPropertyTypeByKey(int propertyId) {
		ProductPropertyTypeDao dao = new ProductPropertyTypeDao();
		return dao.getProductPropertyTypeByKey(propertyId);
	}

	public ProductPropertyTypeBean getProductPropertyTypeByParentId(int parentId) {
		ProductPropertyTypeDao dao = new ProductPropertyTypeDao();
		return dao.getProductPropertyTypeByParentId(parentId);
	}

	public PageResultBean<ProductPropertyTypeBean> getProductPropertyTypePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ProductPropertyTypeDao dao = new ProductPropertyTypeDao();
		return dao.getProductPropertyTypePageListByMap(condMap, pageBean);
	}

	public ProductPropertyTypeBean getProductPropertyTypeByCode(String propertyCode) {
		ProductPropertyTypeDao dao = new ProductPropertyTypeDao();
		return dao.getProductPropertyTypeByCode(propertyCode);
	}

	public List<ProductPropertyTypeBean> getProductPropertyTypeListByType(int infoType, int propertyType, int typeId) {
		List<ProductPropertyTypeBean> result = new ArrayList<ProductPropertyTypeBean>();
		ProductPropertyTypeDao dao = new ProductPropertyTypeDao();
		ProductTypeDao typeDao = new ProductTypeDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("infoType", String.valueOf(infoType));
		condMap.put("propertyType", String.valueOf(propertyType));
		condMap.put("typeId", String.valueOf(typeId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		List<ProductPropertyTypeBean> propertyTypeList = dao.getProductPropertyTypeListByMap(condMap);
		ProductTypeBean typeBean = typeDao.getProductTypeByKey(typeId);
		condMap.put("typeId", String.valueOf(typeBean.getParentTypeId()));
		List<ProductPropertyTypeBean> parentPropertyTypeList = dao.getProductPropertyTypeListByMap(condMap);
		result.addAll(propertyTypeList);
		result.addAll(parentPropertyTypeList);
		return result;
	}

	public ReturnMessageBean verifyProductPropertyType(ProductPropertyTypeBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getPropertyCode())) {
			result.setMessage("属性代码必须填写");
		} else if (!isValidPropertyCode(bean.getPropertyCode())) {
			result.setMessage("属性代码格式不符合规范");
		} else if (!StringKit.isValid(bean.getPropertyName())) {
			result.setMessage("属性名称必须填写");
		} else if (bean.getInfoType() == 0) {
			result.setMessage("信息类型必须填写");
		} else if (bean.getTypeId() <= 0) {
			result.setMessage("商品分类必须填写");
		} else if (bean.getParentPropertyId() != 0) {
			ProductPropertyTypeBean productPropertyTypeBean = getProductPropertyTypeByKey(bean.getParentPropertyId());
			if (productPropertyTypeBean.getStatus() == Constants.STATUS_NOT_VALID) {
				result.setMessage("父属性无效,不允许对子属性进行操作");
			}
		}
		ProductTypeBusiness productTypeBusiness = new ProductTypeBusiness();
		ProductTypeBean typeBean = productTypeBusiness.getProductTypeByKey(bean.getTypeId());
		if (typeBean == null || typeBean.getStatus() == Constants.STATUS_NOT_VALID) {
			result.setMessage("商品类型无效");
		}
		return result;
	}

	public boolean isValidPropertyCode(String propertyCode) {
		if (!propertyCode.matches("^[a-zA-Z0-9_]+$")) {
			return false;
		}
		return true;
	}

}