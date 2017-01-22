package com.lpmas.pdm.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.cache.ProductTypeCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.ProductInfoDao;
import com.lpmas.pdm.dao.ProductPropertyTypeDao;
import com.lpmas.pdm.dao.ProductTypeDao;

public class ProductTypeBusiness {
	public int addProductType(ProductTypeBean bean) {
		ProductTypeDao dao = new ProductTypeDao();
		int result = dao.insertProductType(bean);
		if (result > 0) {
			bean.setTypeId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getTypeId(), LogInfoConfig.LOG_PRODUCT_TYPE);
			refreshCache(bean);
		}
		return result;
	}

	public int updateProductType(ProductTypeBean bean) {
		ProductTypeDao dao = new ProductTypeDao();
		ProductTypeBean originalBean = dao.getProductTypeByKey(bean.getTypeId());
		int result = dao.updateProductType(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_PRODUCT, bean.getTypeId(),
					LogInfoConfig.LOG_PRODUCT_TYPE);
			refreshCache(bean);
		}
		return result;
	}

	private Boolean refreshCache(ProductTypeBean bean) {
		ProductTypeCache cache = new ProductTypeCache();
		return cache.refreshProductTypeCacheByKey(bean.getTypeId());
	}

	public ProductTypeBean getProductTypeByKey(int typeId) {
		ProductTypeDao dao = new ProductTypeDao();
		return dao.getProductTypeByKey(typeId);
	}

	public ProductTypeBean getProductTypeByCode(String typeCode) {
		ProductTypeDao dao = new ProductTypeDao();
		return dao.getProductTypeByCode(typeCode);
	}

	public PageResultBean<ProductTypeBean> getProductTypePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ProductTypeDao dao = new ProductTypeDao();
		return dao.getProductTypePageListByMap(condMap, pageBean);
	}

	public List<ProductTypeBean> getProductTypeListByParentId(int parentId) {
		ProductTypeDao dao = new ProductTypeDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("parentTypeId", String.valueOf(parentId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getProductTypeListByMap(condMap);
	}

	public List<ProductTypeBean> getProductTypeAllList() {
		ProductTypeDao dao = new ProductTypeDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getProductTypeListByMap(condMap);
	}

	public List<ProductTypeBean> getParentTreeListByKey(int typeId) {
		List<ProductTypeBean> treeList = new ArrayList<ProductTypeBean>();
		ProductTypeBean bean = getProductTypeByKey(typeId);
		while (bean != null) {
			treeList.add(bean);
			bean = getProductTypeByKey(bean.getParentTypeId());
		}
		Collections.reverse(treeList);
		return treeList;
	}

	public ReturnMessageBean verifyProductTypeProperty(ProductTypeBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		ProductTypeDao dao = new ProductTypeDao();
		ProductInfoDao productInfoDao = new ProductInfoDao();
		ProductPropertyTypeDao productPropertyTypeDao = new ProductPropertyTypeDao();
		if (!StringKit.isValid(bean.getTypeCode())) {
			result.setMessage("类型代码必须填写");
		} else if (isDuplicateTypeCode(bean.getTypeId(), bean.getTypeCode())) {
			result.setMessage("已存在相同的类型代码");
		} else if (!isValidTypeCode(bean.getTypeCode())) {
			result.setMessage("类型代码格式不符合规范");
		} else if (!StringKit.isValid(bean.getTypeName())) {
			result.setMessage("类型名称必须填写");
		} else if (bean.getTypeName().length() > 30) {
			result.setMessage("类型名称不能大于30字");
		} else if (bean.getParentTypeId() != 0) {
			ProductTypeBean productTypeBean = getProductTypeByKey(bean.getParentTypeId());
			if (productTypeBean.getStatus() == Constants.STATUS_NOT_VALID) {
				result.setMessage("父类型无效,不允许对子类型进行操作");
			}
		}
		if (bean.getStatus() == Constants.STATUS_NOT_VALID && bean.getTypeId() != 0) {
			HashMap<String, String> condMap = new HashMap<String, String>();
			condMap.put("parentTypeId", String.valueOf(bean.getTypeId()));
			condMap.put("typeId", String.valueOf(bean.getTypeId()));
			condMap.put("status", String.valueOf(Constants.STATUS_VALID));
			if (dao.getProductTypeListByMap(condMap).size() > 0) {
				result.setMessage("该商品类型下包含子类型，不能设置为无效");
			} else if (!productInfoDao.getProductInfoListByMap(condMap).isEmpty()) {
				result.setMessage("该商品类型下包含商品，不能设置为无效");
			} else {
				condMap = new HashMap<String, String>();
				condMap.put("typeId", String.valueOf(bean.getTypeId()));
				condMap.put("status", String.valueOf(Constants.STATUS_VALID));
				if (productPropertyTypeDao.getProductPropertyTypeListByMap(condMap).size() > 0) {
					result.setMessage("该商品类型下包含商品属性，不能设置为无效");
				}
			}
		}
		return result;
	}

	public boolean isDuplicateTypeCode(int typeId, String typeCode) {
//		ProductTypeBean bean = getProductTypeByCode(typeCode);
//		if (bean == null) {
//			return false;
//		} else {
//			if (typeId > 0 && typeId == bean.getTypeId()) {
//				return false;
//			}
//		} 
//		return true;
		//暂时允许商品类型编码重复，后续考虑判断是否同一层级，同一层级的才不能重复
		return false;
	}

	public boolean isValidTypeCode(String typeCode) {
		if (!typeCode.matches("^[a-zA-Z0-9_]+$")) {
			return false;
		}
		if (typeCode.length() > 10) {
			return false;
		}
		return true;
	}

}