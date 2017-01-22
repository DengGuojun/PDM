package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.BrandInfoBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.BrandInfoDao;

public class BrandInfoBusiness {

	public int addBrandInfo(BrandInfoBean bean) {
		BrandInfoDao dao = new BrandInfoDao();
		int result = dao.insertBrandInfo(bean);
		if (result > 0) {
			bean.setBrandId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_BRAND, bean.getBrandId(), LogInfoConfig.LOG_BRAND_INFO);
		}
		return result;
	}

	public int updateBrandInfo(BrandInfoBean bean) {
		BrandInfoDao dao = new BrandInfoDao();
		BrandInfoBean originalBean = dao.getBrandInfoByKey(bean.getBrandId());
		int result = dao.updateBrandInfo(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_BRAND, bean.getBrandId(),
					LogInfoConfig.LOG_BRAND_INFO);
		}
		return result;
	}

	public BrandInfoBean getBrandInfoByKey(int brandId) {
		BrandInfoDao dao = new BrandInfoDao();
		return dao.getBrandInfoByKey(brandId);
	}

	public BrandInfoBean getBrandInfoByCode(String brandCode) {
		BrandInfoDao dao = new BrandInfoDao();
		return dao.getBrandInfoByCode(brandCode);
	}

	public PageResultBean<BrandInfoBean> getBrandInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		BrandInfoDao dao = new BrandInfoDao();
		return dao.getBrandInfoPageListByMap(condMap, pageBean);
	}

	public List<BrandInfoBean> getBrandInfoAllList() {
		BrandInfoDao dao = new BrandInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getBrandInfoListByMap(condMap);
	}

	public ReturnMessageBean verifyBrandInfoProperty(BrandInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getBrandCode())) {
			result.setMessage("品牌代码必须填写");
		} else if (isDuplicateBrandCode(bean.getBrandId(), bean.getBrandCode())) {
			result.setMessage("已存在相同的品牌代码");
		} else if (!isValidBrandCode(bean.getBrandCode())) {
			result.setMessage("品牌代码格式不符合规范");
		} else if (!StringKit.isValid(bean.getBrandName())) {
			result.setMessage("品牌名称必须填写");
		} else if (bean.getBrandName().length() > 30) {
			result.setMessage("品牌名称不能大于30字");
		} else if (bean.getGroupId() == 0) {
			result.setMessage("所属用户组必须填写");
		} else if (!StringKit.isNull(bean.getMemo()) && bean.getMemo().length() > 1000) {
			result.setMessage("品牌备注不能大于1000字");
		}

		if (bean.getBrandId() > 0 && bean.getStatus() == Constants.STATUS_NOT_VALID) {
			ProductInfoBusiness productInfoBusiness = new ProductInfoBusiness();
			List<ProductInfoBean> infoList = productInfoBusiness.getProductInfoListByBandId(bean.getBrandId());
			if (!infoList.isEmpty()) {
				result.setMessage("该品牌下包含商品属性，不能设置为无效");
			}
		}
		return result;
	}

	public boolean isDuplicateBrandCode(int brandId, String brandCode) {
		BrandInfoBean bean = getBrandInfoByCode(brandCode);
		if (bean == null) {
			return false;
		} else {
			if (brandId > 0 && brandId == bean.getBrandId()) {
				return false;
			}
		}
		return true;
	}

	public boolean isValidBrandCode(String brandCode) {
		if (!brandCode.matches("^[a-zA-Z0-9_]+$")) {
			return false;
		}
		if (brandCode.length() > 10) {
			return false;
		}
		return true;
	}

}