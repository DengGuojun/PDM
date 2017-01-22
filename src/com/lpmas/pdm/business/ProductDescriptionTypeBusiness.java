package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.ProductDescriptionTypeBean;
import com.lpmas.pdm.dao.ProductDescriptionTypeDao;

public class ProductDescriptionTypeBusiness {
	public int addProductDescriptionType(ProductDescriptionTypeBean bean) {
		ProductDescriptionTypeDao dao = new ProductDescriptionTypeDao();
		return dao.insertProductDescriptionType(bean);
	}

	public int updateProductDescriptionType(ProductDescriptionTypeBean bean) {
		ProductDescriptionTypeDao dao = new ProductDescriptionTypeDao();
		return dao.updateProductDescriptionType(bean);
	}

	public ProductDescriptionTypeBean getProductDescriptionTypeByKey(int typeId) {
		ProductDescriptionTypeDao dao = new ProductDescriptionTypeDao();
		return dao.getProductDescriptionTypeByKey(typeId);
	}
	
	public ProductDescriptionTypeBean getProductDescriptionTypeByCode(String typeCode) {
		ProductDescriptionTypeDao dao = new ProductDescriptionTypeDao();
		return dao.getProductDescriptionTypeByCode(typeCode);
	}
	
	public List<ProductDescriptionTypeBean> getProductDescriptionTypeListByInfoType(int infoType) {
		ProductDescriptionTypeDao dao = new ProductDescriptionTypeDao();
		return dao.getProductDescriptionTypeListByInfoType(infoType);
	}

	public PageResultBean<ProductDescriptionTypeBean> getProductDescriptionTypePageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		ProductDescriptionTypeDao dao = new ProductDescriptionTypeDao();
		return dao.getProductDescriptionTypePageListByMap(condMap, pageBean);
	}
	
	public ReturnMessageBean verifyProductDescriptionType(ProductDescriptionTypeBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getTypeCode())){
			result.setMessage("描述类型代码必须填写");
		} else if (!isValidTypeCode(bean.getTypeCode())){
			result.setMessage("描述类型代码格式不符合规范");
		} else if (isDuplicateTypeCode(bean.getTypeId(), bean.getTypeCode())) {
			result.setMessage("已存在相同的描述类型代码");
		} else if (bean.getInfoType() == 0) {
			result.setMessage("信息类型必须填写");
		} 
		return result;
	}
	
	public boolean isValidTypeCode(String typeCode) {
		if(!typeCode.matches("^[A-Za-z]+$")){
			return false;
		}
		if (typeCode.length() > 10) {
			return false;
		}
		return true;
	}
	
	public boolean isDuplicateTypeCode(int typeId, String typeCode){
		ProductDescriptionTypeBean bean = this.getProductDescriptionTypeByCode(typeCode);
		if (bean == null) {
			return false;
		} else {
			if (typeId > 0 && typeId == bean.getTypeId()) {
				return false;
			}
		}
		return true;
	}

}