package com.lpmas.pdm.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.cache.UnitInfoCache;
import com.lpmas.pdm.config.LogInfoConfig;
import com.lpmas.pdm.dao.UnitInfoDao;

public class UnitInfoBusiness {
	public int addUnitInfo(UnitInfoBean bean) {
		UnitInfoDao dao = new UnitInfoDao();
		int result = dao.insertUnitInfo(bean);
		if (result > 0) {
			bean.setUnitId(result);
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_UNIT, bean.getUnitId(), LogInfoConfig.LOG_UNIT_INFO);
			refreshCache(bean);
		}
		return result;
	}

	public int updateUnitInfo(UnitInfoBean bean) {
		UnitInfoDao dao = new UnitInfoDao();
		UnitInfoBean originalBean = dao.getUnitInfoByKey(bean.getUnitId());
		int result = dao.updateUnitInfo(bean);
		if (result > 0) {
			PdmLogSendHelper helper = new PdmLogSendHelper();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_UNIT, bean.getUnitId(),
					LogInfoConfig.LOG_UNIT_INFO);
			refreshCache(bean);
		}
		return result;
	}

	public Boolean refreshCache(UnitInfoBean bean) {
		UnitInfoCache cache = new UnitInfoCache();
		cache.refreshUnitInfoCacheByCode(bean.getUnitCode());
		return cache.refreshUnitInfoListCacheByKey(bean.getTypeId());
	}

	public UnitInfoBean getUnitInfoByKey(int unitId) {
		UnitInfoDao dao = new UnitInfoDao();
		return dao.getUnitInfoByKey(unitId);
	}

	public UnitInfoBean getUnitInfoByCode(String unitCode) {
		UnitInfoDao dao = new UnitInfoDao();
		return dao.getUnitInfoByCode(unitCode);
	}

	public List<UnitInfoBean> getUnitInfoByTypeId(int typeId) {
		UnitInfoDao dao = new UnitInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("typeId", String.valueOf(typeId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getUnitInfoListByMap(condMap);
	}

	public List<UnitInfoBean> getUnitInfoLsit() {
		UnitInfoDao dao = new UnitInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getUnitInfoListByMap(condMap);
	}

	public PageResultBean<UnitInfoBean> getUnitInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		UnitInfoDao dao = new UnitInfoDao();
		return dao.getUnitInfoPageListByMap(condMap, pageBean);
	}

	public ReturnMessageBean verifyUnitInfoProperty(UnitInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getUnitCode())) {
			result.setMessage("单位信息代码必须填写");
		} else if (isDuplicateUnitCode(bean.getUnitId(), bean.getUnitCode())) {
			result.setMessage("已存在相同的单位信息代码");
		} else if (!isValidUnitCode(bean.getUnitCode())) {
			result.setMessage("单位信息代码格式不符合规范");
		} else if (!StringKit.isValid(bean.getUnitName())) {
			result.setMessage("单位信息名称必须填写");
		} else if (bean.getUnitName().length() > 30) {
			result.setMessage("单位信息名称不能大于30字");
		}
		return result;
	}

	public boolean isDuplicateUnitCode(int unitId, String unitCode) {
		UnitInfoBean bean = getUnitInfoByCode(unitCode);
		if (bean == null) {
			return false;
		} else {
			if (unitId > 0 && unitId == bean.getUnitId()) {
				return false;
			}
		}
		return true;
	}

	public boolean isValidUnitCode(String unitCode) {
		if (!unitCode.matches("^[a-zA-Z0-9_]+$")) {
			return false;
		}
		if (unitCode.length() > 10) {
			return false;
		}
		return true;
	}

}